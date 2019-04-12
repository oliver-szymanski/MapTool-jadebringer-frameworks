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
package de.jadebringer.maptool.extension.hook.ui;

import de.jadebringer.maptool.extension.hook.ExtensionFunctionButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import net.rptools.lib.MD5Key;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.util.ImageManager;

public class TranslucentFrame {
  private TranslucentFrame rootFrame;
  private JFrame actualFrame;
  private JPanel contentContainer;
  private JTabbedPane tabbedPane;
  private JPanel title;
  private JPanel subTab;
  private String frameName;
  private String group;
  private List<TranslucentFrame> subFrames = new LinkedList<>();
  private String prefixedFrameName;
  private boolean minimized = false;
  private Map<ExtensionFunctionButton, JButton> functionButtonsMap = new HashMap<>();
  private String prefixedFrameId;

  public TranslucentFrame(String frameName, String prefixedFrameName, String prefixedFrameId) {
    this.frameName = frameName;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    initRootFrame();
  }

  public TranslucentFrame(
      String frameName,
      String prefixedFrameName,
      String prefixedFrameId,
      Component componentForLabel,
      Component... components) {
    this.frameName = frameName;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    initRootFrame();
    initWithComponents(componentForLabel, components);
  }

  public TranslucentFrame(
      String frameName,
      String prefixedFrameName,
      String prefixedFrameId,
      String group,
      TranslucentFrame root) {
    this.frameName = frameName;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    this.group = group;
    this.rootFrame = root;
    root.subFrames.add(this);
    initSubTab();
  }

  private void initWithComponents(Component componentForLabel, Component... components) {
    JPanel container = new JPanel(new BorderLayout());

    for (Component component : components) {
      container.add(component);
      component.setVisible(true);
      component.invalidate();
    }
    if (componentForLabel != null) {
      title.add(componentForLabel);
    }

    contentContainer.remove(tabbedPane);
    contentContainer.add(container);
  }

  private void initRootFrame() {
    actualFrame = new JFrame(frameName);
    actualFrame.setLayout(new BorderLayout());
    // actualFrame.setLayout(new GridLayout(30,30,30,30));
    actualFrame.setSize(300, 200);
    loadPreferences(false);

    actualFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    actualFrame.setUndecorated(true);
    actualFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    actualFrame.setOpacity(0.65f);
    actualFrame.setAlwaysOnTop(true);
    JLabel label = new JLabel(prefixedFrameName);
    label.setFont(label.getFont().deriveFont(14f));
    label.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2 && !event.isConsumed()) {
              event.consume();
              setMinimized(!isMinimized());
            }
          }
        });
    JLabel pack = new JLabel("# ");
    pack.setFont(pack.getFont().deriveFont(12f));
    pack.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent event) {
            actualFrame.pack();
          }
        });
    JLabel close = new JLabel(" x");
    close.setFont(close.getFont().deriveFont(14f));
    close.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent event) {
            close();
          }
        });
    JLabel minimize = new JLabel(" -");
    minimize.setFont(minimize.getFont().deriveFont(14f));
    minimize.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent event) {
            actualFrame.setState(JFrame.ICONIFIED);
          }
        });
    // label.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
    title = new JPanel();
    title.add(pack);
    title.add(label);
    title.add(minimize);
    title.add(close);
    actualFrame.add(title, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
    tabbedPane.setUI(
        new BasicTabbedPaneUI() {
          @Override
          protected void installDefaults() {
            super.installDefaults();
            highlight = Color.cyan;
            lightHighlight = Color.lightGray;
            shadow = Color.darkGray;
            darkShadow = Color.black;
            focus = Color.lightGray;
          }
        });

    contentContainer =
        new JPanel() {
          private static final long serialVersionUID = 1L;

          @Override
          public Insets getInsets() {
            return new Insets(10, 10, 10, 10);
          }
        };
    contentContainer.setLayout(new GridLayout());
    contentContainer.add(tabbedPane);
    actualFrame.add(contentContainer);
    if (minimized) {
      contentContainer.setVisible(false);
      actualFrame.pack();
    }
    actualFrame.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent we) {
            savePreferences();
          }
        });

    FrameDragListener frameDragListener = new FrameDragListener(actualFrame);
    actualFrame.addMouseListener(frameDragListener);
    actualFrame.addMouseMotionListener(frameDragListener);
    label.addMouseListener(frameDragListener);
    label.addMouseMotionListener(frameDragListener);

    ComponentResizer cr = new ComponentResizer();
    cr.setSnapSize(new Dimension(1, 1));
    cr.registerComponent(actualFrame);
    cr.setFrameDragListener(frameDragListener);
    cr.setResizeThis(actualFrame);
  }

  private void loadPreferences(boolean loadSizeOnly) {
    Integer x =
        PreferenceManager.loadPreference(
            actualFrame.getLocation().x,
            "FrameworkFunctions",
            "ButtonFrame",
            prefixedFrameId,
            group,
            "x");
    Integer y =
        PreferenceManager.loadPreference(
            actualFrame.getLocation().y,
            "FrameworkFunctions",
            "ButtonFrame",
            prefixedFrameId,
            group,
            "y");
    Integer width =
        PreferenceManager.loadPreference(
            actualFrame.getWidth(),
            "FrameworkFunctions",
            "ButtonFrame",
            prefixedFrameId,
            group,
            "width");
    Integer height =
        PreferenceManager.loadPreference(
            actualFrame.getHeight(),
            "FrameworkFunctions",
            "ButtonFrame",
            prefixedFrameId,
            group,
            "height");
    boolean minimized =
        PreferenceManager.loadPreference(
            this.minimized,
            "FrameworkFunctions",
            "ButtonFrame",
            prefixedFrameId,
            group,
            "minimized");

    if (!loadSizeOnly) {
      this.minimized = minimized;
    }
    if (x != null && y != null && width != null && height != null) {
      if (!loadSizeOnly) {
        actualFrame.setLocation(x, y);
      }
      actualFrame.setSize(width, height);
    } else {
      actualFrame.setLocationRelativeTo(null);
    }
  }

  private void initSubTab() {
    this.subTab = new JPanel(new WrapLayout());
    JScrollPane scrollPane = new JScrollPane(this.subTab);
    rootFrame.tabbedPane.addTab(this.group, scrollPane);
  }

  protected JComponent makeTextPanel(String text) {
    JPanel panel = new JPanel(false);
    JLabel filler = new JLabel(text);
    filler.setHorizontalAlignment(JLabel.CENTER);
    panel.setLayout(new GridLayout(1, 1));
    panel.add(filler);
    return panel;
  }

  public void addComponentListener(ComponentListener listener) {
    contentContainer.addComponentListener(listener);
  }

  public void removeComponentListener(ComponentListener listener) {
    contentContainer.removeComponentListener(listener);
  }

  private void savePreferences() {
    PreferenceManager.savePreference(
        actualFrame.getLocation().x,
        "FrameworkFunctions",
        "ButtonFrame",
        prefixedFrameId,
        group,
        "x");
    PreferenceManager.savePreference(
        actualFrame.getLocation().y,
        "FrameworkFunctions",
        "ButtonFrame",
        prefixedFrameId,
        group,
        "y");
    if (!minimized) {
      PreferenceManager.savePreference(
          actualFrame.getWidth(),
          "FrameworkFunctions",
          "ButtonFrame",
          prefixedFrameId,
          group,
          "width");
      PreferenceManager.savePreference(
          actualFrame.getHeight(),
          "FrameworkFunctions",
          "ButtonFrame",
          prefixedFrameId,
          group,
          "height");
    }
    PreferenceManager.savePreference(
        minimized, "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "minimized");
  }

  public void hide() {
    savePreferences();
    actualFrame.setVisible(false);
  }

  public void close() {
    hide();
    actualFrame.dispose();
  }

  public void show() {
    if (actualFrame == null && this.group == null) {
      initRootFrame();
    }

    for (TranslucentFrame subFrame : subFrames) {
      if (subFrame.subTab == null) {
        subFrame.initSubTab();
      }
    }

    if (actualFrame != null && !actualFrame.isVisible()) {
      // only pack the frame if it size does not come from saved preferences
      if (null
          == PreferenceManager.loadPreference(
              (String) null, "FrameworkFunctions", "ButtonFrame", frameName, group, "x")) {
        actualFrame.pack();
      }

      actualFrame.setVisible(true); // this opens the dialog
    }
  }

  public void remove(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    functionButtonsMap.remove(functionButton);

    if (jButton != null) {
      Container parent = jButton.getParent();
      parent.remove(jButton);
      if (this.group == null) {
        actualFrame.invalidate();
        actualFrame.repaint();
      } else {
        subTab.invalidate();
        subTab.repaint();
      }
    }
  }

  public void update(ExtensionFunctionButton functionButton) {
    JButton button = functionButtonsMap.get(functionButton);
    ImageIcon icon = null;

    if (functionButton.getImageFile() != null && functionButton.getImageFile().length() > 0) {
      icon = createImageIcon(functionButton);
    }

    if (functionButton.isTextAndImage() || icon == null) {
      button.setText(functionButton.getText());
    }
    button.setToolTipText(functionButton.getTooltip());
    button.setFont(new Font("Serif", Font.PLAIN, 16));
    button.setBackground(new Color(50, 50, 50)); // import java.awt.Color;
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    if (icon != null) {
      button.setIcon(icon);
    }
  }

  public void enable(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    jButton.setEnabled(true);
  }

  public void disable(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    jButton.setEnabled(false);
  }

  public boolean isEnabled(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    return jButton.isEnabled();
  }

  public void hide(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    jButton.setVisible(false);
  }

  public void show(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    jButton.setVisible(true);
  }

  public boolean isHidden(ExtensionFunctionButton functionButton) {
    JButton jButton = functionButtonsMap.get(functionButton);
    return jButton.isVisible();
  }

  public void add(ExtensionFunctionButton functionButton) {
    JButton jButton = new JButton();
    ;
    functionButtonsMap.put(functionButton, jButton);
    update(functionButton);

    if (this.group == null) {
      actualFrame.add(jButton);
      actualFrame.invalidate();
      actualFrame.repaint();
    } else {
      subTab.add(jButton);
      subTab.invalidate();
      subTab.repaint();
      rootFrame.actualFrame.invalidate();
      rootFrame.actualFrame.repaint();
    }
    jButton.addActionListener(
        e -> {
          try {
            functionButton.execute();
          } catch (Exception e1) {
            MapTool.addLocalMessage(e.toString());
          }
        });
  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  protected ImageIcon createImageIcon(ExtensionFunctionButton functionButton) {

    if (functionButton == null) {
      return null;
    }

    String path = functionButton.getImageFile();
    if (path == null) {
      return null;
    }
    path = path.trim();

    if (path.startsWith("asset://")) {
      MD5Key assetId = new MD5Key(path.substring(8));
      BufferedImage image = ImageManager.getImageAndWait(assetId);
      Image newImg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
      return new ImageIcon(newImg, functionButton.getTooltip());
    }

    URL imgURL = functionButton.getClass().getResource(path);
    if (imgURL != null) {
      ImageIcon icon = new ImageIcon(imgURL, functionButton.getTooltip());
      Image img = icon.getImage();
      Image newImg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
      return new ImageIcon(newImg, functionButton.getTooltip());

    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }

  public boolean isVisble() {
    if (this.actualFrame == null) {
      return false;
    }

    return this.actualFrame.isVisible();
  }

  public boolean isMinimized() {
    return minimized;
  }

  public void setMinimized(boolean minimized) {
    // save preferences before minimizing
    if (contentContainer.isVisible() && minimized) {
      savePreferences();
    }

    this.minimized = minimized;

    if (minimized) {
      contentContainer.setVisible(false);
      actualFrame.pack();
    } else {
      // load preferences if restoring
      loadPreferences(true);
      contentContainer.setVisible(true);
    }

    actualFrame.invalidate();
  }
}
