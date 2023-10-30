import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Task
{
    // the representation of each task
    private String name;
    private int tid;
    private int priority;
    private int burst;
    private int arrival;
    private int turnaround;
    private int waitingTime;
    private int completionTime;
    private int startTime;
    private int responseTime;

    private List<Task> children;

    /**
     * We use an atomic integer to assign each task a unique task id.
     */
    private static AtomicInteger tidAllocator = new AtomicInteger();

    public Task(String name, int priority, int burst, int arrival, List<Task> children) {
        this.name = name;
        this.priority = priority;
        this.burst = burst;
        this.arrival = arrival;
        this.children = children;


        this.tid = tidAllocator.getAndIncrement();
    }

    /**
     * Appropriate getters
     */
    public String getName() {
        return name;
    }

    public int getTid() {
        return tid;
    }

    public int getTurnaround() {
        return turnaround;
    }
    public int getWaitingTime() {return waitingTime;}

    public int getPriority() {
        return priority;
    }

    public int getBurst() {
        return burst;
    }
    public int getArrival() {return arrival;}
    public int getStartTime() {return startTime;}
    public int getResponseTime() {return responseTime;}

    public void addChild (Task child) {
        children.add(child);
    }

    public List<Task> getChildren() {
        return children;
}

    /**
     * Appropriate setters
     */
    public int setPriority(int priority) {
        this.priority = priority;

        return priority;
    }

    public int setBurst(int burst) {
        this.burst = burst;

        return burst;
    }

    public int setArrival(int arrival) {
        this.arrival = arrival;
        return arrival;
    }

    public void setTurnaroundTime(int turnaround) {
        this.turnaround = turnaround;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setResponseTime (int responseTime) {
        this.responseTime = responseTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void decreaseBurstTime(int timeQuantum) {
        if (burst >= timeQuantum) {
            burst -= timeQuantum;
        } else {
            burst = 0;
        }
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * We override equals() so we can use a
     * Task object in Java collection classes.
     */
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (!(other instanceof Task))
            return false;

        /**
         * Otherwise we are dealing with another Task.
         * two tasks are equal if they have the same tid.
         */
        Task rhs = (Task)other;
        return (this.tid == rhs.tid) ? true : false;
    }

    public String toString() {
        return
                "Name: " + name + "\n" +
                        "Tid: " + tid + "\n" +
                        "Priority: " + priority + "\n" +
                        "Burst: " + burst + "\n";
    }
}