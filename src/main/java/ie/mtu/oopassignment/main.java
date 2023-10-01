package ie.mtu.oopassignment;

import ie.mtu.oopassignment.controller.MainController;
import ie.mtu.oopassignment.controller.ModuleController;
import ie.mtu.oopassignment.controller.StudentController;
import ie.mtu.oopassignment.model.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        MainController controller = new MainController();
        StudentController studentController = new StudentController();
        ModuleController moduleController = new ModuleController();
        Database.getConnection();
        Scene scene = controller.view.createUI(controller,studentController, moduleController, stage);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } finally {
            Database.closeConnection();
        }
    }
}
