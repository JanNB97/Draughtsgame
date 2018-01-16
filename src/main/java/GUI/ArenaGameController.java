package GUI;

import artInt.AI;
import artInt.RandomAI;
import artInt.SimpleEvaluationAI;
import gameModel.Game;
import gameModel.Move;
import gameModel.MyGame;
import gameModel.enums.Owner;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.util.logging.Logger;

public class ArenaGameController
{
    private Stage stage;
    private AI battlePlayer1, battlePlayer2;

    private Label winLabel;
    private ProgressBar winLooseBar;
    private TextArea resultArea;

    public ArenaGameController(Stage stage, AI battlePlayer1, AI battlePlayer2)
    {
        this.stage = stage;
        this.battlePlayer1 = battlePlayer1;
        this.battlePlayer2 = battlePlayer2;
    }

    public void show()
    {
        Label headline = new Label("Battle Arena");
        headline.setFont(new Font(40));

        ComboBox<String> comboBoxP1 = new ComboBox<>();
        StartScreenController.setComboBox(comboBoxP1);
        comboBoxP1.setStyle(comboBoxP1.getStyle() + ";-fx-background-color: #0099FF");
        Label vsLabel = new Label("VS");
        vsLabel.setFont(new Font(20));
        ComboBox<String> comboBoxP2 = new ComboBox<>();
        StartScreenController.setComboBox(comboBoxP2);
        comboBoxP2.setStyle(comboBoxP2.getStyle() + ";-fx-background-color:  red");
        HBox hBoxPlayers = new HBox(15, comboBoxP1, vsLabel, comboBoxP2);
        hBoxPlayers.setAlignment(Pos.CENTER);

        winLabel = new Label("-");
        winLabel.setFont(new Font(30));

        winLooseBar = new ProgressBar();
        winLooseBar.setPrefSize(700, 40);
        winLooseBar.setStyle("-fx-control-inner-background: red");

        TextField noOfGamesField = new TextField("100");
        noOfGamesField.setAlignment(Pos.CENTER);
        noOfGamesField.setFont(new Font(20));
        noOfGamesField.setPrefWidth(80);
        Button battleButton = new Button("BATTLE!");
        battleButton.setFont(new Font(20));
        battleButton.setOnMouseClicked(e -> {
            handleOnBattleButtonClicked(comboBoxP1.getValue(), comboBoxP2.getValue(), Integer.parseInt(noOfGamesField.getText()));
        });
        HBox battleHBox = new HBox(10, noOfGamesField, battleButton);
        battleHBox.setAlignment(Pos.CENTER);

        resultArea = new TextArea();
        resultArea.setFont(new Font(15));
        resultArea.setPrefHeight(400);

        VBox vBox = new VBox(5, headline, hBoxPlayers, winLabel, winLooseBar, battleHBox, resultArea);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
    }

    private void handleOnBattleButtonClicked(String AI1, String AI2, int noOfGames)
    {
        AI P1 = StartScreenController.getAI(AI1);
        AI P2 = StartScreenController.getAI(AI2);

        playOneBattle(P1, P2, noOfGames);
    }

    private void playOneBattle(AI P1, AI P2, int noOfGames)
    {
        int pointsPlayer1 = 0;
        int pointsPlayer2 = 0;

        for(int i = 0; i < noOfGames; i++)
        {
            if(i % 2 == 0)
            {
                Owner winner = playOneGame(P1, P2);

                if(winner == Owner.PERSON)
                {
                    pointsPlayer1++;
                }
                else
                {
                    pointsPlayer2++;
                }


            }
            else
            {
                Owner winner = playOneGame(P2, P1);

                if(winner == Owner.PERSON)
                {
                    pointsPlayer2++;
                }
                else
                {
                    pointsPlayer1++;
                }
            }
        }

        printBattle(P1, P2, pointsPlayer1, pointsPlayer2);
    }

    private void printBattle(AI P1, AI P2, int pointsPlayer1, int pointsPlayer2)
    {
        winLabel.setText(pointsPlayer1 + " : " + pointsPlayer2);
        winLooseBar.setProgress(calcProgress(pointsPlayer1, pointsPlayer2));

        StringBuilder result = new StringBuilder(resultArea.getText());

        String area = resultArea.getText();
        int lastNewLine = area.lastIndexOf('\n');

        if(lastNewLine != -1 &&
                (result.indexOf(P1.getName(), lastNewLine) == -1
                        || result.indexOf(P2.getName(), lastNewLine) == -1))
        {
            result.append("\n");
        }

        if(area.equals("") == false)
        {
            result.append("\n");
        }

        result.append("("
                + P1.getName() + ") " + pointsPlayer1 + " - "
                + pointsPlayer2 + " (" + P2.getName() + ")");


        resultArea.setText(result.toString());
    }

    private double calcProgress(int p1, int p2)
    {
        double noOfGames = p1 + p2;

        if(p1 == 0)
        {
            return 0;
        }

        return p1 / noOfGames;
    }

    private Owner playOneGame(AI startPlayer, AI secondPlayer)
    {
        startPlayer.setPlayerNumber(Owner.PERSON);
        secondPlayer.setPlayerNumber(Owner.NP);
        Game game = new MyGame();


        while(game.getWinner() == null)
        {
            Move move1 = startPlayer.getNextMove(game);
            game.doMove(move1);

            if(game.getWinner() != null)
            {
                break;
            }

            Move move2 = secondPlayer.getNextMove(game);
            game.doMove(move2);
        }

        return game.getWinner();
    }
}
