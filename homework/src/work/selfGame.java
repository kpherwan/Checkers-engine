package work;

import core.Engine;
import model.Board;
import model.Move;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

import static work.Helper.generateAndOutputBestMove;
import static work.Helper.getRandomNumber;

public class selfGame {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    public static void main(String[] args) {
            long startTime = System.currentTimeMillis();
            int depth = getRandomNumber(4, 10);
            System.out.println("DEPTH: " + depth);
            int moveNum = 1;

            Board board = new Board(Board.getInitialBoardArray(), true, true, true);
            board.printBoard();
            board.printTotalRemainingCoins();
            Move nextMove = generateAndOutputBestMove(board, depth);
            System.out.println("Next turn:  " + (board.isNextMoveBlack() ? "BLACK" : "WHITE"));
            System.out.println();

            while(nextMove != null) {
                System.out.println("Move #" + (moveNum++) + " : " + nextMove.toString() + " " + nextMove.getTypeOfMove());
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




