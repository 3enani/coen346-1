import java.util.*;
import java.io.*;

public class Driver
{
    public static void main(String[] args) throws IOException {
 //       if (args.length != 2) {
        if (args.length != 3) {
            System.err.println("Usage: java Driver <Scheduling Algorithm> <Input File> <Time Quantum>");
//            System.err.println("Usage: java Driver <Scheduling Algorithm> <Input File>");
            System.exit(1);
        }

        BufferedReader inFile = new BufferedReader(new FileReader(args[1]));

        String schedule;

        // create the queue of tasks
        List<Task> queue = new ArrayList<Task>();
        // read in the tasks and populate the ready queue
        while ( (schedule = inFile.readLine()) != null) {
            String[] params = schedule.split(",\\s*");
            queue.add(new Task(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3])));
        }

        inFile.close();

        Algorithm scheduler = null;
        String choice = args[0].toUpperCase();
        int timeQuantum = Integer.parseInt(args[2]);



        switch(choice) {
            case "FCFS":
                scheduler = new FCFS(queue);
                break;
            case "RR":
                scheduler = new RR(queue, timeQuantum);
//                scheduler = new RR(queue);
                break;
            case "PRI-RR":
                scheduler = new PriorityRR(queue, timeQuantum);
//                scheduler = new PriorityRR(queue);

                break;
            default:
                System.err.println("Invalid algorithm");
                System.exit(0);
        }

        // start the scheduler
        scheduler.schedule();
    }

}