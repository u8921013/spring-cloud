package net.ubn.td.cloud.auth;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.ubn.td.cloud.auth.dto.AccountDTO;
import net.ubn.td.cloud.auth.dto.BookInfo;
import net.ubn.td.cloud.auth.dto.ReturnAccountDTO;
import net.ubn.td.cloud.auth.dto.ReturnFriendDTO;

@SpringBootApplication
@EnableAuthorizationServer
@RestController
public class AuthServerApplication {
	
	 @Autowired
	 private Environment env;
	 
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
	
	/**
	 * Return the principal identifying the logged in user
	 * @param user
	 * @return
	 */
	@RequestMapping("/me")
	public Principal getCurrentLoggedInUser(Principal user) {
		return user;
	}
	
	@RequestMapping("/userInfo")
	public ReturnAccountDTO getUserInfo(Principal user) {
		System.out.println(user.getName());
		AccountDTO accountDTO=userInfos().get(user.getName());
		ReturnAccountDTO returnDTO=new ReturnAccountDTO();
		returnDTO.setStudentNumber(accountDTO.getStudentNumber());
		returnDTO.setClassName(accountDTO.getClassName());
		returnDTO.setReadingTime(System.currentTimeMillis());
		return returnDTO;
//		return user;
	}
	
	@RequestMapping("/getFriends")
	public ReturnFriendDTO getFriends(Principal user) {
		AccountDTO accountDTO=userInfos().get(user.getName());
		
				 
		
	   //假設狀況
	   //cs有三個朋友 min 1234 kevin
	   //kevin有二個朋友 1234 cs 
	   //min有1個朋友 cs
       //1234有2個朋友 cs kevin				           
				 
		ReturnFriendDTO returnDTO=new ReturnFriendDTO();
		returnDTO.setStudentNumber(accountDTO.getStudentNumber());
		switch(accountDTO.getStudentNumber()){
		case "cs":
			returnDTO.setFriends(Arrays.asList(new String[]{"kevin","min","1234"}));
			break;
		case "kevin":
			returnDTO.setFriends(Arrays.asList(new String[]{"1234","cs"}));
			break;
		case "min":
			returnDTO.setFriends(Arrays.asList(new String[]{"cs"}));
			break;
		case "1234":
			returnDTO.setFriends(Arrays.asList(new String[]{"kevin","cs"}));
			break;
		}
		
		
		
		return returnDTO;
//		return user;
	}
	
	@RequestMapping("/bookInfo/{classname}")
	public List<BookInfo> getUserInfo(@PathVariable(value="classname") String classname) {
		System.out.println("classname="+classname);
		List<BookInfo> reusltList=new ArrayList<BookInfo>();
		
		BookInfo bookInfo1=new BookInfo();
		bookInfo1.setId("BOK08");
		bookInfo1.setName("Ch3-物件導向的程式設計思維");
		bookInfo1.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK08&bookNo=BOK08");
		reusltList.add(bookInfo1);
		
		BookInfo bookInfo2=new BookInfo();
		bookInfo2.setId("BOK09");
		bookInfo2.setName("Ch6-認識參考型別與操作物件");
		bookInfo2.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK09&bookNo=BOK09");
		reusltList.add(bookInfo2);
		
		BookInfo bookInfo3=new BookInfo();
		bookInfo3.setId("BOK10");
		bookInfo3.setName("Ch6-測驗一");
		bookInfo3.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK10&bookNo=BOK10");
		reusltList.add(bookInfo3);
		
		BookInfo bookInfo4=new BookInfo();
		bookInfo4.setId("BOK11");
		bookInfo4.setName("Ch6-測驗二");
		bookInfo4.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK11&bookNo=BOK11");
		reusltList.add(bookInfo4);
		
		BookInfo bookInfo5=new BookInfo();
		bookInfo5.setId("BOK12");
		bookInfo5.setName("Ch10-使用 Method 和 Method Overloading");
		bookInfo5.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK12&bookNo=BOK12");
		reusltList.add(bookInfo5);
		
		BookInfo bookInfo6=new BookInfo();
		bookInfo6.setId("BOK13");
		bookInfo6.setName("Ch10-測驗一");
		bookInfo6.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK13&bookNo=BOK13");
		reusltList.add(bookInfo6);
		
		BookInfo bookInfo7=new BookInfo();
		bookInfo7.setId("BOK14");
		bookInfo7.setName("Ch10-測驗二<");
		bookInfo7.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK14&bookNo=BOK14");
		reusltList.add(bookInfo7);
		
		
		BookInfo bookInfo8=new BookInfo();
		bookInfo8.setId("BOK15");
		bookInfo8.setName("Ch11-使用封裝和建構子");
		bookInfo8.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK15&bookNo=BOK15");
		reusltList.add(bookInfo8);
		
		BookInfo bookInfo9=new BookInfo();
		bookInfo9.setId("BOK16");
		bookInfo9.setName("Ch11-測驗一");
		bookInfo9.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK16&bookNo=BOK16");
		reusltList.add(bookInfo9);
		
		BookInfo bookInfo10=new BookInfo();
		bookInfo10.setId("BOK17");
		bookInfo10.setName("Ch11-測驗二");
		bookInfo10.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK17&bookNo=BOK17");
		reusltList.add(bookInfo10);
		
		BookInfo bookInfo18=new BookInfo();
		bookInfo18.setId("BOK18");
		bookInfo18.setName("CH12-進階物件導向程式設計");
		bookInfo18.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK18&bookNo=BOK18");
		reusltList.add(bookInfo18);
		
		BookInfo bookInfo19=new BookInfo();
		bookInfo19.setId("BOK19");
		bookInfo19.setName("CH12-測驗");
		bookInfo19.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK19&bookNo=BOK19");
		reusltList.add(bookInfo19);
		
		BookInfo bookInfo20=new BookInfo();
		bookInfo20.setId("BOK20");
		bookInfo20.setName("Ch7-Java 集合架構與泛型");
		bookInfo20.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK20&bookNo=BOK20");
		reusltList.add(bookInfo20);
		
		
		BookInfo bookInfo21=new BookInfo();
		bookInfo21.setId("BOK21");
		bookInfo21.setName("Ch13-使用JDBC建立資料庫連線");
		bookInfo21.setUrl("http://120.125.83.32:8080/cloud-reader/index.html?epub=..%2Fepub_content%2FBOK21&bookNo=BOK21");
		reusltList.add(bookInfo21);
		
		return reusltList;
	}
	
	@Bean
	public Map<String,AccountDTO> userInfos(){
		try {
			String json = FileUtils.readFileToString(new File(env.getProperty("auth.data.path")), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			Map<String,AccountDTO> storeMap=new HashMap<>();
			AccountDTO[] dataList = mapper.readValue(json.replaceAll("'", "\""), AccountDTO[].class);
			for(AccountDTO data:dataList){
				System.out.println("data="+data.getStudentNumber());
				storeMap.put(data.getStudentNumber(), data);
			}
			return storeMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap<String,AccountDTO>();
		}
	}
}
