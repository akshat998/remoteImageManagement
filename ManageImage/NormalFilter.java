package ManageImage;

import javafx.scene.image.ImageView;

/**
 * NormalFilter represents a filter with no effects
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class NormalFilter implements FilterStrategy {
    /**
     * Remove filters from the image view
     *
     * @param imageView the image view
     */
    @Override
    public void applyFilter(ImageView imageView) {
        imageView.setEffect(null);
    }
}
