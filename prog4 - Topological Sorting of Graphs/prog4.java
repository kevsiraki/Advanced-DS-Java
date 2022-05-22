//Kevin Siraki
//Comp 282 3:30-4:45 PM Mon/Wed
//December 2, 2020
/*
prog4.java:
	GraphTopSort class:
		+initPredCounts(): set all vertex predCt()s to 0
		+outputTopSort(): does topological sort:
			1. set inital predecessor counts by traversing the graph
			2. find the ones with 0 predecessors after traversal/incrementation
			3. push ones found to zeroPredQue
			4. set predCt() of those in the que to -1,
			   put these in topSort[],
			   add remaining targets that become 0 after this to the que as well
			5. output by checking if theres a point where there are no -1 predCts, output topSort[] otherwise.
			
*/
// don't forget the usual top comments
class GraphTopSort extends Graph {
    // set all predecessor counts to 0
    public void initPredCounts() {
        Vertex_Node headCopy = this.head; // start at head node(copy)
        while (headCopy != null) {
            headCopy.setPredCt(0);
            headCopy = headCopy.getNext();
        }
    }

    public void outputTopSort() {
        Vertex_Node[] zeroPredecessorQueue = new Vertex_Node[this.size];
        Vertex_Node[] topSort = new Vertex_Node[this.size];
        Edge_Node e;
        int queueFront = 0, queueBack = 0, outputCt = 0;
        Vertex_Node v = this.head;
        Vertex_Node zeroPredQueVertex; // 1 added variable to keep track of current vertex in the zeroPredQue.
        // initPredCounts();
        initPredCounts();
        // set the predecessor counts by visiting all the edges and
        // incrementing the target predecessor counts of these edges
        // implement as simple nested while loops
        while (v != null) {
            e = v.getNbrList(); // stores both the target of v and next
            while (e != null) { // while edges remain to visit (has predecessor(s)) (also has next)
                e.getTarget().setPredCt((e.getTarget().getPredCt()) + 1); // e.targetPred()++, inc. if has predecessor
                e = e.getNext(); // update e
            }
            v = v.getNext(); // update v
        }
        v = this.head; // reset v back to initial head
        // find vertices with predecessor counts of 0 and put them on the queue
        // this will be a while loop
        while (v != null) { // while vertices remain to be removed from the graph
            if (v.getPredCt() == 0) { // find a vertex with no predecessors
                zeroPredecessorQueue[queueBack] = v; // push v on the zeroPredQue
                queueBack++; // increment the back of queue for push/enque
            }
            v = v.getNext(); // update v
        }
        // the main loop
        // remove a vertex from the zero queue, add it to the topSort array,
        // and traverse its edges to update target predecessor counts -- if any
        // become 0, add to the zero queue
        while (queueBack != queueFront) { // while the queue is not empty (check for Que UnderFlow before deque-ing)
            zeroPredQueVertex = zeroPredecessorQueue[queueFront]; // zeroPredVertex is the front of the zeroPredQue
            queueFront++; // inc. front of queue // (pop/deque)
            e = zeroPredQueVertex.getNbrList(); // stores both the target of zero que's vertex and next
            zeroPredQueVertex.setPredCt(-1); // no predecessor, so make it -1 to prepare for delete/save to que
            while (e != null) { // vvv traverse zero que's edges, update TARGET predCts e.target()--.
                e.getTarget().setPredCt(e.getTarget().getPredCt() - 1); // dec. what the one we just removed points to
                if (e.getTarget().getPredCt() == 0) { // push the ones that become 0 to zero que
                    zeroPredecessorQueue[queueBack] = e.getTarget();
                    queueBack++;
                }
                e = e.getNext(); // next edge
            }
            topSort[outputCt] = zeroPredQueVertex; // put in topSort array the one that was deleted.
            outputCt++; // increment the topSort outputCt index
        } // deleting zeroPredQue element, adding it to topSort
          // check if every vertex has been put in the topSort array and either output it
          // on one line or report "loop"
          // Your output should match mine exactly
        v = this.head; // reset v back to initial head
        boolean isLoop = false; // for outputting "Loop if the PredCt is not -1"
        while (v != null) { // while vertices remain to be removed from the graph
            if (v.getPredCt() != -1) // if any vertex wasnt added to the zeroPredQue, theres a loop.
                isLoop = true; // -1 means its been put into the 0 pred. que
            v = v.getNext(); // update v
        }
        if (!isLoop) { // had 0 preds
            for (int i = 0; i < topSort.length; i++)
                System.out.print(topSort[i].getName() + " ");
            System.out.println();
        } else // some vertex didnt reach 0 preds
            System.out.println("Loop");
    }

    public static String myName() {
        return ("Kevin Siraki");
    }
}