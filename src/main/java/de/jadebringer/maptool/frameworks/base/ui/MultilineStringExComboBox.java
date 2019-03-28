package de.jadebringer.maptool.frameworks.base.ui;

import com.jidesoft.combobox.PopupPanel;

//needed to change the popup for properties
public class MultilineStringExComboBox extends com.jidesoft.combobox.MultilineStringExComboBox {
  
  private static final long serialVersionUID = -4925038979207533627L;
  
  public PopupPanel createPopupComponent() {
    MultilineStringPopupPanel pp = new MultilineStringPopupPanel();
     return pp;
   }
 }