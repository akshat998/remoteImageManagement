package JUnit;

import ManageImage.Entry;
import ManageImage.Log;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The LogTest contains JUnit tests for the class Log
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class LogTest {

    /**
     * The log
     */
    private Log log;

    /**
     * Reconstruct log
     */
    @BeforeEach
    void setup() {
        log = new Log();
    }

    /**
     * Check if a new log contains 0 entries
     */
    @Test
    void constructorNoEntryTest() {
        int logCount = 0;
        for (Entry ignored : log) {
            logCount++;
        }
        assertEquals(0, logCount);
    }

    /**
     * Check if entries can be added to logs
     */
    @Test
    void addTest() {
        Entry entryRaw = new Entry("Unit Test Message");
        log.addEntry(entryRaw);
        for (Entry entry : log) {
            assertEquals(entryRaw, entry);
        }
    }

    /**
     * Check if log iterates correctly
     */
    @Test
    void iteratorTest() {
        LinkedList<Entry> entries = new LinkedList<>();
        entries.add(new Entry("1"));
        entries.add(new Entry("2"));
        entries.add(new Entry("3"));
        entries.add(new Entry("4"));
        for (Entry entry : entries) {
            log.addEntry(entry);
        }
        int counter = 0;
        for (Entry entry : log) {
            assertEquals(entries.get(counter), entry);
            counter++;
        }
        assertEquals(4, counter);
    }
}