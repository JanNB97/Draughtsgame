package MyBoard;

import model.Move;
import model.Piece;
import model.board.MyBoard;
import model.enums.Direction;
import model.enums.Owner;
import model.enums.Type;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TestAllMoves
{
    @Test
    public void testAllMovesKing()
    {
        MyBoard board = new MyBoard();
        board.clearBoard();
        board.setOnBoard(Owner.PERSON, Type.MAN, 2, 1, 2, 3);
        board.setOnBoard(Owner.NP, Type.MAN, 7, 2, 1, 4);
        board.setOnBoard(Owner.NP, Type.KING, 4, 5);

        System.out.println(board.toString());
        ArrayList<ArrayList<Move>> allMovesNP = board.getAllMoves(Owner.NP);
        System.out.println(allMovesNP.toString());
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 1, 4), allMovesNP, 1, 0);
        assertAllMoves(board, new Piece(Owner.NP, Type.KING, 4, 5), allMovesNP, 3, 0);
    }

    @Test
    public void testAllMovesPiece()
    {
        MyBoard board = new MyBoard();

        //Test on start board
        ArrayList<ArrayList<Move>> allMovesP1 = board.getAllMoves(Owner.PERSON);
        ArrayList<ArrayList<Move>> allMovesN1 = board.getAllMoves(Owner.NP);

        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 1, 2), allMovesP1, 0, 3, 2, 3);
        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 3, 2), allMovesP1, 2, 3, 4, 3);
        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 5, 2), allMovesP1, 4, 3, 6, 3);
        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 7, 2), allMovesP1, 6, 3);
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 0, 5), allMovesN1, 1, 4);

        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN,1, 0), allMovesP1);

        //Test in a simple case
        board.clearBoard();
        board.setOnBoard(Owner.PERSON, Type.MAN, 3, 2);
        board.setOnBoard(Owner.NP, Type.MAN, 4, 3);

        ArrayList<ArrayList<Move>> allMovesP2 = board.getAllMoves(Owner.PERSON);
        ArrayList<ArrayList<Move>> allMovesN2 = board.getAllMoves(Owner.NP);

        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 3, 2), allMovesP2,  5, 4);
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 4, 3), allMovesN2, 2, 1);

        //Test with left and right
        board.clearBoard();
        board.setOnBoard(Owner.NP, Type.MAN, 2, 3, 4, 3, 2, 5, 4, 5);
        board.setOnBoard(Owner.PERSON, Type.MAN, 3, 2, 1, 0);

        ArrayList<ArrayList<Move>> allMovesP3 = board.getAllMoves(Owner.PERSON);
        ArrayList<ArrayList<Move>> allMovesN3 = board.getAllMoves(Owner.NP);

        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 3, 2), allMovesP3, 3, 6);
        assertAllMoves(board, new Piece(Owner.PERSON, Type.MAN, 1, 0), allMovesP3);
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 2, 3), allMovesN3, 4, 1);
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 4, 3), allMovesN3, 2, 1);
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 2, 5), allMovesN3);
        assertAllMoves(board, new Piece(Owner.NP, Type.MAN, 4, 5), allMovesN3);
    }

    private void assertAllMoves(MyBoard board, Piece piece, ArrayList<ArrayList<Move>> allMovesFromAllPieces, int...newPos)
    {
        ArrayList<Move> allMoves = getAllMoves(allMovesFromAllPieces, piece);

        Assert.assertTrue(containsThisMoves(piece.getType(), allMoves, piece.getxPos(), piece.getyPos(), newPos));
    }

    //Test with right and left!!!
    private boolean containsThisMoves(Type type, ArrayList<Move> allMoves, int xPos, int yPos, int...newPos)
    {
        if(newPos.length % 2 != 0)
        {
            Logger.getGlobal().severe("Wrong number of arguments");
            return false;
        }

        for(int i = 0; i < newPos.length; i += 2)
        {
            Move move1 = new Move(xPos, yPos, newPos[i], newPos[i + 1], Direction.LEFT);
            Move move2 = new Move(xPos, yPos, newPos[i], newPos[i+1], Direction.RIGHT);

            if(allMoves.contains(move1) == false || (allMoves.contains(move2) == false && type == Type.MAN))
            {
                return false;
            }
        }

        if(type == Type.KING)
        {
            return allMoves.size() == newPos.length / 2;
        }
        else
        {
            return allMoves.size() == newPos.length;
        }
    }

    private ArrayList<Move> getAllMoves(ArrayList<ArrayList<Move>> allMovesFromAllPieces, Piece piece)
    {
        for(ArrayList<Move> allMoves : allMovesFromAllPieces)
        {
            for(Move move : allMoves)
            {
                if(move.getxPos() == piece.getxPos() && move.getyPos() == piece.getyPos())
                {
                    return allMoves;
                }
            }
        }

        return new ArrayList<>();
    }
}
