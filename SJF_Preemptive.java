import java.util.*;  // Import Scanner class for input

public class SJF_Preemptive {

    // Step 1: Define a Process class to store process information
    static class Process {
        int id;  // Process ID
        int at;  // Arrival Time
        int bt;  // Burst Time
        int rt;  // Remaining Time (used for preemption)
        int ct;  // Completion Time
        int tat; // Turnaround Time
        int wt;  // Waiting Time

        // Constructor to initialize a process
        Process(int id, int at, int bt) {
            this.id = id; 
            this.at = at; 
            this.bt = bt; 
            this.rt = bt; // Initially, remaining time = burst time
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);  // Scanner for user input

        // Step 2: Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Step 3: Create array of processes
        Process[] p = new Process[n];

        // Step 4: Input Arrival Time and Burst Time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time of P" + (i+1) + ": ");
            int at = sc.nextInt();  // Arrival time of process i
            System.out.print("Enter Burst Time of P" + (i+1) + ": ");
            int bt = sc.nextInt();  // Burst time of process i
            p[i] = new Process(i+1, at, bt); // Initialize process object
        }

        // Step 5: Initialize simulation variables
        int time = 0;       // Current time
        int completed = 0;  // Number of processes completed

        // Step 6: Loop until all processes are completed
        while (completed < n) {
            int idx = -1;                   // Index of process with shortest remaining time
            int minRt = Integer.MAX_VALUE;  // Minimum remaining time found so far

            // Step 6a: Find the process with shortest remaining time that has arrived
            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && p[i].rt > 0 && p[i].rt < minRt) {
                    minRt = p[i].rt;  // Update minimum remaining time
                    idx = i;          // Update index of chosen process
                }
            }

            // Step 6b: If no process is ready, increment time (CPU idle)
            if (idx == -1) {
                time++; 
                continue;
            }

            // Step 6c: Execute the chosen process for 1 unit of time
            p[idx].rt--;  // Decrement remaining time
            time++;        // Increment current time

            // Step 6d: If process finishes execution
            if (p[idx].rt == 0) {
                p[idx].ct = time;               // Completion Time = current time
                p[idx].tat = p[idx].ct - p[idx].at; // Turnaround Time = CT - AT
                p[idx].wt = p[idx].tat - p[idx].bt; // Waiting Time = TAT - BT
                completed++;                     // Increment completed count
            }
        }

        // Step 7: Display the results
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p) {
            System.out.println("P" + pr.id + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);
        }

        sc.close(); // Close the Scanner
    }
}
