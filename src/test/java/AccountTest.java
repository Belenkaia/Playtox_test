import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void incrementMoney() {
        int money = 1000;
        int incrementMoney = 100;
        Account account = new Account("111", money);
        account.incrementMoney(incrementMoney);
        assertTrue(account.getMoney() == money + incrementMoney);
    }

    @Test
    public void decrementMoney() {
        int money = 1000;
        int decrementMoney = 100;
        Account account = new Account("111", money);
        account.decrementMoney(decrementMoney);
        assertTrue(account.getMoney() == money - decrementMoney);
    }
    @Test
    public void moreThanNullMoney() {
        int money = 1000;
        int decrementMoney = 1001;
        Account account = new Account("111", money);
        account.decrementMoney(decrementMoney);
        assertTrue(account.getMoney() == money);
    }
}