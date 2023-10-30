public class PCB {

    int pid;
    String processStatus;

    public PCB(int pid, String processStatus){
        setProcessStatus(processStatus);
        setPid(pid);
        getProcessStatus();
        getPid();
    }

    public int getPid(){

        System.out.println(pid);
        return pid;
    }
    public void setPid(int pid){
        this.pid = pid;
    }

    public String getProcessStatus(){
        System.out.println(processStatus);
        return processStatus;
    }

    public void setProcessStatus(String processStatus){
        this.processStatus = processStatus;
    }
}
