package com.github.louism33.chesscore;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public final class Art {
    /** This map will hold the ASCII representations of the chess pieces. */
    private static final Map<Integer, String> PIECE_MAP;

    static {
        // Initialize the pieceMap with the ASCII characters for the pieces.
        PIECE_MAP = new java.util.HashMap<>();
        PIECE_MAP.put(1, "♙"); // White Pawn
        PIECE_MAP.put(2, "♘"); // White Knight
        PIECE_MAP.put(3, "♗"); // White Bishop
        PIECE_MAP.put(4, "♖"); // White Rook
        PIECE_MAP.put(5, "♕"); // White Queen
        PIECE_MAP.put(6, "♔"); // White King
        PIECE_MAP.put(7, "♟"); // Black Pawn
        PIECE_MAP.put(8, "♞"); // Black Knight
        PIECE_MAP.put(9, "♝"); // Black Bishop
        PIECE_MAP.put(10, "♜"); // Black Rook
        PIECE_MAP.put(11, "♛"); // Black Queen
        PIECE_MAP.put(12, "♚"); // Black King
        if (log.isTraceEnabled()) {
            log.trace("Art class initialized with pieceMap: {}", PIECE_MAP);
        }
    }

    private Art() {
        // This class should not be instantiated: it is a utility class.
    }

    public static String boardArt(final Chessboard board) {
        final StringBuilder s = new StringBuilder(512);
        s.append("   a b c d e f g h\n");
        s.append("  +---------------+\n");
        for (int y = 7; y >= 0; y--) {
            s.append(y + 1).append(" |");
            for (int x = 7; x >= 0; x--) {
                s.append(pieceByNumberASCII(board.pieceSquareTable[x + y * 8]));
                if (x > 0) {
                    s.append(' ');
                }
            }
            s.append("| ").append(y + 1);
            s.append('\n');
        }
        s.append("  +---------------+\n");
        s.append("   a b c d e f g h\n");

        return s.toString();
    }

    private static String pieceByNumberASCIIOrig(final int s) {
        switch (s) {
            case 1: return ("P");
            case 2: return ("N");
            case 3: return ("B");
            case 4: return ("R");
            case 5: return ("Q");
            case 6: return ("K");
            case 7: return ("p");
            case 8: return ("n");
            case 9: return ("b");
            case 10: return ("r");
            case 11: return ("q");
            case 12: return ("k");
            default: return (".");
        }
    }

    private static String pieceByNumberASCII(final int s) {
        // There is no "intrinsic" better performance in any of these 3 methods.
        return PIECE_MAP.getOrDefault(s, ".");
    }

    private static String pieceByNumberASCII1(final int s) {
        switch (s) {
        case 1:
            return ("♙");
        case 2:
            return ("♘");
        case 3:
            return ("♗");
        case 4:
            return ("♖");
        case 5:
            return ("♕");
        case 6:
            return ("♔");
        case 7:
            return ("♟");
        case 8:
            return ("♞");
        case 9:
            return ("♝");
        case 10:
            return ("♜");
        case 11:
            return ("♛");
        case 12:
            return ("♚");
        default:
            return (".");
        }
    }

}