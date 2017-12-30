package model;

import model.enums.Owner;

import java.util.ArrayList;

public interface Board
{
    Piece getPiece(int xPos, int yPos);
    ArrayList<Piece> getAllPieces(Owner owner);

    ArrayList<Move> getAllMoves(Piece piece);
    ArrayList<Move> getAllMoves(Owner owner);

    boolean isPossibleMove(Move move);
    void doMove(Move move);
    Board tryMove(Move move);

    String toString();
}
