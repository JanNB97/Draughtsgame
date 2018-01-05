package gameModel.board;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;

import gameModel.Move;
import gameModel.Piece;
import gameModel.enums.KingDirection;
import gameModel.enums.Owner;
import gameModel.enums.Type;

public class KingMaster 
{
	private MyBoard board;
	
	public KingMaster(MyBoard board)
	{
		this.board = board;
	}

	public TreeSet<Piece> isPossibleKingMove(Move move)
	{
		//Try to jump in all four directions
		Piece piece = board.getPiece(move.getxPos(), move.getyPos());
		Owner owner = piece.getOwner();

		board.clearPiece(piece.getxPos(), piece.getyPos());

		IsCorrectMove c1 = jumpToDestination(new TreeSet<>(), owner, KingDirection.NORTHEAST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		if(c1.bool() == true)
		{
			board.setOnBoard(owner, Type.KING, piece.getxPos(), piece.getyPos());
			return c1.getAllVictims();
		}
		IsCorrectMove c2 = jumpToDestination(new TreeSet<>(), owner, KingDirection.NORTHWEST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		if(c2.bool() == true)
		{
			board.setOnBoard(owner, Type.KING, piece.getxPos(), piece.getyPos());
			return c2.getAllVictims();
		}
		IsCorrectMove c3 = jumpToDestination(new TreeSet<>(), owner, KingDirection.SOUTHWEST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		if(c3.bool() == true)
		{
			board.setOnBoard(owner, Type.KING, piece.getxPos(), piece.getyPos());
			return c3.getAllVictims();
		}
		IsCorrectMove c4 = jumpToDestination(new TreeSet<>(), owner, KingDirection.SOUTHEAST, move.getxPos(), move.getyPos(), move.getNewXPos(), move.getNewYPos(), true);
		if(c4.bool() == true)
		{
			board.setOnBoard(owner, Type.KING, piece.getxPos(), piece.getyPos());
			return c4.getAllVictims();
		}

		board.setOnBoard(owner, Type.KING, piece.getxPos(), piece.getyPos());
		return null;
	}

	private IsCorrectMove jumpToDestination(TreeSet<Position> jumpedPieces, Owner owner, KingDirection direction, int xPos, int yPos, int destX, int destY, boolean start)
	{
		if(xPos < 0 || xPos > 7 || yPos < 0 || yPos > 7 || 
				(board.getPiece(xPos, yPos) != null && start == false))
		{
			return new IsCorrectMove(false);
		}
		
		if(xPos == destX && yPos == destY && couldStillJump(xPos, yPos, jumpedPieces, owner) == false && start == false)
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

		IsCorrectMove c1 = jumpToDestination(new TreeSet<>(jumpedPieces), owner, KingDirection.NORTHEAST, newXPos, newYPos, destX, destY, false);
		if(c1.bool() == true)
		{
			return c1;
		}
		IsCorrectMove c2 = jumpToDestination(new TreeSet<>(jumpedPieces), owner, KingDirection.NORTHWEST, newXPos, newYPos, destX, destY, false);
		if(c2.bool() == true)
		{
			return c2;
		}
		IsCorrectMove c3 = jumpToDestination(new TreeSet<>(jumpedPieces), owner, KingDirection.SOUTHEAST, newXPos, newYPos, destX, destY, false);
		if(c3.bool() == true)
		{
			return c3;
		}
		IsCorrectMove c4 =  jumpToDestination(new TreeSet<>(jumpedPieces), owner, KingDirection.SOUTHWEST, newXPos, newYPos, destX, destY, false);
		if(c4.bool() == true)
		{
			return c4;
		}

		return new IsCorrectMove(false);
	}
	private boolean isDirectReachable(TreeSet<Position> jumpedPieces, Move move)
	{
		if(move.getKingDirection() == KingDirection.NONE)
		{
			return false;
		}

		if(isOnDiagonale(move))
		{
			Piece nextStone = getNextStone(jumpedPieces, move.getxPos(), move.getyPos(), move.getKingDirection());
			
			if(nextStone != null)
			{
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
				return true;
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
				return null;
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

		public TreeSet<Piece> getAllVictims()
		{
			return victims;
		}
	}
	private boolean couldStillJump(int xPos, int yPos, TreeSet<Position> victims, Owner owner)
	{
		Piece pieceNE = getNextStone(victims, xPos, yPos, KingDirection.NORTHEAST);
		Piece pieceSE = getNextStone(victims, xPos, yPos, KingDirection.SOUTHEAST);
		Piece pieceSW = getNextStone(victims, xPos, yPos, KingDirection.SOUTHWEST);
		Piece pieceNW = getNextStone(victims, xPos, yPos, KingDirection.NORTHWEST);

		if(pieceNE != null && pieceNE.getxPos() + 1 <= 7 && pieceNE.getyPos() - 1 >= 0)
		{
			Piece afterNE = board.getPiece(pieceNE.getxPos() + 1, pieceNE.getyPos() - 1);
			if(pieceNE.getOwner() != owner && afterNE == null)
			{
				return true;
			}
		}

		if(pieceSE != null && pieceSE.getxPos() + 1 <= 7 && pieceSE.getyPos() + 1 <= 7)
		{
			Piece afterSE = board.getPiece(pieceSE.getxPos() + 1, pieceSE.getyPos() + 1);
			if (pieceSE.getOwner() != owner && afterSE == null) {
				return true;
			}
		}

		if(pieceSW != null && pieceSW.getxPos() - 1 >= 0 && pieceSW.getyPos() + 1 <= 7)
		{
			Piece afterSW = board.getPiece(pieceSW.getxPos() - 1, pieceSW.getyPos() + 1);
			if (pieceSW.getOwner() != owner && afterSW == null) {
				return true;
			}
		}

		if(pieceNW != null && pieceNW.getxPos() - 1 >= 0  && pieceNW.getyPos() - 1 >= 0)
		{
			Piece afterNW = board.getPiece(pieceNW.getxPos() - 1, pieceNW.getyPos() - 1);
			if (pieceNW.getOwner() != owner && afterNW == null) {
				return true;
			}
		}

		return false;
	}



	public ArrayList<Move> getAllJumpMoves(Piece piece)
	{
		ArrayList<Move> allJumps = new ArrayList<>();

		//row
		for(int i = 0; i < 8; i++)
		{
			int yPos = i;

			//column
			for(int j = 0; j < 8; j += 2)
			{
				int xPos = j;

				if(i % 2 == 0)
				{
					xPos++;
				}

				Move move = new Move(piece.getxPos(), piece.getyPos(), xPos, yPos);
				TreeSet<Piece> victims = board.isPossibleMove(move);
				if(victims != null && victims.size() > 0)
				{
					allJumps.add(move);
				}
			}
		}

		return allJumps;
	}

	public ArrayList<Move> getAllNormalMoves(Piece piece)
	{
		ArrayList<Move> allNormal = new ArrayList<>();

		int xPos = piece.getxPos();
		int yPos = piece.getyPos();

		for(int i = 1; i < 8; i++)
		{
			//Northeast
			Move move = new Move(xPos, yPos, xPos + i, yPos - i);
			TreeSet<Piece> victims = board.isPossibleMove(move);

			if(victims != null && victims.size() == 0)
			{
				allNormal.add(move);
			}

			//Southeast
			move = new Move(xPos, yPos, xPos + i, yPos + i);
			victims = board.isPossibleMove(move);

			if(victims != null && victims.size() == 0)
			{
				allNormal.add(move);
			}

			//Southwest
			move = new Move(xPos, yPos, xPos - i, yPos + i);
			victims = board.isPossibleMove(move);

			if(victims != null && victims.size() == 0)
			{
				allNormal.add(move);
			}


			//Northwest
			move = new Move(xPos, yPos, xPos - i, yPos - i);
			victims = board.isPossibleMove(move);

			if(victims != null && victims.size() == 0)
			{
				allNormal.add(move);
			}
		}

		return allNormal;
	}
}
