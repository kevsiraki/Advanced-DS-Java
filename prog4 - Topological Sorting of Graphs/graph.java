import java.io.*; // for BufferedReader
import java.util.*; // for StringTokenizer

class Edge_Node {
	private Vertex_Node target;
	private Edge_Node next;

	public Edge_Node(Vertex_Node t, Edge_Node e) {
		target = t;
		next = e;
	}

	public Vertex_Node getTarget() {
		return target;
	}

	public Edge_Node getNext() {
		return next;
	}
}

class Vertex_Node {
	private String name;
	private Edge_Node edge_head;
	private int predCt;
	private Vertex_Node next;

	public Vertex_Node(String s, Vertex_Node v) {
		name = s;
		next = v;
		predCt = -1;
	}

	public String getName() {
		return name;
	}

	public int getPredCt() {
		return predCt;
	}

	public void setPredCt(int d) {
		predCt = d;
	}

	public Edge_Node getNbrList() {
		return edge_head;
	}

	public void setNbrList(Edge_Node e) {
		edge_head = e;
	}

	public Vertex_Node getNext() {
		return next;
	}
}

class Graph {
	public Vertex_Node head;
	public int size;

	public Graph() {
		head = null;
		size = 0;
	}

	public Vertex_Node findVertex(String s) {
		Vertex_Node pt = head;
		while (pt != null && s.compareTo(pt.getName()) != 0)
			pt = pt.getNext();
		return pt;
	}

	public void input(String fileName) throws IOException {
		String inputLine, sourceName, targetName;
		Vertex_Node source = null, target;
		Edge_Node e;
		StringTokenizer input;
		BufferedReader inFile = new BufferedReader(new FileReader(fileName));
		inputLine = inFile.readLine();
		while (inputLine != null) {
			input = new StringTokenizer(inputLine);
			sourceName = input.nextToken();
			source = findVertex(sourceName);
			if (source == null) {
				head = new Vertex_Node(sourceName, head);
				source = head;
				size++;
			}
			if (input.hasMoreTokens()) {
				targetName = input.nextToken();
				target = findVertex(targetName);
				if (target == null) {
					head = new Vertex_Node(targetName, head);
					target = head;
					size++;
				}

				// put edge in one direction -- while checking for repeat
				e = source.getNbrList();
				while (e != null) {
					if (e.getTarget() == target) {
						System.out.print("Multiple edges from " + source.getName() + " to ");
						System.out.println(target.getName() + ".");
						break;
					}
					e = e.getNext();
				}
				source.setNbrList(new Edge_Node(target, source.getNbrList()));
			}
			inputLine = inFile.readLine();
		}
		inFile.close();
	}

	// You might find this helpful when debugging so that you
	// can see what the graph actually looks like
	public void output() {
		Vertex_Node v = head;
		Edge_Node e;
		while (v != null) {
			System.out.print(v.getName() + ":  ");
			e = v.getNbrList();
			while (e != null) {
				System.out.print(e.getTarget().getName() + "  ");
				e = e.getNext();
			}
			System.out.println();
			v = v.getNext();
		}
	}
}
