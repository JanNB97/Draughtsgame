import GUI.StartScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setResizable(false);

        StartScreenController startScreenController = new StartScreenController(stage);
        startScreenController.show();
    }
}
