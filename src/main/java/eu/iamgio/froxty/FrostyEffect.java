package eu.iamgio.froxty;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;

/**
 * This class handles FroXty's frosty/translucent effect
 * @author Giorgio Garofalo
 */
@SuppressWarnings("unused")
public class FrostyEffect {

    /**
     * Effect opacity
     */
    private final SimpleDoubleProperty opacityProperty = new SimpleDoubleProperty(0.5);

    /**
     * Target node
     */
    private Node target;

    private FrostyBox box;

    /**
     * Instantiates a new frosty effect with base opacity 0.50
     */
    public FrostyEffect() {}

    /**
     * Instantiates a new frosty effect
     * @param opacity effect opacity
     */
    public FrostyEffect(double opacity) {
        this();
        setOpacity(opacity);
    }

    /**
     * Opacity of the effect. The more opaque, the more blurry the content looks
     */
    public SimpleDoubleProperty opacityProperty() {
        return opacityProperty;
    }

    /**
     * @return Effect opacity
     */
    public double getOpacity() {
        return opacityProperty.get();
    }

    /**
     * Sets the opacity of the effect
     * @param opacity new effect opacity
     */
    public void setOpacity(double opacity) {
        this.opacityProperty.set(opacity);
    }

    /**
     * @return The node the effect is applied on
     */
    public Node getTarget() {
        return target;
    }

    /**
     * @return Container which contains original node and frosty copy
     */
    public FrostyBox getBox() {
        return box;
    }

    /**
     * Applies the effect to a specific node
     * @param target target node
     */
    public void apply(Node target) {
        this.target = target;
        this.box = new FrostyBox(this);
    }
}
