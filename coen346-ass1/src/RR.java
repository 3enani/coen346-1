import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
public class RR implements Algorithm {
    private List<Task> taskQueue;
    private int timeQuantum;

    public RR(List<Task> taskQueue) {
        this.taskQueue = taskQueue;
        this.timeQuantum = 4;
    }

    @Override
    public void schedule() {
        Queue<Task> readyQueue = new LinkedList<>(taskQueue);
        int currentTime = 0;

        System.out.println("Round Robin scheduling with time quantum " + timeQuantum + ":");
        while (!readyQueue.isEmpty()) {
            Task currentTask = readyQueue.poll();

            System.out.println("Running task: " + currentTask.getName() + " (Start time: " + currentTime + ")");
            if (currentTask.getBurst() > timeQuantum) {
                currentTime += timeQuantum;
                currentTask.decreaseBurstTime(timeQuantum);
                readyQueue.offer(currentTask);
            } else {
                currentTime += currentTask.getBurst();
                currentTask.setCompletionTime(currentTime);
                System.out.println("Completed task: " + currentTask.getName() + " (End Time: " + currentTime + ")");

            }

            double totalTurnaroundTime = 0;
            for (Task task : taskQueue) {
                totalTurnaroundTime += task.getTurnaround(currentTime);
            }

            double averageTurnaroundTime = totalTurnaroundTime / taskQueue.size();
            System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        }

    }

    @Override
    public Task pickNextTask() {
        if (!taskQueue.isEmpty()) {
            Task nextTask = taskQueue.get(0);
            taskQueue.remove(0);
            return nextTask;
        }
        return null;
    }
}
