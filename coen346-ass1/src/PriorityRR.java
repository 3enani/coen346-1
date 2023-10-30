import java.util.*;

public class PriorityRR implements Algorithm {
    private List<Queue<Task>> priorityQueues;
    private int timeQuantum;

    public PriorityRR(List<Task> taskQueue, int timeQuantum) {
        this.timeQuantum = timeQuantum;

        int maxPriorityLevel = 0;
        for (Task task : taskQueue) {
            maxPriorityLevel = Math.max(maxPriorityLevel, task.getPriority());
        }
        this.priorityQueues = new ArrayList<>();
        for (int i = 0; i < maxPriorityLevel; i++) {
            priorityQueues.add(new LinkedList<>());
        }

        for (Task task : taskQueue) {
            int priority = task.getPriority();
            priorityQueues.get(priority - 1).offer(task); // Fixed index
        }
    }

    @Override
    public void schedule() {
        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalResponseTime = 0;

        System.out.println("Priority Round Robin scheduling with time quantum " + timeQuantum + ")");

        // Continue scheduling until all priority queues are empty
        while (!priorityQueues.isEmpty()) {
            Task currentTask = pickNextTask(); // Get the task with the highest priority

            if (currentTask == null) {
                // If there are no tasks remaining, advance the time to the next task arrival
                int nextArrivalTime = Integer.MAX_VALUE;
                for (Queue<Task> queue : priorityQueues) {
                    if (!queue.isEmpty()) {
                        nextArrivalTime = Math.min(nextArrivalTime, queue.peek().getArrival());
                    }
                }
                currentTime = nextArrivalTime;
                continue;
            }

            if (currentTask.getArrival() > currentTime) {
                currentTime = currentTask.getArrival();
            }
            System.out.println("Time " + currentTime + ": Running task: " + currentTask.getName());
            printTaskStates(currentTime, priorityQueues.get(currentTask.getPriority() - 1)); // Fixed parameter

            if (currentTask.getBurst() > timeQuantum) {
                currentTime += timeQuantum;
                currentTask.decreaseBurstTime(timeQuantum);
            } else {
                currentTime += currentTask.getBurst();
                currentTask.setCompletionTime(currentTime);
                System.out.println("Time " + currentTime + ": Completed task: " + currentTask.getName());

                int waitingTime = currentTask.getCompletionTime() - currentTask.getArrival() - currentTask.getBurst();
                currentTask.setWaitingTime(waitingTime);
                totalWaitingTime += waitingTime;

                int responseTime = Math.max(0, currentTask.getStartTime() - currentTask.getArrival());
                currentTask.setResponseTime(responseTime);
                totalResponseTime += responseTime;

                priorityQueues.get(currentTask.getPriority() - 1).poll(); // Remove the completed task
            }
        }

        double totalTurnaroundTime = 0;
        int taskCount = 0;
        for (Queue<Task> queue : priorityQueues) {
            for (Task task : queue) {
                totalTurnaroundTime += task.getTurnaround();
                taskCount++;
            }
        }

        double averageTurnaroundTime = totalTurnaroundTime / taskCount;
        double averageWaitingTime = totalWaitingTime / taskCount;
        double averageResponseTime = totalResponseTime / taskCount;

        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Response Time: " + averageResponseTime);
    }

    @Override
    public Task pickNextTask() {
        Task nextTask = null;
        for (Queue<Task> priorityQueue : priorityQueues) {
            if (!priorityQueue.isEmpty()) {
                Task task = priorityQueue.peek();
                if (nextTask == null || task.getPriority() > nextTask.getPriority()) {
                    nextTask = task;
                }
            }
        }
        return nextTask;
    }

    private void printTaskStates(int currentTime, Queue<Task> priorityQueue) {
        System.out.print("Time " + currentTime + ": Running: " + (priorityQueue.isEmpty() ? "-" : priorityQueue.peek().getName()));

        if (priorityQueue.size() > 1) {
            System.out.print(", Ready: ");
            Queue<Task> readyTasks = new LinkedList<>(priorityQueue);
            readyTasks.poll(); // Remove the currently running task
            while (!readyTasks.isEmpty()) {
                Task task = readyTasks.poll();
                System.out.print(task.getName() + " ");
            }
        } else {
            System.out.print(", Ready: -");
        }

        System.out.print(", Waiting: ");
        boolean anyTasksWaiting = false;

        for (Queue<Task> queue : priorityQueues) {
            for (Task task : queue) {
                Integer arrivalTime = task.getArrival();
                if (arrivalTime != null && arrivalTime > currentTime && arrivalTime <= currentTime + timeQuantum) {
                    System.out.print(task.getName() + " ");
                    anyTasksWaiting = true;
                }
            }

            if (!anyTasksWaiting) {
                System.out.print("-");
            }

            System.out.println();
        }
    }
}