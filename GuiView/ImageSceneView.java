package GuiView;

import GuiController.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

/**
 * GuiController of an individual image's information.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class ImageSceneView {

    /**
     * The image used.
     */
    private ImageFile image;

    /**
     * The actual scene which hold all the elements.
     */
    private Scene imageScene;

    /**
     * All the logs in a text box.
     */
    private static TextArea log;

    /**
     * Image's imageNewName
     */
    private TextField imageNewName;

    /**
     * The directory that the user first opened
     */
    private File directory;

    /**
     * The previous picGrid scene
     */
    private Stage prevScene;

    /**
     * All names the image has had.
     */
    private ComboBox<String> imageNames;

    /**
     * image in form of a viewable icon
     */
    private ImageView icon = new ImageView();

    /**
     * ArrayList for adding multiple tags
     */
    private ArrayList<String> tagsToAdd = new ArrayList<>();

    /**
     * ArrayList for deleting multiple tags
     */
    private ArrayList<String> tagsToDelete = new ArrayList<>();

    /**
     * Get log textBox
     *
     * @return log
     */
    public static TextArea getLog() {
        return log;
    }

    /**
     * Construct an ImageSceneView
     *
     * @param stage the previous scene
     */
    ImageSceneView(Stage stage) {

        this.prevScene = stage;

    }

    /**
     * Construct the GuiController view of an image
     *
     * @param image     particular image in a directory
     * @param directory Location of the file containing images
     * @throws IOException Case when invalid directory
     */
    void initialize(ImageFile image, File directory) throws IOException {

        this.image = image;
        log = new TextArea();

        // inspired from https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        GridPane g;
        ImageSceneController.setImage(image);
        g = gridSetup();
        imageScene = new Scene(g);
        this.directory = directory;

    }

    /**
     * * Return the scene which holds the elements.
     *
     * @return current interaction's screen
     */
    Scene getImageScene() {
        return imageScene;
    }

    /**
     * Setup of the whole screen combining all smaller elements and layouts.
     *
     * @return GridPane
     */
    private GridPane gridSetup() throws IOException {

        GridPane layout = new GridPane();
        layout.setHgap(6);
        layout.setVgap(6);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // ratio preserve solution
        // https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx
        icon.setFitWidth(720);
        icon.setFitHeight(480);
        icon.setEffect(null);
        icon.setImage(image.getImage());
        icon.setPreserveRatio(true);

        // needs the "file://" because image will not understand it is a directory
        // solution found at https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
        icon.setImage(image.getImage());
        layout.add(icon, 1, 2, 4, 2);

        // go to screen with all images
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        imageNames = new ComboBox<>();

        imageNewName = new TextField(image.getName());
        imageNewName.setEditable(true);
        ImageSceneController.changeToNewName(imageNewName, log, imageNames);
        ImageSceneController.setTagsToAdd(tagsToAdd);
        ImageSceneController.setTagsToDelete(tagsToDelete);


        Button rename = new Button("Rename");
        ImageSceneController.renameButtonClick(rename, imageNewName, imageNames, log);


        back.setOnAction(
                e -> SceneManager.swapToPicGrid(this.directory));

        // https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm

        ImageSceneController.imageNameUpdate(imageNames, imageNewName);
        imageNames.setMaxWidth(320);
        imageNames.getSelectionModel().selectFirst();

        Button revertName = new Button("Revert");
        ImageSceneController.revertOldTagName(revertName, imageNames, log, imageNewName);  // gives functionality to the Button


        VBox f = vBoxSetup();
        layout.add(f, 6, 2, 2, 2);

        // cite: https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm Nov 25, 2017
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Normal",
                        "Black and White",
                        "Invert",
                        "Custom"
                );
        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setValue("Normal");

        HBox customFilter = new HBox();

        Text hueText = new Text("Hue:");
        Slider hue = new Slider(-1, 1, 0);

        Text brightnessText = new Text("Brightness:");
        Slider brightness = new Slider(-1, 1, 0);

        Text saturationText = new Text("Saturation:");
        Slider saturation = new Slider(-1, 1, 0);

        Text contrastText = new Text("Contrast:");
        Slider contrast = new Slider(-1, 1, 0);

        ImageSceneController.setIcon(icon);
        ImageSceneController.setHue(hue);
        ImageSceneController.setBrightness(brightness);
        ImageSceneController.setContrast(contrast);
        ImageSceneController.setSaturation(saturation);

        ImageSceneController.customImageFilter();

        customFilter.getChildren().addAll(hueText, hue);
        customFilter.getChildren().addAll(brightnessText, brightness);
        customFilter.getChildren().addAll(saturationText, saturation);
        customFilter.getChildren().addAll(contrastText, contrast);

        customFilter.setVisible(false);

        //cite: https://stackoverflow.com/questions/41323945/javafx-combobox-add-listener-on-selected-item-value Nov 25, 2017
        comboBox.valueProperty().addListener((fields, oldValue, newValue) -> {
            if (newValue.equals("Custom")) {
                icon.setImage(image.getImage());
                customFilter.setVisible(true);
            } else {
                customFilter.setVisible(false);
            }
            image.setFilterStrategy(newValue);
            image.applyFilter(icon);
        });

        Button saveImageAs = new Button("Save Copy");
        saveImageAs.setOnAction(e -> {
            WritableImage writableImage = icon.snapshot(new SnapshotParameters(), null);
            File ad = image.getFile();
            try {

                // taken from: https://stackoverflow.com/questions/4386446/problem-using-imageio-write-jpg-file
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", new File(ad.getParent() + "\\(Copy) " + ad.getName()));

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

        HBox imageName = new HBox();
        imageName.getChildren().addAll(imageNewName, rename, imageNames, revertName, comboBox, saveImageAs);
        imageName.setSpacing(5.0);
        layout.add(customFilter, 1, 2, 1, 1);
        layout.add(imageName, 1, 0, 1, 1);

        layout.setPrefHeight(700);
        layout.setPrefWidth(1325);
        return layout;
    }

    /**
     * Setup the image's information (log, tags, directory) in a flowPane.
     *
     * @return FlowPane
     */
    private VBox vBoxSetup() throws IOException {

        // initial values
        VBox flow = new VBox();
        flow.setPadding(new Insets(0, 0, 5, 0));
        flow.setSpacing(10);

        // image directory
        Text dirText = new Text();
        // Retrieved from: https://stackoverflow.com/questions/12737829/javafx-textfield-resize-to-text-length Date: Nov 21, 2017
        dirText.setWrappingWidth(250);
        dirText.setText(image.getDirectory().toString());

        // button for opening the directory
        Button openImgDir = new Button();
        openImgDir.setText("Open Directory");
        ImageSceneController.openImageDirectory(openImgDir, false);

        // button for opening the parent directory
        Button openParentImgDir = new Button();
        openParentImgDir.setText("Open Parent Directory");
        ImageSceneController.openImageDirectory(openParentImgDir, true);

        // button for opening the parent directory
        Button moveUp = new Button();
        moveUp.setText("Move up one dir");
        ImageSceneController.moveFile(moveUp, true, dirText);

        // button for opening the parent directory
        Button moveDown = new Button();
        moveDown.setText("Move down dir");
        ImageSceneController.moveFile(moveDown, false, dirText);


        // button for changing the directory
        Button changeDir = new Button();
        changeDir.setText("Change Directory");
        changeDir.setOnAction(
                e -> DirView.dirChooser(prevScene, this.image, dirText));

        HBox dir = new HBox();
        dir.boundsInParentProperty();
        dir.setMaxWidth(flow.getMaxWidth());
        dir.setSpacing(8.0);

        dir.getChildren().addAll(dirText, openImgDir, openParentImgDir);
        changeDir.setAlignment(Pos.BASELINE_CENTER);

        // nested panes implemented from
        // https://stackoverflow.com/questions/33339427/javafx-have-multiple-panes-in-one-scene
        flow.getChildren().add(dir);
        flow.setAlignment(Pos.CENTER_LEFT);

        HBox moveDir = new HBox();
        moveDir.boundsInParentProperty();
        moveDir.setMaxWidth(flow.getMaxWidth());
        moveDir.setSpacing(8.0);

        moveDir.getChildren().addAll(changeDir, moveUp, moveDown);
        flow.getChildren().add(moveDir);

        //https://docs.oracle.com/javafx/2/ui_controls/text-field.htm
        TextField newTag = new TextField();

        HBox tagBox = new HBox();
        Button addTag = new Button("+");
        ImageSceneController.addTag(addTag, newTag, log, imageNames, imageNewName);
        ImageSceneController.setTagsToAdd(tagsToAdd);  // record the new tag which just got added

        tagBox.getChildren().addAll(newTag, addTag);
        tagBox.setSpacing(5.0);

        flow.getChildren().add(tagBox);

        Button updateTags = new Button("Update tags");
        ImageSceneController.updateTags(updateTags, log, imageNames, imageNewName);

        tagBox.getChildren().add(updateTags);

        FlowPane pane = new FlowPane(Orientation.VERTICAL, 7, 5);
        ImageSceneController.setFlowLayout(pane);
        pane.setPadding(new Insets(5));
        pane.setPrefHeight(480 / 2.5);
        pane = ImageSceneController.addClickableTags();


        flow.getChildren().add(new ScrollPane(pane));

        ImageSceneController.updateLog(log);

        log.setWrapText(true);
        log.setEditable(false);

        // wrap error solution found at https://stackoverflow.com/questions/29537264/javafx-flowpane-autosize
        log.setPrefHeight(480 / 2);
        flow.getChildren().add(log);

        return flow;
    }


}
