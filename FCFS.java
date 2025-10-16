import java.util.*;  // Import Scanner class for input

public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);  // Create Scanner object to take user input

        // Step 1: Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt(); // Number of processes

        // Step 2: Define arrays
        int[] at = new int[n];  // Arrival Time of each process
        int[] bt = new int[n];  // Burst Time (execution time) of each process
        int[] ct = new int[n];  // Completion Time of each process
        int[] tat = new int[n]; // Turnaround Time = CT - AT
        int[] wt = new int[n];  // Waiting Time = TAT - BT

        // Step 3: Input Arrival Time and Burst Time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time of P" + (i+1) + ": ");
            at[i] = sc.nextInt();  // Arrival time of process i
            System.out.print("Enter Burst Time of P" + (i+1) + ": ");
            bt[i] = sc.nextInt();  // Burst time of process i
        }

        // Step 4: Sort processes by Arrival Time (FCFS executes in arrival order)
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) { // If process i arrives later than process j
                    // Swap Arrival Time
                    int temp = at[i]; 
                    at[i] = at[j]; 
                    at[j] = temp;
                    
                    // Swap corresponding Burst Time to maintain correct process pairing
                    temp = bt[i]; 
                    bt[i] = bt[j]; 
                    bt[j] = temp;
                }
            }
        }

        // Step 5: Calculate Completion Time (CT)
        ct[0] = at[0] + bt[0]; // First process finishes at arrival + burst
        for (int i = 1; i < n; i++) {
            // Start at max of arrival time or previous process completion time
            ct[i] = Math.max(at[i], ct[i-1]) + bt[i];
        }

        // Step 6: Calculate Turnaround Time (TAT) and Waiting Time (WT)
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i]; // TAT = CT - AT
            wt[i] = tat[i] - bt[i]; // WT = TAT - BT
        }

        // Step 7: Display the results
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT"); // Header
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i+1) + "\t" + at[i] + "\t" + bt[i] + "\t" + ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }

        sc.close(); // Close the Scanner
    }
}
