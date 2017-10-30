package net.ubn.td.cloud.auth.dto;

import java.util.List;

public class ReturnUserDTO {
	private String studentNumber;
	private String classname;
	private String img;
	private List<FriendDTO> friends;
	private List<RoomDTO> rooms;
	
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public List<FriendDTO> getFriends() {
		return friends;
	}
	public void setFriends(List<FriendDTO> friends) {
		this.friends = friends;
	}
	public List<RoomDTO> getRooms() {
		return rooms;
	}
	public void setRooms(List<RoomDTO> rooms) {
		this.rooms = rooms;
	}



}
