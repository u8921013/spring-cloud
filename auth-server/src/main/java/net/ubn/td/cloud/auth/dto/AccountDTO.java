package net.ubn.td.cloud.auth.dto;

public class AccountDTO {
	
	private String studentNumber;
	private String password;
	private String classname;
	
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
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	@Override
	public String toString() {
		return "AccountDTO [studentNumber=" + studentNumber + ", password=" + password + ", classname=" + classname
				+ "]";
	}
	
	
}
