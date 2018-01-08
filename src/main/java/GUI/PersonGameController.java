package GUI;

import GUI.enums.BoardDirection;
import GUI.enums.Color;
import artInt.AI;
import gameModel.Game;
import gameModel.Move;
import gameModel.MyGame;
import gameModel.Piece;
import gameModel.enums.Direction;
import gameModel.enums.Owner;
import gameModel.enums.Type;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private MyStackPane[][] fields = new MyStackPane[8][8];
    private final BoardDirection boardDirection = BoardDirection.PLAYERSOUTH;

    private Piece selectedPiece;
    private Move nextMove;

    private ArrayList<Piece> markedVictims;

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
                int k = Math.abs(boardDirection.getValue() - i);

                fields[k][j] = new MyStackPane(j, k);
                MyStackPane stackPane = fields[k][j];

                stackPane.setMinSize(120, 120);

                stackPane.setOnMouseClicked(t -> {
                    onFieldClicked(stackPane.getX(), stackPane.getY(), t);
                });
                stackPane.setOnMouseEntered(t -> {
                    mouseOnField(stackPane.getX(), stackPane.getY());
                });
                hBox.getChildren().add(stackPane);
            }

            vBox.getChildren().add(hBox);
        }
        refreshBoard();

        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        colorPieces(Color.MARKEDANOTHEROPTION, toArray(game.getAllMovablePieces()));
    }

    private void mouseOnField(int x, int y)
    {
        if(selectedPiece != null && game.getOnTurn() == Owner.PERSON)
        {
            int xPos = selectedPiece.getxPos();
            int yPos = selectedPiece.getyPos();

            ArrayList<Move> allMoves = game.getAllCurrentMoves(selectedPiece);

            Move thisMove = new Move(xPos, yPos, x, y, Direction.LEFT);
            Move thisMove2 = new Move(xPos, yPos, x, y, Direction.RIGHT);

            if(allMoves.contains(thisMove))
            {
                TreeSet<Piece> v = game.getBoard().isPossibleMove(thisMove);
                TreeSet<Piece> v2 = game.getBoard().isPossibleMove(thisMove2);

                if(v != null && v.size() > 0)
                {
                    ArrayList<Piece> victims = new ArrayList<>(v);
                    ArrayList<Piece> victims2 = new ArrayList<>(v2);
                    victims.addAll(victims2);

                    //Print victims
                    colorPieces(Color.VICTIM, toArray(victims));

                    markedVictims = victims;
                }
            }
            else
            {
                if(markedVictims != null)
                {
                    removeMarkers(toArray(markedVictims));
                    markedVictims = null;
                }
            }
        }

    }

    private void removeMarkers(Piece...pieces)
    {
        for(Piece piece : pieces)
        {
            int x = piece.getxPos();
            int y = piece.getyPos();

            while(fields[y][x].getChildren().size() > 2)
            {
                //Something is marked
                fields[y][x].getChildren().remove(fields[y][x].getChildren().size() - 1);
            }
        }
    }

    private void onFieldClicked(int xPos, int yPos, MouseEvent mouseEvent)
    {
        Direction direction = null;

        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            direction = Direction.LEFT;
        }
        else if(mouseEvent.getButton() == MouseButton.SECONDARY)
        {
            direction = Direction.RIGHT;
        }

        if(game.getOnTurn() == Owner.PERSON)
        {
            Piece piece = game.getBoard().getPiece(xPos, yPos);

            if(piece != null && piece.getOwner() == Owner.PERSON && game.getAllMovablePieces().contains(piece))
            {
                playerSelectPiece(piece);
            }
            else if(piece == null
                    && selectedPiece != null
                    && game.getAllCurrentMoves(selectedPiece).contains(new Move(selectedPiece.getxPos(), selectedPiece.getyPos(), xPos, yPos)))
            {
                playerDoMove(xPos, yPos, direction);
            }
        }
        else
        {
            computerPlayerDoMove();
        }
    }

    private void playerDoMove(int newXPos, int newYPos, Direction direction)
    {
        //Tries to move
        ArrayList<Piece> victims = game.doMove(new Move(selectedPiece.getxPos(), selectedPiece.getyPos(), newXPos, newYPos, direction));

        if(victims != null)
        {
            selectedPiece = null;
            refreshBoard();

            computerPlayerShowNextMove();
        }
    }

    private void playerSelectPiece(Piece piece)
    {
        //Select piece
        selectedPiece = piece;

        refreshBoard();
        colorPieces(Color.MARKEDANOTHEROPTION, toArray(game.getAllMovablePieces()));
        colorPieces(Color.MARKEDCLICKED, piece);
        colorMoveDestination(Color.DESTINATION, game.getAllCurrentMoves(piece));
    }

    private void computerPlayerShowNextMove()
    {
        Move move = computerPlayer.getNextMove(game);
        nextMove = move;

        Piece startPiece = new Piece(null, null, move.getxPos(), move.getyPos());
        Piece endPiece = new Piece(null, null, move.getNewXPos(), move.getNewYPos());
        ArrayList<Piece> computerVictims = new ArrayList<>(game.getBoard().isPossibleMove(move));

        refreshBoard();
        colorPieces(Color.MARKEDCLICKED, startPiece);
        colorPieces(Color.MARKEDANOTHEROPTION, startPiece);
        colorPieces(Color.DESTINATION, endPiece);
        colorPieces(Color.VICTIM, toArray(computerVictims));
    }

    private void computerPlayerDoMove()
    {
        ArrayList<Piece> computerVictims = game.doMove(nextMove);

        refreshBoard();

        //Show player his moves
        colorPieces(Color.MARKEDANOTHEROPTION, toArray(game.getAllMovablePieces()));
    }

    private void colorPieces(Color color, Piece...pieces)
    {
        for(Piece piece : pieces)
        {
            int xPos = piece.getxPos();
            int yPos = piece.getyPos();


            Image image = null;

            switch (color)
            {
                case VICTIM:
                    image = new Image("file:./src\\main\\resources\\boardBackground/victim.png");
                    break;
                case DESTINATION:
                    image = new Image("file:./src\\main\\resources\\boardBackground/destination.png");
                    break;
                case MARKEDCLICKED:
                    image = new Image("file:./src\\main\\resources\\boardBackground/markedClicked.png");
                    break;
                case MARKEDANOTHEROPTION:
                    image = new Image("file:./src\\main\\resources\\boardBackground/markedAnotherOption.png");
                    break;
            }

            fields[yPos][xPos].getChildren().add(new ImageView(image));
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

    private void refreshBoard()
    {
        clearBoard();

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Piece piece = game.getBoard().getPiece(j, i);



                if(i % 2 == 0 && j % 2 == 0
                        || i % 2 == 1 && j % 2== 1)
                {
                    fields[i][j].getChildren().add(new ImageView(new Image("file:./src\\main\\resources\\boardBackground/white.png")));
                }
                else
                {
                    //BLACK
                    fields[i][j].getChildren().add(new ImageView(new Image("file:./src\\main\\resources\\boardBackground/black.png")));

                }

                if(piece != null)
                {
                    if(piece.getOwner() == Owner.PERSON)
                    {
                        if(piece.getType() == Type.MAN)
                        {
                            fields[i][j].getChildren().add(new ImageView(new Image("file:./src\\main\\resources\\boardBackground/manBlack.png")));
                        }
                        else
                        {
                            fields[i][j].getChildren().add(new ImageView(new Image("file:./src\\main\\resources\\boardBackground/kingBlack.png")));
                        }
                    }
                    else
                    {
                        if(piece.getType() == Type.MAN)
                        {
                            fields[i][j].getChildren().add(new ImageView(new Image("file:./src\\main\\resources\\boardBackground/manWhite.png")));
                        }
                        else
                        {
                            fields[i][j].getChildren().add(new ImageView(new Image("file:./src\\main\\resources\\boardBackground/kingWhite.png")));
                        }
                    }
                }
            }
        }
    }

    private void clearBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                fields[i][j].getChildren().remove(0, fields[i][j].getChildren().size());
            }
        }
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
}
