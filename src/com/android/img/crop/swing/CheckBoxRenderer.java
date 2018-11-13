package com.android.img.crop.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class CheckBoxRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
            return null;
        return (Component) value;
    }

}