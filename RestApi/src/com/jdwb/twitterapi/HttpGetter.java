package com.jdwb.twitterapi;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import twitter4j.FilterQuery;
import twitter4j.JSONException;
import twitter4j.JSONObject;
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
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

@SuppressWarnings("deprecation")
public class HttpGetter {
	
	static ArrayList<TrendStats> activeTrends;
	private static String currentCollection = "TweetsCollection"; // the collection that we save our data at each section of the program
	private static DatabaseManagment database = new DatabaseManagment(); // initialize the new database
	private static TwitterFactory tf;
	private static Twitter twitter;
	private static TwitterStream twitterStream;
	private static FilterQuery filter;
	private static HashMap<Long, OurUser> freqHash;
	private static HashMap<Long, OurUser> nonSuspendedUsers;
	private static long startingTime;
	
	

	public static void main(String[] args) throws UnknownHostException {

		
		establishConnectionAPI();
		firstPart();
		secondPart();
		finalPart();
}
	
	
	
	public static void establishConnectionAPI()
	{
		database.dropDB(); // delete any old contents of the database
		database = new DatabaseManagment(); // initialize again
		int counter = 0;

		
		// stuff to connect to the Twitter REST API
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("PITlssl6DKTBmMv8WPl2XUQZm")
				.setOAuthConsumerSecret(
						"0zqBzkJoov1sHCgGIwKchGv1NdaHL9vFY4kVGR4c5yZ0Soz4OY")
				.setOAuthAccessToken(
						"2864396627-EU9BPTAx0i8S1gmUMj1unQndjdZLKcBgp7WdC1e")
				.setOAuthAccessTokenSecret(
						"nVlqyDZIKg17xGWGSXA9UIFGK6eafUTamBWxmBWQl2b0I");

		// stuff to connect to the Twitter STREAMING API
		ConfigurationBuilder cb2 = new ConfigurationBuilder();
		cb2.setDebugEnabled(true)
				.setOAuthConsumerKey("PITlssl6DKTBmMv8WPl2XUQZm")
				.setOAuthConsumerSecret(
						"0zqBzkJoov1sHCgGIwKchGv1NdaHL9vFY4kVGR4c5yZ0Soz4OY")
				.setOAuthAccessToken(
						"2864396627-EU9BPTAx0i8S1gmUMj1unQndjdZLKcBgp7WdC1e")
				.setOAuthAccessTokenSecret(
						"nVlqyDZIKg17xGWGSXA9UIFGK6eafUTamBWxmBWQl2b0I");
		
		cb2.setJSONStoreEnabled(true);
		// end of configuration
		
		
		// initializing APIs
		tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance(); // rest api

		twitterStream = new TwitterStreamFactory(cb2.build()).getInstance(); // stream api
		
		
		filter = new FilterQuery(); // filter - it is used to filter the incoming tweets
		

		// Status listener - it is used to handle the incoming tweets from the streaming api
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {

				String rawJSON = DataObjectFactory.getRawJSON(status); // get json from a tweet
				database.addTweet(rawJSON, currentCollection); // adds the 			
			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
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
		//end of declaration of the listener
		
		twitterStream.addListener(listener); // adding listener to the api
	}
	
	
	public static void firstPart()
	{
		startingTime = System.currentTimeMillis();

		

		System.out.println("Starting the first marathon: 3 days (Collecting trends and tweets)!");
		// while runs for the desired time 
 		while (System.currentTimeMillis() <= (startingTime + 86400000)) {
 			
 		try {
 			long currentTime = System.currentTimeMillis();
 
 			Trends trends = twitter.getPlaceTrends(1); // gets the worldwide, top 10 trends
 
 
 			updateTrends(trends); // updates trends :  adds new trends, removes the expired trends

 
 			
 			String[] keywords = new String[activeTrends.size()]; // an array that holds the trends - to be passed in the filter of streaming api
 			
 			int kounter = 0;
 			
 			//just printing the trends every 5 mins and adding them to the database
 			System.out.println("Trends:");
 			for (int i = 0; i < activeTrends.size(); i++) {
 				
 				String trendName = activeTrends.get(i).getTrend().getName();
 				long time =  activeTrends.get(i).getStartTime();
 				keywords[kounter++] = trendName;
 				
 				database.addTrend(trendName, time); // adding the trend to the DB
 				System.out.println(i+1 + ")" + trendName);
 			}
 			
 
			twitterStream.cleanUp(); // cleans the streaming api's filter
			filter.track(keywords); // adds the new trends to the filter
			twitterStream.filter(filter); // starts the streaming api thread
			
			
			// wait for 5 minutes
			while (System.currentTimeMillis() <= (currentTime + 300000)) {

			}
			System.out.println("5 minutes have passed...");
			

		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
			
		   
	}
 	
 		System.out.println("Cleaning up and closing the stream...");
 		twitterStream.cleanUp(); // cleaning the stream
 		twitterStream.shutdown(); // close the streaming thread
	}

	
	public static void secondPart()
	{
		// Reading the users of the collected tweets and the tweets themselves

				// a hashmap that holds the frequency of referenced trends of every user 
				freqHash = new HashMap<Long, OurUser>(); // id of user for key, and OurUser for value (contains info about the user)
				
				ArrayList<String> allTrends = database.getAllTrends(); // gets all trends from the database to check if any of them exists in the users' tweets
				
				
				System.out.println("Creating the HashMap with the frequency of users...");
				
				while(true)
				{
					System.out.print("...");
					// getItemsFromDatabase(): returns tweets one by one
				    Values userTweet =  database.getItemsFromDatabase(); // it holds the ID of a user and his / her tweet text
				    if(userTweet == null){
				    	break;
				    }
				    Long userID = userTweet.getId(); // holds id of the user who posted the tweet
				    OurUser user = freqHash.get(userID); // gets the above user from the hashmap
				    // if the user does not exist in hashmap, then add him 
				    if(user == null){
				    	freqHash.put(userID, new OurUser(userID));
				    	user = freqHash.get(userID);
				    }
				    // every trend...
				    for(String temp: allTrends){
				    	// if one exists in the user's tweet
				    	if(userTweet.getText().contains(temp)){
				    		// if the trend is not already contained in the user's trends list then add it to the list and increase his references
				    		if(!user.getList().contains(temp)){
				    			user.getList().add(temp);
				    			user.AddReferences();
				    		}
				    	}
				    }
				    
				}
				
				
				
				System.out.println("Ended with the first HashMap");
				System.out.println("Building second HashMap with non suspended users...");
				System.out.println();
				
				
				// Looking Up for Suspended Users
				Set<Long> keyset = freqHash.keySet(); // creatin' a keyset, that holds every user id from the previous map - it is used to parse the map
				
				long[] lookForUsers = new long[freqHash.size()]; // creatin' an array of all the users ids
				
				int counter = 0;
				
				// add every id in the array
				for(Long key : keyset)
				{
					lookForUsers[counter] = key;
					counter ++;
				}
				
				int iterations = freqHash.size() / 100;
				
				if(freqHash.size() % 100 != 0)
				{
					iterations = iterations + 1;
				}

				
				// creating a new hashmap with the non suspended users
				nonSuspendedUsers = new HashMap<Long, OurUser>();
				
				// for every 100 users in the map call lookUpUser(); - it can handle only 100 users
				for(int i = 0; i < iterations; i++)
				{
					int jiterations = 100;
					if(i == iterations - 1)
					{
						jiterations = freqHash.size() % 100;
					}
					
					long[] tempUserIds = new long[jiterations]; // temp array that stores the 100 users to be searched every time
					
					for(int j = 0; j < jiterations; j++)
					{
						tempUserIds[j] = lookForUsers[j + (i * 100)];	
					}
					
					

					try {
						
						ResponseList<User> temp = twitter.lookupUsers(tempUserIds); // send 100 users in this function and get back the non suspended
						
						// for every user returned from the above fucntion, add the user to the new hash with the non suspended users
						for(User t: temp)
						{
							long id = t.getId();
							OurUser  user= freqHash.get(id);
							nonSuspendedUsers.put(id, user);
						}
						
						
					} catch (TwitterException e) {
						e.printStackTrace();
					}
					
					System.out.println("Ended with the second HashMap");

				}
				//endof finding suspended Users
				System.out.println("Ended with suspended users");

				
				ArrayList<OurUser> listOfUsers = sortByValues(nonSuspendedUsers);// pass the users from the hashmap
				// to an array list and sorting it by references frequency
				

				System.out.println("Old Size: " + freqHash.size() + " New hashmap size:" + nonSuspendedUsers.size());
				System.out.println(listOfUsers);
				
				//nonSuspendedUsers = null; // to idio me katw
				//freqHash = null; //deleting the freq hash with all dem users
				//endof reading users and sorting
				
				
				// Computing quadrants
				int quadSize = listOfUsers.size()/4;
				long[] userIds = new long[40];
				
				
				
				ArrayList<OurUser> monitorList = new ArrayList<OurUser>(); // the 40 users to be monitored
				
				for(int j = 0; j < 4; j++)
				{
					for (int i = 0; i < 10; i++)
					{
						Random rndm = new Random();
						int randNum = rndm.nextInt(quadSize) + (quadSize * j);

						
						OurUser usr = new OurUser(listOfUsers.get(randNum).getId());
					
						userIds[(j-1)*10 + i] = listOfUsers.get(randNum).getId();
						
						monitorList.add(usr);
					}
				}
				
				listOfUsers = null; // freeing the memory
				// end of computing quadrants
				
				
				// adding the 40 non suspended users to the filter, so the stream returns only their tweets
				FilterQuery filterID = new FilterQuery();
				filterID.follow(userIds);
				
				currentCollection = "Monitor";
				
		        twitterStream.filter(filterID); // starting the streaming api thread
		        
				//wait for the desired day to monitor the users
		        while (System.currentTimeMillis() <= (startingTime + 259200000)) {
		        	
		        }
		        twitterStream.cleanUp(); // after that just clean the stream
		        twitterStream.shutdown(); // and shut it down
	}

	
	public static void finalPart()
	{

        //PART 4 - BEGIN
        Set<Long> keys = freqHash.keySet();
        ArrayList<UserDetails> level_one_list = new ArrayList<UserDetails>(); // holds the information about the first level
        for(Long key: keys)
        {
        	try {
        		
				User usr = twitter.showUser(key);
				UserDetails usrdtl = new UserDetails();
				
				usrdtl.setId(key);
				usrdtl.setFollowers_num(usr.getFollowersCount());
				usrdtl.setFollowees_num(usr.getFriendsCount());
				usrdtl.calculateRatio();
				usrdtl.calculateAge(usr.getCreatedAt());
				level_one_list.add(usrdtl);
				
				
			} catch (TwitterException e) {

				e.printStackTrace();
			}
        	
        }

        	//keys = null;
        	freqHash = null; //delete the useless hashmap

            
        	// creating a new hashmap that holds the information asked for the second level, for every user
            HashMap<Long, MonitoredUserDetails> level_two_list = new HashMap<Long, MonitoredUserDetails>();
            keys = nonSuspendedUsers.keySet();
            // initializing the list 
            for(Long k: keys)
            {
            	level_two_list.put(k, new MonitoredUserDetails());
            }
            

		// for every tweet in the database
		while(true)
		{
			
			DBObject obj = database.getTweetId(); // get tweet from database
			if(obj == null)	// if there is no other tweet in the DB then stop								
			{
				break;
			}
			DBObject usr = (DBObject) obj.get("user");

			Long id = Long.parseLong(usr.get("id").toString()); // get user id
			
			manageTweet(obj, level_two_list.get(id));	// get user from the hashmap and manage his info about level two

		}
		
		
		keys = level_two_list.keySet();
		
		for(Long k: keys)
		{
			level_two_list.get(k).calculateMeanHashtagsTweets();
			level_two_list.get(k).calculateHashtagRatio();
			level_two_list.get(k).calculateMeanTweetsRetweets();
			level_two_list.get(k).calculateURLsRatio();
			level_two_list.get(k).computeYOURLR();
			level_two_list.get(k).computeDomains();
		}
		
		
		
		computeLevenshtein(level_two_list); // ypologizei to number of copies stin ousia
	}
	
	// manages tweet - gets information from tweet and adds it to its user
	//gets as parameters the JSON of the tweet and the MonitoredUserDetails object from the level_two_list (the one that the tweet belongs to)
	public static void manageTweet(DBObject json, MonitoredUserDetails usr)
	{
		
		String tweet_text = json.get("text").toString();
		// tweets and retweets
		boolean is_retweeted = (boolean)json.get("retweeted");
		if(is_retweeted)
		{
			usr.increaseRetweets_num();
		}
		else
		{
			usr.increaseTweets_num();
		}
		
		// count replies
		Long reply = (Long)json.get("in_reply_to_status_id");
		
		if(reply != null)
		{
			usr.increaseReplies();
		}
		
		// mentions
		Object obj2 = json.get("entities");
		
		Object mentions = ((BSONObject) obj2).get("user_mentions");
		

		Map<?, ?> temp = ((BSONObject) mentions).toMap();
	
		// remove mentions from tweet
		Iterator<?> iter = temp.entrySet().iterator();
		while (iter.hasNext()) {
			
			String mention_list = iter.next().toString();	        	
			mention_list = mention_list.replaceAll("\\d+\\=", "");
			DBObject dbObject = (DBObject) JSON.parse(mention_list);
		    String screen_name = dbObject.get("screen_name").toString();
		    tweet_text = tweet_text.replace("@" + screen_name, "");
		}

		
		usr.increaseMentions(temp.size());
		
		// Hashtags
		Object hashtags = ((BSONObject) obj2).get("hashtags");
		
		temp = ((BSONObject) hashtags).toMap();
		
		usr.increaseHashtags(temp.size());
		
		//URLs
		Object urls = ((BSONObject) obj2).get("urls");

		temp = ((BSONObject) urls).toMap();
		
		// remove URLs from tweet
		iter = temp.entrySet().iterator();

		// find
		while (iter.hasNext()) {
			
			// get urls list
			String url_list = iter.next().toString();	
			
			url_list = url_list.replaceAll("\\d+\\=", "");

			DBObject dbObject = (DBObject) JSON.parse(url_list);
		    String url = dbObject.get("url").toString();

		    tweet_text = tweet_text.replace(url, "");
		    url = dbObject.get("expanded_url").toString();
		    
		    //get domain and the expanded_urls
		    usr.addExpandedUrl(url);
		    url = url.replace("http://", "");
		    url = url.replaceAll("\\/.*", "");
		    
		    usr.addDomain(url);
	    
		}

		
		usr.increaseURLs(temp.size());
		
		temp = null;
		
		// retweets to user
		int ret = (int) json.get("retweet_count");
		usr.increaseRetweetsToUser(ret);
		
		//
		usr.addToTweetsList(tweet_text);
		
		// source
		String source = json.get("source").toString();
		source = source.replaceAll("\\<.*\\\">", "");
		source = source.replace("</a>", "");
		source = source.replace("Twitter for ", "");
		usr.addSource(source);
		
	}
	
	
	// computes Levenshtein distance for a user's tweets
	public static void computeLevenshtein(HashMap<Long, MonitoredUserDetails> map)
	{
		Set<Long> keys = map.keySet();
		Levenshtein dist = new Levenshtein();
		
		for(Long k: keys) //for every User
		{
			MonitoredUserDetails current_user = map.get(k);
			ArrayList<String> user_tweets = current_user.getTweetsList();
			double distance;
			
			for(int i = 0; i < user_tweets.size() - 1; i++)
			{
				int count = 0;
				for(int j = i + 1; j < user_tweets.size(); j++)
				{
					int lenght1 = user_tweets.get(i).length();
					int lenght2 = user_tweets.get(j).length();
					
					distance = dist.computeLevenshteinDistance(user_tweets.get(i), user_tweets.get(j));
					distance = distance / (lenght1 + lenght2);
					
					if(distance <= 0.1)
					{
						if(count == 0)
						{
							current_user.increaseCopies(2);
							count ++;
						}
						else
						{
							current_user.increaseCopies(1);
						}
					}
				}
			}
		}
	}
	
	
	// puttin a hashmap into an array list and sorts it
	private static ArrayList<OurUser> sortByValues(HashMap<Long, OurUser> map) { 
		
		

		List<OurUser> usersByTweets = new ArrayList<OurUser>();
		Set<Long> keyset = map.keySet();
		
		for(Long key : keyset)
		{
			OurUser usr = map.get(key);
			usersByTweets.add(usr);
		}

		    Collections.sort(usersByTweets, new Comparator<OurUser>() {

		        public int compare(OurUser o1, OurUser o2) {
		            return o1.getNumOfReferences() - o2.getNumOfReferences();
		        }
		    });
		    

	       return (ArrayList<OurUser>) usersByTweets;
	  }

	
	// updates the trends' list - removes expired - adds new
	public static void updateTrends(Trends trends) {
		activeTrends = new ArrayList<>();
		TrendStats[] temp = new TrendStats[trends.getTrends().length];
		int i = 0;
		for (Trend trend : trends.getTrends()) {
			temp[i++] = new TrendStats(trend);
		}

		for (TrendStats t : temp) {
			boolean flag = false;
			for (int j = 0; j < activeTrends.size(); j++) {
				TrendStats temp1 = (TrendStats) activeTrends.get(j);
				if (temp1.getTrend().getName().equals(t.getTrend().getName())) {
					t.updateTime();
					flag = true;
					break;
				}
			}
			if (flag == false) {
				activeTrends.add(t);
			}
		}
		for (int j = 0; j < activeTrends.size(); j++) {
			TrendStats t = (TrendStats) activeTrends.get(j);
			if (t.expired()) {
				
				database.addDethTime(activeTrends.get(i).getTrend().getName(), System.currentTimeMillis());
				activeTrends.remove(j);
			}
		}

	}
	

}
