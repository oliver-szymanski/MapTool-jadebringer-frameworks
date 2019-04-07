/*
 * This software is copyright by the Jadebringer.de development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool-jadebringer-framework Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.ui;

import com.jidesoft.grid.Property;
import com.jidesoft.grid.PropertyTableModel;
import java.util.List;

public class PropertiesTableModel extends PropertyTableModel<PropertiesTableModel.PropertyModel> {
  private static final long serialVersionUID = 2822797264738675580L;

  public PropertiesTableModel(List<PropertiesTableModel.PropertyModel> model) {
    super(model);
  }

  public static class PropertyModel extends Property {
    private static final long serialVersionUID = 4129033551005743554L;

    private Object value;
    private boolean dirty = false;

    public PropertyModel(String key, Object value) {
      super(key, key, String.class);
      if (key != null && key.contains(".")) {
        setCategory(key.substring(0, key.lastIndexOf(".")));
      }
      this.value = value;
      setCellEditor(new MultilineStringCellEditor());
    }

    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public void setValue(Object value) {
      this.value = value;
      this.dirty = true;
    }

    @Override
    public boolean hasValue() {
      return value != null;
    }

    public boolean isDirty() {
      return dirty;
    }

    public void setDirty(boolean dirty) {
      this.dirty = dirty;
    }
  }
}
