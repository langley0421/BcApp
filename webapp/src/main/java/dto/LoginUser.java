package dto;

import java.io.Serializable;

public class LoginUser implements Serializable {
	private String id; // ログイン時のID

	public String getId() {
		return id;
	}

	public void setUserId(String id) {
		this.id = id;
	}

	public LoginUser() {
		this(null);
	}

	public LoginUser(String id) {
		this.id = id;
	}
}
