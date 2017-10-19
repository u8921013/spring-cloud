package net.ubn.td.cloud.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ReturnRoomDTO {
	
	private String id;
	private String name;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private List<UserDTO> members;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<UserDTO> getMembers() {
		return members;
	}

	public void setMembers(List<UserDTO> members) {
		this.members = members;
	}
}
