package model;

import model.enums.Direction;

public class Move
{
    private int xPos;
    private int yPos;
    private int newXPos;
    private int newYPos;
    private Direction jumpDirection;

    public Move(int xPos, int yPos, int newXPos, int newYPos, Direction jumpDirection)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.newXPos = newXPos;
        this.newYPos = newYPos;
        this.jumpDirection = jumpDirection;
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
}
