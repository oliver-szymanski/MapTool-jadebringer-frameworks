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

import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class OutputToFunction extends ExtensionFunction {
	public OutputToFunction() {
		super(false, Alias.create("outputTo", 2, 4));
	}

	private final static OutputToFunction instance = new OutputToFunction();

	public static OutputToFunction getInstance() {
		return instance;
	}

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

		String who = FunctionCaller.getParam(parameters, 0, "GM");
		String message = FunctionCaller.getParam(parameters, 1, "");
		Object defer = FunctionCaller.getParam(parameters, 2, BigDecimal.ZERO);
		String target = FunctionCaller.getParam(parameters, 3, "impersonated");
		outputTo(parser, who, message, FunctionCaller.toBoolean(defer), target);
		return "";
	}

	public void outputTo(Parser parser, String who, String message) throws ParserException {
		outputTo(parser, who, message, false, null);
	}
		
	public void outputTo(Parser parser, String who, String message, boolean defer, String target) throws ParserException {
	  LinkFunctions linkFunction = LinkFunctions.getInstance();
		String callbackFunction = "unpackArgs";
		//if (prefix != null) callbackFunction = prefix + callbackFunction;
		String link = (String)linkFunction.createLink(parser, callbackFunction, who, message, target);
		FunctionCaller.callFunction("execLink", linkFunction, parser, link, FunctionCaller.toBoolean(defer));
	}

}
