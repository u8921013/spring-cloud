package net.ubn.td.cloud.auth;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.ubn.td.cloud.auth.jsonserver.dto.AccountType;
import net.ubn.td.cloud.auth.jsonserver.dto.DBJsonFormatDTO;
import net.ubn.td.cloud.auth.jsonserver.dto.JsonAccountDTO;

public class DBJsonFormat {
	public static void main(String[] args){
		try {
			String strDB=FileUtils.readFileToString(new File("/Users/Lendy/Downloads/studentList2.txt"),"UTF-8");
			strDB=strDB.replaceAll("'", "\"");
//			System.out.println("db="+strDB);
			ObjectMapper mapper=new ObjectMapper();
			DBJsonFormatDTO oldDTO=mapper.readValue(new File("/Users/Lendy/Desktop/20170814/readium-js-viewer/dist/db.json"), DBJsonFormatDTO.class);
			List<JsonAccountDTO> myObjects = mapper.readValue(strDB, new TypeReference<List<JsonAccountDTO>>(){});
//			System.out.println("myObjects:"+myObjects);
			
			List<JsonAccountDTO> newObjectList=myObjects.stream().map(orginAccountDTO->{
				orginAccountDTO.setId(orginAccountDTO.getStudentNumber());
				orginAccountDTO.setType(orginAccountDTO.getStudentNumber().startsWith("teacher")?AccountType.teacher:AccountType.student);
				orginAccountDTO.setImg("http://120.125.83.31/chatroom/img/mem-icon.jpg");
				
				return orginAccountDTO;
			}).collect(Collectors.toList());
			
			
			DBJsonFormatDTO dbJsonDTO=new DBJsonFormatDTO();
			dbJsonDTO.setUsers(newObjectList);
			dbJsonDTO.setRooms(oldDTO.getRooms());
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("/Users/Lendy/Desktop/20170814/db.json"),dbJsonDTO);
//			System.out.println(str);
			
		} catch (Exception e) {
		}
		
	}
}
