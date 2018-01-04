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

    ArrayList<ArrayList<Move>> getAllMoves(Owner owner);

    TreeSet<Piece> isPossibleMove(Move move);
    void doMove(Move move, TreeSet<Piece> victims);
    Board tryMove(Move move, TreeSet<Piece> victims);

    String toString();
}
