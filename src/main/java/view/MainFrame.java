package view;

import model.SudokuCell;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private SudokuBoard sudokuBoard;

    public MainFrame() throws HeadlessException {
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SudokuSolver");

        setLayout(new GridLayout(1, 1));

        sudokuBoard = new SudokuBoard();

        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setBackground(Color.WHITE);
        jPanel.add(sudokuBoard);
        add(jPanel);
    }

    public void refreshTable(SudokuCell[][] sudokuCells) {
        sudokuBoard.refreshCells(sudokuCells);
    }

    public void setSelection(int i, int j) {
        sudokuBoard.setSelection(i, j);
    }
}
