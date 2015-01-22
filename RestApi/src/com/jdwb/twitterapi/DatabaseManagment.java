package com.jdwb.twitterapi;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class DatabaseManagment {
	
	private DB db;
	DBCursor cursor;
	
	
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
		db.createCollection("Monitor", null);
		
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
	
	
	
	
	public ArrayList<String> getAllTrends()
	{
		
		ArrayList<String> getRekt = new ArrayList<String>();
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );
		DBCollection coll = db.getCollection("TrendsCollection");
		
		DBCursor trendcursor = coll.find();
		
		try {
		   while(trendcursor.hasNext()) {
		       getRekt.add(trendcursor.next().get("name").toString());
		   }
		} finally {
			trendcursor.close();
		}
		
		return getRekt;
	}
	
	
	
	
	
	
	public void addTweet(String json, String currCol)
	{
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );
		
		DBCollection coll = db.getCollection(currCol);
		DBObject dbObject = (DBObject)JSON.parse(json);
		 
		coll.insert(dbObject);
			
	
	}
	
	
	Values getItemsFromDatabase()
	{
		Values val = new Values();
		MongoClient mongoClient = null;
		
		try {
			mongoClient = new MongoClient( "localhost" );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = mongoClient.getDB( "twitterDB" );	
		DBCollection coll = db.getCollection("TweetsCollection");
		if(cursor == null)
		{
			cursor = coll.find();
		}
		DBObject obj1 = (DBObject) cursor.next();
		DBObject obj2 = (DBObject) cursor.next().get("user");
		
		
		System.out.println("Hello! :" + cursor.next());
		String id = obj2.get("id").toString();
		String text = obj1.get("text").toString();
		
		val.setId(id);
		val.setText(text);
		
		if(!cursor.hasNext())
		{
			cursor.close();
			return  null;
		}

		return  val;

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
