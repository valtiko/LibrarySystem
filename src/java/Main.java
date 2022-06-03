
import controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;



public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController.setPrimaryStage(primaryStage);
        SceneController.startScene("LoginScene");
        Initializer.getInstance().initialize(null, null);


    }

}
