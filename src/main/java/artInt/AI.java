package artInt;

import gameModel.Game;
import gameModel.Move;
import gameModel.Piece;
import gameModel.board.Board;
import gameModel.enums.Owner;

import java.util.TreeSet;
import java.util.logging.Logger;

public abstract class AI
{
    private Owner playerNumber;

    public AI(Owner playerNumber, String name)
    {
        this.name = name;
        this.playerNumber = playerNumber;
    }

    private String name;

    public Move getNextMove(Game game)
    {
        return getNextMove(game.getBoard());
    }

    public abstract Move getNextMove(Board board);

    public String getName()
    {
        return name;
    }

    public Owner getPlayerNumber()
    {
        return playerNumber;
    }

    public Owner getOpponentNumber()
    {
        if(playerNumber == Owner.PERSON)
        {
            return Owner.NP;
        }
        else if(playerNumber == Owner.NP)
        {
            return Owner.PERSON;
        }
        else
        {
            Logger.getGlobal().severe("No owner selected");
            return null;
        }
    }

    public void setPlayerNumber(Owner playerNumber)
    {
        this.playerNumber = playerNumber;
    }
}
