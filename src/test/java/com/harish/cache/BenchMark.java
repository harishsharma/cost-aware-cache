package com.harish.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class BenchMark {

	private static Map<String, Integer> keysToCostMap = Helper
			.createKeysWithCostInMs(50000);

	private static List<String> startingKeys = new ArrayList<>();

	// select the keys to populate map.
	{
		int n = 10000;
		for (String key : keysToCostMap.keySet()) {
			startingKeys.add(key);
			if (--n <= 0)
				break;
		}
	}

	Cache<String, Integer> cache = CacheBuilder.newBuilder().maximumSize(30000)
			.build();

	CostAwareCache<String, Integer> costAwareCache = new CostAwareCache<String, Integer>(
			30000);

	/**
	 * 
	 * @return time taken to initially populate the google cache.
	 */
	public long populateGoogleCache() {
		long start = System.currentTimeMillis();

		for (String key : startingKeys) {
			cache.put(key, keysToCostMap.get(key));
		}
		return System.currentTimeMillis() - start;
	}

	public long populateCostAwareCache() {
		long start = System.currentTimeMillis();

		for (String key : startingKeys) {
			Integer cost = keysToCostMap.get(key);
			costAwareCache.put(key, cost, cost);
		}
		return System.currentTimeMillis() - start;
	}

	public long doOperationsForGoogleCache() throws InterruptedException {
		String[] keys = keysToCostMap.keySet().toArray(
				new String[keysToCostMap.size()]);
		int LOW = 0, HIGH = 50000;
		Random random = new Random();
		long start = System.currentTimeMillis();
		int hits = 0;
		for (int i = 0; i < 20000; i++) {
			int index = random.nextInt(HIGH - LOW) + LOW;
			String key = keys[index];
			if (cache.getIfPresent(key) == null) {
				// Sleep for some time , equivalent to cost of calculating the
				// value.
				int cost = keysToCostMap.get(key);
				Thread.sleep(cost);
				cache.put(key, cost);
			} else {
				hits++;
			}
			if (i != 0 && i % 999 == 0) {
				System.out.println("Done processing 1000 keys  with hits "
						+ hits);
				hits = 0;
			}

		}
		return System.currentTimeMillis() - start;
	}

	public long doOperationsForCostAwareCache() throws InterruptedException {
		String[] keys = keysToCostMap.keySet().toArray(
				new String[keysToCostMap.size()]);
		int LOW = 0, HIGH = 50000;
		Random random = new Random();
		long start = System.currentTimeMillis();
		int hits = 0;
		for (int i = 0; i < 20000; i++) {
			int index = random.nextInt(HIGH - LOW) + LOW;
			String key = keys[index];
			if (costAwareCache.get(key) == null) {
				// Sleep for some time , equivalent to cost of calculating the
				// value.
				int cost = keysToCostMap.get(key);
				Thread.sleep(cost);
				costAwareCache.put(key, cost, cost);
			} else {
				hits++;
			}
			if (i != 0 && i % 999 == 0) {
				System.out.println("Done processing 1000 keys  with hits "
						+ hits);
				hits = 0;
			}

		}
		return System.currentTimeMillis() - start;
	}

	public static void main(String... arg) throws InterruptedException {
		BenchMark b = new BenchMark();
		System.out.println(b.populateGoogleCache());
		System.out.println(b.doOperationsForGoogleCache());
		// System.out.println(b.populateCostAwareCache());
		// System.out.println(b.doOperationsForCostAwareCache());
	}
}
