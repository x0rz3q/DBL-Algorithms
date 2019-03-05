package visualizer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;

public class Visualizer extends Application {

    public static void main(String[] args) throws Exception{
        if (args.length != 0 && new File(args[0]).exists()) {
            FileInputStream is = new FileInputStream(new File(args[0]));
            System.setIn(is);
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("visualizer.fxml"));
        Parent root = loader.load();
        Controller controller = ((Controller)loader.getController());
        controller.setTitle(primaryStage);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                controller.redraw();
            }
        });

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            controller.redraw();
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            controller.redraw();
        });

        primaryStage.show();
    }
}
