package com.github.louism33.chesscore;

import static com.github.louism33.chesscore.BoardConstants.BLACK_PAWN;
import static com.github.louism33.chesscore.BoardConstants.WHITE_PAWN;
import static com.github.louism33.chesscore.MoveParser.getMovingPieceInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.github.louism33.utils.MoveParserFromAN;

class ChessboardTest {

    @Test
    void previousMoveWasPawnPushToSixTest() {
        String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);
        final int move = MoveParserFromAN.buildMoveFromLAN(board, "c4c3");
        final int turnBefore = board.turn;
        board.makeMoveAndFlipTurn(move);

        assertTrue(board.previousMoveWasPawnPushToSix());
        assertTrue(MoveParser.moveIsPawnPushSix(turnBefore, move));
    }

    @Test
    void previousMoveWasPawnPushToSevenTest() {
        String fen = "8/7p/5k2/5p2/p4P2/PrppPK2/1P1R3P/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int move = MoveParserFromAN.buildMoveFromLAN(board, "c3c2");
        final int turnBefore = board.turn;
        board.makeMoveAndFlipTurn(move);

        assertTrue(board.previousMoveWasPawnPushToSeven());
        assertTrue(MoveParser.moveIsPawnPushSeven(turnBefore, move));
    }

    @Test
    void previousMoveWasPawnPushToSeven2Test() {
        String fen = "8/8/PPPPPPPP/8/k6K/pppppppp/8/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int[] moves = board.generateLegalMoves();
        final int len = moves[moves.length - 1];
        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != BLACK_PAWN) {
                continue;
            }

            assertTrue(MoveParser.moveIsPawnPushSeven(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            assertTrue(board.previousMoveWasPawnPushToSeven());
            board.unMakeMoveAndFlipTurn();
        }

        board.makeNullMoveAndFlipTurn();

        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != WHITE_PAWN) {
                continue;
            }

            assertTrue(MoveParser.moveIsPawnPushSeven(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            assertTrue(board.previousMoveWasPawnPushToSeven());
            board.unMakeMoveAndFlipTurn();
        }
    }

    @Test
    void previousMoveWasPawnPushToSix2Test() {
        String fen = "k6K/8/8/PPPPPPPP/pppppppp/8/8/8 b - -";
        Chessboard board = new Chessboard(fen);

        final int[] moves = board.generateLegalMoves();
        final int len = moves[moves.length - 1];
        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != BLACK_PAWN) {
                continue;
            }

            assertTrue(MoveParser.moveIsPawnPushSix(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            assertTrue(board.previousMoveWasPawnPushToSix());
            board.unMakeMoveAndFlipTurn();
        }

        board.makeNullMoveAndFlipTurn();

        for (int i = 0; i < len; i++) {
            final int move = moves[i];
            if (move == 0) {
                continue;
            }
            final int movingPieceInt = getMovingPieceInt(move);
            if (movingPieceInt != WHITE_PAWN) {
                continue;
            }

            assertTrue(MoveParser.moveIsPawnPushSix(board.turn, move));
            board.makeMoveAndFlipTurn(move);
            assertTrue(board.previousMoveWasPawnPushToSix());
            board.unMakeMoveAndFlipTurn();
        }
    }

    @Test
    void moveIsCaptureOfLastMovePieceTest() {
        final String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -";
        final Chessboard board = new Chessboard(fen);

        final int move = MoveParserFromAN.buildMoveFromLAN(board, "b3b2");
        board.makeMoveAndFlipTurn(move);
        final int moveC = MoveParserFromAN.buildMoveFromLAN(board, "d2b2");
        assertTrue(board.moveIsCaptureOfLastMovePiece(moveC));
    }

    @Test
    void toStringTest() {
        final String fen = "8/7p/5k2/5p2/p1p2P2/Pr1pPK2/1P1R3P/8 b - -";
        final Chessboard board = new Chessboard(fen);
        // @formatter:off
        @SuppressWarnings("unused")
        final String expectedOrig = """

   a b c d e f g h
  +---------------+
8 |. . . . . . . .| 8
7 |. . . . . . . p| 7
6 |. . . . . k . .| 6
5 |. . . . . p . .| 5
4 |p . p . . P . .| 4
3 |P r . p P K . .| 3
2 |. P . R . . . P| 2
1 |. . . . . . . .| 1
  +---------------+
   a b c d e f g h

It is black's turn.
""";
        final String expected = """

   a b c d e f g h
  +---------------+
8 |. . . . . . . .| 8
7 |. . . . . . . ♟| 7
6 |. . . . . ♚ . .| 6
5 |. . . . . ♟ . .| 5
4 |♟ . ♟ . . ♙ . .| 4
3 |♙ ♜ . ♟ ♙ ♔ . .| 3
2 |. ♙ . ♖ . . . ♙| 2
1 |. . . . . . . .| 1
  +---------------+
   a b c d e f g h

It is black's turn.
""";
        // @formatter:on
        assertEquals(expected, board.toString());
    }

}