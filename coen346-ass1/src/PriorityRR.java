import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
public class PriorityRR implements Algorithm{
    private List<Task> taskQueue;
    private int timeQuantum;

    public PriorityRR(List<Task> taskQueue) {
        this.taskQueue = taskQueue;
        this.timeQuantum = 4;
    }

    @Override
    public void schedule() {
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>(Comparator.comparing(Task::getPriority));
        priorityQueue.addAll(taskQueue);

        int currentTime = 0;

        System.out.println("Priority Round Robin scheduling with time quantum " + timeQuantum + ")");
        while (!priorityQueue.isEmpty()) {
            Task currentTask = priorityQueue.poll();

            System.out.println("Running task: " + currentTask.getName() + " (Start time: " + currentTime + ")");
            if (currentTask.getBurst() > timeQuantum) {
                currentTime += timeQuantum;
                currentTask.decreaseBurstTime(timeQuantum);
                priorityQueue.offer(currentTask);
            } else {
                currentTime += currentTask.getBurst();
                currentTask.setCompletionTime(currentTime);
                System.out.println("Completed task: " + currentTask.getName() + "(End Time: " + currentTime + ")");

            }
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
        Task nextTask = null;
        for (Task task : taskQueue) {
            if (nextTask == null || task.getPriority() > nextTask.getPriority()) {
                nextTask = task;
            }
        }
        return nextTask;
    }
}
