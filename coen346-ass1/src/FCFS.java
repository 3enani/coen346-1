import java.util.ArrayList;
import java.util.List;

public class FCFS implements Algorithm {
    private List<Task> taskQueue;
    private int currentIndex;

    public FCFS(List<Task> taskQueue) {
        this.taskQueue = taskQueue;
        this.currentIndex = 0;
    }

    @Override
    public void schedule() {
        int currentTime = 0;
        System.out.println("FCFS Scheduling:");

        List<Task> scheduledTasks = new ArrayList<>(taskQueue);  // Create a list of tasks to schedule

        while (!scheduledTasks.isEmpty()) {  // Continue scheduling until all tasks are done
            Task currentTask = scheduledTasks.get(0);
            printTaskStates(currentTime, taskQueue, currentTask);

            // Check if this task can be scheduled
            if (currentTask.getArrival() <= currentTime) {
                if (!currentTask.isCompleted()) {
                    System.out.println("Time: " + currentTime + " Running task: " + currentTask.getName());

                    // Calculate and set response time, completion time, turnaround time, and waiting time
                    currentTask.setStartTime(currentTime);
                    int responseTime = currentTask.getStartTime() - currentTask.getArrival();
                    currentTask.setResponseTime(responseTime);
                    currentTime += currentTask.getBurst();
                    currentTask.setCompletionTime(currentTime);
                    int turnaroundTime = currentTime - currentTask.getArrival();
                    int waitingTime = turnaroundTime - currentTask.getBurst();
                    currentTask.setTurnaroundTime(turnaroundTime);
                    currentTask.setWaitingTime(waitingTime);

                    // Mark this task as completed
                    currentTask.setCompleted(true);
                      // Remove the completed task from the list
                }
                scheduledTasks.remove(0);
                System.out.println("Time: " + currentTime + ": Completed task: " + currentTask.getName());
            } else {
                // No task to schedule yet, so wait for the next arrival
                currentTime = currentTask.getArrival();
                System.out.println("Time " + currentTime + ": " + currentTask.getName() + " arrives");
            }
        }

        // Calculate and print average statistics
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;
        double totalResponseTime = 0;

        for (Task task : taskQueue) {
            totalTurnaroundTime += task.getTurnaround();
            totalWaitingTime += task.getWaitingTime();
            totalResponseTime += task.getResponseTime();
        }

        double averageTurnaroundTime = totalTurnaroundTime / taskQueue.size();
        double averageWaitingTime = totalWaitingTime / taskQueue.size();
        double averageResponseTime = totalResponseTime / taskQueue.size();

        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Response Time: " + averageResponseTime);
    }

    @Override
    public Task pickNextTask(int currentTime) {
        if (currentIndex < taskQueue.size()) {
            return taskQueue.get(currentIndex++);
        }
        return null;
    }

    private void printTaskStates(int currentTime, List<Task> taskQueue, Task currentTask) {
        System.out.print("Time " + currentTime + ": Running: " + currentTask.getName());

        if (taskQueue.size() > 1) {
            System.out.print(", Ready: ");
            for (Task task : taskQueue) {
                if (task != currentTask && !task.isCompleted()) {
                    System.out.print(task.getName() + " ");
                }
            }
        } else {
            System.out.print(", Ready: -");
        }

        System.out.print(", Waiting: ");
        for (Task task : taskQueue) {
            if (task.getArrival() > currentTime) {
                System.out.print(task.getName() + " ");
            }
        }
        System.out.println();
    }
}