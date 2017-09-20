package net.ubn.td.cloud.auth.dto;

import java.util.List;

public class ReturnFriendDTO {
	private String studentNumber;
	private List<String> friends;

	public String getStudentNumber() {
		return studentNumber;
	}
	
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
}
