package GuiView;

import ManageImage.ImageFile;
import GuiController.DirController;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * The SceneManager manages the currently displayed scene
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class SceneManager {

    /**
     * The main stage.
     */
    private static Stage stage;

    /**
     * PicGrid object.
     */
    private static PicGridView picGrid;

    /**
     * ImageView object.
     */
    private static ImageSceneView imageScene;

    /**
     * Create a manager, to control changes in Scenes.
     *
     * @param stage main Stage
     */
    SceneManager(Stage stage) {

        SceneManager.stage = stage;
        picGrid = new PicGridView(stage);
        imageScene = new ImageSceneView(stage);

    }

    /**
     * Swap to the PicGrid.
     *
     * @param dir File
     */
    static void swapToPicGrid(File dir) {

        stage.setMaximized(true);
        stage.setWidth(1325);
        stage.setHeight(750);

        picGrid.initialize(dir);
        picGrid.picGrid();

    }

    /**
     * Swap to the ImageScene.
     *
     * @param image ImageFile
     * @param dir   File
     */
    public static void swapToImageScene(ImageFile image, File dir) {

        stage.setMaximized(true);
        stage.setWidth(1720);
        stage.setHeight(750);

        try {

            imageScene.initialize(image, dir);

        } catch (IOException e) {

            e.printStackTrace();

        }

        stage.setScene(imageScene.getImageScene());

    }

    /**
     * Brings up the directory chooser. Lets user choose a directory.
     */
    void dirChooser() {

        DirController.dirChooser(stage);

    }

}
