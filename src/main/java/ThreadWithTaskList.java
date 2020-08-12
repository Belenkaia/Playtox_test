import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class ThreadWithTaskList extends Thread {
    private static int SLEEP_TIME_MS_MIN = 1000;
    private static int SLEEP_TIME_MS_MAX = 2000;
    private int sleepTime;
    private Boolean isReady = true;
    private ArrayList<Task> taskList = new ArrayList<Task>();

    private Logger log = LogManager.getLogger();

    @Override
    public void run() {
        while(true)
        {
            synchronized (taskList) {

                if (isInterrupted() && taskList.size() == 0) {
                    log.info("[ ID = " + this.getId() + " ] thread was interrupted and hasn't got tasks");
                    break;
                }
                if (taskList.size() > 0) {
                    Task currentTask = getFirstTask();
                    if(currentTask != null)
                    {
                        log.info("[ ID = " + this.getId() + " ] Run task");
                        currentTask.run();
                    }
                }
            }
            setNewSleepTime();

            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                log.warn("Thread was interrupted");
                interrupt();
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
            log.info("[ ID = " + this.getId() + " ] set isReady = " + value);
        }
    }

    public Task getFirstTask()
    {
        Task task;
        synchronized (taskList)
        {
            task = taskList.get(0);
            taskList.remove(0);
            log.info("[ ID = " + this.getId() + " ] get first task and delete it from list (tasks in list: " + taskList.size()+")");
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
        log.info("[ ID = " + this.getId() + " ] add task to taskList in thread (" + newTask.getIdFrom() + " -> " + newTask.getIdTo()+ ")");
        Task task = new Task(newTask);
        synchronized (taskList)
        {
            taskList.add(task);
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
        log.info("[ ID = " + this.getId() + " ]set sleep time: " + sleepTime);
    }
}
