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
package de.jadebringer.maptool.extension.base.functions;

import java.util.List;

import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import de.jadebringer.maptool.extension.hook.FunctionCaller;
import net.rptools.maptool.client.MapTool;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/** @author oliver.szymanski */
public class MacrosFunctions extends ExtensionFunction {
  public static final String MACROS_SEND_EXECUTE_MACRO = "macros_sendExecuteMacro";
  public static final String MACROS_EXECUTE_MACRO_SEND_OUTPUT = "macros_executeMacroSendOutput";
  public static final String MACROS_EXECUTE_MACRO = "macros_executeMacro";
  public static final String MACROS_EXECUTE_MT = "macros_executeMT";

  public MacrosFunctions() {
    super(
        Alias.create(MACROS_EXECUTE_MT),
        Alias.create(MACROS_EXECUTE_MACRO, 1, 4),
        Alias.create(MACROS_EXECUTE_MACRO_SEND_OUTPUT, 1, 4),
        Alias.create(MACROS_SEND_EXECUTE_MACRO, 3, 6));
    setTrustedRequired(true);
  }

  private static final MacrosFunctions instance = new MacrosFunctions();

  public static MacrosFunctions getInstance() {
    return instance;
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if (MACROS_EXECUTE_MT.equals(functionName)) {
      return executeMT(parser, parameters);
    } else if (MACROS_EXECUTE_MACRO.equals(functionName)) {
      return executeMacro(parser, true, parameters);
    } else if (MACROS_EXECUTE_MACRO_SEND_OUTPUT.equals(functionName)) {
      return executeMacro(parser, false, parameters);
    } else if (MACROS_SEND_EXECUTE_MACRO.equals(functionName)) {
      sendExecuteMacro(parser, parameters);
      return "";
    }

    return throwNotFoundParserException(functionName);
  }

  public Object executeMT(Parser parser, List<Object> parameters) throws ParserException {
    StringBuilder result = new StringBuilder();

    try {
      if (parameters != null) {
        for (Object codeSnippet : parameters) {
          String expression = MapTool.getParser().parseLine(codeSnippet.toString());
          result.append(expression);
        }
      }
    } catch (Exception e) {
      if (!(e instanceof ParserException)) {
        throw new ParserException(e);
      } else {
        throw e;
      }
    }

    return result.toString();
  }

  public Object executeMacro(Parser parser, boolean omitSendingOutput, List<Object> parameters)
      throws ParserException {
    String linkTo = FunctionCaller.getParam(parameters, 0);
    String who = FunctionCaller.getParam(parameters, 1, "self");
    String args = FunctionCaller.getParam(parameters, 2);
    String target = FunctionCaller.getParam(parameters, 3, "impersonated");

    LinkFunctions links = LinkFunctions.getInstance();
    Object link = links.createLink(parser, linkTo, who, args, target);
    Object result = links.execLink(link.toString(), parser, omitSendingOutput);
    if (omitSendingOutput) {
      return result;
    }
    return "";
  }

  public void sendExecuteMacro(Parser parser, List<Object> parameters) throws ParserException {
    String sendToWho = FunctionCaller.getParam(parameters, 0);
    String message = FunctionCaller.getParam(parameters, 1);
    String clickableLinkTitle = FunctionCaller.getParam(parameters, 2);
    String linkTo = FunctionCaller.getParam(parameters, 3);
    String who = FunctionCaller.getParam(parameters, 4, "self");
    String args = FunctionCaller.getParam(parameters, 5);
    String target = FunctionCaller.getParam(parameters, 6, "impersonated");

    LinkFunctions links = LinkFunctions.getInstance();
    Object link = links.createLink(parser, linkTo, who, args, target);
    Object anchor =
        links.createAnchor(parser, clickableLinkTitle, link.toString(), null, null, null);

    if (message == null) {
      message = "%link%";
    }
    if (!message.contains("%link%")) {
      message = message + " %link%";
    }
    message = message.replace("%link%", anchor.toString());
    OutputToFunction.getInstance().outputTo(parser, sendToWho, message, false, "impersonated");
  }
}
