package com.github.louism33.utils;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.louism33.utils.ExtendedPositionDescriptionParser.parseEDPPosition;

@RunWith(Parameterized.class)
public class PGNParserTest {

    @Parameterized.Parameters(name = "{index} Test: {1}")
    public static Collection<Object[]> data() {
        final List<Object[]> answers = new ArrayList<>();

        for (int i = 0; i < SPLIT_UP_POSITIONS.length; i++) {

            final String splitUpWAC = SPLIT_UP_POSITIONS[i];
            final Object[] objectAndName = new Object[2];
            final ExtendedPositionDescriptionParser.EPDObject EPDObject = parseEDPPosition(splitUpWAC);
            objectAndName[0] = EPDObject;
            objectAndName[1] = EPDObject.getId();
            answers.add(objectAndName);
        }
        return answers;
    }

    private static ExtendedPositionDescriptionParser.EPDObject EPDObject;

    public PGNParserTest(final Object edp, final Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @org.junit.Test
    @SuppressWarnings("unused")
    public void test() {
        try {
            final int[] winningMoves = EPDObject.getBestMovesFromComments();
            final int[] losingMoves = EPDObject.getAvoidMoves();
        } catch (final Exception | Error e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private static final String POSITIONS = "" +
            "1k2r3/1p1bP3/2p2p1Q/Ppb5/4Rp1P/2q2N1P/5PB1/6K1 b - - bm Kc7; id \"STS(v11.0) King Activity.001\"; c0 \"Kc7=10, Kc8=4, Qa1+=4\";\n" +
            "1k2r3/p7/Pp1pP1p1/4p2p/2r4P/5P1B/4RB1K/8 w - - bm Kg3; id \"STS(v11.0) King Activity.002\"; c0 \"Kg3=10, Kg2=5, Re1=5, Re3=3\";\n" +
            "1k5r/6p1/p2b4/7p/2r1p2P/R1B1P3/6P1/2R3K1 b - - bm Ka7; id \"STS(v11.0) King Activity.003\"; c0 \"Ka7=10, Bxa3=3, Kb7=4, Rc6=2\";\n" +
            "1n5k/3r2p1/2p1qp1p/3p1N1P/1P1P1rP1/p1R5/P7/1KRQ4 w - - bm Ka1; id \"STS(v11.0) King Activity.004\"; c0 \"Ka1=10, Re3=2, Rg3=3\";\n" +
            "1r2r3/1p1b3k/2p2n2/p1Pp4/P2N1PpP/1R2p3/1P2P1BP/3R2K1 b - - bm Kg6; id \"STS(v11.0) King Activity.005\"; c0 \"Kg6=10, Kh6=8, Ne4=8, Nh5=8\";\n" +
            "1r2rqk1/8/bn1p3p/B1p2p2/p1PnpP2/P3R2Q/1P3NPP/2R2BK1 b - - bm Kh7; id \"STS(v11.0) King Activity.006\"; c0 \"Kh7=10, Re6=3\";\n" +
            "1R3bk1/7p/6p1/8/1pN3P1/8/r4P1P/6K1 b - - bm Kf7; id \"STS(v11.0) King Activity.007\"; c0 \"Kf7=10, Kg7=1\";\n" +
            "1r3k2/2N2pp1/1pR2n1p/4p3/8/1P1K1P2/P5PP/8 w - - bm Kc4; id \"STS(v11.0) King Activity.008\"; c0 \"Kc4=10, a4=6, Ke3=2, Nb5=2\";\n" +
            "1r3k2/3n1p2/6pN/2p1PP2/p2q4/Pr1p2P1/1P1R1R1P/3Q2K1 b - - bm Ke7; id \"STS(v11.0) King Activity.009\"; c0 \"Ke7=10, Ke8=2, Kg7=2\";\n" +
            "2b4r/1p1k4/1pnbppp1/r2p3p/2pP1P1P/2P2NPB/PPN2P2/R3R1K1 b - - bm Ke7; id \"STS(v11.0) King Activity.010\"; c0 \"Ke7=10, Nd8=3, Re8=2\";\n" +
            "2k2r1r/1b4p1/1nq1p3/1p5p/5P2/1Pp1B1KP/2B1QRP1/R7 w - - bm Kh2; id \"STS(v11.0) King Activity.011\"; c0 \"Kh2=10, Bd3=7\";\n" +
            "2k3r1/1b2bp2/2p2n2/pp2p1Bp/2p1P2P/P2n1B2/1P1RN1P1/5K1R b - - bm Kc7; id \"STS(v11.0) King Activity.012\"; c0 \"Kc7=10, a4=3, Ba6=1, c5=2, Kb8=3\";\n" +
            "2kr1b1r/1bq2pp1/p3pn2/7p/1ppPN2P/4PQ2/PPBB1PP1/R1R3K1 b - - bm Kb8; id \"STS(v11.0) King Activity.013\"; c0 \"Kb8=10, a5=2, Bd5=2, e5=2, Rh6=3\";\n" +
            "2kr3r/p3p3/1pn2pp1/1R5p/4P2P/2P1BPP1/P3K3/7R b - - bm Kb7; id \"STS(v11.0) King Activity.014\"; c0 \"Kb7=10, Rd6=7, Rd7=7\";\n" +
            "2q2r2/3bbpkp/r2p4/p1pPp1P1/PpP1P2P/1P3QK1/4RN2/2B3R1 w - - bm Kh2; id \"STS(v11.0) King Activity.015\"; c0 \"Kh2=10, Ng4=6, Qh5=5\";\n" +
            "2r1b3/p3kpp1/7p/3P4/7P/2p1KPP1/P7/1BR5 w - - bm Kd4; id \"STS(v11.0) King Activity.016\"; c0 \"Kd4=10, Bc2=7, Bf5=7\";\n" +
            "2r1k2r/1q1bbp2/p4p2/1p2pP1p/8/1N1B2Q1/PPP3PP/1K1RR3 b k - bm Kf8; id \"STS(v11.0) King Activity.017\"; c0 \"Kf8=10, Kd8=6, Rf8=6\";\n" +
            "2r1r3/1k1b1p2/1p4p1/p3n3/2PRP2p/PP1NK2P/2R3B1/8 b - - bm Kc7; id \"STS(v11.0) King Activity.018\"; c0 \"Kc7=10, Bc6=9, Kc6=9, Rc7=9\";\n" +
            "2r2bk1/1Q3ppp/p6r/P2BP2q/5Pb1/1P1RR1P1/3B4/6K1 w - - bm Kf1; id \"STS(v11.0) King Activity.019\"; c0 \"Kf1=10, Bc3=8, Bg2=8, Bxf7+=8\";\n" +
            "2r2k2/4pp2/pp6/2pPn3/4PN1p/1P5P/P4P2/2R2K2 w - - bm Ke2; id \"STS(v11.0) King Activity.020\"; c0 \"Ke2=10, Ng2=3, Rc3=3\";\n" +
            "2r2k2/p4pp1/b3n2p/8/1pp1P2P/4NPP1/PPB3K1/3R4 w - - bm Kf2; id \"STS(v11.0) King Activity.021\"; c0 \"Kf2=10, Nd5=2, Rd2=1\";\n" +
            "2r3k1/1q6/4p3/p2p2bp/P2P1n2/2P1NP2/1PB4B/1R2R1K1 b - - bm Kf7; id \"STS(v11.0) King Activity.022\"; c0 \"Kf7=10, Kf8=3, Rf8=6\";\n" +
            "2r3k1/2r1b3/p3p2p/3n2p1/2N1KP2/P2N2PP/2R5/2R5 b - - bm Kg7; id \"STS(v11.0) King Activity.023\"; c0 \"Kg7=10, Kh7=1\";\n" +
            "2rb2k1/1p1q3p/1P2b1p1/1N3p2/3B4/4PP1P/1Q4P1/R5K1 w - - bm Kh2; id \"STS(v11.0) King Activity.024\"; c0 \"Kh2=10, Kh1=4, Ra7=3\";\n" +
            "2rk4/1Rp5/1bBp1r2/4p2p/8/6P1/P1P2P1P/5RK1 w - - bm Kg2; id \"STS(v11.0) King Activity.025\"; c0 \"Kg2=10, c4=4, h4=6\";\n" +
            "3b4/3k1p2/4p1p1/p1rpP1Pp/R4P2/1P3K2/P2B1P2/8 b - - bm Kc6; id \"STS(v11.0) King Activity.026\"; c0 \"Kc6=10, Bb6=4, Kc8=5\";\n" +
            "3b4/5p2/2k1p1p1/p1rpP1Pp/R4P2/1P6/P2BKP2/8 b - - bm Kb5; id \"STS(v11.0) King Activity.027\"; c0 \"Kb5=10, Bb6=6, Kb7=6\";\n" +
            "3k1b1r/pp3p1p/5P2/3Bn3/8/2N5/PP5P/2K4R b - - bm Kc7; id \"STS(v11.0) King Activity.028\"; c0 \"Kc7=10, b6=4, Kc8=4\";\n" +
            "3q1rk1/5p2/1pQ1p1p1/p2p4/P2P2P1/1P2P3/6RP/6K1 b - - bm Kg7; id \"STS(v11.0) King Activity.029\"; c0 \"Kg7=10, Kh7=3, Qe7=4, Qf6=4, Qh4=4\";\n" +
            "3q4/p2r1k2/1p1b2nP/1Pp5/2P1Q3/1P2B3/3R1K2/8 w - - bm Kg1; id \"STS(v11.0) King Activity.030\"; c0 \"Kg1=10, Kf1=8\";\n" +
            "3r1k2/4qp1p/6p1/p3p1P1/P2nQ2P/1B1R4/5PK1/8 b - - bm Kg7; id \"STS(v11.0) King Activity.031\"; c0 \"Kg7=10, Rc8=4, Rd7=4, Re8=4\";\n" +
            "3r1k2/p4pp1/2n1p2p/1NPr4/P7/6P1/5P1P/2R1RK2 b - - bm Ke7; id \"STS(v11.0) King Activity.032\"; c0 \"Ke7=10, a6=3, g5=4, g6=4\";\n" +
            "3r1r2/p3bpp1/1p2p1k1/4P2p/2P2P2/5K1P/PP3B2/3R1R2 b - - bm Kf5; id \"STS(v11.0) King Activity.033\"; c0 \"Kf5=10, f5=5, f6=1, Rfe8=5\";\n" +
            "3r1r2/p3bppk/1p2p2p/4P3/2P2P2/8/PP3BKP/3R1R2 b - - bm Kg6; id \"STS(v11.0) King Activity.034\"; c0 \"Kg6=10\";\n" +
            "3r2k1/1p4p1/p2P3p/1pPN4/1K4b1/8/2R4P/8 w - - bm Ka5; id \"STS(v11.0) King Activity.035\"; c0 \"Ka5=10, Rd2=5, Rf2=6, Rg2=5\";\n" +
            "3r2k1/1rq2p2/2bp2pP/p3p2n/4P3/1BN2P2/1PP5/R2RQK2 b - - bm Kh7; id \"STS(v11.0) King Activity.036\"; c0 \"Kh7=10, Nf4=5, Ra7=2\";\n" +
            "3r2k1/2p2ppp/8/P1b1N3/8/1Bn1nP2/5B1P/4K2R b - - bm Kf8; id \"STS(v11.0) King Activity.037\"; c0 \"Kf8=10, g6=3, Ncd5=1\";\n" +
            "3r2k1/4pp1p/2q3p1/8/1P6/r1P2P2/P3Q1PP/RKR5 w - - bm Kb2; id \"STS(v11.0) King Activity.038\"; c0 \"Kb2=10, h3=6, Qe1=6, Qe4=5\";\n" +
            "3r3r/ppk2pb1/4bn1p/3p2p1/3N4/2NR1BP1/PPP1P2P/5RK1 b - - bm Kb8; id \"STS(v11.0) King Activity.039\"; c0 \"Kb8=10, h5=2, Rhe8=1\";\n" +
            "3r4/6p1/5kBp/7P/6P1/4BP2/1p2K3/8 b - - bm Ke5; id \"STS(v11.0) King Activity.040\"; c0 \"Ke5=10, Ke6=6, Rd5=7, Rd7=7\";\n" +
            "3R4/6pk/p4p1p/1r1p3P/2pP4/2P1PN2/4bPP1/6K1 w - - bm Kh2; id \"STS(v11.0) King Activity.041\"; c0 \"Kh2=10, Kh1=2\";\n" +
            "3r4/p3k3/1p2bpp1/2p5/2P2q2/2Q2N2/PP4PP/4R2K w - - bm Kg1; id \"STS(v11.0) King Activity.042\"; c0 \"Kg1=10, b3=9, h3=9, Qc2=9\";\n" +
            "3rbk2/4pp1p/6p1/3PP3/p1PnN1P1/Nn5P/5PBK/1R6 w - - bm Kg3; id \"STS(v11.0) King Activity.043\"; c0 \"Kg3=10, Nc3=4, Re1=4\";\n" +
            "3rbk2/5p1p/1p2p3/pP1nr1p1/P2RN1P1/3BP3/3K3P/3R4 b - - bm Ke7; id \"STS(v11.0) King Activity.044\"; c0 \"Ke7=10, Bd7=4, h6=6, Rd7=8\";\n" +
            "3rr1k1/q4pp1/p1ppbR1p/2P4R/3PP3/2N1Q2P/6P1/6K1 b - - bm Kf8; id \"STS(v11.0) King Activity.045\"; c0 \"Kf8=10, dxc5=3\";\n" +
            "3rr3/p1k4p/1p3p1b/2p2p1n/5P2/2P1BR2/PP1N3P/1K2R3 b - - bm Kc6; id \"STS(v11.0) King Activity.046\"; c0 \"Kc6=10, Kb7=5, Kc8=5\";\n" +
            "4k3/1p4pp/p1b2p2/P4P2/1PNp2qN/2nP2P1/3Q1K1P/8 b - - bm Kd8; id \"STS(v11.0) King Activity.047\"; c0 \"Kd8=10, Kf8=2, Qd1=3\";\n" +
            "4k3/3p3p/p2Pn1p1/Pr2Pp2/5P1P/1NR3P1/5K2/8 w - - bm Ke3; id \"STS(v11.0) King Activity.048\"; c0 \"Ke3=10, h5=7, Ke1=5, Rc8+=4, Rd3=3\";\n" +
            "4r1k1/1p1q2pp/p4pn1/3p4/3N4/1PPK2RP/P2Q1PP1/8 w - - bm Kc2; id \"STS(v11.0) King Activity.049\"; c0 \"Kc2=10, Qc1=5, Qd1=5\";\n" +
            "4r1k1/1Q3ppp/2PB4/pb6/8/P1K5/6P1/1r6 w - - bm Kc2; id \"STS(v11.0) King Activity.050\"; c0 \"Kc2=10, c7=7, Kd4=1\";\n" +
            "4r1k1/2q1p3/2p2pp1/1p1b4/p7/P1Q3R1/1P3PP1/4R1K1 b - - bm Kg7; id \"STS(v11.0) King Activity.051\"; c0 \"Kg7=10, Bf7=8, g5=8, Kf7=8\";\n" +
            "4r1k1/3b1q2/2pn1p1b/1p3P2/p2PN1PP/P4Q2/1P4R1/4R1K1 b - - bm Kf8; id \"STS(v11.0) King Activity.052\"; c0 \"Kf8=10\";\n" +
            "4r1k1/5r2/p5p1/6p1/1PbB4/2P2B2/5PP1/R5K1 w - - bm Kh2; id \"STS(v11.0) King Activity.053\"; c0 \"Kh2=10, Be3=8, Ra5=6, Rd1=4\";\n" +
            "4r1k1/p4p1p/1bpp2p1/3p4/8/1P1Q2P1/P1P1rPKP/5R2 w - - bm Kf3; id \"STS(v11.0) King Activity.054\"; c0 \"Kf3=10, Kg1=2, Qc3=2\";\n" +
            "4r2r/1p6/2p2n2/p1Pp3k/P2NbPp1/4R3/1P2P2P/2R2BK1 b - - bm Kg6; id \"STS(v11.0) King Activity.055\"; c0 \"Kg6=10, Kh4=4, Ref8=3, Rh7=3\";\n" +
            "4r3/2R1pk2/3p2pp/2bP1p2/8/1R4P1/5PK1/8 b - - bm Kf6; id \"STS(v11.0) King Activity.056\"; c0 \"Kf6=10, Bd4=8, g5=9, h5=8, Ra8=8\";\n" +
            "4r3/5k2/2PB4/p4p2/8/1P1R2KP/2P5/5r2 w - - bm Kg2; id \"STS(v11.0) King Activity.057\"; c0 \"Kg2=10, Rd2=1\";\n" +
            "4r3/5k2/3p1npp/1p1P1p2/p4P2/P3N2P/1P2KP2/2R5 w - - bm Kf3; id \"STS(v11.0) King Activity.058\"; c0 \"Kf3=10, h4=8, Kf1=8, Rc7+=8\";\n" +
            "4r3/5k2/p2B2r1/2P2p2/1P5p/3R3P/1P3KP1/8 w - - bm Kf3; id \"STS(v11.0) King Activity.059\"; c0 \"Kf3=10\";\n" +
            "4R3/5pkp/b3p1p1/2Q5/1P6/P2qP3/K7/8 w - - bm Kb2; id \"STS(v11.0) King Activity.060\"; c0 \"Kb2=10, Qc1=4\";\n" +
            "4rbk1/p2R1p2/2p2p1p/5p2/8/PR2p3/2P3PP/5K2 w - - bm Ke2; id \"STS(v11.0) King Activity.061\"; c0 \"Ke2=10, g3=3, Rc3=3\";\n" +
            "4rnk1/1p2rp1p/2p3p1/3p1B1P/N4R2/pP2P1R1/P5P1/6K1 w - - bm Kf2; id \"STS(v11.0) King Activity.062\"; c0 \"Kf2=10, e4=5, hxg6=7, Rff3=6\";\n" +
            "4rrk1/7p/pn1p1npb/N2P4/1P6/5BP1/PB6/3R1RK1 w - - bm Kg2; id \"STS(v11.0) King Activity.063\"; c0 \"Kg2=10, Bxf6=2\";\n" +
            "5bk1/1r3p2/6p1/2pRP3/8/6P1/3N1PK1/8 b - - bm Kg7; id \"STS(v11.0) King Activity.064\"; c0 \"Kg7=10\";\n" +
            "5k2/p3np2/2b1p2p/2P2p1P/8/2N1PP2/2K1B1P1/8 b - - bm Ke8; id \"STS(v11.0) King Activity.065\"; c0 \"Ke8=10, Bd7=7, Nd5=8, Ng8=6\";\n" +
            "5n2/1p2r1kp/1Qp1r1p1/p3Pp2/P6P/4R1P1/1P6/5K2 w - - bm Kg2; id \"STS(v11.0) King Activity.066\"; c0 \"Kg2=10, b3=6, Kg1=7, Re1=9, Re2=8\";\n" +
            "5r2/1N6/2p2Pk1/p1P1P1p1/3b3p/KP1N2n1/P6R/8 b - - bm Kf5; id \"STS(v11.0) King Activity.067\"; c0 \"Kf5=10, Ne4=1, Nf1=1\";\n" +
            "5rk1/1p5p/3p2p1/qP1N1n2/2P2P2/8/P4Q1P/4R1K1 w - - bm Kg2; id \"STS(v11.0) King Activity.068\"; c0 \"Kg2=10, Kf1=3, Kh1=5, Rc1=2, Re4=5\";\n" +
            "5rrk/6qp/2R2b2/1P1pp2Q/5p2/7R/P2B1P2/5K2 w - - bm Ke2; id \"STS(v11.0) King Activity.069\"; c0 \"Ke2=10, Be1=9, Rc1=9, Rh1=9\";\n" +
            "6k1/5b1p/6p1/r1P5/3pNK2/5P1P/p5P1/R7 b - - bm Kf8; id \"STS(v11.0) King Activity.070\"; c0 \"Kf8=10, h6=6, Kg7=4, Rb5=6\";\n" +
            "6k1/8/p2Br1r1/2P2p1p/1P6/7P/1P1R2PK/8 w - - bm Kg1; id \"STS(v11.0) King Activity.071\"; c0 \"Kg1=10, Rc2=4, Rf2=2\";\n" +
            "6r1/1b1k4/2p2p2/ppB1p3/2p1P2P/P2n4/1P1R2P1/5K1R b - - bm Ke6; id \"STS(v11.0) King Activity.072\"; c0 \"Ke6=10, Kc7=4, Ke8=2\";\n" +
            "6r1/pb3pk1/1p2p3/1Bp1P3/2Pb4/P7/4RPPR/6K1 b - - bm Kf8; id \"STS(v11.0) King Activity.073\"; c0 \"Kf8=10, a6=8, Ba8=8, Rb8=7\";\n" +
            "6rk/q2p3p/4pp2/1P1n3P/2R5/p4NP1/P4P2/1Q4K1 w - - bm Kg2; id \"STS(v11.0) King Activity.074\"; c0 \"Kg2=10, Nd4=4, Rd4=4\";\n" +
            "7k/1b1n1pp1/p2Ppq1p/1p5Q/1P1N3P/P3P3/B5P1/6K1 b - - bm Kg8; id \"STS(v11.0) King Activity.075\"; c0 \"Kg8=10, Kh7=6, Nf8=1\";\n" +
            "7k/1q5p/5prP/1p1pr3/1PbR1QPK/2N3P1/2P5/3R4 w - - bm Kh3; id \"STS(v11.0) King Activity.076\"; c0 \"Kh3=10, R1d2=8, Ra1=9, Rb1=8\";\n" +
            "7k/3N1p2/p7/3p1rp1/8/1P2P2p/P4n2/2R2NK1 w - - bm Kh2; id \"STS(v11.0) King Activity.077\"; c0 \"Kh2=10, Ng3=6, Rc2=5, Rc8+=6\";\n" +
            "7k/3r1p2/p1r1p2p/1q1n3P/1PNRQP2/P7/K7/2R5 w - - bm Kb2; id \"STS(v11.0) King Activity.078\"; c0 \"Kb2=10\";\n" +
            "7r/5p2/3k4/1p1p4/rPnP3P/4P3/4RPN1/1R5K w - - bm Kh2; id \"STS(v11.0) King Activity.079\"; c0 \"Kh2=10, Kg1=7, Nf4=5, Rb3=4\";\n" +
            "7r/r1p2k1p/1p5B/1bp5/4pPP1/1P2Nn1P/PR3R2/7K b - - bm Kg6; id \"STS(v11.0) King Activity.080\"; c0 \"Kg6=10, Ne1=2, Ra3=2, Rd8=1, Rha8=2\";\n" +
            "8/1k1r3p/pp3p1R/2pn4/4r3/N1P5/PP5P/2KR4 b - - bm Kc7; id \"STS(v11.0) King Activity.081\"; c0 \"Kc7=10, b5=4, Ree7=4\";\n" +
            "8/1p2bk2/r7/p2P4/2P5/4B1R1/PP4Pn/1K6 w - - bm Kc2; id \"STS(v11.0) King Activity.082\"; c0 \"Kc2=10, Bd2=2, Bd4=7, Bg1=8\";\n" +
            "8/2kr3p/5p2/pppn4/4r3/1PP4R/P1N4P/2KR4 b - - bm Kc6; id \"STS(v11.0) King Activity.083\"; c0 \"Kc6=10, c4=4, Re2=5\";\n" +
            "8/3k3p/2p2p2/2B5/8/1r1b1P1P/3R1KP1/8 b - - bm Ke6; id \"STS(v11.0) King Activity.084\"; c0 \"Ke6=10, Kc7=7, Ke8=7\";\n" +
            "8/4q1k1/5pp1/pp2b3/2p1P3/P1P1Q1Pp/7P/3R2K1 w - - bm Kf1; id \"STS(v11.0) King Activity.085\"; c0 \"Kf1=10, Qd2=3, Rd5=4\";\n" +
            "8/4r1n1/1k5p/1p1prp1P/1PpN2p1/p1K1P1R1/P1B3PR/8 w - - bm Kd2; id \"STS(v11.0) King Activity.086\"; c0 \"Kd2=10, Bb1=9, Rh1=9, Rh4=9\";\n" +
            "8/4r2p/2k2p2/1p1nr3/1Pp5/2P2R2/2NR3P/1K6 b - - bm Kb6; id \"STS(v11.0) King Activity.087\"; c0 \"Kb6=10, f5=6, Rg7=5, Rh5=6\";\n" +
            "8/p1r1rk1p/2R1pb2/P4p2/1N4p1/1Pp1P1P1/5P1P/2R3K1 w - - bm Kf1; id \"STS(v11.0) King Activity.088\"; c0 \"Kf1=10, a6=7, Kg2=7, Rd6=7, Rxc7=6\";\n" +
            "8/p3p1k1/1p1p1pp1/4n3/3R3P/1Pr5/P5P1/3R2K1 b - - bm Kh6; id \"STS(v11.0) King Activity.089\"; c0 \"Kh6=10, a5=8, f5=8, Rc2=6\";\n" +
            "8/pQ3qk1/P2pp2p/2p2n2/2P2PB1/3P3P/8/7K b - - bm Kf6; id \"STS(v11.0) King Activity.090\"; c0 \"Kf6=10, h5=7, Kf8=7, Ng3+=6\";\n" +
            "8/pr3pbk/6p1/7p/R7/2p1P1P1/2R2P1P/6K1 w - - bm Kf1; id \"STS(v11.0) King Activity.091\"; c0 \"Kf1=10, Kg2=4, Ra5=4\";\n" +
            "8/R2n4/pr1k2p1/4pp1p/5P2/1N2P1PP/Pn2B1K1/8 w - - bm Kf1; id \"STS(v11.0) King Activity.092\"; c0 \"Kf1=10, Bxa6=3, Kf2=4, Kh2=3\";\n" +
            "b2q2r1/5p2/2prpkp1/1pN5/p2P1PP1/P3PQ1p/1PB4P/2R3K1 b - - bm Kg7; id \"STS(v11.0) King Activity.093\"; c0 \"Kg7=10, Ke7=4, Qa5=6, Re8=5, Rh8=4\";\n" +
            "br3rk1/2q1bpp1/pnnpp2p/1p3P2/4P3/PNN1BBQ1/1PPR2PP/1R4K1 b - - bm Kh7; id \"STS(v11.0) King Activity.094\"; c0 \"Kh7=10, Bf6=4, Kh8=8\";\n" +
            "r1b3qr/3kppb1/p2p4/2pPn1B1/1pP1P3/1N5P/1P2BQP1/1R3RK1 b - - bm Ke8; id \"STS(v11.0) King Activity.095\"; c0 \"Ke8=10\";\n" +
            "r2r2k1/p4pp1/b6p/8/1pp1Pn2/4NP2/PPB3PP/3RR1K1 w - - bm Kf2; id \"STS(v11.0) King Activity.096\"; c0 \"Kf2=10, g3=4, Nd5=2\";\n" +
            "r2r4/5qbk/2p3pp/pp2p3/Pn1P1p1Q/4P2N/1P1N1PPP/2RR2K1 b - - bm Kg8; id \"STS(v11.0) King Activity.097\"; c0 \"Kg8=10, Bf6=7, Qe8=2, Qf6=6\";\n" +
            "r4b1r/pp1k1pp1/4bn1p/3p4/4p3/1NN3P1/PPP1PPBP/3R1RK1 b - - bm Kc7; id \"STS(v11.0) King Activity.098\"; c0 \"Kc7=10, Kc6=7, Ke8=7\";\n" +
            "r5k1/4pp1p/6p1/3q4/1P6/r1P2P2/PKR1Q1PP/R7 w - - bm Kc1; id \"STS(v11.0) King Activity.099\"; c0 \"Kc1=10, Kb1=3, Kb1=9\";\n" +
            "R7/2k5/2p2p2/4p3/1pp1P1b1/n7/6P1/2R2K2 b - - bm Kb6; id \"STS(v11.0) King Activity.100\"; c0 \"Kb6=10\";" +
            "";

    private static final String[] SPLIT_UP_POSITIONS = POSITIONS.split("\n");
}
