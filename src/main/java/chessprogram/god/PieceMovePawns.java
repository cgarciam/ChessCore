package chessprogram.god;

import java.util.List;

import static chessprogram.god.BitOperations.getAllPieces;

class PieceMovePawns {

    static long singlePawnPushes(Chessboard board, long piece, boolean white, long legalPushes) {
        long allPieces = board.whitePieces() | board.blackPieces();
        long HOME_RANK = (white) ? BitboardResources.RANK_TWO : BitboardResources.RANK_SEVEN;
        long answer = 0;
        long temp = piece;

        // promotion moves are handled elsewhere
        if (white) {
            do {
                if ((temp & BitboardResources.RANK_EIGHT) != 0) break;
                temp <<= 8;
                if ((temp & allPieces) != 0) break;
                answer |= temp;
            } while (((temp & BitboardResources.RANK_THREE) != 0));
        }
        else {
            do {
                if ((temp & BitboardResources.RANK_ONE) != 0) break;
                temp >>>= 8;
                if ((temp & allPieces) != 0) break;
                answer |= temp;
            } while (((temp & BitboardResources.RANK_SIX) != 0));
        }
        return answer & legalPushes;
    }

    public static long singlePawnCaptures(Chessboard board, long piece, boolean white, long legalCaptures) {
        long allPieces = board.whitePieces() | board.blackPieces();
        long answer = 0;

        if (piece == 0) {
            return 0;
        }
        if (white){
            int index = BitOperations.getIndexOfFirstPiece(piece);
            long l = PawnCaptureTable.PAWN_CAPTURE_TABLE_WHITE[index];
            answer |= l;
        }
        else{
            long table = 0;
            int index = BitOperations.getIndexOfFirstPiece(piece);
            long l = PawnCaptureTable.PAWN_CAPTURE_TABLE_BLACK[index];
            answer |= l;
        }

        return answer & legalCaptures;
    }

    public static long masterPawnCapturesTable(Chessboard board, boolean white,
                                               long ignoreThesePieces, long legalCaptures){
        long ans = 0, pawns;
        if (white){
            pawns = board.getWhitePawns();
        }
        else {
            pawns = board.getBlackPawns();
        }
        List<Long> allPawns = getAllPieces(pawns, ignoreThesePieces);
        for (Long piece : allPawns){
            ans |= singlePawnCaptures(board, piece, white, legalCaptures);
        }
        return ans;
    }

}
