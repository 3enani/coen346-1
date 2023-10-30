import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
public class PriorityRR implements Algorithm {
    private List<Task> taskQueue;
    private int timeQuantum;

    public PriorityRR(List<Task> taskQueue, int timeQuantum) {
        //   public PriorityRR(List<Task> taskQueue) {


        this.taskQueue = taskQueue;
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void schedule() {
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>(Comparator.comparing(Task::getPriority));
        priorityQueue.addAll(taskQueue);

        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalResponseTime = 0;

        System.out.println("Priority Round Robin scheduling with time quantum " + timeQuantum + ")");
        while (!priorityQueue.isEmpty()) {
            Task currentTask = priorityQueue.poll();

            System.out.println("Time " + currentTime + ": Running task: " + currentTask.getName());
            printTaskStates(currentTime, priorityQueue);

            if (currentTask.getBurst() > timeQuantum) {
                currentTime += timeQuantum;
                currentTask.decreaseBurstTime(timeQuantum);
                priorityQueue.offer(currentTask);
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

            }

            int turnaroundTime = currentTask.getCompletionTime() - currentTask.getArrival();
            currentTask.setTurnaroundTime(turnaroundTime);

            currentTask.setStartTime(currentTime);
        }

        double totalTurnaroundTime = 0;
        for (Task task : taskQueue) {
            totalTurnaroundTime += task.getTurnaround();
        }

        double averageTurnaroundTime = totalTurnaroundTime / taskQueue.size();
        double averageWaitingTime = totalWaitingTime / taskQueue.size();
        double averageResponseTime = totalResponseTime / taskQueue.size();

        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Response Time: " + averageResponseTime);
    }

    @Override
    public Task pickNextTask() {
        Task nextTask = null;
        for (Task task : taskQueue) {
            if (nextTask == null || task.getPriority() > nextTask.getPriority()) {
                nextTask = task;
            }
        }
        return nextTask;
    }

    private void printTaskStates(int currentTime, PriorityQueue<Task> priorityQueue) {
        System.out.print("Time " + currentTime + ": Running: " + (priorityQueue.isEmpty() ? "-" : priorityQueue.peek().getName()));

        if (priorityQueue.size() > 1) {
            System.out.print(", Ready: ");
            PriorityQueue<Task> readyTasks = new PriorityQueue<>(priorityQueue);
            readyTasks.poll(); // Remove the currently running task
            while (!readyTasks.isEmpty()) {
                Task task = readyTasks.poll();
                Integer arrivalTime = task.getArrival();
                if (arrivalTime == null || arrivalTime <= currentTime) {
                    System.out.print(task.getName() + " ");
                }
            }
        } else {
            System.out.print(", Ready: -");
        }

        System.out.print(", Waiting: ");
        boolean anyTasksWaiting = false;

        for (Task task : taskQueue) {
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