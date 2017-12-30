package model;

import model.enums.Direction;
import model.enums.Owner;
import model.enums.Type;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MyBoard implements Board
{
    private Piece[][] board = new Piece[8][8];
    private ArrayList<Piece> personPieces = new ArrayList<>();
    private ArrayList<Piece> NPPieces = new ArrayList<>();

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
                    personPieces.add(newPiece);
                }
                else if((i == 5 || i == 7) && j % 2 == 0
                        || i == 6 && j % 2 == 1)
                {
                    Piece newPiece = new Piece(Owner.NP, Type.MAN, j, i);
                    board[i][j] = newPiece;
                    NPPieces.add(newPiece);
                }
            }
        }
    }

    public MyBoard(MyBoard board)
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

                    if(newPiece.getOwner() == Owner.PERSON)
                    {
                        personPieces.add(newPiece);
                    }
                    else
                    {
                        NPPieces.add(newPiece);
                    }
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
    public ArrayList<Piece> getAllPieces(Owner owner)
    {
        return null;
    }

    @Override
    public ArrayList<Move> getAllMoves(Piece piece)
    {
        return null;
    }

    @Override
    public ArrayList<Move> getAllMoves(Owner owner)
    {
        return null;
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
                || getPiece(newXPos, newYPos) != null)
        {
            return false;
        }

        if(type == Type.MAN)
        {
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
        else if(type == Type.KING) {
            //TODO
            return false;
        }
        else {
            Logger.getGlobal().severe("No type selected");
            return false;
        }
    }

    //In the right direction, don't tries to move out the board, free destination place, tries to jump
    private boolean isPossibleManJump(Move move)
    {
        Piece piece = getPiece(move.getxPos(), move.getyPos());

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
                || getPiece(newXPos, newYPos) != null
                || getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2) == null
                || getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2).getOwner() == owner)
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

    //Only if it's a possible move!!!
    @Override
    public void doMove(Move move)
    {
        ArrayList<Piece> victims = getManJumpVictims(move);

        Piece piece = getPiece(move.getxPos(), move.getyPos());


        board[piece.getyPos()][piece.getxPos()] = null;

        piece.setxPos(move.getNewXPos());
        piece.setyPos(move.getNewYPos());

        board[piece.getyPos()][piece.getxPos()] = piece;

        for(Piece victim : victims)
        {
            board[victim.getyPos()][victim.getxPos()] = null;
        }
    }

    private ArrayList<Piece> getManJumpVictims(Move move)
    {
        Piece piece = getPiece(move.getxPos(), move.getyPos());
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
                || right != null && left != null)
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
                || getPiece(newXPos, newYPos) != null
                || getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2) == null
                || getPiece((xPos + newXPos) / 2, (yPos + newYPos) / 2).getOwner() == owner)
        {
            return null;
        }

        if(owner == Owner.NP)
        {
            //moves up
            ArrayList<Piece> right = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos + 2, newYPos - 2, destX, destY);
            ArrayList<Piece> left = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos - 2, newYPos - 2, destX, destY);

            if(right != null && left != null && direction == Direction.LEFT
                    || right == null && left != null)
            {
                left.add(getPiece(newXPos + 1, newYPos + 1));
                return left;
            }
            else if(right != null && left != null && direction == Direction.RIGHT
                    || right != null && left != null)
            {
                right.add(getPiece(newXPos - 1, newYPos + 1));
                return right;
            }
            else
            {
                return null;
            }
        }
        else if(owner == Owner.PERSON)
        {
            //moves down
            ArrayList<Piece> right = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos + 2, newYPos + 2, destX, destY);
            ArrayList<Piece> left = getManJumpVictimsSearch(direction, owner, newXPos, newYPos, newXPos - 2, newYPos + 2, destX, destY);

            if(right != null && left != null && direction == Direction.LEFT
                    || right == null && left != null)
            {
                left.add(getPiece(newXPos - 1, newYPos - 1));
                return left;
            }
            else if(right != null && left != null && direction == Direction.RIGHT
                    || right != null && left != null)
            {
                right.add(getPiece(newXPos + 1, newYPos - 1));
                return right;
            }
            else
            {
                return null;
            }
        }
        else
        {
            Logger.getGlobal().severe("No owner selected");
            return null;
        }
    }

    //Only if it's a possible move!!!
    @Override
    public Board tryMove(Move move)
    {
        return null;
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
}
