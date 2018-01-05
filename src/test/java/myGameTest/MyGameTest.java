package myGameTest;

import gameModel.Game;
import gameModel.Move;
import gameModel.MyGame;
import gameModel.Piece;
import org.junit.Assert;
import org.junit.Test;

public class MyGameTest
{
    @Test
    public void testGameFinished()
    {
        MyGame game = new MyGame();

        while(game.getWinner() == null)
        {
            doNextMove(game);
        }

        Assert.assertNotNull(game.getWinner());
        Assert.assertEquals(game.getAllMovablePieces().size(), 0);
    }

    private void doNextMove(Game game)
    {
        Piece piece = game.getAllMovablePieces().get(0);
        Move move = game.getAllCurrentMoves(piece).get(0);

        game.doMove(move);
    }
}
