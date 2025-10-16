import java.util.*;

public class Optimal_PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt(); // Number of frames in memory
        
        System.out.print("Enter number of pages: ");
        int n = sc.nextInt(); // Number of pages in reference string

        int[] pages = new int[n]; // Reference string array
        System.out.println("Enter the reference string:");
        for (int i = 0; i < n; i++)
            pages[i] = sc.nextInt(); // Input page numbers

        List<Integer> memory = new ArrayList<>(); // List representing memory frames
        int pageFaults = 0; // Counter for page faults

        // Traverse all page references one by one
        for (int i = 0; i < n; i++) {
            int currentPage = pages[i]; // Current page being referenced

            if (memory.contains(currentPage)) {
                // Page is already in memory (page hit)
                System.out.println("Step " + (i + 1) + " -> " + memory);
                continue;
            }

            // If there is still space in memory, just add page
            if (memory.size() < frames) {
                memory.add(currentPage);
            } else {
                // Need to replace one page (find the one that will not be used for longest time)
                int farthest = -1, indexToReplace = -1;

                // For each page in memory, find when it will next be used
                for (int j = 0; j < memory.size(); j++) {
                    int page = memory.get(j);
                    int nextUse = Integer.MAX_VALUE; // Default = not used again

                    // Look ahead to find next use of this page
                    for (int k = i + 1; k < n; k++) {
                        if (pages[k] == page) {
                            nextUse = k; // Found next use index
                            break;
                        }
                    }

                    // Select the page with the farthest next use (or never used again)
                    if (nextUse > farthest) {
                        farthest = nextUse;
                        indexToReplace = j; // Record which page to replace
                    }
                }

                // Replace that page with the new page
                memory.set(indexToReplace, currentPage);
            }

            // Increase page fault count
            pageFaults++;
            // Display current memory status
            System.out.println("Step " + (i + 1) + " -> " + memory);
        }

        // Final output of total page faults
        System.out.println("Total Page Faults: " + pageFaults);
        sc.close(); // Close scanner
    }
}
