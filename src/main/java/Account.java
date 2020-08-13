import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Account {
    private String id;
    private Integer money;
    private Logger log = LogManager.getLogger();

//--------------------------------------------------------------------------------------------------------------------------------------------
// Constructor: create an account with id and money
//--------------------------------------------------------------------------------------------------------------------------------------------
    public Account(String id, int money)
    {
        this.id = id;
        this.money = money;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// method is trying to take a mutex (money) and then incrementing money
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void incrementMoney(int money)
    {
        synchronized (this.money)
        {
            this.money += money;
            log.info("Incremented money (+" + money + ") for Account with ID " + id);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
// method is trying to take a mutex (money) and after checking (money - decrement > 0), decrementing it
//--------------------------------------------------------------------------------------------------------------------------------------------
    public boolean decrementMoney(int money)
    {
        boolean canDecrement = false;
        synchronized (this.money)
        {
            if(this.money - money > 0) // this.money has to be > 0 every time
            {
                this.money -= money;
                log.info("Decremented money (-" + money + ") for Account with ID " + id);
                canDecrement = true;
            }
            else
            {
                log.warn("Not enough money on account (" + id + ")");
            }
        }
        return canDecrement;
    }
}
