package AITests;

import artInt.AI;
import artInt.SimpleEvaluationAI;
import gameModel.Move;
import gameModel.board.MyBoard;
import gameModel.enums.Owner;
import gameModel.enums.Type;
import org.junit.Assert;
import org.junit.Test;

public class SimpleEvaluationAITest
{
    @Test
    public void szenario()
    {
        MyBoard board = new MyBoard();
        board.clearBoard();

        board.setOnBoard(Owner.NP, Type.MAN, 5, 4, 2, 5);
        board.setOnBoard(Owner.PERSON, Type.MAN, 3, 2);

        AI simEva = new SimpleEvaluationAI(Owner.NP);

        Move move = simEva.getNextMove(board);
        Assert.assertNotEquals(move, new Move(5, 4, 4, 3));
    }
}
