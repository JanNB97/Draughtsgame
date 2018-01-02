package model;

import model.enums.Owner;
import model.enums.Type;

public class Piece implements Comparable<Piece>
{
    private Owner owner;
    private Type type;
    private int xPos;
    private int yPos;

    public Piece(Owner owner, Type type, int xPos, int yPos)
    {
        this.owner = owner;
        setType(type);
        setxPos(xPos);
        setyPos(yPos);
    }

    public Owner getOwner()
    {
        return owner;
    }

    public int getxPos()
    {
        return xPos;
    }

    public void setxPos(int xPos)
    {
        this.xPos = xPos;
    }

    public int getyPos()
    {
        return yPos;
    }

    public void setyPos(int yPos)
    {
        this.yPos = yPos;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj.getClass().getSimpleName().equals("Piece"))
        {
            Piece piece = (Piece)obj;

            return piece.getOwner() == owner && piece.getxPos() == xPos && piece.getyPos() == yPos && piece.getType() == type;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "(" + xPos + "," + yPos + ")";
    }

    @Override
    public int compareTo(Piece o)
    {
        if(yPos != o.getyPos())
        {
            return yPos - o.getyPos();
        }
        else
        {
            return xPos - o.getxPos();
        }
    }
}
