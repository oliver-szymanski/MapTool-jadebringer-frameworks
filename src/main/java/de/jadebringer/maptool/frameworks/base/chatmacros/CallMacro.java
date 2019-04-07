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
package de.jadebringer.maptool.frameworks.base.chatmacros;

import de.jadebringer.maptool.extensionframework.ExtensionChatMacro;
import java.security.AccessController;
import java.security.PrivilegedAction;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolLineParser;
import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.MacroContext;
import net.rptools.maptool.client.macro.MacroDefinition;
import net.rptools.parser.ParserException;

/**
 * Macro to support calling macro function as /call function [params] chat command
 *
 * @author oliver.szymanski
 */
@MacroDefinition(
  name = "call",
  aliases = {"call"},
  description = "call.description",
  expandRolls = false
)
public class CallMacro extends ExtensionChatMacro {

  public CallMacro() {
    super(false);
  }

  public void run(MacroContext context, String code, MapToolMacroContext executionContext) {
    if (code == null || code.trim().length() == 0) {
      return;
    }
    if (code.indexOf("(") < 0) {
      code = code + "()";
    }

    final String executionCode = "[r: " + code + "]";

    if (executionContext == null) {
      executionContext =
          new MapToolMacroContext(
              MapToolLineParser.CHAT_INPUT,
              MapToolLineParser.CHAT_INPUT,
              MapTool.getPlayer().isGM());
    }
    MapTool.getParser().enterContext(executionContext);

    try {
      String result;
      try {
        result =
            AccessController.doPrivileged(
                (PrivilegedAction<String>)
                    () -> {
                      try {
                        return MapTool.getParser().parseLine(executionCode);
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    });

      } catch (Throwable e) {
        throw new ParserException(e);
      }

      if (result != null && result.length() > 0) MapTool.addLocalMessage(result.toString());
    } catch (ParserException e) {
      MapTool.showError(null, e);
    } finally {
      MapTool.getParser().exitContext();
    }
  }
}
