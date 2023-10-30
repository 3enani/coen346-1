import java.util.*;
import java.text.DecimalFormat;
public class RR implements Algorithm {
    private List<Task> taskQueue;
    private int timeQuantum;

     public RR(List<Task> taskQueue,  int timeQuantum) {


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
        LinkedList<Task> readyQueue = new LinkedList<>(taskQueue);

        System.out.println("Round-Robin Scheduling with Time Quantum: " + timeQuantum);


        while (!readyQueue.isEmpty()) {

            Task currentTask = readyQueue.poll();

            if (currentTask.getArrival() > currentTime) {
                // Task arrives later, adjust the current time
                currentTime = currentTask.getArrival();
            }
            int remainingBurst = currentTask.getBurst();
            System.out.println("Time " + currentTime + ": Running task: " + currentTask.getName());

            if (remainingBurst <= timeQuantum) {
                currentTime += remainingBurst;
                currentTask.setCompletionTime(currentTime);
                System.out.println("Time " + currentTime + ": Completed task: " + currentTask.getName());
                totalTurnaroundTime += currentTask.getCompletionTime() - currentTask.getArrival();
                completedTasks++;

                int waitingTime = currentTask.getCompletionTime() - currentTask.getArrival() - currentTask.getBurst();
                currentTask.setWaitingTime(waitingTime);
                totalWaitingTime += waitingTime;

                int responseTime = Math.max(0, currentTask.getStartTime() - currentTask.getArrival());
                currentTask.setResponseTime(responseTime);
                totalResponseTime += responseTime;
            } else {
                // Task needs more time, enqueue it again

                currentTime += timeQuantum;
                    currentTask.decreaseBurstTime(timeQuantum);
                    readyQueue.offer(currentTask);
                }

            printTaskStates(currentTime, readyQueue);

            int turnaroundTime = currentTask.getCompletionTime() - currentTask.getArrival();
            currentTask.setTurnaroundTime(turnaroundTime);

            currentTask.setStartTime(currentTime);
        }
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
        if (!taskQueue.isEmpty()) {
            Task nextTask = taskQueue.get(0);
            taskQueue.remove(0);
            return nextTask;
        }
        return null;
    }

    private void printTaskStates(int currentTime, LinkedList<Task> readyQueue) {
        System.out.print("Time " + currentTime + ": Running: " + (readyQueue.isEmpty() ? "-" : readyQueue.peek().getName()));

        if (readyQueue.size() > 1) {
            System.out.print(", Ready: ");
            for (int i = 1; i < readyQueue.size(); i++) {
                System.out.print(readyQueue.get(i).getName() + " ");
            }
        } else {
            System.out.print(", Ready: -");
        }

        System.out.print(", Waiting: ");
        boolean anyTasksWaiting = false;

        for (Task task : taskQueue) {
            if (task.getArrival() != -1 && task.getArrival() > currentTime && task.getArrival() <= currentTime + timeQuantum) {
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
