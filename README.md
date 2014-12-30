cost-aware-cache
================

Cost aware cache implementation.
--------------------------------

CostAwareCache is a cache implementation which takes cost of computing values into account. This cache can be used when cost of computing key-value is different for different KVs.
<p>
It combines LRU with cost of computing values to give a more novel eviction policy.
CostAwareCache uses Greedy dual size algorithm to calculate the effective cost for each key.<p>
Note: This implementation is thread unsafe and not optimized fully and just a draft implementation.
<p>
Reference : http://www.cse.iitb.ac.in/~gracias/webcaching/html/node31.html
<p>

Usage:
------
```java
CostAwareCache<K, V> costAwareCache = new CostAwareCache<>(
			CACHE_CAPACITY);
costAwareCache.put(K, V, cost);
costAwareCache.get(K) 
```			
			

BenchMark 
---------
I have used GoogleCache (from GUAVA) and LRUcache (based on LinkedHashMap) to compare with CostAwareCache implementation.

* I have taken 50,000 unique keys to make the key set universe.
* Each cache has capacity to hold maximum 30,000 KV pairs after which eviction happens.(Note: when eviction happens depends on cache implementation)
* Each key has been assigned a value randomly between 1 to 50  , which also is the cost of computing the corresponding value in Milliseconds. e.g. If key has value of 15 then cost of computing that value is 15 Ms.
* Computing Cost is simulated by sleeping the thread for cost millisecond. e.g. if cost was 15 Ms then Thead.sleep(15) is called while put operation.
* 10,000 keys when pre-populated to warm up all the caches.
* For specified number of iterations do the following for all the three caches and compare numbers.
```java
 if cache.get(key) == null
	Get value from KV mapping.
	Thread.sleep(value)
	cache.put(value)
  else
   	hits++
```
* Also to simulate LRU friendly behavior , 40% of the keys are referenced 60% of the time and rest of the keys are referenced 40% of the time.

Results
-------
Iterations:100,000

|Cache | Hits | Hit Ratio | Time in Seconds | Time Taken Compared to Google Cache
|------|------|-----------|-----------------| -----------------------------------
|Google Cache | 57152 | 57.1% | 1107 | 100 % 
|LRU Cache | 57063 | 57% | 1112 | 100.4%
|Cost Aware Cache | 55285 | 55.2% | 985 | 88.9%

Iterations:1000,000

|Cache | Hits | Hit Ratio | Time in Seconds | Time Taken Compared to Google Cache
|------|------|-----------|-----------------| -----------------------------------
|Google Cache | 649567 | 64.9% | 9015 | 100%
|LRU Cache | 649197 | 64.9% | 9082 | 100.7%
|Cost Aware Cache | 637117 | 63.7% | 6264 | 69.4%

Findings
--------

* Hit rate of Cost Aware Cache is slightly less then LRU Based Cache , but still very close.
* Even with less hit rate , over all latency across multiple iterations (100,000 and 1000,000) for Cost aware cache is significantly less then LRU cache , which is justifiable because cost aware cache tries to keep KV pairs with higher cost in cache , which simple LRU cache does not take into account.

TODO
----

* Make it thread safe.
* Do optimizations based on paper (http://dblab.usc.edu/Users/papers/CAMPTR.pdf)

Internal Details of CostAwareCache
----------------------------------
CostAwareCache performs both get(key) , put(key,value,cost) operations in O(log(keys)).
It internally maintains a hashmap of Key - IndexAwareEntry and a binary heap of IndexAwareEntry. IndexAwareEntry is a container to hold Key-Value pair , cost associated with KV pair and the index of this KV pair inside the heap data structure.

Since HashMap allows O(1) get(key) operation(assuming minimal collisions) , we can get IndexAwareEntry associated with the key using hashmap in constant time and using this IndexAwareEntry we can get the index of this Entry inside the binary heap immidiately which allows us to make the random delete operation in heap in O(log(n)) (Note: In normal Heap random delete operation is O(n*log(n)) as first we need to find the item in heap and then delete that.)

Whenever , IndexAwareEntry is moved up or down the heap due to change in priority of that entry , the corresponding index inside the entry is reset accordingly.

Priority of entries are calculated according to Greedy dual size algorithm and called H(key) value for any KV pair. This H(key) include both LRU factor as well as cost factor for KV pair.


			
