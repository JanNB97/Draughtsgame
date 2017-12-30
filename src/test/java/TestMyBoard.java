import model.Board;
import model.Move;
import model.MyBoard;
import model.Piece;
import model.enums.Direction;
import model.enums.Owner;
import org.junit.Assert;
import org.junit.Test;

public class TestMyBoard
{
    @Test
    public void testIsPossibleMove()
    {
        Board board = new MyBoard();
        System.out.println(board.toString());

        assertWrongMove(board, new Move(0, 1, 5, 5, Direction.LEFT));

        assertRightMove(board, new Move(1, 2, 0, 3, Direction.LEFT));
        assertRightMove(board, new Move(0, 3, 1, 4, Direction.LEFT));
        assertRightMove(board, new Move(7, 2, 6, 3, Direction.LEFT));

        //Try jump
        assertRightMove(board, new Move(2, 5, 0, 3, Direction.LEFT));
        Assert.assertNull(board.getPiece(1, 4));

        assertRightMove(board, new Move(0, 3, 1, 2, Direction.LEFT));
        assertRightMove(board, new Move(4, 5, 3, 4, Direction.LEFT));

        //Try double jump
        assertRightMove(board, new Move(0, 1, 4, 5, Direction.LEFT));
        Assert.assertNull(board.getPiece(1, 2));
        Assert.assertNull(board.getPiece(3, 4));

        //Try another double jump
        assertRightMove(board, new Move(3, 6, 7, 2, Direction.LEFT));
        Assert.assertNull(board.getPiece(4, 5));
        Assert.assertNull(board.getPiece(6, 3));
    }

    private void assertRightMove(Board board, Move move)
    {
        Owner owner = board.getPiece(move.getxPos(), move.getyPos()).getOwner();

        boolean b = board.isPossibleMove(move);
        Assert.assertTrue(b);

        board.doMove(move);
        System.out.println(board.toString());
        Assert.assertNull(board.getPiece(move.getxPos(), move.getyPos()));
        Assert.assertEquals(board.getPiece(move.getNewXPos(), move.getNewYPos()).getOwner(), owner);
        Assert.assertEquals(board.getPiece(move.getNewXPos(), move.getNewYPos()).getxPos(), move.getNewXPos());
        Assert.assertEquals(board.getPiece(move.getNewXPos(), move.getNewYPos()).getyPos(), move.getNewYPos());
    }

    private void assertWrongMove(Board board, Move move)
    {
        boolean b = board.isPossibleMove(move);
        Assert.assertFalse(b);
    }
}
