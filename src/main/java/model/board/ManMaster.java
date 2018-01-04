package model.board;

import java.util.ArrayList;
import java.util.TreeSet;
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
	
	TreeSet<Piece> isPossibleManMove(Move move)
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
                return null;
            }
            else
            {
                //Moves in right direction
                if(yPos + 1 == newYPos && (xPos + 1 == newXPos || xPos - 1 == newXPos) )
                {
                    //Just moves
                    if(couldStillJump(xPos, yPos, owner) == false)
                    {
                        return new TreeSet<>();
                    }
                    else {
                        return null;
                    }
                }
                else
                {
                    //Jumped
                    if(couldStillJump(newXPos, newYPos, owner) == false)
                    {
                        return isPossibleManJump(move);
                    }
                    else {
                        return null;
                    }
                }
            }
        }
        else if(owner == Owner.NP)
        {
            //Moves up
            if(newYPos >= yPos)
            {
                return null;
            }
            else
            {
                if(yPos - 1 == newYPos && (xPos + 1 == newXPos || xPos - 1 == newXPos))
                {
                    //Just moves
                    if(couldStillJump(xPos, yPos, owner) == false)
                    {
                        return new TreeSet<>();
                    }
                    else {
                        return null;
                    }
                }
                else
                {
                    //Jumped
                    if(couldStillJump(newXPos, newYPos, owner) == false)
                    {
                        return isPossibleManJump(move);
                    }
                    else {
                        return null;
                    }
                }
            }
        }
        else {
            Logger.getGlobal().severe("No owner selected");
            return null;
        }
	}
    
    TreeSet<Piece> isPossibleManJump(Move move)
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
                TreeSet<Piece> right = getManJumpVictimsSearch(direction, owner , xPos, yPos, xPos - 2, yPos + 2, newXPos, newYPos);
                TreeSet<Piece> left = getManJumpVictimsSearch(direction, owner, xPos, yPos, xPos + 2, yPos + 2, newXPos, newYPos);

                return selectArrayList(left, right, direction);
            case NP:
                //Moves up
                TreeSet<Piece> right2 = getManJumpVictimsSearch(direction, owner , xPos, yPos, xPos + 2, yPos - 2, newXPos, newYPos);
                TreeSet<Piece> left2 = getManJumpVictimsSearch(direction, owner, xPos, yPos, xPos - 2, yPos - 2, newXPos, newYPos);

                return selectArrayList(left2, right2, direction);

            default:
                Logger.getGlobal().severe("No owner selected");
                return new TreeSet<>();
        }
    }
    
    private TreeSet<Piece> selectArrayList(TreeSet<Piece> left, TreeSet<Piece> right, Direction direction)
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
            return null;
        }
    }
    
    private TreeSet<Piece> getManJumpVictimsSearch(Direction direction, Owner owner, int xPos, int yPos, int newXPos, int newYPos, int destX, int destY)
    {
        if(xPos == destX && yPos == destY)
        {
            return new TreeSet<>();
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
            TreeSet<Piece> left = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos + 2, newYPos + 2, destX, destY);
            TreeSet<Piece> right = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos - 2, newYPos + 2, destX, destY);

            return selectTreeSetSearch(left, right, direction, xPos, yPos, newXPos, newYPos);
        }
        else if(owner == Owner.NP)
        {
            //moves up
            TreeSet<Piece> left = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos - 2, newYPos - 2, destX, destY);
            TreeSet<Piece> right = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos + 2, newYPos - 2, destX, destY);

            return selectTreeSetSearch(left, right, direction, xPos, yPos, newXPos, newYPos);
        }
        else
        {
            Logger.getGlobal().severe("No owner selected");
            return null;
        }
    }
    
    private TreeSet<Piece> selectTreeSetSearch(TreeSet<Piece> left, TreeSet<Piece> right, Direction direction, int xPos, int yPos, int newXPos, int newYPos)
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

    private boolean couldStillJump(int xPos, int yPos, Owner owner)
    {
        Piece pieceRight;
        Piece pieceRightNext;
        Piece pieceLeft;
        Piece pieceLeftNext;

        switch (owner)
        {
            case PERSON:
                //Moves down
                if(xPos - 2 >= 0 && yPos + 2 <= 7)
                {
                    pieceRight = board.getPiece(xPos - 1, yPos + 1);
                    pieceRightNext = board.getPiece(xPos - 2, yPos + 2);
                }
                else
                {
                    pieceRight = null;
                    pieceRightNext = null;
                }

                if(xPos + 2 <= 7 && yPos + 2 <= 7)
                {
                    pieceLeft = board.getPiece(xPos + 1, yPos + 1);
                    pieceLeftNext = board.getPiece(xPos + 2, yPos + 2);
                }
                else
                {
                    pieceLeft = null;
                    pieceLeftNext = null;
                }

                if(pieceRight != null && pieceRight.getOwner() == Owner.NP && pieceRightNext == null
                        || pieceLeft != null && pieceLeft.getOwner() == Owner.NP && pieceLeftNext == null)
                {
                    return true;
                }
                else
                {
                    return false;
                }

            case NP:
                //Moves up
                if(xPos + 2 <= 7 && yPos - 2 >= 0)
                {
                    pieceRight = board.getPiece(xPos + 1, yPos - 1);
                    pieceRightNext = board.getPiece(xPos + 2, yPos - 2);
                }
                else
                {
                    pieceRight = null;
                    pieceRightNext = null;
                }

                if(xPos - 2 >= 0 && yPos - 2 >= 0)
                {
                    pieceLeft = board.getPiece(xPos - 1, yPos - 1);
                    pieceLeftNext = board.getPiece(xPos - 2, yPos - 2);
                }
                else
                {
                    pieceLeft = null;
                    pieceLeftNext = null;
                }

                if(pieceRight != null && pieceRight.getOwner() == Owner.PERSON && pieceRightNext == null
                        || pieceLeft != null && pieceLeft.getOwner() == Owner.PERSON && pieceLeftNext == null)
                {
                    return true;
                }
                else
                {
                    return false;
                }

            default:
                Logger.getGlobal().severe("Selected no owner");
                return false;
        }


    }



    ArrayList<Move> getAllNormalMoves(Piece piece)
    {
        ArrayList<Move> allMoves = new ArrayList<>();

        int xPos = piece.getxPos();
        int yPos = piece.getyPos();

        int TYPE;

        switch (piece.getOwner())
        {
            case PERSON:
                TYPE = -1;
                break;
            case NP:
                TYPE = 1;
                break;

            default:
                Logger.getGlobal().severe("No owner selected");
                return null;
        }

        //No jumps
        Move simpleMoveLeft1 = new Move(xPos, yPos, xPos - 1, yPos - 1*TYPE, Direction.LEFT);
        Move simpleMoveLeft2 = new Move(xPos, yPos, xPos - 1, yPos - 1*TYPE, Direction.RIGHT);
        if(board.isPossibleMove(simpleMoveLeft1) != null)
        {
            allMoves.add(simpleMoveLeft1);
            allMoves.add(simpleMoveLeft2);
        }
        Move simpleMoveRight1 = new Move(xPos, yPos, xPos + 1, yPos - 1*TYPE, Direction.LEFT);
        Move simpleMoveRight2 = new Move(xPos, yPos, xPos + 1, yPos - 1*TYPE, Direction.RIGHT);
        if(board.isPossibleMove(simpleMoveRight1) != null)
        {
            allMoves.add(simpleMoveRight1);
            allMoves.add(simpleMoveRight2);
        }

        return allMoves;
    }

    ArrayList<Move> getAllJumpMoves(Piece piece)
    {
        ArrayList<Move> allMoves = new ArrayList<>();

        int xPos = piece.getxPos();
        int yPos = piece.getyPos();

        int TYPE;

        switch (piece.getOwner())
        {
            case PERSON:
                //Moves down
                TYPE = -1;
                break;
            case NP:
                //Moves up
                TYPE = 1;
                break;

            default:
                Logger.getGlobal().severe("No owner selected");
                return null;
        }

        //Jump moves
        //Row 1
        Move moveOne1 = new Move(xPos, yPos, xPos - 2, yPos - 2*TYPE, Direction.RIGHT);
        Move moveOne2 = new Move(xPos, yPos, xPos - 2, yPos - 2*TYPE, Direction.LEFT);
        Move moveTwo1 = new Move(xPos, yPos, xPos + 2, yPos - 2*TYPE, Direction.RIGHT);
        Move moveTwo2 = new Move(xPos, yPos, xPos + 2, yPos - 2*TYPE, Direction.LEFT);

        if(board.isPossibleMove(moveOne1) != null)
        {
            allMoves.add(moveOne1);
            allMoves.add(moveOne2);
        }
        if(board.isPossibleMove(moveTwo1) != null)
        {
            allMoves.add(moveTwo1);
            allMoves.add(moveTwo2);
        }

        int row2Start = xPos - 4;
        for(int i = 0; i < 4; i++)
        {
            Move move1 = new Move(xPos, yPos, row2Start + 4* i, yPos - TYPE*4, Direction.LEFT);
            Move move2 = new Move(xPos, yPos, row2Start + 4* i, yPos - TYPE*4, Direction.RIGHT);

            if(board.isPossibleMove(move1) != null)
            {
                allMoves.add(move1);
                allMoves.add(move2);
            }
        }

        int row3Start = xPos - 6;
        for(int i = 0; i < 5; i++)
        {
            Move move1 = new Move(xPos, yPos, row3Start + 4* i, yPos - 6*TYPE, Direction.LEFT);
            Move move2 = new Move(xPos, yPos, row3Start + 4* i, yPos - 6*TYPE, Direction.RIGHT);

            if(board.isPossibleMove(move1) != null)
            {
                allMoves.add(move1);
                allMoves.add(move2);
            }
        }

        return allMoves;
    }
}
