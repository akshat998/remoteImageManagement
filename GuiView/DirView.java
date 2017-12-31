package GuiView;

import GuiController.*;
import ManageImage.ImageFile;
import ManageImage.ImageManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


/**
 * A stage that pops up when dirChooser is called.
 * There are two usages, pass in only a Stage to select a directory to open. It will then lead to PicGridView for a list of ImageFiles to show.
 * Pass in a Stage, an ImageFile, and a Text to select a directory for the ImageFile to transfer to. It will also update the Text to show
 * the new directory.
 * Functionality of panes from: https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm Date: Nov 6, 2017
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class DirView {
    /**
     * ImageFile for when changing the directory
     */
    private static ImageFile image;

    /**
     * Text that displays the directory of the ImageFile
     */
    private static Text directoryText;

    /**
     * Chosen directory by the user
     */
    private static File directory;


    /**
     * Show an alert if an image was unsuccessfully moved.
     * <p>
     * Common cause: similar image already exists in the same directory.
     */
    private static void imageMovingFailed() {
        // taken from http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Move Directory Error");
        alert.setHeaderText("Failed to move this image to " + directory.toString());
        alert.setContentText("The destination directory likely already contains another image named " + image.nameWithTags());
        alert.showAndWait();
    }

    /**
     * Lets user to choose a directory and then moves the ImageFile to chosen directory.
     * <p>
     * Acts as an initiation for the GUI.
     *
     * @param currentStage the Stage that the user is in
     * @param imageToMove  the ImageFile to move
     * @param dirText      the Text to display the directory of the ImageFile
     */
    static void dirChooser(Stage currentStage, ImageFile imageToMove, Text dirText) {
        guiSetup(currentStage, true);
        image = imageToMove;
        directoryText = dirText;
    }


    /**
     * Sets the stage except for the goBtn.
     *
     * @param file         True if a file was given when dirChooser is called
     * @param currentStage the Stage that the user is in
     */
    public static void guiSetup(Stage currentStage, boolean file) {
        Stage chooser = new Stage();

        chooser.setTitle("Image Viewer - Select Directory");

        GridPane pane = new GridPane();
        Scene scene = new Scene(pane, 500, 250);

        Button dirChooserBtn = new Button();
        dirChooserBtn.setText("...");

        Button goBtn = new Button();
        goBtn.setText("Go");

        TextField dirTextField = new TextField();
        dirTextField.setPrefWidth(350);
        dirTextField.setText("Input Directory");
        GridPane.setHalignment(dirTextField, HPos.LEFT);

        Text instruction = new Text();
        instruction.setText("Please select/type in a directory:");

        Text error = new Text();
        error.setText("Please select a working directory.");
        error.setFill(Color.RED);
        GridPane.setHalignment(error, HPos.RIGHT);
        error.setVisible(false);

        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.add(error, 4, 8);
        pane.add(dirTextField, 4, 7);
        pane.add(dirChooserBtn, 5, 7);
        pane.add(instruction, 4, 6);
        pane.add(goBtn, 5, 8);

        DirController.openDirectoryChooser(dirChooserBtn, dirTextField, error, currentStage);

        btmMethod(goBtn, file, currentStage, chooser, error, dirTextField);

        chooser.setMaximized(false);
        chooser.setScene(scene);
        //Cite: https://www.programcreek.com/java-api-examples/index.php?class=javafx.stage.Stage&method=showAndWait Date: Nov 5, 2017
        chooser.initModality(Modality.APPLICATION_MODAL);
        chooser.show();
    }

    /**
     * Sets the 'Go' button method according to if an ImageFile was passed when dirChooser is called.
     * If True, ImageFile will be moved to the selected directory.
     * If False, PicGridView will be shown.
     *
     * @param button       the 'Go' button
     * @param file         True if a file was given when dirChooser is called
     * @param currentStage the Stage that the user is in
     * @param chooser      the Stage of the directory chooser
     * @param error        the error message to be shown if user chose an invalid directory
     * @param dirTextField the TextField for showing the chosen directory
     */
    private static void btmMethod(Button button, boolean file, Stage currentStage, Stage chooser, Text error, TextField dirTextField) {
        button.setOnAction(
                e -> {
                    directory = new File(dirTextField.getText());
                    // If there is a file to move
                    if (file) {
                        if (directory.isDirectory()) {

                            // Move image to indicated directory
                            try {
                                boolean success = image.move(directory);
                                directoryText.setText(image.getDirectory().toString());
                                ImageSceneController.updateLog(ImageSceneView.getLog());

                                if (!success) {
                                    DirView.imageMovingFailed();
                                }

                            } catch (IOException e1) {
                                Main.logger.log(Level.SEVERE, "new directory path not found.", e);
                            }
                            chooser.hide();
                        } else {
                            error.setVisible(true);
                        }
                    } else {
                        // No file to move, meaning choosing a directory to open PicGrid
                        if (directory.isDirectory()) {
                            ImageManager.createImagesFromDirectory(directory.toString());

                            SceneManager.swapToPicGrid(directory);

                            currentStage.show();
                            chooser.hide();
                        } else {
                            error.setVisible(true);
                        }
                    }
                });
    }

}
