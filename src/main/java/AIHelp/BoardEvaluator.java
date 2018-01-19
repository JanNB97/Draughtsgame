package AIHelp;

import gameModel.Piece;
import gameModel.board.Board;
import gameModel.enums.Owner;
import gameModel.enums.Type;

public class BoardEvaluator
{
    public static int evalutateBoard(Board board, Owner player, int KingMULT, float OwnMULT)
    {
        final int POINT = 8;

        int evaluation = 12*POINT + 12*KingMULT*POINT;

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Piece piece = board.getPiece(i, j);

                if(piece != null)
                {
                    if(piece.getType() == Type.MAN)
                    {
                        if(piece.getOwner() == player)
                        {
                            //Own stone
                            evaluation += OwnMULT*POINT;
                        }
                        else
                        {
                            //Opponents stone
                            evaluation -= POINT;
                        }
                    }
                    else if(piece.getType() == Type.KING)
                    {
                        if(piece.getOwner() == player)
                        {
                            //Own stone
                            evaluation += OwnMULT * KingMULT * POINT;
                        }
                        else
                        {
                            //Opponents stone
                            evaluation -= KingMULT * POINT;
                        }
                    }
                }
            }
        }

        return evaluation;
    }
}
