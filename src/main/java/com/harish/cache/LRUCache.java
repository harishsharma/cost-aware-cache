package com.harish.cache;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * LinkedHashMap based implementation of LRU Cache.
 * 
 * @see {@link LinkedHashMap}
 *
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = -220186610099586225L;
	private final int capacity;

	public LRUCache(final int capacity, final float loadFactor) {
		super(capacity, loadFactor, true);
		this.capacity = capacity;
	}

	public LRUCache(final int capacity) {
		super(capacity, 0.75f, true);
		this.capacity = capacity;

	}

	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		return size() > capacity;
	}
}
