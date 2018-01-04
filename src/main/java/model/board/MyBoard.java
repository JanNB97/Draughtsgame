package model.board;

import model.Move;
import model.Piece;
import model.enums.Direction;
import model.enums.KingDirection;
import model.enums.Owner;
import model.enums.Type;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;

public class MyBoard implements Board
{
    private Piece[][] board = new Piece[8][8];
    private ManMaster manMaster= new ManMaster(this);
    private KingMaster kingMaster = new KingMaster(this);

    
    
    public MyBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if((i == 0 || i == 2) && j % 2 == 1
                        || i == 1 && j % 2 == 0)
                {
                    Piece newPiece = new Piece(Owner.PERSON, Type.MAN, j, i);
                    board[i][j] = newPiece;
                }
                else if((i == 5 || i == 7) && j % 2 == 0
                        || i == 6 && j % 2 == 1)
                {
                    Piece newPiece = new Piece(Owner.NP, Type.MAN, j, i);
                    board[i][j] = newPiece;
                }
            }
        }
    }

    public MyBoard(Board board)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Piece piece = board.getPiece(j, i);

                if(piece != null)
                {
                    Piece newPiece = new Piece(piece.getOwner(), piece.getType(), piece.getxPos(), piece.getyPos());
                    this.board[i][j] = newPiece;
                }
            }
        }
    }

    
    
    @Override
    //Returns copy of piece
    public Piece getPiece(int xPos, int yPos)
    {
        Piece piece = board[yPos][xPos];

        if(piece != null)
        {
            return new Piece(piece.getOwner(), piece.getType(), piece.getxPos(), piece.getyPos());
        }

        return null;
    }
    
    @Override
    public TreeSet<Piece> getAllPieces(Owner owner)
    {
    	if(owner == null)
    	{
    		Logger.getGlobal().severe("No owner entered");
    		return null;
    	}
    	
    	TreeSet<Piece> allPieces = new TreeSet<>();
    	
    	for(int i = 0; i < 8; i++)
        {
        	for(int j = 0; j < 8; j++)
        	{
        		if(board[i][j] != null)
        		{
        			if(board[i][j].getOwner() == owner)
        			{
        				allPieces.add(board[i][j]);
        			}
        		}
        	}
        }
    	
    	return allPieces;
    }

    
    @Override
    public ArrayList<ArrayList<Move>> getAllMoves(Owner owner)
    {
        ArrayList<ArrayList<Move>> allMovesFromAllPieces = new ArrayList<>();
        TreeSet<Piece> allPieces = getAllPieces(owner);

        //First get Jumps
        for(Piece piece : allPieces)
        {
            ArrayList<Move> allJumpMoves = getAllJumpMoves(piece);

            if(allJumpMoves.size() > 0)
            {
                allMovesFromAllPieces.add(allJumpMoves);
            }
        }

        if(allMovesFromAllPieces.size() == 0)
        {
            //After that if no jumps get normal moves
            for(Piece piece : allPieces)
            {
                ArrayList<Move> allNormalMoves = getAllNormalMoves(piece);

                if(allNormalMoves.size() > 0)
                {
                    allMovesFromAllPieces.add(allNormalMoves);
                }
            }
        }


        return allMovesFromAllPieces;
    }

    private ArrayList<Move> getAllNormalMoves(Piece piece)
    {
        ArrayList<Move> allMoves = new ArrayList<>();

        switch (piece.getType())
        {
            case KING:
                //TODO
                return null;
            case MAN:
                return getAllNormalManMoves(piece);
            default:
                Logger.getGlobal().severe("No type selected");
                return null;
        }
    }

    private ArrayList<Move> getAllJumpMoves(Piece piece)
    {
        ArrayList<Move> allMoves = new ArrayList<>();

        switch (piece.getType())
        {
            case KING:
                //TODO
                return null;
            case MAN:
                return getAllJumpManMoves(piece);
            default:
                Logger.getGlobal().severe("No type selected");
                return null;
        }
    }

    private ArrayList<Move> getAllNormalManMoves(Piece piece)
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

        //No jumps
        Move simpleMoveLeft1 = new Move(xPos, yPos, xPos - 1, yPos - 1*TYPE, Direction.LEFT);
        Move simpleMoveLeft2 = new Move(xPos, yPos, xPos - 1, yPos - 1*TYPE, Direction.RIGHT);
        if(isPossibleMove(simpleMoveLeft1))
        {
            allMoves.add(simpleMoveLeft1);
            allMoves.add(simpleMoveLeft2);
        }
        Move simpleMoveRight1 = new Move(xPos, yPos, xPos + 1, yPos - 1*TYPE, Direction.LEFT);
        Move simpleMoveRight2 = new Move(xPos, yPos, xPos + 1, yPos - 1*TYPE, Direction.RIGHT);
        if(isPossibleMove(simpleMoveRight1))
        {
            allMoves.add(simpleMoveRight1);
            allMoves.add(simpleMoveRight2);
        }

        return allMoves;
    }

    private ArrayList<Move> getAllJumpManMoves(Piece piece)
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

        if(isPossibleMove(moveOne1))
        {
            allMoves.add(moveOne1);
            allMoves.add(moveOne2);
        }
        if(isPossibleMove(moveTwo1))
        {
            allMoves.add(moveTwo1);
            allMoves.add(moveTwo2);
        }

        int row2Start = xPos - 4;
        for(int i = 0; i < 4; i++)
        {
            Move move1 = new Move(xPos, yPos, row2Start + 4* i, yPos - TYPE*4, Direction.LEFT);
            Move move2 = new Move(xPos, yPos, row2Start + 4* i, yPos - TYPE*4, Direction.RIGHT);

            if(isPossibleMove(move1))
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

            if(isPossibleMove(move1))
            {
                allMoves.add(move1);
                allMoves.add(move2);
            }
        }

        return allMoves;
    }
  
    
    @Override
    public boolean isPossibleMove(Move move)
    {
        Piece piece = getPiece(move.getxPos(), move.getyPos());

        if(piece == null)
        {
            Logger.getGlobal().severe("prove possible move on null-pointer");
            return false;
        }

        Type type = piece.getType();
        Owner owner = piece.getOwner();
        int xPos = piece.getxPos();
        int yPos = piece.getyPos();
        int newXPos = move.getNewXPos();
        int newYPos = move.getNewYPos();

        if(newXPos < 0 || newXPos > 7 || newYPos < 0 || newYPos > 7
                || (getPiece(newXPos, newYPos) != null && getPiece(newXPos, newYPos).getType() != Type.KING))
        {
            return false;
        }

        if(type == Type.MAN)
        {
            return manMaster.isPossibleManMove(move);
        }
        else if(type == Type.KING) 
        {
            return kingMaster.isPossibleKingMove(move);
        }
        else {
            Logger.getGlobal().severe("No type selected");
            return false;
        }
    }
   
    //Only if it's a possible move!!!
    @Override
    public void doMove(Move move)
    {
        Piece piece = getPiece(move.getxPos(), move.getyPos());

        ArrayList<Piece> victims;
        if(piece.getType() == Type.MAN)
        {
            victims = manMaster.getManJumpVictims(move);
        }
        else if(piece.getType() == Type.KING)
        {
            victims = kingMaster.getKingJumpVictims(move);
        }
        else
        {
            Logger.getGlobal().severe("No type selected");
            victims = null;
        }

        //Set piece
        board[piece.getyPos()][piece.getxPos()] = null;

        piece.setxPos(move.getNewXPos());
        piece.setyPos(move.getNewYPos());

        board[piece.getyPos()][piece.getxPos()] = piece;
        
        //Change to king
        if(piece.getyPos() == 7 || piece.getyPos() == 0)
        {
        	piece.setType(Type.KING);
        }

        //Remove victims
        for(Piece victim : victims)
        {
            board[victim.getyPos()][victim.getxPos()] = null;
        }
    }

    @Override
    public void doAllMoves(Move...moves)
    {
    	for(Move move : moves)
    	{
    		doMove(move);
    	}
    }

    //Only if it's a possible move!!!
    @Override
    public Board tryMove(Move move)
    {
        Board board = new MyBoard(this);

        board.doMove(move);

        return board;
    }

    
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("   0 1 2 3 4 5 6 7\n0| ");

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(board[i][j] == null)
                {
                    builder.append("  ");
                }
                else
                {
                    switch (board[i][j].getOwner())
                    {
                        case NP:
                            builder.append("N ");
                            break;
                        case PERSON:
                            builder.append("P ");
                            break;

                    }
                }
            }

            builder.append("|\n");

            if(i != 7)
            {
                builder.append(i + 1 + "| ");
            }
        }
        return builder.toString();
    }

    //-----------------------------------------------------------------------------------------------//TESTING
    private void setPiece(Piece piece)
    {
    	board[piece.getyPos()][piece.getxPos()] = piece;
    }
    
    public void clearPiece(int xPos, int yPos)
    {
    	board[yPos][xPos] = null;
    }
    
    public void clearBoard()
    {
    	for(int i = 0; i < 8; i++)
    	{
    		for(int j = 0; j < 8; j++)
    		{
    			clearPiece(i, j);
    		}
    	}
    }
    
    public void setOnBoard(Owner owner, Type type, int...x)
    {
    	if(x.length % 2 != 0)
    	{
    		Logger.getGlobal().severe("Wrong number of positions");
    	}
    	
    	for(int i = 0; i < x.length; i += 2)
    	{
    		int xPos = x[i];
    		int yPos = x[i + 1];
    		
    		setPiece(new Piece(owner, type, xPos, yPos));
    	}
    }
}
