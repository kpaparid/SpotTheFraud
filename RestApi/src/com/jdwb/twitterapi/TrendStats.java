package com.jdwb.twitterapi;

import twitter4j.Trend;

public class TrendStats {

	Trend trend;
	long startTime;
	long endTime;
	long lastTime;

	TrendStats(Trend nTrend) {
		trend = nTrend;
		startTime = System.currentTimeMillis();
		lastTime = System.currentTimeMillis();
	}

	public void updateTime() {
		lastTime = System.currentTimeMillis();
	}

	public boolean expired() {
		long current = System.currentTimeMillis();
		if (lastTime + 7500000 < current) // 7.200.000 (+ 5 minutes) millis = 2
											// hours
		{
			endTime = current;
			return true;
		}
		return false;
	}

	public Trend getTrend() {
		return trend;
	}
}
