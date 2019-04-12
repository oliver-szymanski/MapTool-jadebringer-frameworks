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
package de.jadebringer.maptool.extension.hook;

import java.util.Collection;

public abstract class ExtensionBundle {

  private Version version;

  public ExtensionBundle(Version version) {
    this.version = version;
  }

  public Version version() {
    return version;
  }

  public String name() {
    return this.getClass().getName();
  }

  public abstract Collection<? extends ExtensionFunction> getFunctions();

  public abstract Collection<? extends ExtensionFunctionButton> getFunctionButtons();

  public abstract Collection<? extends ExtensionChatMacro> getChatMacros();
}
