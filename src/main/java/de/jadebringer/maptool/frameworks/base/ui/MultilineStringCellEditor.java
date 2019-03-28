package de.jadebringer.maptool.frameworks.base.ui;

//the cell editor for property popups
public class MultilineStringCellEditor extends com.jidesoft.grid.MultilineStringCellEditor {
  
  private static final long serialVersionUID = -5465223132750504992L;

  protected MultilineStringExComboBox createMultilineStringComboBox() {

    MultilineStringExComboBox localMultilineStringExComboBox = new MultilineStringExComboBox();
    localMultilineStringExComboBox.setEditable(true);
    return localMultilineStringExComboBox;
  }
}