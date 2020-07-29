package eu.iamgio.froxty;

import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This {@link Pane} contains the target of the {@link FrostyEffect} and the blurred background
 * @author Giorgio Garofalo
 */
public class FrostyBox extends Pane {

    private final Node node;

    private final ImageView bgImage = new ImageView();

    /**
     * Instantiates a container with frosty backdrop effect
     * @param effect frosty effect instance
     * @param node target node
     */
    public FrostyBox(FrostyEffect effect, Node node) {
        this.node = node;
        getStyleClass().add("frosty-box");
        node.getStyleClass().add("target");

        getChildren().add(node);

        // Bind blur amount to opacityProperty
        Pane background = new Pane(bgImage);
        GaussianBlur blur = new GaussianBlur();
        blur.radiusProperty().bind(effect.opacityProperty().multiply(100));
        background.setEffect(blur);

        getChildren().add(0, background);

        PauseTransition pause = new PauseTransition(Duration.millis(effect.getUpdateTime()));
        pause.setOnFinished(e -> {
            // Set screenshot as background of the box and blur it
            update();
            pause.playFromStart();
        });
        pause.playFromStart();
    }

    private Image screenshot() {
        setVisible(false);

        Bounds bounds = localToParent(node.getBoundsInLocal());
        Scene scene = node.getScene();

        Image snapshot = node.getScene().getRoot().snapshot(null, null);
        try {
            Image cropped = new WritableImage(snapshot.getPixelReader(),
                    properValue(bounds.getMinX(), scene.getWidth()),
                    properValue(bounds.getMinY(), scene.getHeight()),
                    properValue(bounds.getWidth(), scene.getWidth() - bounds.getMinX()),
                    properValue(bounds.getHeight(), scene.getHeight() - bounds.getMinY()));

            setVisible(true);
            return cropped;
        } catch(IllegalArgumentException e) {
            // If either width or height are 0
            return null;
        }
    }

    private int properValue(double value, double max) {
        if(value < 0) return 0;
        if(max < 0) return 0;
        return (int) Math.min(value, max);
    }

    private void update() {
        try {
            bgImage.setImage(screenshot());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
