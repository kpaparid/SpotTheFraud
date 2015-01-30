package com.jdwb.twitterapi;

public class Values {

	private String id;
	private String text;
	private String dummy; //user id when used for tweets
	
	
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
	
	public Long getDummy()
	{
		return Long.parseLong(dummy);
	}
	
	public void setDummy(String id)
	{
		dummy = id;
	}
}
