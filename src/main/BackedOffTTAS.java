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
		int tryNumber = 0;

		Random rand = new Random();

		while (!acquired) {

			// Tries to test the lock without invalidating any cache lines.
			if (!locked.get()) {

				// Tries to lock the lock with an atomic CAS
				acquired = locked.compareAndSet(false, true);
			} else {
				tryNumber++;
				try {

					long k = rand.nextInt((int) Math.pow(2, tryNumber) - 1);

					Thread.sleep(WAIT_CONST * k);

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