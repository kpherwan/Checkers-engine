package model;

import java.io.File;
import java.util.*;

public class Board {
    public static final char EMPTY_CELL = '.';
    public static final char BLACK_WOMAN = 'b';
    public static final char WHITE_WOMAN = 'w';
    public static final char BLACK_QUEEN = 'B';
    public static final char WHITE_QUEEN = 'W';
    public static final String GAMEPLAY_FILE = "playdata.txt";

    private char[][] board;

    private int noOfBlackCoins;
    private int noOfWhiteCoins;
    private int noOfBlackQueenCoins;
    private int noOfWhiteQueenCoins;

    private boolean isNextMoveBlack;
    private int plysCompletedSoFar;
    private int plyNumberOfLastMaterialChange;
    public boolean isEngineBlack;

    public void printTotalRemainingCoins() {
        System.out.println("BLACK " + noOfBlackCoins);
        System.out.println("WHITE " + noOfWhiteCoins);
        System.out.println("BLACK QUEEN " + noOfBlackQueenCoins);
        System.out.println("WHITE QUEEN " + noOfWhiteQueenCoins);
    }

    public Map<Integer, Integer> getFreqOfBoardStates() {
        return freqOfBoardStates;
    }

    public void setFreqOfBoardStates(Map<Integer, Integer> freqOfBoardStates) {
        this.freqOfBoardStates = freqOfBoardStates;
    }

    private Map<Integer, Integer> freqOfBoardStates;

    public boolean isGameMode() {
        return isGameMode;
    }

    private boolean isGameMode;

    public char getEngineColor() {
        return isEngineBlack ? BLACK_QUEEN : WHITE_QUEEN;
    }

    public static char[][] getInitialBoardArray() {
        char boardArr[][] = new char[8][8];
        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                boardArr[i][j] = EMPTY_CELL;

        for (int i=0;i<8;i+=2) {
            boardArr[1][i] = BLACK_WOMAN;
            boardArr[5][i] = 'w';
            boardArr[7][i] = 'w';
        }

        for (int i=1;i<8;i+=2) {
            boardArr[0][i] = BLACK_WOMAN;
            boardArr[2][i] = BLACK_WOMAN;
            boardArr[6][i] = 'w';
        }
        return boardArr;
    }

    public Board(char[][] board, boolean isNextMoveBlack, boolean isEngineBlack, boolean isGameMode) {
        this(board, isNextMoveBlack, isEngineBlack);
        this.isGameMode = isGameMode;
        this.freqOfBoardStates = new HashMap<>();

        // updateFromGamePlayFile();
    }

    private void updateFromGamePlayFile() {
        if (isGameMode) {
            Scanner scanner = null;
            try {
                File tempFile = new File(GAMEPLAY_FILE);
                boolean exists = tempFile.exists();
                if (exists) {
                    scanner = new Scanner(new File(GAMEPLAY_FILE));
                    this.plysCompletedSoFar = Integer.parseInt(scanner.nextLine()) + 1;
                    this.plyNumberOfLastMaterialChange = Integer.parseInt(scanner.nextLine());

                    int updatedCountOfBlackCoins = Integer.parseInt(scanner.nextLine());
                    int updatedCountOfBlackQueens = Integer.parseInt(scanner.nextLine());
                    int updatedCountOfWhiteCoins = Integer.parseInt(scanner.nextLine());
                    int updatedCountOfWhiteQueens = Integer.parseInt(scanner.nextLine());

                    if (updatedCountOfBlackCoins != this.getNoOfBlackCoins() || updatedCountOfBlackQueens != this.getNoOfBlackQueenCoins() ||
                    updatedCountOfWhiteCoins != this.getNoOfWhiteCoins() && updatedCountOfWhiteQueens != this.getNoOfWhiteQueenCoins()) {
                        this.plyNumberOfLastMaterialChange = this.plysCompletedSoFar;
                    }

                    int countOfStates = Integer.parseInt(scanner.nextLine());
                    for(int i=0; i<countOfStates; i++) {
                        String[] freq = scanner.nextLine().split(" ");
                        freqOfBoardStates.put(Integer.parseInt(freq[0]), Integer.parseInt(freq[1]));
                    }
                }
                addToFreqStates(board);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addToFreqStates(char[][] board) {
        int hashCode = Arrays.deepHashCode(board);
        if (freqOfBoardStates.get(hashCode) != null) {
            freqOfBoardStates.put(hashCode, freqOfBoardStates.get(hashCode) + 1);
        }
        else {
            freqOfBoardStates.put(hashCode, 1);
        }
    }

    public Board(char[][] board, boolean isNextMoveBlack, boolean isEngineBlack) {
        this.board = board;
        this.isNextMoveBlack = isNextMoveBlack;
        this.isEngineBlack = isEngineBlack;

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == BLACK_WOMAN) {
                    incrementBlackCoins();
                }
                else if (board[i][j] == WHITE_WOMAN) {
                    incrementWhiteCoins();
                }
                else if (board[i][j] == BLACK_QUEEN) {
                    incrementBlackQueenCoins();
                }
                else if (board[i][j] == WHITE_QUEEN) {
                    incrementWhiteQueenCoins();
                }
            }
        }
    }

    public Board(char[][] board, boolean isNextMoveBlack, boolean isEngineBlack, int noOfBlackCoins, int noOfBlackQueenCoins, int noOfWhiteCoins,
                 int noOfWhiteQueenCoins, int plysCompletedSoFar, int plyNumberOfLastMaterialChange, Map<Integer, Integer> freqOfBoardStates) {
        this.board = board;
        this.isNextMoveBlack = isNextMoveBlack;
        this.isEngineBlack = isEngineBlack;

        this.noOfBlackCoins = noOfBlackCoins;
        this.noOfBlackQueenCoins = noOfBlackQueenCoins;
        this.noOfWhiteCoins = noOfWhiteCoins;
        this.noOfWhiteQueenCoins = noOfWhiteQueenCoins;
        this.plysCompletedSoFar = plysCompletedSoFar;
        this.plyNumberOfLastMaterialChange = plyNumberOfLastMaterialChange;
        this.freqOfBoardStates = freqOfBoardStates;
    }

    public void printBoard() {
        for(int i=0; i<8; i++) {
            System.out.println(i + " : " + Arrays.toString(board[i]));
        }
    }

    public void printBoardForInput() {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public List<Move> generateAllLegalMoves() {
        char curNormalPlayer = isNextMoveBlack ? BLACK_WOMAN : 'w';
        char curKingPlayer = isNextMoveBlack ? 'B' : 'W';

        List<Move> legalMoves = new ArrayList<>();
        List<Cell> cellsOfCurrentPlayer = new ArrayList<>();
        Move currentMove;
        int destinationRow;
        int destinationColumn;
        int captureRow;
        int captureColumn;

        //get all jumps
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (board[row][column] == curNormalPlayer || board[row][column] == curKingPlayer) {
                    cellsOfCurrentPlayer.add(new Cell(row,column));

                    destinationRow = row + 2;
                    destinationColumn = column + 2;
                    captureRow = row + 1;
                    captureColumn = column + 1;
                    currentMove = new Move(row,column, destinationRow,destinationColumn, Move.TypeOfMove.J);
                    if(currentMove.isMoveValid(board)){
                        List<Move> jumps = getJumpsAfterMultiJump(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.addAll(jumps);
                    }

                    destinationRow = row - 2;
                    destinationColumn = column - 2;
                    captureRow = row - 1;
                    captureColumn = column - 1;
                    currentMove = new Move(row,column, destinationRow, destinationColumn, Move.TypeOfMove.J);
                    if(currentMove.isMoveValid(board)){
                        List<Move> jumps = getJumpsAfterMultiJump(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.addAll(jumps);
                    }

                    destinationRow = row + 2;
                    destinationColumn = column - 2;
                    captureRow = row + 1;
                    captureColumn = column - 1;
                    currentMove = new Move(row,column, destinationRow,destinationColumn, Move.TypeOfMove.J);
                    if(currentMove.isMoveValid(board)){
                        List<Move> jumps = getJumpsAfterMultiJump(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.addAll(jumps);
                    }

                    destinationRow = row - 2;
                    destinationColumn = column + 2;
                    captureRow = row - 1;
                    captureColumn = column + 1;
                    currentMove = new Move(row,column, destinationRow,destinationColumn, Move.TypeOfMove.J);
                    if(currentMove.isMoveValid(board)){
                        List<Move> jumps = getJumpsAfterMultiJump(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.addAll(jumps);
                    }
                }
            }
        }

        if (legalMoves.isEmpty()) {
            for (Cell currentCell: cellsOfCurrentPlayer) {
                currentMove = new Move(currentCell.row, currentCell.column, currentCell.row + 1,
                        currentCell.column + 1, Move.TypeOfMove.E);
                if(currentMove.isMoveValid(board))
                    legalMoves.add(currentMove);

                currentMove = new Move(currentCell.row, currentCell.column, currentCell.row - 1,
                        currentCell.column - 1, Move.TypeOfMove.E);
                if(currentMove.isMoveValid(board))
                    legalMoves.add(currentMove);

                currentMove = new Move(currentCell.row, currentCell.column, currentCell.row + 1,
                        currentCell.column - 1, Move.TypeOfMove.E);
                if(currentMove.isMoveValid(board))
                    legalMoves.add(currentMove);

                currentMove = new Move(currentCell.row, currentCell.column, currentCell.row - 1,
                        currentCell.column + 1, Move.TypeOfMove.E);
                if(currentMove.isMoveValid(board))
                    legalMoves.add(currentMove);
            }
        }

        return legalMoves;
    }

    private List<Move> getJumpsAfterMultiJump(char board[][], int row, int column, int captureRow, int captureColumn,
                                    int destinationRow, int destinationColumn, Move currentMove) {
        char[][] newBoard = new char[8][8];
        System.arraycopy(board, 0, newBoard, 0, 8);
        List<Move> allJumps = new ArrayList();
        getAllSubsequentJumps(board[row][column], row, column, captureRow, captureColumn, destinationRow, destinationColumn, newBoard,
                destinationRow + "_" + destinationColumn, new Stack<Move>(), allJumps);
        return allJumps;
        // currentMove.subsequentJumps = convertStringToMoves(subsequentJumps);
    }

    private List<Move> convertStringToMoves(List<String> subsequentJumps) {
        List<Move> moves = new ArrayList<>();

        for (String jumpString: subsequentJumps) {
            String[] cells = jumpString.split(":");
            for (int i = 1; i < cells.length; i++) {
                String[] source = cells[i-1].split("_");
                String[] dest = cells[i].split("_");
                moves.add(new Move(Integer.parseInt(source[0]), Integer.parseInt(source[1]), Integer.parseInt(dest[0]),
                        Integer.parseInt(dest[1]), Move.TypeOfMove.J));
            }
        }
        return moves;
    }

    void getAllSubsequentJumps(char player, int sourceRow, int sourceColumn, int captureRow, int captureColumn,
                               int destinationRow, int destinationColumn, char[][] tempBoard, String source,
                               Stack<Move> stack, List<Move> jumps) {
        //EXECUTE MOVE
        Move currentMove;
        currentMove = new Move(sourceRow, sourceColumn, destinationRow, destinationColumn, Move.TypeOfMove.J);
        stack.push(currentMove);
        boolean multiJumpFound = false;

        if (!currentMove.isCrownMove(player)) {
            char capturedPiece = tempBoard[captureRow][captureColumn];
            tempBoard[captureRow][captureColumn] = EMPTY_CELL;
            tempBoard[sourceRow][sourceColumn] = EMPTY_CELL;
            tempBoard[destinationRow][destinationColumn] = player;

            // TRY SUBSEQUENT JUMPS
            currentMove = new Move(destinationRow, destinationColumn, destinationRow + 2,
                    destinationColumn + 2, Move.TypeOfMove.J);
            if (currentMove.isMoveValid(tempBoard)) {
                char[][] newBoard = tempBoard.clone();
                getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow + 1, destinationColumn + 1,
                        destinationRow + 2, destinationColumn + 2, newBoard,
                        source + ":" + (destinationRow + 2) + "_" + (destinationColumn + 2), (Stack) stack.clone(), jumps);
                multiJumpFound = true;
            }

            currentMove = new Move(destinationRow, destinationColumn, destinationRow - 2,
                    destinationColumn - 2, Move.TypeOfMove.J);
            if (currentMove.isMoveValid(tempBoard)) {
                char[][] newBoard = tempBoard.clone();
                getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow - 1, destinationColumn - 1,
                        destinationRow - 2, destinationColumn - 2, newBoard,
                        source + ":" + (destinationRow - 2) + "_" + (destinationColumn - 2), (Stack) stack.clone(), jumps);
                multiJumpFound = true;
            }

            currentMove = new Move(destinationRow, destinationColumn, destinationRow + 2,
                    destinationColumn - 2, Move.TypeOfMove.J);
            if (currentMove.isMoveValid(tempBoard)) {
                char[][] newBoard = tempBoard.clone();
                getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow + 1, destinationColumn - 1,
                        destinationRow + 2, destinationColumn - 2, newBoard,
                        source + ":" + (destinationRow + 2) + "_" + (destinationColumn - 2), (Stack) stack.clone(), jumps);
                multiJumpFound = true;
            }

            currentMove = new Move(destinationRow, destinationColumn, destinationRow - 2,
                    destinationColumn + 2, Move.TypeOfMove.J);
            if (currentMove.isMoveValid(tempBoard)) {
                char[][] newBoard = tempBoard.clone();
                getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow - 1, destinationColumn + 1,
                        destinationRow - 2, destinationColumn + 2, newBoard,
                        source + ":" + (destinationRow - 2) + "_" + (destinationColumn + 2), (Stack) stack.clone(), jumps);
                multiJumpFound = true;
            }

            tempBoard[captureRow][captureColumn] = capturedPiece;
            tempBoard[sourceRow][sourceColumn] = player;
            tempBoard[destinationRow][destinationColumn] = EMPTY_CELL;
        }

        if (!stack.isEmpty() && !multiJumpFound) {
            Move move = new Move(Move.TypeOfMove.J);
            List<Move> allJumps = new ArrayList<>();
            while(!stack.isEmpty() && !multiJumpFound) {
                allJumps.add(0, stack.pop());
            }
            move.jumps = allJumps;
            jumps.add(move);
        }

    }

    public boolean isEngineBlack() {
        return isEngineBlack;
    }

    public static void cloneBoardArray(char[][] source, char[][] destination) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                destination[i][j] = source[i][j];
            }
        }
    }

    public int getNoOfBlackCoins() {
        return noOfBlackCoins;
    }

    public int getTotalBlackCoins() {
        return noOfBlackCoins + noOfBlackQueenCoins;
    }

    public int getTotalWhiteCoins() {
        return noOfWhiteCoins + noOfWhiteQueenCoins;
    }

    public int getNoOfBlackQueenCoins() {
        return noOfBlackQueenCoins;
    }


    public int getNoOfWhiteQueenCoins() {
        return noOfWhiteQueenCoins;
    }


    public void decrementBlackCoins() {
        noOfBlackCoins--;
    }

    public void decrementWhiteCoins() {
        noOfWhiteCoins--;
    }

    public void decrementBlackQueenCoins() {
        noOfBlackQueenCoins--;
    }

    public void decrementWhiteQueenCoins() {
        noOfWhiteQueenCoins--;
    }

    public void incrementBlackQueenCoins() {
        noOfBlackQueenCoins++;
    }

    public void incrementWhiteQueenCoins() {
        noOfWhiteQueenCoins++;
    }

    public void incrementBlackCoins() {
        noOfBlackCoins++;
    }

    public void incrementWhiteCoins() {
        noOfWhiteCoins++;
    }

    public int getNoOfWhiteCoins() {
        return noOfWhiteCoins;
    }

    public void incrementNumberOfPlies() {
        plysCompletedSoFar++;
    }

    public void updatePlyNumberOfMaterialChange() {
        this.plyNumberOfLastMaterialChange = plysCompletedSoFar;
    }

    public int getPlysCompletedSoFar() {
        return plysCompletedSoFar;
    }

    public int getPlyNumberOfLastMaterialChange() {
        return plyNumberOfLastMaterialChange;
    }
    private void printBoard(char[][] board) {
        for (int i=0; i<8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isNextMoveBlack() {
        return isNextMoveBlack;
    }

    public char getNextMoveColor() {
        return isNextMoveBlack() ? BLACK_QUEEN : WHITE_QUEEN;
    }

    public void flipNextMoveColor() {
        isNextMoveBlack = !isNextMoveBlack;
    }

    public List<Integer> getPieceData() {
        List<Integer> pieceData = new ArrayList<>();
        int blackCoins = 0;
        int whiteCoins = 0;
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                if (board[i][j] == WHITE_WOMAN) {
                    whiteCoins++;
                }
                else if(board[i][j] == BLACK_WOMAN) {
                    blackCoins++;
                }
            }
        }
        pieceData.add(blackCoins);
        pieceData.add(whiteCoins);

        blackCoins = 0;
        whiteCoins = 0;
        for (int i=4; i<8; i++) {
            for (int j=4; j<8; j++) {
                if (board[i][j] == WHITE_WOMAN) {
                    whiteCoins++;
                }
                else if(board[i][j] == BLACK_WOMAN) {
                    blackCoins++;
                }
            }
        }
        pieceData.add(blackCoins);
        pieceData.add(whiteCoins);
        return pieceData;
    }
}
