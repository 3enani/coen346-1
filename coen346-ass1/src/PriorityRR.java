import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class PriorityRR implements Algorithm {
    private final List<Task> taskQueue;
    private final int timeQuantum;
    private final List<Task> completedTasks;

    public PriorityRR(List<Task> taskQueue, int timeQuantum) {
        this.taskQueue = taskQueue;
        this.timeQuantum = timeQuantum;
        this.completedTasks = new ArrayList<>();
    }

    @Override
    public void schedule() {
        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalResponseTime = 0;
        double totalTurnaroundTime = 0; // Initialize total turnaround time
        PriorityQueue<Task> readyQueue = new PriorityQueue<>(Comparator.comparing(Task::getPriority).thenComparing(Task::getArrival));
        Queue<Task> waitingQueue = new LinkedList<>(taskQueue);

        System.out.println("Priority Round Robin scheduling with time quantum " + timeQuantum);

        while (!waitingQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Enqueue tasks with arrival time less than or equal to the current time, including tasks with arrival time of 0
            while (!waitingQueue.isEmpty() && waitingQueue.peek().getArrival() <= currentTime) {
                Task nextTask = waitingQueue.poll();
                readyQueue.offer(nextTask); // Make sure all tasks, including P5 with arrival at 0, go into the ready queue
            }

            if (readyQueue.isEmpty()) {
                System.out.println("Time " + currentTime + ": Idle");
                currentTime++;
            } else {
                Task currentTask = readyQueue.poll();
                currentTask.setStartTime(currentTime); // Set the start time for the task
                System.out.println("Time " + currentTime + ": Running task: " + currentTask.getName());
                printTaskStates(currentTime, currentTask, readyQueue);

                if (currentTask.getBurst() > timeQuantum) {
                    currentTime += timeQuantum;
                    currentTask.decreaseBurstTime(timeQuantum);
                    readyQueue.offer(currentTask); // Re-enqueue the task
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

                    int turnaroundTime = currentTask.getCompletionTime() - currentTask.getArrival();
                    currentTask.setTurnaroundTime(turnaroundTime);
                    totalTurnaroundTime += turnaroundTime;

                    completedTasks.add(currentTask); // Add completed task to the completedTasks list
                }
            }
        }

        // Calculate and print average times
        double averageTurnaroundTime = totalTurnaroundTime / taskQueue.size();
        double averageWaitingTime = totalWaitingTime / taskQueue.size();
        double averageResponseTime = totalResponseTime / taskQueue.size();

        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Response Time: " + averageResponseTime);
    }

    @Override
    public Task pickNextTask(int currentTime) {
        Task nextTask = null;
        for (Task task : taskQueue) {
            if (!completedTasks.contains(task) && task.getBurst() > 0 && task.getArrival() <= currentTime) {
                if (nextTask == null ||
                        task.getPriority() > nextTask.getPriority() ||
                        (task.getPriority() == nextTask.getPriority() && task.getArrival() < nextTask.getArrival())) {
                    nextTask = task;
                }
            }
        }
        return nextTask;
    }

    private void printTaskStates(int currentTime, Task currentTask, PriorityQueue<Task> priorityQueue) {
        System.out.print("Time " + currentTime + ": Running task: " + currentTask.getName());

        System.out.print(" Ready: ");
        boolean anyTasksReady = false;

        for (Task task : taskQueue) {
            if (!completedTasks.contains(task) && task != currentTask && task.getArrival() <= currentTime && task.getBurst() > 0) {
                System.out.print(" " + task.getName());
                anyTasksReady = true;
            }
        }

        if (!anyTasksReady) {
            System.out.print("-");
        }

        System.out.print(", Waiting: ");
        boolean anyTasksWaiting = false;

        for (Task task : taskQueue) {
            if (!completedTasks.contains(task) && task != currentTask && task.getArrival() > currentTime && task.getBurst() > 0) {
                System.out.print(" " + task.getName());
                anyTasksWaiting = true;
            }
        }

        if (!anyTasksWaiting) {
            System.out.print("-");
        }

        System.out.println();
    }
}
