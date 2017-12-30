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
    public void testScenarioOne()
    {
        Board board = new MyBoard();
        System.out.println(board.toString());

        assertWrongMove(board, new Move(0, 1, 5, 5, Direction.LEFT));

        assertRightMove(board, new Move(1, 2, 0, 3, Direction.LEFT));
        assertRightMove(board, new Move(0, 3, 1, 4, Direction.LEFT));
        assertRightMove(board, new Move(7, 2, 6, 3, Direction.LEFT));

        //jump
        assertRightMove(board, new Move(2, 5, 0, 3, Direction.LEFT));
        Assert.assertNull(board.getPiece(1, 4));

        assertRightMove(board, new Move(0, 3, 1, 2, Direction.LEFT));
        assertRightMove(board, new Move(4, 5, 3, 4, Direction.LEFT));

        //double jump
        assertRightMove(board, new Move(0, 1, 4, 5, Direction.LEFT));
        Assert.assertNull(board.getPiece(1, 2));
        Assert.assertNull(board.getPiece(3, 4));

        //another double jump
        assertRightMove(board, new Move(3, 6, 7, 2, Direction.LEFT));
        Assert.assertNull(board.getPiece(4, 5));
        Assert.assertNull(board.getPiece(6, 3));

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

        Board rightTestBoard = new MyBoard(board);
        assertRightMove(rightTestBoard, new Move(3, 6, 3, 2, Direction.RIGHT));
        Assert.assertNotNull(rightTestBoard.getPiece(2, 3));
        Assert.assertNotNull(rightTestBoard.getPiece(2, 5));
        Assert.assertNull(rightTestBoard.getPiece(4, 3));
        Assert.assertNull(rightTestBoard.getPiece(4, 5));

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

        Board rightTrippleBoard1 = new MyBoard(board);
        assertRightMove(rightTrippleBoard1, new Move(3, 6, 1, 0, Direction.RIGHT));
        Assert.assertNull(rightTrippleBoard1.getPiece(2, 1));
        Assert.assertNotNull(rightTrippleBoard1.getPiece(2, 3));
        Assert.assertNotNull(rightTrippleBoard1.getPiece(2, 5));
        Assert.assertNotNull(rightTrippleBoard1.getPiece(4, 1));
        Assert.assertNull(rightTrippleBoard1.getPiece(4, 3));
        Assert.assertNull(rightTrippleBoard1.getPiece(4, 5));

        Board leftTrippleBoard2 = new MyBoard(board);
        assertRightMove(leftTrippleBoard2, new Move(3, 6, 5, 0, Direction.LEFT));
        Assert.assertNotNull(leftTrippleBoard2.getPiece(2, 1));
        Assert.assertNull(leftTrippleBoard2.getPiece(2, 3));
        Assert.assertNull(leftTrippleBoard2.getPiece(2, 5));
        Assert.assertNull(leftTrippleBoard2.getPiece(4, 1));
        Assert.assertNotNull(leftTrippleBoard2.getPiece(4, 3));
        Assert.assertNotNull(leftTrippleBoard2.getPiece(4, 5));

        Board rightTrippleBoard2 = new MyBoard(board);
        assertRightMove(rightTrippleBoard2, new Move(3, 6, 5, 0, Direction.RIGHT));
        Assert.assertNotNull(rightTrippleBoard2.getPiece(2, 1));
        Assert.assertNotNull(rightTrippleBoard2.getPiece(2, 3));
        Assert.assertNotNull(rightTrippleBoard2.getPiece(2, 5));
        Assert.assertNull(rightTrippleBoard2.getPiece(4, 1));
        Assert.assertNull(rightTrippleBoard2.getPiece(4, 3));
        Assert.assertNull(rightTrippleBoard2.getPiece(4, 5));
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
