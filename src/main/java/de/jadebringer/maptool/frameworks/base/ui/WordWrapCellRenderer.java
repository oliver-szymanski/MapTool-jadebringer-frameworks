package de.jadebringer.maptool.frameworks.base.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

// cell renderer for properties table
public class WordWrapCellRenderer extends RSyntaxTextArea implements TableCellRenderer {

  private static final long serialVersionUID = 300303835326338444L;

  public WordWrapCellRenderer() {
    setLineWrap(true);
    setWrapStyleWord(true);
  }

  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

    if (value == null) {
      value = "";
    }

    setText(value.toString());
    setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
    if (table.getRowHeight(row) != getPreferredSize().height) {
      table.setRowHeight(row, getPreferredSize().height);
    }

    return this;
  }
}