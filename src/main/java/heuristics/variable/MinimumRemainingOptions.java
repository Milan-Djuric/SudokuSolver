package heuristics.variable;

import heuristics.Position;
import model.Sudoku;

public class MinimumRemainingOptions implements VariableHeuristics {
    @Override
    public Position nextPosition(Sudoku sudoku) {
        Position p = null;
        int min = 10;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku.getCells()[i][j].getValue() == 0 && sudoku.getCells()[i][j].getOptions().size() < min) {
                    min = sudoku.getCells()[i][j].getOptions().size();
                    p = new Position(i, j);
                }
            }
        }

        return p;
    }
}
