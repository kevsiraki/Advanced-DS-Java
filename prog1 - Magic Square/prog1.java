//Kevin Siraki
//Comp 282 3:30-4:45 PM Mon/Wed
//August 26, 2020
//The Magic Square class itself.

import java.util.Random;

class MagicSquare {
    private int size;
    private int[][] square;
    // constructor -- set the size and instantiate the array
    public MagicSquare(int size) {
        this.size = size;
        square = new int[size][size];
    }

    //find function
    //simple linear search algorithm that returns an index at which diff parameter is found within the numParam array parameter.  
    //The lastNum is for the first 2 algos but can be 0 for the third algo. 
    private int find(int diff, int[] numParam, int lastNumParam) {
        for (int i = 0; i < numParam.length - lastNumParam; i++) {
            if (numParam[i] == diff) return i;
        }
        return -1;
    }

    //this static function is solely for the inefficient pairs.  it is essentially just the Fisher–Yates shuffle Algorithm.
    static void randomize(int arr[], int n) {
        Random r = new Random();
        for (int i = n - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // purely random -- give up after "tries" tries
    public int purelyRandom(int tries) {
        int sizeNum = (size * size) - 1;
        Random rand = new Random();
        int num[] = new int[sizeNum + 1];
        for (int i = 0; i <= sizeNum; i++)
            num[i] = i + 1;
        int last = 0;

        //This is how I set up the main loop. I’m showing you as a hint.
        //Yours can be different. But why mess with a good thing? :)
        boolean found = false;
        int tryCt = 0;

        while (!found && tryCt < tries) {
            tryCt++;
            last = 0;
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int randomNum = (int)((Math.random() * (sizeNum - last - 1)) + 0);

                    square[row][col] = num[randomNum];

                    int temp = num[randomNum];
                    num[randomNum] = num[sizeNum - last];
                    num[sizeNum - last] = temp;
                    last++;
                }
            }

            found = magic();
        }
        //This is how my method ends
        if (found)
            return tryCt;
        else
            return -1;

    }

    // force last number in each row
    public int endOfRow(int tries) {
        boolean found = false;
        int tryCt = 0;
        boolean ok = true;
        int magicSum = (size * ((size * size) + 1)) / 2;
        int rowSum = 0;
        int pick;
        int sizeNum = (size * size);
        Random rand = new Random();
        int num[] = new int[sizeNum];
        for (int i = 0; i < num.length; i++)
            num[i] = i + 1;
        int last = 0;
        int colSum = 0;
        int temp;
        while (!found && tryCt < tries) {
            tryCt++;
            last = 0;
            ok = true;

            //This is how I filled the magic square array.
            //Many statements are missing, but this should
            //give you some ideas.
            //Notice the ok variable. If at any time a row cannot be
            //finished there’s no point in continuing to later rows.
            // fills rows one at a time

            for (int row = 0; row < size && ok; row++) {
                rowSum = 0;
                // put random values in all but the last spot
                for (int col = 0; col < size - 1; col++) {
                    int randomNum = (int)((Math.random() * (sizeNum - last - 1)) + 0);
                    square[row][col] = num[randomNum];
                    temp = num[randomNum];
                    num[randomNum] = num[num.length - 1 - last];
                    num[num.length - 1 - last] = temp;
                    last++;
                    rowSum += square[row][col];
                }
                //a row. Again, you can change these lines if you like.
                // call local method to find needed last element
                // num is the array described above.
                // pick points to that element in the num array
                pick = find(magicSum - rowSum, num, last + 1);
                if (pick == -1)
                    ok = false;
                else {
                    square[row][size - 1] = num[pick];
                    temp = num[pick];
                    num[pick] = num[num.length - 1 - last];
                    num[num.length - 1 - last] = temp;
                    last++;
                }
                if (last >= num.length - 1) square[row][size - 1] = num[0];
            }

            found = magic();
        }

        if (found)
            return tryCt;
        else
            return -1;
    }
    //NOTE TO PROFESSOR! THIS IS THE HIGHLY EFFICIENT INNER/OUTER PAIR HEURISTIC ALGORITHM.  THE ONE UNDERNEATH IS THE LESS EFFICIENT BUT LESS COMPLICATED VERSION OF THIS!!!! 
    //No hints for this one (yet), but it is hard. Concentrate on the first two
    //algorithms and work on this as time allows.
    // put pairs of numbers in rows
    public int pairs(int tries) {
        if (size % 2 != 0) //only works on even magic square size
            return -1;
        int numToDivideBy = size / 2; //the size of the magic square divided by 2
        int pair;
        int magicSum = (size * ((size * size) + 1)) / 2; //magic sum calculation
        int magicSumHalf = magicSum / numToDivideBy; //magic sum divided by numToDivideBy
        int sizeNum = (size * size) - 1;

        Random rand = new Random();
        int num[] = new int[sizeNum + 1];
        for (int i = 0; i <= sizeNum; i++)
            num[i] = i + 1;
        int last = 0; //a counter
        int temp; //for swap as usual
        int randomNumPairs; //this is for randomizing the order of the each pair of numbers
        int randomNum; //this is for randomizing the order of the actual numbers in the individual pairs
        int numAway = 0; //how many elements away from the midpoint are we?

        //here i make sure every 2 numbers in the array is a pair adding to the magicSum/2.
        for (int i = 0; i < num.length; i++) {

            pair = find(magicSumHalf - num[i], num, 0);
            if (i < sizeNum) {
                temp = num[pair];
                num[pair] = num[i + 1];
                num[i + 1] = temp;
            }
            i++;
        }

        //This is how I set up the main loop. I’m showing you as a hint.
        //Yours can be different. But why mess with a good thing? :)
        boolean found = false;
        int tryCt = 0;

        while (!found && tryCt < tries) {
            tryCt++;
            last = 0;
            //here i randomize the pairs 
            for (int i = 0; i <= sizeNum; i++) {
                randomNumPairs = (int)((Math.random() * (sizeNum - 1)) + 0);
                while (randomNumPairs % 2 != 0)
                    randomNumPairs = (int)((Math.random() * (sizeNum - 1)) + 0);
                if (i % 2 == 0) {
                    temp = num[i];
                    num[i] = num[randomNumPairs];
                    num[randomNumPairs] = temp;
                }
                if (i < sizeNum && (i + 1) % 2 != 0) {
                    temp = num[i + 1];
                    num[i + 1] = num[randomNumPairs + 1];
                    num[randomNumPairs + 1] = temp;
                }
            }
            //here I randomize the order of each of the pairs for a much more efficient solution
            for (int i = 0; i <= sizeNum; i += 2) {
                randomNum = (int)(Math.random() * 2);
                if (randomNum == 1) {
                    temp = num[i];
                    num[i] = num[i + 1];
                    num[i + 1] = temp;
                }
            }

            for (int row = 0; row < size; row++) {
                numAway = 0;
                for (int col = 0; col < size; col++) {
                    if (col == 0)
                        square[row][col] = num[last];
                    if (col > 0 && col < size / 2) {
                        square[row][col] = num[last + col];
                    }
                    if (col < size && col >= size / 2) {
                        if (last + col - 1 < sizeNum)
                            square[row][col] = num[last + (col - 1)];
                        else square[row][col] = num[sizeNum];
                        numAway++;

                    }
                    if (col <= size && col >= size / 2 + 1) {

                        square[row][col] = num[last + col - 2 * numAway - 1];
                        numAway++;
                    }
                    last++;
                }
            }
            found = magic();
        }
        //This is how my method ends
        if (found)
            return tryCt;
        else
            return -1;

    }

    //THIS IS WHERE THE LESS EFFICIENT AND LESS COMPLICATED PAIRS ALGORITHM BEGINS.  TO GET IT TO WORK WITH A 6x6 YOU NEED TO USE LONG INTS.
    public int pairsInefficient(int tries) {
        if (size % 2 != 0) //only works on even magic square size
            return -1;
        int numToDivideBy = size / 2; //the size of the magic square divided by 2
        int pair;
        int magicSum = (size * ((size * size) + 1)) / 2; //magic sum calculation
        int magicSumHalf = magicSum / numToDivideBy; //magic sum divided by numToDivideBy
        int sizeNum = (size * size) - 1;

        Random rand = new Random();
        int num[] = new int[sizeNum + 1];
        for (int i = 0; i <= sizeNum; i++)
            num[i] = i + 1;
        int last = 0; //a counter
        int temp; //for swap as usual
        int randomNumPairs; //this is for randomizing the order of the each pair of numbers
        int randomNum; //this is for randomizing the order of the actual numbers in the individual pairs
        int numAway = 0; //how many elements away from the midpoint are we?
        int pairsPerRow[] = new int[size];
        for (int i = 0; i < size; i++)
            pairsPerRow[i] = i;
        randomize(pairsPerRow, size);
        //here i make sure every 2 numbers in the array is a pair adding to the magicSum/2.
        for (int i = 0; i < num.length; i++) {

            pair = find(magicSumHalf - num[i], num, 0);
            if (i < sizeNum) {
                temp = num[pair];
                num[pair] = num[i + 1];
                num[i + 1] = temp;
            }
            i++;
        }
        //This is how I set up the main loop. I’m showing you as a hint.
        //Yours can be different. But why mess with a good thing? :)
        boolean found = false;
        int tryCt = 0;

        while (!found && tryCt < tries) {
            tryCt++;
            last = 0;
            //here i randomize the pairs 
            for (int i = 0; i <= sizeNum; i++) {
                randomNumPairs = (int)((Math.random() * (sizeNum - 1)) + 0);
                while (randomNumPairs % 2 != 0)
                    randomNumPairs = (int)((Math.random() * (sizeNum - 1)) + 0);
                if (i % 2 == 0) {
                    temp = num[i];
                    num[i] = num[randomNumPairs];
                    num[randomNumPairs] = temp;
                }
                if (i < sizeNum && (i + 1) % 2 != 0) {
                    temp = num[i + 1];
                    num[i + 1] = num[randomNumPairs + 1];
                    num[randomNumPairs + 1] = temp;
                }
            }
            //here I randomize the order of each of the pairs for a much more efficient solution
            for (int i = 0; i <= sizeNum; i += 2) {
                randomNum = (int)(Math.random() * 2);
                if (randomNum == 1) {
                    temp = num[i];
                    num[i] = num[i + 1];
                    num[i + 1] = temp;
                }
            }
            randomNum = (int)(Math.random() * size);
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    square[row][pairsPerRow[col]] = num[last];
                    last++;
                }
                randomize(pairsPerRow, size); //RANDOMIZE THE ORDER PAIRS ARE PLACED PER ROW.. THIS USUALLY ENDS UP IN A SIMILAR INNER/OUTER STRUCTURE
            }
            found = magic();
        }
        //This is how my method ends
        if (found)
            return tryCt;
        else
            return -1;
    }

    // determine if a magic square has been created, i.e., check all rows, columns,
    // and diagonals sum to the same value
    public boolean magic() {
        // calculate the sum of 
        // the prime diagonal 
        int sum = 0, sum2 = 0;
        for (int i = 0; i < size; i++)
            sum = sum + square[i][i];

        // the secondary diagonal 
        for (int i = 0; i < size; i++)
            sum2 = sum2 + square[i][size - 1 - i];

        if (sum != sum2)
            return false;

        // For sums of Rows 
        for (int i = 0; i < size; i++) {

            int rowSum = 0;
            for (int j = 0; j < size; j++)
                rowSum += square[i][j];

            // check if every row sum is 
            // equal to prime diagonal sum 
            if (rowSum != sum)
                return false;
        }

        // For sums of Columns 
        for (int i = 0; i < size; i++) {

            int colSum = 0;
            for (int j = 0; j < size; j++)
                colSum += square[j][i];

            // check if every column sum is 
            // equal to prime diagonal sum 
            if (sum != colSum)
                return false;
        }

        return true;
    }
    // output the magic square (or whatever is in the array if it is not)
    public void out() {
        int row, col;
        for (row = 0; row < size; row++) {
            for (col = 0; col < size; col++) {
                System.out.print(String.format("%3d", square[row][col]));
            }
            System.out.println();
        }
    }
    // change to false if this algorithm was not implemented
    public boolean rowLastImplemented() {
        return true;
    }
    // change to false if this algorithm was not implemented
    public boolean pairsImplemented() {
        return true;
    }
    // put your name here
    public static String myName() {
        return "Kevin Siraki";
    }
}