import java.util.Scanner;

class MemoryBlock {
    int blockNumber;
    int size;
    int startAddress;
    int endAddress;
    String status;
    String processID;
    int internalFragmentation;

    public MemoryBlock(int blockNumber, int size, int startAddress) {
        this.blockNumber = blockNumber;
        this.size = size;
        this.startAddress = startAddress;
        this.endAddress = startAddress + size - 1;
        this.status = "free";
        this.processID = "Null";
        this.internalFragmentation = 0;
    }

    public void allocate(String processID, int processSize) {
        this.processID = processID;
        this.status = "allocated";
        this.internalFragmentation = this.size - processSize;
    }

    public void deallocate() {
        this.status = "free";
        this.processID = "Null";
        this.internalFragmentation = 0;
    }

    @Override
    public String toString() {
        return String.format("%-7d %-8d %-12s %-12s %-12s %d",
                blockNumber, size, startAddress + "-" + endAddress, 
                status, processID, internalFragmentation);
    }
}

public class MemoryFragmentationSimulation {
    private MemoryBlock[] memoryBlocks;
    private int allocationStrategy; // 1: first-fit, 2: best-fit, 3: worst-fit

    public void initializeMemory() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the total number of blocks: ");
        int numBlocks = scanner.nextInt();
        
        System.out.print("Enter the size of each block in KB: ");
        scanner.nextLine(); // consume newline
        String[] sizes = scanner.nextLine().split(" ");
        
        memoryBlocks = new MemoryBlock[numBlocks];
        int currentAddress = 0;
        
        for (int i = 0; i < numBlocks; i++) {
            int size = Integer.parseInt(sizes[i]);
            memoryBlocks[i] = new MemoryBlock(i, size, currentAddress);
            currentAddress += size;
        }
        
        System.out.print("Enter allocation strategy (1 for first-fit, 2 for best-fit, 3 for worst-fit): ");
        allocationStrategy = scanner.nextInt();
        
        System.out.println("Memory blocks are created...");
        printMemoryStatus();
    }

    public void allocateMemory(String processID, int processSize) {
        
        for (MemoryBlock block : memoryBlocks) {
    if (block.processID.equals(processID)) {
        System.out.println("Error: Process ID " + processID + " is already in use.");
        return;
    }
}

        MemoryBlock targetBlock = null;
        
        
        switch (allocationStrategy) {
            case 1: // First-Fit
                targetBlock = firstFit(processSize);
                break;
            case 2: // Best-Fit
                targetBlock = bestFit(processSize);
                break;
            case 3: // Worst-Fit
                targetBlock = worstFit(processSize);
                break;
        }
        
        if (targetBlock != null) {
            targetBlock.allocate(processID, processSize);
            System.out.printf("%s Allocated at address %d, and the internal fragmentation is %d\n",
                    processID, targetBlock.startAddress, targetBlock.internalFragmentation);
        } else {
            System.out.println("Error: No suitable block found for allocation");
        }
    }

    private MemoryBlock firstFit(int processSize) {
        for (MemoryBlock block : memoryBlocks) {
            if (block.status.equals("free") && block.size >= processSize) {
                return block;
            }
        }
        return null;
    }

    private MemoryBlock bestFit(int processSize) {
        MemoryBlock bestBlock = null;
        for (MemoryBlock block : memoryBlocks) {
            if (block.status.equals("free") && block.size >= processSize) {
                if (bestBlock == null || block.size < bestBlock.size) {
                    bestBlock = block;
                }
            }
        }
        return bestBlock;
    }

    private MemoryBlock worstFit(int processSize) {
        MemoryBlock worstBlock = null;
        for (MemoryBlock block : memoryBlocks) {
            if (block.status.equals("free") && block.size >= processSize) {
                if (worstBlock == null || block.size > worstBlock.size) {
                    worstBlock = block;
                }
            }
        }
        return worstBlock;
    }

    public void deallocateMemory(String processID) {
        boolean found = false;
        for (MemoryBlock block : memoryBlocks) {
            if (block.processID.equals(processID)) {
                block.deallocate();
                found = true;
                System.out.println("Process " + processID + " deallocated successfully");
            }
        }
        if (!found) {
            System.out.println("Error: Process " + processID + " not found in memory");
        }
    }

    public void printMemoryStatus() {
        System.out.println("Memory blocks:");
        System.out.println("=============================");
        System.out.printf("%-7s %-8s %-12s %-12s %-12s %s\n", 
                "Block#", "size", "start-end", "status", "ProcessID", "InternalFragmentation");
        System.out.println("=============================");
        for (MemoryBlock block : memoryBlocks) {
            System.out.println(block);
        }
        System.out.println("=============================");
    }

    public void runSimulation() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        
        do {
            System.out.println("1) Allocates memory blocks");
            System.out.println("2) De-allocates memory blocks");
            System.out.println("3) Print report about the current state of memory and internal Fragmentation");
            System.out.println("4) Exit");
            System.out.println("=============================");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter the process ID and size of process: ");
                    scanner.nextLine(); // consume newline
                    String[] input = scanner.nextLine().split(" ");
                    String processID = input[0];
                    int size = Integer.parseInt(input[1]);
                    allocateMemory(processID, size);
                    break;
                case 2:
                    System.out.print("Enter the process ID to be released: ");
                    scanner.nextLine(); // consume newline
                    String pid = scanner.nextLine();
                    deallocateMemory(pid);
                    break;
                case 3:
                    printMemoryStatus();
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("=============================");
        } while (choice != 4);
        
        scanner.close();
    }

    public static void main(String[] args) {
        MemoryFragmentationSimulation simulation = new MemoryFragmentationSimulation();
        simulation.initializeMemory();
        simulation.runSimulation();
    }
}