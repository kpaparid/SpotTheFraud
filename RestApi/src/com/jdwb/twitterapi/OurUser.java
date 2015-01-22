package com.jdwb.twitterapi;

import java.util.ArrayList;

public class OurUser {

	private Long id;
	private Integer References;
	private ArrayList<String> referencedTrends = new ArrayList<String>();	
	
	public OurUser (Long id)
	{
		this.id = id;
	}
			
	public Long getId() {
		return id;
	}
	
	public Integer getNumOfReferences() {
		return References;
	}
	
	public ArrayList<String> getList()
	{
		return referencedTrends;
	}
	
	public void AddReferences(){
		References++;
	}
	
}
