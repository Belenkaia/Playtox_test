import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Task implements Runnable{
    private String idFrom;
    private String idTo;
    private Integer sendMoney;
    private Logger log = LogManager.getLogger();

//--------------------------------------------------------------------------------------------------------------------------------------------
// Copy constructor
//--------------------------------------------------------------------------------------------------------------------------------------------
    public Task(Task anotherTask)
    {
        idFrom = anotherTask.getIdFrom();
        idTo = anotherTask.getIdTo();
        sendMoney = anotherTask.getSendMoney();
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// Constructor
//--------------------------------------------------------------------------------------------------------------------------------------------
    public Task(String idFrom, String idTo, Integer sendMoney)
    {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.sendMoney = sendMoney;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// method that will be executed in thread
// method sends money from one account to another
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void run() {
        synchronized (Main.accountMap.get(idFrom)) // take the mutex (Account, from that we want to send money)
        {
            synchronized (Main.accountMap.get(idTo)) // take the mutex (Account, to that we want to send money)
            {
                if(Main.accountMap.get(idFrom).decrementMoney(sendMoney)) // if there is enough money for decrement it
                    Main.accountMap.get(idTo).incrementMoney(sendMoney);
            }
        }
        log.info("Task was successfully done");
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------------------------------------------------
    public String getIdFrom() {
        return idFrom;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------------------------------------------------
    public String getIdTo() {
        return idTo;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------------------------------------------------
    public Integer getSendMoney() {
        return sendMoney;
    }
}
