package JUnit;

import ManageImage.Manager;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The ManagerTest contains JUnit tests for the class Manager
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class ManagerTest {

    /**
     * The manager
     */
    private Manager<String> manager;


    /**
     * Recreate manager
     */
    @BeforeEach
    void setup() {
        manager = new Manager<>("managerTest.ser");
    }

    /**
     * Check if an empty manager contains no items
     */
    @Test
    void emptyWhenCreatedTest() {
        assertEquals(0, manager.getAll().size());
    }

    /**
     * Check if items are added into managers
     */
    @Test
    void addTest() {
        manager.add("1");
        assertTrue(manager.getAll().contains("1"));
    }

    /**
     * Check if the manager does not add duplicate items
     */
    @Test
    void addRepeatTest() {
        manager.add("1");
        assertFalse(manager.add("1"));
    }

    /**
     * Check if the manager correctly returns all items
     */
    @Test
    void getAllTest() {
        manager.add("1");
        manager.add("2");
        LinkedList<String> items = new LinkedList<>();
        items.add("1");
        items.add("2");
        assertEquals(items, manager.getAll());
    }

    /**
     * Check if the manager can save and load items
     */
    @Test
    void saveLoadTest() {
        manager.add("1");
        manager.add("2");
        manager.save();
        manager = new Manager<>("managerTest.ser");
        manager.load();
        LinkedList<String> items = new LinkedList<>();
        items.add("1");
        items.add("2");
        assertEquals(items, manager.getAll());
    }

    /**
     * Check if items are removed from managers
     */
    @Test
    void clearTest() {
        manager.add("1");
        manager.clear();
        assertEquals(0, manager.getAll().size());
    }
}
