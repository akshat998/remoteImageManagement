package JUnit;

import ManageImage.TagManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The TagManagerTest contains JUnit tests for the class TagManager
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class TagManagerTest {

    /**
     * Since tagManager is static, we clear all stored items before each test
     */
    @BeforeEach
    void setup() {
        TagManager.clear();
    }

    /**
     * Check if tag manager is empty on clear
     */
    @Test
    void clearTest() {
        TagManager.add("Test");
        TagManager.clear();
        assertEquals(0, TagManager.getTags().size());
    }

    /**
     * Check if tags are added into tagManager
     */
    @Test
    void addTagTest() {
        TagManager.add("1");
        assertTrue(TagManager.getTags().contains("1"));
    }

    /**
     * Check if tagManager does not add repeated tags.
     */
    @Test
    void addRepeatTest() {
        TagManager.add("1");
        TagManager.add("1");
        assertEquals(1, TagManager.getTags().size());
    }

    /**
     * Check if tagManager properly returns all tags
     */
    @Test
    void getTagsTest() {
        TagManager.add("1");
        TagManager.add("2");
        LinkedList<String> items = new LinkedList<>();
        items.add("1");
        items.add("2");
        assertEquals(items, TagManager.getTags());
    }
}
