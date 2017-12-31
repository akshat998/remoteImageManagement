package GuiController;

import ManageImage.*;
import GuiView.ImageSceneView;
import GuiView.Main;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Manage all interactions of the user with an image
 * <p>
 * Actions include: <br>
 * - choosing a filter for an image <br>
 * - moving an image <br>
 * - adding/deleting tags <br>
 * - viewing directory of the image <br>
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class ImageSceneController {

    /**
     * A reference to the interface viewed by a user
     */
    private static FlowPane flowLayout;

    /**
     * The image that a user has decided to view
     */
    private static ImageFile image;

    /**
     * A collection of tags to be added the image
     */
    private static ArrayList<String> tagsToAdd;

    /**
     * A collection of tags to be deleted from image
     */
    private static ArrayList<String> tagsToDelete;

    /**
     * The hue of the customFilter
     */
    private static Slider hue;

    /**
     * The contrast of the customFilter
     */
    private static Slider contrast;

    /**
     * The brightness of the customFilter
     */
    private static Slider brightness;

    /**
     * The saturation of the customFilter
     */
    private static Slider saturation;


    /**
     * image in form of a viewable icon
     */
    private static ImageView icon;


    /**
     * setter for the custom image filter (hue setting)
     *
     * @param hue Slide setting selected by the user for image's hue
     */
    public static void setHue(Slider hue) {
        ImageSceneController.hue = hue;
    }

    /**
     * setter for the custom image filter (contrast setting)
     *
     * @param contrast Slide setting selected by the user for image's contrast
     */
    public static void setContrast(Slider contrast) {
        ImageSceneController.contrast = contrast;
    }

    /**
     * setter for the custom image filter (brightness setting)
     *
     * @param brightness Slide setting selected by the user for image's brightness
     */
    public static void setBrightness(Slider brightness) {
        ImageSceneController.brightness = brightness;
    }

    /**
     * setter for the custom image filter (saturation setting)
     *
     * @param saturation Slide setting selected by the user for image's saturation
     */
    public static void setSaturation(Slider saturation) {
        ImageSceneController.saturation = saturation;
    }

    /**
     * Setter for the an image's icon reference
     *
     * @param icon An icon reference of the image
     */
    public static void setIcon(ImageView icon) {
        ImageSceneController.icon = icon;
    }


    /**
     * Setter for the tags to be deleted from an image
     *
     * @param tagsToDelete collection of tags which need to be deleted from an image.
     */
    public static void setTagsToDelete(ArrayList<String> tagsToDelete) {
        ImageSceneController.tagsToDelete = tagsToDelete;
    }

    /**
     * Setter for the tags to be added to an image
     *
     * @param tagsToAdd collection of tags which need to be added to an image.
     */
    public static void setTagsToAdd(ArrayList<String> tagsToAdd) {
        ImageSceneController.tagsToAdd = tagsToAdd;
    }


    /**
     * Setter for the image environment opened by a user.
     *
     * @param flowLayout The user's current image environment
     */
    public static void setFlowLayout(FlowPane flowLayout) {
        ImageSceneController.flowLayout = flowLayout;
    }

    /**
     * Setter for the image file opened by a user.
     *
     * @param image The image that has been opened by a user.
     */
    public static void setImage(ImageFile image) {
        ImageSceneController.image = image;
    }

    /**
     * Check if the string entered by the user on the GuiController is a valid tag
     *
     * @param newTag field which contains the new tag imageNewName
     * @return true if valid tag imageNewName.
     */
    private static boolean checkValidTagName(String newTag) {
        return newTag != null && newTag.length() != 0;
    }

    /**
     * Create a generic Alert using the information provided.
     *
     * @param title   String: window title
     * @param header  String
     * @param content String
     */
    private static void createAlert(String title, String header, String content) {

        // taken from http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Update the log with its new information.
     */
    public static void updateLog(TextArea log) {

        Log imageLog = image.getLog();
        StringBuilder logs = new StringBuilder();

        // add all logs as a line
        for (Entry e : imageLog) {

            // https://stackoverflow.com/questions/15977295/control-for-displaying-multiline-text
            logs.append(e.toString()).append(System.lineSeparator());
        }

        // represent log as textArea
        log.setText(logs.toString());
    }

    /**
     * Update the comboBox of image names and the text box name.
     */
    static public void imageNameUpdate(ComboBox<String> imageNames, TextField name) {

        name.setText(image.getName());
        if (!(imageNames.getItems() == null)) {
            imageNames.getItems().clear();
        }

        for (String n : image.getPriorNames()) {
            imageNames.getItems().add(n);
        }
        if (!imageNames.getItems().isEmpty()) {
            Collections.reverse(imageNames.getItems());
            imageNames.getSelectionModel().selectFirst();
        }
    }


    /**
     * Add all tags image has into a clickable section of the scene.
     *
     * @return FlowPane
     */
    public static FlowPane addClickableTags() {

        flowLayout.getChildren().clear();
        //https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        LinkedList<String> tags = TagManager.getTags();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            checkBoxes.add(checkBox);
            if (image.getTags().contains(tag))
                checkBox.setSelected(true);

            checkBox.setOnAction(e -> {
                String text = checkBox.getText();
                if (checkBox.isSelected()) {
                    tagsToAdd.add(text);
                    tagsToDelete.remove(text);
                } else {
                    tagsToDelete.add(text);
                    tagsToAdd.remove(text);
                }
            });
        }
        flowLayout.getChildren().addAll(checkBoxes);
        return flowLayout;
    }


    /**
     * Adding the ability to revert to an older name on the GuiController
     *
     * @param revertName Button that initiates action
     */
    public static void revertOldTagName(Button revertName, ComboBox<String> imageNames, TextArea log, TextField name) {
        revertName.setOnAction(
                event -> {
                    if (!imageNames.getSelectionModel().isEmpty()
                            && !image.nameWithTags().equals(imageNames.getValue())) {

                        ArrayList<String> allNames = new ArrayList<>(imageNames.getItems());

                        //Cite: https://stackoverflow.com/questions/14987971/added-elements-in-arraylist-in-the-reverse-order-in-java
                        Collections.reverse(allNames);

                        boolean success = image.revertName(allNames.indexOf(imageNames.getValue()));
                        if (!success) {
                            createAlert("Revert Error", "The revert is invalid",
                                    "The file name " + imageNames.getValue() + " must be available");
                        }
                        updateLog(log);
                        addClickableTags();
                        imageNameUpdate(imageNames, name);
                    }
                });
    }


    /**
     * cite : https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
     * rename the image to the name collected from the relevant texBox(on hitting enter)
     */
    private static void renameImageFile(TextField name, TextArea log, ComboBox<String> imageNames) {

        boolean success = image.rename(name.getText());
        ImageSceneController.addClickableTags();
        ImageSceneController.updateLog(log);
        ImageSceneController.imageNameUpdate(imageNames, name);

        if (!success) {

            ImageSceneController.createAlert("Invalid Name", "The name you entered is invalid.",
                    "A name should not contain ' @' and the name " + name.getText() + " must be available");
        }
    }


    /**
     * Change the image to the new-name decided by the user.
     *
     * @param imageNewName the container of the new name(user event is recorded with an enter)
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had.
     */
    public static void changeToNewName(TextField imageNewName, TextArea log, ComboBox<String> imageNames) {
        imageNewName.setOnKeyPressed(
                k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        ImageSceneController.renameImageFile(imageNewName, log, imageNames);
                    }
                });
    }

    /**
     * Change the image to the new-name decided by the user.
     *
     * @param rename       The button which records that a user has decided to change image name
     * @param imageNewName the container of the new name(user event is recorded a button click)
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had.
     */
    public static void renameButtonClick(Button rename, TextField imageNewName, ComboBox<String> imageNames, TextArea log) {
        rename.setOnAction(
                e -> ImageSceneController.renameImageFile(imageNewName, log, imageNames));
    }

    /**
     * Open the a new directory of images.
     *
     * @param openNewDir The button which records that a user wants to view the a new directory of images.
     */
    public static void openImageDirectory(Button openNewDir, boolean parent) {
        openNewDir.setOnAction(
                e -> {
                    try {
                        File directory = image.getDirectory();
                        if (parent)
                            directory = image.getDirectory().getParentFile();

                        if (directory == null) {
                            createAlert("Parent folder does not exist", "Error - open parent folder", "Cannot open a folder that does not exits, opening current directory");
                            directory = image.getDirectory();
                        }
                        // https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/ Date: Nov 29 2017
                        String OS = System.getProperty("os.name").toLowerCase();

                        // This is for the Teaching Lab computers
                        if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
                            // https://bb-2017-09.teach.cs.toronto.edu/t/cdf-computers-not-allowing-javafx-to-open-files/1786/4 Date: Nov 29 2017
                            Runtime.getRuntime().exec("xdg-open " + directory);
                        } else {
                            // https://stackoverflow.com/questions/15875295/open-a-folder-in-explorer-using-java Date: Nov 29 2017
                            Desktop.getDesktop().open(directory);
                        }

                    } catch (IOException ex) {
                        ImageSceneController.createAlert("Open directory error", "Execution failed",
                                "The execution is not supported on this computer");
                        Main.logger.log(Level.SEVERE, "Can't open directory", ex);
                    }
                });
    }

    /**
     * Add a new tag to an image
     *
     * @param addTag       Button which adds the new tag to the image
     * @param newTag       Name of the new tah
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had.
     * @param imageNewName The new name of the image, including the newly added tag
     */
    public static void addTag(Button addTag, TextField newTag, TextArea log, ComboBox<String> imageNames, TextField imageNewName) {
        addTag.setOnAction(
                e -> {
                    if (checkValidTagName(newTag.getText())) {
                        boolean success = image.addTag(newTag.getText());
                        if (success) {
                            newTag.setText("");
                        } else {
                            ImageSceneController.createAlert("Add Tag Error", "The tag '" + newTag.getText() + "' was not added successfully",
                                    "Tag imageNewName contains ' @', the tag already exists, or the file imageNewName with the additional tag is occupied");
                        }
                        ImageSceneController.addClickableTags();
                        ImageSceneController.updateLog(log);
                        ImageSceneController.imageNameUpdate(imageNames, imageNewName);
                    }
                });
    }

    /**
     * Records the tag changes made by a user based on the check-box selections.
     *
     * @param updateTags   Button which records all the tag changes for an image
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had
     * @param imageNewName The new name of the image, reflecting all changes made to the tags
     */
    public static void updateTags(Button updateTags, TextArea log, ComboBox<String> imageNames, TextField imageNewName) {
        updateTags.setOnAction(e -> {
            image.addTag(tagsToAdd);
            image.removeTag(tagsToDelete);

            ImageSceneController.addClickableTags();
            ImageSceneController.updateLog(log);
            ImageSceneController.imageNameUpdate(imageNames, imageNewName);

            tagsToAdd.clear();
            tagsToDelete.clear();
        });
    }

    /**
     * Moves the ImageFile up one directory or down into sub-directories (has choice if there are more than one sub-directory)
     *
     * @param moveBtn       the Button to set the action to
     * @param up            if the user chose to move the file up one directory
     * @param directoryText the Text display of the directory of the image
     */
    public static void moveFile(Button moveBtn, boolean up, Text directoryText) throws IOException {
        moveBtn.setOnAction(e -> {
            try {
                if (up) { // If user chose to move up one directory
                    move(directoryText, image.getDirectory().getParentFile());
                } else { // If user chose to move down to subdirectories
                    ArrayList<String> subDirectories = new ArrayList<>();
                    listAllSubDirectories(image.getDirectory().toString(), subDirectories);

                    if (subDirectories.size() > 1) {

                        // Adapted from: http://code.makery.ch/blog/javafx-dialogs-official/ Date: Nov 29 2017
                        ChoiceDialog<String> dialog = new ChoiceDialog<>(subDirectories.get(0), subDirectories);
                        dialog.setTitle("Choose a subdirectory");
                        dialog.setHeaderText("Look, a choice!");
                        dialog.setContentText("Choose your directory:");

                        Optional<String> result = dialog.showAndWait();
                        //https://stackoverflow.com/questions/19762169/forward-slash-or-backslash Nov 29 2017
                        result.ifPresent(choice -> {
                            try {
                                move(directoryText, new File(choice));
                            } catch (IOException e1) {
                                Main.logger.warning("Cannot move file");
                            }
                        });
                    } else if (subDirectories.size() == 1) { // If there is only one subdirectory then ChoiceDialog is not needed
                        move(directoryText, new File(subDirectories.get(0)));
                    } else
                        createAlert("Error - moving file", "Error!", "Cannot move file - target folder does not exist.");

                }

            } catch (IOException e1) {
                Main.logger.warning("Cannot move file");
            }
        });
    }

    /**
     * Moves the ImageFile to the indicated directory
     *
     * @param directoryText the Text display of the directory of the image
     * @param directory     the directory to move the image to
     */
    private static void move(Text directoryText, File directory) throws IOException {
        if (directory == null)
            createAlert("Error - moving file", "Error!", "Cannot move file - target folder does not exist.");
        else {
            boolean success = image.move(directory);
            directoryText.setText(image.getDirectory().toString());
            ImageSceneController.updateLog(ImageSceneView.getLog());
            if (!success) {
                createAlert("Error - moving file", "Error!", "Cannot move file - file with same name in targeted folder.");
            }
        }
    }

    /**
     * Collects all sub-directories under the directory indicated by directoryName, stores the names of the sub-directories in files.
     *
     * @param directoryName the name of the directory to search
     * @param files         the ArrayList to store the sub-Directory names in
     */
    // Cite: https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders Date: Nov 30 2017
    private static void listAllSubDirectories(String directoryName, ArrayList<String> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] listFiles = directory.listFiles();
        if (listFiles != null && listFiles.length != 0)
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    files.add(file.toString());
                    listAllSubDirectories(file.getAbsolutePath(), files);
                }
            }
    }

    /**
     * Add ability to view image using custom image filter.
     * <p>
     * Images can be transformed based on : brightness, contrast, saturation, hue
     */
    public static void customImageFilter() {
        contrast.valueProperty().addListener(e -> image.applyFilter(icon, brightness, contrast, hue, saturation));

        saturation.valueProperty().addListener(e -> image.applyFilter(icon, brightness, contrast, hue, saturation));

        brightness.valueProperty().addListener(e -> image.applyFilter(icon, brightness, contrast, hue, saturation));

        hue.valueProperty().addListener(e -> image.applyFilter(icon, brightness, contrast, hue, saturation));

    }

}
