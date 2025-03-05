/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication11;
import java.util.*;

class Process {
    int id, arrivalTime, burstTime, remainingTime, completionTime, turnaroundTime, waitingTime;

    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class SRTF_Scheduler {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of processes = ");
        int n = scanner.nextInt();
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": Arrival time = ");
            int arrival = scanner.nextInt();
            System.out.print("P" + (i + 1) + ": Burst time = ");
            int burst = scanner.nextInt();
            processes.add(new Process(i + 1, arrival, burst));
        }
        System.out.println("Scheduling Algorithm: Shortest remaining time first");
        System.out.println("Context Switch: 1 ms");

        scheduleProcesses(processes);
    }

    public static void scheduleProcesses(List<Process> processes) {
        
        int n = processes.size();
        int currentTime = 0;
        int completed = 0;
        int totalIdleTime = 0;        
        List<String> ganttChart = new ArrayList<>();
        Queue<Process> queue = new LinkedList<>();
        Process lastProcess = null;
        int startTime = 0;

        while (completed < n) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && !queue.contains(p)) {
                    queue.add(p);
                }
            }

            if (queue.isEmpty()) {
                if (lastProcess != null) {
                    ganttChart.add(startTime + "-" + currentTime + "    P" + lastProcess.id);
                }
                ganttChart.add(currentTime + "-" + (currentTime + 1) + " IDLE");
                totalIdleTime++;
                currentTime++;
                lastProcess = null;
                startTime = currentTime;
                continue;
            }

            // Find the process with the shortest remaining time
            Process currentProcess = findShortestRemainingTime(queue);

            if (lastProcess != null && lastProcess.id != currentProcess.id) {
                ganttChart.add(startTime + "-" + currentTime + "    P" + lastProcess.id);
                ganttChart.add(currentTime + "-" + (currentTime + 1) + "    CS");
                totalIdleTime++;
                currentTime++;
                startTime = currentTime;
            }

            // Process execution (1 time unit)
            currentProcess.remainingTime--;
            currentTime++;

            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                completed++;
                queue.remove(currentProcess); // Remove from queue when done
            }

            lastProcess = currentProcess;
        }

        if (lastProcess != null) {
            ganttChart.add(startTime + "-" + currentTime + "    P" + lastProcess.id);
        }

        printResults(processes, ganttChart, totalIdleTime, currentTime);
    }

    // Method to Find the Process with the Shortest Remaining Time
    public static Process findShortestRemainingTime(Queue<Process> queue) {
        Process shortest = null;
        for (Process p : queue) {
            if (shortest == null || p.remainingTime < shortest.remainingTime || 
                (p.remainingTime == shortest.remainingTime && p.arrivalTime < shortest.arrivalTime)) {
                shortest = p;
            }
        }
        return shortest;
    }

    public static void printResults(List<Process> processes, List<String> ganttChart, int totalIdleTime, int totalTime) {
        double totalTAT = 0, totalWT = 0;

        System.out.println("Time   Process/CS");
        for (String entry : ganttChart) {
            System.out.println(entry);
        }

        System.out.println("\nPerformance Metrics:");
        for (Process p : processes) {
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            totalTAT += p.turnaroundTime;
            totalWT += p.waitingTime;
        }

        double avgTAT = totalTAT / processes.size();
        double avgWT = totalWT / processes.size();
        double executionTime = totalTime - totalIdleTime;
        double cpuUtilization = (executionTime / totalTime) * 100.0;

        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
        System.out.printf("Average Waiting Time: %.2f\n", avgWT);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
        System.out.println();
    
    }
}
