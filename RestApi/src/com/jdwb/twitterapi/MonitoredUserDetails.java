package com.jdwb.twitterapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MonitoredUserDetails {

	
	private int tweets_num; // i
	private int retweets_num; // ii
	private int replies_num; // iii
	private int mentions_num; //iv
	private int retweets_to_user_num; //v
	private int mean; //vi
	private int hashtag_mean; //vi
	private double hashtag_ratio; //vii
	private double url_ratio; //vii
	private int hashtags;
	private int urls;
	private ArrayList<String> tweets = new ArrayList<String>();
	private int copies;
	private HashMap<String, Integer> sources = new HashMap<String, Integer>();
	private HashMap<String, Integer> expanded_urls = new HashMap<String, Integer>();
	private double youRL; // i diairesi sto teleutaio zitoumeno
	private HashMap<String, Integer> domains = new HashMap<String, Integer>();
	private double domain_ratio;
	
	
	
	public MonitoredUserDetails()
	{
		tweets_num = 0;
		retweets_num = 0;
		replies_num = 0;
		mentions_num = 0;
		retweets_to_user_num = 0;
		mean = 0;
		hashtag_mean = 0;
		hashtag_ratio = 0;
		url_ratio = 0;
		copies = 0;
		youRL = 0;
		domain_ratio = 0;
	}
	
	
	public int getTweets_num() {
		return tweets_num;
	}
	public void setTweets_num(int tweets_num) {
		this.tweets_num = tweets_num;
	}
	public int getRetweets_num() {
		return retweets_num;
	}
	public void setRetweets_num(int retweets_num) {
		this.retweets_num = retweets_num;
	}
	public int getReplies_num() {
		return replies_num;
	}
	public void setReplies_num(int replies_num) {
		this.replies_num = replies_num;
	}
	public int getMentions_num() {
		return mentions_num;
	}
	public void setMentions_num(int mentions_num) {
		this.mentions_num = mentions_num;
	}
	public int getRetweets_to_user_num() {
		return retweets_to_user_num;
	}
	public void setRetweets_to_user_num(int retweets_to_user_num) {
		this.retweets_to_user_num = retweets_to_user_num;
	}
	public int getMean() {
		return mean;
	}
	public void setMean(int mean) {
		this.mean = mean;
	}
	public int getHastag_mean() {
		return hashtag_mean;
	}
	public void setHastag_mean(int hastag_mean) {
		this.hashtag_mean = hastag_mean;
	}
	public double getHashtag_ratio() {
		return hashtag_ratio;
	}
	public void setHashtag_ratio(double hashtag_ratio) {
		this.hashtag_ratio = hashtag_ratio;
	}
	public double getUrl_ratio() {
		return url_ratio;
	}
	public void setUrl_ratio(double url_ratio) {
		this.url_ratio = url_ratio;
	}
	public void increaseCopies(int c){
		copies += c;
	}
	
	
	
	
	
	
	
	public void increaseTweets_num()
	{
		tweets_num++;
	}
	
	public void increaseRetweets_num()
	{
		retweets_num++;
	}
	
	public void increaseReplies()
	{
		replies_num++;
	}
	
	public void increaseMentions(int num)
	{
		replies_num += num;;
	}
	
	public void increaseRetweetsToUser(int num)
	{
		retweets_to_user_num += num;;
	}
	
	public void increaseHashtags(int num)
	{
		hashtags += num;;
	}
	
	public void increaseURLs(int num)
	{
		urls += num;;
	}
	
	public void calculateMeanTweetsRetweets()
	{
		mean = retweets_to_user_num / (tweets_num + retweets_num); //??????????????????
	}
	
	public void calculateMeanHashtagsTweets()
	{
		hashtag_mean = hashtags / (tweets_num + retweets_num);
	}
	
	public void calculateHashtagRatio()
	{
		hashtag_ratio = hashtag_mean*100;
	}
	
	public void calculateURLsRatio()
	{
		url_ratio = (urls / tweets_num) * 100;
	}
	
	public void addToTweetsList(String text)
	{
		tweets.add(text);
	}
	
	public ArrayList<String> getTweetsList()
	{
		return tweets;
	}
	
	public HashMap<String, Integer> getSources()
	{
		return sources;
	}

	public void addSource(String src)
	{
		Integer temp = sources.get(src);
		
		if(temp == null)
		{
			sources.put(src, 1);
		}
		else
		{
			Integer i = temp + 1;
			sources.put(src, i);
		}
	}

	public int getNumOfSources()
	{
		return sources.size();
	}
	
	public void printSources()
	{
		
		System.out.println("New user sources.");
		Set<String> keys;
		keys = sources.keySet();
		
		for(String k: keys)
		{
			System.out.println("Source: " + k + "frequency: " + sources.get(k));
			
		}
	}
	
	public void addExpandedUrl(String url)
	{
		Integer temp = sources.get(url);
		
		if(temp == null)
		{
			sources.put(url, 1);
		}
		else
		{
			Integer i = temp + 1;
			sources.put(url, i);
		}
	}

	public void computeYOURLR()
	{
		Integer unique = 0;
		Integer all = 0;;
		
		Set<String> keys;
		keys = expanded_urls.keySet();
		
		for(String k: keys)
		{
			all = all + expanded_urls.get(k);
		}
		unique = expanded_urls.size();
		
		youRL = unique / all;
		
	}
	
	public void addDomain(String domain)
	{
		Integer temp = sources.get(domain);
		
		if(temp == null)
		{
			domains.put(domain, 1);
		}
		else
		{
			Integer i = temp + 1;
			domains.put(domain, i);
		}
	}

	public void computeDomains()
	{
		Integer unique = 0;
		Integer all = 0;;
		
		Set<String> keys;
		keys = domains.keySet();
		
		for(String k: keys)
		{
			all = all + domains.get(k);
		}
		unique = domains.size();
		
		domain_ratio = unique / all;
	}
	
	
}
