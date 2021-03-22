package work;

import core.Engine;
import model.Board;
import model.Move;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static work.Helper.*;

public class peerGame {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    public static void main(String[] args) {
            long startTime = System.currentTimeMillis();
            Scanner scanner = new Scanner(System.in);

            Board board = new Board(Board.getInitialBoardArray(), false, false, true);

            board.printBoard();
            board.printTotalRemainingCoins();
            Move nextMove;

            if (board.getNextMoveColor() == board.getEngineColor()) {
                int depth = getRandomNumber(4, 10);
                System.out.println("DEPTH: " + depth);
                nextMove = generateAndOutputBestMove(board, depth);
                System.out.println("Next turn:  " + (board.isNextMoveBlack() ? "BLACK" : "WHITE"));
                System.out.println();
            }
            else {
                System.out.println("Pick a move: ");
                List<Move> moves = generateAndPrintAllLegalMoves(board);
                System.out.println();
                int moveNo = Integer.parseInt(scanner.nextLine()) - 1;
                nextMove = moves.get(moveNo);
            }

            while(nextMove != null) {
                System.out.println("Next move :" + nextMove.toNumericString());
                board = Engine.getNextState(board, nextMove);
                board.printBoard();
                board.printTotalRemainingCoins();
                System.out.println("Next turn:  " + (board.isNextMoveBlack() ? "BLACK" : "WHITE"));

                if (board.getNextMoveColor() == board.getEngineColor()) {
                    int depth = getRandomNumber(4, 10);
                    System.out.println("DEPTH: " + depth);
                    nextMove = generateAndOutputBestMove(board, depth);
                    System.out.println("Next turn:  " + (board.isNextMoveBlack() ? "BLACK" : "WHITE"));
                    System.out.println();
                }
                else {
                    System.out.println("Pick a move: ");
                    List<Move> moves = generateAndPrintAllLegalMoves(board);
                    int moveNo = Integer.parseInt(scanner.nextLine()) - 1;
                    nextMove = moves.get(moveNo);
                }
                System.out.println();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("TOTAL GAME TIME: " + (endTime - startTime) + " milli secs");
    }
}




