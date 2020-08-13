import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void main() {
        Main.main(new String[]{" ", " "});
        synchronized (Main.accountMap)
        {
            assertTrue(Main.accountMap.size() == Main.ACCOUNT_COUNT);
        }
        synchronized (Main.threadList)
        {
            assertTrue(Main.threadList.size() == Main.THREADS_COUNT);
        }
    }
}