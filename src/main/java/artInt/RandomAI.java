package artInt;

import gameModel.Game;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;
import gameModel.enums.Owner;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.util.logging.Logger;

public class RandomAI extends AI
{
    public RandomAI(Owner playerNumber)
    {
        super(playerNumber,"RandomAI");
    }

    @Override
    public Move getNextMove(Board board)
    {
        ArrayList<ArrayList<Move>> moves = board.getAllMoves(getPlayerNumber());

        if(moves.size() == 0)
        {
            Logger.getGlobal().severe("Game has already finished");
            System.out.println(moves.toString());
        }

        Random random = new Random();
        int i = random.nextInt(moves.size());

        ArrayList<Move> allMoves = moves.get(i);

        int j = random.nextInt(allMoves.size());

        return allMoves.get(j);
    }
}
