package net.ubn.td.cloud.auth.dto;

import java.util.List;
import java.util.Map;

public class CreateRoomDTO {
	
	private String className;
	private String groupName;
	private Map<String,List<String>> rooms;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Map<String, List<String>> getRooms() {
		return rooms;
	}
	public void setRooms(Map<String, List<String>> rooms) {
		this.rooms = rooms;
	}
}
