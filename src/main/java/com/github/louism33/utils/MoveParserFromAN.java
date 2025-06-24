package com.github.louism33.utils;

import static com.github.louism33.chesscore.BitOperations.getFirstPiece;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.BoardConstants.BISHOP;
import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.BLACK_BISHOP;
import static com.github.louism33.chesscore.BoardConstants.BLACK_KING;
import static com.github.louism33.chesscore.BoardConstants.BLACK_KNIGHT;
import static com.github.louism33.chesscore.BoardConstants.BLACK_PAWN;
import static com.github.louism33.chesscore.BoardConstants.BLACK_QUEEN;
import static com.github.louism33.chesscore.BoardConstants.BLACK_ROOK;
import static com.github.louism33.chesscore.BoardConstants.CASTLE_KING_DESTINATIONS;
import static com.github.louism33.chesscore.BoardConstants.FILES;
import static com.github.louism33.chesscore.BoardConstants.INITIAL_PIECES;
import static com.github.louism33.chesscore.BoardConstants.KING;
import static com.github.louism33.chesscore.BoardConstants.KING_MOVE_TABLE;
import static com.github.louism33.chesscore.BoardConstants.KNIGHT;
import static com.github.louism33.chesscore.BoardConstants.KNIGHT_MOVE_TABLE;
import static com.github.louism33.chesscore.BoardConstants.NO_PIECE;
import static com.github.louism33.chesscore.BoardConstants.PAWN;
import static com.github.louism33.chesscore.BoardConstants.PENULTIMATE_RANKS;
import static com.github.louism33.chesscore.BoardConstants.PIECE;
import static com.github.louism33.chesscore.BoardConstants.QUEEN;
import static com.github.louism33.chesscore.BoardConstants.RANKS;
import static com.github.louism33.chesscore.BoardConstants.ROOK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;
import static com.github.louism33.chesscore.BoardConstants.WHITE_BISHOP;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KING;
import static com.github.louism33.chesscore.BoardConstants.WHITE_KNIGHT;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.BoardConstants.WHITE_QUEEN;
import static com.github.louism33.chesscore.BoardConstants.WHITE_ROOK;
import static com.github.louism33.chesscore.MoveConstants.BISHOP_PROMOTION_MASK;
import static com.github.louism33.chesscore.MoveConstants.CASTLING_MASK;
import static com.github.louism33.chesscore.MoveConstants.ENPASSANT_MASK;
import static com.github.louism33.chesscore.MoveConstants.KNIGHT_PROMOTION_MASK;
import static com.github.louism33.chesscore.MoveConstants.PROMOTION_MASK;
import static com.github.louism33.chesscore.MoveConstants.QUEEN_PROMOTION_MASK;
import static com.github.louism33.chesscore.MoveConstants.ROOK_PROMOTION_MASK;
import static com.github.louism33.chesscore.MoveParser.buildMove;
import static com.github.louism33.chesscore.PieceMove.singleBishopTable;
import static com.github.louism33.chesscore.PieceMove.singlePawnCaptures;
import static com.github.louism33.chesscore.PieceMove.singlePawnPushes;
import static com.github.louism33.chesscore.PieceMove.singleQueenTable;
import static com.github.louism33.chesscore.PieceMove.singleRookTable;
import static java.lang.Long.numberOfTrailingZeros;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.Square;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MoveParserFromAN {

    
    private static final Pattern LAN_PATTERN = Pattern.compile(".?([abcdefgh][12345678])[-x]?([abcdefgh][12345678])(\\w)?");
    private static final Pattern AN_PATTERN = Pattern.compile("([PNBRQKpnrqk])?([a-h])?([1-8])?([x])?([a-h])([1-8])([=]?)([QNRB]?)([+#]?)");
    private static final Matcher LAN_MATCHER = LAN_PATTERN.matcher("");
    // is lower case b in first group necessary or safe?
    private static final Matcher AN_MATCHER = AN_PATTERN.matcher("");

    private MoveParserFromAN() {
        // Prevent instantiation (utility class).
    }

    /**
     * Converts a LAN move string to a move code (integer).
     *
     * @param board The chessboard object.
     * @param an The LAN move string.
     * @return The move integer.
     */
    public static int buildMoveFromLAN(final Chessboard board, final String an) {
        LAN_MATCHER.reset(an);

        if (LAN_MATCHER.find()) {
            return buildMoveFromRealLAN(board);
        }

        return buildMoveFromAN(board, an);
    }

    private static int buildMoveFromRealLAN(final Chessboard board) {
        final String movingPieceStr = LAN_MATCHER.group(1);
        final String promotionPiece = LAN_MATCHER.group(3);

        int sourceIndex = 'h' - movingPieceStr.charAt(0) + (movingPieceStr.charAt(1) - '1')*8;
        int destinationIndex = 'h' - LAN_MATCHER.group(2).charAt(0) + (LAN_MATCHER.group(2).charAt(1) - '1')*8;

        int basicMove = buildMove(sourceIndex, board.pieceSquareTable[sourceIndex], destinationIndex, board.pieceSquareTable[destinationIndex]);

        long movingPiece = newPieceOnSquare(sourceIndex);
        long destinationSquare = newPieceOnSquare(destinationIndex);

        int turn = board.turn;
        if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
            if ((destinationSquare & CASTLE_KING_DESTINATIONS) != 0){
                basicMove |= CASTLING_MASK;
            }
        }

        // if it is a diagonal non-capture by a pawn, it must be EP
        if (LAN_MATCHER.group(1).charAt(0) != LAN_MATCHER.group(2).charAt(0)) {
            if (board.pieceSquareTable[sourceIndex] == PIECE[turn][PAWN]
                    && board.pieceSquareTable[destinationIndex] == NO_PIECE) {
                basicMove |= ENPASSANT_MASK;
            }
        }

        if (promotionPiece != null) {
            basicMove |= PROMOTION_MASK;
            switch (promotionPiece) {
                case "n", "N":
                    basicMove |= KNIGHT_PROMOTION_MASK;
                    break;
                case "b", "B":
                    basicMove |= BISHOP_PROMOTION_MASK;
                    break;
                case "r", "R":
                    basicMove |= ROOK_PROMOTION_MASK;
                    break;
                case "q", "Q":
                    basicMove |= QUEEN_PROMOTION_MASK;
                    break;
                default:
                    break;
            }
        }

        return basicMove;
    }

    public static int buildMoveFromANWithOO(final Chessboard board, final String an) {
        AN_MATCHER.reset(an);

        if (an.equals("O-O") || an.equals("O-O+")) {
            if (board.turn == WHITE) {
                return buildMoveFromLAN(board, "e1g1");
            } else {
                return buildMoveFromLAN(board, "e8g8");
            }
        }

        if (an.equals("O-O-O") || an.equals("O-O-O+")) {
            if (board.turn == WHITE) {
                return buildMoveFromLAN(board, "e1c1");
            } else {
                return buildMoveFromLAN(board, "e8c8");
            }
        }
        
        char[] chars = new char[9];
        if (AN_MATCHER.find()) {
            int groupCount = AN_MATCHER.groupCount();

            for (int i = 0; i < groupCount; i++) {
                String entry = AN_MATCHER.group(i + 1);
                if (entry != null && entry.length() != 0) {
                    chars[i] = entry.charAt(0);
                }
            }
        }

        if (chars[1] != 0 && chars[2] != 0 && chars[4] != 0 && chars[5] != 0) {
            return buildMoveFromLAN(board, an);
        }

        int basicMove;

        int turn = board.turn;
        int movingPiece = PIECE[turn][PAWN];

        if (chars[0] != 0) {
            movingPiece = getSourcePiece(chars[0], turn);
        }

        long movingPieceLong = board.pieces[turn][movingPiece < 7 ? movingPiece : movingPiece - 6];

        if (chars[1] != 0){
            movingPieceLong &= FILES['h' - chars[1]];
        }

        if (chars[2] != 0) {
            movingPieceLong &= RANKS[chars[2] - '1'];
        }

        boolean b4 = chars[4] != 0;
        long destinationFile = 0;
        if (b4) {
            destinationFile = FILES['h' - chars[4]];
        }

        boolean b5 = chars[5] != 0;
        long destinationSquare = 0;
        if (b5) {
            long rank = RANKS[chars[5] - '1'];
            destinationSquare = rank & destinationFile;
        }
        
        int destinationIndex = numberOfTrailingZeros(destinationSquare);

        if (populationCount(destinationSquare) != 1) {
            throw new RuntimeException();
        }

        int sourceIndex;

        if (populationCount(movingPieceLong) != 1) {
            sourceIndex = numberOfTrailingZeros(getMovingPiece(board, board.allPieces(), destinationIndex, movingPieceLong, movingPiece));
        }
        else {
            sourceIndex = numberOfTrailingZeros(movingPieceLong);
        }

        if (movingPiece == 0) {
            movingPiece = board.pieceSquareTable[sourceIndex];
        }
        basicMove = buildMove(sourceIndex, movingPiece, destinationIndex, board.pieceSquareTable[destinationIndex]);



        if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
            if ((destinationSquare & CASTLE_KING_DESTINATIONS) != 0){
                basicMove |= CASTLING_MASK;
            }
        }

        // if it is a diagonal non-capture by a pawn, it must be EP
        if ((sourceIndex != destinationIndex + 16)
                && (sourceIndex != destinationIndex - 16)
                && (sourceIndex != destinationIndex + 8)
                && (sourceIndex != destinationIndex - 8)) { // not a push
            if (board.pieceSquareTable[sourceIndex] == PIECE[turn][PAWN]
                    && board.pieceSquareTable[destinationIndex] == NO_PIECE) {
                basicMove |= ENPASSANT_MASK;
            }
        }
        
        if (chars[3] != 0) {
//            Assert.assertTrue(MoveParser.isCaptureMove(basicMove) || MoveParser.isEnPassantMove(basicMove));
        }
        
        if (chars[7] != 0) {
            basicMove |= PROMOTION_MASK;
            switch (chars[7]) {
                case 'n', 'N':
                    basicMove |= KNIGHT_PROMOTION_MASK;
                    break;
                case 'b', 'B':
                    basicMove |= BISHOP_PROMOTION_MASK;
                    break;
                case 'r', 'R':
                    basicMove |= ROOK_PROMOTION_MASK;
                    break;
                case 'q', 'Q':
                    basicMove |= QUEEN_PROMOTION_MASK;
                    break;
                default:
                    break;
            }
        }

        return basicMove;
    }

    public static int buildMoveFromAN(final Chessboard board, final String an){
        AN_MATCHER.reset(an);

        char[] chars = new char[9];
        if (AN_MATCHER.find()) {
            int groupCount = AN_MATCHER.groupCount();

            for (int i = 0; i < groupCount; i++) {
                String entry = AN_MATCHER.group(i + 1);
                if (entry != null && entry.length() != 0) {
                    chars[i] = entry.charAt(0);
                }
            }
        }

        if (chars[1] != 0 && chars[2] != 0 && chars[4] != 0 && chars[5] != 0) {
            return buildMoveFromLAN(board, an);
        }

        int basicMove;

        int turn = board.turn;
        int movingPiece = PIECE[turn][PAWN];

        if (chars[0] != 0) {
            movingPiece = getSourcePiece(chars[0], turn);
        }

        long movingPieceLong = board.pieces[turn][movingPiece < 7 ? movingPiece : movingPiece - 6];

        if (chars[1] != 0){
            movingPieceLong &= FILES['h' - chars[1]];
        }

        if (chars[2] != 0) {
            movingPieceLong &= RANKS[chars[2] - '1'];
        }

        boolean b4 = chars[4] != 0;
        long destinationFile = 0;
        if (b4) {
            destinationFile = FILES['h' - chars[4]];
        }

        boolean b5 = chars[5] != 0;
        long destinationSquare = 0;
        if (b5) {
            long rank = RANKS[chars[5] - '1'];
            destinationSquare = rank & destinationFile;
        }

        int destinationIndex = numberOfTrailingZeros(destinationSquare);

        if (populationCount(destinationSquare) != 1) {
            throw new RuntimeException();
        }

        int sourceIndex;

        if (populationCount(movingPieceLong) != 1) {
            sourceIndex = numberOfTrailingZeros(getMovingPiece(board, board.allPieces(), destinationIndex, movingPieceLong, movingPiece));
        }
        else {
            sourceIndex = numberOfTrailingZeros(movingPieceLong);
        }

        if (movingPiece == 0) {
            movingPiece = board.pieceSquareTable[sourceIndex];
        }
        basicMove = buildMove(sourceIndex, movingPiece, destinationIndex, board.pieceSquareTable[destinationIndex]);

        if (movingPiece == INITIAL_PIECES[turn][KING] && board.pieceSquareTable[sourceIndex] == PIECE[turn][KING]){
            if ((destinationSquare & CASTLE_KING_DESTINATIONS) != 0){
                basicMove |= CASTLING_MASK;
            }
        }

        // if it is a diagonal non-capture by a pawn, it must be EP
        if ((sourceIndex != destinationIndex + 16)
                && (sourceIndex != destinationIndex - 16)
                && (sourceIndex != destinationIndex + 8)
                && (sourceIndex != destinationIndex - 8)) {
            if (board.pieceSquareTable[sourceIndex] == PIECE[turn][PAWN]
                    && board.pieceSquareTable[destinationIndex] == NO_PIECE) {
                basicMove |= ENPASSANT_MASK;
            }
        }
        if (chars[3] != 0) {
//            Assert.assertTrue(MoveParser.isCaptureMove(basicMove) || MoveParser.isEnPassantMove(basicMove));
        }
        if (chars[7] != 0) {
            basicMove |= PROMOTION_MASK;
            switch (chars[7]){
                case 'n':
                case 'N':
                    basicMove |= KNIGHT_PROMOTION_MASK;
                    break;
                case 'b':
                case 'B':
                    basicMove |= BISHOP_PROMOTION_MASK;
                    break;
                case 'r':
                case 'R':
                    basicMove |= ROOK_PROMOTION_MASK;
                    break;
                case 'q':
                case 'Q':
                    basicMove |= QUEEN_PROMOTION_MASK;
                    break;
            }
        }

        return basicMove;
    }

    private static long getMovingPiece(final Chessboard board, final long allPieces, final int destinationIndex, long candidateMovers, final int movingPieceType){
        switch (movingPieceType) {
            case NO_PIECE:
                throw new RuntimeException();
            case WHITE_PAWN:
                candidateMovers = pawnFinder(allPieces, candidateMovers, WHITE, destinationIndex);
                break;
            case BLACK_PAWN:
                candidateMovers = pawnFinder(allPieces, candidateMovers, BLACK, destinationIndex);
                break;
            case WHITE_KNIGHT, BLACK_KNIGHT:
                candidateMovers &= KNIGHT_MOVE_TABLE[destinationIndex];
                break;
            case WHITE_BISHOP, BLACK_BISHOP:
                candidateMovers = singleBishopTable(allPieces, destinationIndex, candidateMovers);
                break;
            case WHITE_ROOK, BLACK_ROOK:
                candidateMovers = singleRookTable(allPieces, destinationIndex, candidateMovers);
                break;
            case WHITE_QUEEN, BLACK_QUEEN:
                candidateMovers = singleQueenTable(allPieces, destinationIndex, candidateMovers);
                break;
            case WHITE_KING, BLACK_KING:
                candidateMovers &= KING_MOVE_TABLE[destinationIndex];
                break;
            default:
                throw new RuntimeException();
        }

        if (populationCount(candidateMovers) != 1) {
            board.generateLegalMoves();
            
            long candidateMoversWithoutPins = ~board.pinnedPieces & candidateMovers;
            if (populationCount(candidateMoversWithoutPins) != 1) {
                throw new RuntimeException();
            }
            return candidateMoversWithoutPins;
        }

        return candidateMovers;
    }
    
    
    private static long pawnFinder(final long allPieces, long myPawns, final int turn, final int destinationIndex) {
        final long destinationSquare = newPieceOnSquare(destinationIndex);
        long myPawns2 = myPawns;
        final boolean quietOrEP = (destinationSquare & allPieces) == 0;
        
        while (myPawns != 0) {
            final long pawn = getFirstPiece(myPawns);
            
            final long quietTable = quiet(allPieces, turn, destinationSquare, pawn);

            if (quietTable != 0) {
                return pawn;
            }

            long captureTable = singlePawnCaptures(pawn, turn, destinationSquare);
            
            captureTable &= allPieces;
            
            captureTable &= destinationSquare;

            if (!quietOrEP && captureTable != 0) {
                return pawn;
            }
            
            myPawns &= myPawns - 1;
        }

        while (myPawns2 != 0) {
            final long pawn = getFirstPiece(myPawns2);
            if (quietOrEP) {
                long captureTable = singlePawnCaptures(pawn, turn, destinationSquare);
                if ((captureTable & destinationSquare) != 0) {
                    return pawn;
                }
            }
            myPawns2 &= myPawns2 - 1;
        }

        throw new RuntimeException("couldn't find moving pawn");
    }

    private static long quiet(final long allPieces, final int turn, final long destinationSquare, final long pawn) {
        long quietTable;
        // doubles
        if ((pawn & PENULTIMATE_RANKS[1 - turn]) != 0) {
            quietTable = singlePawnPushes(pawn, turn, destinationSquare, allPieces);
        }
        else {
            quietTable = (turn == WHITE ? pawn << 8 : pawn >>> 8) & ~allPieces & destinationSquare;
        }

        quietTable &= ~allPieces;
        quietTable &= destinationSquare;
        return quietTable;
    }

    private static int getSourcePiece(final char c, final int turn) {
        switch (c) {
            case 'P', 'p':
                return PIECE[turn][PAWN];
            case 'N', 'n':
                return PIECE[turn][KNIGHT];
            case 'B', 'b':
                return PIECE[turn][BISHOP];
            case 'R', 'r':
                return PIECE[turn][ROOK];
            case 'Q', 'q':
                return PIECE[turn][QUEEN];
            case 'K', 'k':
                return PIECE[turn][KING];
            default:
                return NO_PIECE;
        }
    }

    /**
     * Converts a LAN move string to a SAN move string.
     *
     * @param board The chessboard object.
     * @param lan   The LAN move string.
     * @return The SAN move string.
     */
    public static String lanToSan(final Chessboard board, final String lan) {
        // Parse LAN: e2e4, g1f3, e7e8q, etc.
        if (lan == null || lan.length() < 4) {
            return lan;
        }

        final int fromFile = 'h' - lan.charAt(0);
        final int fromRank = lan.charAt(1) - '1';
        final Square fromSquare = new Square(fromFile, fromRank);
        final int toFile = 'h' - lan.charAt(2);
        final int toRank = lan.charAt(3) - '1';
        final Square toSquare = new Square(toFile, toRank);
        final int fromIndex = fromFile + fromRank * 8;
        final int toIndex = toFile + toRank * 8;        

        final int movingPiece = board.pieceSquareTable[fromIndex];
        final int capturedPiece = board.pieceSquareTable[toIndex];
        final boolean isCapture = capturedPiece != NO_PIECE;
        final String pieceLetter = getPiece(movingPiece);

//        System.out.println("" + Arrays.toString(board.pieceSquareTable));
//        System.out.println("From index: " + fromIndex);
//        System.out.println("To index: " + toIndex);
//        System.out.println("Moving piece: " + movingPiece);
//        System.out.println("Captured piece: " + capturedPiece);
//        System.out.println("Is capture: " + isCapture);
    
        // Castling TO DO: To be implemented???
//        if (movingPiece == WHITE_KING || movingPiece == BLACK_KING) {
//            if (fromIndex == 4 && toIndex == 6) return "O-O";
//            if (fromIndex == 4 && toIndex == 2) return "O-O-O";
//            if (fromIndex == 60 && toIndex == 62) return "O-O";
//            if (fromIndex == 60 && toIndex == 58) return "O-O-O";
//        }

        // Pawn move
        if (pieceLetter.isEmpty()) {
            return pawnMove(board, lan, fromSquare, toSquare, isCapture);
        }

        // Piece move
        return pieceMove(lan, toFile, toRank, isCapture, pieceLetter);
    }
    
    // This works OK for en passant, but not for piece captures.
    @SuppressWarnings("unused")
    private static String lanToSanNew(final Chessboard board, final String lan) {
        if(log.isDebugEnabled()) {
            log.debug("LAN: {}\nBoard:{}", lan, board);
        }
        if (lan == null || lan.length() < 4) {
            return lan;
        }

        //*
        final int fromFile = lan.charAt(0) - 'a';
        final int fromRank = lan.charAt(1) - '1';
        final Square fromSquare = new Square(fromFile, fromRank);
        if(log.isDebugEnabled()) {
            log.debug("From square: {}", fromSquare);
        }
        final int toFile = lan.charAt(2) - 'a';
        final int toRank = lan.charAt(3) - '1';
        final Square toSquare = new Square(toFile, toRank);
        if(log.isDebugEnabled()) {
            log.debug("To square: {}", toSquare);
        }
        final int fromIndex = fromRank * 8 + fromFile;
//        final int fromIndex2 = Square.getIndex(fromSquare);
        final int toIndex = toRank * 8 + toFile;
//        final int toIndex2 = Square.getIndex(toSquare);
        
//        if(log.isDebugEnabled()) {
//            log.debug("From index: {}, From index2: {}", fromIndex, fromIndex2);
//            log.debug("To index: {}, To index2: {}", toIndex, toIndex2);
//        }

        /*/

        final int fromFile = 'h' - lan.charAt(0);
        final int fromRank = lan.charAt(1) - '1';
        final int toFile = 'h' - lan.charAt(2);
        final int toRank = lan.charAt(3) - '1';
        final int fromIndex = fromFile + fromRank * 8;
        final int toIndex = toFile + toRank * 8;
        */

        System.out.println("B pieceSquareTable: " + Arrays.toString(board.pieceSquareTable));
        System.out.println("From index: " + fromIndex);

        final int movingPiece = board.pieceSquareTable[fromIndex];
        final int capturedPiece = board.pieceSquareTable[toIndex];
        boolean isCapture = capturedPiece != NO_PIECE;

        // En passant detection for pawns
        final boolean isPawn = (movingPiece == WHITE_PAWN || movingPiece == BLACK_PAWN);
        final boolean isDiagonal = Math.abs(fromFile - toFile) == 1 && Math.abs(fromRank - toRank) == 1;
        if (isPawn && isDiagonal && capturedPiece == NO_PIECE) {
            final int move = buildMoveFromLAN(board, lan);
            if (MoveParser.isEnPassantMove(move)) {
                isCapture = true;
            }
        }

        // Piece letter for SAN (empty for pawn)
        System.out.println("Moving piece: " + movingPiece);
        final String pieceLetter = getPiece(movingPiece);
//        String pieceLetter = "";
//        switch (movingPiece) {
//            case WHITE_KNIGHT, BLACK_KNIGHT: pieceLetter = "N"; break;
//            case WHITE_BISHOP, BLACK_BISHOP: pieceLetter = "B"; break;
//            case WHITE_ROOK,   BLACK_ROOK:   pieceLetter = "R"; break;
//            case WHITE_QUEEN,  BLACK_QUEEN:  pieceLetter = "Q"; break;
//            case WHITE_KING,   BLACK_KING:   pieceLetter = "K"; break;
//            // Pawns: no letter
//        }
        
        System.out.println("Piece letter: " + pieceLetter);

     // Castling TO DO: To be implemented???
//        if (movingPiece == BoardConstants.WHITE_KING || movingPiece == BoardConstants.BLACK_KING) {
//            if (fromIndex == 4 && toIndex == 6) return "O-O";
//            if (fromIndex == 4 && toIndex == 2) return "O-O-O";
//            if (fromIndex == 60 && toIndex == 62) return "O-O";
//            if (fromIndex == 60 && toIndex == 58) return "O-O-O";
//        }

        // Pawn move
        if (pieceLetter.isEmpty()) {
            String san = "";
            if (isCapture) {
                san += (char)('a' + fromFile) + "x";
            }
            san += (char)('a' + toFile);
            san += (char)('1' + toRank);
            // Promotion
            if (lan.length() > 4) {
                san += "=" + Character.toUpperCase(lan.charAt(4));
            }
            return san;
        }

        // Piece move (include 'x' for captures)
        String san = pieceLetter;
        if (isCapture) {
            san += "x";
        }
        san += (char)('a' + toFile);
        san += (char)('1' + toRank);
        // Promotion (rare for non-pawn, but handle)
        if (lan.length() > 4) {
            san += "=" + Character.toUpperCase(lan.charAt(4));
        }
        return san;
    }


    private static String pieceMove(final String lan, final int toFile, final int toRank, final boolean isCapture,
            final String pieceLetter) {
        String san = pieceLetter;
        // Disambiguation (not implemented: add if needed)
        if (isCapture) {
            san += "x";
        }
        san += (char)('h' - toFile);
        san += (char)('1' + toRank);
        // Promotion (rare for non-pawn, but handle)
        if (lan.length() > 4) {
            san += "=" + Character.toUpperCase(lan.charAt(4));
        }
        return san;
    }

    private static String pawnMove(final Chessboard board, final String lan, final Square fromSquare, final Square toSquare,
            final boolean isCapture) {
        String san = "";
//        System.out.println("LAN: " + lan);
//        final int move = buildMoveFromLAN(board, lan);
//        System.out.println("Move: " + move);
        if (isCapture) {
//        if (MoveParser.isCaptureMove(move)) {
//            if (MoveParser.isEnPassantMove(move)) {
//                return lanToSanNew(board, lan);
//            }
            san += (char)('h' - fromSquare.getFile()) + "x";
        }
        san += (char)('h' - toSquare.getFile());
        san += (char)('1' + toSquare.getRank());
        // Promotion
        if (lan.length() > 4) {
            san += "=" + Character.toUpperCase(lan.charAt(4));
        }
        return san;
    }

    private static String getPiece(final int movingPiece) {
        // Piece letter for SAN (empty for pawn)
        String pieceLetter = "";
        switch (movingPiece) {
            case WHITE_KNIGHT, BLACK_KNIGHT: pieceLetter = "N"; break;
            case WHITE_BISHOP, BLACK_BISHOP: pieceLetter = "B"; break;
            case WHITE_ROOK, BLACK_ROOK:   pieceLetter = "R"; break;
            case WHITE_QUEEN, BLACK_QUEEN:  pieceLetter = "Q"; break;
            case WHITE_KING, BLACK_KING:   pieceLetter = "K"; break;
            // Pawns: no letter
        }
        return pieceLetter;
    }

}