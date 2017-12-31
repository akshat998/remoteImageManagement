package ManageImage;

import java.util.LinkedList;

/**
 * TagManager represents a pool of unique tags
 * <p>TagManager's tags can be taken saved to or loaded from tags.ser</p>
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class TagManager {

    /**
     * the manager that stores tags
     */
    private static Manager<String> manager = new Manager<String>("tags.ser") {
    };

    /**
     * Loads tags from tags.ser
     */
    @SuppressWarnings("unchecked")
    public static void load() {
        manager.load();
    }

    /**
     * Saves tags to tags.ser
     */
    public static void save() {
        manager.save();
    }

    /**
     * Returns the global tags
     *
     * @return LinkedList<String> the global tags
     */
    public static LinkedList<String> getTags() {
        return manager.getAll();
    }

    /**
     * Adds tag to tagManager if tag is not yet added
     *
     * @param tag the tag to be added to ManageImage.TagManager
     */
    public static void add(String tag) {
        manager.add(tag);
    }

    /**
     * Removes all stored tags
     */
    public static void clear() {
        manager.clear();
    }
}
