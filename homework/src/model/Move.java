package model;

import java.util.*;

public class Move {
    private enum TypeOfMove {
        E,J
    }

    private TypeOfMove typeOfMove;
    private int sourceRow;
    private int sourceColumn;
    private int destinationRow;
    private int destinationColumn;

    public String getMoveString() {
        return typeOfMove + " " + getColumnChar(sourceColumn) + getRowNum(sourceRow) + " " +
                getColumnChar(destinationColumn) + getRowNum(destinationRow) + "/n";
    }

    public char getColumnChar(int column) {
        return (char)(column + 'a');
    }

    public int getRowNum(int row) {
        return (8 - row);
    }
}
