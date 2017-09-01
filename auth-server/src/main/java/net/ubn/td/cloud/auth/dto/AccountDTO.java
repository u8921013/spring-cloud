package net.ubn.td.cloud.auth.dto;

public class AccountDTO {
	
	private String studentNumber;
	private String password;
	private String className;
	
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
	@Override
	public String toString() {
		return "AccountDTO [studentNumber=" + studentNumber + ", password=" + password + ", className=" + className
				+ "]";
	}
	
	
}
