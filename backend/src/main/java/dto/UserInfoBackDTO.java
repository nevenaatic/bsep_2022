package dto;

import com.example.demo.model.UserType;

public class UserInfoBackDTO {

	String email;
	UserType userType;
	
	public UserInfoBackDTO() {}

	public UserInfoBackDTO(String email, UserType userType) {
		super();
		this.email = email;
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
}
