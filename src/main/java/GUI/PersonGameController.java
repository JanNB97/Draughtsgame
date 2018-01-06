package GUI;

import GUI.enums.Color;
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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class PersonGameController
{
    private Stage stage;
    private AI computerPlayer;
    private Game game;
    private MyImageView[][] imageViews = new MyImageView[8][8];

    private Piece selectedPiece;

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
                imageViews[i][j] = new MyImageView(j, i);
                MyImageView imageView = imageViews[i][j];
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                imageView.setOnMouseClicked(t -> {
                    onFieldClicked(imageView.getFieldXPos(), imageView.getFieldYPos());
                });
                hBox.getChildren().add(imageView);
            }

            vBox.getChildren().add(hBox);
        }
        refreshBoard();

        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        colorPieces(Color.GREEN, toArray(game.getAllMovablePieces()));
    }

    private Piece[] toArray(ArrayList<Piece> pieces)
    {
        Piece[] allPieces = new Piece[pieces.size()];

        for(int i = 0; i < pieces.size(); i++)
        {
            allPieces[i] = pieces.get(i);
        }

        return allPieces;
    }

    private void onFieldClicked(int xPos, int yPos)
    {
        if(game.getOnTurn() == Owner.PERSON)
        {
            Piece piece = game.getBoard().getPiece(xPos, yPos);

            if(piece != null && piece.getOwner() == Owner.PERSON)
            {
                //Select piece
                selectedPiece = piece;

                refreshBoard();
                colorPieces(Color.RED, piece);
                colorMoveDestination(Color.GREEN, game.getAllCurrentMoves(piece));
            }
            else if(piece == null)
            {
                //Tries to move
                ArrayList<Piece> victims = game.doMove(new Move(selectedPiece.getxPos(), selectedPiece.getyPos(), xPos, yPos));

                if(victims != null)
                {
                    selectedPiece = null;
                    refreshBoard();

                    Move move = computerPlayer.getNextMove(game);
                    Piece startPiece = new Piece(null, null, move.getNewXPos(), move.getNewYPos());
                    Piece endPiece = new Piece(null, null, move.getxPos(), move.getyPos());
                    ArrayList<Piece> computerVictims = game.doMove(move);

                    refreshBoard();
                    colorPieces(Color.BLUE, startPiece, endPiece);
                    colorPieces(Color.BLUE, toArray(computerVictims));
                    colorPieces(Color.GREEN, toArray(game.getAllMovablePieces()));
                }
            }
        }
    }

    private void colorMoveDestination(Color color, ArrayList<Move> moves)
    {
        ArrayList<Piece> allPieces = new ArrayList<>();

        for(Move move : moves)
        {
            int x = move.getNewXPos();
            int y = move.getNewYPos();

            Piece piece = new Piece(null, null, x, y);
            allPieces.add(piece);
        }

        colorPieces(color, toArray(allPieces));
    }

    private void colorPieces(Color color, Piece...pieces)
    {
        for(Piece piece : pieces)
        {
            int xPos = piece.getxPos();
            int yPos = piece.getyPos();

            String path = getPath(xPos, yPos);

            path = cutFour(path);

            StringBuilder builder = new StringBuilder(path);
            builder.append(color.toString());

            path = builder.toString();
            path = appendPng(path);

            Image image = new Image(path);
            imageViews[yPos][xPos].setImage(image);
        }
    }

    private String cutFour(String string)
    {
        StringBuilder sB = new StringBuilder(string);

        sB.delete(string.length() - 4, string.length());

        return sB.toString();
    }

    private String appendPng(String string)
    {
        StringBuilder sB = new StringBuilder(string);
        sB.append(".png");
        return sB.toString();
    }

    public void refreshBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {

                imageViews[i][j].setImage(new Image(getPath(j, i)));
            }
        }
    }

    public String getPath(int xPos, int yPos)
    {
        Piece piece = game.getBoard().getPiece(xPos, yPos);

        StringBuilder imageName = new StringBuilder("file:./src\\main\\resources\\boardBackground/");

        if(yPos % 2 == 0 && xPos % 2 == 0
                || yPos % 2 == 1 && xPos % 2== 1)
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
        return imageName.toString();
    }
}
