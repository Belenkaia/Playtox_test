import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    public static int ACCOUNT_COUNT = 4;
    private static int MAX_ID = 9000;
    public static int THREADS_COUNT = 3;
    public static int DEFAULT_MONEY = 10000;
    private static Logger log = LogManager.getLogger();

    public static HashMap <String, Account> accountMap = new HashMap<String, Account>();
    public static ArrayList<ThreadWithTaskList> threadList = new ArrayList<ThreadWithTaskList>();

//--------------------------------------------------------------------------------------------------------------------------------------------
// application start point: method creates all accounts, tasks, starts taskManager and threads
//--------------------------------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        createAccountMap(); // create all accounts
        TaskManager taskManager = new TaskManager();
        if(!taskManager.createTasks()) // trying to create all tasks
        {
            log.info("Cant create tasks, trying again");
            if(!taskManager.createTasks())
            {
                log.error("Cant create task list");
                return;
            }
        }

        for(int i = 0; i < THREADS_COUNT; i ++)
        {
            ThreadWithTaskList tempThread = new ThreadWithTaskList();
            synchronized (threadList) //take the mutex (threadList)
            {
                threadList.add(tempThread);
                log.info("Thread with ID " + tempThread.getId() +  " was put into the list.");
            }
            tempThread.start();
            log.info("New thread with ID " + tempThread.getId() +  " was started.");
        }
        taskManager.start(); //start taskManager thread
        log.info("Task manager thread was started. (ID = " + taskManager.getId() + ").");
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// method creates a list with default accounts
//--------------------------------------------------------------------------------------------------------------------------------------------
    public static void createAccountMap()
    {
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_COUNT; i ++)
        {
            Integer randomID = random.nextInt(MAX_ID) + 1;
            while (accountMap.containsKey(randomID.toString())) // all ID have to be different
            {
                randomID = random.nextInt(MAX_ID) + 1;
            }
            String tempID = randomID.toString();
            Account tempAccount = new Account(tempID, DEFAULT_MONEY);
            log.info("Account with ID " + tempID + " was created.");
            accountMap.put(tempID, tempAccount);
            log.info("Account with ID " + tempID + " was put into the map.");
        }
    }
}
