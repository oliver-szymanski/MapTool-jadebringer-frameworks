package de.jadebringer.maptool.frameworks.base.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.jidesoft.combobox.PopupPanel;

//the property popup table
public class MultilineStringPopupPanel extends PopupPanel {

  private static final long serialVersionUID = 8573569156309215727L;
  
  private RSyntaxTextArea j = createTextArea();

  public MultilineStringPopupPanel() {
   this("");
  }

  public MultilineStringPopupPanel(String paramString) {
    this.setResizable(true);

    JScrollPane localJScrollPane = new RTextScrollPane(j);
    localJScrollPane.setVerticalScrollBarPolicy(22);
    localJScrollPane.setAutoscrolls(true);
    localJScrollPane.setPreferredSize(new Dimension(300, 200));
    setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
    setLayout(new BorderLayout());
    setTitle(paramString);
    add(localJScrollPane, "Center");
    setDefaultFocusComponent(j);
    j.setLineWrap(false);
    JCheckBox wrapToggle = new JCheckBox("Wrap");
    wrapToggle.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            j.setLineWrap(!j.getLineWrap());
          }
        });

    DefaultComboBoxModel<String> syntaxListModel = new DefaultComboBoxModel<>();
    syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_NONE);
    syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_JSON);
    syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
    syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_HTML);
    syntaxListModel.addElement(SyntaxConstants.SYNTAX_STYLE_XML);

    JComboBox<String> syntaxComboBox = new JComboBox<>(syntaxListModel);
    syntaxComboBox.addActionListener(

     new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
         j.setSyntaxEditingStyle(syntaxComboBox.getSelectedItem().toString());
       }
     });

    add(syntaxComboBox, BorderLayout.BEFORE_FIRST_LINE);
    add(wrapToggle, BorderLayout.AFTER_LAST_LINE);
  }

  public Object getSelectedObject() {
    return j.getText();
  }

  public void setSelectedObject(Object paramObject) {
    if (paramObject != null) {
      j.setText(paramObject.toString());
      if (PopupPanel.i == 0) {}
    } else {
      j.setText("");
    }
  }

  protected RSyntaxTextArea createTextArea() {
    RSyntaxTextArea textArea = new RSyntaxTextArea();
    textArea.setAnimateBracketMatching(true);
    textArea.setBracketMatchingEnabled(true);
    textArea.setLineWrap(false);
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
    return textArea;
  }
}