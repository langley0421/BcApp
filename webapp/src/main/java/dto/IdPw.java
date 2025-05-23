package dto;

import java.io.Serializable;

public class IdPw implements Serializable {
	private String id; // ID
	private String pw; // パスワード

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public IdPw(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}

	public IdPw() {
		this.id = "";
		this.pw = "";
	}

}
