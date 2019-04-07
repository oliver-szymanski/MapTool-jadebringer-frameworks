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

import com.jidesoft.combobox.PopupPanel;

// needed to change the popup for properties
public class MultilineStringExComboBox extends com.jidesoft.combobox.MultilineStringExComboBox {

  private static final long serialVersionUID = -4925038979207533627L;

  public PopupPanel createPopupComponent() {
    MultilineStringPopupPanel pp = new MultilineStringPopupPanel();
    return pp;
  }
}
