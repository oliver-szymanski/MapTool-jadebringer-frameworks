/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.functions;

import java.math.BigDecimal;
import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class MacroFunctions extends ExtensionFunction {
	public MacroFunctions() {
		super(true, Alias.create("executeMT"));
	}

	private final static MacroFunctions instance = new MacroFunctions();

	public static MacroFunctions getInstance() {
		return instance;
	}

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

	  if ("executeMT".equals(functionName)) {
  	  return executeMT(parser, parameters);
	  }
	  
	  return BigDecimal.ZERO;
	}

  private Object executeMT(Parser parser, List<Object> parameters) throws ParserException {
    StringBuilder result = new StringBuilder();
    
    try {
      if (parameters != null) {
        for(Object codeSnippet : parameters) {
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

}
