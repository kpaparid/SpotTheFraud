package com.jdwb.twitterapi;

import java.util.Date;

public class UserDetails {

	private Long id;
	private int followers_num;
	private int followees_num;
	private double ratio;
	private long age; // counts age in days current_date-get_created_at()
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getFollowers_num() {
		return followers_num;
	}
	public void setFollowers_num(int followers_num) {
		this.followers_num = followers_num;
	}
	public int getFollowees_num() {
		return followees_num;
	}
	public void setFollowees_num(int followees_num) {
		this.followees_num = followees_num;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	
	public void calculateRatio()
	{
		ratio = followers_num/followees_num;
	}
	
	public void calculateAge(Date created)
	{
		Date dt = new Date();	
		age = dt.getTime() - created.getTime();
	}
	
	
	
	
}
