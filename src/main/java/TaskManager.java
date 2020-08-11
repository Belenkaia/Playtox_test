import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TaskManager implements Runnable {
    private static int TASK_COUNT = 30;
    private ArrayList<Task> allTasksList = new ArrayList<Task>();
    private Random random = new Random();


    public void createTasks()
    {
        for(int i = 0; i < TASK_COUNT; i ++)
        {//TODO: check if keyListSize < 2
            Set keySet = Main.accountMap.keySet();
            int keyListSize = keySet.size();

            Integer idFrom = random.nextInt(keyListSize);
            Integer idTo = random.nextInt(keyListSize);
            while (idFrom == idTo)
            {
                idTo = random.nextInt(keyListSize);
            }
            Integer moneyToSend = random.nextInt(Main.DEFAULT_MONEY);

            Task tempTask = new Task(idFrom.toString(), idTo.toString(), moneyToSend);
            allTasksList.add(tempTask);
        }
    }

    public void run()
    {
        synchronized (Main.threadList)
        {
            while (allTasksList.size() > 0)
            {
                for(ThreadWithTaskList thread: Main.threadList) {
                    if(!thread.getIsReady())
                    {
                        continue;
                    }

                    thread.addTask(allTasksList.get(0));
                    thread.setReady(false);
                    allTasksList.remove(0);
                }

            }
            interruptThreads();
        }
    }

    private void interruptThreads() {
        synchronized (Main.threadList) {
            for (ThreadWithTaskList thread : Main.threadList) {
                thread.interrupt();
            }

        }
    }
}
