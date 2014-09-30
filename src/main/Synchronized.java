
public class Synchronized extends Benchmarkable {

	private boolean locked = false;

	public Synchronized(int nbOfThreads, int n) {
		super(nbOfThreads, n);
	}

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