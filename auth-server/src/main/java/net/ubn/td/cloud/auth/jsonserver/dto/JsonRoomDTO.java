package net.ubn.td.cloud.auth.jsonserver.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class JsonRoomDTO {
	private String id;
	private String groupName;
	private String name;
	private String className;
	private List<String> members;
	// @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate startDate;

	// @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate endDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "JsonRoomDTO [id=" + id + ", groupName=" + groupName + ", name=" + name + ", className=" + className
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
