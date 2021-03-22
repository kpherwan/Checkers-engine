package work;

import model.Board;
import model.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static work.Helper.*;

public class homework {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            long startTime = System.currentTimeMillis();
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

            int depth = getBestDepth(typeOfPlay, playTimeInSeconds);
            Board board = new Board(boardArray, isNextMoveBlack, isNextMoveBlack, typeOfPlay.equals(GAME));
            generateAndOutputBestMove(board, depth);

            long endTime = System.currentTimeMillis();
            System.out.println("TOTAL time taken: " + (endTime - startTime) + " milli secs");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getBestDepth(String typeOfPlay, double playTimeInSeconds) {
        int depth = 4;

        switch(typeOfPlay) {
            case SINGLE:
                if (playTimeInSeconds < 1) {
                    return 2;
                }
                return 4;
            case GAME:
                if (playTimeInSeconds < 50) {
                    return 4;
                }
                return getRandomNumber(4, 10);
        }
        return depth;
    }
}




