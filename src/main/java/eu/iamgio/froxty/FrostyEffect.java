package eu.iamgio.froxty;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * This class handles FroXty's frosty/translucent effect
 * @author Giorgio Garofalo
 */
public class FrostyEffect {

    /**
     * Effect opacity
     */
    private final SimpleDoubleProperty opacity = new SimpleDoubleProperty(0.5);

    /**
     * Time (in ms) required to update
     */
    private int updateTime = 10;

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
        this(opacity);
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
}
