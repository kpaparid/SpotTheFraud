package com.jdwb.twitterapi;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;

import twitter4j.FilterQuery;
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
	private static String currentCollection = "TweetsCollection";
	private static DatabaseManagment database = new DatabaseManagment();

	public static void main(String[] args) throws UnknownHostException {

		int counter = 0;

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("PITlssl6DKTBmMv8WPl2XUQZm")
				.setOAuthConsumerSecret(
						"0zqBzkJoov1sHCgGIwKchGv1NdaHL9vFY4kVGR4c5yZ0Soz4OY")
				.setOAuthAccessToken(
						"2864396627-EU9BPTAx0i8S1gmUMj1unQndjdZLKcBgp7WdC1e")
				.setOAuthAccessTokenSecret(
						"nVlqyDZIKg17xGWGSXA9UIFGK6eafUTamBWxmBWQl2b0I");

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

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		TwitterStream twitterStream = new TwitterStreamFactory(cb2.build())
				.getInstance();

		//FilterQuery filter = new FilterQuery();
		//long currentTime = 0;

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				
				
				String rawJSON = DataObjectFactory.getRawJSON(status);
				
				database.addTweet(rawJSON, currentCollection);
						                      
				/*
				for (int i = 0; i < activeTrends.size(); i++) {
					TrendStats t = (TrendStats) activeTrends.get(i);
					String name = t.getTrend().getName();
					if (status.getText().contains(name)) {
						// json se arxeio
						System.out.println("@"
								+ status.getUser().getScreenName() + " - "
								+ status.getText());
					}
				}
				*/
				
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

		twitterStream.addListener(listener);
		long startingTime = System.currentTimeMillis();

		
		System.out.println("Starting the first marathon: 3 dayz!");
 		while (System.currentTimeMillis() <= (startingTime + 86400000)) {
 			
 		try {
 			long currentTime = System.currentTimeMillis();
 
 			Trends trends = twitter.getPlaceTrends(1);
 
 
 			updateTrends(trends);
 
 			/*
 			 * counter++; System.out.println("Wordlwide trends");
 			 * System.out.println(counter); System.out.println("As of : " +
 			 * trends.getAsOf()); for (Trend trend : trends.getTrends()) {
 			 * System.out.println(trend); }
 			 */
 
 			
 			String[] keywords = new String[activeTrends.size()];
 			
 			int kounter = 0;
 			System.out.println("Dem Trends:");
 			for (int i = 0; i < activeTrends.size(); i++) {
 				
 				String trendName = activeTrends.get(i).getTrend().getName();
 				long time =  activeTrends.get(i).getStartTime();
 				keywords[kounter++] = trendName;
 				
 				database.addTrend(trendName, time);
 				System.out.println(i+1 + ")" + trendName);
 			}
 			
 

			//database.dropDB();
			//twitterStream.cleanUp();
			//filter.track(keywords);
			//twitterStream.filter(filter);
			
			
			
			while (System.currentTimeMillis() <= (currentTime + 300000)) {

			}
			System.out.println("5 minutes have passed...");
			

		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
			
		// close the Stream plz or gtfo 
		   
	}
 	
 		
 		

 		System.out.println("Cleaning up and closing the stream...");
    twitterStream.cleanUp();
    twitterStream.shutdown();
		

		// Reading users and dem tweetz and sorting 'em
		
		
		
		HashMap<Long, OurUser> freqHash = new HashMap<Long, OurUser>();
		
		ArrayList<String> allTrends = database.getAllTrends();
		
		
		System.out.println("Creating the HashMap with the frequency of dem users...");
		
		while(true)
		{
			System.out.print("...");
		    Values userTweet =  database.getItemsFromDatabase(); // exei ID tou xristi kai ena text, pou einai tweet tou
		    if(userTweet == null){
		    	break;
		    }
		    Long userID = userTweet.getId();
		    OurUser user = freqHash.get(userID);
		    if(user == null){
		    	freqHash.put(userID, new OurUser(userID));
		    	user = freqHash.get(userID);
		    }
		    for(String temp: allTrends){
		    	if(userTweet.getText().contains(temp)){
		    		if(!user.getList().contains(temp)){
		    			user.getList().add(temp);
		    			user.AddReferences();
		    		}
		    	}
		    }
		    
		}
		
		
		
		System.out.println("Ended with the first HashMap");
		System.out.println("Building second HashMap with non suspended users...");
		// Looking Up for Suspended Users
		Set<Long> keyset = freqHash.keySet();
		
		long[] lookForUsers = new long[freqHash.size()]; 
		
		counter = 0;
		
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
		
		
		
		HashMap<Long, OurUser> nonSuspendedUsers = new HashMap<Long, OurUser>(); // new hash with non suspended users
		for(int i = 0; i < iterations; i++)
		{
			int jiterations = 100;
			if(i == iterations - 1)
			{
				jiterations = freqHash.size() % 100;
			}
			
			long[] tempUserIds = new long[jiterations]; // temp pinakas pou kanei store 100 - 100 tous users
			
			for(int j = 0; j < jiterations; j++)
			{
				tempUserIds[j] = lookForUsers[j + (i * 100)];
		
			}
			
			

			try {
				
				ResponseList<User> temp = twitter.lookupUsers(tempUserIds);
				
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

		
		ArrayList<OurUser> listOfUsers = sortByValues(nonSuspendedUsers);
		
		

		System.out.println("Old Size: " + freqHash.size() + " New hashmap size:" + nonSuspendedUsers.size());
		System.out.println(listOfUsers);
		//nonSuspendedUsers = null; // to idio me katw
		//freqHash = null; //deleting the freq hash with all dem users
		//endof reading users and sorting
		
		
		// Computing quadrants
		int quadSize = listOfUsers.size()/4;
		long[] userIds = new long[40];
		
		
		
		ArrayList<OurUser> MonitorList = new ArrayList<OurUser>(); // the 40 users to be monitored
		
		for(int j = 0; j < 4; j++) // to allaksa gt an ksekinaei apo 1, tote gia to prwto tha exeis px an to quadSize = 10 kai random = 2 tha paei
								  // 2 + 10*1 = 12 enw eC thes to 2, enw an to ksekinas apo 0 ginetai swsta
		{
			for (int i = 0; i < 10; i++)
			{
				Random rndm = new Random();
				int randNum = rndm.nextInt(quadSize) + (quadSize * j);

				
				OurUser usr = new OurUser(listOfUsers.get(randNum).getId());
			
				userIds[(j-1)*10 + i] = listOfUsers.get(randNum).getId();
				
				MonitorList.add(usr);
			}
		}
		
		listOfUsers = null;
		
		
		FilterQuery filterID = new FilterQuery();
		filterID.follow(userIds);
		
		currentCollection = "Monitor";
		
        twitterStream.filter(filterID);
        //wait for monitoring the users selected
        while (System.currentTimeMillis() <= (startingTime + 259200000)) {
        	
        }
        twitterStream.cleanUp();
        twitterStream.shutdown();
        
        
        
        
        //PART 4 - BEGIN
        Set<Long> keys = freqHash.keySet();
        ArrayList<UserDetails> level_one_list = new ArrayList<UserDetails>();
        for(Long k: keys)
        {
        	try {
        		
				User usr = twitter.showUser(k);
				UserDetails usrdtl = new UserDetails();
				
				usrdtl.setId(k);
				usrdtl.setFollowers_num(usr.getFollowersCount());
				usrdtl.setFollowees_num(usr.getFriendsCount());
				usrdtl.calculateRatio();
				usrdtl.calculateAge(usr.getCreatedAt());
				level_one_list.add(usrdtl);
				
				
			} catch (TwitterException e) {

				e.printStackTrace();
			}
        	
        }

        	keys = null;
        	freqHash = null; //delete the useless hashmap

            
            // vazei ta ids twn 40 users sto neo hashmap gia ta stoixeia tou epipedou b
            HashMap<Long, MonitoredUserDetails> level_two_list = new HashMap<Long, MonitoredUserDetails>();
            keys = nonSuspendedUsers.keySet();
            for(Long k: keys)
            {
            	level_two_list.put(k, new MonitoredUserDetails());
            }
            

        
        
		
		while(true)
		{
			
			DBObject obj = database.getTweetId();
			//manageTweet(obj, user_to_manage);		
			if(obj == null)
			{
				break;
			}

			//System.out.println(obj2.toString());
		}
		
		
		keys = level_two_list.keySet();
		
		for(Long k: keys)
		{
			level_two_list.get(k).calculateMeanHashtagsTweets();
			level_two_list.get(k).calculateHashtagRatio();
			level_two_list.get(k).calculateMeanTweetsRetweets();
			level_two_list.get(k).calculateURLsRatio();	
		}
                

}
	
	
	

	public static void manageTweet(DBObject json, MonitoredUserDetails usr)
	{
		
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
		
		usr.increaseMentions(temp.size());
		
		// Hashtags
		Object hashtags = ((BSONObject) obj2).get("hashtags");
		
		temp = ((BSONObject) hashtags).toMap();
		
		usr.increaseHashtags(temp.size());
		
		//URL
		Object urls = ((BSONObject) obj2).get("urls");
		
		temp = ((BSONObject) urls).toMap();
		
		usr.increaseURLs(temp.size());
		
		temp = null;
		
		// retweets to user
		int ret = (int) json.get("retweet_count");
		usr.increaseRetweetsToUser(ret);
		
		//
		
	}
	
	
	
	
	
	
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
				database.addDethTime(activeTrends.get(i).getTrend().getName(), 200000000);
				activeTrends.remove(j);
			}
		}

	}
}
