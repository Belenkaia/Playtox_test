import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {

    @Test
    public void run() {
        Main.main(new String[]{" ", " "});
        int accountsSumm = 0;
        int expectedSumm = Main.DEFAULT_MONEY * Main.ACCOUNT_COUNT;
        synchronized (Main.accountMap)
        {
            for (String keyID : Main.accountMap.keySet()) {
                accountsSumm += Main.accountMap.get(keyID).getMoney();
            }
        }
        assertTrue(accountsSumm == expectedSumm);
    }

}