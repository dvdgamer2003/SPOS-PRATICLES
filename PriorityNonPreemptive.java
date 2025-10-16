import java.util.*;  // Import Scanner class for taking input

public class PriorityNonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);  // Create Scanner object for user input

        // Step 1: Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Step 2: Declare arrays
        int[] bt = new int[n];   // Burst Time (execution time) of each process
        int[] pr = new int[n];   // Priority value (lower number = higher priority)
        int[] wt = new int[n];   // Waiting Time for each process
        int[] tat = new int[n];  // Turnaround Time for each process
        int[] pid = new int[n];  // Process ID (P1, P2, etc.)

        // Step 3: Input Burst Time and Priority for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Burst Time for P" + (i+1) + ": ");
            bt[i] = sc.nextInt(); // Read burst time
            System.out.print("Enter Priority (lower = higher priority) for P" + (i+1) + ": ");
            pr[i] = sc.nextInt(); // Read priority
            pid[i] = i + 1;       // Assign process ID (1-based index)
        }

        // Step 4: Sort processes based on priority (ascending order)
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pr[i] > pr[j]) {  // If current process has lower priority (larger number)
                    // Swap priority
                    int t = pr[i]; 
                    pr[i] = pr[j]; 
                    pr[j] = t;

                    // Swap burst time (to match correct process)
                    t = bt[i]; 
                    bt[i] = bt[j]; 
                    bt[j] = t;

                    // Swap process IDs (to maintain process order)
                    t = pid[i]; 
                    pid[i] = pid[j]; 
                    pid[j] = t;
                }
            }
        }

        // Step 5: Calculate Waiting Time (WT) and Turnaround Time (TAT)
        wt[0] = 0;             // First process has no waiting time
        tat[0] = bt[0];        // Turnaround time = burst time for first process

        for (int i = 1; i < n; i++) {
            wt[i] = wt[i-1] + bt[i-1]; // Waiting time = sum of previous burst times
            tat[i] = wt[i] + bt[i];    // Turnaround time = waiting time + current burst time
        }

        // Step 6: Display results
        System.out.println("\nPID\tBT\tPRIORITY\tWT\tTAT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + bt[i] + "\t" + pr[i] + "\t\t" + wt[i] + "\t" + tat[i]);
        }

        sc.close();  // Close Scanner
    }
}
