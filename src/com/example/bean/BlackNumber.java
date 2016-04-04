package com.example.bean;

public class BlackNumber {
	private String number;
	private String mode;

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public BlackNumber(String number, String mode) {
		super();
		this.number = number;
		this.mode = mode;
	}
	public BlackNumber() {
		super();
	}
	
}
