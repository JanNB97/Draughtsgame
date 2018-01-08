package artInt;

import gameModel.Game;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;

import java.util.TreeSet;

public abstract class AI
{
    public AI(String name)
    {
        this.name = name;
    }

    private String name;

    public abstract Move getNextMove(Game game);

    public String getName()
    {
        return name;
    }
}
