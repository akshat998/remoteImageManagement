package JUnit;

import ManageImage.ImageFile;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The ImageFileTest contains JUnit tests for the class ImageFile
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class ImageFileTest {

    /**
     * The imageFile
     */
    private static ImageFile imageFile;


    /**
     * The collection of tests for ImageFiles with no tags by default
     * cite
     * https://www.petrikainulainen.net/programming/testing/junit-5-tutorial-writing-nested-tests/ Date: Nov 30 2017
     */
    @Nested
    class ImagesWithNoTagsTest {

        /**
         * Recreate imageFile
         */
        @BeforeEach
        void setupEach() {

            try {
                imageFile = new ImageFile(new File("src" + System.getProperty("file.separator") +"JUnit" + System.getProperty("file.separator") +"testImages" + System.getProperty("file.separator") +"1.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        /**
         * Revert any changes to imageFile
         */
        @AfterEach
        void afterEach() {
            imageFile.rename("1");
            if (imageFile.getTags().size() > 0)
                imageFile.removeTag(imageFile.getTags());
        }

        /**
         * check if imageFile.getName works
         */
        @org.junit.jupiter.api.Test
        void getNameTest() {

            assertEquals("1", imageFile.getName());

        }

        /**
         * check if imageFile.rename works
         */
        @org.junit.jupiter.api.Test
        void rename() {

            imageFile.rename("newPic");
            assertEquals("newPic", imageFile.getName());

        }

        /**
         * check if imageFile.revertName works
         */
        @org.junit.jupiter.api.Test
        void revertName() {

            imageFile.rename("newPic");
            assertEquals("newPic", imageFile.getName());

            imageFile.revertName(0);
            assertEquals("1", imageFile.getName());


        }

        /**
         * check if imageFile.getPriorNames works
         */
        @org.junit.jupiter.api.Test
        void getPriorNames() {

            imageFile.rename("newPic");
            assertEquals("newPic", imageFile.getName());

            ArrayList<String> namesGotten = imageFile.getPriorNames();
            ArrayList<String> nameGen = new ArrayList<>(Arrays.asList("1.jpg", "newPic.jpg"));

            assertEquals(nameGen, namesGotten);


        }

        /**
         * check if imageFile.move works
         */
        @org.junit.jupiter.api.Test
        void move() {

            try {
                imageFile.move(new File("src" + System.getProperty("file.separator") +"JUnit" + System.getProperty("file.separator") +"testImages" + System.getProperty("file.separator") + "sub"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertEquals(new File("src" + System.getProperty("file.separator") +"JUnit" + System.getProperty("file.separator") +"testImages" + System.getProperty("file.separator") + "sub"), imageFile.getDirectory());

            try {
                imageFile.move(new File("src" + System.getProperty("file.separator") +"JUnit" + System.getProperty("file.separator") +"testImages" ));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /**
         * check if imageFile.addTag works for a single tag
         */
        @org.junit.jupiter.api.Test
        void addTag() {
            imageFile.addTag("Tiger");
            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.add("Tiger");
            assertEquals(expectedTags, tags);
        }

        /**
         * check if imageFile.addTag works for multiple tags
         */
        @org.junit.jupiter.api.Test
        void addMultipleTags() {
            // https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values Date: Nov 29 2017
            ArrayList<String> toAdd = new ArrayList<>(Arrays.asList("Snow", "Cool"));

            imageFile.addTag(toAdd);
            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.addAll(toAdd);
            assertEquals(expectedTags, tags);
        }

        /**
         * check if imageFile.addTag adds 0 tags for an empty string array
         */
        @org.junit.jupiter.api.Test
        void addNoTags() {
            // https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values Date: Nov 29 2017
            ArrayList<String> toAdd = new ArrayList<>();

            imageFile.addTag(toAdd);
            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            assertEquals(expectedTags, tags);
        }

        /**
         * check if imageFile.removeTag works for a single tag removal
         */
        @org.junit.jupiter.api.Test
        void removeTags() {
            imageFile.addTag("Tiger");
            imageFile.addTag("Cool");
            imageFile.removeTag("Tiger");

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.add("Cool");
            assertEquals(expectedTags, tags);
        }

        /**
         * check if imageFile.removeTag works for multiple tag removal
         */
        @org.junit.jupiter.api.Test
        void removeMultipleTag() {
            imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild", "Awesome")));
            imageFile.removeTag(new ArrayList<>(Arrays.asList("Snow", "Awesome")));

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.add("Wild");

            assertEquals(expectedTags, tags);

        }

        /**
         * check if imageFile.removeTag works for a collection of no tags
         */
        @org.junit.jupiter.api.Test
        void removeNoTag() {
            imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild", "Awesome")));
            imageFile.removeTag(new ArrayList<>());

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.addAll(Arrays.asList("Snow", "Wild", "Awesome"));

            assertEquals(expectedTags, tags);

        }

        /**
         * check if imageFile.nameWithTags works
         */
        @org.junit.jupiter.api.Test
        void nameWithTags() {
            imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild")));
            String actualName = imageFile.nameWithTags();
            String expectedName = "1 @Snow @Wild.jpg";

            assertEquals(expectedName, actualName);
        }
    }

    /**
     * A collection of tests for images with tags by default
     */
    @Nested
    class ImageWithTagsAlreadyTest {

        /**
         * Create imageFile
         */
        @BeforeEach
        void setupEach() {

            try {
                imageFile = new ImageFile(new File("src" + System.getProperty("file.separator") +"JUnit" + System.getProperty("file.separator") +"testImages" + System.getProperty("file.separator") +"baby_snake_deadpan @snake @cute.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        /**
         * Check if imageFile.getTags works
         */
        @org.junit.jupiter.api.Test
        void getTagsTest() {

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>(Arrays.asList("snake", "cute"));

            assertEquals(expectedTags, tags);

        }

        /**
         * Check if imageFile.getName works
         */
        @org.junit.jupiter.api.Test
        void getNameTest() {

            assertEquals("baby_snake_deadpan", imageFile.getName());

        }
    }


}