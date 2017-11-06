package net.ubn.td.cloud.auth.dto;

import net.ubn.td.cloud.auth.jsonserver.dto.AccountType;

public class ReturnAccountDTO {
	
	private String id;
	private String studentNumber;
	private String name;
	private String password;
	private String className;
	private AccountType type;
	private String img;
	private long readingTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public long getReadingTime() {
		return readingTime;
	}
	public void setReadingTime(long readingTime) {
		this.readingTime = readingTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
