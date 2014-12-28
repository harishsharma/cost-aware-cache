package com.harish.cache;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//TODO: ADD test cases to validate the cache operations and effective cost of keys.
public class CostAwareCacheTest {
	private CostAwareCache<String, String> classUnderTest;

	@BeforeClass
	void beforeClass() {
		classUnderTest = new CostAwareCache<>(5);
		classUnderTest.put("A", "A", 1);
		classUnderTest.put("B", "B", 2);
		classUnderTest.put("C", "C", 3);
		classUnderTest.put("D", "D", 4);
		classUnderTest.put("E", "E", 5);
		classUnderTest.get("A");
		classUnderTest.put("F", "F", 6);
		classUnderTest.get("B");
		classUnderTest.put("G", "G", 2);
	}

	@AfterClass
	void afterClass() {
	}

	@Test
	void test() {
	}

}
