package eu.iamgio.froxty;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
        scene.getStylesheets().add("/style.css");
        Font.loadFont(getClass().getResourceAsStream("/font/Karla-Regular.ttf"), 16);

        root.getStyleClass().add("root");
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        FrostyEffect effect = setupEffect(root);
        setupSliders(root, effect);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FroXty demo");
        primaryStage.show();
    }

    private FrostyEffect setupEffect(Pane root) {
        Label label = new Label("Welcome to\nFroXty");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);

        Pane target = new Pane(label);
        target.getStyleClass().add("container");
        target.setCursor(Cursor.MOVE);

        FrostyEffect effect = new FrostyEffect(1);

        target.prefWidthProperty().bind(root.prefWidthProperty().divide(2));
        target.prefHeightProperty().bind(root.prefHeightProperty().divide(4));

        label.prefWidthProperty().bind(target.prefWidthProperty());
        label.prefHeightProperty().bind(target.prefHeightProperty());

        FrostyBox box = new FrostyBox(effect, target);
        box.setBorderRadius(32);
        makeBoxDraggable(box);

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

    private void makeBoxDraggable(FrostyBox box) {
        class Delta {
            public double x, y;
        }
        Delta delta = new Delta();
        box.setOnMousePressed(e -> {
            delta.x = box.getLayoutX() - e.getSceneX();
            delta.y = box.getLayoutY() - e.getSceneY();
        });
        box.setOnMouseDragged(e -> {
            box.setLayoutX(e.getSceneX() + delta.x);
            box.setLayoutY(e.getSceneY() + delta.y);
        });
    }
}
