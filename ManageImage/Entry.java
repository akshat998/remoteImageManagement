package ManageImage;

import java.io.Serializable;
import java.util.Date;

/**
 * The state of an ImageFile at a given time.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class Entry implements Serializable {

    /**
     * The name the ImageFile had at this time.
     */
    private String entryName;

    /**
     * The time the Entry was created.
     */
    private Date currDate;

    /**
     * Construct a new Entry.
     *
     * @param entryName String
     */
    public Entry(String entryName) {

        this.entryName = entryName;
        this.currDate = new Date();

    }

    /**
     * Get the date of the entry
     *
     * @return the date
     */
    private Date getDate() {
        return this.currDate;
    }

    /**
     * Get the name of the entry
     *
     * @return the name
     */
    private String getEntryName() {
        return entryName;
    }

    /**
     * Return the entry in an easy to read format. date acquired - entry information.
     * <p>ex.  Mon Nov 13 23:08:15 EST 2017 - Added new tag: @beach</p>
     *
     * @return the string representation of entry
     */
    @Override
    public String toString() {

        return getDate().toString() + " - " + getEntryName();

    }
}
