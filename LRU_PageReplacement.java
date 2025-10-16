import java.util.*;

public class LRU_PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt(); // Number of memory frames
        
        System.out.print("Enter number of pages: ");
        int n = sc.nextInt(); // Number of pages in reference string

        int[] pages = new int[n]; // Reference string
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++)
            pages[i] = sc.nextInt(); // Input each page number

        ArrayList<Integer> memory = new ArrayList<>(); // Memory to store pages (used to track order)
        int pageFaults = 0; // Count number of faults

        for (int i = 0; i < n; i++) {
            int currentPage = pages[i]; // Current page being referenced

            if (memory.contains(currentPage)) {
                // Page hit → move the page to the end to mark it as most recently used
                memory.remove(Integer.valueOf(currentPage)); // Remove old position
                memory.add(currentPage); // Add at the end (most recently used)
            } else {
                // Page fault → page not in memory
                pageFaults++;
                if (memory.size() == frames)
                    memory.remove(0); // Remove least recently used (front of list)
                memory.add(currentPage); // Add new page at the end
            }

            // Print current memory status after each step
            System.out.println("Step " + (i + 1) + " -> " + memory);
        }

        // Print total faults
        System.out.println("Total Page Faults: " + pageFaults);
        sc.close(); // Close scanner
    }
}
