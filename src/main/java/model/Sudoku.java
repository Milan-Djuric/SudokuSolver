package model;

import heuristics.Position;

import java.util.HashSet;
import java.util.Set;

public class Sudoku {
    private SudokuCell[][] cells = new SudokuCell[9][9];

    public Sudoku(int[][] intValues) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                SudokuCell sudokuCell = new SudokuCell(intValues[i][j]);
                cells[i][j] = sudokuCell;
            }
        }
        //TODO options setup
    }

    public Set<Position> getCellPeers(Position cellPosition) {
        Set<Position> peers = new HashSet<>();

        int row = cellPosition.i;
        int column = cellPosition.j;

        for (int j = 0; j < 9; j++)
            peers.add(new Position(row, j));

        for (int i = 0; i < 9; i++)
            peers.add(new Position(i, column));


        int subgridRow = (row / 3) * 3;
        int subgridColumn = (column / 3) * 3;

        for (int i = subgridRow; i < subgridRow + 3; i++)
            for (int j = subgridColumn; j < subgridColumn + 3; j++)
                peers.add(new Position(i, j));

        return peers;
    }

    public boolean solved() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (cells[i][j].getValue() == 0 || !isValidGuess(cells[i][j].getValue(), i, j)) {
                    System.out.println("NOT SOLVED YET!");
                    return false;
                }

        System.out.println("SOLVED!!!");
        return true;
    }

    private boolean isNumberInRow(int number, int row, int column) {

        for (int j = 0; j < 9; j++)
            if (number == this.getCells()[row][j].getValue() && j != column)
                return true;

        return false;
    }

    private boolean isNumberInColumn(int number, int row, int column) {

        for (int i = 0; i < 9; i++)
            if (number == this.getCells()[i][column].getValue() && i != row)
                return true;

        return false;
    }

    private boolean isNumberInSubgrid(int number, int row, int column) {

        int subgridRow = (row / 3) * 3;
        int subgridColumn = (column / 3) * 3;

        for (int i = subgridRow; i < subgridRow + 3; i++)
            for (int j = subgridColumn; j < subgridColumn + 3; j++)
                if (number == this.getCells()[i][j].getValue() && i != row && j != column)
                    return true;

        return false;
    }

    private boolean isValidGuess(int number, int row, int column) {
        return (!isNumberInRow(number, row, column) && !isNumberInColumn(number, row, column) && !isNumberInSubgrid(number, row, column));
    }

    public SudokuCell[][] getCells() {
        return cells;
    }

}
