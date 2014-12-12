package com.jdwb.twitterapi;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class DatabaseManagment {
	
	private DB db;
	
	
	public DatabaseManagment()
	{
		
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Creating new Database and Collections...");
		
		db = mongoClient.getDB( "twitterDB" );
		
		db.createCollection("TrendsCollection", null);
		db.createCollection("TweetsCollection", null);
		
		System.out.println("Created Database and Collections successfully!");
		
		mongoClient.close();
	}
	
	
	public void addTrend(String tName, long sTime)
	{
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );
		
		DBCollection coll = db.getCollection("TrendsCollection");
		
		BasicDBObject toDB = new BasicDBObject("name", tName)
        .append("startTime",sTime)
        .append("deathTime", 0);
		coll.insert(toDB);
		
		mongoClient.close();
		
	}
	
	
	
	public void addTweet(String json)
	{
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );
		
		DBCollection coll = db.getCollection("TweetsCollection");
		DBObject dbObject = (DBObject)JSON.parse(json);
		 
		coll.insert(dbObject);
			
	
	}
	
	
	void printDatabase()
	{
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );	
		DBCollection coll = db.getCollection("TweetsCollection");
		
		DBCursor cursor = coll.find();
		
		System.out.println("empika");
		
		try {
			   while(cursor.hasNext()) {
			       System.out.println(cursor.next());
			   }
			} finally {
			   cursor.close();
			}
		
	}
	
	
	void addDethTime(String tName, long dTime)
	{
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );	
		DBCollection coll = db.getCollection("TrendsCollection");
		
		BasicDBObject query = new BasicDBObject("name", tName);
		BasicDBObject update = new BasicDBObject("deathTime", dTime);	
		
		coll.update(query, new BasicDBObject( "$set", update));
		
	}
	
	
	void dropDB()
	{
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );	
		db.dropDatabase();
	}
	
	public DB getDatabase()
	{
		return db;
	}

}
