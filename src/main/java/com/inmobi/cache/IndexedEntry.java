package com.inmobi.cache;

public class IndexedEntry<T extends Comparable<T>> implements
		Comparable<IndexedEntry<T>> {
	T entry;
	int index;

	public IndexedEntry() {
		this(null, 0);
	}

	public IndexedEntry(T entry, int index) {
		this.entry = entry;
		this.index = index;
	}

	T getEntry() {
		return entry;
	}

	void setEntry(T entry) {
		this.entry = entry;
	}

	int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(IndexedEntry<T> o) {
		return entry.compareTo(o.entry);
	}
}
