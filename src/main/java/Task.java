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
        synchronized (Main.accountMap)
        {
            Account account = Main.accountMap.get(idFrom);
            if(account != null)
                account.decrementMoney(sendMoney);
            else
                log.warn("Cant found account (from)");
            account = Main.accountMap.get(idTo);
            if(account != null)
                account.incrementMoney(sendMoney);
            else
                log.warn("Cant fount account (to)");
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
