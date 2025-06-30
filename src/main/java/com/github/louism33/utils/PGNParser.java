package com.github.louism33.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

public final class PGNParser {

    private PGNParser() {
        // This is a utility class and should not be instantiated.
    }

    public static class TexelObject {
        public List<String> allMoves;
        public float winner;
        public int numberOfBookMoves = 0;

        public TexelObject(final List<String> allMoves, final int numberOfBookMoves, final float winner) {
            this.allMoves = allMoves;
            this.numberOfBookMoves = numberOfBookMoves;
            this.winner = winner;
        }
    }

    static Matcher whiteMates = Pattern.compile("White mates").matcher("");
    static Matcher blackMates = Pattern.compile("Black mates").matcher("");
    static Matcher draw = Pattern.compile("1/2-1/2").matcher("");


    public static TexelObject parsePGNForTexel(String pgn){
        // count book moves
        int cnt = pgn.split("\\{book\\}").length - 1;
        float winner = 0;
        whiteMates.reset(pgn);
        blackMates.reset(pgn);
        draw.reset(pgn);
        final String mateRemover = "\\{[\\+\\-]M.*";
        if (whiteMates.find()) {
            pgn = pgn.replaceAll(mateRemover, "");
            winner = 1;
        } else if (blackMates.find()) {
            pgn = pgn.replaceAll(mateRemover, "");
            winner = 0;
        } else {
            Assert.assertTrue(draw.find());
            winner = 0.5f;
            pgn = pgn.replaceAll("\\{[^\\{]*Draw.*", "");
        }

        pgn = pgn.replaceAll(" ?\\d+\\. ", " ");

        pgn = pgn.replaceAll("\\{[\\+\\-M\\d\\/\\w\\.]*\\s?[\\w\\.]*} ?", "");

        pgn = pgn.replaceAll("#.*", "");

        pgn = pgn.replace('\n', ' ');
        pgn = pgn.replaceAll("  ", " ");
        pgn = pgn.replaceAll("  ", " ");

        final String[] s = pgn.split(" ");

        final List<String> ss = new ArrayList<>();
        // only grab moves that did not lead finding a mate
        for (int i = 0; i < s.length - 1; i++) {
            if (s[i] == null || s[i].equals("") || s[i].equals(" ")) {
                continue;
            }
            ss.add(s[i]);
        }

        return new TexelObject(ss, cnt, winner);
    }

    /**
     * Parses a PGN string and returns a list of plies.
     * In chess, a ply refers to a half-move, meaning a move made by one side only.
     *
     * @param pgn The PGN string to parse.
     * @return A list of moves extracted from the PGN string.
     */
    public static List<String> parsePGNSimple(final String pgn) {
        return Arrays.stream(getPlies(pgn).split(" "))
                .filter(StringUtils::isNotBlank)
                .toList();
    }

    /** Clean up the PGN string and return the plies.
     *
	 * @param pgn0 The PGN string to parse.
	 * @return A cleaned-up string of moves extracted from the PGN string.
	 */
	private static String getPlies(final String pgn0) {
		String pgn = pgn0.replaceAll(" ?\\d+\\. ", " ");

//        pgn = pgn.replaceAll("\\{[\\w\\.]*} ?", "");

        pgn = pgn.replaceAll("\\{[\\+\\-M\\d\\/\\w\\.]*\\s?[\\w\\.]*} ?", "");

        pgn = pgn.replaceAll("#.*", "");

        pgn = pgn.replaceAll("\n", " ");
        pgn = pgn.replaceAll("  ", " ");
        pgn = pgn.replaceAll("  ", " ");

        return pgn;
	}

}