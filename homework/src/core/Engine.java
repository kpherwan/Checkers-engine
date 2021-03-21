package core;

import model.Board;
import model.Cell;
import model.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Engine {
    public Board board;
    private static final int NEG_INFINITY = Integer.MIN_VALUE;
    private static final int POS_INFINITY = Integer.MAX_VALUE;

    private static final int LOSING_SCORE = -100;
    private static final int WINNING_SCORE = 100;

    private int depthToSearchFor = 3;  //Default depth
    private int pruneCount = 0;


    public Engine(Board board, int depth) {
        this.board = board;
        this.depthToSearchFor = depth;
    }

    public Move getBestMoveWithGivenDepth() {
        List<Move> allLegalMoves = board.generateAllLegalMoves();
        Move bestMoveSoFar = null;
        double bestScoreSoFar = NEG_INFINITY;
        double currentScore;

        if (allLegalMoves.size() == 1) {
            /* if (board.isGameMode()) {
                bestMoveSoFar = allLegalMoves.get(0);
                Board newFutureBoard = getNextState(board, bestMoveSoFar);
                updateGamePlayFile(newFutureBoard);
            }*/
            return allLegalMoves.get(0);
        }

        if (allLegalMoves.size() == 0) {
            System.out.println("SON, YOU SEEM TO HAVE SCREWED UP");
            return null;
        }

        System.out.println("Prune count " + pruneCount);
        for (Move currentMove: allLegalMoves) {
            Board newFutureBoard = getNextState(board, currentMove);

            currentScore = alphaBetaSearch(newFutureBoard, 0, NEG_INFINITY, POS_INFINITY, false);

            if (currentScore > bestScoreSoFar || (currentScore == bestScoreSoFar && new Random().nextBoolean())) {
                bestScoreSoFar = currentScore;
                bestMoveSoFar = currentMove;
            }

        }
        System.out.println("Prune count " + pruneCount);
        /* if (board.isGameMode()) {
            Board newFutureBoard = getNextState(board, bestMoveSoFar);
            //updateGamePlayFile(newFutureBoard);
            System.out.println("SINGLE");
            System.out.println(newFutureBoard.isNextMoveBlack() ? "BLACK" : "WHITE");
            System.out.println(100);
            newFutureBoard.printBoardForInput();
            newFutureBoard.printTotalRemainingCoins();
        }*/

        return bestMoveSoFar;
    }

    private void updateGamePlayFile(Board newFutureBoard) {
        File myObj = new File(Board.GAMEPLAY_FILE);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file!!!!");
        }

        try {
            FileWriter fw = new FileWriter(Board.GAMEPLAY_FILE);
            fw.write(newFutureBoard.getPlysCompletedSoFar() + "\n");
            fw.write(newFutureBoard.getPlyNumberOfLastMaterialChange() + "\n");

            fw.write(newFutureBoard.getNoOfBlackCoins() + "\n");
            fw.write(newFutureBoard.getNoOfBlackQueenCoins() + "\n");
            fw.write(newFutureBoard.getNoOfWhiteCoins() + "\n");
            fw.write(newFutureBoard.getNoOfWhiteQueenCoins() + "\n");

            newFutureBoard.addToFreqStates(newFutureBoard.getBoard());
            int sizeOfMap = newFutureBoard.getFreqOfBoardStates().size();
            fw.write(sizeOfMap + "\n");

            for(Map.Entry<Integer, Integer> entry: newFutureBoard.getFreqOfBoardStates().entrySet()) {
                fw.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double alphaBetaSearch(Board futureBoard, int currentDepth, double alpha, double beta, boolean isMaximizingPlayer) {

        Integer terminalValue = getTerminalValueIfLeaf(futureBoard);
        if (terminalValue != null) {
            /* System.out.println("reached terminal value");
            futureBoard.printBoard();*/
            return terminalValue;
        }

        List<Move> allLegalMoves = futureBoard.generateAllLegalMoves();
        Integer terminalValue2 = getTerminalValueIfLeaf2(futureBoard, allLegalMoves);
        if (terminalValue2 != null) {
            /* System.out.println("reached terminal value");
            futureBoard.printBoard();*/
            return terminalValue2;
        }

        if (currentDepth == depthToSearchFor) {
            return getUtilityValue3(futureBoard);
        }

        if (isMaximizingPlayer) {
            double bestValue = NEG_INFINITY;
            for (Move currentMove: allLegalMoves) {
                Board newFutureBoard = getNextState(futureBoard, currentMove);
                double currentValue = alphaBetaSearch(newFutureBoard, currentDepth + 1, alpha, beta, false);
                bestValue = Math.max(bestValue, currentValue);
                alpha = Math.max(bestValue, alpha);

                if (alpha >= beta) {
                    pruneCount++;
                    break;
                }
            }
            return bestValue;
        }
        else {
            double worstValue = POS_INFINITY;
            for (Move currentMove: allLegalMoves) {
                Board newFutureBoard = getNextState(futureBoard, currentMove);
                double currentValue = alphaBetaSearch(newFutureBoard, currentDepth + 1, alpha, beta, true);
                worstValue = Math.min(worstValue, currentValue);
                beta = Math.min(worstValue, beta);

                if (alpha >= beta) {
                    pruneCount++;
                    break;
                }
            }
            return worstValue;
        }
    }

    /*no legal moves left*/
    private Integer getTerminalValueIfLeaf2(Board futureBoard, List<Move> allLegalMoves) {
        if (allLegalMoves.size() == 0) {
            if (futureBoard.getNextMoveColor() == futureBoard.getEngineColor()) {
                return LOSING_SCORE;
            }
            else {
                return WINNING_SCORE;
            }
        }
        return null;
    }

    private double getUtilityValue1(Board inputBoard) {
        double value;
        if (inputBoard.isNextMoveBlack()) {
            /* value = (inputBoard.getNoOfBlackCoins() - inputBoard.getNoOfWhiteCoins()) +
                    3 * (inputBoard.getNoOfBlackQueenCoins()) - inputBoard.getNoOfWhiteQueenCoins();
            value += getNumberOfHomeRowCheckers(inputBoard);*/
            value = (inputBoard.getTotalBlackCoins() - inputBoard.getTotalWhiteCoins()) +
                    0.5 * (inputBoard.getNoOfBlackQueenCoins() - inputBoard.getNoOfWhiteQueenCoins());
        }
        else {
            /* value = (inputBoard.getNoOfWhiteCoins() - inputBoard.getNoOfBlackCoins()) +
                    3 * (inputBoard.getNoOfWhiteQueenCoins()) - inputBoard.getNoOfBlackQueenCoins();
            value += getNumberOfHomeRowCheckers(inputBoard);*/
            value = (inputBoard.getTotalWhiteCoins() - inputBoard.getTotalBlackCoins()) +
                    0.5 * (inputBoard.getNoOfWhiteQueenCoins() - inputBoard.getNoOfBlackQueenCoins());
        }
        return value;
    }

    private double getUtilityValue2(Board inputBoard) {
        double value;
        if (inputBoard.isNextMoveBlack()) {
            value = (inputBoard.getNoOfBlackCoins() - inputBoard.getNoOfWhiteCoins()) +
                    3 * (inputBoard.getNoOfBlackQueenCoins()) - inputBoard.getNoOfWhiteQueenCoins();

        }
        else {
            value = (inputBoard.getNoOfWhiteCoins() - inputBoard.getNoOfBlackCoins()) +
                    3 * (inputBoard.getNoOfWhiteQueenCoins()) - inputBoard.getNoOfBlackQueenCoins();
        }
        value += getNumberOfHomeRowCheckers(inputBoard);
        return value;
    }

    private double getUtilityValue3(Board inputBoard) {
        double value = 0;
        List<Integer> noOfPieces = inputBoard.getPieceData();
        if (inputBoard.isNextMoveBlack()) {
            value += inputBoard.getNoOfBlackQueenCoins() * 20;
            value -= inputBoard.getNoOfWhiteQueenCoins() * 20;

            //black women in home territory
            value += noOfPieces.get(0) * 10;
            //white women that have invaded
            value -= noOfPieces.get(1) * 15;

            //black women that have invaded
            value += noOfPieces.get(2) * 15;
            //white women in home territory
            value -= noOfPieces.get(3) * 10;
        } else {
            value += inputBoard.getNoOfWhiteQueenCoins() * 20;
            value -= inputBoard.getNoOfBlackQueenCoins() * 20;

            //white women in home territory
            value += noOfPieces.get(3) * 10;
            //black women that have invaded
            value -= noOfPieces.get(2) * 15;

            //white women that have invaded
            value += noOfPieces.get(1) * 15;
            //black women in home territory
            value -= noOfPieces.get(0) * 10;
        }
        return value/(inputBoard.getTotalBlackCoins() + inputBoard.getTotalWhiteCoins());
    }

    private int getNumberOfHomeRowCheckers(Board inputBoard) {
        int total = 0;
        if (inputBoard.isNextMoveBlack()) {
            if (isPieceBlack(inputBoard.getBoard()[0][1])) {
                total++;
            }
            if (isPieceBlack(inputBoard.getBoard()[0][3])) {
                total++;
            }
            if (isPieceBlack(inputBoard.getBoard()[0][5])) {
                total++;
            }
            if (isPieceBlack(inputBoard.getBoard()[0][7])) {
                total++;
            }
        }
        else {
            if (isPieceWhite(inputBoard.getBoard()[7][0])) {
                total++;
            }
            if (isPieceWhite(inputBoard.getBoard()[7][2])) {
                total++;
            }
            if (isPieceWhite(inputBoard.getBoard()[7][4])) {
                total++;
            }
            if (isPieceWhite(inputBoard.getBoard()[7][6])) {
                total++;
            }
        }
        return total;
    }

    private boolean isPieceWhite(char piece) {
        return piece == Board.WHITE_WOMAN || piece == Board.WHITE_QUEEN;
    }

    public static Board getNextState(Board inputBoard, Move currentMove) {
        boolean isMaterialChange = false;
        Board newBoard = cloneBoard(inputBoard);
        char [][] newBoardArray = newBoard.getBoard();

        char piece = newBoardArray[currentMove.sourceRow][currentMove.sourceColumn];

        if (currentMove.getTypeOfMove() == Move.TypeOfMove.E) {
            if(!currentMove.isMoveValid(newBoardArray)) {
                throw new IllegalArgumentException();
            }
            newBoardArray[currentMove.sourceRow][currentMove.sourceColumn] = Board.EMPTY_CELL;
            newBoardArray[currentMove.destinationRow][currentMove.destinationColumn] = piece;

            if (currentMove.isCrownMove(piece)) {
                isMaterialChange = true;
                newBoardArray[currentMove.destinationRow][currentMove.destinationColumn] = getPromotedPiece(newBoard, piece);
            }
        }
        else {
            if (currentMove.jumps == null) {
                throw new IllegalArgumentException();
            }

            for(Move jump: currentMove.jumps) {
                piece = newBoardArray[jump.sourceRow][jump.sourceColumn];
                if(!jump.isMoveValid(newBoardArray)) {
                    throw new IllegalArgumentException();
                }
                newBoardArray[jump.sourceRow][jump.sourceColumn] = Board.EMPTY_CELL;
                newBoardArray[jump.destinationRow][jump.destinationColumn] = piece;

                Cell capturedCell = jump.getCapturedCell();
                char capturedPiece = newBoardArray[capturedCell.row][capturedCell.column];
                newBoardArray[capturedCell.row][capturedCell.column] = Board.EMPTY_CELL;

                if (capturedPiece == Board.BLACK_WOMAN) {
                    newBoard.decrementBlackCoins();
                }
                else if (capturedPiece == Board.BLACK_QUEEN) {
                    newBoard.decrementBlackQueenCoins();
                }
                else if (capturedPiece == Board.WHITE_WOMAN) {
                    newBoard.decrementWhiteCoins();
                }
                else if (capturedPiece == Board.WHITE_QUEEN) {
                    newBoard.decrementWhiteQueenCoins();
                }

                if (jump.isCrownMove(piece)) {
                    newBoardArray[jump.destinationRow][jump.destinationColumn] = getPromotedPiece(newBoard, piece);
                }
                isMaterialChange = true;
            }

        }
        newBoard.flipNextMoveColor();
        newBoard.incrementNumberOfPlies();
        if (isMaterialChange) {
            newBoard.updatePlyNumberOfMaterialChange();
        }
        return newBoard;
    }

    private static Character getPromotedPiece(Board inputBoard, char piece) {
        if (piece == Board.BLACK_WOMAN) {
            inputBoard.decrementBlackCoins();
            inputBoard.incrementBlackQueenCoins();
            return Board.BLACK_QUEEN;
        }
        if (piece == Board.WHITE_WOMAN) {
            inputBoard.decrementWhiteCoins();
            inputBoard.incrementWhiteQueenCoins();
            return Board.WHITE_QUEEN;
        }
        throw  new IllegalArgumentException();
    }

    private boolean isPieceBlack(char capturedPiece) {
        return capturedPiece == Board.BLACK_QUEEN || capturedPiece == Board.BLACK_WOMAN;
    }

    /*all pieces captured*/
    private Integer getTerminalValueIfLeaf(Board futureBoard) {

        if (futureBoard.isEngineBlack() && futureBoard.getTotalWhiteCoins() == 0) {
            return WINNING_SCORE;
        }

        if (!futureBoard.isEngineBlack() && futureBoard.getTotalBlackCoins() == 0) {
            return WINNING_SCORE;
        }

        if (futureBoard.isEngineBlack() && futureBoard.getTotalBlackCoins() == 0) {
            return LOSING_SCORE;
        }

        if (!futureBoard.isEngineBlack() && futureBoard.getTotalWhiteCoins() == 0) {
            return LOSING_SCORE;
        }

        //draw
        /* if ((futureBoard.getPlysCompletedSoFar() - futureBoard.getPlyNumberOfLastMaterialChange()) >= 100) {
            System.out.println("Condition for draw reached!!");
            return 0;
        }*/
        return null;
    }

    private static Board cloneBoard(Board inputBoard) {
        char[][] newBoardArray = new char[8][8];
        Board.cloneBoardArray(inputBoard.getBoard(), newBoardArray);
        Board newBoard = new Board(newBoardArray, inputBoard.isNextMoveBlack(), inputBoard.isEngineBlack(), inputBoard.getNoOfBlackCoins(),
                inputBoard.getNoOfBlackQueenCoins(), inputBoard.getNoOfWhiteCoins(), inputBoard.getNoOfWhiteQueenCoins(), inputBoard.getPlysCompletedSoFar(),
                inputBoard.getPlyNumberOfLastMaterialChange(), inputBoard.getFreqOfBoardStates());
        return newBoard;
    }

}
