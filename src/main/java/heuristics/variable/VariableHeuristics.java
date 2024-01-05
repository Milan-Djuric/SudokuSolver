package heuristics.variable;

import heuristics.Position;
import model.Sudoku;

public interface VariableHeuristics {
    public Position nextPosition(Sudoku sudoku);
}
