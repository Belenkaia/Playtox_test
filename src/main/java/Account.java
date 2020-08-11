public class Account {
    private String id;
    private int money;

    public Account(String id, int money)
    {
        this.id = id;
        this.money = money;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public void incrementMoney(int money)
    {
        synchronized (this)
        {
            this.money += money;
        }
    }
    public void decrementMoney(int money)
    {
        synchronized (this)
        {
            this.money -= money;
        }
    }
}
