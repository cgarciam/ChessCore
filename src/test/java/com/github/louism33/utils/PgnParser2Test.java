package com.github.louism33.utils;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.louism33.utils.ExtendedPositionDescriptionParser.parseEDPPosition;

@RunWith(Parameterized.class)
public class PgnParser2Test {

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

    public PgnParser2Test(final Object edp, final Object name) {
        EPDObject = (ExtendedPositionDescriptionParser.EPDObject) edp;
    }

    @SuppressWarnings("unused")
    @org.junit.Test
    public void test() {
        try {
            int[] winningMoves = EPDObject.getBestMovesFromComments();
            int[] losingMoves = EPDObject.getAvoidMoves();
        } catch (Exception | Error e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private static final String POSITIONS = "" +
            "1k2r2r/1bq2p2/pn4p1/3pP3/pbpN1P1p/4QN1B/1P4PP/2RR3K b - - bm Nd7; c0 \"Nd7=10, Bc5=8, Bc6=2, Be7=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.001\";\n" +
            "1q2bn2/6pk/2p1pr1p/2Q2p1P/1PP5/5N2/5PP1/4RBK1 w - - bm Ne5; c0 \"Ne5=10, Nd4=8, Ra1=6, b5=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.002\";\n" +
            "1r1q1rk1/1b1n1p1p/p2b1np1/3pN3/3P1P2/P1N5/3BB1PP/1R1Q1RK1 b - - bm Ne4; c0 \"Ne4=10, Bxa3=6, Nb6=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.003\";\n" +
            "1r1r1bk1/1bq2p1p/pn2p1p1/2p1P3/5P2/P1NBB3/1P3QPP/R2R2K1 b - - bm Nd5; c0 \"Nd5=10, Ba8=8, Kg7=8, a5=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.004\";\n" +
            "1r1r2k1/5pp1/p2p4/1p2pnqp/1BP1Q3/PP1R2P1/5P1P/3R2K1 b - - bm Nd4; c0 \"Nd4=10, Qf6=5, bxc4=3, h4=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.005\";\n" +
            "1r1r4/R3pk2/4n1p1/2p2p2/8/4B3/Pn2BPPP/5RK1 b - - bm Nd4; c0 \"Nd4=10, Nd3=1, c4=4, f4=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.006\";\n" +
            "1r2k2r/pp2ppb1/2n2np1/7p/4P3/P3BB1P/1P1N1PP1/R2R2K1 b k - bm Nd7; c0 \"Nd7=10, Bh6=6, a6=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.007\";\n" +
            "1r2qrk1/3bn3/pp1p3p/n1p1p1p1/P1P5/B1PP1NPP/2Q2PB1/1R2R1K1 w - - bm Nd2; c0 \"Nd2=10, Bc1=6, Qe2=9, Rb2=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.008\";\n" +
            "1r2r2k/2b2q1p/p4p2/3Pn2P/3N1N2/1P2R3/4Q3/1K1R4 w - - bm Nfe6; c0 \"Nfe6=10, Ka1=4, Nf5=6, Qxa6=2, Rc3=3, h6=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.009\";\n" +
            "1r3rk1/8/3p3p/p1qP2p1/R1b1P3/2Np1P2/1P1Q1RP1/6K1 w - - bm Nd1; c0 \"Nd1=10, Na2=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.010\";\n" +
            "1r6/1q2b1k1/pn1pb3/B1p1p1pp/2P1Pp2/NP3P1P/1R2Q1PN/6K1 b - - bm Nc8; c0 \"Nc8=10, Bg8=8, Kg8=8, Nd7=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.011\";\n" +
            "1r6/2qnrpk1/2pp1np1/pp2P3/4P3/PBN2Q2/1PPR1PP1/3R2K1 b - - bm Nxe5; c0 \"Nxe5=10, Nxe4=8, Rxe5=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.012\";\n" +
            "1rr2qk1/3p1pp1/1pb2n1p/4p3/p1P1P2P/P1NQ1BP1/1P3PK1/2RR4 w - - bm Nb5; c0 \"Nb5=10, Kh2=5, Rc2=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.013\";\n" +
            "2b2rk1/1r1nbppp/4p3/1p2P3/p4P2/P1N1B3/BPP4P/R2R2K1 w - - bm Ne4; c0 \"Ne4=10, Rac1=1, Rd2=3, b4=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.014\";\n" +
            "2b3n1/6kp/p1nB1pp1/8/1p2P1P1/4NP2/PP3K2/3B4 w - - bm Nd5; c0 \"Nd5=10, Ba4=8, Kg3=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.015\";\n" +
            "2b5/2p1r2k/1pP2q1p/p2Pp3/4R3/1PN1Q2P/P2KP3/8 w - - bm Nb5; c0 \"Nb5=10, Kc1=7, Rc4=8, h4=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.016\";\n" +
            "2k3r1/1b2bp2/2p2n2/ppn1p1Bp/2p1P2P/P4B2/1P1RN1P1/4K2R b K - bm Nd3+; c0 \"Nd3+=10, Kc7=9, Ne6=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.017\";\n" +
            "2r2r1k/p3ppbp/qpnp2pn/5P2/2P1PP2/P1N1BB2/1PQ3RP/3R2K1 w - - bm Nb5; c0 \"Nb5=10, Be2=8, Nd5=9, fxg6=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.018\";\n" +
            "2r2rk1/4bpp1/p2pbn1p/Pp2p3/1Pq1P2N/2P4P/1BB2PP1/R2QR1K1 w - - bm Nf5; c0 \"Nf5=10, Bb1=5, Qe2=5, Qf3=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.019\";\n" +
            "2r3k1/3q1pp1/ppr1p1np/4P3/P1nPQ3/5N1P/5PPK/RRB5 b - - bm Ne7; c0 \"Ne7=10, Qc7=9, Qd8=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.020\";\n" +
            "2r3k1/p1r1qpb1/1p2p1p1/nR2P3/P2B4/2P5/3NQPP1/R5K1 w - - bm Ne4; c0 \"Ne4=10, Nb3=8, f4=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.021\";\n" +
            "2rq1rk1/1p2b1p1/pn2p3/2p1Pn2/2pP3p/5N1P/PPQ2BP1/1BRR2K1 b - - bm Nd5; c0 \"Nd5=10, Bg5=6, Qc7=6, Qe8=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.022\";\n" +
            "r7/p1r2nk1/1pNq1np1/1P1p1p2/P2Qp3/4P1P1/2R1P1BP/2R3K1 b - - bm Ng5; id \"STS(v3.1) Knight Outposts/Centralization/Repositioning.023\"; c0 \"Ng5=10, Rac8=4, Rb7=3, Re8=3\";\n" +
            "2rq2k1/3nbppp/pprp1nb1/4p3/P1P1P3/1PN1BN1P/2Q1BPP1/R2R2K1 w - - bm Nh4; c0 \"Nh4=10, Ra2=5, Rab1=6, g3=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.024\";\n" +
            "2rqr1k1/1p2bppp/p2p1n2/3P1P2/2Pp4/1P1B4/P3Q1PP/R1B2RK1 b - - bm Nd7; c0 \"Nd7=10, Qa5=7, h6=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.025\";\n" +
            "2rr4/Bp3k1p/5pp1/8/2n3b1/P1N5/1PP2PPP/R1R3K1 w - - bm Ne4; c0 \"Ne4=10, Na4=9, f3=9, h3=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.026\";\n" +
            "3R4/5pk1/2p4r/1p2p1p1/p3P1P1/P1P2P2/1P2B1K1/n7 b - - bm Nb3; c0 \"Nb3=10, Rf6=9, Rg6=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.027\";\n" +
            "3q3r/2p2pk1/6p1/2p1p1Pn/1pBnP3/1P2BP1R/P5Q1/7K b - - bm Nf4; c0 \"Nf4=10, Qd7=5, c6=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.028\";\n" +
            "3r1bk1/1rq2p2/2npb1p1/p3p2p/2P1P2P/1PN3P1/2N1QPBK/R2R4 w - - bm Nb5; c0 \"Nb5=10, Rdb1=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.029\";\n" +
            "3r1rk1/1p4bp/2qPp1p1/p3n3/P2BN3/1PN4P/2PR2P1/4Q1K1 w - - bm Nb5; c0 \"Nb5=10, Qe3=5, Qh4=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.030\";\n" +
            "3r2k1/2q2ppp/p1p1bn2/1p2b1B1/4P3/1PN2B1P/P1Q2PP1/2R4K w - - bm Nd5; c0 \"Nd5=10\"; id \"STS: Knight Outposts/Repositioning/Centralization.031\";\n" +
            "3r2k1/4qpn1/R2p3p/1Pp1p1p1/1rP1P1P1/6P1/3Q1P2/4RBK1 b - - bm Ne6; c0 \"Ne6=10, Kf8=5, Kh7=4, Rd7=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.032\";\n" +
            "3r4/2p2pk1/2q1n1p1/2p1p1Pn/1pB1P3/1P2BP2/P6R/4Q1K1 b - - bm Nd4; c0 \"Nd4=10, Kg8=8, Nhf4=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.033\";\n" +
            "3r4/bp1r2pk/p3npqp/P2Np3/1PR1P2B/5Q1P/2P3P1/5R1K b - - bm Nd4; c0 \"Nd4=10, Bb8=4, Kh8=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.034\";\n" +
            "3r4/r1pb3p/1p4kB/2p3P1/4pP2/1P2NnKP/PR3R2/8 b - - bm Nd4; c0 \"Nd4=10, Bc8=2, Bf5=2, c4=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.035\";\n" +
            "3rb1k1/4qpbp/1p2p1p1/1P3n2/Q1P2p2/2N2B1P/6PK/1NR2R2 b - - bm Ne3; c0 \"Ne3=10, Bd4=3, Rb8=7, Rc8=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.036\";\n" +
            "3rr1k1/1p3ppp/p1q2b2/P4P2/2P1p3/1P6/2N1Q1PP/4RR1K w - - bm Nb4; c0 \"Nb4=10, Ne3=9, Qh5=9, b4=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.037\";\n" +
            "3rr1k1/pb1n1pp1/2q2b1p/2p5/2P1p2N/1P2B1P1/P1QR1PBP/3R2K1 b - - bm Ne5; c0 \"Ne5=10, Bxh4=7, Nf8=7, g6=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.038\";\n" +
            "3rr3/p3b1kp/2p2pp1/1q1np3/4Q1PB/1NP5/PP3P1P/R2R2K1 b - - bm Nf4; c0 \"Nf4=10, Kf7=3, Qa6=2, a5=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.039\";\n" +
            "4b1nk/p1r1p1rp/Bpq5/n3Ppp1/8/5N1P/2P2BPQ/R3R1K1 w - - bm Nd4; c0 \"Nd4=10, Bd3=5, Rad1=3, e6=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.040\";\n" +
            "4k2r/1r2np2/2q1p1p1/p2pP3/n1pP1PP1/1pP1NNK1/1P2Q3/R4R2 w k - bm Ng5; c0 \"Ng5=10, Qd2=3, Qg2=3, Rf2=4, Rh1=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.041\";\n" +
            "4n3/1p1b1pk1/2n5/rN4p1/1p1Np3/1B2P1P1/PP3PK1/2R5 b - - bm Ne5; c0 \"Ne5=10\"; id \"STS: Knight Outposts/Repositioning/Centralization.042\";\n" +
            "4n3/1p1b1pk1/8/rNR1n1p1/1p1Np3/1B2P1P1/PP3PK1/8 b - - bm Nf3; c0 \"Nf3=10, Bxb5=3, f6=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.043\";\n" +
            "4nk2/p4rr1/1pRp3b/1P1Pp2p/1P5P/2NBpP2/4R1P1/5K2 w - - bm Ne4; c0 \"Ne4=10, Kg1=1, Nd1=1, Rc8=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.044\";\n" +
            "4r1k1/2pbqp1p/1r3p2/pP1p4/8/P1QBPN2/3P1PPP/4K2R w K - bm Nd4; c0 \"Nd4=10, Qxc7=6, a4=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.045\";\n" +
            "4r1k1/3q1pp1/3p1n2/rp2nP1p/3B1R2/2N5/2PQ2PP/4R1K1 w - - bm Ne4; c0 \"Ne4=10, Kh1=8, Rb1=6, h3=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.046\";\n" +
            "4r1k1/3r2p1/b1q2n1p/p7/Pp2P2Q/1N2BP2/1PP4P/2R1R2K w - - bm Nd4; c0 \"Nd4=10, Nc5=6, Nxa5=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.047\";\n" +
            "4r3/1p3pk1/2p2n1p/2n1qP2/5bPQ/P3pB2/2R5/1R3N1K b - - bm Nfe4; c0 \"Nfe4=10, Nd3=9, Nh7=9, Qd4=2, Rd8=1\"; id \"STS: Knight Outposts/Repositioning/Centralization.048\";\n" +
            "4rrk1/p4pp1/1b1p3p/2pP4/6q1/5N2/PP3PPP/2RQ1R1K w - - bm Nd2; c0 \"Nd2=10, Kg1=7, Qd3=2, h3=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.049\";\n" +
            "5k2/6p1/Bnp1p2p/5p2/3P1n2/2q2P2/7P/5RQK b - - bm Nbd5; c0 \"Nbd5=10, Kf7=7, Kg8=4, g6=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.050\";\n" +
            "5r1k/2p3q1/1p1npr2/pPn1N1pp/P1PN4/R4PPP/4Q1K1/3R4 w - - bm Ndc6; c0 \"Ndc6=10, Kh1=1, Rb1=8, Rc1=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.051\";\n" +
            "5r2/2r2kp1/3n1p2/1p1Pp2p/p3P2P/PnP4R/1BB2KP1/4R3 b - - bm Nc4; c0 \"Nc4=10, Ke7=9, Rb8=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.052\";\n" +
            "5rk1/3nbp1p/2p1p3/2PpP1pN/3P2B1/q3PQ1P/6P1/5RK1 w - - bm Nf6+; c0 \"Nf6+=10, Qf2=1, Qg3=4, h4=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.053\";\n" +
            "5rk1/6pp/pn1qp1r1/1p2R3/2pP1P2/P1P2Q1P/5P2/2B1R2K b - - bm Nd5; c0 \"Nd5=10, Nd7=9, Qd7=9, Rgf6=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.054\";\n" +
            "5rk1/pb1qbppp/1p6/n1rpP3/7P/2P2NP1/P3QPB1/R1BR2K1 w - - bm Nd4; c0 \"Nd4=10, Bb2=2, Bd2=1\"; id \"STS: Knight Outposts/Repositioning/Centralization.055\";\n" +
            "6k1/3pbpp1/p3p2n/r3P2p/2rBNP2/2P3PP/P3R3/3R3K b - - bm Nf5; c0 \"Nf5=10, Rd5=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.056\";\n" +
            "6k1/p2b3n/1p2pn1q/3r1p2/5P1B/P5NR/1Q2B1P1/4K3 b - - bm Ng4; c0 \"Ng4=10, Qf8=1\"; id \"STS: Knight Outposts/Repositioning/Centralization.057\";\n" +
            "7k/Rb4r1/3pPp1q/1pp2P2/3n2BP/4N1K1/1P3Q2/8 b - - bm Nc6; c0 \"Nc6=10, Kg8=8, Kh7=6, Qxe3+=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.058\";\n" +
            "8/1b5p/1p1rrkp1/p2p1p2/P2P3P/1R1B1N2/nP3PPK/3R4 w - - bm Ne5; c0 \"Ne5=10, Bb5=4, h5=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.059\";\n" +
            "8/1p2kp2/p3pn2/4n1p1/1P2P3/P1r1NB1P/5PPK/R7 b - - bm Ne8; c0 \"Ne8=10, Nxf3+=1, b5=3, b6=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.060\";\n" +
            "8/1q6/3pn1k1/2p1p1p1/2P1P1Pp/1rBP1p1P/2Q2P2/R5K1 b - - bm Nf4; c0 \"Nf4=10, Kf7=4, Kg7=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.061\";\n" +
            "8/2k2p2/pr1pbpn1/2p5/2P1P1P1/2P1KP1p/7P/R2N1B2 b - - bm Ne5; c0 \"Ne5=10, Kd7=5, Rb3=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.062\";\n" +
            "8/pB1b2k1/1p2pn2/5p2/5P1B/P7/3K1NPn/8 b - - bm Ne8; c0 \"Ne8=10, Bb5=8, Kg6=7, Nf1+=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.063\";\n" +
            "b1r2r1k/p2qp1bp/1p1pn1p1/8/1PP2P2/R1NBB3/P2Q2PP/5RK1 w - - bm Nb5; c0 \"Nb5=10, Be2=7, Kh1=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.064\";\n" +
            "b4q2/1r5k/3p4/1p1Pn2B/1PpQP3/2N4P/6P1/B5K1 w - - bm Nd1; c0 \"Nd1=10, Ne2=6, Qe3=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.065\";\n" +
            "q3n3/2rp1ppk/2b4p/4p2P/p3P3/Pr2NBP1/1P1R1PK1/2R1Q3 w - - bm Nd5; c0 \"Nd5=10, Kg1=3, Qe2=1\"; id \"STS: Knight Outposts/Repositioning/Centralization.066\";\n" +
            "r1b1k1nr/pp2p1bp/1q1p2p1/2pP4/2Pnp3/2NB1N1P/PP1B1PP1/R2QK2R w KQkq - bm Nxe4; c0 \"Nxe4=10, Bxe4=3, Na4=7, Nxd4=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.067\";\n" +
            "r1b2r1k/pp3pbp/3n4/4p3/2N4R/2N5/PP3PPP/R1B3K1 b - - bm Nf5; c0 \"Nf5=10, Bf6=2, Nxc4=3, Rd8=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.068\";\n" +
            "r1b2rk1/1pq1bppp/p2p4/P3p3/2N1n3/4B3/1PP1BPPP/R2QK2R w KQ - bm Nb6; c0 \"Nb6=10, O-O=9, f3=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.069\";\n" +
            "r1b2rk1/p2q2b1/1nppp1pp/5pN1/P2P1B2/Q5P1/1P2PPBP/R1R3K1 b - - bm Nd5; c0 \"Nd5=10, Bb7=3, hxg5=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.070\";\n" +
            "r1b2rk1/pp3pp1/2np2qp/b1p1p3/2P1P3/2NPB1P1/PP3PBP/R2Q1RK1 w - - bm Nd5; c0 \"Nd5=10, Qb3=1, Qc2=1, f4=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.071\";\n" +
            "r1bq1rk1/ppp3bp/n2p1pp1/3P4/2P1Pp2/2N2N1P/PP2BPP1/R2QR1K1 w - - bm Nd4; c0 \"Nd4=10, Bf1=9, Qd2=4, Rc1=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.072\";\n" +
            "r1r3k1/pb3p1p/1pqBp1p1/4P3/3b4/2P2P2/PR1N2PP/2RQ3K w - - bm Ne4; c0 \"Ne4=10\"; id \"STS: Knight Outposts/Repositioning/Centralization.073\";\n" +
            "r2b2k1/2pr4/1pn1b1qp/3Np1p1/p1P1p3/1P2B1P1/PQ2PP1P/R2R2NK b - - bm Nd4; c0 \"Nd4=10, Bf5=4, a3=5, h5=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.074\";\n" +
            "r2br3/p2b1q1k/n2P2pp/1p1N1p2/4pP2/BP2Q1PN/P1R4P/3R2K1 w - - bm Nc7; c0 \"Nc7=10, Nf2=3, Qd4=2, Qe2=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.075\";\n" +
            "r2q1bk1/1p1brpp1/p1np1n1p/4p3/PN2P3/1QPP1N1P/B2B1PP1/R3R1K1 w - - bm Nd5; c0 \"Nd5=10, Be3=4, Qb2=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.076\";\n" +
            "r2q1k2/2p2pb1/p2n2rp/1p1RB1p1/8/2PQRN1P/P4PP1/6K1 w - - bm Nd4; c0 \"Nd4=10, Bxg7+=2, Re2=3, h4=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.077\";\n" +
            "r2q1rk1/3nbpp1/p2p3p/8/1p1BP3/3B2Q1/PPP4P/2KR3R b - - bm Ne5; c0 \"Ne5=10, Bf6=1, Bg5+=4, g6=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.078\";\n" +
            "r2q1rk1/pp1bp1bp/5np1/2pP1p2/8/2N2NP1/PP2PPBP/2RQ1RK1 w - - bm Ne5; c0 \"Ne5=10, Ng5=5, Qb3=7, e3=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.079\";\n" +
            "r2q2kr/p3n3/1p1Bp1bp/3pP1pN/5PPn/3B3Q/2P2K2/1R5R w - - bm Nf6+; c0 \"Nf6+=10, Bxg6=3, Rbg1=2\"; id \"STS: Knight Outposts/Repositioning/Centralization.080\";\n" +
            "r2qk2r/1bpnnpbp/p2pp1p1/4P3/Pp1P1P2/2NBBN2/1PP3PP/R2Q1RK1 w kq - bm Ne4; c0 \"Ne4=10, Na2=2, Ne2=2, exd6=8\"; id \"STS: Knight Outposts/Repositioning/Centralization.081\";\n" +
            "r2qr1k1/pp3ppp/2n2nb1/1P4B1/3p4/P2B1P2/2P1N1PP/R2Q1RK1 b - - bm Ne5; c0 \"Ne5=10, Na5=9, Ne7=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.082\";\n" +
            "r2r2k1/1bqn1pp1/1p1p1b1p/1B2n3/2P1P3/P1N1B3/1P1NQ1PP/4RR1K w - - bm Nd5; c0 \"Nd5=10, Bd4=9, Nb3=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.083\";\n" +
            "r2r2k1/1p1n2q1/2ppbp2/6p1/2PBP2p/p1N2P2/Pb4PP/1R1RQBK1 b - - bm Ne5; c0 \"Ne5=10, Qf7=2, Qh7=5, h3=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.084\";\n" +
            "r2rq3/pp1b3k/n2P1bpp/4pp2/8/BPN1Q1PN/P4P1P/2RR2K1 w - - bm Nd5; c0 \"Nd5=10, Bb2=5, Qd3=5, Qf3=5, f3=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.085\";\n" +
            "r3r1k1/1b1nq2p/p2pNppQ/1ppP3n/P3P3/7P/1PB2PP1/R3RNK1 b - - bm Nf8; c0 \"Nf8=10, Ne5=4, Rec8=6\"; id \"STS: Knight Outposts/Repositioning/Centralization.086\";\n" +
            "r3r1k1/1p1q2bp/1n1p2p1/1PpPpp1n/p3P3/R1NQBN1P/1PP2PP1/4R1K1 w - - bm Ng5; c0 \"Ng5=10, Qf1=7, Rd1=8, exf5=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.087\";\n" +
            "r3r1k1/3nbppp/p1q5/1pp1P2b/5B2/1P2QN2/1P1N1PPP/3RR1K1 w - - bm Ne4; c0 \"Ne4=10, Bg3=7, Rc1=7\"; id \"STS: Knight Outposts/Repositioning/Centralization.088\";\n" +
            "r3r1k1/4bppp/pnq5/1pp1P2b/4NB2/1P2QN2/1P3PPP/3RR1K1 w - - bm Nd6; c0 \"Nd6=10, Bg3=4, Qc1=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.089\";\n" +
            "r3r2k/pp3pp1/1np4p/3p2q1/1P1P2b1/P1NBP3/2Q2PPP/R4RK1 w - - bm Ne2; c0 \"Ne2=10, Kh1=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.090\";\n" +
            "r3r3/2P4k/3Bbbqp/ppQ2pp1/4pPP1/1P6/P1R2N1P/3R2K1 w - - bm Nxe4; c0 \"Nxe4=10, Qe3=4, Rcd2=5, Re2=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.091\";\n" +
            "r3rnk1/1bpq1pp1/p2p3p/1p1Pp1b1/P3P1P1/1BP1N1P1/1P3P2/R1BQR1K1 w - - bm Nf5; c0 \"Nf5=10, Bc2=3, Kg2=4, Kg2=9, Qe2=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.092\";\n" +
            "r4rk1/1b1q1ppp/pb1p1nn1/1pp1p3/1PP1P3/P2PNN1P/B2B1PP1/2RQR1K1 w - - bm Nf5; c0 \"Nf5=10, Bb1=7, Qb3=4\"; id \"STS: Knight Outposts/Repositioning/Centralization.093\";\n" +
            "r4rk1/1pq1bppp/1n2p3/p1n1P3/2PR4/2N1BN2/P3QPPP/1R4K1 w - - bm Nb5; c0 \"Nb5=10, Qd2=5, h4=9\"; id \"STS: Knight Outposts/Repositioning/Centralization.094\";\n" +
            "r4rk1/5p1p/p2qpnp1/1p2b3/3p4/3B1R2/PPQ3PP/R1BN3K b - - bm Nd7; c0 \"Nd7=10, Nd5=7, Nh5=5, Rac8=1, Rfc8=1\"; id \"STS: Knight Outposts/Repositioning/Centralization.095\";\n" +
            "r4rk1/ppp3b1/3p1q1p/3Ppn2/P1P3n1/2NQ1N2/1P1B1PP1/R3R1K1 w - - bm Ne4; c0 \"Ne4=10, Nb5=4, Nh2=1, Ra3=1\"; id \"STS: Knight Outposts/Repositioning/Centralization.096\";\n" +
            "r5r1/1pp2k1p/2bn4/2p3B1/p3pPP1/1P2N2P/P1P1R3/R5K1 b - - bm Nb5; c0 \"Nb5=10\"; id \"STS: Knight Outposts/Repositioning/Centralization.097\";\n" +
            "r6k/pp3pp1/1n6/1pQp1q2/3PrN1p/P3P2P/5PP1/2R2RK1 w - - bm Nd3; c0 \"Nd3=10, Ne2=2, Qxb5=2, Rce1=3\"; id \"STS: Knight Outposts/Repositioning/Centralization.098\";\n" +
            "r6r/1q1bbkp1/p1p1pn2/5p1p/N2Bp3/2Q3P1/PPP2PBP/R2R2K1 b - - bm Nd5; c0 \"Nd5=10, Rad8=5, h4=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.099\";\n" +
            "rqr3k1/1p2bppp/3pn3/p3p1Pn/P3P3/1PNBBP2/1P1Q3P/2KR3R b - - bm Nd4; c0 \"Nd4=10, Nef4=6, Nhf4=5\"; id \"STS: Knight Outposts/Repositioning/Centralization.100\";" +
            "";
    
    private static final String[] SPLIT_UP_POSITIONS = POSITIONS.split("\n");
}
