package com.github.louism33.chesscore;

import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.CheckHelper.*;
import static com.github.louism33.chesscore.MakeMoveAndHashUpdate.*;
import static com.github.louism33.chesscore.StackDataUtil.ENPASSANTVICTIM;
import static com.github.louism33.chesscore.StackDataUtil.buildStackData;

public class Chessboard implements Cloneable{

    private ChessboardDetails details;

    long[][] pieces = new long[2][7];
    long[] allPieces = new long[3];
    
    int turn;
    /*
    castling rights bits:
    BK BA WK WQ
     */
    int castlingRights = 0xf; 

    private int fiftyMoveCounter = 0;
    public int getFiftyMoveCounter() {
        return fiftyMoveCounter;
    }

    public void setFiftyMoveCounter(int fiftyMoveCounter) {
        this.fiftyMoveCounter = fiftyMoveCounter;
    }


    private int index = 0;
    private int zobristIndex = 0;

    private long zobristHash;
    long moveStackData;

    private final int maxDepthAndArrayLength = 64;
    private final int maxNumberOfMovesInAnyPosition = 128;

    int[] moves = new int[maxNumberOfMovesInAnyPosition];
    
    private int[][] legalMoveStack = new int[maxDepthAndArrayLength][maxNumberOfMovesInAnyPosition];
    
    private long[] zobristHashStack = new long[maxDepthAndArrayLength];
    private long[] pastMoveStackArray = new long[maxDepthAndArrayLength];
    
    public boolean inCheckRecorder;
    public long pinnedPieces;
    public long[] pinnedPiecesArray = new long[maxDepthAndArrayLength];
    public boolean[] checkStack = new boolean[maxDepthAndArrayLength];
    
    
    /**
     * A new Chessboard in the starting position, white to play.
     */
    public Chessboard() {
        castlingRights = 0xf;
        init();

        
        Assert.assertEquals(details.whiteCanCastleQ, (this.castlingRights & 0b0001) == 0b0001);
        Assert.assertEquals(details.whiteCanCastleK, (this.castlingRights & 0b0010) == 0b0010);
        Assert.assertEquals(details.blackCanCastleQ, (this.castlingRights & 0b0100) == 0b0100);
        Assert.assertEquals(details.blackCanCastleK, (this.castlingRights & 0b1000) == 0b1000);


        makeZobrist();
        Setup.init(false);

        System.arraycopy(INITIAL_PIECES[BLACK], 0, this.pieces[BLACK], 0, INITIAL_PIECES[BLACK].length);
        System.arraycopy(INITIAL_PIECES[WHITE], 0, this.pieces[WHITE], 0, INITIAL_PIECES[WHITE].length);

        turn = WHITE;
    }

    /**
     * New Chessboard based on a FEN string
     * @param fen the String of pieces turn and castling rights and ep square and counters to make a board from
     */
    public Chessboard(String fen) {
        details = new ChessboardDetails();
        makeBoardBasedOnFENSpecific(fen);

        this.zobristHash = ZobristHashUtil.boardToHash(this);
        Setup.init(false);
    }

    Chessboard(boolean blank){
        this.details = new ChessboardDetails();
        this.zobristHash = ZobristHashUtil.boardToHash(this);
    }

    /**
     * Copy Constructor
     * @param board the chessboard you want an exact copy of
     */
    public Chessboard(Chessboard board) {
        this.details = new ChessboardDetails();

        System.arraycopy(board.pastMoveStackArray, 0, this.pastMoveStackArray, 0, board.pastMoveStackArray.length);
        System.arraycopy(board.zobristHashStack, 0, this.zobristHashStack, 0, board.zobristHashStack.length);
        
        System.arraycopy(board.checkStack, 0, this.checkStack, 0, board.checkStack.length);
        System.arraycopy(board.pinnedPiecesArray, 0, this.pinnedPiecesArray, 0, board.pinnedPiecesArray.length);


        this.inCheckRecorder = board.inCheckRecorder;
        this.pinnedPieces = board.pinnedPieces;
        
        this.index = board.index;
        this.zobristIndex = board.zobristIndex;
        this.checkIndex = board.checkIndex;
        this.pinIndex = board.pinIndex;
        
        System.arraycopy(board.pieces[WHITE], 0, this.pieces[WHITE], 0, 7);
        System.arraycopy(board.pieces[BLACK], 0, this.pieces[BLACK], 0, 7);
        
        
        
        this.setWhitePawns(board.getWhitePawns());
        this.setWhiteKnights(board.getWhiteKnights());
        this.setWhiteBishops(board.getWhiteBishops());
        this.setWhiteRooks(board.getWhiteRooks());
        this.setWhiteQueen(board.getWhiteQueen());
        this.setWhiteKing(board.getWhiteKing());

        this.setBlackPawns(board.getBlackPawns());
        this.setBlackKnights(board.getBlackKnights());
        this.setBlackBishops(board.getBlackBishops());
        this.setBlackRooks(board.getBlackRooks());
        this.setBlackQueen(board.getBlackQueen());
        this.setBlackKing(board.getBlackKing());


        this.castlingRights = board.castlingRights;
        this.turn = board.turn;

        this.setWhiteCanCastleK(board.isWhiteCanCastleK());
        this.setBlackCanCastleK(board.isBlackCanCastleK());
        this.setWhiteCanCastleQ(board.isWhiteCanCastleQ());
        this.setBlackCanCastleQ(board.isBlackCanCastleQ());

        this.setWhiteTurn(board.isWhiteTurn());

        // this.zobrist = that.zobrist...
        this.makeZobrist();
        Setup.init(false);

    }

    /**
     * @return a String representation of the current board.
     */
    public String getFenRepresentation(){
        return "not yet";
    }

    /** 
     * legal chess move generation
     * @return an array of length 128 populated with fully legal chess moves, and 0s. 
     * Only make moves that are not 0!
     * Use @see com.github.louism33.chesscore.MoveParser.class for methods to interpret the move object
     */
    public int[] generateLegalMoves() {
        Arrays.fill(this.legalMoveStack[legalMoveStackIndex], 0);

        MoveGeneratorMaster.generateLegalMoves(this,
                this.legalMoveStack[legalMoveStackIndex], turn);

        return this.legalMoveStack[legalMoveStackIndex];
    }
    /**
     * Updates the board with the move you want.
     * @param move the non-0 move you want to make of this board.
     */
    public void makeMoveAndFlipTurn(int move){
        this.rotateMoveIndexUp();
        makeMoveAndHashUpdate(this, move);
        flipTurn();
        
    }

    private void rotateMoveIndexUp(){
        this.legalMoveStackIndex = (this.legalMoveStackIndex + 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }


    private void rotateMoveIndexDown(){
        this.legalMoveStackIndex = (this.legalMoveStackIndex - 1 + this.maxDepthAndArrayLength) % this.maxDepthAndArrayLength;
    }
    /**
     * Completely undoes the last made move, and changes the side to play
     */
    public void unMakeMoveAndFlipTurn(){
        this.rotateMoveIndexDown();
        UnMakeMoveAndHashUpdate(this);
    }

    /**
     * Makes a null move on the board. Make sure to unmake it afterwards
     */
    public void makeNullMoveAndFlipTurn(){
        makeNullMoveAndHashUpdate(this);
        flipTurn();
    }


    /**
     * Unmakes a null move on the board.
     */
    public void unMakeNullMoveAndFlipTurn(){
        unMakeNullMove(this);
    }


    /**
     * Changes whose turn it is
     */
    public void flipTurn(){
        this.turn = 1 - this.turn;
        setWhiteTurn(!isWhiteTurn());
    }

    /**
     * Tells you if the specified player is in check
     * @param white true if white to play
     * @return true if in check, otherwise false
     */
    public boolean inCheck(boolean white){
        long myKing, enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing, enemies, friends;
        if (white){
            myKing = getWhiteKing();
            enemyPawns = getBlackPawns();
            enemyKnights = getBlackKnights();
            enemyBishops = getBlackBishops();
            enemyRooks = getBlackRooks();
            enemyQueen = getBlackQueen();
            enemyKing = getBlackKing();

            enemies = blackPieces();
            friends = whitePieces();
        } else {
            myKing = getBlackKing();
            enemyPawns = getWhitePawns();
            enemyKnights = getWhiteKnights();
            enemyBishops = getWhiteBishops();
            enemyRooks = getWhiteRooks();
            enemyQueen = getWhiteQueen();
            enemyKing = getWhiteKing();

            enemies = whitePieces();
            friends = blackPieces();
        }

        return boardInCheck(white, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                allPieces());

    }

    /**
     * @param white the player 
     * @return true if it is a draw by repetition
     */
    public boolean drawByRepetition (boolean white){
        return isDrawByRepetition(this);
    }

    /**
     * @param white the player
     * @return true if draw by repetition
     */
    private boolean drawByInsufficientMaterial (boolean white){
        return isDrawByInsufficientMaterial(this);
    }

    /**
     * @param white the player
     * @return true if this side does not have enough pieces to ever win the game
     */
    private boolean colourHasInsufficientMaterialToMate (boolean white){
        return CheckHelper.colourHasInsufficientMaterialToMate(this, white);
    }

    /**
     * @return true if in checkmate
     */
    public boolean inCheckmate(){
        if (!this.inCheck(isWhiteTurn())){
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    /**
     * @return true if in stalemate
     */
    public boolean inStalemate(){
        if (this.inCheck(isWhiteTurn())){
            return false;
        }
        return this.generateLegalMoves().length == 0;
    }

    /**
     * Expensive operation to determine pinned pieces to king
     * @param white the player
     * @return a list of squares that have pinned pieces to the king on them
     */
    private List<Square> pinnedPiecesToKing(boolean white){
        long myKing = white ? getWhiteKing() : getBlackKing();
        return pinnedPiecesToSquare(white, Square.getSquareOfBitboard(myKing));
    }

    /**
     * Expensive operation to determine pinned pieces to a particular square
     * @param white the player 
     * @param square the square you are interested in seeing the pins to
     * @return a list of squares that have pinned pieces on them
     */
    public List<Square> pinnedPiecesToSquare(boolean white, Square square){
        return Square.squaresFromBitboard(pinnedPiecesToSquareBitBoard(white, square));
    }

    /**
     * Expensive operation to determine pinned pieces to a particular square
     * @param white the player 
     * @param square the square you are interested in seeing the pins to
     * @return a list of squares that have pinned pieces on them
     */
    public long pinnedPiecesToSquareBitBoard(boolean white, Square square){

        long myPawns, myKnights, myBishops, myRooks, myQueen, myKing,
                enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueen, enemyKing,
                enemies, friends;
        if (isWhiteTurn()){
            myPawns = getWhitePawns();
            myKnights = getWhiteKnights();
            myBishops = getWhiteBishops();
            myRooks = getWhiteRooks();
            myQueen = getWhiteQueen();
            myKing = getWhiteKing();

            enemyPawns = getBlackPawns();
            enemyKnights = getBlackKnights();
            enemyBishops = getBlackBishops();
            enemyRooks = getBlackRooks();
            enemyQueen = getBlackQueen();
            enemyKing = getBlackKing();

            enemies = blackPieces();
            friends = whitePieces();
        } else {
            myPawns = getBlackPawns();
            myKnights = getBlackKnights();
            myBishops = getBlackBishops();
            myRooks = getBlackRooks();
            myQueen = getBlackQueen();
            myKing = getBlackKing();

            enemyPawns = getWhitePawns();
            enemyKnights = getWhiteKnights();
            enemyBishops = getWhiteBishops();
            enemyRooks = getWhiteRooks();
            enemyQueen = getWhiteQueen();
            enemyKing = getWhiteKing();

            enemies = whitePieces();
            friends = blackPieces();
        }

        return PinnedManager.whichPiecesArePinned(white ? getWhiteKing() : getBlackKing(),
                enemyBishops, enemyRooks, enemyQueen,
                friends, allPieces());
    }

    public boolean previousMoveWasPawnPushToSix(){
        if (!hasPreviousMove()){
            return false;
        }
        long peek = moveStackArrayPeek();
        return MoveParser.moveIsPawnPushSix(StackDataUtil.getMove(peek));
    }

    public boolean previousMoveWasPawnPushToSeven(){
        if (!hasPreviousMove()){
            return false;
        }
        long peek = moveStackArrayPeek();
        return MoveParser.moveIsPawnPushSeven(StackDataUtil.getMove(peek));
    }

    public boolean moveIsCaptureOfLastMovePiece(int move){
        if (!hasPreviousMove()){
            return false;
        }
        
        long peek = moveStackArrayPeek();
        if (StackDataUtil.getMove(peek) == 0){
            return false;
        }
        int previousMoveDestinationIndex = MoveParser.getDestinationIndex(StackDataUtil.getMove(peek));
        return (MoveParser.getDestinationIndex(move) == previousMoveDestinationIndex);
    }


    private void init(){
        this.details = new ChessboardDetails(true);
    }

    void makeZobrist(){
        this.zobristHash = ZobristHashUtil.boardToHash(this);
    }

    public long whitePieces(){
        this.pieces[WHITE][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[WHITE][ALL_COLOUR_PIECES] |= this.pieces[WHITE][i];
        }
        return this.pieces[WHITE][ALL_COLOUR_PIECES];
//        return getWhitePawns() | getWhiteKnights() | getWhiteBishops() | getWhiteRooks() | getWhiteQueen() | getWhiteKing();
    }

    public long getPieces(int turn){
        if (turn == WHITE) {
            return whitePieces();
        }
        else {
            return blackPieces();
        }
    }
    
    public long blackPieces(){
        this.pieces[BLACK][ALL_COLOUR_PIECES] = 0;
        for (int i = PAWN; i <= KING; i++) {
            this.pieces[BLACK][ALL_COLOUR_PIECES] |= this.pieces[BLACK][i];
        }
        return this.pieces[BLACK][ALL_COLOUR_PIECES];
//        return getBlackPawns() | getBlackKnights() | getBlackBishops() | getBlackRooks() | getBlackQueen() | getBlackKing();
    }

    public long allPieces(){
        return whitePieces() | blackPieces();
    }

    public boolean isWhiteTurn() {
//        return this.turn == WHITE;
        return this.details.whiteTurn;
    }

    public void setTurn(int whiteTurn) {
        this.turn = whiteTurn;
    }
    public void setWhiteTurn(boolean whiteTurn) {
        setTurn(whiteTurn ? WHITE : BLACK);
        this.details.whiteTurn = whiteTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chessboard that = (Chessboard) o;
        return Objects.equals(details, that.details)
                && Objects.equals(zobristHash, that.zobristHash)
                && Arrays.equals(zobristHashStack, that.zobristHashStack)
                && Arrays.equals(pinnedPiecesArray, that.pinnedPiecesArray)
                && Arrays.equals(checkStack, that.checkStack)
                ;
    }

    @Override
    public String toString() {
        String turn = isWhiteTurn() ? "It is white's turn." : "It is black's turn.";
        return "\n" + Art.boardArt(this) + "\n" + turn
                +"\n" + this.zobristHash
                +"\n" + this.getZobristHashStack();
    }

    public boolean isWhiteCanCastleK() {
        Assert.assertEquals(
                (this.castlingRights & castlingRightsOn[WHITE][K]) == castlingRightsOn[WHITE][K], this.details.whiteCanCastleK);
//        return this.details.whiteCanCastleK;
        return (this.castlingRights & castlingRightsOn[WHITE][K]) == castlingRightsOn[WHITE][K];
    }

    public void setWhiteCanCastleK(boolean whiteCanCastleK) {
        this.castlingRights &= castlingRightsMask[WHITE][K];
        this.castlingRights |= whiteCanCastleK ? castlingRightsOn[WHITE][K] : 0;
        this.details.whiteCanCastleK = whiteCanCastleK;
    }

    public boolean isWhiteCanCastleQ() {
//        Assert.assertEquals(
//                (this.castlingRights & castlingRightsOn[WHITE][Q]) == castlingRightsOn[WHITE][Q], this.details.whiteCanCastleQ);
//        return details.whiteCanCastleQ;
        return (this.castlingRights & castlingRightsOn[WHITE][Q]) == castlingRightsOn[WHITE][Q];
    }

    public void setWhiteCanCastleQ(boolean whiteCanCastleQ) {
        this.castlingRights &= castlingRightsMask[WHITE][Q];
        this.castlingRights |= whiteCanCastleQ ? castlingRightsOn[WHITE][Q] : 0;
        this.details.whiteCanCastleQ = whiteCanCastleQ;
    }

    public boolean isBlackCanCastleK() {
        Assert.assertEquals(details.blackCanCastleK, 
                (this.castlingRights & castlingRightsOn[BLACK][K]) == castlingRightsOn[BLACK][K]);
//        return details.blackCanCastleK;
        return (this.castlingRights & castlingRightsOn[BLACK][K]) == castlingRightsOn[BLACK][K];
    }

    public void setBlackCanCastleK(boolean blackCanCastleK) {
        this.castlingRights &= castlingRightsMask[BLACK][K];
        this.castlingRights |= blackCanCastleK ? castlingRightsOn[BLACK][K] : 0;
        this.details.blackCanCastleK = blackCanCastleK;
    }

    public boolean isBlackCanCastleQ() {
        Assert.assertEquals(details.blackCanCastleQ, 
                (this.castlingRights & castlingRightsOn[BLACK][Q]) == castlingRightsOn[BLACK][Q]);
//        return details.blackCanCastleQ;
        return (this.castlingRights & castlingRightsOn[BLACK][Q]) == castlingRightsOn[BLACK][Q];
    }

    public void setBlackCanCastleQ(boolean blackCanCastleQ) {
        this.castlingRights &= castlingRightsMask[BLACK][Q];
        this.castlingRights |= blackCanCastleQ ? castlingRightsOn[BLACK][Q] : 0;
        this.details.blackCanCastleQ = blackCanCastleQ;
    }
    
    
    

    public long getWhitePawns() {
        return this.details.whitePawns;
    }

    public void setWhitePawns(long whitePawns) {
        this.details.whitePawns = whitePawns;
    }

    public long getWhiteKnights() {
        return this.details.whiteKnights;
    }

    public void setWhiteKnights(long whiteKnights) {
        this.details.whiteKnights = whiteKnights;
    }

    public long getWhiteBishops() {
        return this.details.whiteBishops;
    }

    public void setWhiteBishops(long whiteBishops) {
        this.details.whiteBishops = whiteBishops;
    }

    public long getWhiteRooks() {
//        Assert.assertEquals(this.details.whiteRooks, this.pieces[WHITE][ROOK]);
        return this.details.whiteRooks;
    }

    public void setWhiteRooks(long whiteRooks) {
        this.details.whiteRooks = whiteRooks;
    }

    public long getWhiteQueen() {
//        Assert.assertEquals(this.details.whiteQueen, this.pieces[WHITE][QUEEN]);
        return this.details.whiteQueen;
    }

    public void setWhiteQueen(long whiteQueen) {
        this.details.whiteQueen = whiteQueen;
    }

    public long getWhiteKing() {
//        Assert.assertEquals(this.details.whiteKing, this.pieces[WHITE][KING]);
        return this.details.whiteKing;
    }

    public void setWhiteKing(long whiteKing) {
        this.details.whiteKing = whiteKing;
    }

    public long getBlackPawns() {
//        Assert.assertEquals(this.details.blackPawns, this.pieces[BLACK][PAWN]);
        return this.details.blackPawns;
    }

    public void setBlackPawns(long blackPawns) {
        this.details.blackPawns = blackPawns;
    }

    public long getBlackKnights() {
//        Assert.assertEquals(this.details.blackKnights, this.pieces[BLACK][KNIGHT]);
        return this.details.blackKnights;
    }

    public void setBlackKnights(long blackKnights) {
        this.details.blackKnights = blackKnights;
    }

    public long getBlackBishops() {
//        Assert.assertEquals(this.details.blackBishops, this.pieces[BLACK][BISHOP]);
        return this.details.blackBishops;
    }

    public void setBlackBishops(long blackBishops) {
        this.details.blackBishops = blackBishops;
    }

    public long getBlackRooks() {
        return this.details.blackRooks;
    }

    public void setBlackRooks(long blackRooks) {
        this.details.blackRooks = blackRooks;
    }

    public long getBlackQueen() {
        return this.details.blackQueen;
    }

    public void setBlackQueen(long blackQueen) {
        this.details.blackQueen = blackQueen;
    }

    public long getBlackKing() {
        return this.details.blackKing;
    }

    public void setBlackKing(long blackKing) {
        this.details.blackKing = blackKing;
    }









    private void makeBoardBasedOnFENSpecific(String fen){
        parseFenStringSpecific(fen);

        this.setWhiteTurn(isItWhitesTurnSpecific(fen));

        boolean[] castlingRights = castlingRightsSpecific(fen);
        this.setWhiteCanCastleK(castlingRights[0]);
        this.setWhiteCanCastleQ(castlingRights[1]);
        this.setBlackCanCastleK(castlingRights[2]);
        this.setBlackCanCastleQ(castlingRights[3]);

        if (isEPFlagSetSpecific(fen)){
            epFlagSpecific(fen);
        }
    }


    private static boolean totalMovesSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }


    private static boolean fiftyMovesSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }

    private void epFlagSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        int epFlag;
        switch (epFlags) {
            case "a": {
                epFlag = 1;
                break;
            }
            case "b": {
                epFlag = 2;
                break;
            }
            case "c": {
                epFlag = 3;
                break;
            }
            case "d": {
                epFlag = 4;
                break;
            }
            case "e": {
                epFlag = 5;
                break;
            }
            case "f": {
                epFlag = 6;
                break;
            }
            case "g": {
                epFlag = 7;
                break;
            }
            case "h": {
                epFlag = 8;
                break;
            }
            default:
                return;
        }
        final long item = buildStackData(0, this, ENPASSANTVICTIM, epFlag);
        this.moveStackArrayPush(item);
    }

    private static boolean isEPFlagSetSpecific(String fen){
        String boardPattern = " (.) (\\w+|-) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);

        String epFlags = "";

        if (m.find()){
            epFlags = m.group(3);
        }
        if (epFlags.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return !epFlags.equals("-");
    }

    private static boolean[] castlingRightsSpecific(String fen){
        boolean[] castlingRights = {
                false, false, false, false,
        };
        String boardPattern = " (.) (\\w+|-)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String castleString = "";
        if (m.find()){
            castleString = m.group(2);
        }
        if (castleString.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        if (castleString.equals("-")){
            return castlingRights;
        }

        if (castleString.contains("K")){
            castlingRights[0] = true;
        }
        if (castleString.contains("Q")){
            castlingRights[1] = true;
        }
        if (castleString.contains("k")){
            castlingRights[2] = true;
        }
        if (castleString.contains("q")){
            castlingRights[3] = true;
        }

        return castlingRights;
    }

    private boolean isItWhitesTurnSpecific(String fen){
        String boardPattern = " (.)";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String player = "";
        if (m.find()){
            player = m.group(1);
        }
        if (player.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }
        return player.equals("w");
    }

    private void parseFenStringSpecific (String fen){
        String boardRepresentation = boardRepSpecific(fen);
        int length = boardRepresentation.length();
        int index = -1;
        int square = 63;
        while (true){
            index++;
            if (index >= length){
                break;
            }
            if (square < 0){
                break;
            }
            String entry = Character.toString(boardRepresentation.charAt(index));
            if (entry.equals("/")){
                continue;
            }
            try {
                int i = Integer.parseInt(entry);
                square -= (i);
                continue;
            }
            catch (NumberFormatException ignored){
            }
            long pieceFromFen = BitOperations.newPieceOnSquare(square);
            square--;
            int whichPiece = 0;
            switch (entry) {
                case "P":
                    this.setWhitePawns(this.getWhitePawns() | pieceFromFen);
                    whichPiece = WHITE_PAWN;
                    break;
                case "N":
                    this.setWhiteKnights(this.getWhiteKnights() | pieceFromFen);
                    whichPiece = WHITE_KNIGHT;
                    break;
                case "B":
                    this.setWhiteBishops(this.getWhiteBishops() | pieceFromFen);
                    whichPiece = WHITE_BISHOP;
                    break;
                case "R":
                    this.setWhiteRooks(this.getWhiteRooks() | pieceFromFen);
                    whichPiece = WHITE_ROOK;
                    break;
                case "Q":
                    this.setWhiteQueen(this.getWhiteQueen() | pieceFromFen);
                    whichPiece = WHITE_QUEEN;
                    break;
                case "K":
                    this.setWhiteKing(this.getWhiteKing() | pieceFromFen);
                    whichPiece = WHITE_KING;
                    break;

                case "p":
                    this.setBlackPawns(this.getBlackPawns() | pieceFromFen);
                    whichPiece = BLACK_PAWN;
                    break;
                case "n":
                    this.setBlackKnights(this.getBlackKnights() | pieceFromFen);
                    whichPiece = BLACK_KNIGHT;
                    break;
                case "b":
                    this.setBlackBishops(this.getBlackBishops() | pieceFromFen);
                    whichPiece = BLACK_BISHOP;
                    break;
                case "r":
                    this.setBlackRooks(this.getBlackRooks() | pieceFromFen);
                    whichPiece = BLACK_ROOK;
                    break;
                case "q":
                    this.setBlackQueen(this.getBlackQueen() | pieceFromFen);
                    whichPiece = BLACK_QUEEN;
                    break;
                case "k":
                    this.setBlackKing(this.getBlackKing() | pieceFromFen);
                    whichPiece = BLACK_KING;
                    break;
                default:
                    throw new RuntimeException("Could not parse fen string");
            }
            this.pieces[whichPiece / 7][whichPiece < 7 ? whichPiece : whichPiece - 6] |= pieceFromFen;
        }
    }

    private static String boardRepSpecific(String fen){
        String boardPattern = "^[\\w*/]*";
        Pattern r = Pattern.compile(boardPattern);
        Matcher m = r.matcher(fen);
        String boardRepresentation = "";
        if (m.find()){  
            boardRepresentation = m.group();
        }
        if (boardRepresentation.length() == 0){
            throw new RuntimeException("Could not Parse board rep of fen string");
        }

        return boardRepresentation;
    }


    public ChessboardDetails getDetails() {
        return details;
    }

    public void setDetails(ChessboardDetails details) {
        this.details = details;
    }

    public long getBoardHash() {
        return zobristHash;
    }

    public long getZobrist() {
        return zobristHash;
    }

    public void setBoardHash(long zobristHash) {
        this.zobristHash = zobristHash;
    }


    void masterStackPush() {
        this.checkStackArrayPush();
        this.pinStackArrayPush();
        this.zobristStackArrayPush(this.getBoardHash());
    }
    
    void masterStackPop(){
        this.checkStackArrayPop();
        this.pinStackArrayPop();
        this.zobristStackArrayPop();
        this.moveStackArrayPop();
    }
    
    private int legalMoveStackIndex = 0;
    void legalMoveStackPush(){
        this.legalMoveStack[legalMoveStackIndex] = this.moves;
        legalMoveStackIndex++;
    }
    
    void legalMoveStackPop(){
        if (legalMoveStackIndex < 1) {
            throw new RuntimeException();
        }
        legalMoveStackIndex--;
        this.moves = this.legalMoveStack[legalMoveStackIndex];
    }
    
    void zobristStackArrayPush(long l){
        zobristHashStack[zobristIndex] = l;

        zobristIndex++;
    }

    void zobristStackArrayPop(){
        if (zobristIndex < 1){
            throw new RuntimeException("popping an empty zobrist array");
        }
        
        zobristIndex--;
        zobristHash = zobristHashStack[zobristIndex];
        zobristHashStack[zobristIndex] = 0;
        
    }

    long zobristStackArrayPeek(){
        if (zobristIndex < 1){
            throw new RuntimeException("peeking at empty zobrist array");
        }
        return zobristHashStack[zobristIndex-1];
    }

    private int checkIndex = 0;
    void checkStackArrayPush(){
        checkStack[checkIndex] = this.inCheckRecorder;
        this.inCheckRecorder = false;
        checkIndex++;
    }

    private void checkStackArrayPop(){
        if (checkIndex < 1){
            throw new RuntimeException("popping an empty array");
        }       
        checkIndex--; 
        inCheckRecorder = checkStack[checkIndex];
        checkStack[checkIndex] = false;
    }
    

    private int pinIndex = 0;
    void pinStackArrayPush(){
        pinnedPiecesArray[pinIndex] = this.pinnedPieces;
        this.pinnedPieces = 0;
        pinIndex++;
    }

    private void pinStackArrayPop(){
        if (pinIndex < 1){
            throw new RuntimeException("popping an empty array");
        } 
        pinIndex--;  
        pinnedPieces = pinnedPiecesArray[pinIndex];
        pinnedPiecesArray[pinIndex] = 0;
    }
    
    
    
    void moveStackArrayPush(long l){
        pastMoveStackArray[index] = l;

        index++;
    }

    private void moveStackArrayPop(){
        if (index < 1){
            throw new RuntimeException("popping an empty array");
        }
        pastMoveStackArray[index] = 0;
        index--;
        moveStackData = pastMoveStackArray[index];
    }

    long moveStackArrayPeek(){
        if (index < 1){
            throw new RuntimeException("peeking at empty array");
        }
        return pastMoveStackArray[index-1];
    }
    
    boolean hasPreviousMove(){
        return index > 0 && pastMoveStackArray[index - 1] != 0;
    }

    public long[] getZobristHashStack() {
        return zobristHashStack;
    }
}
