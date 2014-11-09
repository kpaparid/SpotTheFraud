package com.jdwb.twitterapi;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
 


import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;


public class HttpGetter {

	public static void main(String[] args) throws UnknownHostException {

		
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		
		
		twitter.setOAuthConsumer("HkcYUNlVLch6EiWHvjaKBQnvj", "xCAITsQyvZJQf0MkQ140t8Plt66EwaxZUrM8KQfYKAoPgSMYKw");
		twitter.setOAuthAccessToken(new AccessToken("2864427430-P6wK4L6HypoeQZpYWtvNp3a3desDFHnTcm9Njy0", "m0PELMvI6kJxsPkeKmmp6oDgQGnC7BX40K6qeVjen6L41"));
		
		try {
			 Trends trends = twitter.getPlaceTrends(1);
			 System.out.println("Dem greek trends");
			 
			 System.out.println("As of : " + trends.getAsOf());
			 for (Trend trend : trends.getTrends()) {
			 System.out.println(trend);
			 
			 
			 }
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			
			ResponseList<Status> a = twitter.getHomeTimeline(new Paging(1, 5));
			
			for(Status b: a)
			{
				System.out.println(b.getText());
			}
			
		}catch(Exception e){
			System.out.println("Error!");
		}
		
		/*
		MongoClient mongoClient = new MongoClient();

		
		DB db = mongoClient.getDB("mydb");
	*/
		
	}


}
