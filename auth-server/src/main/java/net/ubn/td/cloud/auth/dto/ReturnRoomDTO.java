package net.ubn.td.cloud.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Lendy
 *  比roomDTO多加上 群組人員清單
 */
public class ReturnRoomDTO extends RoomDTO{

	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private List<UserDTO> members;
	
	
	public List<UserDTO> getMembers() {
		return members;
	}

	public void setMembers(List<UserDTO> members) {
		this.members = members;
	}
}
