package com.github.louism33.chesscore;

public class BoardConstants {

    public static final int NO_PIECE = 0;

    public static final int ALL_COLOUR_PIECES = 0;
    
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    
    public static final int BOTH = 2;

    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    public static final int WHITE_PAWN = 1;
    public static final int WHITE_KNIGHT = 2;
    public static final int WHITE_BISHOP = 3;
    public static final int WHITE_ROOK = 4;
    public static final int WHITE_QUEEN = 5;
    public static final int WHITE_KING = 6;

    public static final int BLACK_PAWN = 7;
    public static final int BLACK_KNIGHT = 8;
    public static final int BLACK_BISHOP = 9;
    public static final int BLACK_ROOK = 10;
    public static final int BLACK_QUEEN = 11;
    public static final int BLACK_KING = 12;
    
    public static final int
            H1 = 0, G1 = 1, F1 = 2, E1 = 3, D1 = 4, C1 = 5, B1 = 6, A1 = 7,
            H2 = 8, G2 = 9, F2 = 10, E2 = 11, D2 = 12, C2 = 13, B2 = 14, A2 = 15,
            H3 = 16, G3 = 17, F3 = 18, E3 = 19, D3 = 20, C3 = 21, B3 = 22, A3 = 23,
            H4 = 24, G4 = 25, F4 = 26, E4 = 27, D4 = 28, C4 = 29, B4 = 30, A4 = 31,
            H5 = 32, G5 = 33, F5 = 34, E5 = 35, D5 = 36, C5 = 37, B5 = 38, A5 = 39,
            H6 = 40, G6 = 41, F6 = 42, E6 = 43, D6 = 44, C6 = 45, B6 = 46, A6 = 47,
            H7 = 48, G7 = 49, F7 = 50, E7 = 51, D7 = 52, C7 = 53, B7 = 54, A7 = 55,
            H8 = 56, G8 = 57, F8 = 58, E8 = 59, D8 = 60, C8 = 61, B8 = 62, A8 = 63;

    public static long[][] inBetweenSquares = new long[64][64];

    public static final long UNIVERSE = 0xffffffffffffffffL;

    public static final long INITIAL_WHITE_PIECES = 0x000000000000FFFFL;

    public static final long INITIAL_WHITE_PAWNS = 0x000000000000FF00L;
    public static final long INITIAL_WHITE_KNIGHTS = 0x0000000000000042L;
    public static final long INITIAL_WHITE_BISHOPS = 0x0000000000000024L;
    public static final long INITIAL_WHITE_ROOKS = 0x0000000000000081L;
    public static final long INITIAL_WHITE_QUEEN = 0x0000000000000010L;
    public static final long INITIAL_WHITE_KING = 0x0000000000000008L;

    public static final long INITIAL_BLACK_PIECES = 0xFFFF000000000000L;
    
    public static final long INITIAL_BLACK_PAWNS = 0x00FF000000000000L;
    public static final long INITIAL_BLACK_KNIGHTS = 0x4200000000000000L;
    public static final long INITIAL_BLACK_BISHOPS = 0x2400000000000000L;
    public static final long INITIAL_BLACK_ROOKS = 0x8100000000000000L;
    public static final long INITIAL_BLACK_QUEEN = 0x1000000000000000L;
    public static final long INITIAL_BLACK_KING = 0x0800000000000000L;
    public static final long[][] INITIAL_PIECES = new long[2][7];

    static {
        INITIAL_PIECES[WHITE][PAWN] = INITIAL_WHITE_PAWNS;
        INITIAL_PIECES[WHITE][KNIGHT] = INITIAL_WHITE_KNIGHTS;
        INITIAL_PIECES[WHITE][BISHOP] = INITIAL_WHITE_BISHOPS;
        INITIAL_PIECES[WHITE][ROOK] = INITIAL_WHITE_ROOKS;
        INITIAL_PIECES[WHITE][QUEEN] = INITIAL_WHITE_QUEEN;
        INITIAL_PIECES[WHITE][KING] = INITIAL_WHITE_KING;

        INITIAL_PIECES[WHITE][ALL_COLOUR_PIECES] = INITIAL_WHITE_PIECES;

        INITIAL_PIECES[BLACK][PAWN] = INITIAL_BLACK_PAWNS;
        INITIAL_PIECES[BLACK][KNIGHT] = INITIAL_BLACK_KNIGHTS;
        INITIAL_PIECES[BLACK][BISHOP] = INITIAL_BLACK_BISHOPS;
        INITIAL_PIECES[BLACK][ROOK] = INITIAL_BLACK_ROOKS;
        INITIAL_PIECES[BLACK][QUEEN] = INITIAL_BLACK_QUEEN;
        INITIAL_PIECES[BLACK][KING] = INITIAL_BLACK_KING;

        INITIAL_PIECES[BLACK][ALL_COLOUR_PIECES] = INITIAL_BLACK_PIECES;
    }
    
    public static final long WHITE_COLOURED_SQUARES = 0x5555555555555555L;
    public static final long BLACK_COLOURED_SQUARES = 0xAAAAAAAAAAAAAAAAL;

    static long CASTLE_WHITE_KING_SQUARES = 0x0000000000000006L;
    static long CASTLE_WHITE_QUEEN_SQUARES = 0x0000000000000070L;
    static long CASTLE_BLACK_KING_SQUARES = 0x0600000000000000L;
    static long CASTLE_BLACK_QUEEN_SQUARES = 0x7000000000000000L;

    public static final long RANK_ONE = 0x00000000000000FFL;
    public static final long RANK_TWO = 0x000000000000FF00L;
    public static final long RANK_THREE = 0x0000000000FF0000L;
    public static final long RANK_FOUR = 0x00000000FF000000L;
    public static final long RANK_FIVE = 0x000000FF00000000L;
    public static final long RANK_SIX = 0x0000FF0000000000L;
    public static final long RANK_SEVEN = 0x00FF000000000000L;
    public static final long RANK_EIGHT = 0xFF00000000000000L;

    public static final long FILE_H = 0x0101010101010101L;
    public static final long FILE_G = 0x0202020202020202L;
    public static final long FILE_F = 0x0404040404040404L;
    public static final long FILE_E = 0x0808080808080808L;
    public static final long FILE_D = 0x1010101010101010L;
    public static final long FILE_C = 0x2020202020202020L;
    public static final long FILE_B = 0x4040404040404040L;
    public static final long FILE_A = 0x8080808080808080L;

    public static final long[] FILES = new long[]{
            0x0101010101010101L,
            0x0202020202020202L,
            0x0404040404040404L,
            0x0808080808080808L,
            0x1010101010101010L,
            0x2020202020202020L,
            0x4040404040404040L,
            0x8080808080808080L,
    };

    public static final long[] PROMOTING_RANKS = new long[2];

    static {
        PROMOTING_RANKS[WHITE] = RANK_SEVEN;
        PROMOTING_RANKS[BLACK] = RANK_TWO;
    }
    
    public static final long[] ROWS = new long[]{
            0x00000000000000FFL,
            0x000000000000FF00L,
            0x0000000000FF0000L,
            0x00000000FF000000L,
            0x000000FF00000000L,
            0x0000FF0000000000L,
            0x00FF000000000000L,
            0xFF00000000000000L,
    };

    public static final long NORTH_WEST = FILE_A | RANK_EIGHT;
    public static final long NORTH_EAST = FILE_H | RANK_EIGHT;
    public static final long SOUTH_WEST = FILE_A | RANK_ONE;
    public static final long SOUTH_EAST = FILE_H | RANK_ONE;

    public static final long NORTH_WEST_CORNER = FILE_A & RANK_EIGHT;
    public static final long NORTH_EAST_CORNER = FILE_H & RANK_EIGHT;
    public static final long SOUTH_WEST_CORNER = FILE_A & RANK_ONE;
    public static final long SOUTH_EAST_CORNER = FILE_H & RANK_ONE;

    public static final long centreFourSquares = (RANK_FOUR | RANK_FIVE) & (FILE_D | FILE_E);
    public static final long centreNineSquares = (RANK_THREE | RANK_FOUR | RANK_FIVE | RANK_SIX) &
            (FILE_C |FILE_D | FILE_E | FILE_F);

    public static final long noMansLand = (RANK_THREE | RANK_FOUR | RANK_FIVE | RANK_SIX);
    public static final long eastWestHighway = (RANK_FOUR | RANK_FIVE);
    public static final long northSouthHighway = (FILE_D | FILE_E);

    public static final long DIAGONAL_NW_SE = 0x8040201008040201L;
    public static final long DIAGONAL_SW_NE = 0x0102040810204080L;

    public static final long boardWithoutEdges = UNIVERSE ^ (NORTH_WEST | NORTH_EAST | SOUTH_WEST | SOUTH_EAST);

    public static final long whiteCastleKingEmpties = 0x0000000000000006L;
    public static final long whiteCastleQueenEmpties = 0x0000000000000070L;

    public static final long blackCastleKingEmpties = 0x0600000000000000L;
    public static final long blackCastleQueenEmpties = 0x7000000000000000L;

    public static final long whiteCastleQueenUnthreateneds = 0x0000000000000030L;
    public static final long blackCastleQueenUnthreateneds = 0x3000000000000000L;
    
    public static final long[] castleQueenNoThreat = new long[2];
    public static final long[][] castleEmpties = new long[2][2];
    public static final int[][] castlingRightsOn = new int[2][2];
    public static final int[][] castlingRightsMask = new int[2][2];
    
    public static final int K = 1, Q = 0;
    static{
        castleQueenNoThreat[WHITE] = whiteCastleQueenUnthreateneds;
        castleQueenNoThreat[BLACK] = blackCastleQueenUnthreateneds;

        castleEmpties[WHITE][Q] = whiteCastleQueenEmpties;
        castleEmpties[WHITE][K] = whiteCastleKingEmpties;
        
        castleEmpties[BLACK][Q] = blackCastleQueenEmpties;
        castleEmpties[BLACK][K] = blackCastleKingEmpties;

        castlingRightsOn[WHITE][Q] = 0b0001;
        castlingRightsOn[WHITE][K] = 0b0010;
                
        castlingRightsOn[BLACK][Q] = 0b0100;
        castlingRightsOn[BLACK][K] = 0b1000;

        castlingRightsMask[WHITE][Q] = 0b1110;
        castlingRightsMask[WHITE][K] = 0b1101;
        
        castlingRightsMask[BLACK][Q] = 0b1011;
        castlingRightsMask[BLACK][K] = 0b0111;
    }

    public final static long[] immediateAdjacentSquares = {
            0x0000000000000302L, 0x0000000000000705L, 0x0000000000000e0aL, 0x0000000000001c14L, 0x0000000000003828L, 0x0000000000007050L, 0x000000000000e0a0L, 0x000000000000c040L,
            0x0000000000030203L, 0x0000000000070507L, 0x00000000000e0a0eL, 0x00000000001c141cL, 0x0000000000382838L, 0x0000000000705070L, 0x0000000000e0a0e0L, 0x0000000000c040c0L,
            0x0000000003020300L, 0x0000000007050700L, 0x000000000e0a0e00L, 0x000000001c141c00L, 0x0000000038283800L, 0x0000000070507000L, 0x00000000e0a0e000L, 0x00000000c040c000L,
            0x0000000302030000L, 0x0000000705070000L, 0x0000000e0a0e0000L, 0x0000001c141c0000L, 0x0000003828380000L, 0x0000007050700000L, 0x000000e0a0e00000L, 0x000000c040c00000L,
            0x0000030203000000L, 0x0000070507000000L, 0x00000e0a0e000000L, 0x00001c141c000000L, 0x0000382838000000L, 0x0000705070000000L, 0x0000e0a0e0000000L, 0x0000c040c0000000L,
            0x0003020300000000L, 0x0007050700000000L, 0x000e0a0e00000000L, 0x001c141c00000000L, 0x0038283800000000L, 0x0070507000000000L, 0x00e0a0e000000000L, 0x00c040c000000000L,
            0x0302030000000000L, 0x0705070000000000L, 0x0e0a0e0000000000L, 0x1c141c0000000000L, 0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L, 0xc040c00000000000L,
            0x0203000000000000L, 0x0507000000000000L, 0x0a0e000000000000L, 0x141c000000000000L, 0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L
    };

    public final static long[] bigAdjacentSquares = {
            460550L, 986893L, 2039579L, 4079158L, 8158316L, 16316632L, 15790256L, 14737504L,
            117900807L, 252644623L, 522132255L, 1044264510L, 2088529020L, 4177058040L, 4042305776L, 3772801248L,
            30182606599L, 64677023503L, 133665857311L, 267331714622L, 534663429244L, 1069326858488L, 1034830278896L, 965837119712L,
            7726747289344L, 16557318016768L, 34218459471616L, 68436918943232L, 136873837886464L, 273747675772928L, 264916551397376L, 247254302646272L,
            1978047306072064L, 4238673412292608L, 8759925624733696L, 17519851249467392L, 35039702498934784L, 70079404997869568L, 67818637157728256L, 63297101477445632L,
            506380110354448384L, 1085100393546907648L, 2242540959931826176L, 4485081919863652352L, 8970163839727304704L, -506416394254942208L, -1085172961331118080L, -2242686095483469824L,
            506099734771924992L, 1084539642365083648L, 2241419457551400960L, 4482838915102801920L, 8965677830205603840L, -515388413298343936L, -1103116995122954240L, -2278574158772174848L,
            434323585645936640L, 940987339818139648L, 1954314848162545664L, 3908629696325091328L, 7817259392650182656L, -2812225288409186304L, -5696789645833011200L, 6980825713028890624L,
    };

    public final static long[] blackPawnKillZone = {
            197376L, 460544L, 921088L, 1842176L, 3684352L, 7368704L, 14737408L, 12632064L,
            50528256L, 117899264L, 235798528L, 471597056L, 943194112L, 1886388224L, 3772776448L, 3233808384L,
            12935233536L, 30182211584L, 60364423168L, 120728846336L, 241457692672L, 482915385344L, 965830770688L, 827854946304L,
            3311419785216L, 7726646165504L, 15453292331008L, 30906584662016L, 61813169324032L, 123626338648064L, 247252677296128L, 211930866253824L,
            847723465015296L, 1978021418369024L, 3956042836738048L, 7912085673476096L, 15824171346952192L, 31648342693904384L, 63296685387808768L, 54254301760978944L,
            217017207043915776L, 506373483102470144L, 1012746966204940288L, 2025493932409880576L, 4050987864819761152L, 8101975729639522304L, -2242792614430507008L, -4557642822898941952L,
            216172782113783808L, 504403158265495552L, 1008806316530991104L, 2017612633061982208L, 4035225266123964416L, 8070450532247928832L, -2305843009213693952L, -4611686018427387904L,
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
    };

    public final static long[] whitePawnKillZone = {
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
            3L, 7L, 14L, 28L, 56L, 112L, 224L, 192L,
            771L, 1799L, 3598L, 7196L, 14392L, 28784L, 57568L, 49344L,
            197376L, 460544L, 921088L, 1842176L, 3684352L, 7368704L, 14737408L, 12632064L,
            50528256L, 117899264L, 235798528L, 471597056L, 943194112L, 1886388224L, 3772776448L, 3233808384L,
            12935233536L, 30182211584L, 60364423168L, 120728846336L, 241457692672L, 482915385344L, 965830770688L, 827854946304L,
            3311419785216L, 7726646165504L, 15453292331008L, 30906584662016L, 61813169324032L, 123626338648064L, 247252677296128L, 211930866253824L,
            847723465015296L, 1978021418369024L, 3956042836738048L, 7912085673476096L, 15824171346952192L, 31648342693904384L, 63296685387808768L, 54254301760978944L,
    };

    public final static long[] fileForwardBlack = {
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
            1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L,
            257L, 514L, 1028L, 2056L, 4112L, 8224L, 16448L, 32896L,
            65793L, 131586L, 263172L, 526344L, 1052688L, 2105376L, 4210752L, 8421504L,
            16843009L, 33686018L, 67372036L, 134744072L, 269488144L, 538976288L, 1077952576L, 2155905152L,
            4311810305L, 8623620610L, 17247241220L, 34494482440L, 68988964880L, 137977929760L, 275955859520L, 551911719040L,
            1103823438081L, 2207646876162L, 4415293752324L, 8830587504648L, 17661175009296L, 35322350018592L, 70644700037184L, 141289400074368L,
            282578800148737L, 565157600297474L, 1130315200594948L, 2260630401189896L, 4521260802379792L, 9042521604759584L, 18085043209519168L, 36170086419038336L,
    };

    public final static long[] fileForwardWhite = {
            72340172838076672L, 144680345676153344L, 289360691352306688L, 578721382704613376L, 1157442765409226752L, 2314885530818453504L, 4629771061636907008L, -9187201950435737600L,
            72340172838076416L, 144680345676152832L, 289360691352305664L, 578721382704611328L, 1157442765409222656L, 2314885530818445312L, 4629771061636890624L, -9187201950435770368L,
            72340172838010880L, 144680345676021760L, 289360691352043520L, 578721382704087040L, 1157442765408174080L, 2314885530816348160L, 4629771061632696320L, -9187201950444158976L,
            72340172821233664L, 144680345642467328L, 289360691284934656L, 578721382569869312L, 1157442765139738624L, 2314885530279477248L, 4629771060558954496L, -9187201952591642624L,
            72340168526266368L, 144680337052532736L, 289360674105065472L, 578721348210130944L, 1157442696420261888L, 2314885392840523776L, 4629770785681047552L, -9187202502347456512L,
            72339069014638592L, 144678138029277184L, 289356276058554368L, 578712552117108736L, 1157425104234217472L, 2314850208468434944L, 4629700416936869888L, -9187343239835811840L,
            72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, -9223372036854775808L,
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L,
    };

    public final static long[] antiDiagonal = {
            1L, 258L, 66052L, 16909320L, 4328785936L, 1108169199648L, 283691315109952L, 72624976668147840L, 145249953336295424L, 290499906672525312L, 580999813328273408L, 1161999622361579520L, 2323998145211531264L, 4647714815446351872L, -9223372036854775808L,
    };

    public final static long[] diagonal = {
            128L, 32832L, 8405024L, 2151686160L, 550831656968L, 141012904183812L, 36099303471055874L, -9205322385119247871L, 4620710844295151872L, 2310355422147575808L, 1155177711073755136L, 577588855528488960L, 288794425616760832L, 144396663052566528L, 72057594037927936L,
    };

    public static final long[] PAWN_PUSH_MASK_BLACK = {
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 1L,
            2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L,
            512L, 1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L,
            131072L, 262144L, 524288L, 1048576L, 2097152L, 4194304L, 8388608L, 16777216L,
            33554432L, 67108864L, 134217728L, 268435456L, 536870912L, 1073741824L, 2147483648L, 4294967296L,
            8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1103806595072L,
            2207613190144L, 4415226380288L, 8830452760576L, 17660905521152L, 35321811042304L, 70643622084608L, 141287244169216L, 0L,
            0L, 0L, 0L, 0L, 0L, 0L, 0L
    };
    
    public static final long[] PAWN_PUSH_MASK_WHITE = {
            0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 16842752L,
            33685504L, 67371008L, 134742016L, 269484032L, 538968064L, 1077936128L, 2155872256L, 16777216L,
            33554432L, 67108864L, 134217728L, 268435456L, 536870912L, 1073741824L, 2147483648L, 4294967296L,
            8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L,
            2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L,
            562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L,
            144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, -9223372036854775808L, 0L,
            0L, 0L, 0L, 0L, 0L, 0L, 0L,
    };
    
    public static final long[] PAWN_CAPTURE_TABLE_WHITE = {
            0x200L, 0x500L, 0xa00L, 0x1400L, 0x2800L, 0x5000L, 0xa000L, 0x4000L,
            0x20000L, 0x50000L, 0xa0000L, 0x140000L, 0x280000L, 0x500000L, 0xa00000L, 0x400000L,
            0x2000000L, 0x5000000L, 0xa000000L, 0x14000000L, 0x28000000L, 0x50000000L, 0xa0000000L, 0x40000000L,
            0x200000000L, 0x500000000L, 0xa00000000L, 0x1400000000L, 0x2800000000L, 0x5000000000L, 0xa000000000L, 0x4000000000L,
            0x20000000000L, 0x50000000000L, 0xa0000000000L, 0x140000000000L, 0x280000000000L, 0x500000000000L, 0xa00000000000L, 0x400000000000L,
            0x2000000000000L, 0x5000000000000L, 0xa000000000000L, 0x14000000000000L, 0x28000000000000L, 0x50000000000000L, 0xa0000000000000L, 0x40000000000000L,
            0x200000000000000L, 0x500000000000000L, 0xa00000000000000L, 0x1400000000000000L, 0x2800000000000000L, 0x5000000000000000L, 0xa000000000000000L, 0x4000000000000000L,
            0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L,
    };

    public static final long[] PAWN_CAPTURE_TABLE_BLACK = {
            0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L,
            0x2L, 0x5L, 0xaL, 0x14L, 0x28L, 0x50L, 0xa0L, 0x40L,
            0x200L, 0x500L, 0xa00L, 0x1400L, 0x2800L, 0x5000L, 0xa000L, 0x4000L,
            0x20000L, 0x50000L, 0xa0000L, 0x140000L, 0x280000L, 0x500000L, 0xa00000L, 0x400000L,
            0x2000000L, 0x5000000L, 0xa000000L, 0x14000000L, 0x28000000L, 0x50000000L, 0xa0000000L, 0x40000000L,
            0x200000000L, 0x500000000L, 0xa00000000L, 0x1400000000L, 0x2800000000L, 0x5000000000L, 0xa000000000L, 0x4000000000L,
            0x20000000000L, 0x50000000000L, 0xa0000000000L, 0x140000000000L, 0x280000000000L, 0x500000000000L, 0xa00000000000L, 0x400000000000L,
            0x2000000000000L, 0x5000000000000L, 0xa000000000000L, 0x14000000000000L, 0x28000000000000L, 0x50000000000000L, 0xa0000000000000L, 0x40000000000000L,
    };


    public static long[][] rookDatabase = new long[64][];
    public static long[][] bishopDatabase = new long[64][];

    public static final int[] rookShiftAmounts = new int[]{
            12, 11, 11, 11, 11, 11, 11, 12,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            12, 11, 11, 11, 11, 11, 11, 12,
    };

    public static final int[] bishopShiftAmounts = new int[]{
            6, 5, 5, 5, 5, 5, 5, 6,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 5, 5, 5, 5, 5, 5, 6,
    };

    static final long[] rookBlankBoardAttackMasks = new long[]{
            282578800148862L, 565157600297596L, 1130315200595066L, 2260630401190006L, 4521260802379886L, 9042521604759646L, 18085043209519166L, 36170086419038334L, 282578800180736L, 565157600328704L, 1130315200625152L, 2260630401218048L, 4521260802403840L, 9042521604775424L, 18085043209518592L, 36170086419037696L, 282578808340736L, 565157608292864L, 1130315208328192L, 2260630408398848L, 4521260808540160L, 9042521608822784L, 18085043209388032L, 36170086418907136L, 282580897300736L, 565159647117824L, 1130317180306432L, 2260632246683648L, 4521262379438080L, 9042522644946944L, 18085043175964672L, 36170086385483776L, 283115671060736L, 565681586307584L, 1130822006735872L, 2261102847592448L, 4521664529305600L, 9042787892731904L, 18085034619584512L, 36170077829103616L, 420017753620736L, 699298018886144L, 1260057572672512L, 2381576680245248L, 4624614895390720L, 9110691325681664L, 18082844186263552L, 36167887395782656L, 35466950888980736L, 34905104758997504L, 34344362452452352L, 33222877839362048L, 30979908613181440L, 26493970160820224L, 17522093256097792L, 35607136465616896L, 9079539427579068672L, 8935706818303361536L, 8792156787827803136L, 8505056726876686336L, 7930856604974452736L, 6782456361169985536L, 4485655873561051136L, 9115426935197958144L,
    };

    static final long[] bishopBlankBoardAttackMasks = new long[]{
            18049651735527936L, 70506452091904L, 275415828992L, 1075975168L, 38021120L, 8657588224L, 2216338399232L, 567382630219776L, 9024825867763712L, 18049651735527424L, 70506452221952L, 275449643008L, 9733406720L, 2216342585344L, 567382630203392L, 1134765260406784L, 4512412933816832L, 9024825867633664L, 18049651768822272L, 70515108615168L, 2491752130560L, 567383701868544L, 1134765256220672L, 2269530512441344L, 2256206450263040L, 4512412900526080L, 9024834391117824L, 18051867805491712L, 637888545440768L, 1135039602493440L, 2269529440784384L, 4539058881568768L, 1128098963916800L, 2256197927833600L, 4514594912477184L, 9592139778506752L, 19184279556981248L, 2339762086609920L, 4538784537380864L, 9077569074761728L, 562958610993152L, 1125917221986304L, 2814792987328512L, 5629586008178688L, 11259172008099840L, 22518341868716544L, 9007336962655232L, 18014673925310464L, 2216338399232L, 4432676798464L, 11064376819712L, 22137335185408L, 44272556441600L, 87995357200384L, 35253226045952L, 70506452091904L, 567382630219776L, 1134765260406784L, 2832480465846272L, 5667157807464448L, 11333774449049600L, 22526811443298304L, 9024825867763712L, 18049651735527936L
    };


    static final long[] rookMagicNumbers = new long[]{
            36028940904563728L, 18014535970456640L, 4647732409781977096L, 432350237422537216L, 5044033783291195400L, 72096077146227208L, 36040891806261760L, 72067628323080448L,
            1407516659417152L, 774193629106176L, 1243134303397740676L, 3518471603224708L, 703790562944000L, 6665890681930647556L, 361695349369487872L, 562958597955716L,
            36031271058546688L, -9200853763833724607L, 422763831529472L, 2393087863195648L, 18155685887939584L, -8862942779253193728L, 90076390677950488L, 32934771373957380L,
            669059268493312L, 74678830832295936L, 41730872912936976L, 20002317680510336L, 2315034928570172032L, 6665890681930647556L, 361695349369487872L, 4406636478661L,
            9148211763610672L, 20407210697826320L, 18032269885202432L, 72484273277372416L, 703790562944000L, 18156244107793408L, 5066553909316616L, 1182199301338562729L,
            36064256310935568L, 162217548052447236L, -9146810688436396032L, 2269396304855072L, 1126449796907076L, 6918092049602445336L, 198164993693450256L, 18449807299444737L,
            594758275059171584L, 594642551477109888L, -9205285048531254784L, 288239172513268608L, -4314444042790764416L, 95138598110626304L, 577023720107868672L, 2414070176413738112L,
            144326375921156131L, 590112461579943954L, 297255206259793921L, 1171499128487153670L, 36310323576700945L, 648799832189960193L, 4900074733263392900L, 621497066950034434L,
    };

    static final long[] bishopMagicNumbers = new long[]{
            18023473842520080L,20275029052760096L,2343003281309597712L,5137472375750790L,-7988820521250707200L,-6340496245680762624L,2522253440731054208L,28429419358454808L,
            4521200508731920L,4758140985635012640L,52780857360416L,8815437152260L,3468127976386723904L,282626565603329L,4416367494216L,3417840619225600L,
            -9204223422844763070L,2308099619525007504L,20406987485360384L,218424584108524544L,2306968917997782049L,-8646770542499422207L,-8930074834085345280L,2523563994988943392L,
            2256275304878080L,1442842930179736609L,-8052365489512316416L,-8052365489512316416L,-9222101001404620800L,-9221118033720736744L,1130882137727232L,-9223296718961638362L,
            572020924747908L,-9223072930883174080L,37967520334048L,581034824293286400L,4913504159812158080L,4517901868537984L,4517901868537984L,36535124271105032L,
            -9077000580991024064L,1189093341754819080L,1161171386241042L,1729593646619305984L,2378956169317188097L,10151809146552576L,1233988617198669888L,-8032728479613058556L,
            2522253440731054208L,4611730557374578824L,594493035392544768L,234820508490139648L,10977663145542144L,459376027409850496L,2359890637194985472L,20275029052760096L,
            28429419358454808L,3417840619225600L,4611721409604096000L,4611976293897077282L,3534219749380719108L,1125969163305216L,4521200508731920L,18023473842520080L
    };

    static final long[] KNIGHT_MOVE_TABLE = {
            0x20400L, 0x50800L, 0xa1100L, 0x142200L, 0x284400L, 0x508800L, 0xa01000L, 0x402000L,
            0x2040004L, 0x5080008L, 0xa110011L, 0x14220022L, 0x28440044L, 0x50880088L, 0xa0100010L, 0x40200020L,
            0x204000402L, 0x508000805L, 0xa1100110aL, 0x1422002214L, 0x2844004428L, 0x5088008850L, 0xa0100010a0L, 0x4020002040L,
            0x20400040200L, 0x50800080500L, 0xa1100110a00L, 0x142200221400L, 0x284400442800L, 0x508800885000L, 0xa0100010a000L, 0x402000204000L,
            0x2040004020000L, 0x5080008050000L, 0xa1100110a0000L, 0x14220022140000L, 0x28440044280000L, 0x50880088500000L, 0xa0100010a00000L, 0x40200020400000L,
            0x204000402000000L, 0x508000805000000L, 0xa1100110a000000L, 0x1422002214000000L, 0x2844004428000000L, 0x5088008850000000L, 0xa0100010a0000000L, 0x4020002040000000L,
            0x400040200000000L, 0x800080500000000L, 0x1100110a00000000L, 0x2200221400000000L, 0x4400442800000000L, 0x8800885000000000L, 0x100010a000000000L, 0x2000204000000000L,
            0x4020000000000L, 0x8050000000000L, 0x110a0000000000L, 0x22140000000000L, 0x44280000000000L, 0x88500000000000L, 0x10a00000000000L, 0x20400000000000L,
    };

    static final long[] KING_MOVE_TABLE = {
            0x302L, 0x705L, 0xe0aL, 0x1c14L, 0x3828L, 0x7050L, 0xe0a0L, 0xc040L,
            0x30203L, 0x70507L, 0xe0a0eL, 0x1c141cL, 0x382838L, 0x705070L, 0xe0a0e0L, 0xc040c0L,
            0x3020300L, 0x7050700L, 0xe0a0e00L, 0x1c141c00L, 0x38283800L, 0x70507000L, 0xe0a0e000L, 0xc040c000L,
            0x302030000L, 0x705070000L, 0xe0a0e0000L, 0x1c141c0000L, 0x3828380000L, 0x7050700000L, 0xe0a0e00000L, 0xc040c00000L,
            0x30203000000L, 0x70507000000L, 0xe0a0e000000L, 0x1c141c000000L, 0x382838000000L, 0x705070000000L, 0xe0a0e0000000L, 0xc040c0000000L,
            0x3020300000000L, 0x7050700000000L, 0xe0a0e00000000L, 0x1c141c00000000L, 0x38283800000000L, 0x70507000000000L, 0xe0a0e000000000L, 0xc040c000000000L,
            0x302030000000000L, 0x705070000000000L, 0xe0a0e0000000000L, 0x1c141c0000000000L, 0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L, 0xc040c00000000000L,
            0x203000000000000L, 0x507000000000000L, 0xa0e000000000000L, 0x141c000000000000L, 0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L,
    };
}
