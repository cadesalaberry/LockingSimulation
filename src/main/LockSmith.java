


public class LockSmith {

	public static void main(String[] args) {
		solveQuestion(1, 2, 100000);
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
		String report = "With a max wait time of " + b.getMaxDelay();
		report += " turns, It took " + time + "ms to run ";

		switch (q) {
		case 1:
			report += "Synchronized lock ";
			break;
		case 2:
			report += "SimpleTTAS lock ";
			break;
		case 3:
			report += "TTAS with exponential back-off lock ";
			break;
		case 4:
			report += "CLH lock ";
			break;
		default:
			break;
		}
		
		report += "for " + n + " points";
		
		System.out.println(report);

	}
}