package heuristics.value;

import heuristics.Position;
import model.Sudoku;

import java.util.List;

public interface ValueHeuristics {
    public int nextNumber(Sudoku sudoku, Position position);
}
