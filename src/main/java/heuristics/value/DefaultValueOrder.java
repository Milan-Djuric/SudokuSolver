package heuristics.value;

import heuristics.Position;
import heuristics.variable.VariableHeuristics;
import model.Sudoku;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultValueOrder implements ValueHeuristics {
    @Override
    public int nextNumber(Sudoku sudoku, Position position) {
        if (sudoku.getCells()[position.i][position.j].getOptions().size() == 0)
            return -1;

        return sudoku.getCells()[position.i][position.j].getOptions().get(0);
    }
}
