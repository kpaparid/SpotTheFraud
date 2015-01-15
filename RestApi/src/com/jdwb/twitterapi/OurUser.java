package com.jdwb.twitterapi;

public class OurUser {

	private Long id;
	private Integer numOfTweets;
	
	
	public OurUser (Long id, Integer num)
	{
		this.id = id;
		numOfTweets = num;
	}
			
	public Long getId() {
		return id;
	}
	public Integer getNumOfTweets() {
		return numOfTweets;
	}
	
}
