package ManageImage;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

/**
 * CustomFilter represents a filter with variable effects
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class CustomFilter implements FilterStrategy {

    /**
     * Apply a custom filter onto the image.
     * <p>Since no parameters are given, no effects are applied</p>
     *
     * @param imageView the image view
     */
    // Adapted from: https://stackoverflow.com/questions/43068319/how-to-create-javafx-16-bit-greyscale-images Date: Nov 21, 207
    @Override
    public void applyFilter(ImageView imageView) {

        ColorAdjust colorAdjust = new ColorAdjust();
        imageView.setEffect(colorAdjust);
    }

    /**
     * Apply a custom filter onto the image
     *
     * @param imageView  the image
     * @param brightness the brightness of the image. Max 1, min -1
     * @param contrast   the contrast of the image. Max 1, min -1
     * @param hue        the hue of the image. Max 1, min -1
     * @param saturation the saturation of the image. Max 1, min -1
     */
    void applyFilter(ImageView imageView, double brightness, double contrast, double hue, double saturation) {
        ColorAdjust colorAdjust = new ColorAdjust(hue, saturation, brightness, contrast);
        imageView.setEffect(colorAdjust);

    }
}
