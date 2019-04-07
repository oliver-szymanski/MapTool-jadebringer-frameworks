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
package de.jadebringer.maptool.extensionframework.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameDragListener extends MouseAdapter {

  private final Component frameToMove;

  private boolean enabled;
  private Point mouseDownCompCoords = null;

  public FrameDragListener(Component frameToMove) {
    this.frameToMove = frameToMove;
    this.enabled = true;
  }

  public void mouseReleased(MouseEvent e) {
    mouseDownCompCoords = null;
  }

  public void mousePressed(MouseEvent e) {
    mouseDownCompCoords = e.getPoint();
  }

  public void mouseDragged(MouseEvent e) {
    if (enabled) {
      Point currCoords = e.getLocationOnScreen();
      frameToMove.setLocation(
          currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
