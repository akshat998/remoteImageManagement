package GuiView;

import ManageImage.ImageManager;
import ManageImage.TagManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Where the program run from.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class Main extends Application {

    /**
     * The logger
     * Citation for all logger related code, adapted from: https://www.loggly.com/ultimate-guide/java-logging-basics/ Date: Nov 16, 2017
     */
    public static Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Start the program
     *
     * @param args default arguments
     */
    public static void main(String[] args) {

        LogManager.getLogManager().reset();

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.SEVERE);
        logger.addHandler(handler);
        logger.setLevel(Level.OFF);

        launch(args);
    }

    /**
     * Opens DirController, loads prior ImageManager.imageFiles and TagManager.tags.
     *
     * @param currentStage the stage user is in.
     */
    @Override
    public void start(Stage currentStage) {
        ImageManager.load();
        TagManager.load();
        SceneManager sM = new SceneManager(currentStage);
        sM.dirChooser();
    }

    /**
     * Saves  ImageManager.imageFiles and TagManager.tags to files when program closes.
     */
    @Override
    public void stop() {
        ImageManager.save();
        TagManager.save();
    }
}
