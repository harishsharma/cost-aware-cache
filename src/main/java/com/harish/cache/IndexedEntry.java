package com.harish.cache;

import com.harish.cache.CostAwareCache.Entry;

public class IndexedEntry<K, V> implements Comparable<IndexedEntry<K, V>> {
	final Entry<K, V> entry;
	int index;

	public IndexedEntry(Entry<K, V> entry) {
		this.entry = entry;
	}

	Entry<K, V> getEntry() {
		return entry;
	}

	int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(IndexedEntry<K, V> o) {
		return entry.compareTo(o.entry);
	}

	@Override
	public String toString() {
		return String.format("Entry with cost %d is at index %d", entry.h,
				index);
	}
}
