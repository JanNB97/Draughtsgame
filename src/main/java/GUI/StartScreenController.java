package GUI;

import artInt.AI;
import artInt.RandomAI;
import artInt.SimpleEvaluationAI;
import gameModel.enums.Owner;
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

import javax.swing.*;
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
        Label titel = new Label("Checkers");
        titel.setFont(new Font(25));

        nameField = new TextField("Name");
        nameField.setFont(new Font(20));
        nameField.setAlignment(Pos.CENTER);
        ComboBox<String> comboBox2 = new ComboBox<>();
        setComboBox(comboBox2);
        Label label = new Label(" VS. ");
        HBox hBox = new HBox(10, nameField, label, comboBox2);
        hBox.setAlignment(Pos.CENTER);

        Button button = new Button("START");
        button.setStyle("-fx-font-size: 30");
        button.setOnAction(t -> {
            startButtonClicked(comboBox2.getValue());
        });
        Button arenaButton = new Button("ARENA");
        arenaButton.setOnAction(t -> {
            arenaButtonClicked();
        });
        arenaButton.setFont(new Font(15));
        HBox arenaBox = new HBox(10, arenaButton);
        arenaBox.setAlignment(Pos.BOTTOM_RIGHT);
        VBox buttonVBox = new VBox(button, arenaBox);
        buttonVBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(20, titel, hBox, buttonVBox);
        vBox.setAlignment(Pos.CENTER);



        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void startButtonClicked(String s)
    {
        AI p2 = getAI(s);

        PersonGameController personGameController = new PersonGameController(stage, p2, nameField.getText());
        personGameController.show();
    }

    private void arenaButtonClicked()
    {
        ArenaGameController arenaGameController = new ArenaGameController(stage);
        arenaGameController.show();
    }

    public static AI getAI(String name)
    {
        final Owner playerNumber = Owner.NP;

        if(name.equals("RandomAI"))
        {
            return new RandomAI(playerNumber);
        }
        else if(name.equals("SimpleEvaluationAI"))
        {
            return new SimpleEvaluationAI(playerNumber);
        }
        else
        {
            Logger.getGlobal().severe("artInt not found");
            System.exit(1);
            return null;
        }
    }

    public static void setComboBox(ComboBox<String> comboBox)
    {
        File file = new File("./src/main/java/artInt");
        File[] allFiles = file.listFiles();

        comboBox.setStyle("-fx-font-size: 20");
        comboBox.setPrefWidth(300);

        for(int i = 0; i < allFiles.length; i++)
        {
            if(allFiles[i].getName().equals("AI.java") == false)
            {
                comboBox.getItems().add(cutFour(allFiles[i].getName()));
            }
        }

        comboBox.getSelectionModel().select(0);
    }

    private static String cutFour(String string)
    {
        StringBuilder builder = new StringBuilder(string);
        builder.delete(string.length() - 5, string.length());

        return builder.toString();
    }
}
