package net.ubn.td.cloud.auth.dto;

/**
 * @author CSLin
 *
 *	2017-10-30 新增加 groupName屬性
 */
public class RoomDTO {
	private String id;
	private String groupName;
	private String name;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
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
}
