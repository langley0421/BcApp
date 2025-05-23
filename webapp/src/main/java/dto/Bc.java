package dto;

import java.io.Serializable;

public class Bc implements Serializable {
	private int number; 	// 番号
	private String name; 	// 氏名
	private String address; // 住所

	public Bc(int number, String name, String address) {
		this.number = number;
		this.name = name;
		this.address = address;
	}

	public Bc() {
		this.number = 0;
		this.name = "";
		this.address = "";
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
