package com.harish.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Helper {

	/**
	 * Creates string keys with mapping to cost associated with them.
	 * <p>
	 * Note: key length will be fixed to 20 chars and cost value will be between
	 * 1ms and 50ms , randomly assigned to each key.
	 * 
	 * @param n
	 * @return
	 */
	public static Map<String, Integer> createKeysWithCostInMs(final int n) {
		Random random = new Random();
		Map<String, Integer> result = new HashMap<>(n);

		for (int i = n - 1; i >= 0; i--) {
			result.put(UUID.randomUUID().toString().substring(0, 20),
					random.nextInt(50 - 1) + 1);
		}
		return result;
	}
}
