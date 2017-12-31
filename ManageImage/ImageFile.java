package ManageImage;

import GuiView.Main;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * An ImageFile represents an image File.
 * <p>An ImageFile represents an image, manages tags and logs changes</p>
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class ImageFile implements Serializable {

    /**
     * The File object of the image
     */
    private File imageFile;

    /**
     * The list of image tags
     */
    private ArrayList<String> tags;

    /**
     * The name without tags
     */
    private String name;

    /**
     * The history of the image
     */
    private Log log;

    /**
     * Extension of image
     */
    private String extension;

    /**
     * Directory of image
     */
    private File directory;

    /**
     * Prior names and tags of the image
     */
    private List<String> priorNames;

    /**
     * FilterStrategy for  applying filter to images
     */
    private static FilterStrategy strategy;

    /**
     * Indicates if the picture is currently inverted
     */
    private boolean inverted;

    /**
     * Construct an image representing File imageFile
     * <p>Precondition: imageFile is a valid image.</p>
     *
     * @param imageFile The File object of the image
     */
    public ImageFile(File imageFile) throws IOException {
        if (!imageFile.isFile()) {
            Main.logger.log(Level.SEVERE, "Invalid file path", new InvalidFileException("Invalid File"));
        } else if (ImageIO.read(imageFile) == null) {
            Main.logger.log(
                    Level.SEVERE, "Directory not being read", new InvalidFileException("Invalid File"));
        } else {
            this.imageFile = imageFile;
            tags = new ArrayList<>();
            priorNames = new ArrayList<>();
            log = new Log();

            setUpFromImageFile();
            //log.addEntry(new Entry("Set initial name"));
            priorNames.add(nameWithTags());
        }


    }

    /**
     * Sets up instance variables from imageFile
     */
    private void setUpFromImageFile() {
        List<String> tags = splitTags(imageFile.getName());
        this.tags = new ArrayList<>();
        this.tags.addAll(tags);
        directory = imageFile.getParentFile();
    }

    /**
     * Take the name and remove all tags.
     *
     * @param rawName The name stored on computer. IE: 'Test @tag.png'
     * @return List of tags
     */
    private List<String> splitTags(String rawName) {

        int indexExtension = rawName.lastIndexOf(".");
        extension = rawName.substring(indexExtension);
        rawName = rawName.substring(0, indexExtension);
        String[] nameParts = rawName.split(" @");

        name = nameParts[0];

        return Arrays.asList(nameParts).subList(1, nameParts.length);
    }

    /**
     * Get the tags of this image
     * <p>The returned list is a cloned copy. Changes to list will not mutate this image</p>
     *
     * @return The tags of this image
     */
    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Get the log of this image
     * <p>The returned ManageImage.Log is mutable and will affect ManageImage.ImageFile.
     *
     * @return The Log representing change history of this image
     */
    public Log getLog() {
        return log;
    }

    /**
     * Get the name, without tag information, of this image
     *
     * @return the name of this image
     */
    public String getName() {
        return name;
    }

    /**
     * Get the directory of this image
     * <p>The returned directory is a cloned File.</p>
     *
     * @return the directory of this image
     */
    public File getDirectory() {
        return new File(directory.toString());
    }

    /**
     * Get the file this image represents
     * <p>The returned file is a cloned copy. Changes, especially changes that mutate the actual file,
     * may affect if this image's file is valid.</p>
     *
     * @return the file this image represents
     */
    public File getFile() {
        return new File(imageFile.toString());
    }

    /**
     * Renames this ManageImage.ImageFile and the File this ManageImage.ImageFile represents
     * <p>Precondition: newName does not contain " @"</p>
     *
     * @param newName The ManageImage.ImageFile's new name
     * @return indicates a successful rename
     */
    public boolean rename(String newName) {
        if (newName.contains("@")) {
            Main.logger.log(
                    Level.SEVERE,
                    "Name inappropriately contains \" @\"",
                    new InvalidNameException("Name contains \\\" @\\"));
            return false;
        } else if (newName.equals(this.name)) {
            return false;
        } else {
            String oldName = this.name;
            this.name = newName;

            boolean success = updateFile("Image \"" + oldName + "\" was renamed to \"" + newName + "\"");
            if (success) {
                priorNames.remove(nameWithTags());
                priorNames.add(nameWithTags());
            }
            return success;
        }
    }

    /**
     * Reverts the name back to a previous name.
     * <p>entryNumber = 0 represents revert to oldest name. The larger the entryNumber, the more recent the name</p>
     *
     * @param entryNumber the position of entry to revert to
     * @return indicate if the revert was successful
     */
    public boolean revertName(int entryNumber) {

        List<String> priorTags = splitTags(priorNames.get(entryNumber));

        tags.clear();
        tags.addAll(priorTags);

        for (String tag : priorTags)
            if (!TagManager.getTags().contains(tag))
                TagManager.add(tag);

        boolean success = updateFile("Reverted " + getName() + " to prior tags: " + nameWithTags());
        if (success) {
            priorNames.add(priorNames.remove(entryNumber));
        }
        return success;
    }

    /**
     * Get a list of previous names
     *
     * @return the list of previous names
     */
    public ArrayList<String> getPriorNames() {

        return new ArrayList<>(priorNames);
    }

    /**
     * Moves this ManageImage.ImageFile to newDirectory
     * <p>Precondition: newDirectory is a valid directory</p>
     *
     * @param newDirectory the directory that will store the image
     * @return indicate if the move was successful
     */
    public boolean move(File newDirectory) throws IOException {
        boolean success;
        if (!newDirectory.isDirectory()) {
            success = false;
            Main.logger.log(
                    Level.SEVERE,
                    "Proper file path need to be selected",
                    new InvalidFileException("Invalid directory"));
        } else {
            directory = newDirectory;
            success = updateFile("Moved image \"" + name + "\" to \"" + newDirectory.toString() + "\"");
        }
        return success;
    }

    /**
     * Adds tag to image
     * <p>Precondition: tag does not contain " @". Images cannot have duplicate tags</p>
     *
     * @param tag to add
     * @return Indicates if the tag insertion was successful
     */
    public boolean addTag(String tag) {
        boolean success = false;
        if (tag.contains("@")) {
            Main.logger.log(
                    Level.SEVERE, "Invalid tag name", new InvalidNameException("Tag contains \" @\""));
        } else if (!tags.contains(tag)) {
            tags.add(tag);
            success = updateFile("Added tag \"" + tag + "\" to image \"" + name + "\"");
            if (success) {
                priorNames.remove(nameWithTags());
                priorNames.add(nameWithTags());
            }
            TagManager.add(tag);
        }
        return success;
    }

    /**
     * Adds tag to image
     * <p>Precondition: tag does not contain " @". Images cannot have duplicate tags</p>
     *
     * @param tagList to add
     */
    public void addTag(ArrayList<String> tagList) {
        boolean success;
        if (tagList.size() != 0) {
            for (String tag : tagList) {
                if (tag.contains(" @")) {
                    Main.logger.log(
                            Level.SEVERE, "Invalid tag name", new InvalidNameException("Tag contains \" @\""));
                } else if (!tags.contains(tag)) {
                    tags.add(tag);

                    if (!TagManager.getTags().contains(tag)) TagManager.add(tag);
                }
            }

            success = updateFile("Added tags \"" + tagList.toString() + "\" to image \"" + name + "\"");
            if (success) {
                priorNames.remove(nameWithTags());
                priorNames.add(nameWithTags());
            }
        }
    }

    /**
     * Removes tag from image
     *
     * @param tag tag to remove
     */
    public void removeTag(String tag) {
        boolean success;
        if (tag.contains(tag)) {
            tags.remove(tag);
            // TagManager.remove(tag);
            success = updateFile("Removed tag \"" + tag + "\" from image \"" + name + "\"");
            if (success) {
                priorNames.remove(nameWithTags());
                priorNames.add(nameWithTags());
            }
        }
    }

    /**
     * Removes tag from image
     *
     * @param tagList tag to remove
     */
    public void removeTag(ArrayList<String> tagList) {
        boolean success;
        if (tagList.size() != 0) {
            for (String tag : tagList) {
                if (tag.contains(tag)) {
                    tags.remove(tag);
                    // TagManager.remove(tag);

                }
            }

            success = updateFile("Removed tags \"" + tagList.toString() + "\" from image \"" + name + "\"");
            if (success) {
                priorNames.remove(nameWithTags());
                priorNames.add(nameWithTags());
            }
        }
    }

    /**
     * Update image file using current image properties and record log with logMessage
     * <p>If the update fails, the imageFile restores properties to the current image File</p>
     *
     * @return Indicates if the update was successful
     */
    private boolean updateFile(String logMessage) {
        String oldName = imageFile.getName();
        File newImageFile = createLocation();
        boolean success = false;
        if (!newImageFile.exists()) {
            success = imageFile.renameTo(newImageFile);
        }
        String newName = newImageFile.getName();
        if (success) {
            imageFile = newImageFile;
            log.addEntry(
                    new Entry(
                            logMessage
                                    + ":"
                                    + System.lineSeparator()
                                    + oldName
                                    + " -> "
                                    + newName
                                    + System.lineSeparator()));
        } else {
            setUpFromImageFile();
            Main.logger.log(
                    Level.SEVERE, "File unsuccessfully renamed", new UnsuccessfulRenameException());
        }
        return success;
    }

    /**
     * Create File from imageFile's properties
     *
     * @return File representation of this image
     */
    private File createLocation() {

        return new File(directory, nameWithTags());
    }

    /**
     * Get the name of the image including tags and extension
     *
     * @return the full name of the image
     */
    public String nameWithTags() {

        StringBuilder fileName = new StringBuilder();
        fileName.append(name);
        for (String tag : tags) {
            fileName.append(" @");
            fileName.append(tag);
        }
        fileName.append(extension);

        return fileName.toString();
    }

    /**
     * this ImageFile and object are equal they represent the same image file
     *
     * @param object the object to compare to
     * @return indicate if this ImageObject is equal to object
     */
    @Override
    public boolean equals(Object object) {
        return object instanceof ImageFile
                && ((ImageFile) object).getFile().equals(imageFile)
                && ((ImageFile) object).getDirectory().equals(this.getDirectory());
    }

    /**
     * Returns the Image of this ImageFile, creates a new Image every time so filter does not reference the original image.
     *
     * @return the Image that the GUI uses to display the image.
     */
    public Image getImage() {

        // needs the "file://" because image will not understand it is a directory
        // solution found at https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
        return new Image("file:///" + this.getFile().toString());

    }

    /**
     * Sets the FilterStrategy
     *
     * @param chosenStrategy the FilterStrategy that is being used, could be different kinds of filter.
     */
    public void setFilterStrategy(String chosenStrategy) {
        switch (chosenStrategy) {
            case "Normal":
                strategy = new NormalFilter();
                break;
            case "Black and White":
                strategy = new BlackAndWhiteFilter();
                break;
            case "Invert":
                strategy = new InvertColoursFilter();
                break;
            case "Custom":
                strategy = new CustomFilter();
                break;
        }
    }

    /**
     * Returns the chosen directory by the user
     *
     * @param imageView the imageView to apply the filter
     */
    public void applyFilter(ImageView imageView) {
        if (inverted) {
            imageView.setImage(this.getImage());
        }

        if (strategy instanceof InvertColoursFilter) {
            inverted = true;
        }
        strategy.applyFilter(imageView);

    }

    /**
     * Modifies the imageView to apply CustomFilter
     *
     * @param imageView  the imageView to apply the CustomFilter to
     * @param brightness the brightness slider
     * @param contrast   the contrast slider
     * @param hue        the hue slider
     * @param saturation the saturation slider
     **/
    public void applyFilter(ImageView imageView, Slider brightness, Slider contrast, Slider hue, Slider saturation) {
        if (inverted) {
            imageView.setImage(this.getImage());
        }
        double brightnessVal = brightness.getValue();
        double contrastVal = contrast.getValue();
        double hueVal = hue.getValue();
        double saturationVal = saturation.getValue();
        ((CustomFilter) strategy).applyFilter(imageView, brightnessVal, contrastVal, hueVal, saturationVal);


    }
}

/**
 * ManageImage.InvalidFileException represents the exception where the File type does not correspond
 * with the expected File type
 */
class InvalidFileException extends RuntimeException {
    InvalidFileException(String message) {
        super(message);
    }
}

/**
 * ManageImage.InvalidNameException represents the exception where a tag or the image is given an
 * invalid name
 */
class InvalidNameException extends RuntimeException {
    InvalidNameException(String message) {
        super(message);
    }
}

/**
 * ManageImage.UnsuccessfulRenameException represents the exception where the image is renamed
 * unsuccessfully
 */
class UnsuccessfulRenameException extends RuntimeException {
    UnsuccessfulRenameException() {
        super();
    }
}
