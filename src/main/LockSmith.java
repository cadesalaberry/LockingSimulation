public class LockSmith {
	
	public static void main(String[] args) {
		
		Synchronized sync = new Synchronized(2, 100);
		
		sync.benchmark();
	}
}