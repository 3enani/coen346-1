import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Driver
{
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = new TaskManager();

        int timeQuantum;
        List<Task> queue = new ArrayList<Task>();



            queue.add(new Task("P1", 1, 20, 0, new ArrayList<>(3)));
            queue.add(new Task("P2", 2, 5, 4,new ArrayList<>(0)));
            queue.add(new Task("P3", 3, 10, 5, new ArrayList<>(0)));
            queue.add(new Task("P4", 3, 15, 6, new ArrayList<>(0)));
            queue.add(new Task("P5", 5, 10, 0, new ArrayList<>(0)));




//        if (args.length != 3) {
//            System.err.println("Usage: java Driver <Scheduling Algorithm> <Input File> <Time Quantum>");
//
//            System.exit(1);
//        }
//
//        BufferedReader inFile = new BufferedReader(new FileReader(args[1]));
//
//        String schedule;
//
//        // create the queue of tasks
//        List<Task> queue = new ArrayList<Task>();
//        // read in the tasks and populate the ready queue
//        while ( (schedule = inFile.readLine()) != null) {
//            String[] params = schedule.split(",\\s*");
//            queue.add(new Task(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3])));
//        }
//
//        inFile.close();

        Algorithm scheduler = null;
        String choice = args[0].toUpperCase();
//        int timeQuantum = Integer.parseInt(args[2]);
        if (choice.equals("FCFS")) {
            // Set a default time quantum for FCFS
            timeQuantum = 1; // Set your default time quantum here
        } else {
            // Prompt for the time quantum for other algorithms
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the time quantum: ");
            timeQuantum = scanner.nextInt();
            scanner.close();
        }



        switch(choice) {
            case "FCFS":
                scheduler = new FCFS(queue);
                break;
            case "RR":
                scheduler = new RR(queue, timeQuantum);

                break;
            case "PRI-RR":
                scheduler = new PriorityRR(queue, timeQuantum);


                break;
            default:
                System.err.println("Invalid algorithm");
                System.exit(0);
        }

        // start the scheduler
        scheduler.schedule();
    }

}