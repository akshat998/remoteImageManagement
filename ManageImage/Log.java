package ManageImage;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Log of all changes to an ImageFile.
 * A Log is able to iterate through every Entry it has recorded.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class Log implements Iterable<Entry>, Serializable {

    /**
     * List of all changes, each stored as an Entry.
     */
    private List<Entry> history;

    /**
     * Construct a new Log.
     */
    public Log() {

        history = new ArrayList<>();

    }

    /**
     * Records a new Entry in the history.
     *
     * @param e ManageImage.Entry
     */
    public void addEntry(Entry e) {

        history.add(e);
        addToText(e);

    }

    /**
     * Adds log to the log.txt.
     *
     * @param e ManageImage.Entry
     */
    private static void addToText(Entry e) {

        // taken from https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
        try (FileWriter fw = new FileWriter("log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(e.toString() + (System.lineSeparator()));

        } catch (IOException e1) {

            e1.printStackTrace();

        }

    }


    /**
     * The Iterator of Log, used to go through each Entry.
     *
     * @return Iterator
     */
    @Override
    public Iterator<Entry> iterator() {

        return new LogIterator();

    }

    /**
     * The Iterator Log uses to go through each Entry found in history.
     */
    private class LogIterator implements Iterator<Entry> {

        /**
         * The index
         */
        private int index = 0;

        /**
         * Checks if there are more elements needed to be iterated through.
         *
         * @return boolean
         */
        @Override
        public boolean hasNext() {

            return index < history.size();

        }

        /**
         * The next Entry.
         *
         * @return Entry
         */
        @Override
        public Entry next() {

            return history.get(index++);

        }

    }

}
