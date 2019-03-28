package de.jadebringer.maptool.frameworks.base.ui;

import java.util.List;

import com.jidesoft.grid.Property;
import com.jidesoft.grid.PropertyTableModel;

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