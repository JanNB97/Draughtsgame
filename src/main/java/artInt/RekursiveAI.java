package artInt;

import AIHelp.BoardEvaluator;
import gameModel.Move;
import gameModel.board.Board;
import gameModel.enums.Owner;

import java.util.ArrayList;
import java.util.Random;

public class RekursiveAI extends AI
{
    private final int KingMULT = 2;
    private final float OwnMULT = 1.25f;

    public RekursiveAI(Owner playerNumber)
    {
        super(playerNumber, "rekursive AI");
    }

    @Override
    public Move getNextMove(Board board)
    {
        final int DEPTH = 4;

        int bestEvaluation = Integer.MIN_VALUE;
        Move bestMove = null;

        for(Move move : getAllMoves(board, getPlayerNumber()))
        {
            Board boardAfterMove = board.tryMove(move);

            Board lastBoard = getLastBoard(boardAfterMove, getOtherPlayer(getPlayerNumber()), DEPTH);

            int evaluation = BoardEvaluator.evalutateBoard(lastBoard, getPlayerNumber(), KingMULT, OwnMULT);

            if(evaluation > bestEvaluation)
            {
                bestEvaluation = evaluation;
                bestMove = move;
            }
            else if(evaluation == bestEvaluation)
            {
                Random random = new Random();
                if(random.nextInt(2) == 0)
                {
                    bestEvaluation = evaluation;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private Board getLastBoard(Board board, Owner owner, int depth)
    {
        int bestEvaluation = Integer.MIN_VALUE;
        Board bestBoard = null;

        ArrayList<Move> allMoves = getAllMoves(board, owner);

        if(allMoves.size() == 0)
        {
            return board;
        }

        if(depth == 0)
        {
            for(Move move : allMoves)
            {
                Board boardAfterMove = board.tryMove(move);

                int evaluation = BoardEvaluator.evalutateBoard(boardAfterMove, owner, KingMULT, OwnMULT);

                if(evaluation > bestEvaluation)
                {
                    bestEvaluation = evaluation;
                    bestBoard = boardAfterMove;
                }
                else if(evaluation == bestEvaluation)
                {
                    Random random = new Random();
                    if(random.nextInt(2) == 0)
                    {
                        bestEvaluation = evaluation;
                        bestBoard = boardAfterMove;
                    }
                }
            }
        }
        else
        {
            for(Move move : allMoves)
            {
                Board boardAfterMove = board.tryMove(move);

                Board lastBoard = getLastBoard(boardAfterMove, getOtherPlayer(owner), depth - 1);

                int evaluation = BoardEvaluator.evalutateBoard(lastBoard, owner, KingMULT, OwnMULT);

                if(evaluation > bestEvaluation)
                {
                    bestEvaluation = evaluation;
                    bestBoard = boardAfterMove;
                }
                else if(evaluation == bestEvaluation)
                {
                    Random random = new Random();
                    if(random.nextInt(2) == 0)
                    {
                        bestEvaluation = evaluation;
                        bestBoard = boardAfterMove;
                    }
                }
            }
        }

        return bestBoard;
    }

    private ArrayList<Move> getAllMoves(Board board, Owner owner)
    {
        ArrayList<Move> result = new ArrayList<>();

        for(ArrayList<Move> allMoves : board.getAllMoves(owner))
        {
            for(Move move : allMoves)
            {
                result.add(move);
            }
        }

        return result;
    }

    private Owner getOtherPlayer(Owner owner)
    {
        if(owner == Owner.PERSON)
        {
            return Owner.NP;
        }
        else
        {
            return Owner.PERSON;
        }
    }
}
