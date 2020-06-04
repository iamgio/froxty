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
    private final SimpleDoubleProperty opacity = new SimpleDoubleProperty(0.5);

    /**
     * Time required to update
     */
    private int updateTime = 40;

    /**
     * Target node
     */
    private Node target;

    /**
     * Container which contains original node and frosty copy
     */
    private FrostyBox box;

    /**
     * Instantiates a new frosty effect with base opacity 0.50
     */
    public FrostyEffect() {}

    /**
     * Instantiates a new frosty effect with base opacity 0.50 and custom update time
     * @param updateTime time required to update the effect
     */
    public FrostyEffect(int updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Instantiates a new frosty effect
     * @param opacity effect opacity
     */
    public FrostyEffect(double opacity) {
        setOpacity(opacity);
    }

    /**
     * Instantiates a new frosty effect
     * @param opacity effect opacity
     * @param updateTime time required to update the effect
     */
    public FrostyEffect(double opacity, int updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Opacity of the effect. The more opaque, the more blurry the content looks
     */
    public SimpleDoubleProperty opacityProperty() {
        return opacity;
    }

    /**
     * @return Opacity of the effect. The more opaque, the more blurry the content looks
     */
    public double getOpacity() {
        return opacity.get();
    }

    /**
     * Sets the opacity of the effect
     * @param opacity new effect opacity
     */
    public void setOpacity(double opacity) {
        this.opacity.set(opacity);
    }

    /**
     * @return Time (millis) between updates. Default value is 40
     */
    public int getUpdateTime() {
        return updateTime;
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
        target.getStyleClass().add("target");
        this.target = target;
        this.box = new FrostyBox(this);
    }
}
