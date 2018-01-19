package GUI;

import artInt.AI;
import gameModel.Game;
import gameModel.Move;
import gameModel.MyGame;
import gameModel.enums.Owner;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ArenaGameController
{
    private Stage stage;
    private int battleNum = 0;
    private AI lastP1;
    private AI lastP2;

    private Label winLabel;
    private ProgressBar winLooseBar;
    private TextArea resultArea;
    private Button battleButton;
    private ProgressIndicator progressIndicator;

    public ArenaGameController(Stage stage)
    {
        this.stage = stage;
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
        battleButton = new Button("BATTLE!");
        battleButton.setFont(new Font(20));
        battleButton.setOnMouseClicked(e -> {
            handleOnBattleButtonClicked(comboBoxP1.getValue(), comboBoxP2.getValue(), Integer.parseInt(noOfGamesField.getText()));
        });
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        HBox battleHBox = new HBox(10, noOfGamesField, battleButton, progressIndicator);
        battleHBox.setAlignment(Pos.CENTER);

        resultArea = new TextArea();
        resultArea.setEditable(false);
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
        final int[] pointsPlayer1 = {0};
        final int[] pointsPlayer2 = {0};

        winLabel.setText("-");
        battleButton.setDisable(true);
        progressIndicator.setVisible(true);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for(int i = 0; i < noOfGames; i++)
                {
                    if(i % 2 == 0)
                    {

                        Owner winner = playOneGame(P1, P2);

                        if (winner == Owner.PERSON)
                        {
                            pointsPlayer1[0]++;
                        } else {
                            pointsPlayer2[0]++;
                        }
                    }
                    else
                    {
                        Owner winner = playOneGame(P2, P1);

                        if (winner == Owner.PERSON)
                        {
                            pointsPlayer2[0]++;
                        } else {
                            pointsPlayer1[0]++;
                        }
                    }
                }

                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        printBattle(P1, P2, pointsPlayer1[0], pointsPlayer2[0]);
                        battleButton.setDisable(false);
                        progressIndicator.setVisible(false);
                        lastP1 = P1;
                        lastP2 = P2;

                        battleNum++;
                    }
                });
            }
        }).start();
    }

    private void printBattle(AI P1, AI P2, int pointsPlayer1, int pointsPlayer2)
    {
        winLabel.setText(pointsPlayer1 + " : " + pointsPlayer2);
        winLooseBar.setProgress(calcProgress(pointsPlayer1, pointsPlayer2));

        refreshResultArea(P1, P2, pointsPlayer1, pointsPlayer2);
    }

    private void refreshResultArea(AI P1, AI P2, int pointsPlayer1, int pointsPlayer2)
    {
        StringBuilder result = new StringBuilder(resultArea.getText());

        String P1Name = P1.getName();
        String P2Name = P2.getName();

        if(battleNum != 0)
        {
            String P1LastName = lastP1.getName();
            String P2LastName = lastP2.getName();

            if((P1Name.equals(P1LastName) && P2Name.equals(P2LastName)) == false)
            {
                result.append("\n" + P1Name + " vs. " + P2Name + "\n");
            }
        }
        else
        {
            result.append(P1Name + " vs " + P2Name + "\n");
        }


        result.append("\t" + pointsPlayer1 + "\t-\t"
                + pointsPlayer2 + "\n");


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
