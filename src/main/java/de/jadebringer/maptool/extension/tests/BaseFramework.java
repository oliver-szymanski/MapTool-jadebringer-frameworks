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
package de.jadebringer.maptool.extension.tests;

import de.jadebringer.maptool.extension.hook.ExtensionChatMacro;
import de.jadebringer.maptool.extension.hook.ExtensionFrameworkBundle;
import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import de.jadebringer.maptool.extension.hook.ExtensionFunctionButton;
import de.jadebringer.maptool.extension.hook.Version;
import de.jadebringer.maptool.extension.tests.functions.TrySecurity;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BaseFramework extends ExtensionFrameworkBundle {

  private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
  private List<ExtensionChatMacro> chatMacros = new LinkedList<>();

  public BaseFramework() {
    super(new Version(1, 0, 0, "jadebringer-test"));

    functions.add(TrySecurity.getInstance());
  }

  @Override
  public List<ExtensionFunction> getFunctions() {
    return functions;
  }

  @Override
  public List<ExtensionChatMacro> getChatMacros() {
    return chatMacros;
  }

  @Override
  public Collection<? extends ExtensionFunctionButton> getFunctionButtons() {
    return functionButtons;
  }
}
