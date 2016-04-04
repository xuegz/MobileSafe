package com.example.bean;

/*
 * 黑名单
 */
public class BlackNumber {
	private String number;//号码
	private String mode;//拦截模式

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
