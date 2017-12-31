package GuiController;

import ManageImage.ImageFile;
import ManageImage.ImageManager;
import ManageImage.TagManager;
import GuiView.PicGridView;
import GuiView.SceneManager;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Manage all interactions of the user with PicGridView
 * <p>Actions include: <br>
 * - managing tags independently <br>
 * - selecting an image</p>
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class PicGridController {

    /**
     * Manage a user's interaction with tags for a particular/all images
     *
     * @param dir     directory of the image file
     * @param showAll Allows the view of all images in both parent/sub-directory
     */
    public static void independentTags(File dir, Boolean showAll) {

        Stage independentTagEditor = new Stage();

        GridPane layout = new GridPane();
        layout.setVgap(5);
        layout.setHgap(5);

        Scene scene = new Scene(layout);

        ArrayList<String> selectedTags = new ArrayList<>();
        ListView<HBox> listView = new ListView<>();

        independentTags(selectedTags, listView);
        layout.add(listView, 1, 1, 1, 1);

        // only show images currently shown on PicGrid
        ArrayList<ImageFile> images;
        if (showAll)
            images = ImageManager.getImageFilesByDirectory(dir);
        else
            images = ImageManager.getImageFilesInParentDirectory(dir);
        ArrayList<CheckBox> imageCheckBox = new ArrayList<>();
        ArrayList<ImageFile> imageSelected = new ArrayList<>();

        CheckBox selectAllImages = new CheckBox();

        // on check select all images to be modified
        selectAllImages.setOnAction(e -> {

            if (selectAllImages.isSelected()) {
                imageSelected.addAll(images);
                for (CheckBox check : imageCheckBox) {
                    check.setSelected(true);
                }
            } else {
                imageSelected.removeAll(images);
                for (CheckBox check : imageCheckBox) {
                    check.setSelected(false);
                }
            }
        });

        // create a checkbox for each image to be selected by user
        for (ImageFile image : images) {
            CheckBox checkBox = new CheckBox(image.getName());
            imageCheckBox.add(checkBox);

            checkBox.setOnAction(e -> {
                ImageFile currImage = images.get(imageCheckBox.indexOf(checkBox));

                if (checkBox.isSelected())
                    imageSelected.add(currImage);
                else
                    imageSelected.remove(currImage);

            });
        }

        ListView<CheckBox> imageView = new ListView<>();
        imageView.getItems().add(selectAllImages);
        imageView.getItems().addAll(imageCheckBox);

        layout.add(imageView, 3, 1, 1, 1);

        Button add = new Button("Add to");
        add.setTooltip(new Tooltip("Add selected tags to selected images."));
        add.setMaxWidth(Double.MAX_VALUE);

        // add all selected tags to selected images
        add.setOnAction(e -> {

            if (imageSelected.isEmpty() || selectedTags.isEmpty()) {
                createAlert(false);
            } else {
                for (ImageFile i : imageSelected) {
                    i.addTag(selectedTags);
                }
                deselect(imageCheckBox, listView);
            }

        });


        Button remove = new Button("Remove from");
        remove.setMaxWidth(Double.MAX_VALUE);
        remove.setTooltip(new Tooltip("Remove selected tags from selected images."));

        // remove selected tags from selected images
        remove.setOnAction(e -> {
            if (imageSelected.isEmpty() || selectedTags.isEmpty()) {
                createAlert(false);
            } else {
                for (ImageFile i : imageSelected) {
                    i.removeTag(selectedTags);
                }
                deselect(imageCheckBox, listView);
            }

        });

        Button deleteTags = new Button("Independently Delete Tags");
        deleteTags.setTooltip(new Tooltip("Remove selected tags permanently."));
        deleteTags.setMaxWidth(Double.MAX_VALUE);
        deleteTags.setAlignment(Pos.BASELINE_CENTER);
        // cite: https://stackoverflow.com/questions/27295505/javafx-button-with-multiple-text-lines Date: Nov 30 2017
        deleteTags.wrapTextProperty().setValue(true);

        // delete all selected tags permanently from the program
        deleteTags.setOnAction(e -> {
            if (!selectedTags.isEmpty()) {
                for (String tag : selectedTags)
                    ImageManager.deleteGlobalTag(tag);

                TagManager.getTags().removeAll(selectedTags);
                selectedTags.clear();
                independentTags(selectedTags, listView);

                deselect(imageCheckBox, listView);
            } else {
                createAlert(true);
            }


        });

        VBox buttonsBox = new VBox();
        buttonsBox.getChildren().addAll(add, remove, deleteTags);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(5);

        layout.add(buttonsBox, 2, 1, 1, 1);
        GridPane.setValignment(buttonsBox, VPos.CENTER);

        independentTagEditor.setMaximized(false);
        independentTagEditor.setScene(scene);
        independentTagEditor.initModality(Modality.APPLICATION_MODAL);
        independentTagEditor.show();


    }

    /**
     * Deselect an image while manipulation tags
     * <p>
     * Changes made to the tags will not be reflected onto the deselected image
     *
     * @param imageCheckBox Gives user a choice to reflect tag changes onto image
     * @param listView      A list of possible image choices
     */
    private static void deselect(ArrayList<CheckBox> imageCheckBox, ListView<HBox> listView) {

        for (CheckBox check : imageCheckBox)
            check.setSelected(false);

        for (HBox h : listView.getItems())
            ((CheckBox) h.getChildren().get(0)).setSelected(false);

    }

    /**
     * Refreshes independent tags listView when include new tags.
     *
     * @param selectedTags keep selected tags selected on refresh
     * @param listView     change the listView items
     */
    private static void independentTags(ArrayList<String> selectedTags, ListView<HBox> listView) {

        listView.getItems().clear();
        LinkedList<String> tags = TagManager.getTags();
        ArrayList<HBox> tagCheckBox = new ArrayList<>();

        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            HBox v = new HBox();
            v.getChildren().add(checkBox);
            tagCheckBox.add(v);

            checkBox.setOnAction(e -> {
                String currTag = checkBox.getText();

                if (checkBox.isSelected())
                    selectedTags.add(currTag);
                else
                    selectedTags.remove(currTag);

            });
        }

        // selects/deselects all tags
        CheckBox selectAllCheckBox = new CheckBox();
        selectAllCheckBox.setOnAction(e -> {

            if (selectAllCheckBox.isSelected()) {

                selectedTags.addAll(tags);

                for (HBox hBox : tagCheckBox) {

                    ((CheckBox) hBox.getChildren().get(0)).setSelected(true);

                }

            } else {

                selectedTags.removeAll(tags);
                for (HBox hBox : tagCheckBox) {

                    ((CheckBox) hBox.getChildren().get(0)).setSelected(false);

                }

            }

        });

        TextField newTag = new TextField();
        newTag.setEditable(true);
        newTag.setPrefWidth(150);
        newTag.setOnKeyPressed(k -> {

            if (k.getCode().equals(KeyCode.ENTER)) {

                TagManager.add(newTag.getText());
                independentTags(selectedTags, listView);

            }

        });

        Button add = new Button("+");
        add.setTooltip(new Tooltip("Create new tag."));
        add.setOnAction(e -> {

            if (newTag.getText() != null) {
                TagManager.add(newTag.getText());
                independentTags(selectedTags, listView);
            }

        });

        // add new tags and select all tags should be first entry for user to see
        HBox firstElement = new HBox();
        firstElement.getChildren().addAll(selectAllCheckBox, newTag, add);
        firstElement.setSpacing(5);

        listView.getItems().add(firstElement);
        listView.getItems().addAll(tagCheckBox);

    }

    /**
     * Give the user an option to view images in the parent directory or all images(including sub-directories)
     * <p>
     * If no sub-directory exists, the choice is not provided.
     *
     * @param subDirImageButtons record the user's choice of including images in the sub-directories
     * @param pane               A view consisting of images from a directory.
     */
    public static void activateDirectoryFolders(ArrayList<Button> subDirImageButtons, FlowPane pane) {
        if (subDirImageButtons.size() != 0) {
            Button show;
            if (!PicGridView.showAll)
                show = new Button("Press to show all images under this directory (includes subfolder)");
            else show = new Button("Press to show images in this directory (only parent folder)");

            show.setOnAction(
                    e -> {
                        if (PicGridView.showAll) {
                            show.setText("Press to show all images under this directory (includes subfolder)");
                            pane.getChildren().removeAll(subDirImageButtons);
                        } else {
                            show.setText("Press to show images in this directory (only parent folder)");
                            pane.getChildren().addAll(subDirImageButtons);
                        }
                        PicGridView.showAll = !PicGridView.showAll;
                    });
            pane.getChildren().add(show);
        }
    }


    /**
     * Open up an image that a user has selected.
     *
     * @param viewImage Records the user's action to view the image
     * @param img       The image file that will be opened
     * @param dir       The directory(path) of the image that will be opened
     */
    public static void viewImage(Button viewImage, ImageFile img, File dir) {
        viewImage.setOnAction(e -> SceneManager.swapToImageScene(img, dir));
    }


    /**
     * Open up a new set of images from a user selected directory path.
     *
     * @param chooseDirectory Records the user's action to view a new directory of images
     * @param currentStg      A reference to the current GUI scene.
     */
    public static void chooseNewDir(Button chooseDirectory, Stage currentStg) {
        chooseDirectory.setOnAction(e -> DirController.dirChooser(currentStg));

    }

    /**
     * Create an Alert for when no image is selected.
     *
     * @param tagOnly if the alertBox is for only not selecting tag
     */
    private static void createAlert(boolean tagOnly) {
        String image = "";
        String oneImage = "";
        if (!tagOnly) {
            image = "image/";
            oneImage = "and one image";
        }
        // taken from http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("No " + image + "tag selected");
        alert.setContentText("Please select at least one tag " + oneImage + " for this operation.");
        alert.showAndWait();
    }

}
