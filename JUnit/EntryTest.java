package JUnit;

import ManageImage.Entry;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The EntryTest contains JUnit tests for the class Entry
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class EntryTest {

    /**
     * The entry
     */
    private Entry entry;
    /**
     * The date the entry was created
     */
    private Date date;

    /**
     * Refresh entry and date
     */
    @BeforeEach
    void setup() {
        entry = new Entry("Entry message");
        date = new Date();
    }

    /**
     * Check if string representation is "date - message"
     */
    @Test
    void toStringTest() {
        assertEquals(date.toString() + " - Entry message", entry.toString());
    }
}