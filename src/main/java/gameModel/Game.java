package gameModel;

import gameModel.board.Board;
import gameModel.enums.Owner;

import java.util.ArrayList;
import java.util.TreeSet;

public interface Game
{
    //returns null, if no possible move, else returns victims
    TreeSet<Piece> doMove(Move move);

    ArrayList<Piece> getAllMovablePieces();
    ArrayList<Move> getAllCurrentMoves(Piece piece);

    Board getBoard();
    Owner getWinner();
}
