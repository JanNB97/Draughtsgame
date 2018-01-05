package myBoardTest;

import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;
import gameModel.board.MyBoard;
import gameModel.enums.Direction;
import gameModel.enums.Owner;
import gameModel.enums.Type;

import org.junit.Assert;
import org.junit.Test;

import java.util.TreeSet;

public class TestManKingJump
{
    @Test
    public void testManJumpSzenario1()
    {
        MyBoard board = new MyBoard();
        Assert.assertEquals(board.getAllPieces(Owner.PERSON).size(), 12);
        Assert.assertEquals(board.getAllPieces(Owner.NP).size(), 12);

        assertWrongMove(board, new Move(0, 1, 5, 5, Direction.LEFT));
        assertWrongMove(board, new Move(1, 0, 1, 0));

        assertRightMove(board, new Move(1, 2, 0, 3, Direction.LEFT));
        assertRightMove(board, new Move(0, 3, 1, 4, Direction.LEFT));
        assertRightMove(board, new Move(7, 2, 6, 3, Direction.LEFT));

        //jump
        assertRightMove(board, new Move(2, 5, 0, 3, Direction.LEFT));
        Assert.assertNull(board.getPiece(1, 4));

        Assert.assertEquals(11, board.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(12, board.getAllPieces(Owner.NP).size());

        assertRightMove(board, new Move(0, 3, 1, 2, Direction.LEFT));
        assertRightMove(board, new Move(4, 5, 3, 4, Direction.LEFT));

        //double jump
        assertRightMove(board, new Move(0, 1, 4, 5, Direction.LEFT));
        Assert.assertNull(board.getPiece(1, 2));
        Assert.assertNull(board.getPiece(3, 4));

        Assert.assertEquals(11, board.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, board.getAllPieces(Owner.NP).size());

        //another double jump
        assertRightMove(board, new Move(3, 6, 7, 2, Direction.LEFT));
        Assert.assertNull(board.getPiece(4, 5));
        Assert.assertNull(board.getPiece(6, 3));

        Assert.assertEquals(9, board.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, board.getAllPieces(Owner.NP).size());

        assertRightMove(board, new Move(2, 7, 3, 6, Direction.LEFT));
        assertRightMove(board, new Move(3, 2, 2, 3, Direction.LEFT));
        assertRightMove(board, new Move(2, 3, 1, 4, Direction.LEFT));
        assertRightMove(board, new Move(1, 4, 2, 5, Direction.LEFT));

        assertRightMove(board, new Move(5, 2, 6, 3, Direction.LEFT));
        assertRightMove(board, new Move(6, 3, 5, 4, Direction.LEFT));
        assertRightMove(board, new Move(5, 4, 4, 5, Direction.LEFT));

        assertRightMove(board, new Move(2, 1, 1, 2, Direction.LEFT));
        assertRightMove(board, new Move(1, 2, 2, 3, Direction.LEFT));
        assertRightMove(board, new Move(4, 1, 5, 2, Direction.LEFT));
        assertRightMove(board, new Move(5, 2, 4, 3, Direction.LEFT));

        //Try double jump with different directions
        Board leftTestBoard = new MyBoard(board);
        assertRightMove(leftTestBoard, new Move(3, 6, 3, 2, Direction.LEFT));
        Assert.assertNull(leftTestBoard.getPiece(2, 3));
        Assert.assertNull(leftTestBoard.getPiece(2, 5));
        Assert.assertNotNull(leftTestBoard.getPiece(4, 3));
        Assert.assertNotNull(leftTestBoard.getPiece(4, 5));

        Assert.assertEquals(7, leftTestBoard.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, leftTestBoard.getAllPieces(Owner.NP).size());

        Board rightTestBoard = new MyBoard(board);
        assertRightMove(rightTestBoard, new Move(3, 6, 3, 2, Direction.RIGHT));
        Assert.assertNotNull(rightTestBoard.getPiece(2, 3));
        Assert.assertNotNull(rightTestBoard.getPiece(2, 5));
        Assert.assertNull(rightTestBoard.getPiece(4, 3));
        Assert.assertNull(rightTestBoard.getPiece(4, 5));

        Assert.assertEquals(7, rightTestBoard.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, rightTestBoard.getAllPieces(Owner.NP).size());

        assertRightMove(board, new Move(1, 0, 2, 1, Direction.RIGHT));
        assertRightMove(board, new Move(5, 0, 4, 1, Direction.RIGHT));


        //Try tripple jump with different directions
        Board leftTrippleBoard1 = new MyBoard(board);
        assertRightMove(leftTrippleBoard1, new Move(3, 6, 1, 0, Direction.LEFT));
        Assert.assertNull(leftTrippleBoard1.getPiece(2, 1));
        Assert.assertNull(leftTrippleBoard1.getPiece(2, 3));
        Assert.assertNull(leftTrippleBoard1.getPiece(2, 5));
        Assert.assertNotNull(leftTrippleBoard1.getPiece(4, 1));
        Assert.assertNotNull(leftTrippleBoard1.getPiece(4, 3));
        Assert.assertNotNull(leftTrippleBoard1.getPiece(4, 5));

        Assert.assertEquals(6, leftTrippleBoard1.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, leftTrippleBoard1.getAllPieces(Owner.NP).size());

        Board rightTrippleBoard1 = new MyBoard(board);
        assertRightMove(rightTrippleBoard1, new Move(3, 6, 1, 0, Direction.RIGHT));
        Assert.assertNull(rightTrippleBoard1.getPiece(2, 1));
        Assert.assertNotNull(rightTrippleBoard1.getPiece(2, 3));
        Assert.assertNotNull(rightTrippleBoard1.getPiece(2, 5));
        Assert.assertNotNull(rightTrippleBoard1.getPiece(4, 1));
        Assert.assertNull(rightTrippleBoard1.getPiece(4, 3));
        Assert.assertNull(rightTrippleBoard1.getPiece(4, 5));

        Assert.assertEquals(6, rightTrippleBoard1.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, rightTrippleBoard1.getAllPieces(Owner.NP).size());

        Board leftTrippleBoard2 = new MyBoard(board);
        assertRightMove(leftTrippleBoard2, new Move(3, 6, 5, 0, Direction.LEFT));
        Assert.assertNotNull(leftTrippleBoard2.getPiece(2, 1));
        Assert.assertNull(leftTrippleBoard2.getPiece(2, 3));
        Assert.assertNull(leftTrippleBoard2.getPiece(2, 5));
        Assert.assertNull(leftTrippleBoard2.getPiece(4, 1));
        Assert.assertNotNull(leftTrippleBoard2.getPiece(4, 3));
        Assert.assertNotNull(leftTrippleBoard2.getPiece(4, 5));

        Assert.assertEquals(6, leftTrippleBoard2.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, leftTrippleBoard2.getAllPieces(Owner.NP).size());

        Board rightTrippleBoard2 = new MyBoard(board);
        assertRightMove(rightTrippleBoard2, new Move(3, 6, 5, 0, Direction.RIGHT));
        Assert.assertNotNull(rightTrippleBoard2.getPiece(2, 1));
        Assert.assertNotNull(rightTrippleBoard2.getPiece(2, 3));
        Assert.assertNotNull(rightTrippleBoard2.getPiece(2, 5));
        Assert.assertNull(rightTrippleBoard2.getPiece(4, 1));
        Assert.assertNull(rightTrippleBoard2.getPiece(4, 3));
        Assert.assertNull(rightTrippleBoard2.getPiece(4, 5));

        Assert.assertEquals(6, rightTrippleBoard2.getAllPieces(Owner.PERSON).size());
        Assert.assertEquals(10, rightTrippleBoard2.getAllPieces(Owner.NP).size());
    }

    @Test
    public void testManJumpSzenario2()
    {
        //Proove forced jump
        MyBoard board = new MyBoard();

        board.clearBoard();
        board.setOnBoard(Owner.PERSON, Type.MAN, 1, 0);
        board.setOnBoard(Owner.NP, Type.MAN, 2, 1, 4, 3);

        Assert.assertNull(board.isPossibleMove(new Move(1, 0, 0, 1)));
        Assert.assertNull(board.isPossibleMove(new Move(1, 0, 3, 2)));
        Assert.assertNotNull(board.isPossibleMove(new Move(1, 0, 5, 4)));
    }

    @Test
    public void testKingJumpSzenario1()
    {
        MyBoard board = new MyBoard();
        board.clearBoard();

        board.setOnBoard(Owner.NP, Type.MAN, 0, 1);

        //Change man to king
        Move m = new Move(0, 1, 1, 0);
        board.doMove(m, board.isPossibleMove(m));
        Assert.assertEquals(board.getPiece(1, 0).getType(), Type.KING);
    }

    @Test
    public void testKingJumpSzenario2()
    {
        MyBoard board = new MyBoard();

        board.clearBoard();
        board.setOnBoard(Owner.PERSON, Type.MAN, 5, 2, 4, 5);
        board.setOnBoard(Owner.NP, Type.KING, 3, 4);

        //Try to move with king
        Assert.assertNotNull(board.isPossibleMove(new Move(3, 4, 6, 1)));
        Assert.assertNotNull(board.isPossibleMove(new Move(3, 4, 5, 6)));

        board.setOnBoard(Owner.PERSON, Type.MAN, 2, 3, 2, 1);

        Assert.assertNotNull(board.isPossibleMove(new Move(3, 4, 3, 6)));
        Assert.assertNull(board.isPossibleMove(new Move(3, 4, 1, 0)));
        Assert.assertNotNull(board.isPossibleMove(new Move(3, 4, 5, 6)));
        Assert.assertNull(board.isPossibleMove(new Move(3, 4, 7, 2)));

        board.doMove(new Move(3, 4, 5, 6), board.isPossibleMove(new Move(3, 4, 5, 6)));
        Assert.assertNull(board.getPiece(4, 5));
        Assert.assertNull(board.getPiece(2, 3));
        Assert.assertEquals(board.getPiece(5, 6), new Piece(Owner.NP, Type.KING, 5, 6));
        Assert.assertEquals(board.getPiece(5, 2), new Piece(Owner.PERSON, Type.MAN, 5, 2));
        Assert.assertEquals(board.getPiece(2, 1), new Piece(Owner.PERSON, Type.MAN, 2, 1));

        board.setOnBoard(Owner.PERSON, Type.MAN, 2, 3);
        board.setOnBoard(Owner.PERSON, Type.KING, 3, 6);

        Assert.assertNull(board.isPossibleMove(new Move(5, 6, 6, 3)));
        Assert.assertNotNull(board.isPossibleMove(new Move(5, 6, 2, 7)));

        board.doMove(new Move(5, 6, 2, 7), board.isPossibleMove(new Move(5, 6, 2, 7)));
        Assert.assertNull(board.getPiece(5, 6));
        Assert.assertNull(board.getPiece(2, 1));
        Assert.assertNull(board.getPiece(5, 2));
        Assert.assertNull(board.getPiece(2, 3));
        Assert.assertNull(board.getPiece(3, 6));
        Assert.assertEquals(board.getPiece(2, 7), new Piece(Owner.NP, Type.KING, 2, 7));

        //Try round jump
        board.clearBoard();
        board.setOnBoard(Owner.PERSON, Type.KING, 3, 6);
        board.setOnBoard(Owner.NP, Type.MAN, 2, 3, 4, 3, 2, 5, 4, 5);

        Assert.assertNull(board.isPossibleMove(new Move(3, 6, 3, 2)));
        Assert.assertNull(board.isPossibleMove(new Move(3, 6, -1, 2)));
        Assert.assertNotNull(board.isPossibleMove(new Move(3, 6, 3, 6)));

        board.doMove(new Move(3, 6, 3, 6), board.isPossibleMove(new Move(3, 6, 3, 6)));
        Assert.assertNull(board.getPiece(2, 3));
        Assert.assertNull(board.getPiece(4, 3));
        Assert.assertNull(board.getPiece(2, 5));
        Assert.assertNull(board.getPiece(4, 5));
        Assert.assertEquals(board.getPiece(3, 6), new Piece(Owner.PERSON, Type.KING, 3, 6));

        //Try false jumps
        board.clearBoard();
        board.setOnBoard(Owner.NP, Type.KING, 7, 4);
        board.setOnBoard(Owner.PERSON, Type.KING, 7, 2);

        Assert.assertNull(board.isPossibleMove(new Move(7, 4, 8, 1)));
        Assert.assertNull(board.isPossibleMove(new Move(7, 4, 7, 4)));

        //Try direct run, while jumps are available
        board.clearBoard();
        board.setOnBoard(Owner.NP, Type.KING, 4, 3);
        board.setOnBoard(Owner.PERSON, Type.MAN, 5, 4);

        Assert.assertNull(board.isPossibleMove(new Move(4, 3, 7, 0)));
    }

    private void assertRightMove(Board board, Move move)
    {
        Owner owner = board.getPiece(move.getxPos(), move.getyPos()).getOwner();

        TreeSet<Piece> b = board.isPossibleMove(move);
        Assert.assertNotNull(b);

        board.doMove(move, b);
        Assert.assertNull(board.getPiece(move.getxPos(), move.getyPos()));
        Assert.assertEquals(board.getPiece(move.getNewXPos(), move.getNewYPos()).getOwner(), owner);
        Assert.assertEquals(board.getPiece(move.getNewXPos(), move.getNewYPos()).getxPos(), move.getNewXPos());
        Assert.assertEquals(board.getPiece(move.getNewXPos(), move.getNewYPos()).getyPos(), move.getNewYPos());
    }

    private void assertWrongMove(Board board, Move move)
    {
        TreeSet<Piece> b = board.isPossibleMove(move);
        Assert.assertNull(b);
    }
}
