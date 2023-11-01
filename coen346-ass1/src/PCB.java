import java.util.ArrayList;
import java.util.List;

public class PCB {
    int tid;
    String processStatus;
    List<Task> childProcessesWaiting;

    public PCB(int tid, String processStatus) {
        setProcessStatus(processStatus);
        setTid(tid);
        childProcessesWaiting = new ArrayList<>();
    }

    private void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    private void setTid(int tid) {
        this.tid = tid;
    }

    public void addChildProcessToWaitQueue(Task childProcess) {
        childProcessesWaiting.add(childProcess);
    }

    public void removeChildProcessFromWaitQueue(Task childProcess) {
        childProcessesWaiting.remove(childProcess);
    }

    public boolean isWaitingForChildProcesses() {
        return !childProcessesWaiting.isEmpty();
    }

    public int getTid() {
        return tid;
    }
}