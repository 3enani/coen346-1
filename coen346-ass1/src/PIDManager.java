import java.io.IOException;

class PIDManager {

    static final int MIN_PID = 300;
    static final int MAX_PID = 5000;

    private static int[] pidArray;

    PIDManager() throws IOException {
    }

    static void allocateMap()throws Exception{
        pidArray = new int[MAX_PID - MIN_PID];
        if (pidArray == null) {
            System.out.println("Unsuccessful");
        }
        for (int i = 0; i < pidArray.length; i++){
            pidArray[i] = 0;
        }
        System.out.println("Success");
    }

    static int allocatePid()throws Exception{
        if (pidArray == null) {
            System.out.println("PID manager not initialized");
            return -1;
        }
        int pidNum = -1;
        for (int i = 0; i < pidArray.length; i++){
            if (pidArray[i] == 0){
                pidArray[i] = 1;
                pidNum = i + MIN_PID;
                break;
            }
        }
        if (pidNum == -1){
            System.out.println("Unable to allocate PID");
            return -1;
        }
        System.out.println("Allocate PID :" + pidNum);
        return pidNum;
    }

    static void releasePid(int pid){
        if (pidArray == null){
            System.out.println("PID manager not initialized");
            return;
        }
        if (pid < MIN_PID || pid > MAX_PID){
            System.out.println("Given PID is out of range");
        }

        int newPid = pid - MIN_PID;
        if (pidArray[newPid] == 0){
            System.out.println("PID " + pid + "is already released");
            return;
        }
        System.out.println("Releasing PID :" + pid);
        pidArray[newPid] = 0;
    }


}