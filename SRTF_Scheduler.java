/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package srtf_scheduler;

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

        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for P" + (i + 1) + ": ");
            int arrival = scanner.nextInt();
            int burst = scanner.nextInt();
            processes.add(new Process(i + 1, arrival, burst));
        }

        scheduleProcesses(processes);
        scanner.close();
    }

    public static void scheduleProcesses(List<Process> processes) {
        int n = processes.size(), currentTime = 0, completed = 0, totalIdleTime = 0;
        boolean[] isCompleted = new boolean[n];
        List<String> ganttChart = new ArrayList<>();
        PriorityQueue<Process> pq = new PriorityQueue<>((a, b) -> a.remainingTime != b.remainingTime ? a.remainingTime - b.remainingTime : a.arrivalTime - b.arrivalTime);
        Process lastProcess = null;
        int startTime = 0;

        while (completed < n) {
            for (Process p : processes) {
                if (!isCompleted[p.id - 1] && p.arrivalTime <= currentTime && !pq.contains(p)) {
                    pq.add(p);
                }
            }

            if (pq.isEmpty()) {
                if (lastProcess != null) {
                    ganttChart.add(startTime + "-" + currentTime + " P" + lastProcess.id);
                }
                ganttChart.add(currentTime + "-" + (currentTime + 1) + " IDLE");
                totalIdleTime++;
                currentTime++;
                lastProcess = null;
                startTime = currentTime;
                continue;
            }

            Process currentProcess = pq.poll();
            if (lastProcess != null && lastProcess.id != currentProcess.id) {
                ganttChart.add(startTime + "-" + currentTime + " P" + lastProcess.id);
                ganttChart.add(currentTime + "-" + (currentTime + 1) + " CS");
                totalIdleTime++;
                currentTime++;
                startTime = currentTime;
            }

            currentProcess.remainingTime--;
            currentTime++;

            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                isCompleted[currentProcess.id - 1] = true;
                completed++;
            } else {
                pq.add(currentProcess);
            }

            lastProcess = currentProcess;
        }

        if (lastProcess != null) {
            ganttChart.add(startTime + "-" + currentTime + " P" + lastProcess.id);
        }

        printResults(processes, ganttChart, totalIdleTime, currentTime);
    }

    public static void printResults(List<Process> processes, List<String> ganttChart, int totalIdleTime, int totalTime) {
        double totalTAT = 0, totalWT = 0;

        System.out.println("\nExecution Timeline (Gantt Chart):");
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
        
// Printing additional Gantt Chart without time
        System.out.println("\nFinal Gantt Chart");
        System.out.print("|");
        for (String entry : ganttChart) {
            String formattedEntry = entry.replaceAll("\\d+-\\d+ ", ""); // Remove time ranges
            System.out.print(" " + formattedEntry + " | ");
        }
        System.out.println();
    
    }
    
}
