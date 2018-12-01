package com.github.louism33.chesscore;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;

import java.util.List;

public class UCIBoardParser {
    
    public static GenericMove convertMyMoveToGenericMove(int move){
        GenericMove genericMove = null;
        try {
            genericMove = new GenericMove(MoveParser.toString(move));
        } catch (IllegalNotationException e) {
            System.out.println("Problem with: " +move);
            e.printStackTrace();
        }
        return genericMove;
    }

    public static Chessboard convertGenericBoardToChessboard(GenericBoard genericBoard, List<GenericMove> moves){
        if (genericBoard == null || moves == null){
            return null;
        }
        Chessboard board = xoldFenParser.makeBoardBasedOnFEN(genericBoard.toString());
        for (GenericMove genericMove : moves){
            board.makeMoveAndFlipTurn(moveFromGenericMove(board, genericMove));
        }
        return board;
    }

    public static Chessboard convertGenericBoardToChessboardDelta(Chessboard board, List<GenericMove> moves){

        for (int i = moves.size() - 2; i < moves.size(); i++) {
            GenericMove genericMove = moves.get(i);
            board.makeMoveAndFlipTurn(moveFromGenericMove(board, genericMove));
        }
        return board;
    }
    
    //avoid move gen if possible
    private static int moveFromGenericMove(Chessboard board, GenericMove genericMove){
        String s = genericMove.toString();
        int[] moves = board.generateLegalMoves();
        int move = 0;

        for (int i = 0; i < moves.length; i++) {
            int myMove = moves[i];
            if (s.equalsIgnoreCase(MoveParser.toString(myMove))) {
                move = myMove;
                break;
            }
        }
        if (move == 0){
            throw new RuntimeException("Could not parse move: "+ s);
        }
        return move;
    }
}




















