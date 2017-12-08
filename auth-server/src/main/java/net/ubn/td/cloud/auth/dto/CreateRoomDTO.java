package net.ubn.td.cloud.auth.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

public class CreateRoomDTO {

	private String className;
	private String groupName;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate endDate;

	private Map<String, List<String>> rooms;

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

}
