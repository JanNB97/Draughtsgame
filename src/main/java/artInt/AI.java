package artInt;

import gameModel.Game;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;

import java.util.TreeSet;

public abstract class AI
{
    public abstract Move getNextMove(Game game);
}
