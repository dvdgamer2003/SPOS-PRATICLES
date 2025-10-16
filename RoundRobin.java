//preemptive 
import java.util.*;  // Import utility package for Scanner, Queue, etc.

public class RoundRobin {
    
    // Define a Process class to hold process details
    static class Process {
        int id, at, bt, rt, ct, wt, tat;  // id=PID, at=Arrival Time, bt=Burst Time, rt=Remaining Time, ct=Completion Time, wt=Waiting Time, tat=Turnaround Time
        
        Process(int id, int at, int bt) {  // Constructor
            this.id = id;       // Assign process ID
            this.at = at;       // Assign arrival time
            this.bt = bt;       // Assign burst time
            this.rt = bt;       // Initially remaining time = burst time
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);  // Create Scanner object for user input
        
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();  // Number of processes
        
        System.out.print("Enter Time Quantum: ");
        int tq = sc.nextInt(); // Time quantum for round robin
        
        Process[] p = new Process[n];  // Array of Process objects
        
        // Take input for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time of P" + (i+1) + ": ");
            int at = sc.nextInt();  // Arrival time input
            System.out.print("Enter Burst Time of P" + (i+1) + ": ");
            int bt = sc.nextInt();  // Burst time input
            p[i] = new Process(i+1, at, bt);  // Create new Process and store it in array
        }

        Arrays.sort(p, Comparator.comparingInt(x -> x.at)); // Sort processes by arrival time (so earliest arrival comes first)
        
        Queue<Process> q = new LinkedList<>();  // Create ready queue using LinkedList
        
        int time = p[0].at, i = 0;  // Initialize current time to first arrival, i for indexing
        
        q.add(p[i++]);  // Add first process to the queue
        
        // Continue until queue becomes empty
        while (!q.isEmpty()) {
            Process cur = q.poll();  // Remove first process from the queue (process currently running)

            // If remaining time > time quantum → process executes for tq time then goes back to queue
            if (cur.rt > tq) {
                cur.rt -= tq;   // Subtract time quantum from remaining time
                time += tq;     // Increase current time by time quantum
            } 
            // If process can complete within this quantum
            else {
                time += cur.rt;           // Add remaining time to current time
                cur.rt = 0;               // No remaining time left (process finished)
                cur.ct = time;            // Set completion time
                cur.tat = cur.ct - cur.at; // Calculate turnaround time
                cur.wt = cur.tat - cur.bt; // Calculate waiting time
            }

            // Add all newly arrived processes (arrival time ≤ current time) to queue
            while (i < n && p[i].at <= time) 
                q.add(p[i++]);

            // If current process not yet finished, add it again to the queue
            if (cur.rt > 0) 
                q.add(cur);
        }

        // Display results
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p)
            System.out.println("P" + pr.id + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);

        sc.close();  // Close scanner
    }
}
