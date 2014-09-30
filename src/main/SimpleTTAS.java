

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleTTAS extends Benchmarkable {

	private AtomicBoolean locked = new AtomicBoolean(false);

	public SimpleTTAS(int nbOfThreads, int n) {
		super(nbOfThreads, n);
	}

	public boolean lock() {

		boolean acquired = false;

		while (!acquired) {

			// Tests the lock without invalidating any cache lines.
			if (!locked.get()) {

				// Tries locking with with an atomic CAS
				acquired = locked.compareAndSet(false, true);
			}
		}
		return true;
	}

	public void unlock() {
		locked.set(false);
	}
}