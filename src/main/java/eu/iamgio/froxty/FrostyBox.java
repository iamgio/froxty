package eu.iamgio.froxty;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * This {@link Pane} contains the target of the {@link FrostyEffect} and the blurred background.
 * @author Giorgio Garofalo
 */
public class FrostyBox extends Pane {

    private final GaussianBlur blur;
    private final SimpleObjectProperty<Image> image = new SimpleObjectProperty<>();

    private Node child;

    /**
     * Instantiates a container with frosty backdrop effect.
     * @param effect frosty effect instance
     * @param child target node
     */
    public FrostyBox(FrostyEffect effect, Node child) {
        this.child = child;

        getStyleClass().add("frosty-box");

        Region background = new Region();
        getChildren().add(background);
        image.addListener((observable, old, img) -> {
            if(img != null) {
                background.setPrefSize(img.getWidth(), img.getHeight());
                background.setBackground(new Background(new BackgroundImage(img, null, null, null, null)));
            }
        });

        // Bind blur amount to opacityProperty
        blur = new GaussianBlur();
        blur.radiusProperty().bind(effect.opacityProperty().multiply(100));

        if(child != null) getChildren().add(child);

        PauseTransition pause = new PauseTransition(Duration.millis(effect.getUpdateTime()));
        pause.setOnFinished(e -> {
            if(this.child != null && getScene() != null) {
                // Set screenshot as background of the box and blur it
                update();
            }
            pause.playFromStart();
        });
        pause.playFromStart();
    }

    /**
     * Instantiates a container with default frosty effect.
     */
    public FrostyBox() {
        this(new FrostyEffect(), null);
    }

    /**
     * @return Target of the effect
     */
    public Node getChild() {
        return child;
    }

    /**
     * Sets the target of the effect.
     * @param child target
     */
    public void setChild(Node child) {
        this.child = child;
        if(child == null) return;
        if(getChildren().size() < 2) {
            getChildren().add(child);
        } else {
            getChildren().set(1, child);
        }
    }

    private Image snapshot() {
        setVisible(false);

        Bounds bounds = localToParent(child.getLayoutBounds());
        Scene scene = child.getScene();

        Parent root = scene.getRoot();
        Effect eff = root.getEffect();
        root.setEffect(blur);

        Image snapshot = child.getScene().getRoot().snapshot(null, null);
        try {
            Image cropped = new WritableImage(snapshot.getPixelReader(),
                    properValue(bounds.getMinX() + blur.getRadius(), scene.getWidth()),
                    properValue(bounds.getMinY() + blur.getRadius(), scene.getHeight()),
                    properValue(bounds.getWidth(), scene.getWidth() - bounds.getMinX()),
                    properValue(bounds.getHeight(), scene.getHeight() - bounds.getMinY()));

            setVisible(true);

            return cropped;
        } catch(IllegalArgumentException e) {
            // If either width or height are 0
            return null;
        } finally {
            root.setEffect(eff);
        }
    }

    private int properValue(double value, double max) {
        if(value < 0) return 0;
        if(max < 0) return 0;
        return (int) Math.min(value, max);
    }

    private void update() {
        try {
            image.set(snapshot());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
