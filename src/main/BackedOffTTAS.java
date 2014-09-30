import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackedOffTTAS extends Benchmarkable {

	private AtomicBoolean locked = new AtomicBoolean(false);
	private static final long WAIT_CONST = 52;

	public BackedOffTTAS(int nbOfThreads, int n) {
		super(nbOfThreads, n);
	}

	@Override
	public boolean lock() {

		boolean acquired = false;
		int fail = 0;

		Random rand = new Random();

		while (!acquired) {
			/*
			 * First test the lock without invalidating any cache lines.
			 */
			if (!locked.get()) {
				/* Attempt to lock the lock with an atomic CAS. */
				acquired = locked.compareAndSet(false, true);
			} else {
				fail++;
				try {
					// Sleep a random amount of time k * WAIT_CONST were
					// k=rand(0,2^fail-1)
					Thread.sleep(WAIT_CONST
							* rand.nextInt((int) Math.pow(2, fail) - 1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public void unlock() {
		locked.set(false);
	}
}