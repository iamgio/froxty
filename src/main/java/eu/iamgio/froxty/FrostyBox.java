package eu.iamgio.froxty;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * This single-child node contains takes a {@link FrostyEffect} and blurs the content beneath.
 * @author Giorgio Garofalo
 */
public class FrostyBox extends Parent {

    private final SimpleObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Node> child = new SimpleObjectProperty<>();

    private final SnapshotHelper helper = new SnapshotHelper();
    private final GaussianBlur blur;

    private double antialiasingLevel = 0.35;

    /**
     * Instantiates a container with frosty backdrop effect.
     * @param effect frosty effect instance
     * @param child target node
     */
    public FrostyBox(FrostyEffect effect, Node child) {
        this.child.set(child);

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
     * Anti-aliasing level defines a value in range 0-1 where alpha values should be removed.
     * The higher the value, the more precise the process is, but it might interfere with semi-transparent nodes.
     * It is recommended that you set it to the minimum opacity present in the child node.
     * Defaults to 0.35.
     * @return anti-aliasing level in range 0-1
     */
    public double getAntialiasingLevel() {
        return antialiasingLevel;
    }

    /**
     * @param antialiasingLevel anti-aliasing level to set in range 0-1
     */
    public void setAntialiasingLevel(double antialiasingLevel) {
        this.antialiasingLevel = antialiasingLevel;
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

    private void update() {
        try {
            image.set(helper.snapshot(this));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
