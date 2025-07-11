package com.github.louism33.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class TexelPosLoader {
    private TexelPosLoader() {
        // This is a utility class and should not be instantiated.
    }

    private static final Pattern RESULT = Pattern.compile(" c9 ");

    public static class TexelPos {
        public String fen;
        public float score;

        public TexelPos(final String fen, final float score) {
            this.fen = fen;
            this.score = score;
        }

        @Override
        public String toString() {
            return "TextPos{" +
                    "fen='" + fen + '\'' +
                    ", score=" + score +
                    '}';
        }
    }

    public static List<TexelPos> readFile(final String url) throws IOException {
        final List<TexelPos> texelPosList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            String line;

            while ((line = br.readLine()) != null) {
                final String[] split = RESULT.split(line);
                texelPosList.add(new TexelPos(split[0], getScore(split[1])));
            }
        }
        
        return texelPosList;
    }

    static float getScore(final String c9) {
        if (c9.equalsIgnoreCase("\"0-1\";")) {
            return 0;
        } else if (c9.equalsIgnoreCase("\"1-0\";")) {
            return 1;
        } 
        return 0.5f;
    }

}
