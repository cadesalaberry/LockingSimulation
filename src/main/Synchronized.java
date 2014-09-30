public class Synchronized extends Benchmarkable {

	private boolean locked = false;

	public Synchronized(int nbOfThreads, int n) {
		super(nbOfThreads, n);
	}
	
	/**
	 * Return the number of times the lock has been
	 */
	@Override
	public synchronized boolean lock() {

		if (locked) {
			return false;
		} else {

			locked = true;
			return true;
		}
	}

	@Override
	public synchronized void unlock() {
		locked = false;
	}
}