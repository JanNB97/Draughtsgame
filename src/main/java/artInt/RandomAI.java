package artInt;

import gameModel.Game;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class RandomAI extends AI
{
    public RandomAI()
    {
        super("RandomAI");
    }

    @Override
    public Move getNextMove(Game game)
    {
        ArrayList<Piece> pieces = game.getAllMovablePieces();

        Random random = new Random();
        int i = random.nextInt(pieces.size());

        ArrayList<Move> allMoves = game.getAllCurrentMoves(pieces.get(i));

        int j = random.nextInt(allMoves.size());

        return allMoves.get(j);
    }
}
