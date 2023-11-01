import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class TaskManager {
    private Queue<Task> taskQueue;

    public TaskManager() {
        taskQueue = new LinkedList<>();
    }

    public void addTaskToQueue(Task task) {
        taskQueue.add(task);
    }

    public Task getNextTask() {
        return taskQueue.poll();
    }

    public boolean isTaskQueueEmpty() {
        return taskQueue.isEmpty();
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Create a parent task with tid 3
        Task parentTask = new Task("ParentTask", 1, 0, 0, null);

        // Create a new Task with the parent task as the last argument
        taskManager.addTaskToQueue(new Task("P1", 1, 20, 0, new ArrayList<>()));

        // Retrieve and process tasks from the queue as needed
        Task nextTask = taskManager.getNextTask();
        if (nextTask != null) {
            // Process the task
            System.out.println("Processing task: " + nextTask.getName());
        }
    }
}
