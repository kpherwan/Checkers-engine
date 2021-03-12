package model;

import java.util.*;

public class Move {
    public boolean isCrownMove(char pieceMoved) {
        return (pieceMoved == 'b' && destinationRow == 7) || (pieceMoved == 'w' && destinationRow == 0);
    }

    public enum TypeOfMove {
        E,J
    }

    public TypeOfMove getTypeOfMove() {
        return typeOfMove;
    }

    private TypeOfMove typeOfMove;
    public int sourceRow;
    public int sourceColumn;
    public int destinationRow;
    public int destinationColumn;

    public List<Move> jumps;

    private static Map<String,String> cellToNumber;

    Move(TypeOfMove typeOfMove) {
        this.typeOfMove = typeOfMove;
    }

    Move(int sourceRow, int sourceColumn, int destinationRow, int destinationColumn, TypeOfMove typeOfMove) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;
        this.typeOfMove = typeOfMove;
    }

    Move(Cell source, Cell destination) {
        this.sourceRow = source.row;
        this.sourceColumn = source.column;
        this.destinationRow = destination.row;
        this.destinationColumn = destination.column;
    }

    public void setDestination(int destinationRow, int destinationColumn) {
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;
    }

    public String toAlphaNumericString() {
        return typeOfMove + " " + getColumnChar(sourceColumn) + getRowNum(sourceRow) + " " +
                getColumnChar(destinationColumn) + getRowNum(destinationRow);
    }

    public char getColumnChar(int column) {
        return (char)(column + 'a');
    }

    public int getRowNum(int row) {
        return (8 - row);
    }

    public String toNumericString() {
        return convertCellToNumberRepresentation(sourceRow, sourceColumn) + "-"
                + convertCellToNumberRepresentation(destinationRow, destinationColumn);
    }

    private String convertCellToNumberRepresentation(int row, int column) {
        if (cellToNumber == null) {
            initializeMappingOfNotation();
        }

        return cellToNumber.get(row + "_" + column);
    }

    private void initializeMappingOfNotation() {
        cellToNumber = new HashMap<>();
        cellToNumber.put("0_1","1");
        cellToNumber.put("0_3","2");
        cellToNumber.put("0_5","3");
        cellToNumber.put("0_7","4");

        cellToNumber.put("1_0","5");
        cellToNumber.put("1_2","6");
        cellToNumber.put("1_4","7");
        cellToNumber.put("1_6","8");

        cellToNumber.put("2_1","9");
        cellToNumber.put("2_3","10");
        cellToNumber.put("2_5","11");
        cellToNumber.put("2_7","12");

        cellToNumber.put("3_0","13");
        cellToNumber.put("3_2","14");
        cellToNumber.put("3_4","15");
        cellToNumber.put("3_6","16");

        cellToNumber.put("4_1","17");
        cellToNumber.put("4_3","18");
        cellToNumber.put("4_5","19");
        cellToNumber.put("4_7","20");

        cellToNumber.put("5_0","21");
        cellToNumber.put("5_2","22");
        cellToNumber.put("5_4","23");
        cellToNumber.put("5_6","24");

        cellToNumber.put("6_1","25");
        cellToNumber.put("6_3","26");
        cellToNumber.put("6_5","27");
        cellToNumber.put("6_7","28");

        cellToNumber.put("7_0","29");
        cellToNumber.put("7_2","30");
        cellToNumber.put("7_4","31");
        cellToNumber.put("7_6","32");
    }


    @Override
    public String toString() {
        return sourceRow + "_" + sourceColumn + " " + destinationRow + "_" + destinationColumn;
    }

    public boolean isMoveValid(char[][] board) {
        //move is to a square that is not empty
        char player = board[sourceRow][sourceColumn];
        if (player == '.') {
            throw new IllegalArgumentException();
        }

        //move is outside the board
        if (sourceRow < 0 || sourceRow > 7 || sourceColumn < 0 || sourceColumn > 7 ||
                destinationRow < 0 || destinationRow > 7 || destinationColumn < 0 ||
                destinationColumn > 7) {
            return false;
        }

        else if (board[destinationRow][destinationColumn] == '.') {
            //move is a simple move
            if (Math.abs(sourceColumn - destinationColumn) == 1) {
                if (player == 'w' && (sourceRow - destinationRow == 1))
                    return true;
                else if (player == 'b' && (destinationRow - sourceRow == 1))
                    return true;
                if ((player == 'W' || player == 'B') && Math.abs(sourceRow - destinationRow) == 1)
                    return true;
            }

            //move is a jump
            else if (Math.abs(sourceColumn - destinationColumn) == 2) {
                char capturedCell = board[(sourceRow + destinationRow)/2][(destinationColumn + sourceColumn)/2];
                if (player == 'w' && (sourceRow - destinationRow == 2) && (capturedCell == 'b' || capturedCell == 'B'))
                    return true;
                else if (player == 'b' && (destinationRow - sourceRow == 2) && (capturedCell == 'w' || capturedCell == 'W'))
                    return true;
                else if (player == 'B' && Math.abs(destinationRow - sourceRow) == 2 && (capturedCell == 'w' || capturedCell == 'W'))
                    return true;
                else if (player == 'W' && Math.abs(destinationRow - sourceRow) == 2 && (capturedCell == 'b' || capturedCell == 'B'))
                    return true;
            }
        }
        return false;
    }

    public Cell getCapturedCell() {
        int capturedRow = (this.sourceRow + this.destinationRow)/2;
        int capturedColumn = (this.sourceColumn + this.destinationColumn)/2;

        return new Cell(capturedRow, capturedColumn);
    }
 }
