package com.jdwb.twitterapi;

public class Values {

	private String id;
	private String text;
	
	
	public Long getId() {
		return Long.parseLong(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
