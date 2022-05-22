//Kevin Siraki
//Comp 282 3:30-4:45 PM Mon/Wed
//November 16th, 2020
/*prog3.java contains a ArraySorts class and a pair class.
Contents:
ArraySorts:
-insertion sort
-5 variants of QS respectively
-3 repeates of first 3/5 above with no insertion sort for partial QS
-methods to build a heap top down or bottom up
-trickle down and up methods
-heapsorts for both TD and BU
-a generic but altered swap method
-3 partitioning algorithms 
pair:
-some constructer 
-getLeft and getRight methods
*/
// Get random numbers the way you did in the first program. I think most of you
// used the Random class. If you use the Random class be sure not to instantiate
// multiple, unnecessary copies. (i will try)
//import java.math.*; // maybe not needed? //not needed ;)!
// or
import java.util.Random;

// use this class to contain everything related to your sorts
class ArraySorts {
   /*
    * 1. The test program ends with a bunch of tests to see how well your program
    * works with large arrays. Part of it includes testing your sorts with
    * non-random data. I call these the "test sets." One particular test in the
    * test sets is an array where nearly every element is the same. There is a good
    * chance a number of your quicksorts will be very very slow with this data. As
    * a result I ask you to include one more method in your ArraySorts class:
    */
   public static boolean testSetsWork() {
      return true;
   }

   // Some sample driver method headers
   // Note:
   // alteration of the IS in the book, book used a template class T
   // I removed the template and the compareTo as this is int only
   public static void insertionSort(int a[], int n) { // Insertion Sort
      for (int unsorted = 1; unsorted < n; ++unsorted) {
         int nextItem = a[unsorted];
         int loc = unsorted;
         while ((loc > 0) && (a[loc - 1] > nextItem)) { // shift
            a[loc] = a[loc - 1];
            loc--;
         }
         a[loc] = nextItem; // save
      }
   }// Insertion Sort

   // quicksorts (introsort/introspective in a way)

   // qs1
   // driver
   public static void QuickSort1(int a[], int n, int cutoff) {
      if ((cutoff <= 1)) // for deciding if moving to IS or not (partial QS)
         cutoff = 2; // cannot sort if the cutoff is less than 2.
      Random rand = new Random();
      QuickSort1(a, 0, n - 1, cutoff, rand);
      insertionSort(a, n);
   }

   // method
   private static void QuickSort1(int[] a, int lf, int rt, int cutoff, Random rand) {
      int pivot, lfpt, rtpt;
      while ((rt - lf + 1) >= cutoff) { // if rt-lf+1 was to be less than the cutoff we would do insertion sort.
         pivot = rand.nextInt(rt - lf) + lf;
         pivot = outsideIn(a, lf, rt, pivot);
         lfpt = pivot + 1; // lfpt and rtpt crossed. we can start at lfpt+1 here
         rtpt = pivot; // rtpt should now contain the pivot index
         if ((rtpt - lf) < (rt - lfpt)) { // find smaller partition. recursively call qs on it
            QuickSort1(a, lf, rtpt, cutoff, rand); // left smaller
            lf = lfpt; // update lf
         } else {
            QuickSort1(a, lfpt, rt, cutoff, rand); // right smaller
            rt = rtpt; // update rt
         }
      }
   }

   // qs2
   // driver
   public static void QuickSort2(int a[], int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      Random rand = new Random();
      QuickSort2(a, 0, n - 1, cutoff, rand);
      insertionSort(a, n);
   }

   // method //same comments as 1 generally
   private static void QuickSort2(int[] a, int lf, int rt, int cutoff, Random rand) {
      int pivot, lfpt, rtpt;
      while ((rt - lf + 1) >= cutoff) {
         pivot = rand.nextInt(rt - lf) + lf;
         pivot = leftToRight1(a, lf, rt, pivot);
         lfpt = pivot + 1;
         rtpt = pivot - 1;
         if ((rtpt - lf) < (rt - lfpt)) { // find smaller partition. recursively call qs on it
            QuickSort2(a, lf, rtpt, cutoff, rand);
            lf = lfpt;
         } else {
            QuickSort2(a, lfpt, rt, cutoff, rand);
            rt = rtpt;
         }
      }
   }

   // qs3
   // driver
   public static void QuickSort3(int[] a, int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      Random rand = new Random();
      QuickSort3(a, 0, n - 1, cutoff, rand);
      insertionSort(a, n);
   }

   // method
   private static void QuickSort3(int[] a, int lf, int rt, int cutoff, Random rand) {
      // we must have 3 partition. sizes of partitions
      int lfPartition, rtPartition, midPartition;
      // pivots points
      pair partitionPoints;
      // pointers to pp
      int lfpt, rtpt;
      while ((rt - lf + 1) >= cutoff) {
         if (lf < (rt + 1)) {
            partitionPoints = leftToRight2(a, lf, rt, rand); // lf begins as 0, rt begins as n-1.
            lfpt = partitionPoints.getLeft(); // lastSmall
            rtpt = partitionPoints.getRight();// firstBig
            lfPartition = (lfpt - 1) - lf;
            rtPartition = rt - (rtpt + 1);
            // middle partition. simply subtract lf pp from rt pp
            // point
            // to get its size
            midPartition = rtpt - lfpt;
            // 3 cases as to where we call quicksort
            // left partition bigger than both middle and right partitions
            if (lfPartition > rtPartition && lfPartition > midPartition) { // make recusrive calls on
               QuickSort3(a, rtpt + 1, rt, cutoff, rand); // right to end //the smaller partitions
               QuickSort3(a, lfpt + 1, rtpt - 1, cutoff, rand);// mid //in each of the 3 cases
               rt = lfpt - 1; // rt is now the end of the left partition ls
            } else if (midPartition > lfPartition && rtPartition < midPartition) { // middle>both partitions
               QuickSort3(a, lf, lfpt - 1, cutoff, rand); // left to ls
               QuickSort3(a, rtpt + 1, rt, cutoff, rand); // fb to rt (rtpt+1 corresponds to first of right)
               rt = rtpt - 1; // rt start of right partition fb
               lf = lfpt + 1; // lf end of left partition ls
            } // right partition bigger than both middle and left partitions
            else if (rtPartition > lfPartition && rtPartition > midPartition) {
               QuickSort3(a, lf, lfpt - 1, cutoff, rand); // left to ls (lfpt-1 corresponds to last of left)
               QuickSort3(a, lfpt + 1, rtpt - 1, cutoff, rand); // mid (calling quicksort between ls and fb)
               lf = rtpt + 1; // lf now start of right partition fb
            } else {
               // do nothing in this case. no proper partitions.
            }
         } else {
            // do nothing, index out of bounds.
         }
      }
   }

   // qs4
   // driver //same comments as 1-2 generally
   public static void QuickSort4(int[] a, int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      QuickSort4(a, 0, n - 1, cutoff);
      insertionSort(a, n);
   }

   // method
   private static void QuickSort4(int[] a, int lf, int rt, int cutoff) {
      int pivot, lfpt, rtpt;
      while ((rt - lf + 1) >= cutoff) {
         pivot = lf;
         pivot = outsideIn(a, lf, rt, pivot);
         lfpt = pivot + 1; // lfpt and rtpt crossed.
         rtpt = pivot; // rtpt should now contain the pivot index
         if ((rtpt - lf) < (rt - lfpt)) { // find smaller partition. recursively call qs on it
            QuickSort4(a, lf, rtpt, cutoff);
            lf = lfpt;
         } else {
            QuickSort4(a, lfpt, rt, cutoff);
            rt = rtpt;
         }
      }
   }

   // qs5
   // driver //same comments as 1-2,4 generally
   public static void QuickSort5(int[] a, int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      QuickSort5(a, 0, n - 1, cutoff);
      insertionSort(a, n);
   }

   // method
   private static void QuickSort5(int[] a, int lf, int rt, int cutoff) {
      int pivot, lfpt, rtpt;
      while ((rt - lf + 1) >= cutoff) {
         pivot = lf;
         pivot = leftToRight1(a, lf, rt, pivot);
         lfpt = pivot + 1;
         rtpt = pivot - 1;
         if ((rtpt - lf) < (rt - lfpt)) { // find smaller partition. recursively call qs on it
            QuickSort5(a, lf, rtpt, cutoff);
            lf = lfpt;
         } else {
            QuickSort5(a, lfpt, rt, cutoff);
            rt = rtpt;
         }
      }
   }

   // aqs1
   // driver only
   public static void AlmostQS1(int a[], int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      Random rand = new Random();
      QuickSort1(a, 0, n - 1, cutoff, rand);
      // insertionSort(a,n);
   }

   // aqs2
   // driver only
   public static void AlmostQS2(int a[], int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      Random rand = new Random();
      QuickSort2(a, 0, n - 1, cutoff, rand);
      // insertionSort(a, n);
   }

   // aqs3
   // driver only
   public static void AlmostQS3(int[] a, int n, int cutoff) {
      if ((cutoff <= 1))
         cutoff = 2;
      Random rand = new Random();
      QuickSort3(a, 0, n - 1, cutoff, rand);
      // insertionSort(a, n);
   }

   // heapsorts pg 679
   // for my reference
   // Given a node a[i], its
   // parent is a[(i-1)/2] and its two children, if they exist, are a[2×i + 1] and
   // a[2×i + 2].
   // 1 trickle-down helper //(shifts down)
   private static void trickleDown(int[] a, int i, int n) { // needs last parameter b/c of BU's for loop
      int node = a[i]; // current node in the heap/ "tree"
      int childOne = 2 * i + 1; // left child
      int childTwo = 2 * i + 2; // right child
      boolean ifOneExists = false; // both initially false until loop
      boolean ifTwoExists = false;
      boolean someChildExists = true;
      while (someChildExists) {
         childOne = 2 * i + 1; // I think of this as the left child
         childTwo = 2 * i + 2; // I think of this as the right child
         ifOneExists = false;
         ifTwoExists = false;
         if (childOne < n) // are they in the array..
            ifOneExists = true; // the child(ren) do not exist if their index is>length
         if (childTwo < n)
            ifTwoExists = true;
         if (ifTwoExists) { // check if RIGHT child (two) exists
            if ((a[childOne] < a[childTwo]) && a[childTwo] > node) { // first child<second childs and second<the node
               a[i] = a[childTwo]; // trickle down to this index
               i = childTwo;
            } else if (node < a[childOne]) { // the node is<first child
               a[i] = a[childOne]; // trickle down to this index
               i = childOne;
            } else // break;
               someChildExists = false; // no children left to trickle down, was using break but this is more elegant.
         } else if (ifOneExists && node < a[childOne]) { // see if LEFT child (one) exists, check the same thing.
            a[i] = a[childOne];
            i = childOne;
         } else // break;
            someChildExists = false;
      }
      a[i] = node; // put back the node.
   }

   // p. 670s
   // 2 trickle-up helper (shifts up)
   // parent is a[(i-1)/2]
   private static void trickleUp(int[] a, int i) {
      int parent = (i - 1) / 2; // parent of current node passed in
      int node = a[i];// current node passed in
      while (i - 1 >= 0 && parent >= 0 && node > a[parent]) {// is the index in bounds, is the parents index in bounds,
                                                             // and is the node more than its parent?
         a[i] = a[parent]; // trickle up the parent to where the node is (as a parent cant be less than its
                           // child)
         i = parent;// update the index
         parent = (i - 1) / 2;
      }
      a[i] = node; // put back the node.
   }

   // from lec. 10
   // 1 building a heap from bottom to top helper O(n) (linear comp.)
   private static void buildHeapBottomUp(int[] a, int n) {
      for (int i = n - 1; i >= 0; i--) // start from the end/bottom of the array
         trickleDown(a, i, n); // trickle down
   }

   // 2 building a heap from top to bottom. helper, O(n*logn) (trickles up)
   private static void buildHeapTopDown(int[] a, int n) {
      for (int i = 1; i < n; i++) // start from the beginning/top of the array
         trickleUp(a, i); // trickle up
   }

   // 1 BU (nlogn best/avg/worst)
   public static void HeapSortBU(int[] a, int n) {
      buildHeapBottomUp(a, n); // buildheap linear complexity O(n)
      for (int i = n - 1; i > 0; i--) { // start from the end of the array
         swap(a, 0, i); // swap everything from the end to the beginning
         trickleDown(a, 0, i); // trickle down each i to the start
      }
   }

   // 2 TD nlogn best/avg/worst)
   public static void HeapSortTD(int[] a, int n) {
      buildHeapTopDown(a, n); // buildheap logarithmically O(nlogn)
      for (int i = n - 1; i > 0; i--) { // start from the end of the array
         swap(a, 0, i); // swap everything from the end to the beginning
         trickleDown(a, 0, i); // trickle down each i to the start
      }
   }

   // swap PRIVATE HELPER (alteration of the one from prog 1).
   private static void swap(int[] a, int x, int y) {
      int temp = a[x];
      a[x] = a[y];
      a[y] = temp;
   }

   // partitioning algorithms
   // outside in, check for lfpt and rtpt cross.
   private static int outsideIn(int[] a, int lfpt, int rtpt, int pivotIndex) {
      swap(a, pivotIndex, lfpt);// First swap the pivot to the left of the array.
      pivotIndex = lfpt;
      int pivotElement = a[pivotIndex];
      while (lfpt <= rtpt) { // check for cross
         while (lfpt != rtpt && a[lfpt] < pivotElement) // cant == since cant inc. at end of array.
            lfpt++; // a[lfpt]<pivotElement case, increment lfpt to the right
         while (lfpt != rtpt && a[rtpt] > pivotElement) // cant == since cant dec. at start of array.
            rtpt--; // a[rtpt]>pivotElement case, decrement rtpt to the left
         if (lfpt < rtpt || lfpt > rtpt) { // they arent crossing still,
            swap(a, lfpt, rtpt);// swap (values are on wrong sides of a)
            lfpt++; // keep advancing lftp to the right
            rtpt--; // keep decrementing rtpt to the left
         } else if (a[lfpt] <= pivotElement) // try to make the two cross, if theres a duplicate increment
            lfpt++; // lfpt value less than or equals the pivot value itself
         else // rtpt value more than pivot value
            rtpt--; // move rtpt left
      }
      swap(a, rtpt, pivotIndex); // Now put the pivot between the two partitions by swapping the pivot with the
      return rtpt; // value at the rtpt
   }

   // l2rfollows pg 541, while loop instead of for loop.
   // ls = last small = last index of smaller partition.
   private static int leftToRight1(int[] a, int ls, int rtpt, int pivotIndex) {
      int fu;
      int pivotElement = a[pivotIndex];
      swap(a, pivotIndex, ls); // simply swap pivot and first ELEMENTS.
      pivotIndex = ls; // same for the index
      fu = ls + 1; // sets the unkown region to a[ls+1]
      // boolean alternatingBoolean = false;
      while (fu <= rtpt) { // fu less than or equal last //i find it impossible to use books for loop
                           // as the foor loop makes it hard to check duplicates like this.
         // alternatingBoolean = !alternatingBoolean; //works to force swapping
         // sometimes, helps w duplicates.
         if (a[fu] <= pivotElement) {
            if (a[fu] == pivotElement && fu % 2 == 0) { // fu%x==0 is another way we can if several duplicates
               fu++; // duplicate only advance fu, since the array may have a lot.
            } else { // here we have something where a[fu]<=pivots element so
               ls++; // we have to not only incrememnt both lastSmall and firstUnknown but swapping
                     // them as well
               swap(a, ls, fu); // move fu into ls
               fu++;
            }
         } else // a[fu]>=pivotElement, in the correct partition so advance fu
            fu++; // increment fu
      }
      swap(a, pivotIndex, ls); // swap theArray[first] with theArray[ls]
      return ls;
   }

   // left-right with 2 (rand) pivots
   // PAGE 539,541:
   // first unknown region goes up by 1 each iteration
   // it is the last smallest index + 1.
   // ls = last element of small //fb = first elemnt of big partition
   // mid will be between ls and fb
   private static pair leftToRight2(int[] a, int lfpt, int rtpt, Random rand) {
      pair pr; // pair to return later
      // storing the small pivot values and indices
      // smallIndex and bigIndex correspond to the indices of smaller and bigger
      // pivots
      int smallIndex, bigIndex, smallValue, bigValue; // bigValue = bigger fb pivot value, smallValue = smaller ls pivot
                                                      // value
      // index of last item in smaller partition and index of first item in larger
      // partition.
      int ls, fb;
      // the two pivots (if a>2 elem.)
      int pivotOne, pivotTwo;
      int fu;
      if ((rtpt - lfpt) < 2) { // array has 2 elements. find biggest and swap.
         if (a[lfpt] >= a[rtpt]) // cant really make 3 partitions with 2 pointers so if lfpt was > that rtpt we
                                 // swap it, if rtpt is already bigger leave it alone.
            swap(a, lfpt, rtpt);
         ls = lfpt;
         fb = rtpt;
      } else {
         pivotOne = rand.nextInt(rtpt - lfpt) + lfpt; // first pivot between lfpt and rtpt
         swap(a, lfpt, pivotOne);
         pivotOne = lfpt; // first pivot index and first pivot values now swapped with lfpt
         // second pivot after the first
         pivotTwo = rand.nextInt(rtpt - (lfpt + 1)) + (lfpt + 1); // random, after the first pivot though.
         if (a[pivotTwo] > a[pivotOne]) { // find larger/smaller pivot
            // second bigger
            smallIndex = pivotOne; // smallIndex/Value = the smaller one
            smallValue = a[pivotOne];
            bigIndex = pivotTwo;
            bigValue = a[pivotTwo];
         } else {
            // first bigger
            bigIndex = pivotOne;
            bigValue = a[pivotOne];
            smallIndex = pivotTwo;
            smallValue = a[pivotTwo];
         }
         // put smaller pibot into the first index.
         swap(a, lfpt, smallIndex);
         if (bigIndex == lfpt)
            bigIndex = smallIndex; // if the bigIndex was the same as lfpt, bigIndex = smallIndex
         swap(a, rtpt, bigIndex); // rtpt or last element should now be bigValue (big pivot) (same as above).
         bigIndex = rtpt; // bigger piv index is now at last
         smallIndex = lfpt; // smaller piv is now at first (lfpt)
         ls = smallIndex; // update last small and first big to their respective indices
         fb = bigIndex;
         fu = ls + 1; // first unknown region set to lastsmall+1
         while (fu < fb) { // similar algorithm to single pivot but pseudocode added from lecture 10.
            if ((a[fu] <= bigValue) && (smallValue < a[fu]))
               // fu>= P1, < P2 This is a middle element so it is already in the right
               // partition. Simply increment fu
               fu++;
            else if (a[fu] > bigValue) {
               // fu> P2 Decrement fb (first big), swap a[fb] and a[fu]. Do not change fu.
               fb--;
               swap(a, fu, fb);
            } else {// a duplicate pivot!
               if ((a[fu] == smallValue) && fu % 2 == 0) // similar fu% something trick.
                  fu++;
               // fu <= P1 Just like in the left-to-right case, increment ls (last small),
               // swap a[ls] and a[fu], increment fu.
               else {
                  ls++;
                  swap(a, fu, ls);
                  fu++;
               }
            }
         }
         swap(a, smallIndex, ls); // smallIndex and last small swap
         swap(a, bigIndex, fb); // bigIndex first big swap
      }
      pr = new pair(ls, fb); // returns a PAIR (2 pivot, 3 partitions). (ls is last small = last element of
      return pr; // smaller left partition. fb is first big or first element of right partition.)
   }

   public static String myName() {
      return ("Kevin Siraki");
   }
}

// use this class to return the two pivot locations in the 2-pivot partition
// algorithm
class pair {
   public int left, right;

   public pair(int left, int right) {
      this.left = left;
      this.right = right;
   }

   // some getters below
   public int getLeft() {
      return left;
   }

   public int getRight() {
      return right;
   }
}
/*
 * // one of my several personal tests // (Will comment out prior to Canvas
 * submission) class test { public static void printArr(int arr[]) { for (int i
 * = 0; i < arr.length; i++) System.out.print(arr[i] + " "); }
 * 
 * public static void main(String args[]) { ArraySorts as = new ArraySorts();
 * int arr[] = { 4,90,29,67,49,98,21,5,28,80,55,5,58,36,42,26,6,44,90,57 };
 * System.out.println("Unsorted: "); printArr(arr); as.AlmostQS1(arr,
 * arr.length,2); System.out.println("\nSorted: "); printArr(arr); } }
 */
/*
 * A great Test1 Run Testing Kevin Siraki's program.
 * 
 * Testing Kevin Siraki's sorts on a huge array. Java's QuickSort on random data
 * runs in 4.101 seconds. Java's QuickSort on the test sets runs in 1.051
 * seconds.
 * 
 * 
 * Kevin Siraki's QS: outside-in partition, random pivot, cutoff=50 on all
 * random values runs in 3.351 seconds. Java Ratio = 0.81 on the test sets runs
 * in 1.288 seconds. Java Ratio = 1.22
 * 
 * Kevin Siraki's QS: left-to-right, random pivot partition, cutoff=50 on all
 * random values runs in 4.283 seconds. Java Ratio = 1.04 on the test sets runs
 * in 2.365 seconds. Java Ratio = 2.25
 * 
 * Kevin Siraki's QS: 2 random pivots partition, cutoff=50 on all random values
 * runs in 4.419 seconds. Java Ratio = 1.07 on the test sets runs in 2.384
 * seconds. Java Ratio = 2.26
 * 
 * Kevin Siraki's QS: outside-in partition, lf pivot, cutoff=50 on all random
 * values runs in 3.402 seconds. Java Ratio = 0.82
 * 
 * Kevin Siraki's QS: left-to-right, lf pivot, cutoff=50 on all random values
 * runs in 3.931 seconds. Java Ratio = 0.95
 * 
 * Kevin Siraki's QS: outside-in partition, random pivot, cutoff=2 on all random
 * values runs in 4.575 seconds. Java Ratio = 1.11 on the test sets runs in
 * 2.267 seconds. Java Ratio = 2.15
 * 
 * Kevin Siraki's QS: left-to-right, random pivot partition, cutoff=2 on all
 * random values runs in 5.187 seconds. Java Ratio = 1.26 on the test sets runs
 * in 3.193 seconds. Java Ratio = 3.03
 * 
 * Kevin Siraki's QS: 2 random pivots partition, cutoff=2 on all random values
 * runs in 5.938 seconds. Java Ratio = 1.44 on the test sets runs in 3.761
 * seconds. Java Ratio = 3.57
 * 
 * Kevin Siraki's QS: outside-in partition, lf pivot, cutoff=2 on all random
 * values runs in 4.175 seconds. Java Ratio = 1.01
 * 
 * Kevin Siraki's QS: left-to-right, lf pivot, cutoff=2 on all random values
 * runs in 4.602 seconds. Java Ratio = 1.12
 * 
 * Kevin Siraki's Almost QS: outside-in partition, random pivot, cutoff=2 on all
 * random values runs in 4.483 seconds. Java Ratio = 1.09 on the test sets runs
 * in 2.215 seconds. Java Ratio = 2.10
 * 
 * Kevin Siraki's Almost QS: left-to-right, random pivot partition, cutoff=2 on
 * all random values runs in 5.107 seconds. Java Ratio = 1.24 on the test sets
 * runs in 3.140 seconds. Java Ratio = 2.98
 * 
 * Kevin Siraki's Almost QS: 2 random pivots partition, cutoff=2 on all random
 * values runs in 5.758 seconds. Java Ratio = 1.40 on the test sets runs in
 * 3.604 seconds. Java Ratio = 3.42
 * 
 * Kevin Siraki's HeapSort: top-down build heap on all random values runs in
 * 7.721 seconds. Java Ratio = 1.88 on the test sets runs in 3.592 seconds. Java
 * Ratio = 3.41
 * 
 * Kevin Siraki's HeapSort: bottom-up build heap on all random values runs in
 * 7.686 seconds. Java Ratio = 1.87 on the test sets runs in 3.308 seconds. Java
 * Ratio = 3.14
 * 
 * Done testing Kevin Siraki methods.
 */