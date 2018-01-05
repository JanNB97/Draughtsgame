package gameModel;

import gameModel.board.Board;
import gameModel.board.MyBoard;
import gameModel.enums.Owner;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;

public class MyGame implements Game
{
    private Board board;
    private Owner onTurn;
    private Owner winner;

    public MyGame()
    {
        board = new MyBoard();
        onTurn = Owner.PERSON;
        winner = null;
    }

    @Override
    public TreeSet<Piece> doMove(Move move)
    {
        Piece piece = board.getPiece(move.getxPos(), move.getyPos());
        TreeSet<Piece> victims = board.isPossibleMove(move);

        if(piece.getOwner() == onTurn && victims != null)
        {
            board.doMove(move, victims);
            moveFinished();
            return victims;
        }
        else
        {
            return null;
        }
    }

    private void moveFinished()
    {
        switch (onTurn)
        {
            case NP:
                onTurn = Owner.PERSON;
                break;
            case PERSON:
                onTurn = Owner.NP;
                break;
            default:
                Logger.getGlobal().severe("No owner selected");
                break;
        }

        if(gameIsFinished() != null)
        {
            endGame(gameIsFinished());
        }
    }

    //Returns winner
    private Owner gameIsFinished()
    {
        //Player on turn has no possible moves
        ArrayList<ArrayList<Move>> victims = board.getAllMoves(onTurn);

        if(victims.size() == 0)
        {
            if(onTurn == Owner.NP)
            {
                return Owner.PERSON;
            }
            else if(onTurn == Owner.PERSON)
            {
                return Owner.NP;
            }
            else
            {
                Logger.getGlobal().severe("No owner selected");
                return null;
            }
        }

        //One player has no pieces
        if(board.getAllPieces(Owner.NP).size() == 0)
        {
            return Owner.PERSON;
        }

        if(board.getAllPieces(Owner.PERSON).size() == 0)
        {
            return Owner.NP;
        }

        return null;
    }

    private void endGame(Owner winner)
    {
        onTurn = null;
        this.winner = winner;
    }

    @Override
    public ArrayList<Piece> getAllMovablePieces()
    {
        if(onTurn == null)
        {
            return new ArrayList<>();
        }

        ArrayList<Piece> allPieces = new ArrayList<>();

        ArrayList<ArrayList<Move>> m = board.getAllMoves(onTurn);

        for(ArrayList<Move> allMoves : m)
        {
            allPieces.add(board.getPiece(allMoves.get(0).getxPos(), allMoves.get(0).getyPos()));
        }

        return allPieces;
    }

    @Override
    public ArrayList<Move> getAllCurrentMoves(Piece piece)
    {
        ArrayList<ArrayList<Move>> m = board.getAllMoves(onTurn);

        for(ArrayList<Move> allMoves : m)
        {
            Move firstMove = allMoves.get(0);
            Piece thisPiece = board.getPiece(firstMove.getxPos(), firstMove.getyPos());

            if(thisPiece.equals(piece))
            {
                return allMoves;
            }
        }

        Logger.getGlobal().severe("This piece got no possible moves");
        return null;
    }

    @Override
    public Board getBoard()
    {
        return board;
    }

    @Override
    public Owner getWinner()
    {
        return winner;
    }
}
