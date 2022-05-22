//Kevin Siraki
//Comp 282 3:30-4:45 PM Mon/Wed
//September 28th, 2020
//prog2.java contains a StringNode Class, a mutable WrapString class
//and a SplayBST class which is just a splay tree implementation that uses strings to store a value and key

//test0 output:
/*
 * 
insertion tests.

fred-0
(fred-1)joanie-0
((fred-2)joanie-1)lynn-0
(((fred-3)joanie-2)lynn-1)peter-0
(fred-1)helen-0((joanie-2)lynn-1(peter-2))
(((((((fred-7)helen-6)hope-5)ian-4)joanie-3)john-2)kelly-1((lynn-3)miki-2))nichole-0(peter-1(tom-2))


search tests.
Found.
(((((((fred-7)helen-6)hope-5)ian-4)joanie-3)john-2)kelly-1((lynn-3)miki-2))nichole-0(peter-1(tom-2))
Found.
((fred-2)helen-1)hope-0(((ian-3(joanie-4))john-2(kelly-3((lynn-5)miki-4)))nichole-1(peter-2(tom-3)))
Found.
(((fred-3)helen-2)hope-1((ian-3(joanie-4))john-2(kelly-3((lynn-5)miki-4))))nichole-0(peter-1(tom-2))
Not in the tree.
((((fred-4)helen-3)hope-2(ian-3(joanie-4)))john-1(kelly-2))lynn-0((miki-2)nichole-1(peter-2(tom-3)))
Not in the tree.
((((fred-4)helen-3)hope-2)ian-1)joanie-0(john-1((kelly-3)lynn-2((miki-4)nichole-3(peter-4(tom-5)))))
Not in the tree.
(((fred-3)helen-2)hope-1)ian-0(joanie-1(john-2((kelly-4)lynn-3((miki-5)nichole-4(peter-5(tom-6))))))
*/

//Node that is String
class StringNode {
    //key+value of the StringNode instance
    private String word = "";
    //left/right children of node
    private StringNode left, right;

    //constructor // The only constructor you will need
    public StringNode(String w) {
        word = w;
		  this.left = null;
		  this.right = null;
    }

    //getter for left child
    public StringNode getLeft() {
        return left;
    }

    //setter for left child
    public void setLeft(StringNode pt) {
        this.left = pt;
    }

    //getter for right child
    public StringNode getRight() {
        return right;
    }

    //setter for right child
    public void setRight(StringNode pt) {
        this.right = pt;
    }

    //getter for word. no need for a setter here. see line 77 :)
    public String getString() {
        return word;
    }
}

//class allowing for strings to be changed.. (mutable strings)
// So that a String can change. There is nothing you need to add// to this class 
class WrapString {
    //string itself
    // Yes, I am allowing (and encouraging) direct access to the String (thus I did not add getters and setters for this public member!)
    public String string;

    //wrapstring constructor 
    public WrapString(String str) {
        this.string = str;
    }
}

 //splay tree implemntaion. last node visited is isSplayed until it reaches root.
class SplayBST {
    //root of tree
    // member variable pointing to the root of the splay tree// It really should be private but I need access to it for the test program
    StringNode root;

    //root getter
    public StringNode getRoot() {
        return root;
    }

    //constructor 1
    // default constructor
    public SplayBST() {
         root = null; 
    }
    
    //copy constructor
    // copy constructor   // Be sure to make a copy of the entire tree   // Do not make two pointers point to the same tree
    public SplayBST(SplayBST t) {   
         this.root = t.copyOf(t.root);
         //this.root = t.root;  this is an example of what not to do.. caused a lot of issues when inserting into copied tree...
    }
    
    //copy helper recursive method. needed for copy constructor.
	//referring to my last comment, this is NEEDED to make a deep copy of a BST, works on any type of BST as well.
    public StringNode copyOf(StringNode originalRoot) {
         StringNode newNode = null;
         if (originalRoot==null) 
            newNode = null;
         else {
            newNode = new StringNode(originalRoot.getString());
            newNode.setLeft(copyOf(originalRoot.getLeft()));
            newNode.setRight(copyOf(originalRoot.getRight()));
         }
         return newNode;
     } 

    //my name. See comments above as well.
    // like last time
    public static String myName() {
        return "Kevin Siraki";
    }

	// this is the recursive search driver.
	//we either splay a found node to the root or just splay the last node we went to to the root
    public StringNode search(String s) {
        WrapString str = new WrapString(s);
        root = search(str, root); // newly isSplayed root
        root = splay(root, str.string); //splay the string to the root
        StringNode node; //node we will eventually return, avoiding any extra returns.
        if (str.string.compareToIgnoreCase(s) == 0) { //is it in the tree
            node = root; //yes. put s at root, return s.
        } else {
            node = null; //no. return NOTHING!
        }
        return node;
    }

    
	 //the search method itself
	 /*follows this idea:
	 is the string in the tree? 
	 yes, return the node where it was found
	 no, mutate the latest node that we went to to contain the string.
	 after each call, we test to see if we should do zig-zag, zig-zig, or zag-zag rotations and splay.
    // if s is not in the tree, splay the last node visited// final zig, if needed, is done here// Return null if the string is not found
	 */
    public static StringNode search(WrapString str, StringNode t) {
        String val = str.string; //val has string to search for.
        //if (t == null) { //do nothing at this base case
        //} 
        if(t!=null) {
            int compTo = t.getString().compareToIgnoreCase(val);
            //comparison of str to current node.
            if (compTo > 0) { //str is less than current node; search left
                t.setLeft(search(str, t.getLeft())); 
                if (t.getLeft() == null) {
					//if the node we are looking for is null, its not in the tree
                    str.string = (t.getString()); //storing value of node to splay
                }
                val = str.string; 
                boolean isSplayed = false; //not isSplayed yet..
                //is the left child the one to splay??? if yes then next the 3rd iff following this one wont run!
				//or else it will
                if (t.getLeft() != null) {
                    if (t.getLeft().getLeft() != null) {
                        compTo = t.getLeft().getLeft().getString().compareToIgnoreCase(val);
                        if (compTo == 0) {
                            //left left = zig zig.
                            t = rotateRight(t); //rotate parent of parent
                            t = rotateRight(t); //rotate parent
                            isSplayed = true; //yes
                        }
                    }
                    //no
                    if (!isSplayed) {
                        if (t.getLeft().getRight() != null) {
                            compTo = t.getLeft().getRight().getString().compareToIgnoreCase(val);
                            if (compTo == 0) {
                                //left right = zig zag.
                                t.setLeft(rotateLeft(t.getLeft())); //rotate parent
                                t = rotateRight(t); //rotate parent of parent
                                isSplayed = true; //doesnt really matter. could honestly go without doing this here.
                            }
                        }
                    }
                }
            } else if (compTo < 0) { //str is > than current node; search right
                t.setRight(search(str, t.getRight()));
                if (t.getRight() == null) {
					//if the node we are looking for is null, its not in the tree
                    str.string = (t.getString()); //storing value of node to splay
                }
                val = str.string; 
                boolean isSplayed = false; //same exact code as the top but we are just in the right subtrees now

                //see above comments but replace left with right.

                if (t.getRight() != null) {
                    if (t.getRight().getLeft() != null) {
                        compTo = t.getRight().getLeft().getString().compareToIgnoreCase(val);
                        if (compTo == 0) {
                            //right left : zig zag				
                            t.setRight(rotateRight(t.getRight())); //rotate parent; 
                            t = rotateLeft(t); //rotate parents parent; possible to also do this in my splay method if only I had made a .parent method. just wanted to keep that one short.
                            isSplayed = true;
                        }
                    }
                    //no
                    if (!isSplayed) {
                        if (t.getRight().getRight() != null) {
                            compTo = t.getRight().getRight().getString().compareToIgnoreCase(val);
                            if (compTo == 0) {
                                //right right : zag zag
                                t = rotateLeft(t); //rotate parents parent
                                t = rotateLeft(t); //rotate parents
                                isSplayed = true; 
                            }
                        }
                    }
                }
            } else { 
                //Base Case if node containing s is found: return current node
            }
        }
        return t;
    }

    
	// the insert recursive driver
	//also calling my splay method to splay each insert to the root. 
   // This is the driver method. You should also check for and perform// a final zig here// You will also have to write the 2-parameter recursive insert method
    public void insert(String s) {
        root = splay(insert(root, s), s); //splay to root
    }

	//similar to other inserts but also checks for the different zig zag zig zig zag zag cases.
    private static StringNode insert(StringNode root, String s) {
        if (root == null) {
           //base case, root empty
            root = new StringNode(s);
        } else { //root not empty
            int compTo = root.getString().compareTo(s); //compTo is where we store string comparison between root and s
            if (compTo > 0) { //s<root, go left
                root.setLeft(insert(root.getLeft(), s));
                boolean isSplayed = false; //no splay yet....

                //do we splay the left or right child?
				//left?

                if (root.getLeft().getLeft() != null) {
                    compTo = root.getLeft().getLeft().getString().compareToIgnoreCase(s);
                    if (compTo == 0) {
                        //left left zig zig
                        root = rotateRight(root); //rotate parents parent
                        root = rotateRight(root); //rotate parent
                        isSplayed = true; //yes
                    }
                }
                //not isSplayed? 
                if (!isSplayed) {
                    if (root.getLeft().getRight() != null) {
                        compTo = root.getLeft().getRight().getString().compareToIgnoreCase(s);
                        if (compTo == 0) {
                            //left right zig zag
                            root.setLeft(rotateLeft(root.getLeft())); //rotate  parent
                            root = rotateRight(root); //rotate parents parent
                            isSplayed = true; //yes
                        }
                    }
                }
            } else if (compTo < 0) { //s>root, go right
                root.setRight(insert(root.getRight(), s));
                boolean isSplayed = false; 

                //right??

                if (root.getRight().getLeft() != null) {
                    compTo = root.getRight().getLeft().getString().compareToIgnoreCase(s);
                    if (compTo == 0) {
                        //right left zig zag
                        root.setRight(rotateRight(root.getRight())); //rotate parent
                        root = rotateLeft(root); //rotate parents parent
                        isSplayed = true;
                    }
                }
                //not isSplayed? 
                if (!isSplayed) {
                    if (root.getRight().getRight() != null) {
                        compTo = root.getRight().getRight().getString().compareToIgnoreCase(s);
                        if (compTo == 0) {
                            //right right zag zag
                            root = rotateLeft(root); //rotate parents parent
                            root = rotateLeft(root); //rotate original parent
                            isSplayed = true; 
                        }
                    }
                } else { 
                    //already in tree. 
                }
            }
        }
        return root;
    }

   //standard left rotation.
    public static StringNode rotateLeft(StringNode t) {
        if (t == null) {
            //do nothing in this base case.
        } else {
            //iff theres a left child of the root just rotate.
            if (t.getRight() != null) {
                StringNode newRoot = t.getRight(); //newRoot = old root t's right child
                StringNode newRootLeftChild = newRoot.getLeft(); //newroot old left child
                newRoot.setLeft(t); //left = original root
                t.setRight(newRootLeftChild); // make old right into new left
                t = newRoot; //new root 
            } else {
                //right child node null.
                //do nothing
            }
        }
        return t;
    }

	 // a standard right rotation. 
    public static StringNode rotateRight(StringNode t) {
        if (t == null) {
            //do nothing in this base case
        } else {
            //iff theres a left child of the root just rotate.
            if (t.getLeft() != null) {
                StringNode newRoot = t.getLeft(); //newRoot = old root t's left child
                StringNode newRootRightChild = newRoot.getRight(); //newroot old right child
                newRoot.setRight(t); //right = original root
                t.setLeft(newRootRightChild); // make old left into new right
                t = newRoot; //new root
            } else {
                //left child node null.
                //do nothing
            }
        }
        return t;
    }

    
	 //a method I added to check if a left/right zig operation is needed before splaying to the root
	 //if it is not necessary, we do nothing
    private static StringNode splay(StringNode root, String s) {
        if (root != null) {
            int compTo = root.getString().compareToIgnoreCase(s);
            //compare s to current node.
            if (compTo > 0) { //s is the left child of root;
                //zig right
                root = rotateRight(root);
            } else if (compTo < 0) { //s is the right child of root;
                //zig left
                root = rotateLeft(root);
            } //else { 
			   //already isSplayed 
            //method will do nothing
			//}
        }
        return root;
    }
    
	//leafct recursive driver
    public int leafCt() {
        return leafCt(root);
    }

	//how many nodes have 2 null children (both left and right are null).
   // How many leaves in the splay tree?
    private static int leafCt(StringNode root) {
        int numLeafNodes = 0;
        if (root != null) {
            //the number of leaf nodes in this tree will always be a sum of
            //the number of leaf nodes in its subtrees
            numLeafNodes = leafCt(root.getLeft()) + leafCt(root.getRight());
            if (numLeafNodes == 0) {
                //both children are null, this is a leaf.
                numLeafNodes = 1;
            }
        } else {
            //root is null; do nothing
        }
        return numLeafNodes;
    }

    //height recursive driver.
    public int height() {
        return height(root);
    }

    //find the height of a bst.
    // What is the height the splay tree?
    private static int height(StringNode root) {
        int height = 0;
        if (root != null) {
            //include root and add up both left and right subtrees recursively.
            height = 1 + Math.max(height(root.getLeft()), height(root.getRight()));
            //System.out.println(height);
        }
        return height;
    }

    //stickct recursive driver
    public int stickCt() {
        return stickCt(root);
    }
      
    //stickct method to see how many nodes have 1 non null child
    // How many nodes have exactly 1 non-null children
    private static int stickCt(StringNode root) {
        int oneChildNodes = 0;
        if (root != null) {
            if (root.getLeft() != null && root.getRight() == null) {
                //1 child node isnt null
                oneChildNodes = 1 + stickCt(root.getLeft());
            } else if (root.getLeft() == null && root.getRight() != null) {
                //1 child node isnt null
                oneChildNodes = 1 + stickCt(root.getRight());
            } else if (root.getLeft() != null && root.getRight() != null) {
                //2 child nodes, neither null, recurse again!
                oneChildNodes = stickCt(root.getLeft()) + stickCt(root.getRight());
            } else {
                //2 child nodes are null, nothing to do here.
            }
        }
        return oneChildNodes;
    }
}
//commented after testing.

class Test0 {

    public static void main(String[] args) {
        SplayBSTXtra t = new SplayBSTXtra();
        System.out.println("insertion tests.");
        System.out.println(t);
        t.insert("fred");
        System.out.println(t);
        t.insert("joanie");
        System.out.println(t);
        t.insert("lynn");
        System.out.println(t);
        t.insert("peter");
        System.out.println(t);
        t.insert("helen");
        System.out.println(t);
        t.insert("joanie");
        t.insert("hope");
        t.insert("ian");
        t.insert("tom");
        t.insert("miki");
        t.insert("john");
        t.insert("kelly");
        t.insert("nichole");
        System.out.println(t.stickCt());
        SplayBSTXtra q = new SplayBSTXtra(t); //TESTING MY COPY CONTRUCTOR! tested by doing an insert to it. 
        //q.insert("extra insert!"); //test inserting to both. see behaviour.
        //t.insert("O.O");
        System.out.println(q);
        System.out.println(t);
        System.out.println();
        System.out.println();
        System.out.println("search tests.");
        if (t.search("nichole") == null)
            System.out.println("Not in the tree.");
        else
            System.out.println("Found.");
        System.out.println(t);
        if (t.search("hope") == null)
            System.out.println("Not in the tree.");
        else
            System.out.println("Found.");
        System.out.println(t);
        if (t.search("nichole") == null)
            System.out.println("Not in the tree.");
        else
            System.out.println("Found.");
        System.out.println(t);
        if (t.search("larry") == null)
            System.out.println("Not in the tree.");
        else
            System.out.println("Found.");
        System.out.println(t);
        if (t.search("jack") == null)
            System.out.println("Not in the tree.");
        else
            System.out.println("Found.");
        System.out.println(t);
        if (t.search("jack") == null)
            System.out.println("Not in the tree.");
        else
            System.out.println("Found.");
        System.out.println(t);
        //t.height();
        
    }
}

class SplayBSTXtra extends SplayBST {

    public SplayBSTXtra() {
        super();
    }
    
    public SplayBSTXtra(SplayBSTXtra t) { 
        super(t); //copy constructor
    }

    public StringNode getRoot() {
        return root;
    }

    // for output purposes -- override Object version
    public String toString() {
        return toString(getRoot(), 0);
    }

    private static String toString(StringNode l, int x) {
        String s = "";
        if (l == null)
        ; // nothing to output
        else {
            if (l.getLeft() != null) // don't output empty subtrees
                s = '(' + toString(l.getLeft(), x + 1) + ')';
            s = s + l.getString() + "-" + x;
            if (l.getRight() != null) // don't output empty subtrees
                s = s + '(' + toString(l.getRight(), x + 1) + ')';
        }
        return s;
    }
}
