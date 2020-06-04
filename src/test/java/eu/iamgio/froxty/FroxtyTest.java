package eu.iamgio.froxty;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Giorgio Garofalo
 */
public class FroxtyTest extends Application {

    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("/teststyle.css");

        root.getStyleClass().add("root");
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        setupEffect(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FroXty test");
        primaryStage.show();
    }

    private void setupEffect(Pane root) {
        Pane pane = new Pane();

        FrostyEffect effect = new FrostyEffect();

        pane.prefWidthProperty().bind(root.prefWidthProperty().divide(2));
        pane.prefHeightProperty().bind(root.prefHeightProperty().divide(4));

        effect.apply(pane);
        FrostyBox box = effect.getBox();

        box.translateXProperty()
                .bind(root.prefWidthProperty().divide(2).subtract(pane.prefWidthProperty().divide(2)));
        box.translateYProperty()
                .bind(root.prefHeightProperty().divide(2).subtract(pane.prefHeightProperty().divide(2)));

        root.getChildren().add(box);
    }
}
