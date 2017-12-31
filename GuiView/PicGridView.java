package GuiView;

import GuiController.*;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ManageImage.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Shows a list of thumbnails of all images under the directory.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class PicGridView {

    /**
     * The main stage to show the user
     */
    private static Stage currentStg;

    /**
     * Parent directory that user chose
     */
    private static File dir;

    /**
     * True if user wants to see all images under the chosen directory (includes sub-folders)
     */
    public static boolean showAll = true;

    /**
     * Stores the buttons with image which leads to ImageSceneView on click
     */
    private ArrayList<Button> imageButtons = new ArrayList<>();

    /**
     * Stores the buttons with image in sub-folders which leads to ImageSceneView on click
     */
    private ArrayList<Button> subDirImageButtons = new ArrayList<>();

    PicGridView(Stage currentStg) {

        PicGridView.currentStg = currentStg;

    }

    /**
     * Refreshes the PicGrid with the indicated directory.
     *
     * @param dir the the root directory the user is viewing
     */
    void initialize(File dir) {

        PicGridView.dir = dir;
        imageButtons = gatherImages(ImageManager.getImageFilesInParentDirectory(dir));
        subDirImageButtons = gatherImages(ImageManager.getImageFilesInSubDirectory(dir));
        imageButtons.addAll(subDirImageButtons);

    }

    /**
     * Return a view of all images form a directory, onto the GuiController
     *
     * @param files the imageFiles that will be displayed as buttons
     * @return ArrayList an array list of all clickable image buttons
     */
    private static ArrayList<Button> gatherImages(ArrayList<ImageFile> files) {
        ArrayList<Button> buttons = new ArrayList<>();

        for (ImageFile img : files) {

            // needs the "file://" because image will not understand it is a directory
            // solution found at https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
            Image image = new Image("file:///" + img.getFile().toString(), 200, 200, true, true, true);
            ImageView view = new ImageView(image);

            // Source:
            // https://stackoverflow.com/questions/18911186/how-do-setcache-and-cachehint-work-together-in-javafx (Date: Nov 9, 2017)
            view.setCache(true);
            view.setCacheHint(CacheHint.SPEED);

            Button viewImage = new Button(img.getName(), view);
            viewImage.setContentDisplay(ContentDisplay.TOP);

            // Source:
            // https://stackoverflow.com/questions/18911186/how-do-setcache-and-cachehint-work-together-in-javafx (Date: Nov 9, 2017)
            viewImage.setCache(true);
            viewImage.setCacheHint(CacheHint.SPEED);
            PicGridController.viewImage(viewImage, img, dir);

            buttons.add(viewImage);

        }
        return buttons;
    }

    /**
     * Sets the pane and ImageFiles for GuiController in a grid like format.
     * Functionality of panes from: https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm Date:  Nov 9, 2017
     */

    void picGrid() {

        currentStg.setTitle("Image Viewer - List images");

        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(20, 20, 5, 20));
        pane.setVgap(15);
        pane.setHgap(15);
        pane.setPrefWrapLength(300);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        currentStg.setResizable(true);
        currentStg.setMaximized(true);
        scrollPane.setMinViewportWidth(currentStg.getWidth());
        scrollPane.setMinViewportHeight(currentStg.getHeight());

        Scene scene = new Scene(scrollPane);
        currentStg.setScene(scene);

        Button chooseDirectory = new Button("Select directory");
        PicGridController.chooseNewDir(chooseDirectory, currentStg);

        pane.getChildren().add(chooseDirectory);

        PicGridController.activateDirectoryFolders(subDirImageButtons, pane);

        Button tagManage = new Button("Edit Tags");
        tagManage.setOnAction(e -> PicGridController.independentTags(dir, PicGridView.getShowAll()));
        pane.getChildren().add(tagManage);

        Label currentDirectory = new Label("Parent directory: " + dir.toString());
        currentDirectory.setMinWidth(2000);
        pane.getChildren().add(currentDirectory);

        pane.getChildren().addAll(imageButtons);

        if (!showAll && subDirImageButtons.size() != 0)
            pane.getChildren().removeAll(subDirImageButtons);
    }

    /**
     * Returns true if user chose to view all images (in and under directory), false if viewing only in parent folder
     *
     * @return boolean true if user chose to view all images (in and under directory), false if viewing only in parent folder
     */
    public static boolean getShowAll() {
        return showAll;
    }

    /**
     * Returns the chosen directory by the user
     *
     * @return boolean true if user chose to view all images (in and under directory), false if viewing only in parent folder
     */
    public static File getDirectory() {
        return dir;
    }
}
