package net.ubn.td.cloud.auth.dto;

import java.util.List;
import java.util.Map;

public class CreateRoomDTO {
	
	private String name;
	private Map<String,List<String>> rooms;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, List<String>> getRooms() {
		return rooms;
	}
	public void setRooms(Map<String, List<String>> rooms) {
		this.rooms = rooms;
	}
	
	
	
}
