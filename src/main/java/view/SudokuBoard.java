package view;

import model.SudokuCell;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SudokuBoard extends JTable {
    public SudokuBoard() {
        super(9, 9);

        int cellSize = 50;
        this.setBorder(new LineBorder(Color.BLACK, 3));
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
//        this.setEnabled(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setRowSelectionAllowed(false);

        TableCellRenderer tableCellRenderer = new TableCellRenderer();


        this.setPreferredSize(new Dimension(9 * cellSize, 9 * cellSize));
        this.setRowHeight(cellSize);
        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setPreferredWidth(cellSize);
            this.getColumnModel().getColumn(i).setCellRenderer(tableCellRenderer);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void refreshCells(SudokuCell[][] sudokuCells) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuCells[i][j].getValue() == 0)
                    this.setValueAt("", i, j);
                else
                    this.setValueAt(String.valueOf(sudokuCells[i][j].getValue()), i, j);
            }
        }
    }

    public void setSelection(int i, int j) {
        this.changeSelection(i, j, false, false);
    }

}
