package com.jdwb.twitterapi;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import twitter4j.FilterQuery;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class HttpGetter {

	public static void main(String[] args) throws UnknownHostException {

		 
		int counter = 0;
		Twitter twitter = new TwitterFactory().getInstance();

		twitter.setOAuthConsumer("HkcYUNlVLch6EiWHvjaKBQnvj",
				"xCAITsQyvZJQf0MkQ140t8Plt66EwaxZUrM8KQfYKAoPgSMYKw");
		twitter.setOAuthAccessToken(new AccessToken(
				"2864427430-P6wK4L6HypoeQZpYWtvNp3a3desDFHnTcm9Njy0",
				"m0PELMvI6kJxsPkeKmmp6oDgQGnC7BX40K6qeVjen6L41"));

		while(true) {
		   try {
			   counter++;
			   Trends trends = twitter.getPlaceTrends(1);
			   System.out.println("Wordlwide trends");
			   System.out.println(counter);
			   System.out.println("As of : " + trends.getAsOf());
			   for (Trend trend : trends.getTrends()) {
			   	System.out.println(trend.getName());
			   }
		   }
		   catch (TwitterException e1) {
		      	// TODO Auto-generated catch block
		     	e1.printStackTrace();
		   }
		   try {
			   Thread.sleep(300000);
		   }
		   catch(Exception e){
			   Thread.currentThread().interrupt();
		   }
			   
		}
/*
 * FilterQuery filter = new FilterQuery();

String[] keywords = {"#katsantekis","#dematis"};
filter.track(keywords);
StatusListener listener = new StatusListener() {
@Override
public void onStatus(Status status) {
System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
}
@Override
public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
}
@Override
public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
}
@Override
public void onScrubGeo(long userId, long upToStatusId) {
System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
}
@Override
public void onStallWarning(StallWarning warning) {
System.out.println("Got stall warning:" + warning);
}
@Override
public void onException(Exception ex) {
ex.printStackTrace();
}
};
twitterStream.addListener(listener);
twitterStream.filter(filter);
 * 
 * 
 * 
 */

		/*
		 MongoClient mongoClient = new MongoClient();
		 
		 
		 DB db = mongoClient.getDB("mydb");
		 
		 Set<String> colls = db.getCollectionNames();
		 for(String s : colls){
			 System.out.println(s);
		 }
		 DBCollection coll = db.getCollection("testCollection");
		 mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		 
		 BasicDBObject doc = new BasicDBObject("name", "MongoDB")
	        .append("type", "database")
	        .append("count", 1)
	        .append("info", new BasicDBObject("x", 203).append("y", 102));
		 
	coll.insert(doc);
		 
		 DBObject myDoc = coll.findOne();
		 System.out.println(myDoc);
		 */

	}

}
