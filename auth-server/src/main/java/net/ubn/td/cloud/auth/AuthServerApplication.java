package net.ubn.td.cloud.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ubn.td.cloud.auth.dto.*;
import net.ubn.td.cloud.auth.jsonserver.dto.AccountType;
import net.ubn.td.cloud.auth.jsonserver.dto.JsonAccountDTO;
import net.ubn.td.cloud.auth.jsonserver.dto.JsonRoomDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableAuthorizationServer
@RestController
public class AuthServerApplication {

	Logger logger = LoggerFactory.getLogger(AuthServerApplication.class);

	private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

	@Value("${jsonserver.domain}")
	private String json_server_domain;

	@Value("${store.image.path}")
	private String image_storepath;

	@Value("${store.image.domainname}")
	private String image_domain;

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	/**
	 * Return the principal identifying the logged in user
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/me")
	public Principal getCurrentLoggedInUser(Principal user) {
		logger.debug("class:"+user.getClass());
		CustomUsernamePasswordAuthenticationToken userAuthentication=(CustomUsernamePasswordAuthenticationToken)((OAuth2Authentication)user).getUserAuthentication();
		logger.debug("className:"+userAuthentication.getClassName());
		logger.debug("accountType:{}",userAuthentication.getType().name());
		return user;
	}

	@RequestMapping("/userInfo")
	public ReturnAccountDTO getUserInfo(Principal user) {

		JsonAccountDTO jsonAccountDTO = getAccountDTO(user);

		ReturnAccountDTO returnDTO = new ReturnAccountDTO();
		returnDTO.setStudentNumber(jsonAccountDTO.getStudentNumber());
		returnDTO.setClassName(jsonAccountDTO.getClassName());
		returnDTO.setType(jsonAccountDTO.getType());
		returnDTO.setReadingTime(System.currentTimeMillis());
		return returnDTO;
	}

	@RequestMapping("/getRoomInfo/{roomId}")
	public ReturnRoomDTO getRoomInfo(Principal user, @PathVariable(value = "roomId") String roomId) {
		JsonRoomDTO jsonRoomDTO = restTemplate.getForObject("http://" + json_server_domain + "/rooms/" + roomId,
				JsonRoomDTO.class);

		ReturnRoomDTO returnDTO = new ReturnRoomDTO();
		returnDTO.setId(roomId);
		returnDTO.setGroupName(jsonRoomDTO.getGroupName());
		returnDTO.setName(jsonRoomDTO.getName());
		returnDTO.setMembers(new ArrayList<UserDTO>());
		returnDTO.setAnnouncement(jsonRoomDTO.getAnnouncement());

		String connectedStudentNumber = "studentNumber=" + String.join("&studentNumber=", jsonRoomDTO.getMembers());
		List<JsonAccountDTO> accountDTOList = restTemplate
				.exchange("http://" + json_server_domain + "/users?" + connectedStudentNumber, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<JsonAccountDTO>>() {
						})
				.getBody();

		for (JsonAccountDTO roomMember : accountDTOList) {
			UserDTO userDTO = new UserDTO();
			userDTO.setImg(roomMember.getImg());
			userDTO.setName(roomMember.getName());
			userDTO.setStudentNumber(roomMember.getStudentNumber());
			returnDTO.getMembers().add(userDTO);
		}
		return returnDTO;
	}

	private JsonAccountDTO getAccountDTO(Principal user) {
		CustomUsernamePasswordAuthenticationToken userAuthentication=(CustomUsernamePasswordAuthenticationToken)((OAuth2Authentication)user).getUserAuthentication();

		String url="http://" + json_server_domain + "/users?studentNumber=" + userAuthentication.getName()+"&className="+userAuthentication.getClassName();
		logger.info("request from url[{}]",url);
		List<JsonAccountDTO> accountDTOList = restTemplate
				.exchange(url, HttpMethod.GET,null, new ParameterizedTypeReference<List<JsonAccountDTO>>() {})
				.getBody();

		JsonAccountDTO accountDTO = accountDTOList.get(0);
		return accountDTO;
	}

	@RequestMapping(path = "/listClassmates/{className}", method = RequestMethod.GET)
	public List<ReturnAccountDTO> listClassmates(@PathVariable(value = "className") String className) {

		List<JsonAccountDTO> classmateDTOList = restTemplate
				.exchange("http://" + json_server_domain + "/users?className=" + className, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<JsonAccountDTO>>() {
						})
				.getBody();
		List<ReturnAccountDTO> returnDTOList = classmateDTOList.stream()
				.filter(o -> o.getType().equals(AccountType.student)).map(classmate -> {
					ReturnAccountDTO returnDTO = new ReturnAccountDTO();
					returnDTO.setId(classmate.getId());
					returnDTO.setStudentNumber(classmate.getStudentNumber());
					returnDTO.setPassword(classmate.getPassword());
					returnDTO.setClassName(classmate.getClassName());
					returnDTO.setName(classmate.getName());
					returnDTO.setImg(classmate.getImg());
					return returnDTO;
				}).collect(Collectors.toList());
		return returnDTOList;
	}

	@RequestMapping(path = "/findClassmate/{stuentNumber}", method = RequestMethod.GET)
	public ReturnAccountDTO findClassmateByStuentNumber(@PathVariable("stuentNumber") String strStuentNumber) {

		List<JsonAccountDTO> accountDTOList = restTemplate
				.exchange("http://" + json_server_domain + "/users?studentNumber=" + strStuentNumber, HttpMethod.GET,
						null, new ParameterizedTypeReference<List<JsonAccountDTO>>() {
						})
				.getBody();

		JsonAccountDTO accountDTO = accountDTOList.get(0);

		ReturnAccountDTO returnDTO = new ReturnAccountDTO();
		returnDTO.setId(accountDTO.getId());
		returnDTO.setStudentNumber(accountDTO.getStudentNumber());
		returnDTO.setPassword(accountDTO.getPassword());
		returnDTO.setClassName(accountDTO.getClassName());
		returnDTO.setType(accountDTO.getType());
		returnDTO.setImg(accountDTO.getImg());
		return returnDTO;
	}

	@RequestMapping(path = "/createClassmate", method = RequestMethod.POST)
	public ReturnDTO createClassmate(@RequestBody RequestAccountDTO paramDTO) {
		logger.debug("[createClassmate] studentNumber={}", paramDTO.getStudentNumber());
		logger.debug("[createClassmate] password={}", paramDTO.getPassword());
		logger.debug("[createClassmate] name={}", paramDTO.getName());
		logger.debug("[createClassmate]  imageName={}", paramDTO.getImgName());
		// logger.debug("[createClassmate] image={}",paramDTO.getImg());

		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(paramDTO.getImg());
		String ext = paramDTO.getImgName().substring(paramDTO.getImgName().indexOf(".") + 1);
		String imageName = paramDTO.getStudentNumber() + "." + ext;
		try {
			File dictFile = new File(new File(image_storepath), paramDTO.getClassName());
			if (!dictFile.exists()) {
				dictFile.mkdirs();
			}
			File imageFile = new File(dictFile, imageName);
			logger.debug("write image file:{}", imageFile);
			FileUtils.writeByteArrayToFile(imageFile, imageBytes);
		} catch (IOException e1) {

		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		JsonAccountDTO jsonAccountDTO = new JsonAccountDTO();
		jsonAccountDTO.setStudentNumber(paramDTO.getStudentNumber());
		jsonAccountDTO.setPassword(paramDTO.getPassword());
		jsonAccountDTO.setName(paramDTO.getName());
		jsonAccountDTO.setClassName(paramDTO.getClassName());
		jsonAccountDTO.setType(AccountType.student);

		jsonAccountDTO.setImg(image_domain + paramDTO.getClassName() + "/" + imageName);
		// Jackson ObjectMapper to convert requestBody to JSON
		ReturnDTO returnDTO = new ReturnDTO();
		String json;
		try {
			json = new ObjectMapper().writeValueAsString(jsonAccountDTO);
			logger.debug("json=" + json);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			String response = restTemplate
					.postForEntity("http://" + json_server_domain + "/users", entity, String.class).getBody();
			logger.debug("response:" + response);
			returnDTO.setCode(0);
			returnDTO.setMessage("success");
		} catch (Exception e) {
			returnDTO.setCode(-1);
			returnDTO.setMessage("Fail");
		}

		return returnDTO;
	}

	@RequestMapping(path = "/deleteClassmate/{id}", method = RequestMethod.POST)
	public ReturnDTO deleteClassmate(@PathVariable(name = "id") String id) {
		logger.debug("deleteClassmate id={}", id);
		ReturnDTO returnDTO = new ReturnDTO();
		try {
			String url = "http://" + json_server_domain + "/users/" + id;
			logger.debug("deleteClassmate url={}", url);
			restTemplate.delete(url);
			returnDTO.setCode(0);
			returnDTO.setMessage("success");
		} catch (Exception e) {
			returnDTO.setCode(-1);
			returnDTO.setMessage("Fail");
		}
		return returnDTO;
	}

	@RequestMapping(path = "/updateClassmate", method = RequestMethod.POST)
	public ReturnDTO updataClassmate(@RequestBody RequestAccountDTO paramDTO) {
		logger.debug("[updataClassmate] studentNumber={}", paramDTO.getStudentNumber());
		logger.debug("[updataClassmate] password={}", paramDTO.getPassword());

		String url = "http://" + json_server_domain + "/users/" + paramDTO.getId();
		logger.debug("[updataClassmate] url={}", url);
		JsonAccountDTO jsonAccountDTO = restTemplate.getForObject(url, JsonAccountDTO.class);

		if (!StringUtils.isEmpty(paramDTO.getImg())) {
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(paramDTO.getImg());
			String ext = paramDTO.getImgName().substring(paramDTO.getImgName().indexOf(".") + 1);

			String strNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			String imageName = paramDTO.getStudentNumber() + strNow + "." + ext;
			try {
				File dictFile = new File(new File(image_storepath), paramDTO.getClassName());
				if (!dictFile.exists()) {
					dictFile.mkdirs();
				}
				File imageFile = new File(dictFile, imageName);
				FileUtils.writeByteArrayToFile(imageFile, imageBytes);
				jsonAccountDTO.setImg(image_domain + paramDTO.getClassName() + "/" + imageName);
			} catch (IOException e1) {
				logger.error("apple", e1);
			}
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		jsonAccountDTO.setStudentNumber(paramDTO.getStudentNumber());
		jsonAccountDTO.setPassword(paramDTO.getPassword());
		jsonAccountDTO.setName(paramDTO.getName());
		jsonAccountDTO.setType(AccountType.student);
		ReturnDTO returnDTO = new ReturnDTO();

		String json;
		try {
			json = new ObjectMapper().writeValueAsString(jsonAccountDTO);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);

			String response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
			logger.debug(response);
			returnDTO.setCode(0);
			returnDTO.setMessage("success");
		} catch (Exception e) {
			returnDTO.setCode(-1);
			returnDTO.setMessage("Fail");
		}

		return returnDTO;
	}

	@RequestMapping("/getRoomHeader/{className}")
	public List<UserRoomHeaderDTO> getRoomHeader(@PathVariable(value = "className") String className) {
		String url = "http://" + json_server_domain + "/rooms?className=" + className;
		logger.debug("[getRoomHeader] url:{} ", url);
		// rooms清單
		List<JsonRoomDTO> jsonRoomDTOList = restTemplate
				.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonRoomDTO>>() {
				}).getBody();

		List<UserRoomHeaderDTO> returnList=new ArrayList<>();
		List<String> lst = new ArrayList<>();
		jsonRoomDTOList.stream().forEach(jsonRoomDTO -> {
			String strGroupName = jsonRoomDTO.getGroupName();
			// String strName = jsonRoomDTO.getName();
			if (!lst.contains(strGroupName)) {
				lst.add(strGroupName);
				UserRoomHeaderDTO newDTO=new UserRoomHeaderDTO();
				newDTO.setGroupName(strGroupName);
				newDTO.setStartDate(jsonRoomDTO.getStartDate());
				newDTO.setEndDate(jsonRoomDTO.getEndDate());
				returnList.add(newDTO);
			}
		});
		logger.debug("getRoomHeader({}) result:{}", className, lst);
		return returnList;
	}
	
	
	@RequestMapping(path = "/updateRoomAnnouncement/{roomId}", method = RequestMethod.POST)
	public ReturnDTO updateRoomAnnouncement(@PathVariable("roomId") String roomId, @RequestBody UpdateRoomAnnouncementDTO announcementDTO){
		logger.debug("[updateRoomAnnouncement] roomId={},announcement={}", roomId,announcementDTO.getAnnouncement());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		
		try {
			String url="http://" + json_server_domain + "/rooms/"+roomId;
			logger.debug("[updateRoomAnnouncement] url={}", url);
			JsonRoomDTO jsonRoomDTO=restTemplate.getForEntity(url, JsonRoomDTO.class).getBody();
			
			jsonRoomDTO.setAnnouncement(announcementDTO.getAnnouncement());
			
			String json = new ObjectMapper().writeValueAsString(jsonRoomDTO);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			String response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
			logger.debug("response:" + response);
			

			ReturnDTO returnDTO=new ReturnDTO();
			returnDTO.setCode(0);
			returnDTO.setMessage("success");
			return returnDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			ReturnDTO returnDTO=new ReturnDTO();
			returnDTO.setCode(-1);
			returnDTO.setMessage(e.getMessage());
			return returnDTO;
		}
	}
	
	@RequestMapping("/getRoomData/{className}")
	public List<UserRoomDTO> getRoomData(@PathVariable(value = "className") String className) {

		ReturnFriendDTO returnDTO = new ReturnFriendDTO();
		logger.debug("[getRoomData] strClassName={}", className);

		// 同學資料
		List<JsonAccountDTO> classmateDTOList = restTemplate
				.exchange("http://" + json_server_domain + "/users?className=" + className, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<JsonAccountDTO>>() {
						})
				.getBody();
		returnDTO.setFriends(new ArrayList<FriendDTO>());
		List<UserRoomDTO> dataList = classmateDTOList.stream()
				.filter(classmate -> classmate.getType().equals(AccountType.student)).map(classmate -> {
					logger.debug("name={} type()={}", classmate.getStudentNumber(), classmate.getType());
					List<JsonRoomDTO> rooms = restTemplate
							.exchange("http://" + json_server_domain + "/rooms?q=" + classmate.getStudentNumber(),
									HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonRoomDTO>>() {
									})
							.getBody();
					List<ReturnRoomDTO> roomIds = rooms.stream()
							.filter(room -> room.getMembers().contains(classmate.getStudentNumber())).map(room -> {
								ReturnRoomDTO returnRoomDTO = new ReturnRoomDTO();
								returnRoomDTO.setId(room.getId());
								returnRoomDTO.setGroupName(room.getGroupName());
								returnRoomDTO.setName(room.getName());
								return returnRoomDTO;
							}).collect(Collectors.toList());
					UserRoomDTO userRoomDTO = new UserRoomDTO();
					userRoomDTO.setStudentNumber(classmate.getStudentNumber());
					userRoomDTO.setRooms(roomIds);
					return userRoomDTO;
				}).collect(Collectors.toList());

		return dataList;
	}

	@RequestMapping("/getUserInfo")
	public ReturnUserDTO getLoginUserData(Principal user) {
		CustomUsernamePasswordAuthenticationToken userAuthentication=(CustomUsernamePasswordAuthenticationToken)((OAuth2Authentication)user).getUserAuthentication();

		ReturnUserDTO returnDTO = new ReturnUserDTO();

		JsonAccountDTO accountDTO = getAccountDTO(user);

		returnDTO.setStudentNumber(accountDTO.getStudentNumber());
		returnDTO.setClassname(accountDTO.getClassName());
		returnDTO.setName(accountDTO.getName());
		returnDTO.setImg(accountDTO.getImg());

		// 同學資料
		List<JsonAccountDTO> classmateDTOList = restTemplate
				.exchange("http://" + json_server_domain + "/users?className=" + accountDTO.getClassName(),
						HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonAccountDTO>>() {
						})
				.getBody();
		returnDTO.setFriends(new ArrayList<FriendDTO>());
		
        List<FriendDTO> friendsDTOList =null;
        switch (accountDTO.getType()){
            case teacher:
            	System.out.println("1");
                friendsDTOList = classmateDTOList.stream()
                        .filter(classmate -> !accountDTO.getStudentNumber().equals(classmate.getStudentNumber())).map(classmate -> {
                            FriendDTO dto = new FriendDTO();
                            dto.setStudentNumber(classmate.getStudentNumber());
                            dto.setName(classmate.getName());
                            dto.setImg(classmate.getImg());
                            return dto;
                        }).collect(Collectors.toList());
                break;
            case student:
                friendsDTOList = classmateDTOList.stream()
                        .filter(classmate -> !accountDTO.getStudentNumber().equals(classmate.getStudentNumber())&&classmate.getType().equals(AccountType.teacher)).map(classmate -> {
                            FriendDTO dto = new FriendDTO();
                            dto.setStudentNumber(classmate.getStudentNumber());
                            dto.setName(classmate.getName());
                            dto.setImg(classmate.getImg());
                            return dto;
						}).collect(Collectors.toList());
                break;

        }
        returnDTO.setFriends(friendsDTOList);

		// 個人清單
		ResponseEntity<List<JsonRoomDTO>> roomDTOListResponse = restTemplate.exchange(
				"http://" + json_server_domain + "/rooms?q=" + user.getName(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<JsonRoomDTO>>() {
				});
		List<JsonRoomDTO> rooms = roomDTOListResponse.getBody();
		List<RoomDTO> roomList = rooms.stream()
						.filter(room->room.getClassName().equals(userAuthentication.getClassName()))  //增加要判斷班級
						.filter(room->room.getEndDate()==null)
						.map(jsonRoomDTO -> {
							RoomDTO roomDTO = new RoomDTO();roomDTO.setId(jsonRoomDTO.getId());
							roomDTO.setGroupName(jsonRoomDTO.getGroupName());
							roomDTO.setName(jsonRoomDTO.getName());
							return roomDTO;
						}).collect(Collectors.toList());
		returnDTO.setRooms(roomList);
		return returnDTO;
	}



	@RequestMapping(path = "/suspendRoom", method = RequestMethod.POST)
	public ReturnDTO suspendRoom(@RequestBody SuspendRoomDTO suspendRoomDTO) {
		logger.debug("[suspendRoom] getClassName={} ,getGroupName={}", suspendRoomDTO.getClassName(),
				suspendRoomDTO.getGroupName());
		try {
			List<JsonRoomDTO> roomList=restTemplate.exchange(
					"http://" + json_server_domain + "/rooms?className=" + suspendRoomDTO.getClassName()+"&groupName="+suspendRoomDTO.getGroupName(),
							HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonRoomDTO>>() {
							}).getBody();
			
			
			logger.debug("roomList={}", roomList);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			
			roomList.stream().forEach(room->{
				room.setEndDate(LocalDate.now());
				String json;
				try {
					json = new ObjectMapper().writeValueAsString(room);
					HttpEntity<String> entity = new HttpEntity<>(json, headers);
					String response =restTemplate.exchange("http://" + json_server_domain + "/rooms/"+room.getId(), HttpMethod.PUT, entity, String.class).getBody();
					
					logger.debug("response:" + response);
				} catch (JsonProcessingException e) {

				}
				
			});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReturnDTO returnDTO = new ReturnDTO();
		returnDTO.setCode(0);
		returnDTO.setMessage("success");

		return returnDTO;
	}

	@RequestMapping(path = "/createRoom", method = RequestMethod.POST)
	public ReturnDTO createRoom(@RequestBody CreateRoomDTO createRoomDTO) {
		logger.debug("[createRoomDTO] getClassName={} ,getGroupName={}", createRoomDTO.getClassName(),
				createRoomDTO.getGroupName());
		logger.debug("[createRoomDTO] rooms={}", createRoomDTO.getRooms());
		logger.debug("[createRoomDTO] startDate={},endDate={}", createRoomDTO.getStartDate(),
				createRoomDTO.getEndDate());
		try {
			List<String> teacherList = restTemplate
					.exchange("http://" + json_server_domain + "/users?className=" + createRoomDTO.getClassName(),
							HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonAccountDTO>>() {
							})
					.getBody().stream().filter(o -> o.getType().equals(AccountType.teacher))
					.map(o -> o.getStudentNumber()).collect(Collectors.toList());
			logger.debug("teacherList={}", teacherList);
			createRoomDTO.getRooms().keySet().stream().forEach(roomIndex -> {
				List<String> members = createRoomDTO.getRooms().get(roomIndex);
				members.addAll(teacherList);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

				JsonRoomDTO jsonRoomDTO = new JsonRoomDTO();
				jsonRoomDTO.setClassName(createRoomDTO.getClassName());
				jsonRoomDTO.setGroupName(createRoomDTO.getGroupName());
				jsonRoomDTO.setName("第" + roomIndex + "組");
				jsonRoomDTO.setStartDate(createRoomDTO.getStartDate());
				jsonRoomDTO.setEndDate(createRoomDTO.getEndDate());
				jsonRoomDTO.setMembers(members);
				// Jackson ObjectMapper to convert requestBody to JSON
				String json;
				try {
					json = new ObjectMapper().writeValueAsString(jsonRoomDTO);
					HttpEntity<String> entity = new HttpEntity<>(json, headers);
					String response = restTemplate
							.postForEntity("http://" + json_server_domain + "/rooms", entity, String.class).getBody();
					logger.debug("response:" + response);
				} catch (JsonProcessingException e) {

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ReturnDTO returnDTO = new ReturnDTO();
		returnDTO.setCode(0);
		returnDTO.setMessage("success");

		return returnDTO;
	}
}
