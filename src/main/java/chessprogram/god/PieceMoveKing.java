package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.*;
import static chessprogram.god.BitOperations.getAllPieces;
import static chessprogram.god.MoveConstantsKing.*;

class PieceMoveKing {

    static long singleKingTable(long piece, long mask){
        return KING_MOVE_TABLE[getIndexOfFirstPiece(piece)] & mask;
    }

    static long masterAttackTableKing(Chessboard board, boolean white,
                                             long ignoreThesePieces, long legalPushes, long legalCaptures){

        long ans = 0, kings = white ? board.getWhiteKing() : board.getBlackKing();
        while (kings != 0) {
            final long king = BitOperations.getFirstPiece(kings);
            if ((king & ignoreThesePieces) == 0) {
                ans |= singleKingTable(king, legalPushes | legalCaptures);
            }
            kings &= kings - 1;
        }
        return ans;
    }

}
