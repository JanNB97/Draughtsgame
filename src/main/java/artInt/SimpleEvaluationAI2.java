package artInt;

import AIHelp.BoardEvaluator;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;
import gameModel.enums.Owner;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class SimpleEvaluationAI2 extends AI
{
    private final int KingMULT = 3;
    private final float OwnMULT = 1.5f;

    public SimpleEvaluationAI2(Owner playerNumer)
    {
        super(playerNumer, "SimpleEvaluationAI2");
    }

    @Override
    public Move getNextMove(Board board)
    {
        return getBestMove(board);
    }

    private Move getBestMove(Board board)
    {
        Move bestMove = null;
        int bestEvaluation = Integer.MIN_VALUE;

        ArrayList<ArrayList<Move>> m = board.getAllMoves(getPlayerNumber());

        for(ArrayList<Move> allMoves : m)
        {
            for(Move move : allMoves)
            {
                Board boardAfterAIMove = board.tryMove(move);

                int evaluation = getMinimumNextEvaluation(boardAfterAIMove);

                if(evaluation > bestEvaluation)
                {
                    bestMove = move;
                    bestEvaluation = evaluation;
                }
                else if(evaluation == bestEvaluation)
                {
                    Random rand = new Random();
                    if(rand.nextInt(2) == 0)
                    {
                        bestMove = move;
                        bestEvaluation = evaluation;
                    }
                }
            }
        }

        return bestMove;
    }

    private int getMinimumNextEvaluation(Board boardAfterAIMove)
    {
        int minEvaluation = Integer.MAX_VALUE;

        ArrayList<ArrayList<Move>> mPerson = boardAfterAIMove.getAllMoves(getOpponentNumber());

        for(ArrayList<Move> allMovesPerson : mPerson)
        {
            for(Move movePerson : allMovesPerson)
            {
                Board boardAfterPersonMove = boardAfterAIMove.tryMove(movePerson);

                int evaluation = BoardEvaluator.evalutateBoard(boardAfterPersonMove, getPlayerNumber(), KingMULT, OwnMULT);

                if(evaluation < minEvaluation)
                {
                    minEvaluation = evaluation;
                }
            }
        }

        return minEvaluation;
    }
}
