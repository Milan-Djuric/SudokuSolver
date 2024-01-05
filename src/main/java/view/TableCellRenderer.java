package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {
    public TableCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (hasFocus)
            cell.setBackground(Color.ORANGE);
        else
            cell.setBackground(Color.WHITE);

        int top = 0, bottom = 0, left = 0, right = 0;

        if (column == 2 || column == 5)
            right = 2;
        else if (column == 3 || column == 6)
            left = 2;
        if (row == 2 || row == 5)
            bottom = 2;
        else if (row == 3 || row == 6)
            top = 2;

        setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

        return cell;
    }
}
