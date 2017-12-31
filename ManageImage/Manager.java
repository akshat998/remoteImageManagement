package ManageImage;

import GuiView.Main;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * A Manager stores and manages items at a file
 *
 * @param <T> The type of items to be stored
 */
public class Manager<T> implements Serializable {

    /**
     * the stored items
     */
    private LinkedList<T> items;
    /**
     * the file save location
     */
    private String fileName;

    /**
     * Constructs a new manager
     */
    public Manager(String fileName) {
        this.fileName = fileName;
        items = new LinkedList<>();
    }

    /**
     * Loads items from filename
     * <p>
     * Adapted: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html
     * Date: Nov 9, 2017
     * </p>
     */
    @SuppressWarnings("unchecked")
    public void load() {
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            items = (java.util.LinkedList<T>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            Main.logger.log(Level.SEVERE, "Serializable file path not found", e);
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "Improper file reading", e);
        } catch (ClassNotFoundException e) {
            Main.logger.log(Level.SEVERE, "Improper class path", e);
        }
    }

    /**
     * Saves items to fileName
     * <p>
     * Adapted from: http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html
     * Date: Nov 9, 2017
     * </p>
     */
    public void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(items);
            objectOutputStream.close();

            items.clear();
        } catch (FileNotFoundException e) {
            System.out.println("No file");

        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * Returns the items
     *
     * @return LinkedList<T> the stored items
     */
    public LinkedList<T> getAll() {
        return items;
    }

    /**
     * Adds unique item
     *
     * @param item the added item
     * @return indicates if the item was added
     */
    public boolean add(T item) {
        if (!items.contains(item)) {
            items.add(item);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all stored items
     */
    public void clear() {
        items.clear();
    }
}