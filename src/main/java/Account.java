import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Account {
    private String id;
    private Integer money;

    private Logger log = LogManager.getLogger();

    public Account(String id, int money)
    {
        this.id = id;
        this.money = money;
    }

    public void incrementMoney(int money)
    {
        synchronized (this.money)
        {
            this.money += money;
            log.info("Incremented money (+" + money + ") for Account with ID " + id);
        }
    }
    public void decrementMoney(int money)
    {
        synchronized (this.money)
        {
            this.money -= money;
            log.info("Decremented money (-" + money + ") for Account with ID " + id);
        }
    }

    public String getId() {
        return id;
    }
}
