package ManageImage;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

/**
 * NormalFilter represents a filter with inverted colours
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class InvertColoursFilter implements FilterStrategy {

    /**
     * Apply a filter which inverts the original colours of an image
     * <p>adapted from http://java-buddy.blogspot.ca/2012/12/javafx-example-copy-image-pixel-by.html</p>
     *
     * @param imageView view of an image before an filter implementation
     */
    @Override
    public void applyFilter(ImageView imageView) {

        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color.invert());

            }
        }

        imageView.setImage(writableImage);


    }
}
