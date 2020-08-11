import java.util.ArrayList;
import java.util.Random;

public class ThreadWithTaskList extends Thread {
    private static int SLEEP_TIME_MS_MIN = 1000;
    private static int SLEEP_TIME_MS_MAX = 2000;
    private int sleepTime;
    private Boolean isReady = true;
    private ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    public void run() {
        while(true)
        {
            int taskListSize;
            synchronized (taskList){
                taskListSize = taskList.size();
            }

            if(isInterrupted() && taskListSize == 0)
            {//TODO: log
                break;
            }
            if(taskListSize > 0)
            {
                Task currentTask = getFirstTask();
                currentTask.run();
            }
            setNewSleepTime();

            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO: logger
            }
            setReady(true);
        }
    }


    public boolean getIsReady()
    {
        boolean res;
        synchronized (isReady)
        {
            res = isReady;
        }
        return res;
    }

    public void setReady(boolean value)
    {
        synchronized (isReady)
        {
            isReady = value;
        }
    }

    public Task getFirstTask()
    {
        Task task;
        synchronized (taskList)
        {
            task = taskList.get(0);
            taskList.remove(0);
        }
        return task;
    }
    public void setTaskList(ArrayList<Task> taskList) {
        synchronized (this.taskList)
        {
            this.taskList = taskList;
        }
    }
    public void addTask(Task newTask)
    {
        synchronized (taskList)
        {
            taskList.add(newTask);
        }
    }
    public void deleteTask(Task task)
    {
        synchronized (taskList) {
            taskList.remove(task);
        }
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }
    private void setNewSleepTime()
    {
        Random random = new Random();
        sleepTime = random.nextInt(SLEEP_TIME_MS_MAX + SLEEP_TIME_MS_MIN) + SLEEP_TIME_MS_MIN;
    }
}
