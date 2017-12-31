package ManageImage;

import GuiView.Main;
import GuiView.PicGridView;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;

/**
 * ImageManager represents a collection of images
 * <p>ImageManager's images can be taken saved to or loaded from out.ser</p>
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class ImageManager implements Serializable {

    /**
     * The manager that stores imageFiles
     */
    private static Manager<ImageFile> manager = new Manager<ImageFile>("out.ser") {
    };

    /**
     * Loads imageFiles from out.ser
     */
    @SuppressWarnings("unchecked")
    public static void load() {
        manager.load();
    }

    /**
     * Saves imageFiles to out.ser
     */
    public static void save() {
        manager.save();
    }

    /**
     * Get images stored in directory
     * <p>Assumes stored images and directory all use absolute paths</p>
     *
     * @param directory the directory that contains returned images
     * @return the images stored in directory
     */
    public static ArrayList<ImageFile> getImageFilesByDirectory(File directory) {

        // cite:
        // https://stackoverflow.com/questions/4746671/how-to-check-if-a-given-path-is-possible-child-of-another-path
        ArrayList<ImageFile> filteredImages = new ArrayList<>();
        LinkedList<ImageFile> imageFiles = manager.getAll();
        for (ImageFile imageFile : imageFiles) {
            File imageDirectory = imageFile.getDirectory();
            Path child = Paths.get(imageDirectory.toString()).toAbsolutePath();
            Path parent = Paths.get(directory.toString()).toAbsolutePath();
            if (child.startsWith(parent)) {
                filteredImages.add(imageFile);
            }
        }
        return filteredImages;
    }

    /**
     * Get images stored in directory (only in sub-folders)
     * <p>Assumes stored images and directory all use absolute paths</p>
     *
     * @param directory the directory that contains returned images
     * @return the images stored in directory (only in sub-folders)
     */
    public static ArrayList<ImageFile> getImageFilesInSubDirectory(File directory) {
        ArrayList<ImageFile> filteredImages = getImageFilesByDirectory(directory);
        LinkedList<ImageFile> imageFiles = manager.getAll();
        for (ImageFile imageFile : imageFiles) {
            if (imageFile.getDirectory().equals(directory)) {
                filteredImages.remove(imageFile);
            }
        }
        return filteredImages;
    }

    /**
     * Get images stored in directory (only in parent folder)
     * <p>Assumes stored images and directory all use absolute paths</p>
     *
     * @param directory the directory that contains returned images
     * @return the images stored in directory (only in parent folder)
     */
    public static ArrayList<ImageFile> getImageFilesInParentDirectory(File directory) {
        ArrayList<ImageFile> filteredImages = getImageFilesByDirectory(directory);

        filteredImages.removeAll(getImageFilesInSubDirectory(directory));

        return filteredImages;
    }

    /**
     * Add a new image
     *
     * @param imageInsert the image which will be added
     */
    private static void addImage(ImageFile imageInsert) {
        boolean added = manager.add(imageInsert);
        if (added) {
            LinkedList<String> tags = new LinkedList<>(imageInsert.getTags());
            for (String tag : tags) {
                TagManager.add(tag);
            }
        }
    }

    /**
     * Return an array of image files in a directory
     *
     * @param directory the path of the folder to search in.
     * @return File[] array of all imageFiles in a directory.
     */
    private static ArrayList<File> findImages(String directory) {

        File folder = new File(directory);
        File[] collect;

        // return a list of all image files.
        collect =
                folder.listFiles(
                        (dir, name) -> (name.endsWith(".png")
                                | name.endsWith(".jpg")
                                | name.endsWith(".jpeg")
                                | // Considering file of the appropriate type
                                name.endsWith(".tiff")
                                | name.endsWith(".ppm")
                                | name.endsWith(".jfif")));

        if (collect == null) { // no files of the right format were found
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(collect));
    }

    /**
     * Get the names of all sub-directories in a directory.
     *
     * @param directory the path of the folder to search in.
     * @return The array list of names of sub-directories.
     */
    private static ArrayList<String> checkForSubDirectory(String directory) {

        File folder = new File(directory);
        ArrayList<String> gatherSubDirectories = new ArrayList<>();

        File[] allFiles = folder.listFiles();

        if (allFiles == null) {
            return gatherSubDirectories;
        } else {
            for (File f : allFiles) {
                if (f.isDirectory()) {
                    gatherSubDirectories.add(f.getName());
                }
            }
        }

        return gatherSubDirectories;
    }

    /**
     * Check for sub-directories in directory, while adding imageFiles.
     *
     * @param directory the path of the folder to search in.
     * @return arrayList of all imageFiles(including those found in sub-directories).
     */
    private static ArrayList<File> checkSubDirectories(String directory) {

        // if no sub-directory, return findImages
        if (checkForSubDirectory(directory).isEmpty()) {
            return findImages(directory);
        }
        // else return findImages + findImages(NEW_PATH)
        else {
            ArrayList<File> allImages = new ArrayList<>();

            // loop through the names-list generated
            for (String dirName : checkForSubDirectory(directory)) {

                // keep recurring through names list - and adding to the temp array
                //https://stackoverflow.com/questions/19762169/forward-slash-or-backslash Nov 29 2017
                allImages.addAll(checkSubDirectories(directory + System.getProperty("file.separator") + dirName));
            }

            ArrayList<File> gather = findImages(directory);
            gather.addAll(allImages);
            return gather;
        }
    }

    /**
     * Convert and store an array of File objects as imageFiles.
     *
     * @param possibleImages List of imageFiles in a directory that will be converted in to
     *                       ManageImage.ImageFile objects.
     */
    private static void convertToImageObjects(ArrayList<File> possibleImages) {

        // ArrayList<ManageImage.ImageFile> imageFiles = new ArrayList<>();
        for (File f : possibleImages) {
            try {
                addImage(new ImageFile(f));

            } catch (IOException e) {
                Main.logger.log(Level.SEVERE, "ImageManager serialized file incorrectly read", e);
            }
        }
    }

    /**
     * Add all imageFiles from a specified directory into the list of imageFiles that a user has seen.
     *
     * @param directory The path of the folder the imageFiles are to be found
     */
    public static void createImagesFromDirectory(String directory) {

        // get the relevant File objects from directory & sub-directory
        ArrayList<File> files = checkSubDirectories(directory);
        // convert the files into an array-List of ManageImage.ImageFile objects.
        convertToImageObjects(files);
    }

    /**
     * Delete a tag from TagManager.tags and all ImageFiles containing that tag.
     *
     * @param tag The tag to delete from TagManager.tags and ImageFiles all containing that tag.
     */
    public static void deleteGlobalTag(String tag) {
        ArrayList<ImageFile> list = getDisplayedFiles(PicGridView.getShowAll(), PicGridView.getDirectory());

        for (ImageFile file : list)
            file.removeTag(tag);

    }

    /**
     * Returns the displayed ImageFiles in PicGridView
     *
     * @return ArrayList<ImageFile> the displayed ImageFiles in PicGridView
     */
    private static ArrayList<ImageFile> getDisplayedFiles(boolean showAll, File directory) {
        ArrayList<ImageFile> list = getImageFilesByDirectory(directory);
        if (!showAll)
            list.removeAll(getImageFilesInSubDirectory(directory));

        return list;
    }
}
