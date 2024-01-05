package model;

import java.util.ArrayList;
import java.util.List;

public class SudokuCell {
    private int value;
    private List<Integer> options;

    public SudokuCell(int value) {
        this.value = value;
        options = new ArrayList<>();

        if (value == 0)
            for (int i = 1; i <= 9; i++)
                    options.add(i);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }
}
