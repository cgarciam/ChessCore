package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.ConstantsMove.*;
import static com.github.louism33.chesscore.MovePrettifier.prettyMove;
import static com.github.louism33.chesscore.Piece.*;

public class MoveParser {
    
    public static int newMove(Chessboard board, String algebraicNotation){
        return MoveParserFromAN.buildMoveFromAN(board, algebraicNotation);
    }

    public static int numberOfRealMoves(int[] moves){
        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        return index;
    }
    
    public static void printMoves(int[] moves){
        System.out.println(Arrays.toString(MoveParser.toString(moves)));
    }
    
    public static int copyMove(int move){
        return move;
    }

    private static int buildMove(Chessboard board, int s, int d) {
        Assert.assertTrue(s >= 0 && s < 64 && d >= 0 && d < 64);
        
        int move = 0;
        move |= ((s << SOURCE_OFFSET) & SOURCE_MASK);
        move |= (d & DESTINATION_MASK);
        
        move |= (ConstantsMove.SOURCE_PIECE_MASK | whichPieceMask(pieceOnSquare(board, newPieceOnSquare(s)))) << ConstantsMove.SOURCE_PIECE_OFFSET;
        
        return move;
    }

    public static String[] toString(int[] moves){
        final int number = numberOfRealMoves(moves);
        String[] realMoves = new String[number];
        for (int i = 0; i < number; i ++){
            realMoves[i] = prettyMove(moves[i]);
        }
        return realMoves;
    }
    
    public static String toString(int move){
        return move == 0 ? "NULL_MOVE" : prettyMove(move);
    }

    public static int moveFromSourceDestination(Chessboard board, int source, int destinationIndex) {
        return buildMove(board, source, destinationIndex);
    }

    static int moveFromSourceDestinationSquareCaptureSecure(Chessboard board, Piece movingPiece, 
                                                            long file, Square source, Square destinationIndex, boolean capture) {
        if (source == null){
            int sourceIndex = -1;
            
            int[] moves = board.generateLegalMoves();
            for (int i = 0; i < moves.length; i++){
                int move = moves[i];
                if (move == 0){
                    break;
                }
                if ((MoveParser.getSourceLong(move) & file) == 0){
                    continue;
                }
                
                if (MoveParser.getDestinationIndex(move) == destinationIndex.ordinal()){
                    if (movingPiece != null && movingPiece != NO_PIECE){
                        if (MoveParser.getMovingPiece(move) != movingPiece){
                            continue;
                        }
                    }
                    sourceIndex = MoveParser.getSourceIndex(move);
                }
            }
            if (sourceIndex == -1){
                throw new RuntimeException("Could not parse Algebraic notation move");
            }
            return buildMove(board, sourceIndex, destinationIndex.ordinal())
                    | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex.ordinal())) : 0);
        }
        
        return buildMove(board, source.ordinal(), destinationIndex.ordinal())
                | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex.ordinal())) : 0);
    }
    
    static int moveFromSourceDestinationSquareCapture(Chessboard board, Square source, Square destinationIndex, boolean capture) {
        return buildMove(board, source.ordinal(), destinationIndex.ordinal())
                | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex.ordinal())) : 0);
    }
    
    static int moveFromSourceDestinationCapture(Chessboard board, int source, int destinationIndex, boolean capture) {
        return buildMove(board, source, destinationIndex) 
                | (capture ? (CAPTURE_MOVE_MASK | capturePieceMask(board, destinationIndex)) : 0);
    }

    private static int capturePieceMask(Chessboard board, int destinationIndex) {
        return whichPieceMask(pieceOnSquare(board, newPieceOnSquare(destinationIndex))) << ConstantsMove.VICTIM_PIECE_OFFSET;
    }

    private static int whichPieceMask(Piece piece) {
        switch (piece){

            case WHITE_PAWN:
                return ConstantsMove.WHITE_PAWN_MASK;
            case WHITE_KNIGHT:
                return ConstantsMove.WHITE_KNIGHT_MASK;
            case WHITE_BISHOP:
                return ConstantsMove.WHITE_BISHOP_MASK;
            case WHITE_ROOK:
                return ConstantsMove.WHITE_ROOK_MASK;
            case WHITE_QUEEN:
                return ConstantsMove.WHITE_QUEEN_MASK;
            case WHITE_KING:
                return ConstantsMove.WHITE_KING_MASK;

            case BLACK_PAWN:
                return ConstantsMove.BLACK_PAWN_MASK;
            case BLACK_KNIGHT:
                return ConstantsMove.BLACK_KNIGHT_MASK;
            case BLACK_BISHOP:
                return ConstantsMove.BLACK_BISHOP_MASK;
            case BLACK_ROOK:
                return ConstantsMove.BLACK_ROOK_MASK;
            case BLACK_QUEEN:
                return ConstantsMove.BLACK_QUEEN_MASK;
            case BLACK_KING:
                return ConstantsMove.BLACK_KING_MASK;
                
            case NO_PIECE:
                return 0;
        }
        return 0;
    }

    private static int whichMover(int move){
        return 0;
    }
    
    
    public static int makeSpecialMove(Chessboard board, int source, int destinationIndex, boolean castling, boolean enPassant, boolean promotion,
                                      boolean promoteToKnight, boolean promoteToBishop, boolean promoteToRook, boolean promoteToQueen) {

        int move = buildMove(board, source, destinationIndex);

        if (castling) move |= CASTLING_MASK;
        if (enPassant) move |= ENPASSANT_MASK;
        if (promotion) {
            if (promoteToKnight) move |= KNIGHT_PROMOTION_MASK;
            else if (promoteToBishop) move |= BISHOP_PROMOTION_MASK;
            else if (promoteToRook) move |= ROOK_PROMOTION_MASK;
            else if (promoteToQueen) move |= QUEEN_PROMOTION_MASK;
        }
        return move;
    }

    public static int getSourceIndex(int move) {
        return ((move & SOURCE_MASK) >>> SOURCE_OFFSET);
    }
    
    public static long getSourceLong(int move) {
        return BitOperations.newPieceOnSquare((move & SOURCE_MASK) >>> SOURCE_OFFSET);
    }

    public static int getDestinationIndex(int move) {
        return move & DESTINATION_MASK;
    }

    public static long getDestinationLong(int move) {
        return BitOperations.newPieceOnSquare(move & DESTINATION_MASK);
    }

    public static boolean isCaptureMove(int move){
        return (move & CAPTURE_MOVE_MASK) != 0;
    }

    public static boolean isSpecialMove (int move){
        return (move & SPECIAL_MOVE_MASK) != 0;
    }

    public static boolean isCastlingMove (int move){
        return (move & SPECIAL_MOVE_MASK) == CASTLING_MASK;
    }

    public static boolean isEnPassantMove (int move){
        return (move & SPECIAL_MOVE_MASK) == ENPASSANT_MASK;
    }

    public static boolean isPromotionMove (int move){
        return (move & SPECIAL_MOVE_MASK) == PROMOTION_MASK;
    }

    public static boolean isPromotionToKnight (int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK;
    }

    public static boolean isPromotionToBishop(int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == BISHOP_PROMOTION_MASK;
    }

    public static boolean isPromotionToRook (int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == ROOK_PROMOTION_MASK;
    }

    public static boolean isPromotionToQueen (int move){
        if (!((move & SPECIAL_MOVE_MASK) == PROMOTION_MASK)) return false;
        return (move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK;
    }
    
    static int makeCheckingMove(int move){
        return move | (inCheck << checkOffset);
    }

    public static boolean isCheckingMove (int move){
        if (((move & OPTIONAL_CHECKING_MOVE_MASK) >>> checkOffset)  == notSet){
            return false;
        }
        else if (((move & OPTIONAL_CHECKING_MOVE_MASK) >>> checkOffset) == notInCheck){
            return false;
        }
        else if (((move & OPTIONAL_CHECKING_MOVE_MASK) >>> checkOffset) == inCheck){
            return true;
        }

        return (move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK;
    }

    public static Piece getMovingPiece(int move){
        final int indexOfSourcePiece = (move & SOURCE_PIECE_MASK) >>> SOURCE_PIECE_OFFSET;
        return values()[indexOfSourcePiece];
    }

    public static Piece getVictimPiece(int move){
        if (!isCaptureMove(move)) {
            return NO_PIECE;
        }
        final int indexOfVictimPiece = (move & VICTIM_PIECE_MASK) >>> VICTIM_PIECE_OFFSET;
        return values()[indexOfVictimPiece];
    }

    public static boolean moveIsPawnPushSeven(int move){
        return getMovingPiece(move) == WHITE_PAWN
                & getMovingPiece(move) == BLACK_PAWN
                & (getDestinationLong(move) & BitboardResources.RANK_SEVEN) != 0
                & (getDestinationLong(move) & BitboardResources.RANK_TWO) != 0;
    }

    public static boolean moveIsPawnPushSix(int move){
        return getMovingPiece(move) == WHITE_PAWN
                & getMovingPiece(move) == BLACK_PAWN
                & (getDestinationLong(move) & BitboardResources.RANK_SIX) != 0
                & (getDestinationLong(move) & BitboardResources.RANK_THREE) != 0;
    }
    
    public static boolean equalsANMove(int move, int compareMove){
        int destinationIndexMove = getDestinationIndex(move);
        int destinationIndexCompare = getDestinationIndex(compareMove);
        
        return destinationIndexMove == destinationIndexCompare;
    }

}