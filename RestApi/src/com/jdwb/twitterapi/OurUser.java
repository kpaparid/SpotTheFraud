package com.jdwb.twitterapi;

import java.util.ArrayList;

public class OurUser {

	private Long id;
	private Integer numOfTrendReferences;
	private ArrayList<String> referencedTrends = new ArrayList<String>();	
	
	public OurUser (Long id, Integer num)
	{
		this.id = id;
		numOfTrendReferences = num;
	}
			
	public Long getId() {
		return id;
	}
	
	public Integer getNumOfTweets() {
		return numOfTrendReferences;
	}
	
	public ArrayList<String> getList()
	{
		return referencedTrends;
	}
	
}
