package eu.iamgio.froxty;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This node contains the target of the {@link FrostyEffect} blurs the content beneath.
 * @author Giorgio Garofalo
 */
public class FrostyBox extends Region {

    private final SimpleObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Node> child = new SimpleObjectProperty<>();

    private final GaussianBlur blur;
    private final SnapshotParameters parameters = new SnapshotParameters();

    /**
     * Instantiates a container with frosty backdrop effect.
     * @param effect frosty effect instance
     * @param child target node
     */
    public FrostyBox(FrostyEffect effect, Node child) {
        this.child.set(child);
        this.parameters.setFill(Color.TRANSPARENT);

        getStyleClass().add("frosty-box");

        // Set-up blurred background
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

        // Set-up loop
        PauseTransition pause = new PauseTransition(Duration.millis(effect.getUpdateTime()));
        pause.setOnFinished(e -> {
            if(getChild() != null && getScene() != null) {
                // Set screenshot as background of the box and blur it
                update();
            }
            pause.playFromStart();
        });
        pause.playFromStart();

        // Set-up listener to when child changes
        this.child.addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                getChildren().remove(1, 2);
            } else if(getChildren().size() < 2) {
                getChildren().add(newValue);
            } else {
                getChildren().set(1, newValue);
            }
        });
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
        return child.get();
    }

    /**
     * Sets the target of the effect.
     * @param child target
     */
    public void setChild(Node child) {
        this.child.set(child);
    }

    private Image snapshot() {
        // Temporarily hide this node
        setVisible(false);

        // Get child position
        Node child = getChild();
        Bounds bounds = localToParent(child.getLayoutBounds());
        Scene scene = child.getScene();

        // Temporarily blur the root
        Parent root = scene.getRoot();
        Effect oldEffect = root.getEffect();
        root.setEffect(blur);

        // Get an image of the root without the target itself
        Image snapshot = root.snapshot(parameters, null);
        try {
            // Crop the snapshot
            return new WritableImage(snapshot.getPixelReader(),
                    properValue(bounds.getMinX() + blur.getRadius(), scene.getWidth()),
                    properValue(bounds.getMinY() + blur.getRadius(), scene.getHeight()),
                    properValue(bounds.getWidth(), scene.getWidth() - bounds.getMinX()),
                    properValue(bounds.getHeight(), scene.getHeight() - bounds.getMinY()));
        } catch(IllegalArgumentException e) {
            // If either width or height are 0
            return null;
        } finally {
            // Make this node visible again
            setVisible(true);
            // Apply the previous effect to the root
            root.setEffect(oldEffect);
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
