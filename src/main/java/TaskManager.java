import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TaskManager extends Thread {
    private static int TASK_COUNT = 30;
    private static int SLEEP_TIME = 1000;
    private ArrayList<Task> allTasksList = new ArrayList<Task>();
    private Random random = new Random();
    private Logger log = LogManager.getLogger();

    public boolean createTasks()
    {
        if(Main.accountMap.size() > 2) {
            for (int i = 0; i < TASK_COUNT; i++) {
                ArrayList<String> keyList = new ArrayList<String>(Main.accountMap.keySet());
                int keyListSize = keyList.size();

                String idFrom = keyList.get(random.nextInt(keyListSize));

                String idTo = keyList.get(random.nextInt(keyListSize));
                while (idFrom.equals(idTo)) {
                    idTo = keyList.get(random.nextInt(keyListSize));
                }
                Integer moneyToSend = random.nextInt(Main.DEFAULT_MONEY);

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

    public void run()
    {

        while ((allTasksList.size() > 0) && (!isInterrupted()))
        {
            synchronized (Main.threadList)
            {
                for(ThreadWithTaskList thread: Main.threadList) {
                    if(!thread.getIsReady())
                    {
                        continue;
                    }
                    if(allTasksList.size() == 0)
                    {
                        break;
                    }

                    thread.addTask(allTasksList.get(0));
                    thread.setReady(false);
                    allTasksList.remove(0);
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

    private void interruptThreads() {
        synchronized (Main.threadList) {
            for (ThreadWithTaskList thread : Main.threadList) {
                thread.interrupt();
                log.info("Interrupt thread " + thread.getId());
            }

        }
    }
}
