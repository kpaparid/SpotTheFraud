package com.jdwb.twitterapi;

public class User {

	private Long id;
	private Integer numOfTweets;
	
	
	public User (Long id, Integer num)
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
