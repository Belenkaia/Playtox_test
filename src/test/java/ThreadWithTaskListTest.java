import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ThreadWithTaskListTest {
    private Task newTask = new Task("111", "112", 100);
    private ThreadWithTaskList tempThread = new ThreadWithTaskList();
    private ThreadWithTaskList tempThread2 = new ThreadWithTaskList();
    private Logger log = LogManager.getLogger();

    @Test
    public void addTask() {
        tempThread.addTask(newTask);
        Task resTask = tempThread.getFirstTask();
        assertEquals(newTask.getIdFrom(), resTask.getIdFrom());
        assertEquals(newTask.getIdTo(), resTask.getIdTo());
        assertEquals(newTask.getSendMoney(), resTask.getSendMoney());
    }
    @Test
    public void addTaskNull() {
        tempThread.addTask(newTask);
        tempThread.getFirstTask();
        assertNull(tempThread.getFirstTask());
    }

    @Test
    public void run() {
        int money1 = 1000;
        int money2 = 2000;
        int money3 = 3000;
        int money4 = 4000;
        int sendMoney = 100;
        Account account1 = new Account("111", money1);
        Account account2 = new Account("112", money2);
        Account account3 = new Account("113", money3);
        Account account4 = new Account("114", money4);

        Main.accountMap.put(account1.getId(), account1);
        Main.accountMap.put(account2.getId(), account2);
        Main.accountMap.put(account3.getId(), account3);
        Main.accountMap.put(account4.getId(), account4);

        Task task1 = new Task(account1.getId(), account2.getId(), sendMoney); // 1 -> 2
        Task task2 = new Task(account4.getId(), account3.getId(), sendMoney + 3); // 4 -> 3

        tempThread.addTask(task1);
        tempThread2.addTask(task2);
        tempThread2.setReady(false);
        tempThread.setReady(false);

        tempThread.start();
        tempThread2.start();

        while (!tempThread.getIsReady())
        {
            waitThread(1);
        }
        checkTransaction(account1, account2, money1 - sendMoney, money2 + sendMoney);

        while (!tempThread2.getIsReady())
        {
            waitThread(1);
        }
        checkTransaction(account4, account3, money4 - (sendMoney + 3), money3 + sendMoney + 3);
        tempThread2.interrupt();
        tempThread.interrupt();
    }

    private void checkTransaction(Account account1, Account account2, int money1, int money2)
    {
        synchronized (account1)
        {
            assertTrue(account1.getMoney() == money1);

        }
        synchronized (account2)
        {
            assertTrue(account2.getMoney() == money2);
        }
    }

    private void waitThread(int sec)
    {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            log.error("Interrupted thread");
            assertTrue(false);
        }
    }
}