import org.junit.Test;

import static org.junit.Assert.*;

public class TaskManagerTest {

    @Test
    public void createTasks() {
        TaskManager manager = new TaskManager();
        assertFalse(manager.createTasks());
        Main.createAccountMap();
        assertTrue(manager.createTasks());
    }
}