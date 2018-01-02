package model.board;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;
import model.Move;
import model.Piece;
import model.enums.KingDirection;
import model.enums.Owner;

public class KingMaster 
{
	private Board board;
	
	public KingMaster(Board board)
	{
		this.board = board;
	}


	boolean isPossibleKingMove(Move move)
	{
		//Try to reach direct
		if(isDirectReachable(new TreeSet<>(), move))
		{
			return true;
		}
		else
		{
			//Try to jump in all four directions
			Owner owner = board.getPiece(move.getxPos(), move.getyPos()).getOwner();

			return jumpToDestination(new TreeSet<>(), owner, KingDirection.NORTHEAST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true).bool()
					|| jumpToDestination(new TreeSet<>(), owner, KingDirection.NORTHWEST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true).bool()
					|| jumpToDestination(new TreeSet<>(), owner, KingDirection.SOUTHWEST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true).bool()
					|| jumpToDestination(new TreeSet<>(), owner, KingDirection.SOUTHEAST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true).bool();
		}
	}
	
	private IsCorrectMove jumpToDestination(TreeSet<Position> jumpedPieces, Owner owner, KingDirection direction, int xPos, int yPos, int destX, int destY, boolean start)
	{
		if(xPos < 0 || xPos > 7 || yPos < 0 || yPos > 7 || 
				(board.getPiece(xPos, yPos) != null && start == false))
		{
			return new IsCorrectMove(false);
		}
		
		if(xPos == destX && yPos == destY)
		{
			IsCorrectMove isCorrectMove = new IsCorrectMove(true);
			isCorrectMove.addAll(jumpedPieces);

			return isCorrectMove;
		}
		
		Piece piece;
		
		switch(direction)
		{
			case NORTHEAST:
				piece = getNextStone(jumpedPieces, xPos, yPos, KingDirection.NORTHEAST);
				
				if(piece != null && piece.getOwner() != owner)
				{
					int newXPos = piece.getxPos() + 1;
					int newYPos = piece.getyPos() - 1;
					
					return jumpToDestinationHelp(piece, jumpedPieces, owner, newXPos, newYPos, destX, destY);
				}
				else
				{
					return new IsCorrectMove(false);
				}
			case SOUTHEAST:
				piece = getNextStone(jumpedPieces, xPos, yPos, KingDirection.SOUTHEAST);
				
				if(piece != null && piece.getOwner() != owner)
				{
					int newXPos = piece.getxPos() + 1;
					int newYPos = piece.getyPos() + 1;

					return jumpToDestinationHelp(piece, jumpedPieces, owner, newXPos, newYPos, destX, destY);
				}
				else
				{
					return new IsCorrectMove(false);
				}
			case NORTHWEST:
				piece = getNextStone(jumpedPieces, xPos, yPos, KingDirection.NORTHWEST);
				
				if(piece != null && piece.getOwner() != owner)
				{
					int newXPos = piece.getxPos() - 1;
					int newYPos = piece.getyPos() - 1;

					return jumpToDestinationHelp(piece, jumpedPieces, owner, newXPos, newYPos, destX, destY);
				}
				else
				{
					return new IsCorrectMove(false);
				}
			case SOUTHWEST:
				piece = getNextStone(jumpedPieces, xPos, yPos, KingDirection.SOUTHWEST);

				if(piece != null && piece.getOwner() != owner)
				{
					int newXPos = piece.getxPos() - 1;
					int newYPos = piece.getyPos() + 1;

					return jumpToDestinationHelp(piece, jumpedPieces, owner, newXPos, newYPos, destX, destY);
				}
				else
				{
					return new IsCorrectMove(false);
				}
			case EAST: case NORTH: case SOUTH: case WEST:
				Logger.getGlobal().severe("Wrong direction selected");
				return new IsCorrectMove(false);
			default:
				Logger.getGlobal().severe("No direction selected");
				return new IsCorrectMove(false);
		}
	}
	private IsCorrectMove jumpToDestinationHelp(Piece piece, TreeSet<Position> jumpedPieces, Owner owner, int newXPos, int newYPos, int destX, int destY)
	{
		Position victimPos = new Position(piece.getxPos(), piece.getyPos());

		jumpedPieces.add(victimPos);

		IsCorrectMove c1 = jumpToDestination(jumpedPieces, owner, KingDirection.NORTHEAST, newXPos, newYPos, destX, destY, false);
		IsCorrectMove c2 = jumpToDestination(jumpedPieces, owner, KingDirection.NORTHWEST, newXPos, newYPos, destX, destY, false);
		IsCorrectMove c3 = jumpToDestination(jumpedPieces, owner, KingDirection.SOUTHEAST, newXPos, newYPos, destX, destY, false);
		IsCorrectMove c4 =  jumpToDestination(jumpedPieces, owner, KingDirection.SOUTHWEST, newXPos, newYPos, destX, destY, false);

		if(c1.bool() == true)
		{
			return c1;
		}
		else if(c2.bool() == true)
		{
			return c2;
		}
		else if(c3.bool() == true)
		{
			return c3;
		}
		else if(c4.bool() == true)
		{
			return c4;
		}
		else
		{
			return new IsCorrectMove(false);
		}
	}

	private boolean isDirectReachable(TreeSet<Position> jumpedPieces, Move move)
	{
		if(isOnDiagonale(move))
		{
			Piece nextStone = getNextStone(jumpedPieces, move.getxPos(), move.getyPos(), move.getKingDirection());
			
			int disToNextStone = distance(move.getxPos(), move.getyPos(), nextStone.getxPos(), nextStone.getyPos());
			int disToDestination = distance(move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos());
			
			if(disToDestination < disToNextStone)
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
	private boolean isOnDiagonale(Move move)
	{
		int x = Math.abs(move.getxPos() - move.getNewXPos());
		int y = Math.abs(move.getyPos() - move.getNewYPos());
		
		if(x == y)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private int distance(int x1, int y1, int x2, int y2)
    {
    	return (int) Math.sqrt((int)Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    private Piece getNextStone(TreeSet<Position> jumpedPieces, int xPos, int yPos, KingDirection direction)
    {
    	int x = xPos;
    	int y = yPos;
    	
    	while(x >= 0 && x <= 7 && y >= 0 && y <= 7)
    	{
    		switch(direction)
    		{
    		case NORTHEAST:
    			x = x + 1;
    			y = y - 1;
    			break;
    		case SOUTHEAST:
    			x = x + 1;
    			y = y + 1;
    			break;
    		case SOUTHWEST:
    			x = x - 1;
    			y = y + 1;
    			break;
    		case NORTHWEST:
    			x = x - 1;
    			y = y - 1;
    			break;
			default:
				Logger.getGlobal().severe("No kingDirection selected");
				break;
    		}

    		if(x >= 0 && x <= 7 && y >= 0 && y <= 7)
    		{
				Piece piece = board.getPiece(x, y);
				if (piece != null && jumpedPieces.contains(new Position(x, y)) == false)
				{
					return piece;
				}
			}
    	}
    	
    	return null;
    }
	private class Position implements Comparable<Position>
	{
		private int xPos;
		private int yPos;

		public Position(int xPos, int yPos)
		{
			this.xPos = xPos;
			this.yPos = yPos;
		}

		public int getXPos()
		{
			return xPos;
		}

		public int getyPos()
		{
			return yPos;
		}

		@Override
		public boolean equals(Object obj)
		{
			return obj.getClass().getSimpleName().equals("Position")
					&& xPos == ((Position)obj).getXPos() && yPos == ((Position)obj).getyPos();
		}

		@Override
		public int compareTo(Position o)
		{
			if(yPos != o.getyPos())
			{
				return yPos - o.getyPos();
			}
			else
			{
				return xPos - o.getXPos();
			}
		}
	}
	private class IsCorrectMove
	{
		TreeSet<Piece> victims;
		boolean isCorrectMove;

		public IsCorrectMove(boolean isCorrectMove)
		{
			this.isCorrectMove = isCorrectMove;
			victims = new TreeSet<>();
		}

		public void addAll(TreeSet<Position> positions)
		{
			for(Position position : positions)
			{
				victims.add(board.getPiece(position.getXPos(), position.getyPos()));
			}
		}

		public boolean bool()
		{
			return isCorrectMove;
		}

		public ArrayList<Piece> getAllVictims()
		{
			ArrayList<Piece> pieces = new ArrayList<>();

			for(Piece victim : victims)
			{
				pieces.add(victim);
			}

			return pieces;
		}
	}


	public ArrayList<Piece> getKingJumpVictims(Move move)
	{
		//Try to jump in all four directions
		Owner owner = board.getPiece(move.getxPos(), move.getyPos()).getOwner();

		IsCorrectMove c1 = jumpToDestination(new TreeSet<>(), owner, KingDirection.NORTHEAST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		IsCorrectMove c2 = jumpToDestination(new TreeSet<>(), owner, KingDirection.NORTHWEST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		IsCorrectMove c3 = jumpToDestination(new TreeSet<>(), owner, KingDirection.SOUTHWEST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		IsCorrectMove c4 = jumpToDestination(new TreeSet<>(), owner, KingDirection.SOUTHEAST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);

		if(c1.bool() == true)
		{
			return c1.getAllVictims();
		}
		else if(c2.bool() == true)
		{
			return c2.getAllVictims();
		}
		else if(c3.bool() == true)
		{
			return c3.getAllVictims();
		}
		else if(c4.bool() == true)
		{
			return c4.getAllVictims();
		}
		else
		{
			return new ArrayList<>();
		}
	}
}
