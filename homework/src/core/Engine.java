package core;

import model.Board;
import model.Cell;
import model.Move;

import java.util.List;

public class Engine {
    public Board board;
    private static final int NEG_INFINITY = Integer.MIN_VALUE;
    private static final int POS_INFINITY = Integer.MAX_VALUE;

    private int depthToSearchFor = 3;  //Default depth
    private int pruneCount = 0;

    public Engine(Board board, int depth) {
        this.board = board;
        this.depthToSearchFor = depth;
    }

    public Move getBestMoveWithGivenDepth() {
        List<Move> allLegalMoves = board.generateAllLegalMoves();
        Move bestMoveSoFar = null;
        int bestScoreSoFar = NEG_INFINITY;
        int currentScore;

        if (allLegalMoves.size() == 1) {
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
            if (currentScore > bestScoreSoFar) {
                bestScoreSoFar = currentScore;
                bestMoveSoFar = currentMove;
            }
        }
        System.out.println("Prune count " + pruneCount);
        return bestMoveSoFar;
    }

    private int alphaBetaSearch(Board futureBoard, int currentDepth, int alpha, int beta, boolean isMaximizingPlayer) {
        Integer terminalValue = getTerminalValueIfLeaf(futureBoard);
        if (terminalValue != null) {
            return terminalValue;
        }
        
        if (currentDepth == depthToSearchFor) {
            return getUtilityValue(futureBoard);
        }

        if (isMaximizingPlayer) {
            int bestValue = NEG_INFINITY;
            List<Move> allLegalMoves = futureBoard.generateAllLegalMoves();
            for (Move currentMove: allLegalMoves) {
                Board newFutureBoard = getNextState(futureBoard, currentMove);
                int currentValue = alphaBetaSearch(newFutureBoard, currentDepth + 1, alpha, beta, false);
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
            int bestValue = POS_INFINITY;
            List<Move> allLegalMoves = futureBoard.generateAllLegalMoves();
            for (Move currentMove: allLegalMoves) {
                Board newFutureBoard = getNextState(futureBoard, currentMove);
                int currentValue = alphaBetaSearch(newFutureBoard, currentDepth + 1, alpha, beta, true);
                bestValue = Math.min(bestValue, currentValue);
                beta = Math.min(bestValue, beta);

                if (alpha >= beta) {
                    pruneCount++;
                    break;
                }
            }
            return bestValue;
        }
    }

    private int getUtilityValue(Board inputBoard) {
        int value;
        if (inputBoard.isNextMoveBlack()) {
            value = (inputBoard.getNoOfBlackCoins() - inputBoard.getNoOfWhiteCoins()) +
                    2 * (inputBoard.getNoOfBlackQueenCoins() - inputBoard.getNoOfWhiteQueenCoins());
        }
        else {
            value = (inputBoard.getNoOfWhiteCoins() - inputBoard.getNoOfBlackCoins()) +
                    2 * (inputBoard.getNoOfWhiteQueenCoins() - inputBoard.getNoOfBlackQueenCoins());
        }
        return value;
    }

    private Board getNextState(Board inputBoard, Move currentMove) {
        Board newBoard = cloneBoard(inputBoard);
        newBoard.incrementNumberOfMoves();
        char [][] newBoardArray = newBoard.getBoard();

        char piece = newBoardArray[currentMove.sourceRow][currentMove.sourceColumn];

        if (currentMove.getTypeOfMove() == Move.TypeOfMove.E) {
            if(!currentMove.isMoveValid(newBoardArray)) {
                throw new IllegalArgumentException();
            }
            newBoardArray[currentMove.sourceRow][currentMove.sourceColumn] = Board.EMPTY_CELL;
            newBoardArray[currentMove.destinationRow][currentMove.destinationColumn] = piece;

            if (currentMove.isCrownMove(piece)) {
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
            }
        }
        newBoard.flipNextMoveColor();
        return newBoard;
    }

    private Character getPromotedPiece(Board inputBoard, char piece) {
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

    private Integer getTerminalValueIfLeaf(Board futureBoard) {
        if (futureBoard.isEngineBlack() && futureBoard.getNoOfWhiteCoins() == 0) {
            return POS_INFINITY;
        }

        if (!futureBoard.isEngineBlack() && futureBoard.getNoOfBlackCoins() == 0) {
            return POS_INFINITY;
        }

        if (futureBoard.isEngineBlack() && futureBoard.getNoOfBlackCoins() == 0) {
            return NEG_INFINITY;
        }

        if (!futureBoard.isEngineBlack() && futureBoard.getNoOfWhiteCoins() == 0) {
            return NEG_INFINITY;
        }
        return null;
    }

    private Board cloneBoard(Board inputBoard) {
        char[][] newBoardArray = new char[8][8];
        Board.cloneBoardArray(inputBoard.getBoard(), newBoardArray);
        Board newBoard = new Board(newBoardArray, inputBoard.isNextMoveBlack());
        return newBoard;
    }
}
