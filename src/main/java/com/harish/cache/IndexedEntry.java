package com.harish.cache;

import com.harish.cache.CostAwareCache.Entry;

@SuppressWarnings("rawtypes")
public class IndexedEntry implements Comparable<IndexedEntry> {
	final Entry entry;
	int index;

	public IndexedEntry(Entry entry) {
		this.entry = entry;
	}

	Entry getEntry() {
		return entry;
	}

	int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(IndexedEntry o) {
		return entry.compareTo(o.entry);
	}

	@Override
	public String toString() {
		return String.format("Entry with cost %d is at index %d", entry.h,
				index);
	}
}
