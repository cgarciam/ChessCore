package com.github.louism33.chesscore;

//import java.util.HashMap;
//import java.util.Map;

import lombok.Data;

/**
 * Square class represents a square on a chess-board.
 * It contains the file and rank of the square.
 */
@Data
public class Square {
    private final int file;
    private final int rank;
    /*
    private static final Map<Integer, Square> SQUARES;

    // Any square has a given position on the board:
    static {
        SQUARES = new HashMap<>();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                SQUARES.put(file + rank * 8, new Square(file, rank));
            }
        }
    }

    public static void main(final String... args) {
        // Example usage
        final Square square = new Square(3, 4);
        System.out.println("File: " + square.getFile() + ", Rank: " + square.getRank());
        System.out.println("Square: " + square);
        System.out.println("Square from index 35: " + SQUARES.get(35));
        // Get index from square:
        final int index = square.getFile() + square.getRank() * 8;
        System.out.println("Index: " + index);
        // Get index from square using static method:
//        final Square squareFromIndex = SQUARES.get(index);
        final Integer indexFromSquare = getIndex(square);
        System.out.println("Square from index: " + indexFromSquare);
    }

    public static Integer getIndex(final Square square) {
        Integer index = null;
        for (Map.Entry<Integer, Square> entry : SQUARES.entrySet()) {
            final Integer key = entry.getKey();
            final Square val = entry.getValue();
            if (square.equals(val)) {
                System.out.println("Found square: Key: " + key + ", Value: " + val);
                index = key;
                break;
            }
        }
        return index;
    }
*/
}