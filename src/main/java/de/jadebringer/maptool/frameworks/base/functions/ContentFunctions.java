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
package de.jadebringer.maptool.frameworks.base.functions;

import de.jadebringer.maptool.extensionframework.ExtensionFunction;
import de.jadebringer.maptool.extensionframework.FunctionCaller;
import de.jadebringer.maptool.extensionframework.FunctionCaller.TokenWrapper;
import java.math.BigDecimal;
import java.util.List;
import net.rptools.maptool.model.MacroButtonProperties;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/** @author oliver.szymanski */
public class ContentFunctions extends ExtensionFunction {

  public static final String CONTENT_SAVE = "content_save";
  public static final String CONTENT_LOAD = "content_load";

  protected ContentFunctions() {
    super(Alias.create(CONTENT_LOAD, 3, 3), Alias.create(CONTENT_SAVE, 4, 4));
    this.setTrustedRequired(true);
  }

  private static final ContentFunctions instance = new ContentFunctions();

  public static ContentFunctions getInstance() {
    return instance;
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if (CONTENT_LOAD.equals(functionName)) {
      return loadContent(parser, parameters);
    } else if (CONTENT_SAVE.equals(functionName)) {
      return saveContent(parser, parameters);
    }

    return throwNotFoundParserException(functionName);
  }

  private Object loadContent(Parser parser, List<Object> parameters) throws ParserException {

    String name = FunctionCaller.getParam(parameters, 0);
    String sourceType = FunctionCaller.getParam(parameters, 1);
    String source = FunctionCaller.getParam(parameters, 2);

    if ("tokenMacro".equalsIgnoreCase(sourceType)) {
      TokenWrapper token;
      if (source == null) {
        token = FunctionCaller.getCurrentToken(parser);
        source = token.getToken().getId().toString();
      } else {
        token = FunctionCaller.findToken(source, null, true);
        source = token.getToken().getId().toString();
      }

      // false => even get when player does not own token
      List<MacroButtonProperties> macros = token.getToken().getMacroList(false);
      for (MacroButtonProperties macro : macros) {
        if (macro.getLabel().equals(name)) {
          return macro.getCommand();
        }
      }
    } else if ("tokenProperty".equalsIgnoreCase(sourceType)) {
      return SettingsFunctions.getInstance().getSetting(parser, name, "", source);
    } else if ("table".equalsIgnoreCase(sourceType)) {
      return FunctionCaller.callFunction("table", parser, source, name);
    }

    return "";
  }

  private Object saveContent(Parser parser, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String sourceType = FunctionCaller.getParam(parameters, 1);
    String source = FunctionCaller.getParam(parameters, 2);
    String content = FunctionCaller.getParam(parameters, 3);

    if ("tokenMacro".equalsIgnoreCase(sourceType)) {
      TokenWrapper token;
      if (source == null) {
        token = FunctionCaller.getCurrentToken(parser);
        source = token.getToken().getId().toString();
      } else {
        token = FunctionCaller.findToken(source, null, true);
        source = token.getToken().getId().toString();
      }

      // false => even get when player does not own token
      List<MacroButtonProperties> macros = token.getToken().getMacroList(false);
      for (MacroButtonProperties macro : macros) {
        if (macro.getLabel().equals(name)) {
          macro.setCommand(content);
        }
      }
    } else if ("tokenProperty".equalsIgnoreCase(sourceType)) {
      return SettingsFunctions.getInstance().setSetting(parser, name, content, source);
    } else if ("table".equalsIgnoreCase(sourceType)) {
      return FunctionCaller.callFunction("setTableEntry", parser, source, name, content);
    }

    return BigDecimal.ONE;
  }
}
