import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Task implements Runnable{
    private String idFrom;
    private String idTo;
    private Integer sendMoney;
    private Logger log = LogManager.getLogger();

    public Task(Task anotherTask)
    {
        idFrom = anotherTask.getIdFrom();
        idTo = anotherTask.getIdTo();
        sendMoney = anotherTask.getSendMoney();
    }
    public Task(String idFrom, String idTo, Integer sendMoney)
    {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.sendMoney = sendMoney;
    }
    public void run() {
        synchronized (Main.accountMap.get(idFrom))
        {
            synchronized (Main.accountMap.get(idTo))
            {
                Main.accountMap.get(idFrom).decrementMoney(sendMoney);
                Main.accountMap.get(idTo).incrementMoney(sendMoney);
            }
        }
        log.info("Task was successfully done");
    }

    public String getIdFrom() {
        return idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public Integer getSendMoney() {
        return sendMoney;
    }
}
