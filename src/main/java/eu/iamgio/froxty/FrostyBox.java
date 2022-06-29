package eu.iamgio.froxty;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * This single-child node contains takes a {@link FrostyEffect} and blurs the content beneath.
 * @author Giorgio Garofalo
 */
public class FrostyBox extends Parent {

    private final SimpleObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Node> child = new SimpleObjectProperty<>();
    private final SimpleDoubleProperty borderRadius = new SimpleDoubleProperty();

    private final SnapshotHelper helper = new SnapshotHelper();
    private final GaussianBlur blur;

    /**
     * Instantiates a container with frosty backdrop effect.
     * @param effect frosty effect instance
     * @param child target node
     */
    public FrostyBox(FrostyEffect effect, Node child) {
        this.child.set(child);

        getStyleClass().add("frosty-box");

        // Set-up blurred background
        ImageView background = new ImageView();
        getChildren().add(background);

        image.addListener((observable, old, img) -> {
            if(img != null) {
                background.setFitWidth(img.getWidth());
                background.setFitHeight(img.getHeight());
                background.setImage(img);
                updateClip(background, img.getWidth(), img.getHeight());
            }
        });

        // Bind blur amount to opacityProperty
        blur = new GaussianBlur();
        blur.radiusProperty().bind(effect.opacityProperty().multiply(100));

        if(child != null) getChildren().add(child);

        initLoop(effect.getUpdateTime());
        initChildListener();
    }

    /**
     * Instantiates a container with frosty backdrop effect and no child.
     * @param effect frosty effect instance
     */
    public FrostyBox(FrostyEffect effect) {
        this(effect, null);
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

    /**
     * @return The border radius of this box.
     */
    public double getBorderRadius() {
        return borderRadius.doubleValue();
    }

    /**
     * @return The border radius of this box.
     */
    public SimpleDoubleProperty borderRadiusProperty() {
        return borderRadius;
    }

    /**
     * Sets the border radius of this box.
     * @param borderRadius new border radius
     */
    public void setBorderRadius(double borderRadius) {
        this.borderRadius.set(borderRadius);
    }

    /**
     * @return JavaFX blur effect
     */
    GaussianBlur getBlur() {
        return blur;
    }

    /**
     * Starts the loop that continuously snapshots and blurs the background
     * @param updateTime time in millis between tasks
     */
    private void initLoop(double updateTime) {
        // Set-up loop
        PauseTransition loop = new PauseTransition(Duration.millis(updateTime));
        loop.setOnFinished(e -> {
            if(getChild() != null && getScene() != null) {
                // Set screenshot as background of the box and blur it
                update();
            }
            loop.playFromStart();
        });
        loop.playFromStart();

        // Set-up listener to check whether the box is in the scene.
        // If not, the loop is paused.
        sceneProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                loop.stop();
            } else {
                loop.playFromStart();
            }
        });
    }

    /**
     * Sets-up a listener for this box's child
     */
    private void initChildListener() {
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
     * Updates the clip mask of this box, which allows to round borders
     * @param background background view
     * @param width mask width
     * @param height mask height
     */
    private void updateClip(Node background, double width, double height) {
        Rectangle clip;
        if(background.getClip() instanceof Rectangle) {
            clip = (Rectangle) background.getClip();
        } else {
            clip = new Rectangle();
            background.setClip(clip);
        }
        clip.setWidth(width);
        clip.setHeight(height);

        double radius = borderRadius.doubleValue();
        clip.setArcWidth(radius);
        clip.setArcHeight(radius);
    }

    private void update() {
        try {
            image.set(helper.snapshot(this));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
