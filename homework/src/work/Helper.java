package work;

import core.Engine;
import model.Board;
import model.Move;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Helper {
    public static List<Move> generateAndPrintAllLegalMovesAllFormats(Board board) {
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

        System.out.println();
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
        System.out.println();

        i = 1;
        for(Move move: moves) {
            if (move.getTypeOfMove() == Move.TypeOfMove.E) {
                System.out.println((i++) + ": " + move.toAlphaNumericString());
            }
            else {
                for(Move jump: move.jumps) {
                    System.out.println(jump.toAlphaNumericString());
                }
                System.out.println();
            }
        }
        return moves;
    }

    public static List<Move> generateAndPrintAllLegalMoves(Board board) {
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

        return moves;
    }

    public static Move generateAndOutputBestMove(Board board, int depth) {
        try {
            long startTime = System.currentTimeMillis();
            FileWriter fw = new FileWriter("output.txt");
            Engine engine = new Engine(board, depth);
            Move bestMove = engine.getBestMoveWithGivenDepth();
            if (bestMove == null) {
                return null;
            }

            if (bestMove.getTypeOfMove() == Move.TypeOfMove.E) {
                System.out.println(bestMove.toAlphaNumericString());
                fw.write(bestMove.toAlphaNumericString() + "\n");
            } else {
                for (Move jump : bestMove.jumps) {
                        System.out.println(jump.toAlphaNumericString());
                        fw.write(jump.toAlphaNumericString() + "\n");
                }
            }
            fw.close();
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + (endTime - startTime) + " milli secs");
            System.out.println("Depth: " + depth);
            return bestMove;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
