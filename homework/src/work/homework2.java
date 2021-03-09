package work;
/*
* PRINTS ALL MOVES POSSIBLE FOR THE GIVEN BOARD INPUT
* */
import model.Board;
import model.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static work.Helper.generateAndPrintAllLegalMoves;

public class homework2 {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            //scanner = new Scanner(new File("src/work/externalInput.txt"));
            scanner = new Scanner(new File("homework/src/work/inputNumber.txt"));
            String typeOfPlay = scanner.nextLine();

            boolean isNextMoveBlack = scanner.nextLine().equals(BLACK);

            double playTimeInSeconds = Double.parseDouble(scanner.nextLine());
            char boardArray[][] = new char[8][8];
            Map<Integer, Character> map = new HashMap<>();
            map.put(0, '.');
            map.put(1, 'b');
            map.put(2, 'w');
            map.put(3, 'B');
            map.put(4, 'W');

            for(int i=0; i<8; i++) {
                String[] nextRow = scanner.nextLine().split(" ");
                for(int j=0; j<8; j++) {
                    int player = Integer.parseInt(nextRow[j]);
                    boardArray[i][j] = map.get(player);
                }
            }

            generateAndPrintAllLegalMoves(boardArray, isNextMoveBlack);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addToOutputFile() {

        try {
            FileWriter fw = new FileWriter("output.txt");
            fw.write("FAIL" + "\n");
            StringBuilder sbr = new StringBuilder();
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}




