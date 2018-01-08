package GUI;

import artInt.AI;
import artInt.RandomAI;
import artInt.RekursiveAI;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Logger;

public class StartScreenController
{
    private Stage stage;

    private TextField nameField;

    public StartScreenController(Stage stage)
    {
        this.stage = stage;
    }

    public void show()
    {
        nameField = new TextField("Name");
        nameField.setFont(new Font(30));
        nameField.setAlignment(Pos.CENTER);

        ComboBox<String> comboBox1 = new ComboBox<>();
        ComboBox<String> comboBox2 = new ComboBox<>();
        comboBox1.getItems().add("PERSON");
        setComboBox(comboBox1);
        setComboBox(comboBox2);

        Label label = new Label(" VS. ");
        HBox hBox = new HBox(10, comboBox1, label, comboBox2);
        hBox.setAlignment(Pos.CENTER);

        Button button = new Button("START");
        button.setStyle("-fx-font-size: 30");
        VBox vBox = new VBox(20, nameField, hBox, button);
        vBox.setAlignment(Pos.CENTER);

        button.setOnAction(t -> {
            startButtonClicked(comboBox1.getValue(), comboBox2.getValue());
        });

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void startButtonClicked(String s1, String s2)
    {
        if(s1 == null || s2 == null)
        {
            return;
        }
        else if(s1.equals("PERSON"))
        {
            AI p2 = getAI(s2);

            PersonGameController personGameController = new PersonGameController(stage, p2, nameField.getText());
            personGameController.show();
        }
        else
        {
            //TODO
            Logger.getGlobal().severe("Arena is not yet implemented");
        }
    }

    private AI getAI(String name)
    {
        if(name.equals("RandomAI"))
        {
            return new RandomAI();
        }
        else if(name.equals("RekursiveAI"))
        {
            return new RekursiveAI();
        }
        else
        {
            Logger.getGlobal().severe("artInt not found");
            System.exit(1);
            return null;
        }
    }

    private void setComboBox(ComboBox<String> comboBox)
    {
        File file = new File("./src/main/java/artInt");
        File[] allFiles = file.listFiles();

        comboBox.setStyle("-fx-font-size: 20");
        comboBox.setPrefWidth(200);

        for(int i = 0; i < allFiles.length; i++)
        {
            if(allFiles[i].getName().equals("AI.java") == false)
            {
                comboBox.getItems().add(cutFour(allFiles[i].getName()));
            }
        }

        comboBox.getSelectionModel().select(0);
    }

    private String cutFour(String string)
    {
        StringBuilder builder = new StringBuilder(string);
        builder.delete(string.length() - 5, string.length());

        return builder.toString();
    }
}
