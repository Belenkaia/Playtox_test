import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Random;

public class ThreadWithTaskList extends Thread {
    private static int SLEEP_TIME_MS_MIN = 1000;
    private static int SLEEP_TIME_MS_MAX = 2000;

    private int sleepTime;
    private Boolean isReady = true;
    private ArrayList<Task> taskList = new ArrayList<Task>(); // now here is only one task,
    // but in case application has to work full day (without stopping after 30 tasks), such implementation may be useful
    private Logger log = LogManager.getLogger();

//--------------------------------------------------------------------------------------------------------------------------------------------
// method runs in thread
// thread run task until it will be interrupted and taskList will be clear
//--------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        while(true)
        {
            synchronized (taskList) {

                if (isInterrupted() && taskList.size() == 0) { // taskManager said that that was all tasks and thread has done it
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
                interrupt(); // we want to have that flag because thread will work until all his tasks will be done
            }
            setReady(true);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// get the flag
//--------------------------------------------------------------------------------------------------------------------------------------------
    public boolean getIsReady()
    {
        boolean res;
        synchronized (isReady)
        {
            res = isReady;
        }
        return res;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// set the flag
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void setReady(boolean value)
    {
        synchronized (isReady)
        {
            isReady = value;
            log.info("[ ID = " + this.getId() + " ] set isReady = " + value);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// get first task from taskList
//--------------------------------------------------------------------------------------------------------------------------------------------
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

//--------------------------------------------------------------------------------------------------------------------------------------------
// add task to the taskList
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void addTask(Task newTask)
    {
        log.info("[ ID = " + this.getId() + " ] add task to taskList in thread (" + newTask.getIdFrom() + " -> " + newTask.getIdTo()+ ")");
        Task task = new Task(newTask);
        synchronized (taskList)
        {
            taskList.add(task);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// delete task from the taskList
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteTask(Task task)
    {
        synchronized (taskList) {
            taskList.remove(task);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// set random sleep time in(SLEEP_TIME_MS_MIN - SLEEP_TIME_MS_MAX)
//--------------------------------------------------------------------------------------------------------------------------------------------
    private void setNewSleepTime()
    {
        Random random = new Random();
        sleepTime = random.nextInt(SLEEP_TIME_MS_MAX + SLEEP_TIME_MS_MIN) + SLEEP_TIME_MS_MIN;
        log.info("[ ID = " + this.getId() + " ]set sleep time: " + sleepTime);
    }
}
