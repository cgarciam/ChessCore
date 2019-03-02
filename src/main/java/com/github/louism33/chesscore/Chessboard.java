package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.*;
import static com.github.louism33.chesscore.MakeMoveSpecial.*;
import static com.github.louism33.chesscore.MoveAdder.addMovesFromAttackTableMasterBetter;
import static com.github.louism33.chesscore.MoveConstants.*;
import static com.github.louism33.chesscore.MoveGeneratorCheck.addCheckEvasionMoves;
import static com.github.louism33.chesscore.MoveGeneratorPseudo.addAllMovesWithoutKing;
import static com.github.louism33.chesscore.MoveGeneratorRegular.addKingLegalMovesOnly;
import static com.github.louism33.chesscore.MoveGeneratorSpecial.*;
import static com.github.louism33.chesscore.MoveMakingUtilities.removePieces;
import static com.github.louism33.chesscore.MoveMakingUtilities.togglePiecesFrom;
import static com.github.louism33.chesscore.MoveParser.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static com.github.louism33.chesscore.PinnedManager.whichPiecesArePinned;
import static com.github.louism33.chesscore.StackDataUtil.*;
import static com.github.louism33.chesscore.ZobristHashUtil.*;
import static java.lang.Long.numberOfTrailingZeros;

public class Chessboard {

    public long[][] pieces = new long[2][7];
    
    public int[] pieceSquareTable = new int[64];
    public int turn;
    /*
    castling rights bits:
    BK BA WK WQ
     */
    private int castlingRights = 0xf;

    private int fiftyMoveCounter = 0, fullMoveCounter = 0;

    long zobristHash;

    private long moveStackData;
    private final int maxDepthAndArrayLength = 64;

    private final int maxNumberOfMovesInAnyPosition = 128;
    int[] moves = new int[maxNumberOfMovesInAnyPosition];

    private int[][] legalMoveStack = new int[maxDepthAndArrayLength][maxNumberOfMovesInAnyPosition];

    long[] zobristHashStack = new long[maxDepthAndArrayLength];

    private long[] pastMoveStackArray = new long[maxDepthAndArrayLength];
    public boolean inCheckRecorder;

    // todo needs array
    public long checkingPieces;
    
    public long pinnedPieces;
    private long[] pinnedPiecesArray = new long[maxDepthAndArrayLength];
    private boolean[] checkStack = new boolean[maxDepthAndArrayLength];
    
    
    private long boardToHash(){
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = pieceSquareTable[numberOfTrailingZeros(pieceOnSquare)] - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }
        }

        hash ^= zobristHashCastlingRights[castlingRights];

        if (!isWhiteTurn()){
            hash = zobristFlipTurn(hash);
        }

        if (hasPreviousMove()){
            hash = updateWithEPFlags(moveStackArrayPeek(), hash);
        }

        return hash;
    }

    private int getFiftyMoveCounter() {
        return fiftyMoveCounter;
    }


    /**
     * A new Chessboard in the starting position, white to play.
     */
    public Chessboard() {
        boardToHash();
        Setup.init(false);

        System.arraycopy(INITIAL_PIECES[BLACK], 0, this.pieces[BLACK], 0, INITIAL_PIECES[BLACK].length);
        System.arraycopy(INITIAL_PIECES[WHITE], 0, this.pieces[WHITE], 0, INITIAL_PIECES[WHITE].length);

        System.arraycopy(INITIAL_PIECE_SQUARES, 0, pieceSquareTable, 0, pieceSquareTable.length);

        turn = WHITE;
    }

    /**
     * Copy Constructor
     * @param board the chessboard you want an exact copy of
     */
    public Chessboard(Chessboard board) {
        this.turn = board.turn;
        this.castlingRights = board.castlingRights;
        this.fiftyMoveCounter = board.fiftyMoveCounter;
        this.zobristHash = board.zobristHash;
        this.moveStackData = board.moveStackData;
        this.inCheckRecorder = board.inCheckRecorder;
        this.pinnedPieces = board.pinnedPieces;
        this.legalMoveStackIndex = board.legalMoveStackIndex;
        this.masterIndex = board.masterIndex;
        this.moveStackIndex = board.moveStackIndex;
        System.arraycopy(board.pieces[WHITE], 0, this.pieces[WHITE], 0, 7);
        System.arraycopy(board.pieces[BLACK], 0, this.pieces[BLACK], 0, 7);
        System.arraycopy(board.moves, 0, this.moves, 0, board.moves.length);

        for (int i = 0; i < board.legalMoveStack.length; i++) {
            System.arraycopy(board.legalMoveStack[i], 0, this.legalMoveStack[i], 0, board.legalMoveStack[i].length);
        }

        System.arraycopy(board.zobristHashStack, 0, this.zobristHashStack, 0, board.zobristHashStack.length);
        System.arraycopy(board.pastMoveStackArray, 0, this.pastMoveStackArray, 0, board.pastMoveStackArray.length);
        System.arraycopy(board.pinnedPiecesArray, 0, this.pinnedPiecesArray, 0, board.pinnedPiecesArray.length);
        System.arraycopy(board.checkStack, 0, this.checkStack, 0, board.checkStack.length);

        System.arraycopy(board.pieceSquareTable, 0, pieceSquareTable, 0, board.pieceSquareTable.length);

        Setup.init(false);
    }

    /**
     * legal chess move generation
     * @return an array of length 128 populated with fully legal chess moves, and 0s.
     * Use @see com.github.louism33.chesscore.MoveParser.class for methods to interpret the move object
     */
    public final int[] generateLegalMoves() {
        Assert.assertNotNull(this.legalMoveStack[legalMoveStackIndex]);
        // only clean array of moves if it has something in it
        if (this.legalMoveStack[legalMoveStackIndex][0] != 0) {
            Arrays.fill(this.legalMoveStack[legalMoveStackIndex], 0);
        }

        final long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        final long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        final long friends, enemies;

        myPawns = pieces[turn][PAWN];
        myKnights = pieces[turn][KNIGHT];
        myBishops = pieces[turn][BISHOP];
        myRooks = pieces[turn][ROOK];
        myQueens = pieces[turn][QUEEN];
        myKing = pieces[turn][KING];

        enemyPawns = pieces[1 - turn][PAWN];
        enemyKnights = pieces[1 - turn][KNIGHT];
        enemyBishops = pieces[1 - turn][BISHOP];
        enemyRooks = pieces[1 - turn][ROOK];
        enemyQueens = pieces[1 - turn][QUEEN];
        enemyKing = pieces[1 - turn][KING];

        getPieces();
        friends = this.pieces[turn][ALL_COLOUR_PIECES];
        enemies = this.pieces[1 - turn][ALL_COLOUR_PIECES];

        final long allPieces = friends | enemies;


        final long checkingPieces = bitboardOfPiecesThatLegalThreatenSquare(turn, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, 0,
                allPieces, 2);
        
        this.checkingPieces = checkingPieces;
        
        final int numberOfCheckers = populationCount(checkingPieces);

        if (numberOfCheckers > 1) {
            inCheckRecorder = true;

            addKingLegalMovesOnly(this.legalMoveStack[legalMoveStackIndex], turn, this.pieces, pieceSquareTable,
                    myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    friends, allPieces);
            return this.legalMoveStack[legalMoveStackIndex];
        }

        final long currentPinnedPieces = whichPiecesArePinned(myKing,
                enemyBishops, enemyRooks, enemyQueens,
                friends, allPieces);

        pinnedPieces = currentPinnedPieces;

        final boolean hasPreviousMove = hasPreviousMove();
        if (numberOfCheckers == 1) {
            inCheckRecorder = true;

            addCheckEvasionMoves(this.checkingPieces, this.legalMoveStack[legalMoveStackIndex], turn, pieceSquareTable,
                    this.pieces, hasPreviousMove, moveStackArrayPeek(), currentPinnedPieces,
                    myPawns, myKnights, myBishops, myRooks, myQueens, myKing,
                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                    enemies, friends, allPieces);

            return this.legalMoveStack[legalMoveStackIndex];
        }

        inCheckRecorder = false;

        long pinnedPieces = currentPinnedPieces;

        // not in check moves
        final long emptySquares = ~allPieces;
        final long promotablePawns = myPawns & PENULTIMATE_RANKS[turn];
        final long pinnedPiecesAndPromotingPawns = pinnedPieces | promotablePawns;

        addCastlingMoves(this.legalMoveStack[legalMoveStackIndex], turn, castlingRights,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                allPieces);

        addKingLegalMovesOnly(this.legalMoveStack[legalMoveStackIndex], turn, this.pieces, pieceSquareTable,
                myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing,
                friends, allPieces);

        if (pinnedPieces == 0) {
            addPromotionMoves
                    (this.legalMoveStack[legalMoveStackIndex], turn, pieceSquareTable, 0, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (this.legalMoveStack[legalMoveStackIndex], this.pieces, turn, pieceSquareTable, promotablePawns, emptySquares, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            if (hasPreviousMove) {
                addEnPassantMoves
                        (this.legalMoveStack[legalMoveStackIndex], moveStackArrayPeek(), turn, promotablePawns, emptySquares, enemies,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
            }
        } else {
            addPromotionMoves
                    (this.legalMoveStack[legalMoveStackIndex], turn, pieceSquareTable, pinnedPieces, emptySquares, enemies,
                            myPawns,
                            enemies, allPieces);

            addAllMovesWithoutKing
                    (this.legalMoveStack[legalMoveStackIndex], this.pieces, turn, pieceSquareTable, pinnedPiecesAndPromotingPawns, ~allPieces, enemies,
                            myKnights, myBishops, myRooks, myQueens,
                            allPieces);

            if (hasPreviousMove) {
                addEnPassantMoves
                        (this.legalMoveStack[legalMoveStackIndex], moveStackArrayPeek(), turn, pinnedPiecesAndPromotingPawns, emptySquares, enemies,
                                myPawns, myKing,
                                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                        );
            }

            // pinned pieces moves
            while (pinnedPieces != 0) {
                long pinnedPiece = getFirstPiece(pinnedPieces);
                long pinningPiece = xrayQueenAttacks(allPieces, pinnedPiece, myKing) & enemies;
                long pushMask = extractRayFromTwoPiecesBitboardInclusive(myKing, pinningPiece)
                        ^ (pinningPiece | myKing);

                final int pinnedPieceIndex = numberOfTrailingZeros(pinnedPiece);
                final long mask = (pushMask | pinningPiece);

                if ((pinnedPiece & myKnights) != 0) {
                    // knights cannot move cardinally or diagonally, and so cannot move while pinned
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myPawns) != 0) {
                    final long allButPinnedFriends = friends & ~pinnedPiece;

                    if ((pinnedPiece & PENULTIMATE_RANKS[turn]) == 0) {

                        final long table = singlePawnPushes(pinnedPiece, turn, pushMask, allPieces)
                                | singlePawnCaptures(pinnedPiece, turn, pinningPiece);
                        if (table != 0) {
                            addMovesFromAttackTableMasterBetter(this.legalMoveStack[legalMoveStackIndex],
                                    table,
                                    pinnedPieceIndex, PIECE[turn][PAWN], pieceSquareTable);
                        }

                        // a pinned pawn may still EP
                        if (hasPreviousMove) {
                            addEnPassantMoves(this.legalMoveStack[legalMoveStackIndex], 
                                    moveStackArrayPeek(), turn, allButPinnedFriends, pushMask, pinningPiece,
                                    myPawns, myKing,
                                    enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing, allPieces
                            );
                        }
                    } else {
                        // a pinned pawn may still promote, through a capture of the pinner
                        addPromotionMoves(this.legalMoveStack[legalMoveStackIndex], turn, 
                                pieceSquareTable, allButPinnedFriends, pushMask, pinningPiece,
                                myPawns,
                                enemies, allPieces);
                    }
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myBishops) != 0) {
                    final long table = singleBishopTable(allPieces, pinnedPiece, UNIVERSE) & mask;
                    if (table != 0) {
                        addMovesFromAttackTableMasterBetter(this.legalMoveStack[legalMoveStackIndex],
                                table,
                                pinnedPieceIndex, PIECE[turn][BISHOP], pieceSquareTable);
                    }
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myRooks) != 0) {
                    final long table = singleRookTable(allPieces, pinnedPiece, UNIVERSE) & mask;
                    if (table != 0) {
                        addMovesFromAttackTableMasterBetter(this.legalMoveStack[legalMoveStackIndex],
                                table,
                                pinnedPieceIndex, PIECE[turn][ROOK], pieceSquareTable);
                    }
                    pinnedPieces &= pinnedPieces - 1;
                    continue;
                }
                if ((pinnedPiece & myQueens) != 0) {
                    final long table = singleQueenTable(allPieces, pinnedPiece, UNIVERSE) & mask;
                    if (table != 0) {
                        addMovesFromAttackTableMasterBetter(this.legalMoveStack[legalMoveStackIndex],
                                table,
                                pinnedPieceIndex, PIECE[turn][QUEEN], pieceSquareTable);
                    }
                }

                pinnedPieces &= pinnedPieces - 1;
            }
        }

        return this.legalMoveStack[legalMoveStackIndex];
    }

    /**
     * Updates the board with the move you want.
     * @param move the non-0 move you want to make of this board.
     */
    public final void makeMoveAndFlipTurnBetter(int move) {
        this.rotateMoveIndexUp();
        Assert.assertNotEquals(move, 0);
        masterStackPush();

        final int sourceSquare = getSourceIndex(move);
        final int destinationIndex = getDestinationIndex(move);
        final int sourcePieceIdentifier = pieceSquareTable[sourceSquare] - 1;
        final boolean captureMove = isCaptureMove(move);
        final long destinationPiece = newPieceOnSquare(destinationIndex);
        final long destinationZH = zobristHashPieces[destinationIndex][sourcePieceIdentifier];

        zobristHash ^= zobristHashPieces[sourceSquare][sourcePieceIdentifier];
        zobristHash ^= destinationZH;

        if (captureMove){
            zobristHash ^= zobristHashPieces[destinationIndex][pieceSquareTable[destinationIndex] - 1];
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        if (hasPreviousMove()){
            zobristHash = updateWithEPFlags(moveStackArrayPeek(), zobristHash);
        }

        if (move == 0) {
            moveStackArrayPush(buildStackDataBetter(0, turn, getFiftyMoveCounter(), castlingRights, NULL_MOVE));
            return;
        }

        boolean resetFifty = true;

        if (isSpecialMove(move)) {
            switch (move & SPECIAL_MOVE_MASK) {
                case CASTLING_MASK:
                    int originalRookIndex = 0;
                    int newRookIndex = 0;
                    switch (destinationIndex) {
                        case 1:
                            originalRookIndex = 0;
                            newRookIndex = destinationIndex + 1;
                            break;
                        case 5:
                            originalRookIndex = 7;
                            newRookIndex = destinationIndex - 1;
                            break;
                        case 57:
                            originalRookIndex = 56;
                            newRookIndex = destinationIndex + 1;
                            break;
                        case 61:
                            originalRookIndex = 63;
                            newRookIndex = destinationIndex - 1;
                            break;
                    }

                    int myRook = pieceSquareTable[originalRookIndex] - 1;
                    zobristHash ^= zobristHashPieces[originalRookIndex][myRook];
                    zobristHash ^= zobristHashPieces[newRookIndex][myRook];
                    
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, CASTLING));
                    castlingRights = makeCastlingMove(castlingRights, pieces, pieceSquareTable, move);
                    break;

                case ENPASSANT_MASK:
                    long victimPawn = turn == WHITE ? destinationPiece >>> 8 : destinationPiece << 8;
                    zobristHash ^= zobristHashPieces
                            [numberOfTrailingZeros(victimPawn)]
                            [pieceSquareTable[numberOfTrailingZeros(victimPawn)] - 1];
                    
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, ENPASSANTCAPTURE));
                    makeEnPassantMove(pieces, pieceSquareTable, turn, move);
                    break;

                case PROMOTION_MASK:
                    int whichPromotingPiece = 0;

                    switch (move & WHICH_PROMOTION){
                        case KNIGHT_PROMOTION_MASK:
                            whichPromotingPiece = 2 + turn * 6;
                            break;
                        case BISHOP_PROMOTION_MASK:
                            whichPromotingPiece = 3 + turn * 6;
                            break;
                        case ROOK_PROMOTION_MASK:
                            whichPromotingPiece = 4 + turn * 6;
                            break;
                        case QUEEN_PROMOTION_MASK:
                            whichPromotingPiece = 5 + turn * 6;
                            break;
                    }

                    /*
                    remove my pawn from zh
                     */
                    zobristHash ^= destinationZH;

                    Assert.assertTrue(whichPromotingPiece != 0);
                    long promotionZH = zobristHashPieces[destinationIndex][whichPromotingPiece - 1];
                    zobristHash ^= promotionZH;
                    
                    moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, PROMOTION));
                    makePromotingMove(pieces, pieceSquareTable, turn, move);
                    break;
            }
        } else {
            if (captureMove) {
                moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICCAPTURE));
            } else if (enPassantPossibility(turn, pieces[turn][PAWN], move)) {
                int whichFile = 8 - getSourceIndex(move) % 8;
                moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, ENPASSANTVICTIM, whichFile));
            } else {
                switch (pieceSquareTable[getSourceIndex(move)]) {
                    case WHITE_PAWN: 
                    case BLACK_PAWN: 
                        moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICLOUDPUSH));
                        break;
                    default:
                        // increment 50 move rule
                        resetFifty = false;
                        moveStackArrayPush(buildStackDataBetter(move, turn, getFiftyMoveCounter(), castlingRights, BASICQUIETPUSH));
                }

            }

            makeRegularMove(pieces, pieceSquareTable, move);
        }

        // todo update unmake move to compensate
//        if (resetFifty) {
//            setFiftyMoveCounter(0);
//        }
//        else {
//            setFiftyMoveCounter(getFiftyMoveCounter() + 1);
//        }


        castleFlagManager(move);

        Assert.assertTrue(hasPreviousMove());
        zobristHash = (updateHashPostMove(moveStackArrayPeek(), castlingRights, zobristHash));

        this.turn = 1 - this.turn;
    }

    private void castleFlagManager(int move) {
        // disable relevant castle flag whenever a piece moves into the relevant square.
        switch (getSourceIndex(move)) {
            case 0:
                castlingRights &= castlingRightsMask[WHITE][K];
                break;
            case 3:
                castlingRights &= castlingRightsMask[WHITE][K];
            case 7:
                castlingRights &= castlingRightsMask[WHITE][Q];
                break;
            case 56:
                castlingRights &= castlingRightsMask[BLACK][K];
                break;
            case 59:
                castlingRights &= castlingRightsMask[BLACK][K];
            case 63:
                castlingRights &= castlingRightsMask[BLACK][Q];
                break;
        }
        switch (getDestinationIndex(move)) {
            case 0:
                castlingRights &= castlingRightsMask[WHITE][K];
                break;
            case 3:
                castlingRights &= castlingRightsMask[WHITE][K];
            case 7:
                castlingRights &= castlingRightsMask[WHITE][Q];
                break;
            case 56:
                castlingRights &= castlingRightsMask[BLACK][K];
                break;
            case 59:
                castlingRights &= castlingRightsMask[BLACK][K];
            case 63:
                castlingRights &= castlingRightsMask[BLACK][Q];
                break;
        }
    }

    private static boolean enPassantPossibility(int turn, long myPawns, int move) {
        // determine if flag should be added to enable EP on next turn
        long sourceSquare = newPieceOnSquare(getSourceIndex(move));
        long destinationSquare = newPieceOnSquare(getDestinationIndex(move));
        long HOME_RANK = PENULTIMATE_RANKS[1 - turn];
        long enPassantPossibilityRank = ENPASSANT_RANK[turn];

        if ((sourceSquare & HOME_RANK) == 0) {
            return false;
        }

        if ((sourceSquare & myPawns) == 0) {
            return false;
        }
        return (destinationSquare & enPassantPossibilityRank) != 0;
    }

    private void rotateMoveIndexUp() {
        this.legalMoveStackIndex = (this.legalMoveStackIndex + 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }


    private void rotateMoveIndexDown() {
        this.legalMoveStackIndex = (this.legalMoveStackIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }

    /**
     * Completely undoes the last made move, and changes the side to play
     */
    public void unMakeMoveAndFlipTurn() {
        this.rotateMoveIndexDown();

        Assert.assertTrue(hasPreviousMove());

        masterStackPop();

        long pop = moveStackData;

        if (StackDataUtil.getMove(pop) == 0) {
            turn = StackDataUtil.getTurn(pop);
            return;
        }

        int pieceToMoveBackIndex = getDestinationIndex(StackDataUtil.getMove(pop));
        int squareToMoveBackTo = getSourceIndex(StackDataUtil.getMove(pop));
        int basicReversedMove = buildMove(pieceToMoveBackIndex, pieceSquareTable[pieceToMoveBackIndex],
                squareToMoveBackTo, NO_PIECE);

        switch (StackDataUtil.getSpecialMove(pop)) {
            //double pawn push
            case ENPASSANTVICTIM:
            case BASICQUIETPUSH:
            case BASICLOUDPUSH:
                makeRegularMove(pieces, pieceSquareTable, basicReversedMove);
                break;

            case BASICCAPTURE:
                makeRegularMove(pieces, pieceSquareTable, basicReversedMove);
                int takenPiece = getVictimPieceInt(StackDataUtil.getMove(pop));
                if (getVictimPieceInt(StackDataUtil.getMove(pop)) != 0) {
                    togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(pieceToMoveBackIndex), takenPiece);
                }
                break;

            case ENPASSANTCAPTURE:
                makeRegularMove(pieces, pieceSquareTable, basicReversedMove);
                if (StackDataUtil.getTurn(pop) == BLACK) {
                    togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(pieceToMoveBackIndex - 8), BLACK_PAWN);
                } else {
                    togglePiecesFrom(pieces, pieceSquareTable, newPieceOnSquare(pieceToMoveBackIndex + 8), WHITE_PAWN);
                }
                break;

            case CASTLING:
                // king moved to:
                long originalRook, newRook,
                        originalKing = newPieceOnSquare(squareToMoveBackTo),
                        newKing = newPieceOnSquare(pieceToMoveBackIndex);

                if (getTurn(pop) == BLACK) {
                    originalRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? 0 : 7);
                    newRook = newPieceOnSquare(pieceToMoveBackIndex == 1 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                    togglePiecesFrom(pieces, pieceSquareTable, newKing, WHITE_KING);
                    togglePiecesFrom(pieces, pieceSquareTable, newRook, WHITE_ROOK);
                } else {
                    originalRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? 56 : 63);
                    newRook = newPieceOnSquare(pieceToMoveBackIndex == 57 ? pieceToMoveBackIndex + 1 : pieceToMoveBackIndex - 1);
                    togglePiecesFrom(pieces, pieceSquareTable, newKing, BLACK_KING);
                    togglePiecesFrom(pieces, pieceSquareTable, newRook, BLACK_ROOK);
                }

                pieces[1 - StackDataUtil.getTurn(pop)][KING] |= originalKing;
                pieces[1 - StackDataUtil.getTurn(pop)][ROOK] |= originalRook;

                pieceSquareTable[squareToMoveBackTo] = WHITE_KING + (1 - StackDataUtil.getTurn(pop)) * 6;
                pieceSquareTable[numberOfTrailingZeros(originalRook)] = WHITE_ROOK + (1 - StackDataUtil.getTurn(pop)) * 6;
                break;

            case PROMOTION:
                long sourceSquare = newPieceOnSquare(pieceToMoveBackIndex);
                long destinationSquare = newPieceOnSquare(squareToMoveBackTo);
                long mask = ~(sourceSquare | destinationSquare);

                pieces[WHITE][PAWN] &= mask;
                pieces[WHITE][KNIGHT] &= mask;
                pieces[WHITE][BISHOP] &= mask;
                pieces[WHITE][ROOK] &= mask;
                pieces[WHITE][QUEEN] &= mask;
                pieces[WHITE][KING] &= mask;

                pieces[BLACK][PAWN] &= mask;
                pieces[BLACK][KNIGHT] &= mask;
                pieces[BLACK][BISHOP] &= mask;
                pieces[BLACK][ROOK] &= mask;
                pieces[BLACK][QUEEN] &= mask;
                pieces[BLACK][KING] &= mask;

                pieceSquareTable[pieceToMoveBackIndex] = 0;
                pieceSquareTable[squareToMoveBackTo] = 0;

                togglePiecesFrom(pieces, pieceSquareTable, destinationSquare,
                        StackDataUtil.getTurn(pop) == 1 ? WHITE_PAWN : BLACK_PAWN);
                int takenPiecePromotion = getVictimPieceInt(StackDataUtil.getMove(pop));
                if (takenPiecePromotion > 0) {
                    togglePiecesFrom(pieces, pieceSquareTable, sourceSquare, takenPiecePromotion);
                }
                break;
        }

        castlingRights = StackDataUtil.getCastlingRights(pop);
        turn = 1 - turn;
    }

    private static void makeRegularMove(long[][] pieces, int[] pieceSquareTable, int move) {
        final long destinationPiece = newPieceOnSquare(getDestinationIndex(move));
        removePieces(pieces, pieceSquareTable, newPieceOnSquare(getSourceIndex(move)), destinationPiece, move);
        togglePiecesFrom(pieces, pieceSquareTable, destinationPiece, getMovingPieceInt(move));
    }

    /**
     * Makes a null move on the board. Make sure to unmake it afterwards
     */
    public void makeNullMoveAndFlipTurn() {
        masterStackPush();

        if (hasPreviousMove()) {
            zobristHash = (updateWithEPFlags(moveStackArrayPeek(), zobristHash));
        }

        moveStackArrayPush(buildStackDataBetter(0, turn, getFiftyMoveCounter(), castlingRights, NULL_MOVE));

        zobristHash = zobristFlipTurn(zobristHash);

        this.turn = 1 - this.turn;
    }


    /**
     * Unmakes a null move on the board.
     */
    public void unMakeNullMoveAndFlipTurn() {
        Assert.assertTrue(hasPreviousMove());
        masterStackPop();
        this.turn = 1 - this.turn;
    }

    public boolean isWhiteTurn() {
        return this.turn == WHITE;
    }

    /**
     * Tells you if the specified player is in check
     *
     * @param white true if white to play
     * @return true if in check, otherwise false
     */
    public boolean inCheck(boolean white) {
        long myKing, enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing, enemies, friends;
        if (white) {
            myKing = pieces[WHITE][KING];
            enemyPawns = pieces[BLACK][PAWN];
            enemyKnights = pieces[BLACK][KNIGHT];
            enemyBishops = pieces[BLACK][BISHOP];
            enemyRooks = pieces[BLACK][ROOK];
            enemyQueen = pieces[BLACK][QUEEN];
            enemyKing = pieces[BLACK][KING];

            enemies = blackPieces();
            friends = whitePieces();
        } else {
            myKing = pieces[BLACK][KING];
            enemyPawns = pieces[WHITE][PAWN];
            enemyKnights = pieces[WHITE][KNIGHT];
            enemyBishops = pieces[WHITE][BISHOP];
            enemyRooks = pieces[WHITE][ROOK];
            enemyQueen = pieces[WHITE][QUEEN];
            enemyKing = pieces[WHITE][KING];

            enemies = whitePieces();
            friends = blackPieces();
        }

        return boardInCheckBetter(turn, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                allPieces(), 1);

    }

    /**
     * @param white the player
     * @return true if it is a draw by repetition
     */
    public boolean drawByRepetition(boolean white) {
        return isDrawByRepetition(this);
    }

    /**
     * @param white the player
     * @return true if draw by repetition
     */
    private boolean drawByInsufficientMaterial(boolean white) {
        return isDrawByInsufficientMaterial(this);
    }

    /**
     * @param white the player
     * @return true if this side does not have enough pieces to ever win the game
     */
    private boolean colourHasInsufficientMaterialToMate(boolean white) {
        return CheckHelper.colourHasInsufficientMaterialToMate(this, white);
    }

    /**
     * @return true if in checkmate
     */
    public boolean inCheckmate() {
        if (!this.inCheck(isWhiteTurn())) {
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    /**
     * @return true if in stalemate
     */
    public boolean inStalemate() {
        if (this.inCheck(isWhiteTurn())) {
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    public boolean previousMoveWasPawnPushToSix() {
        if (!hasPreviousMove()) {
            return false;
        }
        long peek = moveStackArrayPeek();
        return moveIsPawnPushSix(StackDataUtil.getMove(peek));
    }

    public boolean previousMoveWasPawnPushToSeven() {
        if (!hasPreviousMove()) {
            return false;
        }
        long peek = moveStackArrayPeek();
        return moveIsPawnPushSeven(StackDataUtil.getMove(peek));
    }

    public boolean moveIsCaptureOfLastMovePiece(int move) {
        if (!hasPreviousMove()) {
            return false;
        }

        long peek = moveStackArrayPeek();
        if (StackDataUtil.getMove(peek) == 0) {
            return false;
        }
        int previousMoveDestinationIndex = getDestinationIndex(StackDataUtil.getMove(peek));
        return (getDestinationIndex(move) == previousMoveDestinationIndex);
    }


    private long whitePieces() {
        this.pieces[WHITE][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[WHITE][ALL_COLOUR_PIECES] |= this.pieces[WHITE][i];
        }
        return this.pieces[WHITE][ALL_COLOUR_PIECES];
    }

    private void getPieces(){
        long b = 0, w = 0;
        for (int i = PAWN; i <= KING; i++) {
            w |= this.pieces[WHITE][i];
            b |= this.pieces[BLACK][i];
        }
        this.pieces[WHITE][ALL_COLOUR_PIECES] = w;
        this.pieces[BLACK][ALL_COLOUR_PIECES] = b;
    }

    private long blackPieces() {
        this.pieces[BLACK][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[BLACK][ALL_COLOUR_PIECES] |= this.pieces[BLACK][i];
        }
        return this.pieces[BLACK][ALL_COLOUR_PIECES];
    }

    public long allPieces() {
        getPieces();
        return this.pieces[WHITE][ALL_COLOUR_PIECES] | this.pieces[BLACK][ALL_COLOUR_PIECES];
    }

    @Override
    public boolean equals(Object o) {
        // we do not check equality for fields that change during move gen
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chessboard that = (Chessboard) o;
        this.whitePieces();
        this.blackPieces();
        that.whitePieces();
        that.blackPieces();
        return turn == that.turn &&
                castlingRights == that.castlingRights &&
                fiftyMoveCounter == that.fiftyMoveCounter &&
                zobristHash == that.zobristHash &&
                masterIndex == that.masterIndex &&
                Arrays.deepEquals(pieces, that.pieces) &&
                Arrays.equals(zobristHashStack, that.zobristHashStack) &&
                Arrays.equals(pinnedPiecesArray, that.pinnedPiecesArray) &&
                Arrays.equals(checkStack, that.checkStack) &&
                Arrays.equals(pieceSquareTable, that.pieceSquareTable)
                ;
    }

    @Override
    public String toString() {
        String turn = isWhiteTurn() ? "It is white's turn." : "It is black's turn.";
        return "\n" + Art.boardArt(this) + "\n" + turn
                ;
    }

    private int legalMoveStackIndex = 0;
    private int masterIndex = 0;
    private int moveStackIndex = 0;

    private void rotateMasterIndexUp() {
        this.masterIndex = (this.masterIndex + 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }
    private void rotateMasterIndexDown() {
        this.masterIndex = (this.masterIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }
    
    private void rotateMoveStackIndexUp() {
        this.moveStackIndex = (this.moveStackIndex + 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }
    private void rotateMoveStackIndexDown() {
        this.moveStackIndex = (this.moveStackIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }

    //todo can this return incorrectly after 64 moves have been made / simulated
    private boolean hasPreviousMove() {
        return pastMoveStackArray[(this.moveStackIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength] != 0;
    }
    
    private void masterStackPush() {
        checkStack[masterIndex] = this.inCheckRecorder;
        inCheckRecorder = false;

        pinnedPiecesArray[masterIndex] = this.pinnedPieces;
        pinnedPieces = 0;

        zobristHashStack[masterIndex] = zobristHash;
        rotateMasterIndexUp();
    }

    private void masterStackPop() {
//        masterIndex--;
        rotateMasterIndexDown();
        inCheckRecorder = checkStack[masterIndex];
        checkStack[masterIndex] = false;

        pinnedPieces = pinnedPiecesArray[masterIndex];
        pinnedPiecesArray[masterIndex] = 0;

        zobristHash = zobristHashStack[masterIndex];
        zobristHashStack[masterIndex] = 0;

        pastMoveStackArray[moveStackIndex] = 0;
        rotateMoveStackIndexDown();
        moveStackData = pastMoveStackArray[moveStackIndex];
    }

    private void moveStackArrayPush(long l) {
        pastMoveStackArray[moveStackIndex] = l;
        rotateMoveStackIndexUp();
    }

    private long moveStackArrayPeek() {
        return moveStackIndex > 0 ? pastMoveStackArray[moveStackIndex - 1] : 0;
    }

    /**
     * New Chessboard based on a FEN string
     * @param fen the String of pieces turn and castling rights and ep square and counters to make a board from
     */
    public Chessboard(String fen) {
        char[] c = fen.toCharArray();
        int phase = 1;
        int square = 64, whichPiece;

        Arrays.fill(this.pieceSquareTable, 0);
        Arrays.fill(this.pieces[WHITE], 0);
        Arrays.fill(this.pieces[BLACK], 0);

        castlingRights = 0;

        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                phase++;
                continue;
            }
            if (c[i] == '-') {
                continue;
            }
            switch (phase) {
                case 1: //board
                    switch (c[i]) {
                        case 'P':
                            whichPiece = WHITE_PAWN;
                            break;
                        case 'N':
                            whichPiece = WHITE_KNIGHT;
                            break;
                        case 'B':
                            whichPiece = WHITE_BISHOP;
                            break;
                        case 'R':
                            whichPiece = WHITE_ROOK;
                            break;
                        case 'Q':
                            whichPiece = WHITE_QUEEN;
                            break;
                        case 'K':
                            whichPiece = WHITE_KING;
                            break;

                        case 'p':
                            whichPiece = BLACK_PAWN;
                            break;
                        case 'n':
                            whichPiece = BLACK_KNIGHT;
                            break;
                        case 'b':
                            whichPiece = BLACK_BISHOP;
                            break;
                        case 'r':
                            whichPiece = BLACK_ROOK;
                            break;
                        case 'q':
                            whichPiece = BLACK_QUEEN;
                            break;
                        case 'k':
                            whichPiece = BLACK_KING;
                            break;
                        case '/':
                            continue;
                        default:
                            square -= (c[i] - 48);
                            continue;
                    }
                    square--;

                    this.pieces[whichPiece / 7][whichPiece < 7 ? whichPiece : whichPiece - 6] |= newPieceOnSquare(square);
                    pieceSquareTable[square] = whichPiece;

                    break;

                case 2: //player
                    if (c[i] == 'b') {
                        turn = BLACK;
                    }
                    else{
                        turn = WHITE;
                    }
                    break;

                case 3: //castle
                    switch (c[i]) {
                        case 'K':
                            castlingRights |= castlingRightsOn[WHITE][K];
                            break;
                        case 'Q':
                            castlingRights |= castlingRightsOn[WHITE][Q];
                            break;
                        case 'q':
                            castlingRights |= castlingRightsOn[BLACK][Q];
                            break;
                        case 'k':
                            castlingRights |= castlingRightsOn[BLACK][K];
                            break;
                    }
                    break;

                case 4: //ep
                    final long item = buildStackDataBetter(0, turn, fiftyMoveCounter,
                            castlingRights, ENPASSANTVICTIM, (int) c[i] - 96);
                    moveStackArrayPush(item);
                    phase++;
                    break;

                case 5:
                    fiftyMoveCounter = c[i];
                    phase++;
                    break;

                case 6:
                    fullMoveCounter = c[i];
                    phase++;
                    break;
            }
        }
        zobristHash = boardToHash();
        Setup.init(false);
    }


}
