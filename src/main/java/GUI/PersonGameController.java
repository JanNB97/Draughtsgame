package GUI;

import artInt.AI;
import gameModel.Game;
import gameModel.Move;
import gameModel.MyGame;
import gameModel.Piece;
import gameModel.board.Board;
import gameModel.enums.Owner;
import gameModel.enums.Type;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.TreeSet;

public class PersonGameController
{
    private Stage stage;
    private AI computerPlayer;
    private Game game;
    private ImageView[][] imageViews = new ImageView[8][8];

    public PersonGameController(Stage stage, AI computerPlayer)
    {
        this.stage = stage;
        this.computerPlayer = computerPlayer;

        game = new MyGame();
    }

    public void show()
    {
        stage.hide();
        stage.show();

        VBox vBox = new VBox();

        for(int i = 0; i < 8; i++)
        {
            HBox hBox = new HBox();

            for(int j = 0; j < 8; j++)
            {
                imageViews[i][j] = new ImageView();
                ImageView imageView = imageViews[i][j];
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                hBox.getChildren().add(imageView);
            }

            vBox.getChildren().add(hBox);
        }
        refreshBoard();

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
    }

    private void startGame()
    {
        ArrayList<Piece> allPieces = game.getAllMovablePieces();

        //TODO
    }

    public void refreshBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Piece piece = game.getBoard().getPiece(j, i);

                StringBuilder imageName = new StringBuilder("file:./src\\main\\resources\\boardBackground/");

                if(i % 2 == 0 && j % 2 == 0
                        || i % 2 == 1 && j % 2== 1)
                {
                    imageName.append("white");
                }
                else
                {
                    imageName.append("black");
                }

                if(piece != null)
                {
                    if(piece.getOwner() == Owner.PERSON)
                    {
                        imageName.append("Black");
                    }
                    else
                    {
                        imageName.append("White");
                    }

                    if(piece.getType() == Type.MAN)
                    {
                        imageName.append("Man");
                    }
                    else
                    {
                        imageName.append("King");
                    }
                }

                imageName.append(".png");
                imageViews[i][j].setImage(new Image(imageName.toString()));
            }
        }
    }
}
