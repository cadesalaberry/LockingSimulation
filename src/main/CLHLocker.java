

import java.util.concurrent.atomic.AtomicReference;

public class CLHLocker extends Benchmarkable {

	private final ThreadLocal<Node> pred = new ThreadLocal<Node>() {
        protected Node initialValue() {
            return null;
        }
    };
	private final ThreadLocal<Node> me = new ThreadLocal<Node>(){
        protected Node initialValue() {
            return new Node();
        }
    };

	private final AtomicReference<Node> tail = new AtomicReference<Node>(
			new Node());

	public CLHLocker(int nbOfThreads, int n) {
		super(nbOfThreads, n);
	}

	@Override
	public boolean lock() {

		// Gets the thread node and lock it
		this.me.get().locked = true;

		// Remembers what was the previous node
		Node prev = this.tail.getAndSet(this.me.get());

		// Remember the previous node for this thread
		this.pred.set(prev);

		while (prev.locked)
			;// Waits for unlocked previous node.

		return true;
	}

	@Override
	public void unlock() {

		// Unlocks current node
		this.me.get().locked = false;

		this.me.set(this.pred.get());
	}

	private class Node {
		public volatile boolean locked = false;
	}
}