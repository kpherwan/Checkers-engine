package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Move {
    public enum TypeOfMove {
        E,J
    }

    private TypeOfMove typeOfMove;
    public int sourceRow;
    public int sourceColumn;
    public int destinationRow;
    public int destinationColumn;

    public List<Move> subsequentJumps;

    private static Map<String,String> cellToNumber;

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

    public void addJump(Move move) {
        if (subsequentJumps == null) {
            subsequentJumps = new LinkedList<>();
        }
        subsequentJumps.add(move);
    }

    public void setDestination(int destinationRow, int destinationColumn) {
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;
    }

    public String toAlphaNumericString() {
        return typeOfMove + " " + getColumnChar(sourceColumn) + getRowNum(sourceRow) + " " +
                getColumnChar(destinationColumn) + getRowNum(destinationRow) + "/n";
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

}
