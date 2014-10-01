LockingSimulation
=================

A micro-benchmark program to test several different locking strategies using multi-threading in Java.

Understanding the cost of different lock designs is important. Develop a micro-benchmark program LockSmith. Use `q` ∈ {1, 2, 3, 4} to indicates which of several different locking strategies will be used by `p` threads so they each compete to acquire (and release) a single lock `n` times. Map :
* `q = 1` to using synchronized,
* `q = 2` to using a simple TTAS (no delay/back-off),
* `q = 3` to TTAS with exponential back-off,
* `q = 4` to a CLH queue lock.

Each time a thread `x` acquires the lock, and then again after the lock is released, `x` should execute a `Thread.yield()` operation. You will also need to track how many times a thread acquires the lock (so it knows when to stop), and also track `Delay_x`, the maximum number of times the lock was acquired by other threads between `x`’s own successful acquires.
Time this whole process. Once all threads are done, go through the individual thread’s maximum `Delay_x` values and compute `Delay_x` over all `x`. Print out the total time taken to perform the simulation, and the maximum delay on separate lines.
On a multiprocessor machine, find out how many cores you have, and set `p` to that value. Then, for each of `q` ∈ {1, 2, 3, 4}, start `n` at 100 000, and keep doubling `n` until the slowest locking strategy takes 10s or more. On a single graph show 3 plots, illustrating the the time taken by each locking strategy for each value of `n` tested. On another graph show 3 plots, illustrating the max `Delay_x` over all `x` taken by each locking strategy for each value of `n` tested.

![Time Taken by Number of Lock Operations Graph](https://github.com/cadesalaberry/LockingSimulation/blob/master/assets/q2a.png)

| n	| Synchronised	| SimpleTTAS	| backoffTTAS	| CLH	|
|---|---------------|-------------|-------------|-----|
| 100000	| 451	| 352	| 398	| 222	|
| 200000	| 913	| 709	| 653	| 458	|
| 400000	| 1730	| 1304	| 1328	| 1029	|
| 800000	| 7295	| 2862	| 2513	| 1564	|
| 1600000	| 9053	| 5556	| 7033	| 3098	|
| 3200000	| 13401	| 10861	| 10467	| 6953	|


The graph shows a pretty linear increment of time relative to the number of lock operations. Also, all the methods have a similar behavior and comparable timing. The synchronized method is surprisingly slow as the graph shows, but the use of OpenJDK instead of the official Oracle binaries might be one of the reasons. The CLH took more time to code, but shows great performances towards its competitor. It might be from the tight organisation of the implicit queue, getting rid of the busy waiting of other methods.

![Lock Delay per Number of Lock Operations Graph](https://github.com/cadesalaberry/LockingSimulation/blob/master/assets/q2b.png)

| n	| Synchronised	| SimpleTTAS	| backoffTTAS	| CLH	|
|---|---------------|-------------|-------------|-----|
| 100000	| 4293	| 99487	| 95182	| 3244	|
| 200000	| 6954	| 199989	| 188045	| 8348	|
| 400000	| 20104	| 399948	| 400000	| 7959	|
| 800000	| 7282	| 370914	| 364173	| 3721	|
| 1600000	| 13581	| 764350	| 1599796	| 5663	|
| 3200000	| 10566	| 2314305	| 2293087	| 11859	|


We can clearly see the naive implementation offsetting the graph with their numerous waisted delays. As opposed to the native synchronized which must be heavily optimised under the hood to avoid useless operations and the CLH lock which imposes its own ordering to solve the locking problem.


