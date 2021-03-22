package work;

import model.Board;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static work.Helper.*;

public class homeworkAllMoves {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            //scanner = new Scanner(new File("input.txt"));
            scanner = new Scanner(new File("homework/src/work/input6.txt"));
            String typeOfPlay = scanner.nextLine();

            boolean isNextMoveBlack = scanner.nextLine().equals(BLACK);

            double playTimeInSeconds = Double.parseDouble(scanner.nextLine());
            char boardArray[][] = new char[8][8];

            for(int i=0; i<8; i++) {
                char[] nextRow = scanner.nextLine().toCharArray();
                boardArray[i] = nextRow;
            }

            Board board = new Board(boardArray, isNextMoveBlack, isNextMoveBlack, typeOfPlay.equals(GAME));
            generateAndPrintAllLegalMovesAllFormats(board);
            generateAndOutputBestMove(board, 10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




