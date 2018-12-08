package com.github.louism33.chesscore;

class ConstantsMove {

    /*
    00000001
    11111111
    00000000
    00000000
     */
    final static int
            WHITE_PAWN_MASK = 0x00000001,
            WHITE_KNIGHT_MASK = 0x00000002,
            WHITE_BISHOP_MASK = 0x00000003,
            WHITE_ROOK_MASK = 0x00000004,
            WHITE_QUEEN_MASK = 0x00000005,
            WHITE_KING_MASK = 0x00000006,

    BLACK_PAWN_MASK = 0x00000007,
            BLACK_KNIGHT_MASK = 0x00000008,
            BLACK_BISHOP_MASK = 0x00000009,
            BLACK_ROOK_MASK = 0x0000000a,
            BLACK_QUEEN_MASK = 0x0000000b,
            BLACK_KING_MASK = 0x0000000c,

    NO_PIECE = 0,

    WHITE_PAWN = 1,
            WHITE_KNIGHT = 2,
            WHITE_BISHOP = 3,
            WHITE_ROOK = 4,
            WHITE_QUEEN = 5,
            WHITE_KING = 6,

    BLACK_PAWN = 7,
            BLACK_KNIGHT = 8,
            BLACK_BISHOP = 9,
            BLACK_ROOK = 10,
            BLACK_QUEEN = 11,
            BLACK_KING = 12,

    

    SOURCE_OFFSET = 6,
            SOURCE_MASK = 0x00000fc0,
            DESTINATION_MASK = 0x0000003f,

    ENPASSANT_MASK = 0x00002000,
            PROMOTION_MASK = 0x00003000,

    SPECIAL_MOVE_MASK = 0x00003000,
            CASTLING_MASK = 0x00001000,


    KNIGHT_PROMOTION_MASK = 0x00000000,
            BISHOP_PROMOTION_MASK = 0x00004000,
            ROOK_PROMOTION_MASK = 0x00008000,
            QUEEN_PROMOTION_MASK = 0x0000c000,

    WHICH_PROMOTION = 0x0000c000,

    SOURCE_PIECE_MASK = 0x000f0000,
            SOURCE_PIECE_OFFSET = 16,

    VICTIM_PIECE_MASK = 0x00f00000,
            VICTIM_PIECE_OFFSET = 20,


    CAPTURE_MOVE_MASK = 0x01000000
            ;
}
