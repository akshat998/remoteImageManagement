package JUnit;

import ManageImage.ImageFile;
import ManageImage.ImageManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The ImageManagerTest contains JUnit tests for the class ImageManager
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class ImageManagerTest {

    /**
     * Load in test images from directory
     */
    @BeforeAll
    static void setup() {
        ImageManager.createImagesFromDirectory("src/JUnit/testImages");
    }

    /**
     * Check if ImageManager returns imageFiles from subdirectory
     */
    @Test
    void getImageFilesInSubDirectoryTest() {

        ArrayList<ImageFile> filesInSubDirectory = ImageManager.getImageFilesInSubDirectory(new File("src/JUnit/testImages"));
        ArrayList<ImageFile> expectedFiles = new ArrayList<>();

        try {
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/sub/pokemon.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedFiles, filesInSubDirectory);

    }

    /**
     * Check if imageManager returns imageFiles from parent directory
     */
    @Test
    void getImageFilesInParentDirectoryTest() {

        ArrayList<ImageFile> filesInParentDirectory = ImageManager.getImageFilesInParentDirectory(new File("src/JUnit/testImages"));
        ArrayList<ImageFile> expectedFiles = new ArrayList<>();

        try {
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/1.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/1copy.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/3.jpeg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/3copy.jpeg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/4.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/4copy.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/baby_snake_deadpan @snake @cute.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/filterJUnitTest.jpg")));


        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedFiles, filesInParentDirectory);
    }

    /**
     * Check if imageManager creates imageFiles from directory
     */
    @Test
    void createImagesFromDirectoryTest() {


        ArrayList<ImageFile> filesInDirectory = ImageManager.getImageFilesByDirectory(new File("src/JUnit/testImages"));
        ArrayList<ImageFile> expectedFiles = new ArrayList<>();

        try {
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/1.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/1copy.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/3.jpeg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/3copy.jpeg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/4.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/4copy.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/baby_snake_deadpan @snake @cute.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/filterJUnitTest.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/sub/pokemon.jpg")));


        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedFiles, filesInDirectory);


    }

}