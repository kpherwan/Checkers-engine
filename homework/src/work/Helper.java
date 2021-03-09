package work;

import model.Board;
import model.Move;

import java.util.List;

public class Helper {
    public static void generateAndPrintAllLegalMoves(char[][] boardArray, boolean isNextMoveBlack) {
        Board board = new Board(boardArray, isNextMoveBlack);
        List<Move> moves = board.generateAllLegalMoves();

        int i = 1;
        for(Move move: moves) {
            System.out.println((i++) + ": " + move.toNumericString());
            if (move.subsequentJumps != null) {
                for(Move jump: move.subsequentJumps) {
                    System.out.println((i++) + ": " + jump.toNumericString());
                }
            }
        }
    }
}
