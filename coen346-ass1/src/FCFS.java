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
        for (Task task : taskQueue) {
            System.out.println("Time: " + currentTime + " Running task: " + task.getName());
            printTaskStates(currentTime,taskQueue, task);
            if (task.getArrival() > currentTime) {

                currentTime = task.getArrival();
                System.out.println("Time " + currentTime + ": " + task.getName() + " arrives");
            }

            task.setStartTime(currentTime);
            int responseTime = task.getStartTime() - task.getArrival();
            task.setResponseTime(responseTime);

            currentTime += task.getBurst();
            task.setCompletionTime(currentTime);
            System.out.println("Time: " + currentTime + ": Completed task: " + task.getName());

            int turnaroundTime = currentTime - task.getArrival();
            int waitingTime = turnaroundTime - task.getBurst();
            task.setTurnaroundTime(turnaroundTime);
            task.setWaitingTime(waitingTime);

        }

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
                if (task != currentTask) {
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