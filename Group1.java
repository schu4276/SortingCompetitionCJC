import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.List;
import java.math.BigInteger;
import java.util.*;

// The isprime number algorithm is based off the following algorithm:
// https://www.geeksforgeeks.org/program-to-find-the-next-prime-number/
public class Group1 {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
        // testing the comparator:
        // Data.test_Data(); // This MUST be commented out for your submission to the
        // competition!

        if (args.length < 2) {
            System.out.println("Please run with two command line arguments: input and output file names");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outFileName = args[1];

        // read as strings
        String[] data = readData(inputFileName);
        String[] toSort = data.clone();
        Data[] sorted = sort(toSort); // Warm up the VM
        toSort = data.clone();
        Thread.sleep(10); // to let other things finish before timing; adds stability of runs

        long start = System.currentTimeMillis(); // Begin the timing
        sorted = sort(toSort);
        long end = System.currentTimeMillis(); // End the timing
            
        System.out.println(end - start); // Report the results
        writeOutResult(sorted, outFileName);
    }

    // YOUR SORTING METHOD GOES HERE.
    // You may call other methods and use other classes.
    // You may ALSO modify the methods, innner classes, etc, of Data[]
    // But not in way that transfers information from the warmup sort to the timed
    // sort.
    // Note: you may change the return type of the method.
    // You would need to provide your own function that prints your sorted array to
    // a file in the exact same format that my program outputs
    private static Data[] sort(String[] toSort) {
        Data[] toSortData = new Data[toSort.length];
        for (int i = 0; i < toSort.length; ++i) {
            toSortData[i] = new Data(toSort[i]);
        }
        Arrays.sort(toSortData, new GematriaComparator());
        return toSortData;
    }

    
    // public static class InsertionSort { 
    //     GematriaComparator comparator = new GematriaComparator();
    //     /*Function to sort array using insertion sort*/
    //     public void sort(Data[] arr){ 
    //         int n = arr.length;
    //         System.out.println(n);
    //         for (int j =1; j < n; j++) {
    //         Data key = arr[j];
    //         int i = j -1;
    //             while((i > -1) && (comparator.compare(arr[i],key) > 0)) {
    //                 arr[i + 1] = arr[i];
    //                 i = i -1;
    //                 arr[i + 1] = key;
    //             }
    //         }
    //     }
      
    // }
    public static class Quicksort{
        public Quicksort(){}
        public static void randomizedQuickSort(Data[] a, int low, int high){
            if(low < high){
                int q = randomPartition(a, low, high);
                randomizedQuickSort(a,low, q-1);
                randomizedQuickSort(a,q+1,high);
            }
        }//end quicksort 
        
        public static int randomPartition(Data[] a, int low, int high){
            Random rand = new Random();
            int randIndex = (int)(Math.random() * (high - low + 1) + low);
            // Swap random partition with a[high]		
            a[high] = a[randIndex];
            return partition(a,low,high);
        }//end partition
        public static int partition(Data[] a, int low, int high){
            // testing prints
            GematriaComparator comparator = new GematriaComparator();
            Data pivot = a[high];
    
            int index = (low -1);
            Data temp = new Data("");
            for(int i = low; i< high; i++){
                // if curr element is smaller than pivot
                if(comparator.compare(a[i],pivot) < 0){

                    index++;
                    temp = a[index];
                    a[index] = a[i];
                    a[i] = temp;
                }
            }
            temp = a[index+1];
            a[index+1] = a[high];
            a[high] = temp;
            return(index+1);
        }//end partition




    }// end quicksort class
  

    public static class Heapsort {    
        // Constructor
        public Heapsort() {  }

        public void hsort(Data arr[]) {
            int n = arr.length;
            // Building Heap
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(arr, n, i);
            }
            for (int i=n-1; i>0; i--) { 
            // Move current root to end 
            Data temp = arr[0]; 
            arr[0] = arr[i]; 
            arr[i] = temp; 
            // call max heapify on the reduced heap 
            heapify(arr, i, 0); 
            } 

        }
        public void heapify(Data arr[], int n, int i) {
            GematriaComparator comparator = new GematriaComparator();
            int largest = i; // Initialize largest as root
            int l = 2 * i + 1; // left = 2*i + 1
            int r = 2 * i + 2; // right = 2*i + 2

            // If left child is larger than root
            if (l < n && comparator.compare(arr[l], arr[largest]) > 0){
                largest = l;
            }
            // If right child is larger than largest so far
            if (r < n && comparator.compare(arr[r], arr[largest]) > 0){
                largest = r;
            }
            // If largest is not root
            if (largest != i) {
                Data swap = arr[i];
                arr[i] = arr[largest];
                arr[largest] = swap;

                // Recursively heapify the affected sub-tree
                heapify(arr, n, largest);
            }
        }


    }// end heapsort class

    // private static void printArray(String[] Arr, int n) {
    //     for (int i = 0; i < n; i++) {
    //         System.out.println(Arr[i]);
    //     }
    // }

    private static String[] readData(String inFile) throws FileNotFoundException, IOException {
        List<String> input = Files.readAllLines(Paths.get(inFile));
        // the string array is passed just so that the correct type can be created
        return input.toArray(new String[0]);
    }

    private static void writeOutResult(Data[] sorted, String outputFilename) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(outputFilename);
        for (Data s : sorted) {
            out.println(s.word);
        }
        out.close();
    }

    private static class GematriaComparator implements Comparator<Data> {
        public int toVal(char ch) { // This function is an ancient evil that has no place in a unicode-based world :(
            return (int) ch - (int) 'a' + 1; // type-casting a ch to (int) turns it into an ascii value
        } // Warning: this will work with non-lower-case ascii characters too.

        public int gematrify(String str) {
            char[] ch = str.toCharArray();
            int gematria = 0;
            int multiplier = 1;
            for (int i = str.length() - 1; i >= 0; i--) { // Work from the right to the left
                gematria += toVal(ch[i]) * multiplier;
                multiplier = 2 * multiplier;
            }
            return gematria;
        }
        
        // new is prime method this algorithm is based off of algorithm from https://www.geeksforgeeks.org/program-to-find-the-next-prime-number/ 
        static boolean isPrime(long n) {  
        // Corner cases  
        if (n <= 1) return false;  
        if (n <= 3) return true;  
          
        // This is checked so that we can skip  
        // middle five numbers in below loop  
        if (n % 2 == 0 || n % 3 == 0) return false;  
          
        for (int i = 5; i * i <= n; i = i + 6)  
            if (n % i == 0 || n % (i + 2) == 0)  
            return false;  
          
        return true;  
        }  


        // boolean isPrime(long n) {
        //     for (int i = 2; i <= Math.sqrt(n); i++) {
        //         if (n % i == 0) {
        //             return false;
        //         }
        //     }
        //     return true;
        // }
        
        int nextPrime(int n) {
            int m = n;
            while (true) { // In a worst-case scenario we'll run out of values for m... if we don't throw
                           // an exception we'll wrap around to the negatives. We'll find a prime
                           // eventually
                m = m + 1;
                if (isPrime(m)) {
                    return (m);
                }
            }
        }

        @Override
        public int compare(Data s1, Data s2) {
            int g1 = gematrify(s1.word); //change long to int
            int g2 = gematrify(s2.word); //change long to int

            if (g1 == g2) {
                return s1.word.compareTo(s2.word);
            } // in case of tie, compare lexicographically
            int p = 2; //change long to int
            boolean done = false;
            boolean d1 = false;
            boolean d2 = false;
            while (!done) { // Since g1 != g2 as we strip divisors we either find a smaller one, or one of
                            // the two reaches 1 first
                if (g1 == 1) {
                    return (-1);
                }
                if (g2 == 1) {
                    return (1);
                }
                // We know that g1 AND g2 have unprocessed divisors
                d1 = (g1 % p == 0); // d1 is a BOOLEAN value
                d2 = (g2 % p == 0); // d2 is a BOOLEAN VALUE
                if ((!d1) && (!d2)) {
                    p = nextPrime(p);
                    continue;
                } // Neither are divisible, move to next prime
                if (d1 && d2) { // both are divisible. Remove the divisor and continue
                    g1 = g1 / p;
                    g2 = g2 / p;
                    continue;
                }
                if (d1) {
                    return -1;
                } else {
                    return 1;
                }
            }
            System.out.println("WARNING... you should't be here!");
            return (0); // This should NEVER happen
        }
    }

    private static class Data {
        public String word; // The original string-- useful to outputting at the end.

        public Data(String inWord) {
            word = new String(inWord); // Make a copy of the string
        }

        public static void print_test(String s1, String s2) {
            Data testItem1 = new Data(s1);
            Data testItem2 = new Data(s2);
            GematriaComparator comparator = new GematriaComparator();

            System.out.println("Defining: s1 = " + s1 + " and s2 = " + s2);
            System.out.println("\tg1 = " + comparator.gematrify(s1) + " and g2 = " + comparator.gematrify(s2));
            System.out.println("Compare: " + s1 + " to " + s2 + ": ");
            System.out.println("Result=" + comparator.compare(testItem1, testItem2));
            System.out.println("Compare: " + s2 + " to " + s1 + ": ");
            System.out.println("Result=" + comparator.compare(testItem2, testItem1));
            System.out.println("---");
        }

        public static void test_Data() {
            String s1, s2;

            print_test("cat", "cat"); // Two of the same thing
            print_test("beauteous", "dog"); // Two different
            print_test("cat", "doh");
            print_test("cat", "doh");
            print_test("unfair", "quite");
            print_test("head", "leaving"); // both with even gematria
            print_test("readable", "allow");
            print_test("cat", "doh");
            System.out.println("---");
        }

    }
}
