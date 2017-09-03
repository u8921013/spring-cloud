package net.ubn.td.cloud.auth.dto;

public class ReturnAccountDTO {
	private String studentNumber;
	private String className;
	private long readingTime;
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public long getReadingTime() {
		return readingTime;
	}
	public void setReadingTime(long readingTime) {
		this.readingTime = readingTime;
	}
}
