import java.util.ArrayList;

public class ThreadWithTaskList extends Thread {
    private ArrayList<Task> taskList;

    public ThreadWithTaskList(Runnable runnable)
    {
        super(runnable);
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }
}
