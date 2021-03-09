package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private char[][] board;
    // b for Black Men, B for Black King, w for White Men, W for White King
    private int noOfBlackCoins;
    private int noOfWhiteCoins;
    private boolean isNextMoveBlack;
    private int currentMoveNumber;
    private int moveNumberOfLastMaterialChange;
    private int[] movesHashArr;

    public Board() {
        board = new char[8][8];
        noOfBlackCoins = 12;
        noOfWhiteCoins = 12;
        isNextMoveBlack = true;    //Black opens the game

        for (int i=0;i<8;i++)
            for (int j=0;j<8;j++)
                board[i][j] = '.';

        for (int i=1;i<8;i+=2) {
            board[i][1] = 'b';
            board[i][5] = 'w';
            board[i][7] = 'w';
        }

        for (int i=0;i<8;i+=2) {
            board[i][0] = 'b';
            board[i][2] = 'b';
            board[i][6] = 'w';
        }
    }

    public Board(char[][] board, boolean isNextMoveBlack) {
        this.board = board;
        this.isNextMoveBlack = isNextMoveBlack;
    }

    public void printBoard() {
        for(int i=0; i<8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public boolean isMoveValid(Move move) {
        //move is to a square that is not empty
        char player = board[move.sourceRow][move.sourceColumn];

        //move is outside the board
        if (move.sourceRow < 0 || move.sourceRow > 7 || move.sourceColumn < 0 || move.sourceColumn > 7 ||
                move.destinationRow < 0 || move.destinationRow > 7 || move.destinationColumn < 0 ||
                move.destinationColumn > 7) {
            return false;
        }

        else if (board[move.destinationRow][move.destinationColumn] == '.') {
            //move is a simple move
            if (Math.abs(move.sourceColumn - move.destinationColumn) == 1) {
                if (player == 'w' && (move.sourceRow - move.destinationRow == 1))
                    return true;
                else if (player == 'b' && (move.destinationRow - move.sourceRow == 1))
                    return true;
                if ((player == 'W' || player == 'B') && Math.abs(move.sourceRow - move.destinationRow) == 1)
                    return true;
            }

            //move is a jump
            else if (Math.abs(move.sourceColumn - move.destinationColumn) == 2) {
                char capturedCell = board[(move.sourceRow + move.destinationRow)/2][(move.destinationColumn + move.sourceColumn)/2];
                if (player == 'w' && (move.sourceRow - move.destinationRow == 2) && (capturedCell == 'b' || capturedCell == 'B'))
                    return true;
                else if (player == 'b' && (move.destinationRow - move.sourceRow == 2) && (capturedCell == 'w' || capturedCell == 'W'))
                    return true;
                else if (player == 'B' && Math.abs(move.destinationRow - move.sourceRow) == 2 && (capturedCell == 'w' || capturedCell == 'W'))
                    return true;
                else if (player == 'W' && Math.abs(move.destinationRow - move.sourceRow) == 2 && (capturedCell == 'b' || capturedCell == 'B'))
                    return true;
            }
        }
        return false;
    }

    public List<Move> generateAllLegalMoves() {
        char curManPlayer = isNextMoveBlack ? 'b' : 'w';
        char curKingPlayer = isNextMoveBlack ? 'B' : 'W';

        List<Move> legalMoves = new ArrayList<>();
        List<Cell> cellsOfCurrentPlayer = new ArrayList<>();
        Move currentMove;

        //get all jumps
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (board[row][column] == curManPlayer || board[row][column] == curKingPlayer) {
                    cellsOfCurrentPlayer.add(new Cell(row,column));

                    if(isMoveValid(currentMove = new Move(row,column, row + 2,column + 2, Move.TypeOfMove.J))){
                        int destinationRow = row + 2;
                        int destinationColumn = column + 2;
                        int captureRow = row + 1;
                        int captureColumn = column + 1;

                        setSubsequentJumps(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.add(currentMove);
                    }

                    if(isMoveValid(currentMove = new Move(row,column, row - 2,column - 2, Move.TypeOfMove.J))){
                        int destinationRow = row - 2;
                        int destinationColumn = column - 2;
                        int captureRow = row - 1;
                        int captureColumn = column - 1;

                        setSubsequentJumps(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.add(currentMove);
                    }

                    if(isMoveValid(currentMove = new Move(row,column, row + 2,column - 2, Move.TypeOfMove.J))){
                        int destinationRow = row + 2;
                        int destinationColumn = column - 2;
                        int captureRow = row + 1;
                        int captureColumn = column - 1;

                        setSubsequentJumps(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.add(currentMove);
                    }

                    if(isMoveValid(currentMove = new Move(row,column, row - 2,column + 2, Move.TypeOfMove.J))){
                        int destinationRow = row - 2;
                        int destinationColumn = column + 2;
                        int captureRow = row - 1;
                        int captureColumn = column + 1;

                        setSubsequentJumps(board, row, column, captureRow, captureColumn, destinationRow, destinationColumn, currentMove);
                        legalMoves.add(currentMove);
                    }
                }
            }
        }

        if (legalMoves.isEmpty()) {
            for (Cell currentCell: cellsOfCurrentPlayer) {
                if(isMoveValid(currentMove = new Move(currentCell.row, currentCell.column, currentCell.row + 1,
                        currentCell.column + 1, Move.TypeOfMove.E)))
                    legalMoves.add(currentMove);

                if(isMoveValid(currentMove = new Move(currentCell.row, currentCell.column, currentCell.row - 1,
                        currentCell.column - 1, Move.TypeOfMove.E)))
                    legalMoves.add(currentMove);

                if(isMoveValid(currentMove = new Move(currentCell.row, currentCell.column, currentCell.row + 1,
                        currentCell.column - 1, Move.TypeOfMove.E)))
                    legalMoves.add(currentMove);

                if(isMoveValid(currentMove = new Move(currentCell.row, currentCell.column, currentCell.row - 1,
                        currentCell.column + 1, Move.TypeOfMove.E)))
                    legalMoves.add(currentMove);
            }
        }

        return legalMoves;
    }

    private void setSubsequentJumps(char board[][], int row, int column, int captureRow, int captureColumn,
                                    int destinationRow, int destinationColumn, Move currentMove) {
        char[][] newBoard = new char[8][8];
        List subsequentJumps = new ArrayList<>();
        System.arraycopy(board, 0, newBoard, 0, 8);
        getAllSubsequentJumps(board[row][column], row, column, captureRow, captureColumn, destinationRow,
                destinationColumn, newBoard, destinationRow + "_" + destinationColumn, subsequentJumps);
        currentMove.subsequentJumps = convertStringToMoves(subsequentJumps);
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
                               List<String> jumps) {
        tempBoard[captureRow][captureColumn] = '.';
        tempBoard[sourceRow][sourceColumn] = '.';
        tempBoard[destinationRow][destinationColumn] = player;

        if (isMoveValid(new Move(destinationRow, destinationColumn, destinationRow + 2,
                destinationColumn + 2, Move.TypeOfMove.J))) {
            char[][] newBoard = new char[8][8];
            System.arraycopy(tempBoard, 0, newBoard, 0, 8);
            getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow + 1, destinationColumn + 1,
                    destinationRow + 2,destinationColumn + 2, newBoard,
                    source + ":" + (destinationRow + 2) + "_" + (destinationColumn + 2), jumps);
        }

        if (isMoveValid(new Move(destinationRow, destinationColumn, destinationRow - 2,
                destinationColumn - 2, Move.TypeOfMove.J))) {
            char[][] newBoard = new char[8][8];
            System.arraycopy(tempBoard, 0, newBoard, 0, 8);
            getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow - 1, destinationColumn - 1,
                    destinationRow - 2,destinationColumn - 2, newBoard,
                    source + ":" + (destinationRow - 2) + "_" + (destinationColumn - 2), jumps);
        }

        if (isMoveValid(new Move(destinationRow, destinationColumn, destinationRow + 2,
                destinationColumn - 2, Move.TypeOfMove.J))) {
            char[][] newBoard = new char[8][8];
            System.arraycopy(tempBoard, 0, newBoard, 0, 8);
            getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow + 1, destinationColumn - 1,
                    destinationRow + 2,destinationColumn - 2, newBoard,
                    source + ":" + (destinationRow + 2) + "_" + (destinationColumn - 2), jumps);
        }

        if (isMoveValid(new Move(destinationRow, destinationColumn, destinationRow - 2,
                destinationColumn + 2, Move.TypeOfMove.J))) {
            char[][] newBoard = new char[8][8];
            System.arraycopy(tempBoard, 0, newBoard, 0, 8);
            getAllSubsequentJumps(player, destinationRow, destinationColumn, destinationRow - 1, destinationColumn + 1,
                    destinationRow - 2,destinationColumn + 2, newBoard,
                    source + ":" + (destinationRow - 2) + "_" + (destinationColumn + 2), jumps);
        }

        if(source.length() > 3) {
            jumps.add(source);
        }
    }
}
