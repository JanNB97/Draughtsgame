package model.board;

import model.Move;
import model.Piece;
import model.enums.Owner;

import java.util.ArrayList;
import java.util.TreeSet;

public interface Board
{
    Piece getPiece(int xPos, int yPos);
    TreeSet<Piece> getAllPieces(Owner owner);

    ArrayList<Move> getAllMoves(Piece piece);
    ArrayList<Move> getAllMoves(Owner owner);

    boolean isPossibleMove(Move move);
    void doMove(Move move);
    void doAllMoves(Move...moves);
    Board tryMove(Move move);

    String toString();
}
