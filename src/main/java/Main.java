import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    private static int ACCOUNT_COUNT = 4;
    private static int MAX_ID = 9000;
    private static int THREADS_COUNT = 3;
    public static int DEFAULT_MONEY = 10000;
    private static Logger log = LogManager.getLogger();

    public static HashMap <String, Account> accountMap = new HashMap<String, Account>();
    public static ArrayList<ThreadWithTaskList> threadList = new ArrayList<ThreadWithTaskList>();
    public static void main(String[] args) {
        createAccountMap();

        TaskManager taskManager = new TaskManager();
        while(!taskManager.createTasks())
        {
            log.info("Cant create tasks, trying again");
        }


        for(int i = 0; i < THREADS_COUNT; i ++)
        {
            ThreadWithTaskList tempThread = new ThreadWithTaskList();

            synchronized (threadList)
            {
                threadList.add(tempThread);
                log.info("Thread with ID " + tempThread.getId() +  " was put into the list.");
            }
            tempThread.start();
            log.info("New thread with ID " + tempThread.getId() +  " was started.");
        }
        taskManager.start();
        log.info("Task manager thread was started. (ID = " + taskManager.getId() + ").");

    }
    private static void createAccountMap()
    {
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_COUNT; i ++)
        {
            Integer randomID = random.nextInt(MAX_ID) + 1;
            String tempID = randomID.toString();
            Account tempAccount = new Account(tempID, DEFAULT_MONEY);
            log.info("Account with ID " + tempID + " was created.");
            accountMap.put(tempID, tempAccount);
            log.info("Account with ID " + tempID + " was put into the map.");
        }
    }
}
