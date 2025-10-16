import java.util.*;  // Import the Scanner class for user input

public class BankersAlgorithm {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);  // Create Scanner object to take input from user

        // Step 1: Input number of processes and resources
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();  // Number of processes
        System.out.print("Enter number of resources: ");
        int m = sc.nextInt();  // Number of resource types

        // Step 2: Define matrices and arrays
        int[][] allocation = new int[n][m]; // Allocation Matrix: resources currently allocated to processes
        int[][] max = new int[n][m];        // Max Matrix: maximum resources each process may need
        int[][] need = new int[n][m];       // Need Matrix: resources still needed (Max - Allocation)
        int[] available = new int[m];       // Available Array: number of available instances of each resource

        // Step 3: Input Allocation Matrix
        System.out.println("\nEnter Allocation Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                allocation[i][j] = sc.nextInt(); // Input resources allocated to process i for resource j
            }
        }

        // Step 4: Input Max Matrix
        System.out.println("\nEnter Max Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max[i][j] = sc.nextInt(); // Input maximum resources process i may need for resource j
            }
        }

        // Step 5: Input Available Resources
        System.out.println("\nEnter Available Resources:");
        for (int j = 0; j < m; j++) {
            available[j] = sc.nextInt(); // Input currently available instances of resource j
        }

        // Step 6: Calculate Need Matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = max[i][j] - allocation[i][j]; // Need = Max - Allocation
            }
        }

        // Step 7: Apply Banker’s Safety Algorithm
        boolean[] finished = new boolean[n]; // Track which processes have finished
        int[] safeSequence = new int[n];     // Store the safe sequence of process execution
        int count = 0;                        // Counter for processes included in safe sequence

        while (count < n) { // Loop until all processes are checked
            boolean found = false; // Flag to check if a safe process is found in this iteration
            for (int i = 0; i < n; i++) {
                if (!finished[i]) { // If process i is not finished
                    int j;
                    for (j = 0; j < m; j++) { // Check if all needed resources are available
                        if (need[i][j] > available[j])
                            break; // If any needed resource is not available, break
                    }

                    if (j == m) { // If all needed resources are available
                        for (int k = 0; k < m; k++)
                            available[k] += allocation[i][k]; // Release resources after process finishes
                        safeSequence[count++] = i; // Add process to safe sequence
                        finished[i] = true;        // Mark process as finished
                        found = true;              // Safe process found in this iteration
                    }
                }
            }
            if (!found) break; // If no process could be allocated safely, exit loop
        }

        // Step 8: Display result
        if (count == n) { // If all processes are finished, system is safe
            System.out.println("\nSystem is in a SAFE STATE.");
            System.out.print("Safe Sequence: ");
            for (int i = 0; i < n; i++)
                System.out.print("P" + safeSequence[i] + " "); // Print safe sequence
        } else {
            System.out.println("\nSystem is NOT in a safe state!"); // Unsafe state
        }

        sc.close(); // Close the Scanner
    }
}
