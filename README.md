cost-aware-cache
================

Cost aware cache implementation.

CostAwareCache is a cache implementation which takes cost of computing values into account. This cache can be used when cost of computing key-value is different for different KVs.
It combines LRU with cost of computing values to give a more novel eviction policy.
CostAwareCache is an implementation of Greedy dual size algorithm.
Note: This implementation is thread unsafe and not optimized fully and just a draft implementation
Reference : http://www.cse.iitb.ac.in/~gracias/webcaching/html/node31.html

Use:
			
			
			CostAwareCache<String, Integer> costAwareCache = new CostAwareCache<>(
			CACHE_CAPACITY);
			costAwareCache.put(key, value, cost);
			costAwareCache.get(key) 
			