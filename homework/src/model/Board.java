package model;

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
            board[i][1] = 'B';
            board[i][5] = 'W';
            board[i][7] = 'W';
        }

        for (int i=0;i<8;i+=2) {
            board[i][0] = 'B';
            board[i][2] = 'B';
            board[i][6] = 'W';
        }
    }

    public Board(char[][] board, boolean isNextMoveBlack) {
        this.board = board;
        this.isNextMoveBlack = isNextMoveBlack;
    }
}
