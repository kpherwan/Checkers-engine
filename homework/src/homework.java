import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class homework {
    private static final String SINGLE = "SINGLE";
    private static final String GAME = "GAME";

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            //scanner = new Scanner(new File("src/work/externalInput.txt"));
            scanner = new Scanner(new File("homework/src/input.txt"));
            String typeOfPlay = scanner.nextLine();

            String nextMoveColor = scanner.nextLine();

            double playTimeInSeconds = Double.parseDouble(scanner.nextLine());
            char board[][] = new char[8][8];

            for(int i=0; i<8; i++) {
                char[] nextRow = scanner.nextLine().toCharArray();
                board[i] = nextRow;
            }

            /* Printing path
            for(int i=0; i<8; i++) {
                System.out.println(Arrays.toString(board[i]));
            }*/

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




