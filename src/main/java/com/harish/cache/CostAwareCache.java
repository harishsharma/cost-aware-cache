package com.harish.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CostAwareCache<K, V> {

	private static final int MAXIMUM_CAPACITY = 1 << 30;

	private final PriorityQueue<Entry<K, V>> queue;
	private final Map<K, EntryIndexHolder<K, V>> map;

	private int everIncreasingLValue = 0;

	/**
	 * The number of key-value mappings contained in this map.
	 */
	transient int size;

	public CostAwareCache(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
		if (capacity > MAXIMUM_CAPACITY)
			capacity = MAXIMUM_CAPACITY;

		queue = new PriorityQueue<CostAwareCache.Entry<K, V>>(capacity);
		map = new HashMap<K, EntryIndexHolder<K, V>>(capacity);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public V get(K key) {
		if (null == key)
			return null;
		EntryIndexHolder<K, V> holder = map.get(key);
		if (null == holder || null == holder.entry)
			return null;
		V value = holder.entry.value;
		// int indexInHeap = holder.index;
		// recalculate h value for entry.
		// reOrderPriority(int index)
		return value;
	}

	public V put(K key, V value, int costOfValue) {
		if (null == key)
			throw new NullPointerException("Null keys are not supported");
		if (costOfValue < 1)
			throw new IllegalArgumentException("Minimum cost allowed is one.");

		if (map.containsKey(key)) {
			Entry<K, V> entry = new Entry<>(key, value, costOfValue,
					calculateHValue(costOfValue));
			// replace(Entry<K, V> new , Entry<K, V> old);
		}

		int h = 0;
		// calculate h value here.
		//
		return null;
	}

	private int calculateHValue(int cost) {
		return everIncreasingLValue + cost;
	}

	static class EntryIndexHolder<K, V> {
		final Entry<K, V> entry;
		int index;

		public EntryIndexHolder(final Entry<K, V> entry, int index) {
			this.entry = entry;
			this.index = index;
		}

		Entry<K, V> getEntry() {
			return entry;
		}

		int getIndex() {
			return index;
		}
	}

	static class Entry<K, V> implements Comparable<Entry<K, V>> {
		// benefit value associated with this entry. Key with minimum h value is
		// removed during the purging.
		int h;
		final int cost;
		final K key;
		V value;

		Entry(final K key, final V value, final int cost, final int h) {
			this.key = key;
			this.value = value;
			this.cost = cost;
			this.h = h;
		}

		K getKey() {
			return key;
		}

		V getValue() {
			return value;
		}

		V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		int getCost() {
			return cost;
		}

		int getH() {
			return h;
		}

		void setH(int h) {
			this.h = h;
		}

		@Override
		public int compareTo(Entry<K, V> anotherEntry) {
			if (null == anotherEntry)
				throw new NullPointerException();
			return compare(this.h, anotherEntry.getH());
		}

		static int compare(int x, int y) {
			return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
	}
}
