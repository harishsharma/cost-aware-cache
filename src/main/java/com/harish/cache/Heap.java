package com.harish.cache;

import java.util.Arrays;

public class Heap<T extends Comparable<T>> {

	static final int INITIAL_CAPACITY = 1 << 4;
	static final int MAXIMUM_CAPACITY = 1 << 30;

	T[] array;
	int size;

	public Heap() {
		this(INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public Heap(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
		if (capacity > MAXIMUM_CAPACITY)
			capacity = MAXIMUM_CAPACITY;
		array = (T[]) new Comparable[capacity];
		size = 0;
	}

	public void add(T value) {
		if (size >= array.length - 1) {
			array = resize();
		}

		// place element into heap at bottom
		size++;
		int index = size;
		array[index] = value;

		shiftUp();
	}

	/**
	 * Returns true if the heap has no elements; false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns (but does not remove) the minimum element in the heap.
	 */
	public T peek() {
		if (isEmpty()) {
			return null;
		}

		return array[1];
	}

	/**
	 * Removes and returns the minimum element in the heap.
	 */
	public T remove() {
		T result = peek();

		// get rid of the last leaf/decrement
		array[1] = array[size];
		array[size] = null;
		size--;

		shiftDown();

		return result;
	}

	/**
	 * Returns a string representation of heap with values stored with heap
	 * structure and order properties.
	 */
	@Override
	public String toString() {
		return Arrays.toString(array);
	}

	void shiftDown() {
		int index = 1;

		while (hasLeftChild(index)) {
			int smallerChild = leftIndex(index);

			if (hasRightChild(index)
					&& array[leftIndex(index)]
							.compareTo(array[rightIndex(index)]) > 0) {
				smallerChild = rightIndex(index);
			}

			if (array[index].compareTo(array[smallerChild]) > 0) {
				swap(index, smallerChild);
			} else {
				break;
			}
			index = smallerChild;
		}
	}

	void shiftUp() {
		int index = this.size;

		while (hasParent(index) && (parent(index).compareTo(array[index]) > 0)) {
			swap(index, parentIndex(index));
			index = parentIndex(index);
		}
	}

	boolean hasParent(int i) {
		return i > 1;
	}

	int leftIndex(int i) {
		return i * 2;
	}

	int rightIndex(int i) {
		return i * 2 + 1;
	}

	boolean hasLeftChild(int i) {
		return leftIndex(i) <= size;
	}

	boolean hasRightChild(int i) {
		return rightIndex(i) <= size;
	}

	T parent(int i) {
		return array[parentIndex(i)];
	}

	int parentIndex(int i) {
		return i / 2;
	}

	T[] resize() {
		return Arrays.copyOf(array, array.length * 2);
	}

	void swap(int index1, int index2) {
		T tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
	}
}
