package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Board {
    public static final char EMPTY_CELL = '.';
    public static final char BLACK_WOMAN = 'b';
    public static final char WHITE_WOMAN = 'w';
    public static final char BLACK_QUEEN = 'B';
    public static final char WHITE_QUEEN = 'W';

    public char[][] getBoard() {
        return board;
    }

    private char[][] board;

    private int noOfBlackCoins;
    private int noOfWhiteCoins;
    private int noOfBlackQueenCoins;
    private int noOfWhiteQueenCoins;

    public boolean isNextMoveBlack() {
        return isNextMoveBlack;
    }

    public void flipNextMoveColor() {
        isNextMoveBlack = !isNextMoveBlack;
    }

    private boolean isNextMoveBlack;
    private int currentMoveNumber;
    private int moveNumberOfLastMaterialChange;
    private int[] movesHashArr;
    private boolean isEngineBlack;

    public Board() {
        board = new char[8][8];
        noOfBlackCoins = 12;
        noOfWhiteCoins = 12;
        isNextMoveBlack = true;    //Black opens the game

        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                board[i][j] = EMPTY_CELL;

        for (int i=1;i<8;i+=2) {
            board[i][1] = BLACK_WOMAN;
            board[i][5] = 'w';
            board[i][7] = 'w';
        }

        for (int i=0;i<8;i+=2) {
            board[i][0] = BLACK_WOMAN;
            board[i][2] = BLACK_WOMAN;
            board[i][6] = 'w';
        }
    }

    public Board(char[][] board, boolean isNextMoveBlack) {
        this.board = board;
        this.isNextMoveBlack = isNextMoveBlack;
        this.isEngineBlack = isNextMoveBlack;
        noOfBlackCoins = 12;
        noOfWhiteCoins = 12;
    }

    public void printBoard() {
        for(int i=0; i<8; i++) {
            System.out.println(Arrays.toString(board[i]));
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

        Move move = new Move(Move.TypeOfMove.J);
        List<Move> allJumps = new ArrayList<>();
        while(!stack.isEmpty() && !multiJumpFound) {
            allJumps.add(0, stack.pop());
        }
        move.jumps = allJumps;

        jumps.add(move);
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

    public int getNoOfWhiteCoins() {
        return noOfWhiteCoins;
    }

    public void incrementNumberOfMoves() {
        currentMoveNumber++;
    }

    private void printBoard(char[][] board) {
        for (int i=0; i<8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}
