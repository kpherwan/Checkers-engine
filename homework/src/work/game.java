package work;

import core.Engine;
import model.Board;
import model.Move;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

import static work.Helper.generateAndOutputBestMove;

public class game {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void main(String[] args) {
            long startTime = System.currentTimeMillis();
            /* boolean isEngineBlack = new Random().nextBoolean();
            System.out.println("Engine is : " + (isEngineBlack ? "BLACK" : "WHITE"));*/
            int depth = getRandomNumber(3, 12);
            System.out.println("DEPTH: " + depth);

            Board board = new Board(Board.getInitialBoardArray(), true, true, true);
            board.printBoard();
            board.printTotalRemainingCoins();
            Move nextMove = generateAndOutputBestMove(board, depth);
            System.out.println("Next turn:  " + (board.isNextMoveBlack() ? "BLACK" : "WHITE"));
            System.out.println();

            while(nextMove != null) {
                System.out.println("Next move :" + nextMove.toString());
                board = Engine.getNextState(board, nextMove);
                board.isEngineBlack = board.isNextMoveBlack();
                board.printBoard();
                board.printTotalRemainingCoins();
                System.out.println("Next turn:  " + (board.isNextMoveBlack() ? "BLACK" : "WHITE"));

                nextMove = generateAndOutputBestMove(board, depth);
                System.out.println();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("TOTAL GAME TIME: " + (endTime - startTime) + " milli secs");
    }
}




