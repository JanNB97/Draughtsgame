package model.board;

import java.util.ArrayList;
import java.util.logging.Logger;

import model.Move;
import model.Piece;
import model.enums.Direction;
import model.enums.Owner;

public class ManMaster 
{
	private Board board;
	
	public ManMaster(Board board)
	{
		this.board = board;
	}
	
	boolean isPossibleManMove(Move move)
	{
		Piece piece = board.getPiece(move.getxPos(), move.getyPos());
		Owner owner = piece.getOwner();
        int xPos = piece.getxPos();
        int yPos = piece.getyPos();
        int newXPos = move.getNewXPos();
        int newYPos = move.getNewYPos();
		
		if(owner == Owner.PERSON)
        {
            //Moves down
            if(newYPos <= yPos)
            {
                return false;
            }
            else
            {
                if(yPos + 1 == newYPos && (xPos + 1 == newXPos || xPos - 1 == newXPos))
                {
                    return true;
                }
                else
                {
                    //Jumped
                    return isPossibleManJump(move);
                }
            }
        }
        else if(owner == Owner.NP)
        {
            //Moves up
            if(newYPos >= yPos)
            {
                return false;
            }
            else
            {
                if(yPos - 1 == newYPos && (xPos + 1 == newXPos || xPos - 1 == newXPos))
                {
                    return true;
                }
                else
                {
                    //Jumped
                    return isPossibleManJump(move);
                }
            }
        }
        else {
            Logger.getGlobal().severe("No owner selected");
            return false;
        }
	}
	
	//In the right direction, don't tries to move out the board, free destination place, tries to jump
    private boolean isPossibleManJump(Move move)
    {
        Piece piece = board.getPiece(move.getxPos(), move.getyPos());

        switch (piece.getOwner())
        {
            case PERSON:
                //Moves down
                return isPossibleManJumpSearch(Owner.PERSON, piece.getxPos(), piece.getyPos(), piece.getxPos() - 2, piece.getyPos() + 2, move.getNewXPos(), move.getNewYPos())
                    || isPossibleManJumpSearch(Owner.PERSON, piece.getxPos(), piece.getyPos(), piece.getxPos() + 2, piece.getyPos() + 2, move.getNewXPos(), move.getNewYPos());
            case NP:
                //Moves up
                return isPossibleManJumpSearch(Owner.NP, piece.getxPos(), piece.getyPos(), piece.getxPos() - 2, piece.getyPos() - 2, move.getNewXPos(), move.getNewYPos())
                    || isPossibleManJumpSearch(Owner.NP, piece.getxPos(), piece.getyPos(), piece.getxPos() + 2, piece.getyPos() - 2, move.getNewXPos(), move.getNewYPos());
            default:
                Logger.getGlobal().severe("No owner selected");
                return false;
        }
    }
    
    private boolean isPossibleManJumpSearch(Owner owner, int xPos, int yPos, int newXPos, int newYPos, int destX, int destY)
    {
        if(xPos == destX && yPos == destY)
        {
            return true;
        }
        else if(owner == Owner.PERSON && destY < yPos
                || owner == Owner.NP && destY > yPos)
        {
            return false;
        }


        if(newXPos < 0 || newXPos > 7 || newYPos < 0 || newYPos > 7
                || board.getPiece(newXPos, newYPos) != null
                || board.getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2) == null
                || board.getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2).getOwner() == owner)
        {
            return false;
        }

        if(owner == Owner.NP)
        {
            //moves up
            return (isPossibleManJumpSearch(owner, newXPos, newYPos, newXPos + 2, newYPos - 2, destX, destY)
                    || isPossibleManJumpSearch(owner, newXPos, newYPos, newXPos - 2, newYPos - 2, destX, destY));
        }
        else if(owner == Owner.PERSON)
        {
            //moves down
            return (isPossibleManJumpSearch(owner, newXPos, newYPos, newXPos + 2, newYPos + 2, destX, destY)
                    || isPossibleManJumpSearch(owner, newXPos, newYPos, newXPos - 2, newYPos + 2, destX, destY));
        }
        else
        {
            Logger.getGlobal().severe("No owner selected");
            return false;
        }
    }
    
    ArrayList<Piece> getManJumpVictims(Move move)
    {
        Piece piece = board.getPiece(move.getxPos(), move.getyPos());
        Owner owner = piece.getOwner();
        Direction direction = move.getJumpDirection();
        int xPos = move.getxPos();
        int yPos = move.getyPos();
        int newXPos = move.getNewXPos();
        int newYPos = move.getNewYPos();

        switch (piece.getOwner())
        {
            case PERSON:
                //Moves down
                ArrayList<Piece> right = getManJumpVictimsSearch(direction, owner , xPos, yPos, xPos - 2, yPos + 2, newXPos, newYPos);
                ArrayList<Piece> left = getManJumpVictimsSearch(direction, owner, xPos, yPos, xPos + 2, yPos + 2, newXPos, newYPos);

                return selectArrayList(left, right, direction);
            case NP:
                //Moves up
                ArrayList<Piece> right2 = getManJumpVictimsSearch(direction, owner , xPos, yPos, xPos + 2, yPos - 2, newXPos, newYPos);
                ArrayList<Piece> left2 = getManJumpVictimsSearch(direction, owner, xPos, yPos, xPos - 2, yPos - 2, newXPos, newYPos);

                return selectArrayList(left2, right2, direction);

            default:
                Logger.getGlobal().severe("No owner selected");
                return new ArrayList<>();
        }
    }
    
    private ArrayList<Piece> selectArrayList(ArrayList<Piece> left, ArrayList<Piece> right, Direction direction)
    {
        if(right != null && left != null && direction == Direction.LEFT
                || right == null && left != null)
        {
            return left;
        }
        else if(right != null && left != null && direction == Direction.RIGHT
                || right != null && left == null)
        {
            return right;
        }
        else
        {
            return new ArrayList<>();
        }
    }
    
    private ArrayList<Piece> getManJumpVictimsSearch(Direction direction, Owner owner, int xPos, int yPos, int newXPos, int newYPos, int destX, int destY)
    {
        if(xPos == destX && yPos == destY)
        {
            return new ArrayList<>();
        }
        else if(owner == Owner.PERSON && destY < yPos
                || owner == Owner.NP && destY > yPos)
        {
            return null;
        }


        if(newXPos < 0 || newXPos > 7 || newYPos < 0 || newYPos > 7
                || board.getPiece(newXPos, newYPos) != null
                || board.getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2) == null
                || board.getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2).getOwner() == owner)
        {
            return null;
        }


        if(owner == Owner.PERSON)
        {
            //moves down
            ArrayList<Piece> left = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos + 2, newYPos + 2, destX, destY);
            ArrayList<Piece> right = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos - 2, newYPos + 2, destX, destY);

            return selectArrayListSearch(left, right, direction, xPos, yPos, newXPos, newYPos);
        }
        else if(owner == Owner.NP)
        {
            //moves up
            ArrayList<Piece> left = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos - 2, newYPos - 2, destX, destY);
            ArrayList<Piece> right = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos + 2, newYPos - 2, destX, destY);

            return selectArrayListSearch(left, right, direction, xPos, yPos, newXPos, newYPos);
        }
        else
        {
            Logger.getGlobal().severe("No owner selected");
            return null;
        }
    }
    
    private ArrayList<Piece> selectArrayListSearch(ArrayList<Piece> left, ArrayList<Piece> right, Direction direction, int xPos, int yPos, int newXPos, int newYPos)
    {
        if(right != null && left != null && direction == Direction.LEFT
                || right == null && left != null)
        {
            left.add(board.getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2));
            return left;
        }
        else if(right != null && left != null && direction == Direction.RIGHT
                || right != null && left == null)
        {
            right.add(board.getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2));
            return right;
        }
        else
        {
            return null;
        }
    }
}
