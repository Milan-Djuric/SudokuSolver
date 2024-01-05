package heuristics.value;

import heuristics.Position;
import model.Sudoku;

import java.util.HashSet;
import java.util.Set;

public class LeastConstraintValue implements ValueHeuristics {
    @Override
    public int nextNumber(Sudoku sudoku, Position position) {

        if (sudoku.getCells()[position.i][position.j].getOptions().size() == 0)
            return -1;

        Set<Position> peers = sudoku.getCellPeers(position);

        int min = Integer.MAX_VALUE;
        int bestOption = 0;

        for (int option : sudoku.getCells()[position.i][position.j].getOptions()) {
            int cnt = 0;

            for (Position peerPosition : peers)
                if (sudoku.getCells()[peerPosition.i][peerPosition.j].getValue() == 0)
                    if (sudoku.getCells()[peerPosition.i][peerPosition.j].getOptions().contains(option))
                        cnt++;

            if (cnt < min) {
                min = cnt;
                bestOption = option;
            }
        }

        sudoku.getCells()[position.i][position.j].getOptions().remove(Integer.valueOf(bestOption));
        sudoku.getCells()[position.i][position.j].getOptions().add(0, bestOption);

        return sudoku.getCells()[position.i][position.j].getOptions().get(0);
    }
}
