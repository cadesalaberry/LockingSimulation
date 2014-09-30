public class LockSmith {

	public static void main(String[] args) {

		int cores = Runtime.getRuntime().availableProcessors();

		System.out.println("n,\ttime,\tdelay");

		// Does the questions one by one.
		for (int j = 1; j <= 4; j++) {

			// Start n at 100 000, and keep doubling n until the slowest locking
			// strategy takes 10s
			for (int i = 0; i < 5; i++) {
				solveQuestion(j, cores, (2 << i) * 100000);
			}
		}
	}

	public static void solveQuestion(int q, int nbOfThreads, int n) {

		Benchmarkable b;
		long time = 0;

		switch (q) {
		case 1:
			b = new Synchronized(2, n);

			break;
		case 2:
			b = new SimpleTTAS(2, n);

		case 3:
			b = new BackedOffTTAS(2, n);
			break;

		default:
			b = new CLHLocker(2, n);
			break;
		}

		time = b.benchmark();

		String report = "" + n;
		report += "," + time;
		report += "," + b.getMaxDelay();

		switch (q) {
		case 1:
			report += ", Synchronized";
			break;
		case 2:
			report += ", SimpleTTAS";
			break;
		case 3:
			report += ", backoffTTAS";
			break;
		case 4:
			report += ", CLH";
			break;
		default:
			break;
		}

		System.out.println(report);

	}
}