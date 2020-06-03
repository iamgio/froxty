package eu.iamgio.froxty;

import javafx.beans.property.SimpleDoubleProperty;

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
     * Instantiates a new frosty effect with base opacity 0.50
     */
    public FrostyEffect() {}

    /**
     * Instantiates a new frosty effect
     * @param opacity effect opacity
     */
    public FrostyEffect(double opacity) {
        setOpacity(opacity);
    }

    /**
     * Opacity of the effect. The more opaque, the more blurry the content looks
     */
    public SimpleDoubleProperty opacityProperty() {
        return opacity;
    }

    /**
     * @return Effect opacity
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
}
