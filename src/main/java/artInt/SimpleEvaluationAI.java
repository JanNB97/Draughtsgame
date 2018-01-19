package artInt;

import AIHelp.BoardEvaluator;
import gameModel.Game;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;
import gameModel.enums.Owner;
import gameModel.enums.Type;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import static gameModel.enums.Owner.NP;
import static gameModel.enums.Owner.PERSON;

public class SimpleEvaluationAI extends AI
{
    private final int KingMULT = 3;
    private final float OwnMULT = 1.5f;

    public SimpleEvaluationAI(Owner playerNumber)
    {
        super(playerNumber,"SimpleEvaluation");
    }

    @Override
    public Move getNextMove(Board board)
    {
        Move bestMove = null;
        int bestEvaluation = -1;

        ArrayList<ArrayList<Move>> allPieces = board.getAllMoves(getPlayerNumber());

        for(ArrayList<Move> movesOnePiece : allPieces)
        {
            for(Move move : movesOnePiece)
            {
                Board boardMove = board.tryMove(move);

                //If win
                if(boardMove.getAllPieces(getOtherPlayer(getPlayerNumber())).size() == 0
                        || boardMove.getAllMoves(getOtherPlayer(getPlayerNumber())).size() == 0)
                {
                    return move;
                }

                Move bestMovePlayer = getBestEvaluatedMove(getOtherPlayer(getPlayerNumber()), boardMove);

                Board boardPlayerMoves = boardMove.tryMove(bestMovePlayer);

                int evaluation = BoardEvaluator.evalutateBoard(boardPlayerMoves, getPlayerNumber(), KingMULT, OwnMULT);

                if(evaluation > bestEvaluation)
                {
                    bestMove = move;
                    bestEvaluation = evaluation;
                }
                else if(evaluation == bestEvaluation)
                {
                    Random rand = new Random();
                    int i = rand.nextInt(2);

                    if(i == 0)
                    {
                        bestMove = move;
                        bestEvaluation = evaluation;
                    }
                }
            }
        }

        return bestMove;
    }

    private Move getBestEvaluatedMove(Owner player, Board board)
    {
        ArrayList<ArrayList<Move>> allMoves = board.getAllMoves(player);

        int bestEvaluation = -1;
        Move bestMove = null;

        for(ArrayList<Move> movesFromPiece : allMoves)
        {
            for(Move move : movesFromPiece)
            {
                Board boardMove = board.tryMove(move);

                int evaluation = BoardEvaluator.evalutateBoard(boardMove, player, KingMULT, OwnMULT);

                if(evaluation > bestEvaluation)
                {
                    bestMove = move;
                    bestEvaluation = evaluation;
                }
                else if(evaluation == bestEvaluation)
                {
                    Random rand = new Random();
                    int i = rand.nextInt(2);

                    if(i == 0)
                    {
                        bestMove = move;
                        bestEvaluation = evaluation;
                    }
                }
            }
        }

        return bestMove;
    }

    private Owner getOtherPlayer(Owner player)
    {
        switch (player)
        {
            case PERSON:
                return NP;
            case NP:
                return PERSON;
            default:
                Logger.getGlobal().severe("No owner selected");
                return null;
        }
    }
}
