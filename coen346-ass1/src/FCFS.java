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
            System.out.println("Running task: " + task.getName() + " (Start Time: " + currentTime + ")");
            currentTime += task.getBurst();
            System.out.println("Completed task: " + task.getName() + " (End Time: " + currentTime + ")");
        }

        double totalTurnaroundTime = 0;
        for (Task task : taskQueue) {
            totalTurnaroundTime += task.getTurnaround(currentTime);
        }

        double averageTurnaroundTime = totalTurnaroundTime / taskQueue.size();
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    @Override
    public Task pickNextTask() {
        if (currentIndex < taskQueue.size()) {
            return taskQueue.get(currentIndex++);
        }
        return null;
    }
}