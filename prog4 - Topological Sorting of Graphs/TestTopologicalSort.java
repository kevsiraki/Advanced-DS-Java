import java.io.*; // for IOException

public class TestTopologicalSort {

	// Set this string to be the path to the test graphs
	final static String GraphLocation = new String(
			"C:\\Users\\kevsi\\Desktop\\prog4\\");

	public static void main(String[] args) throws IOException {
		GraphTopSort g;

		for (int i = 1; i <= 11; i++) {
			g = new GraphTopSort();
			g.input(GraphLocation + "Graph" + i + ".txt");
			System.out.println("Test #" + i + ":  Topological Sort  -- " + GraphTopSort.myName());
			System.out.println("=======");
			g.outputTopSort();
			//g.output();
			System.out.println();
			if (i == 3) {
				System.out.println("=======");
				g.outputTopSort();
				System.out.println();

			}
		}
		System.out.println("Done with " + GraphTopSort.myName() + "'s test run.");
	}
}
