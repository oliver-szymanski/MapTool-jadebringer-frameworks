/*
 * This software is copyright by the Jadebringer.de development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool-jadebringer-extension Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.extension.base.ui;

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
