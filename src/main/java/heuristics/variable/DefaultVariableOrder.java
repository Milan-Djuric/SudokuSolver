package heuristics.variable;

import heuristics.Position;
import model.Sudoku;

public class DefaultVariableOrder implements VariableHeuristics {
    @Override
    public Position nextPosition(Sudoku sudoku) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[i][j].getValue() == 0)
                    return new Position(i, j);


        return null;
    }
}
