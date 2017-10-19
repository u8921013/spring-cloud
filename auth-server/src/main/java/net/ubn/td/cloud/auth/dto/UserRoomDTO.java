package net.ubn.td.cloud.auth.dto;

import java.util.List;

public class UserRoomDTO {
	private String studentNumber;
	private List<ReturnRoomDTO> rooms;
	
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	 
	public List<ReturnRoomDTO> getRooms() {
		return rooms;
	}
	public void setRooms(List<ReturnRoomDTO> rooms) {
		this.rooms = rooms;
	}
}
