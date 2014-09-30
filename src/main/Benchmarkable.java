import java.util.ArrayList;

public class Benchmarkable extends Lockable implements Runnable {

	public int nbOfThreads, n;

	public long maxDelay = 0;

	protected int currentTime = 0;

	private ThreadLocal<Integer> lastTime = new ThreadLocal<>();
	private ThreadLocal<Integer> grantCount = new ThreadLocal<>();
	private ThreadLocal<Integer> serialCount = new ThreadLocal<>();

	protected int serialLockCount = 0;

	public Benchmarkable(int nbOfThreads, int n) {
		super();
		this.nbOfThreads = nbOfThreads;
		this.n = n;
	}

	public long benchmark() {

		ArrayList<Thread> threads = new ArrayList<>();
		
		long t0 = System.currentTimeMillis();

		try {
			for (int i = 0; i < nbOfThreads; i++) {
				Thread t = new Thread(this);
				threads.add(t);
				t.start();
			}

			for (Thread thread : threads) {
				thread.join();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long delay = System.currentTimeMillis() - t0;

		return delay;

	}

	public void didNotGetLock() {
		this.serialCount.set(0);
	}

	public void lockAcquired() {

		// Each time a thread x acquires the lock it should yield.
		Thread.yield();

		// Computes how much time we waited
		int delay = this.currentTime - this.lastTime.get();

		if (this.maxDelay < delay) {
			this.maxDelay = delay;
		}
		// System.out.println("#" + Thread.currentThread().getId() + " waited "
		// + delay + " before getting the lock.");

		// Remembers for next time when we got the lock
		this.lastTime.set(this.currentTime++);

		// Tracks how much time we got the lock
		this.grantCount.set(this.grantCount.get() + 1);

	}

	public void lockReleased() {

		// Tracks if we got the lock multiple times in a row
		this.serialCount.set(serialCount.get() + 1);

		Thread.yield();
	}

	@Override
	public void run() {

		lastTime.set(0);
		grantCount.set(0);
		serialCount.set(0);

		while (this.grantCount.get() < n) {

			// Waits for lock to be unlocked
			while (!this.lock())
				this.didNotGetLock();

			// Starts critical section
			this.lockAcquired();

			this.unlock();
			// Ends critical section

			this.lockReleased();
		}
	}
	
	public long getMaxDelay() {
		return maxDelay;
	}
}