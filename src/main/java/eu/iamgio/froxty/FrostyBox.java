package eu.iamgio.froxty;

import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
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

    private final FrostyEffect effect;
    private final ImageView bgImage = new ImageView();

    FrostyBox(FrostyEffect effect) {
        this.effect = effect;
        getStyleClass().add("frosty-box");

        Node node = effect.getTarget();
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

        Bounds bounds = localToParent(effect.getTarget().getBoundsInLocal());

        Image snapshot = effect.getTarget().getScene().getRoot().snapshot(null, null);
        Image cropped = new WritableImage(snapshot.getPixelReader(),
                (int) bounds.getMinX(),
                (int) bounds.getMinY(),
                (int) bounds.getWidth(),
                (int) bounds.getHeight()
        );

        setVisible(true);
        return cropped;
    }

    private void update() {
        try {
            bgImage.setImage(screenshot());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
