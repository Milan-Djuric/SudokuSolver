package model;

import heuristics.Position;
import heuristics.value.ValueHeuristics;
import heuristics.variable.VariableHeuristics;
import utils.Utils;
import view.MainFrame;

import java.util.*;

public class SudokuSolver {
    private final Sudoku sudoku;
    private final MainFrame mainFrame;
    private final VariableHeuristics variableHeuristics;
    private final ValueHeuristics valueHeuristics;
    public int backtrackN = 0;
    private final int waitTime = 1000;

    public SudokuSolver(Sudoku sudoku, MainFrame mainFrame, VariableHeuristics variableHeuristics, ValueHeuristics valueHeuristics) {
        this.sudoku = sudoku;
        this.mainFrame = mainFrame;
        this.variableHeuristics = variableHeuristics;
        this.valueHeuristics = valueHeuristics;
        updateCellsOptions();
    }

    public void solve() {
        mainFrame.refreshTable(sudoku.getCells());

        while (obviousSingles() || lastRemainingCell() || obviousPairs() || hiddenPairs() || obviousTriples()) {
        }

        if (!sudoku.solved()) {
            System.out.println("Backtracking");
            solveBacktracking();
        } else return;

        sudoku.solved();
    }

    private boolean lastRemainingCell() {
        //value has only one position in row/column/subgrid
        boolean sudokuChanged = false;

        //values that can be placed in only one position in a row/column/subgrid
        HashMap<Integer, Integer> values = new HashMap<>();

        //subgrid
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                //reset values for each subgrid
                values = new HashMap<>();
                for (int b = 1; b <= 9; b++)
                    values.put(b, 0);

                for (int i = 3 * r; i < 3 * r + 3; i++)
                    for (int j = 3 * c; j < 3 * c + 3; j++)
                        //if value is already assigned to a position within the subgrid remove it
                        if (sudoku.getCells()[i][j].getValue() != 0) values.remove(sudoku.getCells()[i][j].getValue());

                            //else count how many positions have this value as an option
                        else for (int option : sudoku.getCells()[i][j].getOptions())
                            if (values.containsKey(option)) values.put(option, values.get(option) + 1);


                //if value appeared more than once remove it
                for (int p = 1; p <= 9; p++)
                    if (values.getOrDefault(p, 0) != 1) values.remove(p);

                //assign all values that have one appearance to their positions
                for (int i = 3 * r; i < 3 * r + 3; i++)
                    for (int j = 3 * c; j < 3 * c + 3; j++)
                        if (sudoku.getCells()[i][j].getValue() == 0) for (int value : values.keySet())
                            if (sudoku.getCells()[i][j].getOptions().contains(value)) {
                                mainFrame.setSelection(i, j);
                                System.out.println("Last Remaining Value in Subgrid at i=" + i + " j=" + j);
                                Utils.sleep(waitTime);
                                assignValue(new Position(i, j), value);
                                mainFrame.refreshTable(sudoku.getCells());
                                sudokuChanged = true;
                            }

            }
        }

        //row
        for (int r = 0; r < 9; r++) {

            //reset values for each row
            values = new HashMap<>();
            for (int b = 1; b <= 9; b++)
                values.put(b, 0);

            for (int j = 0; j < 9; j++)
                //if value is already assigned to a position within the row remove it
                if (sudoku.getCells()[r][j].getValue() != 0) values.remove(sudoku.getCells()[r][j].getValue());

                    //else count how many positions have this value as an option
                else for (int option : sudoku.getCells()[r][j].getOptions())
                    if (values.containsKey(option)) values.put(option, values.get(option) + 1);

            //if value appeared more than once remove it
            for (int p = 1; p <= 9; p++)
                if (values.getOrDefault(p, 0) != 1) values.remove(p);

            //assign all values that have one appearance to their positions
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[r][j].getValue() == 0) for (int value : values.keySet())
                    if (sudoku.getCells()[r][j].getOptions().contains(value)) {
                        mainFrame.setSelection(r, j);
                        System.out.println("Last Remaining Value in Row at i=" + r + " j=" + j);
                        Utils.sleep(waitTime);
                        assignValue(new Position(r, j), value);
                        mainFrame.refreshTable(sudoku.getCells());
                        sudokuChanged = true;
                    }
        }


        //column
        for (int c = 0; c < 9; c++) {

            //reset values for each column
            values = new HashMap<>();
            for (int b = 1; b <= 9; b++)
                values.put(b, 0);

            for (int i = 0; i < 9; i++)
                //if value is already assigned to a position within the column remove it
                if (sudoku.getCells()[i][c].getValue() != 0) values.remove(sudoku.getCells()[i][c].getValue());

                    //else count how many positions have this value as an option
                else for (int option : sudoku.getCells()[i][c].getOptions())
                    if (values.containsKey(option)) values.put(option, values.get(option) + 1);

            //if value appeared more than once remove it
            for (int p = 1; p <= 9; p++)
                if (values.getOrDefault(p, 0) != 1) values.remove(p);

            //assign all values that have one appearance to their positions
            for (int i = 0; i < 9; i++)
                if (sudoku.getCells()[i][c].getValue() == 0) for (int value : values.keySet())
                    if (sudoku.getCells()[i][c].getOptions().contains(value)) {
                        mainFrame.setSelection(i, c);
                        System.out.println("Last Remaining Value in Column at i=" + i + " j=" + c);
                        Utils.sleep(waitTime);
                        assignValue(new Position(i, c), value);
                        mainFrame.refreshTable(sudoku.getCells());
                        sudokuChanged = true;
                    }
        }

        return sudokuChanged;
    }

    private boolean obviousSingles() {
        //position only has one option left
        boolean sudokuChanged = false;

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[i][j].getValue() == 0) if (sudoku.getCells()[i][j].getOptions().size() == 1) {
                    mainFrame.setSelection(i, j);
                    System.out.println("Obvious Singles at i=" + i + " j=" + j);
                    Utils.sleep(waitTime);
                    assignValue(new Position(i, j), sudoku.getCells()[i][j].getOptions().get(0));
                    mainFrame.refreshTable(sudoku.getCells());
                    sudokuChanged = true;
                }

        return sudokuChanged;
    }

    private boolean obviousPairs() {
        //exactly two positions in subgrid/row/column have exactly two options that are same with no other options
        boolean sudokuChanged = false;

        List<Integer> optionsInArea;

        //subgrid
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                //reset options available in subgrid for each subgrid
                optionsInArea = new ArrayList<>();
                for (int b = 1; b <= 9; b++)
                    optionsInArea.add(b);

                //remove all used numbers in subgrid
                for (int i = 3 * r; i < 3 * r + 3; i++)
                    for (int j = 3 * c; j < 3 * c + 3; j++)
                        if (sudoku.getCells()[i][j].getValue() != 0)
                            optionsInArea.remove(Integer.valueOf(sudoku.getCells()[i][j].getValue()));

                for (int m = 0; m < optionsInArea.size(); m++) {
                    for (int n = m + 1; n < optionsInArea.size(); n++) {
                        int cnt = 0;

                        //counts how many empty positions in subgrid have exactly two options that are same
                        for (int i = 3 * r; i < 3 * r + 3; i++)
                            for (int j = 3 * c; j < 3 * c + 3; j++)
                                if (sudoku.getCells()[i][j].getValue() == 0)
                                    if (sudoku.getCells()[i][j].getOptions().size() == 2 && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)))
                                        cnt++;

                        //if there are exactly two of these positions remove these two options from all other positions in this subgrid
                        if (cnt == 2) {
                            System.out.println("Obvious Pairs in Subgrid r=" + r + " c=" + c + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n));
                            for (int i = 3 * r; i < 3 * r + 3; i++)
                                for (int j = 3 * c; j < 3 * c + 3; j++)
                                    if (sudoku.getCells()[i][j].getValue() == 0)
                                        if (!(sudoku.getCells()[i][j].getOptions().size() == 2 && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)))) {
                                            if (sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n))) {
                                                sudoku.getCells()[i][j].getOptions().remove(optionsInArea.get(m));
                                                sudoku.getCells()[i][j].getOptions().remove(optionsInArea.get(n));
                                                sudokuChanged = true;
                                            }
                                        }
                        }
                    }
                }

            }
        }


        //row
        for (int r = 0; r < 9; r++) {

            //reset options available in row
            optionsInArea = new ArrayList<>();
            for (int b = 1; b <= 9; b++)
                optionsInArea.add(b);

            //remove all used numbers in row
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[r][j].getValue() != 0)
                    optionsInArea.remove(Integer.valueOf(sudoku.getCells()[r][j].getValue()));

            for (int m = 0; m < optionsInArea.size(); m++) {
                for (int n = m + 1; n < optionsInArea.size(); n++) {
                    int cnt = 0;

                    //counts how many empty positions in row have exactly two options that are same
                    for (int j = 0; j < 9; j++)
                        if (sudoku.getCells()[r][j].getValue() == 0)
                            if (sudoku.getCells()[r][j].getOptions().size() == 2 && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)))
                                cnt++;

                    //if there are exactly two of these positions remove these two options from all other positions in this row
                    if (cnt == 2) {
                        System.out.println("Obvious Pairs in Row r=" + r + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n));
                        for (int j = 0; j < 9; j++)
                            if (sudoku.getCells()[r][j].getValue() == 0)
                                if (sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n))) {
                                    if (!(sudoku.getCells()[r][j].getOptions().size() == 2 && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)))) {
                                        sudoku.getCells()[r][j].getOptions().remove(optionsInArea.get(m));
                                        sudoku.getCells()[r][j].getOptions().remove(optionsInArea.get(n));
                                        sudokuChanged = true;
                                    }
                                }
                    }
                }
            }
        }

        //column
        for (int c = 0; c < 9; c++) {

            //reset options available in column
            optionsInArea = new ArrayList<>();
            for (int b = 1; b <= 9; b++)
                optionsInArea.add(b);

            //remove all used numbers in row
            for (int i = 0; i < 9; i++)
                if (sudoku.getCells()[i][c].getValue() != 0)
                    optionsInArea.remove(Integer.valueOf(sudoku.getCells()[i][c].getValue()));

            for (int m = 0; m < optionsInArea.size(); m++) {
                for (int n = m + 1; n < optionsInArea.size(); n++) {
                    int cnt = 0;

                    //counts how many empty positions in column have exactly two options that are same
                    for (int i = 0; i < 9; i++)
                        if (sudoku.getCells()[i][c].getValue() == 0)
                            if (sudoku.getCells()[i][c].getOptions().size() == 2 && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)))
                                cnt++;

                    //if there are exactly two of these positions remove these two options from all other positions in this column
                    if (cnt == 2) {
                        System.out.println("Obvious Pairs in Column c=" + c + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n));
                        for (int i = 0; i < 9; i++)
                            if (sudoku.getCells()[i][c].getValue() == 0)
                                if (!(sudoku.getCells()[i][c].getOptions().size() == 2 && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)))) {
                                    if (sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n))) {
                                        sudoku.getCells()[i][c].getOptions().remove(optionsInArea.get(m));
                                        sudoku.getCells()[i][c].getOptions().remove(optionsInArea.get(n));
                                        sudokuChanged = true;
                                    }
                                }
                    }
                }
            }
        }

        return sudokuChanged;
    }

    private boolean hiddenPairs() {
        //exactly two positions in subgrid/row/column have two options that are same
        boolean sudokuChanged = false;

        List<Integer> optionsInArea;

        //subgrid
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                //reset options available in subgrid for each subgrid
                optionsInArea = new ArrayList<>();
                for (int b = 1; b <= 9; b++)
                    optionsInArea.add(b);

                //remove all used numbers in subgrid
                for (int i = 3 * r; i < 3 * r + 3; i++)
                    for (int j = 3 * c; j < 3 * c + 3; j++)
                        if (sudoku.getCells()[i][j].getValue() != 0)
                            optionsInArea.remove(Integer.valueOf(sudoku.getCells()[i][j].getValue()));

                for (int m = 0; m < optionsInArea.size(); m++) {
                    for (int n = m + 1; n < optionsInArea.size(); n++) {
                        int cnt = 0;

                        //counts how many empty positions in subgrid have two options that are same
                        for (int i = 3 * r; i < 3 * r + 3; i++)
                            for (int j = 3 * c; j < 3 * c + 3; j++)
                                if (sudoku.getCells()[i][j].getValue() == 0)
                                    if (sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)))
                                        cnt++;
                                    else if (sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)))
                                        cnt = Integer.MIN_VALUE;

                        //if there are exactly two of these positions remove all other options from these positions
                        if (cnt == 2) {
                            System.out.println("Hidden Pairs in Subgrid r=" + r + " c=" + c + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n));
                            for (int i = 3 * r; i < 3 * r + 3; i++)
                                for (int j = 3 * c; j < 3 * c + 3; j++)
                                    if (sudoku.getCells()[i][j].getValue() == 0) {
                                        if (sudoku.getCells()[i][j].getOptions().size() != 2 && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n))) {
                                            List<Integer> newOptions = new ArrayList<>();
                                            newOptions.add(optionsInArea.get(m));
                                            newOptions.add(optionsInArea.get(n));
                                            sudoku.getCells()[i][j].setOptions(newOptions);
                                            sudokuChanged = true;
                                        }
                                    }
                        }
                    }
                }

            }
        }


        //row
        for (int r = 0; r < 9; r++) {

            //reset options available in row
            optionsInArea = new ArrayList<>();
            for (int b = 1; b <= 9; b++)
                optionsInArea.add(b);

            //remove all used numbers in row
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[r][j].getValue() != 0)
                    optionsInArea.remove(Integer.valueOf(sudoku.getCells()[r][j].getValue()));

            for (int m = 0; m < optionsInArea.size(); m++) {
                for (int n = m + 1; n < optionsInArea.size(); n++) {
                    int cnt = 0;

                    //counts how many empty positions in row have two options that are same
                    for (int j = 0; j < 9; j++)
                        if (sudoku.getCells()[r][j].getValue() == 0)
                            if (sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)))
                                cnt++;
                            else if (sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)))
                                cnt = Integer.MIN_VALUE;

                    //if there are exactly two of these positions remove all other options from these positions
                    if (cnt == 2) {
                        System.out.println("Hidden Pairs in Row r=" + r + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n));
                        for (int j = 0; j < 9; j++)
                            if (sudoku.getCells()[r][j].getValue() == 0)
                                if (sudoku.getCells()[r][j].getOptions().size() != 2 && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n))) {
                                    List<Integer> newOptions = new ArrayList<>();
                                    newOptions.add(Integer.valueOf(optionsInArea.get(m)));
                                    newOptions.add(Integer.valueOf(optionsInArea.get(n)));
                                    sudoku.getCells()[r][j].setOptions(newOptions);
                                    sudokuChanged = true;
                                }

                    }
                }
            }
        }

        //column
        for (int c = 0; c < 9; c++) {

            //reset options available in column
            optionsInArea = new ArrayList<>();
            for (int b = 1; b <= 9; b++)
                optionsInArea.add(b);

            //remove all used numbers in row
            for (int i = 0; i < 9; i++)
                if (sudoku.getCells()[i][c].getValue() != 0)
                    optionsInArea.remove(Integer.valueOf(sudoku.getCells()[i][c].getValue()));

            for (int m = 0; m < optionsInArea.size(); m++) {
                for (int n = m + 1; n < optionsInArea.size(); n++) {
                    int cnt = 0;

                    //counts how many empty positions in column have two options that are same
                    for (int i = 0; i < 9; i++)
                        if (sudoku.getCells()[i][c].getValue() == 0)
                            if (sudoku.getCells()[i][c].getOptions().size() == 2 && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)))
                                cnt++;
                            else if (sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)))
                                cnt = Integer.MIN_VALUE;

                    //if there are exactly two of these positions remove all other options from these positions
                    if (cnt == 2) {
                        System.out.println("Obvious Pairs in Column c=" + c + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n));
                        for (int i = 0; i < 9; i++)
                            if (sudoku.getCells()[i][c].getValue() == 0)
                                if (sudoku.getCells()[i][c].getOptions().size() != 2 && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n))) {
                                    List<Integer> newOptions = new ArrayList<>();
                                    newOptions.add(Integer.valueOf(optionsInArea.get(m)));
                                    newOptions.add(Integer.valueOf(optionsInArea.get(n)));
                                    sudoku.getCells()[i][c].setOptions(newOptions);
                                    sudokuChanged = true;
                                }

                    }
                }
            }
        }

//        if (sudokuChanged) {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//        }
        return sudokuChanged;
    }

    private boolean obviousTriples() {
        //exactly three positions in subgrid/row/column have exactly three options that are same
        boolean sudokuChanged = false;

        List<Integer> optionsInArea;

        //subgrid
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                //reset options available in subgrid for each subgrid
                optionsInArea = new ArrayList<>();
                for (int b = 1; b <= 9; b++)
                    optionsInArea.add(b);

                //remove all used numbers in subgrid
                for (int i = 3 * r; i < 3 * r + 3; i++)
                    for (int j = 3 * c; j < 3 * c + 3; j++)
                        if (sudoku.getCells()[i][j].getValue() != 0)
                            optionsInArea.remove(Integer.valueOf(sudoku.getCells()[i][j].getValue()));

                for (int m = 0; m < optionsInArea.size(); m++) {
                    for (int n = m + 1; n < optionsInArea.size(); n++) {
                        for (int o = n + 1; o < optionsInArea.size(); o++) {
                            int cnt = 0;

                            //counts how many empty positions in subgrid have exactly three options that are same
                            for (int i = 3 * r; i < 3 * r + 3; i++)
                                for (int j = 3 * c; j < 3 * c + 3; j++)
                                    if (sudoku.getCells()[i][j].getValue() == 0)
                                        if (sudoku.getCells()[i][j].getOptions().size() == 3 && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(o)))
                                            cnt++;

                            //if there are exactly three of these positions remove these three options from all other positions in this subgrid
                            if (cnt == 3) {
                                System.out.println("Obvious Triples in Subgrid r=" + r + " c=" + c + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n) + ", " + optionsInArea.get(o));
                                for (int i = 3 * r; i < 3 * r + 3; i++)
                                    for (int j = 3 * c; j < 3 * c + 3; j++)
                                        if (sudoku.getCells()[i][j].getValue() == 0)
                                            if (!(sudoku.getCells()[i][j].getOptions().size() == 3 && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)) && sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(o)))) {
                                                if (sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(n)) || sudoku.getCells()[i][j].getOptions().contains(optionsInArea.get(o))) {
                                                    sudoku.getCells()[i][j].getOptions().remove(optionsInArea.get(m));
                                                    sudoku.getCells()[i][j].getOptions().remove(optionsInArea.get(n));
                                                    sudoku.getCells()[i][j].getOptions().remove(optionsInArea.get(o));
                                                    sudokuChanged = true;
                                                }
                                            }
                            }
                        }
                    }
                }

            }
        }

        //row
        for (int r = 0; r < 9; r++) {

            //reset options available in row
            optionsInArea = new ArrayList<>();
            for (int b = 1; b <= 9; b++)
                optionsInArea.add(b);

            //remove all used numbers in row
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[r][j].getValue() != 0)
                    optionsInArea.remove(Integer.valueOf(sudoku.getCells()[r][j].getValue()));

            for (int m = 0; m < optionsInArea.size(); m++) {
                for (int n = m + 1; n < optionsInArea.size(); n++) {
                    for (int o = n + 1; o < optionsInArea.size(); o++) {
                        int cnt = 0;

                        //counts how many empty positions in row have exactly three options that are same
                        for (int j = 0; j < 9; j++)
                            if (sudoku.getCells()[r][j].getValue() == 0)
                                if (sudoku.getCells()[r][j].getOptions().size() == 3 && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(o)))
                                    cnt++;

                        //if there are exactly three of these positions remove these options from all other positions in this row
                        if (cnt == 3) {
                            System.out.println("Obvious Triples in Row r=" + r + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n) + ", " + optionsInArea.get(o));
                            for (int j = 0; j < 9; j++)
                                if (sudoku.getCells()[r][j].getValue() == 0)
                                    if (!(sudoku.getCells()[r][j].getOptions().size() == 3 && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)) && sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(o)))) {
                                        if (sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(n)) || sudoku.getCells()[r][j].getOptions().contains(optionsInArea.get(o))) {
                                            sudoku.getCells()[r][j].getOptions().remove(optionsInArea.get(m));
                                            sudoku.getCells()[r][j].getOptions().remove(optionsInArea.get(n));
                                            sudoku.getCells()[r][j].getOptions().remove(optionsInArea.get(o));
                                            sudokuChanged = true;
                                        }
                                    }
                        }
                    }
                }
            }
        }

        //column
        for (int c = 0; c < 9; c++) {

            //reset options available in column
            optionsInArea = new ArrayList<>();
            for (int b = 1; b <= 9; b++)
                optionsInArea.add(b);

            //remove all used numbers in row
            for (int i = 0; i < 9; i++)
                if (sudoku.getCells()[i][c].getValue() != 0)
                    optionsInArea.remove(Integer.valueOf(sudoku.getCells()[i][c].getValue()));

            for (int m = 0; m < optionsInArea.size(); m++) {
                for (int n = m + 1; n < optionsInArea.size(); n++) {
                    for (int o = n + 1; o < optionsInArea.size(); o++) {
                        int cnt = 0;

                        //counts how many empty positions in row have exactly three options that are same
                        for (int i = 0; i < 9; i++)
                            if (sudoku.getCells()[i][c].getValue() == 0)
                                if (sudoku.getCells()[i][c].getOptions().size() == 3 && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(o)))
                                    cnt++;

                        //if there are exactly three of these positions remove these options from all other positions in this row
                        if (cnt == 3) {
                            System.out.println("Obvious Triples in Column c=" + c + " for values: " + optionsInArea.get(m) + ", " + optionsInArea.get(n) + ", " + optionsInArea.get(o));
                            for (int i = 0; i < 9; i++)
                                if (sudoku.getCells()[i][c].getValue() == 0)
                                    if (!(sudoku.getCells()[i][c].getOptions().size() == 3 && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)) && sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(o)))) {
                                        if (sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(m)) || sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(n)) || sudoku.getCells()[i][c].getOptions().contains(optionsInArea.get(o))) {
                                            sudoku.getCells()[i][c].getOptions().remove(optionsInArea.get(m));
                                            sudoku.getCells()[i][c].getOptions().remove(optionsInArea.get(n));
                                            sudoku.getCells()[i][c].getOptions().remove(optionsInArea.get(o));
                                            sudokuChanged = true;
                                        }
                                    }
                        }
                    }
                }
            }
        }

        return sudokuChanged;
    }

    public void assignValue(Position position, int number) {
        int row = position.i;
        int column = position.j;

        sudoku.getCells()[row][column].setValue(number);

        if (number != 0) {
            Set<Position> peerPostions = sudoku.getCellPeers(position);

            for (Position peerPosition : peerPostions)
                if (sudoku.getCells()[peerPosition.i][peerPosition.j].getValue() == 0)
                    sudoku.getCells()[peerPosition.i][peerPosition.j].getOptions().remove(Integer.valueOf(number));
        }
    }

    public boolean solveBacktracking() {
        Position nextPosition = variableHeuristics.nextPosition(sudoku);
        if (nextPosition != null) {
            mainFrame.setSelection(nextPosition.i, nextPosition.j);

            int num = valueHeuristics.nextNumber(sudoku, nextPosition);
            while (num != -1) {
                sudoku.getCells()[nextPosition.i][nextPosition.j].setValue(num);
                updateCellsOptions();

                if (!forwardCheck(nextPosition)) {
                    backtrackN++;
                    sudoku.getCells()[nextPosition.i][nextPosition.j].setValue(0);
                    sudoku.getCells()[nextPosition.i][nextPosition.j].getOptions().remove(0);
                    num = valueHeuristics.nextNumber(sudoku, nextPosition);
                    continue;
                }

                Utils.sleep(waitTime);

                mainFrame.refreshTable(sudoku.getCells());

                if (solveBacktracking()) return true;
                else {
                    backtrackN++;
                    sudoku.getCells()[nextPosition.i][nextPosition.j].setValue(0);
                    sudoku.getCells()[nextPosition.i][nextPosition.j].getOptions().remove(0);
                }

                num = valueHeuristics.nextNumber(sudoku, nextPosition);
            }
            return false;
        }
        return true;
    }

    private boolean forwardCheck(Position editedCellPosition) {
        Set<Position> peers = sudoku.getCellPeers(editedCellPosition);

        for (Position peerPosition : peers) {
            if (sudoku.getCells()[peerPosition.i][peerPosition.j].getValue() == 0)
                if (sudoku.getCells()[peerPosition.i][peerPosition.j].getOptions().size() == 0) return false;
        }

        return true;
    }

    public void updateCellsOptions() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (sudoku.getCells()[i][j].getValue() == 0) {
                    List<Integer> options = new ArrayList<>();
                    for (int k = 1; k <= 9; k++)
                        options.add(k);
                    sudoku.getCells()[i][j].setOptions(options);
                }


        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                int number = sudoku.getCells()[row][column].getValue();

                if (number != 0) {
                    for (int j = 0; j < 9; j++)
                        if (sudoku.getCells()[row][j].getValue() == 0)
                            sudoku.getCells()[row][j].getOptions().remove(Integer.valueOf(number));

                    for (int i = 0; i < 9; i++)
                        if (sudoku.getCells()[i][column].getValue() == 0)
                            sudoku.getCells()[i][column].getOptions().remove(Integer.valueOf(number));


                    int subgridRow = (row / 3) * 3;
                    int subgridColumn = (column / 3) * 3;

                    for (int i = subgridRow; i < subgridRow + 3; i++)
                        for (int j = subgridColumn; j < subgridColumn + 3; j++)
                            if (sudoku.getCells()[i][j].getValue() == 0)
                                sudoku.getCells()[i][j].getOptions().remove(Integer.valueOf(number));
                }
            }
        }

    }

}
