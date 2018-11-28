package chessprogram.god;

import java.util.ArrayList;
import java.util.List;

import static chessprogram.god.MoveConstants.*;
import static chessprogram.god.StackMoveData.SpecialMove;

class MoveGeneratorEnPassant {

    static void addEnPassantMoves(List<Move> moves, Chessboard board, boolean white,
                                  long ignoreThesePieces, long legalPushes, long legalCaptures) {
        List<Move> temp = new ArrayList<>();

        long myPawns = white ? board.getWhitePawns() : board.getBlackPawns();
        long enemyPawns = white ? board.getBlackPawns() : board.getWhitePawns();
        long enPassantTakingRank = white ? BitboardResources.RANK_FIVE : BitboardResources.RANK_FOUR;

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return;
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return;
        }

        if (board.moveStack.size() < 1){
            return;
        }

        StackMoveData previousMove = board.moveStack.peek();
        if (previousMove.typeOfSpecialMove != SpecialMove.ENPASSANTVICTIM){
            return;
        }



        long FILE = extractFileFromInt(previousMove.enPassantFile);

        List<Long> allEnemyPawnsInPosition = BitOperations.getAllPieces(enemyPawnsInPosition, ignoreThesePieces);

        long enemyTakingSpots = 0;
        for (Long enemyPawn : allEnemyPawnsInPosition){
            long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
            long potentialTakingSpot = takingSpot & FILE;

            if ((potentialTakingSpot & board.allPieces()) != 0){
                continue;
            }
            
            if (((enemyPawn & legalCaptures) == 0) && ((potentialTakingSpot & legalPushes) == 0)) {
                continue;
            }
            enemyTakingSpots |= potentialTakingSpot;
        }


        if (enemyTakingSpots == 0){
            return;
        }

        List<Long> allMyPawnsInPosition = BitOperations.getAllPieces(myPawnsInPosition, ignoreThesePieces);

//        for (Long myPawn : allMyPawnsInPosition){
//            int indexOfFirstPiece = BitOperations.getIndexOfFirstPiece(myPawn);
//            long pawnEnPassantCapture = PieceMovePawns.singlePawnCaptures(myPawn, white, enemyTakingSpots);
//            List<Move> epMoves = new ArrayList<>();
//            MoveGenerationUtilities.movesFromAttackBoardCapture(epMoves, pawnEnPassantCapture, indexOfFirstPiece, true);
//            temp.addAll(epMoves);
//        }

        while (myPawnsInPosition != 0){
            final long pawn = BitOperations.getFirstPiece(myPawnsInPosition);
            if ((pawn & ignoreThesePieces) == 0) {
                int indexOfFirstPiece = BitOperations.getIndexOfFirstPiece(pawn);

                long pawnEnPassantCapture = PieceMovePawns.singlePawnCaptures(pawn, white, enemyTakingSpots);

                List<Move> epMoves = new ArrayList<>();
                MoveGenerationUtilities.movesFromAttackBoardCapture(epMoves, pawnEnPassantCapture, indexOfFirstPiece, true);
                temp.addAll(epMoves);
            }
            myPawnsInPosition &= myPawnsInPosition - 1;
        }
        
        /*
        while (pawns != 0){
            final long pawn = BitOperations.getFirstPiece(pawns);
            if ((pawn & ignoreThesePieces) == 0) {
                ans |= singlePawnCaptures(pawn, white, legalCaptures);
            }
            pawns &= pawns - 1;
        }
         */

        List<Move> safeEPMoves = new ArrayList<>();
        // remove moves that would leave us in check
        for (Move move : temp){
            move.move |= ENPASSANT_MASK;

//            MoveMaker.makeMoveMaster(board, move);
            board.makeMoveAndFlipTurn(move);
            boolean enPassantWouldLeadToCheck = CheckHelper.boardInCheck(board, white);
//            MoveUnmaker.unMakeMoveMaster(board);
            board.unMakeMoveAndFlipTurn();
            
            if (enPassantWouldLeadToCheck){
                continue;
            }
            safeEPMoves.add(move);
        }
        moves.addAll(safeEPMoves);
    }


    private static long extractFileFromInt(int file){
        if (file == 1){
            return BitboardResources.FILE_A;
        }
        else if (file == 2){
            return BitboardResources.FILE_B;
        }
        else if (file == 3){
            return BitboardResources.FILE_C;
        }
        else if (file == 4){
            return BitboardResources.FILE_D;
        }
        else if (file == 5){
            return BitboardResources.FILE_E;
        }
        else if (file == 6){
            return BitboardResources.FILE_F;
        }
        else if (file == 7){
            return BitboardResources.FILE_G;
        }
        else if (file == 8){
            return BitboardResources.FILE_H;
        }
        throw new RuntimeException("Incorrect File gotten from Stack.");
    }

}
