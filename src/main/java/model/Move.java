package model;

import java.util.logging.Logger;

import model.enums.Direction;
import model.enums.KingDirection;

public class Move
{
    private int xPos;
    private int yPos;
    private int newXPos;
    private int newYPos;
    private Direction jumpDirection;
    private KingDirection kingDirection;

    public Move(int xPos, int yPos, int newXPos, int newYPos, Direction jumpDirection)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.newXPos = newXPos;
        this.newYPos = newYPos;
        this.jumpDirection = jumpDirection;
        
        setKingDirection(xPos, yPos, newXPos, newYPos);
    }
    
    public Move(int xPos, int yPos, int newXPos, int newYPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.newXPos = newXPos;
        this.newYPos = newYPos;
        this.jumpDirection = Direction.LEFT;
        
        setKingDirection(xPos, yPos, newXPos, newYPos);
    }
    
    private void setKingDirection(int xPos, int yPos, int newXPos, int newYPos)
    {
    	int x = newXPos - xPos;
        int y = newYPos - yPos;
        
        if(x == 0)
        {
        	//NOWHERE
        	if(y == 0)
        	{
        		//NOWHERE
        		kingDirection = KingDirection.NONE;
        	}
        	else if(y > 0)
        	{
        		//SOUTH
        		kingDirection = KingDirection.SOUTH;
        	}
        	else
        	{
        		//y < 0
        		kingDirection = KingDirection.NORTH;
        	}
        }
        else if(x > 0)
        {
        	//EAST
        	if(y == 0)
        	{
        		kingDirection = KingDirection.EAST;
        	}
        	else if(y > 0)
        	{
        		//SOUTH
        		kingDirection = KingDirection.SOUTHEAST;
        	}
        	else
        	{
        		//y < 0
        		kingDirection = KingDirection.NORTHEAST;
        	}
        }
        else
        {
        	//x < 0
        	//WEST
        	if(y == 0)
        	{
        		kingDirection = KingDirection.WEST;
        	}
        	else if(y > 0)
        	{
        		kingDirection = KingDirection.SOUTHWEST;
        	}
        	else
        	{
        		//y < 0
        		kingDirection = KingDirection.NORTHWEST;
        	}
        }
    }

    public int getxPos()
    {
        return xPos;
    }

    public int getyPos()
    {
        return yPos;
    }

    public int getNewXPos()
    {
        return newXPos;
    }

    public int getNewYPos()
    {
        return newYPos;
    }

    public Direction getJumpDirection()
    {
        return jumpDirection;
    }
    
    public KingDirection getKingDirection()
    {
		return kingDirection;
	}

    @Override
    public String toString()
    {
        return "(" + xPos + "," + yPos + ") -> (" + newXPos + "," + newYPos + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj.getClass().getSimpleName().equals("Move"))
        {
            Move move = ((Move)obj);

            if(move.getxPos() == getxPos() && move.getyPos() == getyPos()
                    && move.getNewXPos() == getNewXPos() && move.getNewYPos() == getNewYPos()
                    && move.getKingDirection() == getKingDirection()
                    && move.getJumpDirection() == jumpDirection)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
