import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Random;

public class TaskManager extends Thread {
    private static int TASK_COUNT = 30;
    private static int SLEEP_TIME = 1000;
    private ArrayList<Task> allTasksList = new ArrayList<Task>();
    private Random random = new Random();
    private Logger log = LogManager.getLogger();

//--------------------------------------------------------------------------------------------------------------------------------------------
// method that creates all tasks(randomly). If in Main.accountMap is less than 2 accounts, we cant create task
//--------------------------------------------------------------------------------------------------------------------------------------------
    public boolean createTasks()
    {
        if(Main.accountMap.size() > 2) {
            for (int i = 0; i < TASK_COUNT; i++) { // creates TASK_COUNT tasks
                ArrayList<String> keyList = new ArrayList<String>(Main.accountMap.keySet()); // get id for all accounts
                int keyListSize = keyList.size();

                String idFrom = keyList.get(random.nextInt(keyListSize));// generate random number (0 - keyListSize).
                                                                        // That number will be the index in keyList
                String idTo = keyList.get(random.nextInt(keyListSize));
                while (idFrom.equals(idTo)) { // you cant send money to yourself
                    idTo = keyList.get(random.nextInt(keyListSize)); // generate again
                }
                Integer moneyToSend = random.nextInt(Main.DEFAULT_MONEY); // generate random value of money to send.
                                                                        // It will be less than DEFAULT_MONEY
                Task tempTask = new Task(idFrom, idTo, moneyToSend);
                log.info("Task was created");
                allTasksList.add(tempTask);
                log.info("Add task to list");
            }
            return true;
        }
        else
        {
            log.warn("Less than 2 accounts in map");
            return false;
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// method that will run in thread
// method separates tasks to threads
//--------------------------------------------------------------------------------------------------------------------------------------------
@Override
    public void run()
    {
        while ((allTasksList.size() > 0) && (!isInterrupted()))
        {
            synchronized (Main.threadList) // take the mutex
            {
                for(ThreadWithTaskList thread: Main.threadList) {
                    if(!thread.getIsReady()) // thread already has task
                    {
                        continue;
                    }
                    if(allTasksList.size() == 0)
                    {
                        break;
                    }

                    thread.addTask(allTasksList.get(0));// send task to the thread
                    thread.setReady(false);
                    allTasksList.remove(0); // remove task from the list
                    log.info("Remove assigned task from the list ( tasks: " + allTasksList.size() + ")");
                }
            }
            try {
                sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                log.warn("Thread was interrupted");
                break;
            }
        }
        log.info("All tasks were assigned");
        interruptThreads();
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// method goes throw the threadList and interrupt them
//--------------------------------------------------------------------------------------------------------------------------------------------
    private void interruptThreads() {
        synchronized (Main.threadList) {
            for (ThreadWithTaskList thread : Main.threadList) {
                thread.interrupt();
                log.info("Interrupt thread " + thread.getId());
            }
        }
    }
}
