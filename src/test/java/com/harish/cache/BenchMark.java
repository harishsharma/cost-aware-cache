package com.harish.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class BenchMark {

	private static Map<String, Integer> keysToValueMap = Helper
			.createKeysWithValues(50000);

	private static List<String> startingKeys = new ArrayList<>();

	// select the keys to populate map.
	{
		int n = 10000;
		for (String key : keysToValueMap.keySet()) {
			startingKeys.add(key);
			if (--n <= 0)
				break;
		}
	}

	private static final int CACHE_CAPACITY = 30000;
	private static final int NUM_ITERATIONS = 100000;

	Cache<String, Integer> cache = CacheBuilder.newBuilder()
			.maximumSize(CACHE_CAPACITY).build();

	CostAwareCache<String, Integer> costAwareCache = new CostAwareCache<>(
			CACHE_CAPACITY);

	LRUCache<String, Integer> lruCache = new LRUCache<>(CACHE_CAPACITY);

	/**
	 * 
	 * @return time taken to initially populate the google cache.
	 */
	public long populateGoogleCache() {
		long start = System.currentTimeMillis();

		for (String key : startingKeys) {
			cache.put(key, keysToValueMap.get(key));
		}
		return System.currentTimeMillis() - start;
	}

	public long populateCostAwareCache() {
		long start = System.currentTimeMillis();

		for (String key : startingKeys) {
			Integer value = keysToValueMap.get(key);
			costAwareCache.put(key, value, 10 * value);
		}
		return System.currentTimeMillis() - start;
	}

	public long populateLRUCache() {
		long start = System.currentTimeMillis();

		for (String key : startingKeys) {
			Integer value = keysToValueMap.get(key);
			lruCache.put(key, value);
		}
		return System.currentTimeMillis() - start;
	}

	public long doOperationsForGoogleCache() throws InterruptedException {
		String[] keys = keysToValueMap.keySet().toArray(
				new String[keysToValueMap.size()]);
		Random random = new Random();
		long start = System.currentTimeMillis();
		int hits = 0;
		for (int i = 0; i < NUM_ITERATIONS; i++) {
			int index = getNextInt(random);
			String key = keys[index];
			if (cache.getIfPresent(key) == null) {
				// Sleep for some time , equivalent to cost of calculating the
				// value.
				int value = keysToValueMap.get(key);
				Thread.sleep(value);
				cache.put(key, value);
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
		String[] keys = keysToValueMap.keySet().toArray(
				new String[keysToValueMap.size()]);
		Random random = new Random();
		long start = System.currentTimeMillis();
		int hits = 0;
		for (int i = 0; i < NUM_ITERATIONS; i++) {
			int index = getNextInt(random);
			String key = keys[index];
			if (costAwareCache.get(key) == null) {
				// Sleep for some time , equivalent to cost of calculating the
				// value.
				int value = keysToValueMap.get(key);
				Thread.sleep(value);
				costAwareCache.put(key, value, 10 * value);
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

	public long doOperationsForLRUCache() throws InterruptedException {
		String[] keys = keysToValueMap.keySet().toArray(
				new String[keysToValueMap.size()]);
		Random random = new Random();
		long start = System.currentTimeMillis();
		int hits = 0;
		for (int i = 0; i < NUM_ITERATIONS; i++) {
			int index = getNextInt(random);
			String key = keys[index];
			if (lruCache.get(key) == null) {
				// Sleep for some time , equivalent to cost of calculating the
				// value.
				int value = keysToValueMap.get(key);
				Thread.sleep(value);
				lruCache.put(key, value);
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

	/*
	 * Generates random number with skewed frequency to help LRU. It generates
	 * indexes between 30000-50000 60% of the time and indexes 0-30000 40% of
	 * the time. Or you can say in cache 40% of the keys are references 60% of
	 * the time.
	 */
	private int getNextInt(final Random random) {
		int n = random.nextInt(11 - 1) + 1;
		if (n <= 6)
			return random.nextInt(50000 - 30000) + 30000;
		else
			return random.nextInt(30000 - 0) + 0;
	}

	public static void main(String... arg) throws InterruptedException {
		BenchMark b = new BenchMark();
		// System.out.println(b.populateGoogleCache());
		// System.out.println(b.doOperationsForGoogleCache());

		// System.out.println(b.populateCostAwareCache());
		// System.out.println(b.doOperationsForCostAwareCache());

		System.out.println(b.populateLRUCache());
		System.out.println(b.doOperationsForLRUCache());
	}
}
