package net.ubn.td.cloud.auth.dto;

import java.util.List;

public class RoomDTO {
	private String id;
	private String name;
	private List<String> accounts;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
	
	
}
