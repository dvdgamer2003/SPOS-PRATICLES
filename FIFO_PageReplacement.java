import java.util.*;

public class FIFO_PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int framesCount = sc.nextInt(); // Number of memory frames
        System.out.print("Enter number of pages: ");
        int pagesCount = sc.nextInt();  // Number of pages in reference string

        int[] pages = new int[pagesCount]; // Array to store reference string
        System.out.println("Enter the reference string:");
        for (int i = 0; i < pagesCount; i++)
            pages[i] = sc.nextInt(); // Input each page number

        Queue<Integer> frames = new LinkedList<>(); // FIFO queue for frames
        int pageFaults = 0, pageHits = 0; // Counters for faults and hits

        System.out.println("\nPage Replacement Process (FIFO):");
        for (int i = 0; i < pagesCount; i++) {
            int currentPage = pages[i]; // Current page being referenced

            if (frames.contains(currentPage)) {
                // Page Hit: Page already in memory
                pageHits++;
                System.out.println("Page " + currentPage + " => HIT | Frames: " + frames);
            } else {
                // Page Fault: Page not found in memory
                pageFaults++;
                if (frames.size() == framesCount)
                    frames.poll(); // Remove the oldest page (FIFO rule)
                frames.add(currentPage); // Add new page into frame
                System.out.println("Page " + currentPage + " => FAULT | Frames: " + frames);
            }
        }

        // Final output
        System.out.println("\nTotal Page Hits = " + pageHits);
        System.out.println("Total Page Faults = " + pageFaults);
        sc.close(); // Close scanner
    }
}
