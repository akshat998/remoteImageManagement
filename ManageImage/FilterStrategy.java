package ManageImage;

import javafx.scene.image.*;

// Adapted from: https://dzone.com/articles/design-patterns-strategy Date: Nov 21, 207

/**
 * An interface class which applies filters onto an image.
 */
public interface FilterStrategy {

    /**
     * Apply filter onto an image.
     *
     * @param image an image before any filter
     */
    void applyFilter(ImageView image);
}

