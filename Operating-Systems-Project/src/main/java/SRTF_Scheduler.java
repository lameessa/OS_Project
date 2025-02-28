/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
            ganttChart.add(startTime + "-" + currentTime + " P" + lastProcess.id);
        }

        // حساب وإظهار النتائج
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
        double cpuUtilization = (executionTime / (double) totalTime) * 100.0;

        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
        System.out.printf("Average Waiting Time: %.2f\n", avgWT);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}