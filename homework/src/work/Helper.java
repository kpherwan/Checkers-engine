package work;

import model.Board;
import model.Move;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Helper {
    public static void generateAndPrintAllLegalMoves(char[][] boardArray, boolean isNextMoveBlack) {
        Board board = new Board(boardArray, isNextMoveBlack);
        List<Move> moves = board.generateAllLegalMoves();

        int i = 1;
        for(Move move: moves) {
            if (move.getTypeOfMove() == Move.TypeOfMove.E) {
                System.out.println((i++) + ": " + move.toNumericString());
            }
            else {
                for(Move jump: move.jumps) {
                    System.out.println(jump.toNumericString());
                }
                System.out.println();
            }
        }

        i = 1;
        for(Move move: moves) {
            if (move.getTypeOfMove() == Move.TypeOfMove.E) {
                System.out.println((i++) + ": " + move.toString());
            }
            else {
                for(Move jump: move.jumps) {
                    System.out.println(jump.toString());
                }
                System.out.println();
            }
        }

        boolean isOneOutputDone = false;
        i = 1;
        try {
            FileWriter fw = new FileWriter("output.txt");
            for(Move move: moves) {
                if (move.getTypeOfMove() == Move.TypeOfMove.E) {
                    System.out.println((i++) + ": " + move.toAlphaNumericString());
                    if (!isOneOutputDone) {
                        fw.write(move.toAlphaNumericString() + "\n");
                        isOneOutputDone = true;
                    }
                } else {
                    for (Move jump : move.jumps) {
                        System.out.println(jump.toAlphaNumericString());
                        if (!isOneOutputDone) {
                            fw.write(jump.toAlphaNumericString() + "\n");
                        }
                    }
                    isOneOutputDone = true;
                    System.out.println();
                }
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
