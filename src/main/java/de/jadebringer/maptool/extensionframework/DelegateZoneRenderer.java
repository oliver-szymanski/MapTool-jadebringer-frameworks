package de.jadebringer.maptool.extensionframework;

import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.FocusEvent.Cause;
import java.awt.geom.Area;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.accessibility.AccessibleContext;
import javax.swing.InputVerifier;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.ComponentUI;

import net.rptools.lib.CodeTimer;
import net.rptools.maptool.client.ScreenPoint;
import net.rptools.maptool.client.ui.Scale;
import net.rptools.maptool.client.ui.zone.PlayerView;
import net.rptools.maptool.client.ui.zone.ZoneOverlay;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.client.ui.zone.ZoneView;
import net.rptools.maptool.model.AbstractPoint;
import net.rptools.maptool.model.CellPoint;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Label;
import net.rptools.maptool.model.Path;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.TokenFootprint;
import net.rptools.maptool.model.Zone;
import net.rptools.maptool.model.ZonePoint;
import net.rptools.maptool.model.Player.Role;
import net.rptools.maptool.model.Zone.Layer;

@SuppressWarnings("deprecation")
class DelegateZoneRenderer extends ZoneRenderer {

  private static final long serialVersionUID = -381662796500611853L;

  public int hashCode() {
    return wrapped.hashCode();
  }

  public boolean equals(Object obj) {
    return wrapped.equals(obj);
  }

  public int getComponentCount() {
    return wrapped.getComponentCount();
  }

  public int countComponents() {
    return wrapped.countComponents();
  }

  public void setAutoResizeStamp(boolean value) {
    wrapped.setAutoResizeStamp(value);
  }

  public Component getComponent(int n) {
    return wrapped.getComponent(n);
  }

  public boolean isAutoResizeStamp() {
    return wrapped.isAutoResizeStamp();
  }

  public void showPath(Token token, boolean show) {
    wrapped.showPath(token, show);
  }

  public void centerOn(Token token) {
    wrapped.centerOn(token);
  }

  public Component[] getComponents() {
    return wrapped.getComponents();
  }

  public ZonePoint getCenterPoint() {
    return wrapped.getCenterPoint();
  }

  public boolean isPathShowing(Token token) {
    return wrapped.isPathShowing(token);
  }

  public void clearShowPaths() {
    wrapped.clearShowPaths();
  }

  public Scale getZoneScale() {
    return wrapped.getZoneScale();
  }

  public void setZoneScale(Scale scale) {
    if (wrapped == null)
      return; // happens during initialization
    wrapped.setZoneScale(scale);
  }

  public void flushDrawableRenderer() {
    wrapped.flushDrawableRenderer();
  }

  public Insets insets() {
    return wrapped.insets();
  }

  public ScreenPoint getPointUnderMouse() {
    return wrapped.getPointUnderMouse();
  }

  public void setMouseOver(Token token) {
    wrapped.setMouseOver(token);
  }

  public boolean isOpaque() {
    return wrapped.isOpaque();
  }

  public void addMoveSelectionSet(String playerId, GUID keyToken, Set<GUID> tokenList, boolean clearLocalSelected) {
    wrapped.addMoveSelectionSet(playerId, keyToken, tokenList, clearLocalSelected);
  }

  public Component add(Component comp) {
    return wrapped.add(comp);
  }

  public boolean hasMoveSelectionSetMoved(GUID keyToken, ZonePoint point) {
    return wrapped.hasMoveSelectionSetMoved(keyToken, point);
  }

  public void updateMoveSelectionSet(GUID keyToken, ZonePoint offset) {
    wrapped.updateMoveSelectionSet(keyToken, offset);
  }

  public Component add(String name, Component comp) {
    return wrapped.add(name, comp);
  }

  public void toggleMoveSelectionSetWaypoint(GUID keyToken, ZonePoint location) {
    wrapped.toggleMoveSelectionSetWaypoint(keyToken, location);
  }

  public ZonePoint getLastWaypoint(GUID keyToken) {
    return wrapped.getLastWaypoint(keyToken);
  }

  public void removeMoveSelectionSet(GUID keyToken) {
    wrapped.removeMoveSelectionSet(keyToken);
  }

  public void commitMoveSelectionSet(GUID keyTokenId) {
    wrapped.commitMoveSelectionSet(keyTokenId);
    eventDispatcher.onTokensMoved(wrapped.getSelectedTokensList());
  }

  public Component add(Component comp, int index) {
    return wrapped.add(comp, index);
  }

  public void setInheritsPopupMenu(boolean value) {
    wrapped.setInheritsPopupMenu(value);
  }

  public boolean getInheritsPopupMenu() {
    return wrapped.getInheritsPopupMenu();
  }

  public void setComponentPopupMenu(JPopupMenu popup) {
    wrapped.setComponentPopupMenu(popup);
  }

  public JPopupMenu getComponentPopupMenu() {
    return wrapped.getComponentPopupMenu();
  }

  public void addMouseListener(MouseListener l) {
    if (wrapped == null)
      return; // happens during initialization
    wrapped.addMouseListener(l);
  }

  public boolean isTokenMoving(Token token) {
    return wrapped.isTokenMoving(token);
  }

  public void centerOn(ZonePoint point) {
    wrapped.centerOn(point);
  }

  public void updateUI() {
    wrapped.updateUI();
  }

  public void centerOn(CellPoint point) {
    wrapped.centerOn(point);
  }

  public void flush(Token token) {
    wrapped.flush(token);
  }

  public ComponentUI getUI() {
    return wrapped.getUI();
  }

  public ZoneView getZoneView() {
    return wrapped.getZoneView();
  }

  public void flush() {
    wrapped.flush();
  }

  public void flushLight() {
    wrapped.flushLight();
  }

  public void flushFog() {
    wrapped.flushFog();
  }

  public Zone getZone() {
    if (wrapped == null)
      return EventDispatcher.getCurrentZoneRenderer().getZone(); // happens during initialization
    return wrapped.getZone();
  }

  public void addOverlay(ZoneOverlay overlay) {
    wrapped.addOverlay(overlay);
  }

  public void removeOverlay(ZoneOverlay overlay) {
    wrapped.removeOverlay(overlay);
  }

  public void moveViewBy(int dx, int dy) {
    wrapped.moveViewBy(dx, dy);
  }

  public void zoomReset(int x, int y) {
    wrapped.zoomReset(x, y);
  }

  public void zoomIn(int x, int y) {
    wrapped.zoomIn(x, y);
  }

  public void zoomOut(int x, int y) {
    wrapped.zoomOut(x, y);
  }

  public void setView(int x, int y, double scale) {
    wrapped.setView(x, y, scale);
  }

  public void enforceView(int x, int y, double scale, int gmWidth, int gmHeight) {
    wrapped.enforceView(x, y, scale, gmWidth, gmHeight);
  }

  public String getUIClassID() {
    return wrapped.getUIClassID();
  }

  public void restoreView() {
    wrapped.restoreView();
  }

  public void setComponentZOrder(Component comp, int index) {
    wrapped.setComponentZOrder(comp, index);
  }

  public void forcePlayersView() {
    wrapped.forcePlayersView();
  }

  public void maybeForcePlayersView() {
    wrapped.maybeForcePlayersView();
  }

  public BufferedImage getMiniImage(int size) {
    return wrapped.getMiniImage(size);
  }

  public void paintComponent(Graphics g) {
    wrapped.paintComponent(g);
  }

  public PlayerView getPlayerView() {
    return wrapped.getPlayerView();
  }

  public PlayerView getPlayerView(Role role) {
    return wrapped.getPlayerView(role);
  }

  public Rectangle fogExtents() {
    return wrapped.fogExtents();
  }

  public Rectangle zoneExtents(PlayerView view) {
    return wrapped.zoneExtents(view);
  }

  public String getName() {
    return wrapped.getName();
  }

  public void setName(String name) {
    wrapped.setName(name);
  }

  public Container getParent() {
    return wrapped.getParent();
  }

  public void setDropTarget(DropTarget dt) {
    wrapped.setDropTarget(dt);
  }

  public void invalidateCurrentViewCache() {
    wrapped.invalidateCurrentViewCache();
  }

  public DropTarget getDropTarget() {
    if (wrapped == null)
      return EventDispatcher.getCurrentZoneRenderer().getDropTarget(); // happens during initialization
    return wrapped.getDropTarget();
  }

  public void renderZone(Graphics2D g2d, PlayerView view) {
    wrapped.renderZone(g2d, view);
  }

  public GraphicsConfiguration getGraphicsConfiguration() {
    return wrapped.getGraphicsConfiguration();
  }

  public int getComponentZOrder(Component comp) {
    return wrapped.getComponentZOrder(comp);
  }

  public void update(Graphics g) {
    wrapped.update(g);
  }

  public void paint(Graphics g) {
    wrapped.paint(g);
  }

  public void add(Component comp, Object constraints) {
    wrapped.add(comp, constraints);
  }

  public void add(Component comp, Object constraints, int index) {
    wrapped.add(comp, constraints, index);
  }

  public Toolkit getToolkit() {
    return wrapped.getToolkit();
  }

  public boolean isValid() {
    if (wrapped == null)
      return EventDispatcher.getCurrentZoneRenderer().isValid(); // happens during initialization
    return wrapped.isValid();
  }

  public boolean isDisplayable() {
    return wrapped.isDisplayable();
  }

  public boolean isVisible() {
    return wrapped.isVisible();
  }

  public void printAll(Graphics g) {
    wrapped.printAll(g);
  }

  public void print(Graphics g) {
    wrapped.print(g);
  }

  public Point getMousePosition() throws HeadlessException {
    return wrapped.getMousePosition();
  }

  public void remove(int index) {
    wrapped.remove(index);
  }

  public boolean isPaintingTile() {
    return wrapped.isPaintingTile();
  }

  public boolean isShowing() {
    return wrapped.isShowing();
  }

  public boolean isEnabled() {
    return wrapped.isEnabled();
  }

  public CodeTimer getCodeTimer() {
    return wrapped.getCodeTimer();
  }

  public boolean isManagingFocus() {
    if (wrapped == null)
      return EventDispatcher.getCurrentZoneRenderer().isManagingFocus(); // happens during initialization
    return wrapped.isManagingFocus();
  }

  public void remove(Component comp) {
    wrapped.remove(comp);
  }

  public void removeAll() {
    wrapped.removeAll();
  }

  public void enable(boolean b) {
    wrapped.enable(b);
  }

  public void setNextFocusableComponent(Component aComponent) {
    wrapped.setNextFocusableComponent(aComponent);
  }

  public Component getNextFocusableComponent() {
    return wrapped.getNextFocusableComponent();
  }

  public void enableInputMethods(boolean enable) {
    wrapped.enableInputMethods(enable);
  }

  public void setRequestFocusEnabled(boolean requestFocusEnabled) {
    wrapped.setRequestFocusEnabled(requestFocusEnabled);
  }

  public boolean isRequestFocusEnabled() {
    return wrapped.isRequestFocusEnabled();
  }

  public void show() {
    wrapped.show();
  }

  public void requestFocus() {
    wrapped.requestFocus();
  }

  public void show(boolean b) {
    wrapped.show(b);
  }

  public boolean requestFocus(boolean temporary) {
    return wrapped.requestFocus(temporary);
  }

  public LayoutManager getLayout() {
    return wrapped.getLayout();
  }

  public void setLayout(LayoutManager mgr) {
    wrapped.setLayout(mgr);
  }

  public boolean requestFocusInWindow() {
    return wrapped.requestFocusInWindow();
  }

  public void doLayout() {
    wrapped.doLayout();
  }

  public void layout() {
    wrapped.layout();
  }

  public Color getForeground() {
    return wrapped.getForeground();
  }

  public void grabFocus() {
    wrapped.grabFocus();
  }

  public void setVerifyInputWhenFocusTarget(boolean verifyInputWhenFocusTarget) {
    wrapped.setVerifyInputWhenFocusTarget(verifyInputWhenFocusTarget);
  }

  public boolean isForegroundSet() {
    return wrapped.isForegroundSet();
  }

  public void invalidate() {
    if (wrapped == null)
      return;
    wrapped.invalidate();
  }

  public Color getBackground() {
    return wrapped.getBackground();
  }

  public boolean getVerifyInputWhenFocusTarget() {
    return wrapped.getVerifyInputWhenFocusTarget();
  }

  public void validate() {
    wrapped.validate();
  }

  public FontMetrics getFontMetrics(Font font) {
    return wrapped.getFontMetrics(font);
  }

  public void setPreferredSize(Dimension preferredSize) {
    wrapped.setPreferredSize(preferredSize);
  }

  public boolean isBackgroundSet() {
    return wrapped.isBackgroundSet();
  }

  public Dimension getPreferredSize() {
    return wrapped.getPreferredSize();
  }

  public Font getFont() {
    return wrapped.getFont();
  }

  public void setMaximumSize(Dimension maximumSize) {
    wrapped.setMaximumSize(maximumSize);
  }

  public Dimension getMaximumSize() {
    return wrapped.getMaximumSize();
  }

  public void setMinimumSize(Dimension minimumSize) {
    wrapped.setMinimumSize(minimumSize);
  }

  public boolean isFontSet() {
    return wrapped.isFontSet();
  }

  public Locale getLocale() {
    return wrapped.getLocale();
  }

  public Dimension getMinimumSize() {
    return wrapped.getMinimumSize();
  }

  public boolean contains(int x, int y) {
    return wrapped.contains(x, y);
  }

  public void setLocale(Locale l) {
    wrapped.setLocale(l);
  }

  public void setBorder(Border border) {
    wrapped.setBorder(border);
  }

  public ColorModel getColorModel() {
    return wrapped.getColorModel();
  }

  public Point getLocation() {
    return wrapped.getLocation();
  }

  public Dimension preferredSize() {
    return wrapped.preferredSize();
  }

  public Border getBorder() {
    return wrapped.getBorder();
  }

  public Area getVisibleArea(Token token) {
    return wrapped.getVisibleArea(token);
  }

  public boolean isLoading() {
    return wrapped.isLoading();
  }

  public Insets getInsets() {
    return wrapped.getInsets();
  }

  public Point getLocationOnScreen() {
    return wrapped.getLocationOnScreen();
  }

  public Insets getInsets(Insets insets) {
    return wrapped.getInsets(insets);
  }

  public Dimension minimumSize() {
    return wrapped.minimumSize();
  }

  public Point location() {
    return wrapped.location();
  }

  public float getAlignmentY() {
    return wrapped.getAlignmentY();
  }

  public void setLocation(int x, int y) {
    wrapped.setLocation(x, y);
  }

  public void setAlignmentY(float alignmentY) {
    wrapped.setAlignmentY(alignmentY);
  }

  public float getAlignmentX() {
    return wrapped.getAlignmentX();
  }

  public void move(int x, int y) {
    wrapped.move(x, y);
  }

  public void setAlignmentX(float alignmentX) {
    wrapped.setAlignmentX(alignmentX);
  }

  public void setInputVerifier(InputVerifier inputVerifier) {
    wrapped.setInputVerifier(inputVerifier);
  }

  public void setLocation(Point p) {
    wrapped.setLocation(p);
  }

  public InputVerifier getInputVerifier() {
    return wrapped.getInputVerifier();
  }

  public Dimension getSize() {
    return wrapped.getSize();
  }

  public Graphics getGraphics() {
    return wrapped.getGraphics();
  }

  public Dimension size() {
    return wrapped.size();
  }

  public void setDebugGraphicsOptions(int debugOptions) {
    wrapped.setDebugGraphicsOptions(debugOptions);
  }

  public void setSize(int width, int height) {
    wrapped.setSize(width, height);
  }

  public void resize(int width, int height) {
    wrapped.resize(width, height);
  }

  public void setSize(Dimension d) {
    wrapped.setSize(d);
  }

  public int getDebugGraphicsOptions() {
    return wrapped.getDebugGraphicsOptions();
  }

  public void resize(Dimension d) {
    wrapped.resize(d);
  }

  public Rectangle getBounds() {
    return wrapped.getBounds();
  }

  public void registerKeyboardAction(ActionListener anAction, String aCommand, KeyStroke aKeyStroke, int aCondition) {
    wrapped.registerKeyboardAction(anAction, aCommand, aKeyStroke, aCondition);
  }

  public Rectangle bounds() {
    return wrapped.bounds();
  }

  public void setBounds(int x, int y, int width, int height) {
    wrapped.setBounds(x, y, width, height);
  }

  public void paintComponents(Graphics g) {
    wrapped.paintComponents(g);
  }

  public void printComponents(Graphics g) {
    wrapped.printComponents(g);
  }

  public void addContainerListener(ContainerListener l) {
    wrapped.addContainerListener(l);
  }

  public void removeContainerListener(ContainerListener l) {
    wrapped.removeContainerListener(l);
  }

  public ContainerListener[] getContainerListeners() {
    return wrapped.getContainerListeners();
  }

  public void setBounds(Rectangle r) {
    wrapped.setBounds(r);
  }

  public void registerKeyboardAction(ActionListener anAction, KeyStroke aKeyStroke, int aCondition) {
    wrapped.registerKeyboardAction(anAction, aKeyStroke, aCondition);
  }

  public void unregisterKeyboardAction(KeyStroke aKeyStroke) {
    wrapped.unregisterKeyboardAction(aKeyStroke);
  }

  public void renderPath(Graphics2D g, Path<? extends AbstractPoint> path, TokenFootprint footprint) {
    wrapped.renderPath(g, path, footprint);
  }

  public KeyStroke[] getRegisteredKeyStrokes() {
    return wrapped.getRegisteredKeyStrokes();
  }

  public int getConditionForKeyStroke(KeyStroke aKeyStroke) {
    return wrapped.getConditionForKeyStroke(aKeyStroke);
  }

  public boolean isLightweight() {
    return wrapped.isLightweight();
  }

  public ActionListener getActionForKeyStroke(KeyStroke aKeyStroke) {
    return wrapped.getActionForKeyStroke(aKeyStroke);
  }

  public void resetKeyboardActions() {
    wrapped.resetKeyboardActions();
  }

  public boolean isPreferredSizeSet() {
    return wrapped.isPreferredSizeSet();
  }

  public boolean isMinimumSizeSet() {
    return wrapped.isMinimumSizeSet();
  }

  public void drawText(String text, int x, int y) {
    wrapped.drawText(text, x, y);
  }

  public void setShape(Shape shape) {
    wrapped.setShape(shape);
  }

  public void setShape2(Shape shape) {
    wrapped.setShape2(shape);
  }

  public Shape getShape() {
    return wrapped.getShape();
  }

  public Shape getShape2() {
    return wrapped.getShape2();
  }

  public void drawShape(Shape shape, int x, int y) {
    wrapped.drawShape(shape, x, y);
  }

  public boolean isMaximumSizeSet() {
    return wrapped.isMaximumSizeSet();
  }

  public void showBlockedMoves(Graphics2D g, ZonePoint point, double angle, BufferedImage image, float size) {
    wrapped.showBlockedMoves(g, point, angle, image, size);
  }

  public void highlightCell(Graphics2D g, ZonePoint point, BufferedImage image, float size) {
    wrapped.highlightCell(g, point, image, size);
  }

  public void deliverEvent(java.awt.Event e) {
    wrapped.deliverEvent(e);
  }

  public Component getComponentAt(int x, int y) {
    return wrapped.getComponentAt(x, y);
  }

  public void addDistanceText(Graphics2D g, ZonePoint point, float size, double distance) {
    wrapped.addDistanceText(g, point, size, distance);
  }

  public Component locate(int x, int y) {
    return wrapped.locate(x, y);
  }

  public int getBaseline(int width, int height) {
    return wrapped.getBaseline(width, height);
  }

  public List<Token> getTokensOnScreen() {
    return wrapped.getTokensOnScreen();
  }

  public Component getComponentAt(Point p) {
    return wrapped.getComponentAt(p);
  }

  public BaselineResizeBehavior getBaselineResizeBehavior() {
    return wrapped.getBaselineResizeBehavior();
  }

  public Point getMousePosition(boolean allowChildren) throws HeadlessException {
    return wrapped.getMousePosition(allowChildren);
  }

  public Layer getActiveLayer() {
    return wrapped.getActiveLayer();
  }

  public void setActiveLayer(Layer layer) {
    wrapped.setActiveLayer(layer);
  }

  public boolean requestDefaultFocus() {
    return wrapped.requestDefaultFocus();
  }

  public Component findComponentAt(int x, int y) {
    return wrapped.findComponentAt(x, y);
  }

  public void setVisible(boolean aFlag) {
    wrapped.setVisible(aFlag);
  }

  public void setEnabled(boolean enabled) {
    wrapped.setEnabled(enabled);
  }

  public void setForeground(Color fg) {
    wrapped.setForeground(fg);
  }

  public void setBackground(Color bg) {
    wrapped.setBackground(bg);
  }

  public Component findComponentAt(Point p) {
    return wrapped.findComponentAt(p);
  }

  public void setFont(Font font) {
    wrapped.setFont(font);
  }

  public Cursor getCursor() {
    return wrapped.getCursor();
  }

  public boolean isAncestorOf(Component c) {
    return wrapped.isAncestorOf(c);
  }

  public boolean isCursorSet() {
    return wrapped.isCursorSet();
  }

  public void paintAll(Graphics g) {
    wrapped.paintAll(g);
  }

  public void repaint() {
    wrapped.repaint();
  }

  public void setToolTipText(String text) {
    wrapped.setToolTipText(text);
  }

  public void repaint(long tm) {
    wrapped.repaint(tm);
  }

  public void list(PrintStream out, int indent) {
    wrapped.list(out, indent);
  }

  public String getToolTipText() {
    return wrapped.getToolTipText();
  }

  public void repaint(int x, int y, int width, int height) {
    wrapped.repaint(x, y, width, height);
  }

  public String getToolTipText(MouseEvent event) {
    return wrapped.getToolTipText(event);
  }

  public void list(PrintWriter out, int indent) {
    wrapped.list(out, indent);
  }

  public Point getToolTipLocation(MouseEvent event) {
    return wrapped.getToolTipLocation(event);
  }

  public Point getPopupLocation(MouseEvent event) {
    return wrapped.getPopupLocation(event);
  }

  public JToolTip createToolTip() {
    return wrapped.createToolTip();
  }

  public void scrollRectToVisible(Rectangle aRect) {
    wrapped.scrollRectToVisible(aRect);
  }

  public void setAutoscrolls(boolean autoscrolls) {
    wrapped.setAutoscrolls(autoscrolls);
  }

  public Set<AWTKeyStroke> getFocusTraversalKeys(int id) {
    return wrapped.getFocusTraversalKeys(id);
  }

  public boolean getAutoscrolls() {
    return wrapped.getAutoscrolls();
  }

  public void setTransferHandler(TransferHandler newHandler) {
    if (wrapped == null)
      return; // happens during initialization
    wrapped.setTransferHandler(newHandler);
  }

  public boolean areFocusTraversalKeysSet(int id) {
    return wrapped.areFocusTraversalKeysSet(id);
  }

  public Image createImage(ImageProducer producer) {
    return wrapped.createImage(producer);
  }

  public TransferHandler getTransferHandler() {
    return wrapped.getTransferHandler();
  }

  public Image createImage(int width, int height) {
    return wrapped.createImage(width, height);
  }

  public boolean isFocusCycleRoot(Container container) {
    return wrapped.isFocusCycleRoot(container);
  }

  public VolatileImage createVolatileImage(int width, int height) {
    return wrapped.createVolatileImage(width, height);
  }

  public VolatileImage createVolatileImage(int width, int height, ImageCapabilities caps) throws AWTException {
    return wrapped.createVolatileImage(width, height, caps);
  }

  public boolean prepareImage(Image image, ImageObserver observer) {
    return wrapped.prepareImage(image, observer);
  }

  public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
    return wrapped.prepareImage(image, width, height, observer);
  }

  public void setFocusTraversalPolicy(FocusTraversalPolicy policy) {
    wrapped.setFocusTraversalPolicy(policy);
  }

  public int checkImage(Image image, ImageObserver observer) {
    return wrapped.checkImage(image, observer);
  }

  public FocusTraversalPolicy getFocusTraversalPolicy() {
    return wrapped.getFocusTraversalPolicy();
  }

  public int checkImage(Image image, int width, int height, ImageObserver observer) {
    return wrapped.checkImage(image, width, height, observer);
  }

  public boolean isFocusTraversalPolicySet() {
    return wrapped.isFocusTraversalPolicySet();
  }

  public void setFocusCycleRoot(boolean focusCycleRoot) {
    wrapped.setFocusCycleRoot(focusCycleRoot);
  }

  public Set<GUID> getSelectedTokenSet() {
    return wrapped.getSelectedTokenSet();
  }

  public void setKeepSelectedTokenSet(boolean keep) {
    wrapped.setKeepSelectedTokenSet(keep);
  }

  public Set<GUID> getOwnedTokens(Set<GUID> tokenSet) {
    return wrapped.getOwnedTokens(tokenSet);
  }

  public List<Token> getSelectedTokensList() {
    return wrapped.getSelectedTokensList();
  }

  public boolean isFocusCycleRoot() {
    return wrapped.isFocusCycleRoot();
  }

  public boolean isTokenSelectable(GUID tokenGUID) {
    return wrapped.isTokenSelectable(tokenGUID);
  }

  public void deselectToken(GUID tokenGUID) {
    wrapped.deselectToken(tokenGUID);
  }

  public boolean selectToken(GUID tokenGUID) {
    return wrapped.selectToken(tokenGUID);
  }

  public void selectTokens(Collection<GUID> tokens) {
    wrapped.selectTokens(tokens);
  }

  public void selectTokens(Rectangle rect) {
    wrapped.selectTokens(rect);
  }

  public void clearSelectedTokens() {
    wrapped.clearSelectedTokens();
  }

  public void undoSelectToken() {
    wrapped.undoSelectToken();
  }

  public void transferFocusDownCycle() {
    wrapped.transferFocusDownCycle();
  }

  public void applyComponentOrientation(ComponentOrientation o) {
    wrapped.applyComponentOrientation(o);
  }

  public void cycleSelectedToken(int direction) {
    wrapped.cycleSelectedToken(direction);
  }

  public void enable() {
    wrapped.enable();
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    wrapped.addPropertyChangeListener(listener);
  }

  public void disable() {
    wrapped.disable();
  }

  public boolean playerOwnsAllSelected() {
    return wrapped.playerOwnsAllSelected();
  }

  public Area getTokenBounds(Token token) {
    return wrapped.getTokenBounds(token);
  }

  public Area getMarkerBounds(Token token) {
    return wrapped.getMarkerBounds(token);
  }

  public Rectangle getLabelBounds(Label label) {
    return wrapped.getLabelBounds(label);
  }

  public Token getTokenAt(int x, int y) {
    return wrapped.getTokenAt(x, y);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    wrapped.addPropertyChangeListener(propertyName, listener);
  }

  public Token getMarkerAt(int x, int y) {
    return wrapped.getMarkerAt(x, y);
  }

  public List<Token> getTokenStackAt(int x, int y) {
    return wrapped.getTokenStackAt(x, y);
  }

  public Label getLabelAt(int x, int y) {
    return wrapped.getLabelAt(x, y);
  }

  public int getViewOffsetX() {
    return wrapped.getViewOffsetX();
  }

  public int getViewOffsetY() {
    return wrapped.getViewOffsetY();
  }

  public void adjustGridSize(int delta) {
    wrapped.adjustGridSize(delta);
  }

  public void moveGridBy(int dx, int dy) {
    wrapped.moveGridBy(dx, dy);
  }

  public CellPoint getCellAt(ScreenPoint screenPoint) {
    return wrapped.getCellAt(screenPoint);
  }

  public void setScale(double scale) {
    wrapped.setScale(scale);
  }

  public double getScale() {
    return wrapped.getScale();
  }

  public double getScaledGridSize() {
    return wrapped.getScaledGridSize();
  }

  public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
    return wrapped.imageUpdate(img, infoflags, x, y, w, h);
  }

  public void dragEnter(DropTargetDragEvent dtde) {
    wrapped.dragEnter(dtde);
  }

  public void dragExit(DropTargetEvent dte) {
    wrapped.dragExit(dte);
  }

  public void dragOver(DropTargetDragEvent dtde) {
    wrapped.dragOver(dtde);
  }

  public void setFocusTraversalKeys(int id, Set<? extends AWTKeyStroke> keystrokes) {
    wrapped.setFocusTraversalKeys(id, keystrokes);
  }

  public void reshape(int x, int y, int w, int h) {
    wrapped.reshape(x, y, w, h);
  }

  public Rectangle getBounds(Rectangle rv) {
    return wrapped.getBounds(rv);
  }

  public void drop(DropTargetDropEvent dtde) {
    wrapped.drop(dtde);
  }

  public Set<GUID> getVisibleTokenSet() {
    return wrapped.getVisibleTokenSet();
  }

  public Dimension getSize(Dimension rv) {
    return wrapped.getSize(rv);
  }

  public void dropActionChanged(DropTargetDragEvent dtde) {
    wrapped.dropActionChanged(dtde);
  }

  public Point getLocation(Point rv) {
    return wrapped.getLocation(rv);
  }

  public int compareTo(ZoneRenderer o) {
    return wrapped.compareTo(o);
  }

  public void setIgnoreRepaint(boolean ignoreRepaint) {
    wrapped.setIgnoreRepaint(ignoreRepaint);
  }

  public int getX() {
    return wrapped.getX();
  }

  public List<Token> getHighlightCommonMacros() {
    return wrapped.getHighlightCommonMacros();
  }

  public void setHighlightCommonMacros(List<Token> affectedTokens) {
    wrapped.setHighlightCommonMacros(affectedTokens);
  }

  public void setCursor(Cursor cursor) {
    wrapped.setCursor(cursor);
  }

  public int getY() {
    return wrapped.getY();
  }

  public boolean getIgnoreRepaint() {
    return wrapped.getIgnoreRepaint();
  }

  public int getWidth() {
    return wrapped.getWidth();
  }

  public int getHeight() {
    return wrapped.getHeight();
  }

  public boolean inside(int x, int y) {
    return wrapped.inside(x, y);
  }

  public Cursor createCustomCursor(String resource, String tokenName) {
    return wrapped.createCustomCursor(resource, tokenName);
  }

  public boolean contains(Point p) {
    return wrapped.contains(p);
  }

  public void setOpaque(boolean isOpaque) {
    wrapped.setOpaque(isOpaque);
  }

  public void computeVisibleRect(Rectangle visibleRect) {
    wrapped.computeVisibleRect(visibleRect);
  }

  public Rectangle getVisibleRect() {
    return wrapped.getVisibleRect();
  }

  public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void addVetoableChangeListener(VetoableChangeListener listener) {
    wrapped.addVetoableChangeListener(listener);
  }

  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    wrapped.removeVetoableChangeListener(listener);
  }

  public VetoableChangeListener[] getVetoableChangeListeners() {
    return wrapped.getVetoableChangeListeners();
  }

  public Container getTopLevelAncestor() {
    return wrapped.getTopLevelAncestor();
  }

  public void addAncestorListener(AncestorListener listener) {
    wrapped.addAncestorListener(listener);
  }

  public void removeAncestorListener(AncestorListener listener) {
    wrapped.removeAncestorListener(listener);
  }

  public AncestorListener[] getAncestorListeners() {
    return wrapped.getAncestorListeners();
  }

  public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
    return wrapped.getListeners(listenerType);
  }

  public void addNotify() {
    wrapped.addNotify();
  }

  public void removeNotify() {
    wrapped.removeNotify();
  }

  public void repaint(long tm, int x, int y, int width, int height) {
    wrapped.repaint(tm, x, y, width, height);
  }

  public void repaint(Rectangle r) {
    wrapped.repaint(r);
  }

  public void revalidate() {
    wrapped.revalidate();
  }

  public boolean postEvent(java.awt.Event e) {
    return wrapped.postEvent(e);
  }

  public boolean isValidateRoot() {
    return wrapped.isValidateRoot();
  }

  public void addComponentListener(ComponentListener l) {
    wrapped.addComponentListener(l);
  }

  public boolean isOptimizedDrawingEnabled() {
    return wrapped.isOptimizedDrawingEnabled();
  }

  public void removeComponentListener(ComponentListener l) {
    wrapped.removeComponentListener(l);
  }

  public void paintImmediately(int x, int y, int w, int h) {
    wrapped.paintImmediately(x, y, w, h);
  }

  public ComponentListener[] getComponentListeners() {
    return wrapped.getComponentListeners();
  }

  public void addFocusListener(FocusListener l) {
    wrapped.addFocusListener(l);
  }

  public void removeFocusListener(FocusListener l) {
    wrapped.removeFocusListener(l);
  }

  public void paintImmediately(Rectangle r) {
    wrapped.paintImmediately(r);
  }

  public FocusListener[] getFocusListeners() {
    return wrapped.getFocusListeners();
  }

  public void addHierarchyListener(HierarchyListener l) {
    wrapped.addHierarchyListener(l);
  }

  public void removeHierarchyListener(HierarchyListener l) {
    wrapped.removeHierarchyListener(l);
  }

  public HierarchyListener[] getHierarchyListeners() {
    return wrapped.getHierarchyListeners();
  }

  public void addHierarchyBoundsListener(HierarchyBoundsListener l) {
    wrapped.addHierarchyBoundsListener(l);
  }

  public void removeHierarchyBoundsListener(HierarchyBoundsListener l) {
    wrapped.removeHierarchyBoundsListener(l);
  }

  public HierarchyBoundsListener[] getHierarchyBoundsListeners() {
    return wrapped.getHierarchyBoundsListeners();
  }

  public void addKeyListener(KeyListener l) {
    wrapped.addKeyListener(l);
  }

  public void removeKeyListener(KeyListener l) {
    wrapped.removeKeyListener(l);
  }

  public void setDoubleBuffered(boolean aFlag) {
    wrapped.setDoubleBuffered(aFlag);
  }

  public boolean isDoubleBuffered() {
    return wrapped.isDoubleBuffered();
  }

  public KeyListener[] getKeyListeners() {
    return wrapped.getKeyListeners();
  }

  public JRootPane getRootPane() {
    return wrapped.getRootPane();
  }

  public void removeMouseListener(MouseListener l) {
    wrapped.removeMouseListener(l);
  }

  public MouseListener[] getMouseListeners() {
    return wrapped.getMouseListeners();
  }

  public void addMouseMotionListener(MouseMotionListener l) {
    if (wrapped == null)
      return; // happens during initialization
    wrapped.addMouseMotionListener(l);
  }

  public void removeMouseMotionListener(MouseMotionListener l) {
    wrapped.removeMouseMotionListener(l);
  }

  public MouseMotionListener[] getMouseMotionListeners() {
    return wrapped.getMouseMotionListeners();
  }

  public void addMouseWheelListener(MouseWheelListener l) {
    if (wrapped == null)
      return; // happens during initialization
    wrapped.addMouseWheelListener(l);
  }

  public void removeMouseWheelListener(MouseWheelListener l) {
    wrapped.removeMouseWheelListener(l);
  }

  public MouseWheelListener[] getMouseWheelListeners() {
    return wrapped.getMouseWheelListeners();
  }

  public void addInputMethodListener(InputMethodListener l) {
    wrapped.addInputMethodListener(l);
  }

  public void removeInputMethodListener(InputMethodListener l) {
    wrapped.removeInputMethodListener(l);
  }

  public void hide() {
    wrapped.hide();
  }

  public InputMethodListener[] getInputMethodListeners() {
    return wrapped.getInputMethodListeners();
  }

  public InputMethodRequests getInputMethodRequests() {
    return wrapped.getInputMethodRequests();
  }

  public InputContext getInputContext() {
    return wrapped.getInputContext();
  }

  public boolean handleEvent(java.awt.Event evt) {
    return wrapped.handleEvent(evt);
  }

  public boolean mouseDown(java.awt.Event evt, int x, int y) {
    return wrapped.mouseDown(evt, x, y);
  }

  public boolean mouseDrag(java.awt.Event evt, int x, int y) {
    return wrapped.mouseDrag(evt, x, y);
  }

  public boolean mouseUp(java.awt.Event evt, int x, int y) {
    return wrapped.mouseUp(evt, x, y);
  }

  public boolean mouseMove(java.awt.Event evt, int x, int y) {
    return wrapped.mouseMove(evt, x, y);
  }

  public boolean mouseEnter(java.awt.Event evt, int x, int y) {
    return wrapped.mouseEnter(evt, x, y);
  }

  public boolean mouseExit(java.awt.Event evt, int x, int y) {
    return wrapped.mouseExit(evt, x, y);
  }

  public boolean keyDown(java.awt.Event evt, int key) {
    return wrapped.keyDown(evt, key);
  }

  public boolean keyUp(java.awt.Event evt, int key) {
    return wrapped.keyUp(evt, key);
  }

  public boolean action(java.awt.Event evt, Object what) {
    return wrapped.action(evt, what);
  }

  public boolean gotFocus(java.awt.Event evt, Object what) {
    return wrapped.gotFocus(evt, what);
  }

  public boolean lostFocus(java.awt.Event evt, Object what) {
    return wrapped.lostFocus(evt, what);
  }

  public boolean isFocusTraversable() {
    return wrapped.isFocusTraversable();
  }

  public boolean isFocusable() {
    return wrapped.isFocusable();
  }

  public void setFocusable(boolean focusable) {
    if (wrapped == null)
      return; // happens during initialization
    wrapped.setFocusable(focusable);
  }

  public void setFocusTraversalKeysEnabled(boolean focusTraversalKeysEnabled) {
    wrapped.setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
  }

  public boolean getFocusTraversalKeysEnabled() {
    return wrapped.getFocusTraversalKeysEnabled();
  }

  public void requestFocus(Cause cause) {
    wrapped.requestFocus(cause);
  }

  public boolean requestFocusInWindow(Cause cause) {
    return wrapped.requestFocusInWindow(cause);
  }

  public Container getFocusCycleRootAncestor() {
    return wrapped.getFocusCycleRootAncestor();
  }

  public void transferFocus() {
    wrapped.transferFocus();
  }

  public void nextFocus() {
    wrapped.nextFocus();
  }

  public void transferFocusBackward() {
    wrapped.transferFocusBackward();
  }

  public void transferFocusUpCycle() {
    wrapped.transferFocusUpCycle();
  }

  public boolean hasFocus() {
    return wrapped.hasFocus();
  }

  public boolean isFocusOwner() {
    return wrapped.isFocusOwner();
  }

  public void add(PopupMenu popup) {
    wrapped.add(popup);
  }

  public void remove(MenuComponent popup) {
    wrapped.remove(popup);
  }

  public String toString() {
    return wrapped.toString();
  }

  public void list() {
    wrapped.list();
  }

  public void list(PrintStream out) {
    wrapped.list(out);
  }

  public void list(PrintWriter out) {
    wrapped.list(out);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    wrapped.removePropertyChangeListener(listener);
  }

  public PropertyChangeListener[] getPropertyChangeListeners() {
    return wrapped.getPropertyChangeListeners();
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    wrapped.removePropertyChangeListener(propertyName, listener);
  }

  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    return wrapped.getPropertyChangeListeners(propertyName);
  }

  public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    wrapped.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void setComponentOrientation(ComponentOrientation o) {
    wrapped.setComponentOrientation(o);
  }

  public ComponentOrientation getComponentOrientation() {
    return wrapped.getComponentOrientation();
  }

  public AccessibleContext getAccessibleContext() {
    return wrapped.getAccessibleContext();
  }

  public void setMixingCutoutShape(Shape shape) {
    wrapped.setMixingCutoutShape(shape);
  }

  private ZoneRenderer wrapped;
  private EventDispatcher eventDispatcher;
  
  DelegateZoneRenderer (ZoneRenderer zoneRenderer, EventDispatcher eventDispatcher) {
    super(zoneRenderer.getZone());
    this.wrapped = zoneRenderer;
    this.eventDispatcher = eventDispatcher;
  }
  
  
}