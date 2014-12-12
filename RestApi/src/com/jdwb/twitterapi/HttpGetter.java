package com.jdwb.twitterapi;

import java.net.UnknownHostException;
import java.util.ArrayList;

import twitter4j.FilterQuery;
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
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class HttpGetter {
	
	static ArrayList<TrendStats> activeTrends;
	private static DatabaseManagment database;

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

		FilterQuery filter = new FilterQuery();
		long currentTime = 0;

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				
				
				@SuppressWarnings("deprecation")
				String rawJSON = DataObjectFactory.getRawJSON(status);
				database.addTweet(rawJSON);
						                      
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
		
		database = new DatabaseManagment();

		while (true) {
			try {
				currentTime = System.currentTimeMillis();

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
				for (int i = 0; i < activeTrends.size(); i++) {
					
					String trendName = activeTrends.get(i).getTrend().getName();
					long time =  activeTrends.get(i).getStartTime();
					keywords[kounter++] = trendName;
					
					database.addTrend(trendName, time);
				}
				
				

				//database.dropDB();
				twitterStream.cleanUp();
				filter.track(keywords);
				twitterStream.filter(filter);
				
				database.printDatabase();
				

				while (System.currentTimeMillis() <= (currentTime + 300000)) {

				}

			} catch (TwitterException e1) {
				e1.printStackTrace();
			}

		}
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
