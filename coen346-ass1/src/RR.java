import java.util.*;
import java.text.DecimalFormat;

public class RR implements Algorithm {
    private List<Task> taskQueue;
    private int timeQuantum;
    private Task currentTask;

    public RR(List<Task> taskQueue, int timeQuantum) {
        this.taskQueue = taskQueue;
        this.timeQuantum = timeQuantum;
        Collections.sort(this.taskQueue, Comparator.comparing(Task::getArrival));
    }

    @Override
    public void schedule() {
        int currentTime = 0;
        double totalTurnaroundTime = 0;
        int completedTasks = 0;
        double totalWaitingTime = 0;
        double totalResponseTime = 0;
        LinkedList<Task> readyQueue = new LinkedList<>();
        LinkedList<Task> waitingQueue = new LinkedList<>(taskQueue); // Separate waiting queue

        System.out.println("Round-Robin Scheduling with Time Quantum: " + timeQuantum);

        while (completedTasks < taskQueue.size()) {
            // Enqueue tasks with arrival time less than or equal to the current time
            while (!waitingQueue.isEmpty() && waitingQueue.peek().getArrival() <= currentTime) {
                readyQueue.add(waitingQueue.poll());
            }

            if (currentTask == null) {
                if (readyQueue.isEmpty()) {
                    // No tasks are ready to run, increase the current time
                    System.out.println("Time " + currentTime + ": Idle");
                    currentTime++;
                } else {
                    currentTask = readyQueue.poll();
                    currentTask.setStartTime(currentTime);
                    int remainingBurst = currentTask.getBurst();
                    System.out.println("Time " + currentTime + ": Running task: " + currentTask.getName());

                    if (remainingBurst <= timeQuantum) {
                        currentTime += remainingBurst;
                        currentTask.setCompletionTime(currentTime);
                        // Calculate turnaround time, waiting time, and response time here
                        int turnaroundTime = currentTask.getCompletionTime() - currentTask.getArrival();
                        int waitingTime = turnaroundTime - currentTask.getBurst();
                        int responseTime = currentTask.getStartTime() - currentTask.getArrival();
                        totalTurnaroundTime += turnaroundTime;
                        totalWaitingTime += waitingTime;
                        totalResponseTime += responseTime;
                        completedTasks++;
                        System.out.println("Time " + currentTime + ": Completed task: " + currentTask.getName());
                        currentTask = null;
                    } else {
                        currentTime += timeQuantum;
                        currentTask.decreaseBurstTime(timeQuantum);
                        readyQueue.add(currentTask); // Re-enqueue the task
                        currentTask = null;
                    }
                    printTaskStates(currentTime, currentTask, readyQueue, waitingQueue);
                }
            } else {
                // Current task is running
                int remainingBurst = currentTask.getBurst();
                if (remainingBurst <= timeQuantum) {
                    currentTime += remainingBurst;
                    currentTask.setCompletionTime(currentTime);
                    // Calculate turnaround time, waiting time, and response time here
                    int turnaroundTime = currentTask.getCompletionTime() - currentTask.getArrival();
                    int waitingTime = turnaroundTime - currentTask.getBurst();
                    int responseTime = currentTask.getStartTime() - currentTask.getArrival();
                    totalTurnaroundTime += turnaroundTime;
                    totalWaitingTime += waitingTime;
                    totalResponseTime += responseTime;
                    completedTasks++;

                    // Mark the current task as completed.
                    currentTask.markAsCompleted();

                    System.out.println("Time " + currentTime + ": Completed task: " + currentTask.getName());
                    currentTask = null;
                } else {
                    currentTime += timeQuantum;
                    currentTask.decreaseBurstTime(timeQuantum);
                    readyQueue.add(currentTask); // Re-enqueue the task
                    currentTask = null;
                }
                printTaskStates(currentTime, currentTask, readyQueue, waitingQueue);
            }
        }

        // Calculate and print average times
        double averageTurnaroundTime = totalTurnaroundTime / completedTasks;
        double averageWaitingTime = totalWaitingTime / completedTasks;
        double averageResponseTime = totalResponseTime / completedTasks;

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Average Turnaround Time: " + df.format(averageTurnaroundTime));
        System.out.println("Average Waiting Time: " + df.format(averageWaitingTime));
        System.out.println("Average Response Time: " + df.format(averageResponseTime));
    }

    @Override
    public Task pickNextTask(int currentTime) {
        List<Task> remainingTasks = new ArrayList<>(taskQueue); // Create a copy of the taskQueue
        if (!remainingTasks.isEmpty()) {
            Task nextTask = remainingTasks.get(0);
            remainingTasks.remove(0);
            return nextTask;
        }
        return null;
    }

    private void printTaskStates(int currentTime, Task currentTask, LinkedList<Task> readyQueue, LinkedList<Task> waitingQueue) {
        System.out.print("Time " + currentTime + ": Running: " + (currentTask != null ? currentTask.getName() : "-"));

        System.out.print(", Ready: ");
        for (Task task : readyQueue) {
            System.out.print(task.getName() + " ");
        }

        System.out.print(", Waiting: ");
        for (Task task : waitingQueue) {
            System.out.print(task.getName() + " ");
        }

        System.out.println();
    }
}
