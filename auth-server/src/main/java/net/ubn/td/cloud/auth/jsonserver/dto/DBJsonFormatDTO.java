package net.ubn.td.cloud.auth.jsonserver.dto;

import java.util.List;

public class DBJsonFormatDTO {
	private List<JsonAccountDTO> users;
	private List<JsonRoomDTO> rooms;
	public List<JsonAccountDTO> getUsers() {
		return users;
	}
	public void setUsers(List<JsonAccountDTO> users) {
		this.users = users;
	}
	public List<JsonRoomDTO> getRooms() {
		return rooms;
	}
	public void setRooms(List<JsonRoomDTO> rooms) {
		this.rooms = rooms;
	}
	
}
