package net.ubn.td.cloud.auth.dto;

public class FriendDTO {
	private String studentNumber;
	private String name;
	private String img;
	
	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	public static Builder createBuilder(String studentNumber,String name,String img){
		return new Builder(studentNumber,name,img);
	}
	public static class Builder {
		private FriendDTO friendDTO;
		Builder(String studentNumber,String name,String img){
			friendDTO=new FriendDTO();
			friendDTO.studentNumber=studentNumber;
			friendDTO.name=name;
			friendDTO.img=img;
		}
		
		public FriendDTO getDTO(){
			return friendDTO;
		}
	}
	
}
