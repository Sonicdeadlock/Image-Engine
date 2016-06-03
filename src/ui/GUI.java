package ui;/**
 * Created by alexthomas on 11/2/15.
 */

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        AsciiRenderScene ars = new AsciiRenderScene(pane);
        ars.init(pane);
        primaryStage.setScene(ars);
        primaryStage.show();
    }
}
