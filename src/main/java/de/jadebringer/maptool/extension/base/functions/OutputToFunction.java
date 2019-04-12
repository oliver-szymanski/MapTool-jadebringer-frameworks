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

import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import de.jadebringer.maptool.extension.hook.FunctionCaller;
import java.math.BigDecimal;
import java.util.List;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/** @author oliver.szymanski */
public class OutputToFunction extends ExtensionFunction {
  public static final String OUTPUT_TO = "outputTo";

  public OutputToFunction() {
    super(Alias.create(OUTPUT_TO, 2, 4));
    setTrustedRequired(false);
  }

  private static final OutputToFunction instance = new OutputToFunction();

  public static OutputToFunction getInstance() {
    return instance;
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if (OUTPUT_TO.equals(functionName)) {
      String who = FunctionCaller.getParam(parameters, 0, "GM");
      String message = FunctionCaller.getParam(parameters, 1, "");
      Object defer = FunctionCaller.getParam(parameters, 2, BigDecimal.ZERO);
      String target = FunctionCaller.getParam(parameters, 3, "impersonated");
      outputTo(parser, who, message, FunctionCaller.toBoolean(defer), target);
      return "";
    }

    return throwNotFoundParserException(functionName);
  }

  public void outputTo(Parser parser, String who, String message) throws ParserException {
    outputTo(parser, who, message, false, null);
  }

  public void outputTo(Parser parser, String who, String message, boolean defer, String target)
      throws ParserException {
    LinkFunctions linkFunction = LinkFunctions.getInstance();
    String callbackFunction = "unpackArgs";
    String link = (String) linkFunction.createLink(parser, callbackFunction, who, message, target);
    FunctionCaller.callFunction(
        "links_execLink", linkFunction, parser, link, FunctionCaller.toBoolean(defer));
  }
}
