package GuiController;

import GuiView.DirView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Records all interactions of the user with the directory selector.
 * <p>
 * Interactions include: <br>
 * - Selecting a directory of images
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class DirController {

    /**
     * Lets user to choose a directory and then shows PicGridView.
     *
     * @param currentStage the Stage that the user is in
     */
    public static void dirChooser(Stage currentStage) {
        DirView.guiSetup(currentStage, false);
    }

    /**
     * Check if the user has decided to open a directory by clicking on a button
     *
     * @param dirChooserButton Button that user clicks for choosing a directory
     * @param dirTextField     Displays the path of the selected directory
     * @param error            Error message in case an improper directory is provided
     * @param currentStage     Representation in which all actions will be taking place
     */
    public static void openDirectoryChooser(Button dirChooserButton, TextField dirTextField, Text error, Stage currentStage) {
        dirChooserButton.setOnAction(
                e -> {
                    // Adapted from: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.html Date: Nov 5, 2017
                    DirectoryChooser dirChooser = new DirectoryChooser();
                    File directory = dirChooser.showDialog(currentStage);
                    if (directory != null) {
                        dirTextField.setText(directory.toString());
                        error.setVisible(false);
                    }
                });
    }


}
