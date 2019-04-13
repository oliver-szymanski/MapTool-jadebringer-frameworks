/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.extension.base.functions;

import java.util.List;
import java.util.stream.Collectors;

import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import de.jadebringer.maptool.extension.hook.FunctionCaller;
import de.jadebringer.maptool.extension.hook.FunctionCaller.TokenWrapper;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class TokenFunctions extends ExtensionFunction {
  
	protected TokenFunctions() {
		super( 
		    Alias.create("findTokens", 0, 3)
		    );
		setTrustedRequired(false);
	}
	
	private static final TokenFunctions instance = new TokenFunctions();
	
	public static TokenFunctions getInstance() {
    return instance;
  }

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

	  if ("tokens_findTokens".equals(functionName)) {
	    boolean fullJsonObject = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 3, false));
	    if (fullJsonObject) 
	      return findTokens(parser, functionName, parameters).stream().map(
	          t -> t.toJsonObjectFull()
	          ).collect(Collectors.toList());
	    else
	      return findTokens(parser, functionName, parameters).stream().map(
  	      t -> t.toJsonObject()
  	      ).collect(Collectors.toList());
	  }
	  
	  throw new ParserException("non existing function: " + functionName);
	}
	
	public List<TokenWrapper> findTokens(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String nameRegEx = FunctionCaller.getParam(parameters, 0, "");
    String zoneRegEx = FunctionCaller.getParam(parameters, 1, "");
    boolean libOnly = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 2, false));
    return FunctionCaller.findTokens(nameRegEx, zoneRegEx, libOnly);
	}


}
