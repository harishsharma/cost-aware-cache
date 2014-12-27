package com.harish.cache;

import java.util.HashMap;
import java.util.Map;

public class CostAwareCache<K, V> {

	private static final int MAXIMUM_CAPACITY = 1 << 30;

	private final IndexAwareHeap<K, V> heap;
	private final Map<K, IndexedEntry<K, V>> map;

	private int everIncreasingLValue = 0;

	/**
	 * The number of key-value mappings contained in this map.
	 */
	private transient int size;

	private final int capacity;

	public CostAwareCache(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
		if (capacity > MAXIMUM_CAPACITY)
			capacity = MAXIMUM_CAPACITY;

		this.capacity = capacity;
		heap = new IndexAwareHeap<K, V>(capacity);
		map = new HashMap<K, IndexedEntry<K, V>>(capacity);
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
		IndexedEntry<K, V> holder = map.get(key);
		if (null == holder || null == holder.entry)
			return null;

		V value = holder.entry.value;
		holder.entry.h = calculateHValue(holder.entry.cost);
		heap.increaseValue(holder.index, holder.entry.h);
		return value;
	}

	public void put(K key, V value, int costOfValue) {
		if (null == key)
			throw new NullPointerException("Null keys are not supported");
		if (costOfValue <= 0)
			throw new IllegalArgumentException("Less than minimum cost allowed");

		if (map.containsKey(key)) {
			IndexedEntry<K, V> holder = map.get(key);
			holder.entry.value = value;
			holder.entry.cost = costOfValue;
			int oldH = holder.entry.h;
			int newH = calculateHValue(costOfValue);
			holder.entry.h = newH;
			if (oldH < newH)
				heap.increaseValue(holder.index, newH);
			else
				heap.decreaseValue(holder.index, newH);
			return;
		}

		if (size >= capacity) {
			IndexedEntry<K, V> holder = heap.remove();
			map.remove(holder.entry.key);
			size = heap.size;
		}

		int hValue = calculateHValue(costOfValue);
		IndexedEntry<K, V> holder = new IndexedEntry<>(new Entry<K, V>(key,
				value, costOfValue, hValue));
		map.put(key, holder);
		heap.add(holder);
		size = heap.size;
	}

	private int calculateHValue(int cost) {
		int result = everIncreasingLValue + cost;
		IndexedEntry<K, V> holder = heap.peek();
		if (holder == null || holder.entry == null) {
			// should not happen more than once.
			everIncreasingLValue = result;
		} else {
			everIncreasingLValue = holder.entry.h;
		}

		return result;
	}

	/**
	 * TODO: Write proper toString.
	 */
	@Override
	public String toString() {
		return heap.toString();
	}

	static class Entry<K, V> implements Comparable<Entry<K, V>> {
		// benefit value associated with this entry. Key with minimum h value is
		// removed during the purging.
		int h;
		int cost;
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

		void setCost(int cost) {
			this.cost = cost;
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
