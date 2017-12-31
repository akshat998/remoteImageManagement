package ManageImage;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

/**
 * BlackAndWhiteFilter represents a black and white filter
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class BlackAndWhiteFilter implements FilterStrategy {

    /**
     * Apply a filter onto image view
     *
     * @param imageView the image view
     */
    @Override
    public void applyFilter(ImageView imageView) {
        // Adapted from: https://stackoverflow.com/questions/43068319/how-to-create-javafx-16-bit-greyscale-images Date: Nov 21, 207

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        imageView.setEffect(colorAdjust);

    }
}
