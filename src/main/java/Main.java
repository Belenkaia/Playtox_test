import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    private static int ACCOUNT_COUNT = 4;
    private static int MAX_ID = 9000;
    private static int THREADS_COUNT = 3;
    public static int DEFAULT_MONEY = 10000;

    public static HashMap <String, Account> accountMap = new HashMap<String, Account>();
    public static ArrayList<ThreadWithTaskList> threadList = new ArrayList<ThreadWithTaskList>();
    public static void main(String[] args) {
        createAccountMap();

        TaskManager taskManager = new TaskManager();
        taskManager.createTasks();
        Thread taskManagerThread = new Thread(taskManager);
        taskManagerThread.start();

        for(int i = 0; i < THREADS_COUNT; i ++)
        {
            ThreadWithTaskList tempThread = new ThreadWithTaskList();
            tempThread.start();
            synchronized (threadList)
            {
                threadList.add(tempThread);
            }
        }

    }
    private static void createAccountMap()
    {
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_COUNT; i ++)
        {
            Integer randomID = random.nextInt(MAX_ID) + 1;
            String tempID = randomID.toString();
            Account tempAccount = new Account(tempID, DEFAULT_MONEY);
            accountMap.put(tempID, tempAccount);
        }
    }
}
