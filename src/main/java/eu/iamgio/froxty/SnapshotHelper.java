package eu.iamgio.froxty;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Class that handles image-based operations.
 * @author Giorgio Garofalo
 */
public class SnapshotHelper {

    private final SnapshotParameters parameters = new SnapshotParameters();

    SnapshotHelper() {
        this.parameters.setFill(Color.TRANSPARENT);
    }

    /**
     * Snapshots the blurred content below a {@link FrostyBox} and crops the result so that it fits the size of the box
     * @param box frosty box
     * @return cropped and blurred snapshot of the background
     */
    Image snapshot(FrostyBox box) {
        // Temporarily hide this node
        box.setVisible(false);

        // Get child position
        Node child = box.getChild();
        Bounds bounds = child.localToScene(child.getBoundsInLocal());
        Scene scene = child.getScene();

        // Temporarily blur the root
        Parent root = scene.getRoot();
        Effect oldEffect = root.getEffect();
        root.setEffect(box.getBlur());

        // Get an image of the root without the target itself
        Image backgroundSnapshot = root.snapshot(parameters, null);

        // Get an image of the target
        box.setVisible(true);
        Image childSnapshot = child.snapshot(parameters, null);

        try {
            // Crop the snapshot
            WritableImage cropped = crop(box.getBlur().getRadius(), backgroundSnapshot, scene, bounds);
            // Remove transparent values
            return subtract(box.getAntialiasingLevel() * 255, cropped, childSnapshot);
        } catch(IllegalArgumentException e) {
            // If either width or height are 0
            return null;
        } finally {
            // Apply the previous effect to the root
            root.setEffect(oldEffect);
        }
    }

    /**
     * Given two images, background and node, they get overlapped so that transparent pixels of the node get deleted from the background
     * @param antialiasingLevel anti-aliasing level in range 0-255
     * @param background background image
     * @param child box image
     * @return background image that fits the size of the box
     * @see FrostyBox#getAntialiasingLevel()
     */
    private WritableImage subtract(double antialiasingLevel, WritableImage background, Image child) {
        PixelReader childReader = child.getPixelReader();
        PixelWriter backgroundWriter = background.getPixelWriter();

        for(int y = 0; y < child.getHeight(); y++) {
            for(int x = 0; x < child.getWidth(); x++) {
                if(x < background.getWidth() && y < background.getHeight() && x < child.getWidth() && y < child.getHeight()) {
                    int argb = childReader.getArgb(x, y);
                    int alpha = (argb >> 24) & 0xFF;
                    if(alpha < antialiasingLevel) backgroundWriter.setArgb(x, y, argb);
                }
            }
        }
        return background;
    }

    /**
     * Crops the snapshot of the root to the size of the box
     * @param blurRadius radius of the gaussian blur
     * @param source snapshot of the box
     * @param scene scene of the box
     * @param bounds coordinates and size of the box
     * @return cropped image
     */
    private WritableImage crop(double blurRadius, Image source, Scene scene, Bounds bounds) {
        return new WritableImage(source.getPixelReader(),
                properValue(bounds.getMinX() + blurRadius, scene.getWidth()),
                properValue(bounds.getMinY() + blurRadius, scene.getHeight()),
                properValue(bounds.getWidth(), scene.getWidth() - bounds.getMinX()),
                properValue(bounds.getHeight(), scene.getHeight() - bounds.getMinY()));
    }

    private int properValue(double value, double max) {
        if(value < 0) return 0;
        if(max < 0) return 0;
        return (int) Math.min(value, max);
    }
}
