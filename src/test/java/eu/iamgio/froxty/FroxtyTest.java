package eu.iamgio.froxty;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * @author Giorgio Garofalo
 */
public class FroxtyTest extends Application {

    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("/teststyle.css");
        Font.loadFont(getClass().getResourceAsStream("/font/Karla-Regular.ttf"), 16);

        root.getStyleClass().add("root");
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        FrostyEffect effect = setupEffect(root);
        setupSliders(root, effect);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FroXty test");
        primaryStage.show();
    }

    private FrostyEffect setupEffect(Pane root) {
        Label label = new Label("Welcome to\nFroXty");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);

        Pane target = new Pane(label);

        FrostyEffect effect = new FrostyEffect();

        target.prefWidthProperty().bind(root.prefWidthProperty().divide(2));
        target.prefHeightProperty().bind(root.prefHeightProperty().divide(4));

        label.prefWidthProperty().bind(target.prefWidthProperty());
        label.prefHeightProperty().bind(target.prefHeightProperty());

        effect.apply(target);
        FrostyBox box = effect.getBox();

        box.translateXProperty()
                .bind(root.prefWidthProperty().divide(2).subtract(target.prefWidthProperty().divide(2)));
        box.translateYProperty()
                .bind(root.prefHeightProperty().divide(2).subtract(target.prefHeightProperty().divide(2)));

        root.getChildren().add(box);
        return effect;
    }

    private void setupSliders(Pane root, FrostyEffect effect) {
        Slider opacitySlider = new Slider(0, 1, .5);
        effect.opacityProperty().bind(opacitySlider.valueProperty());

        root.getChildren().add(new VBox(opacitySlider));
    }
}
