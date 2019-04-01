/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.functions;

import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class MacroFunctions extends ExtensionFunction {
	public MacroFunctions() {
		super(true, 
		    Alias.create("executeMT"),
		    Alias.create("executeMacro", 1, 4),
        Alias.create("executeMacroSendOutput", 1, 4),
		    Alias.create("sendExecuteMacro", 3, 6));
	}

	private final static MacroFunctions instance = new MacroFunctions();

	public static MacroFunctions getInstance() {
		return instance;
	}

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

	  
	 // try {
//      return AccessController.doPrivileged(
  //        new PrivilegedExceptionAction<>() {
    //        public Object run() throws Exception {
      if ("executeMT".equals(functionName)) {
        return executeMT(parser, parameters);
      } else if ("executeMacro".equals(functionName)) {
        return executeMacro(parser, parameters, true);
      } else if ("executeMacroSendOutput".equals(functionName)) {
        return executeMacro(parser, parameters, false);
      } else if ("sendExecuteMacro".equals(functionName)) {
        sendExecuteMacro(parser, parameters);
        return "";
      }
      throw new ParserException("non existing function: " + functionName);
        //    }
          //});
    //} catch (PrivilegedActionException e) {
      //e.printStackTrace();
     // throw new ParserException(e);
   // }
	  
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
  
  private Object executeMacro(Parser parser, List<Object> parameters, boolean omitSendingOutput) throws ParserException {
    String linkTo = FunctionCaller.getParam(parameters, 0);
    String who = FunctionCaller.getParam(parameters, 1, "self");
    String args = FunctionCaller.getParam(parameters, 2);
    String target = FunctionCaller.getParam(parameters, 3, "impersonated"); 
    
    LinkFunctions links = LinkFunctions.getInstance();
    Object link = links.createLink(parser, linkTo, who, args, target);
    Object result = links.execLink(link.toString(), parser, omitSendingOutput);
    if (!omitSendingOutput) { return ""; }
    return result;
  }
  
  private void sendExecuteMacro(Parser parser, List<Object> parameters) throws ParserException {
    String sendToWho = FunctionCaller.getParam(parameters, 0);
    String linkTitle = FunctionCaller.getParam(parameters, 1);
    String clickableLinkTitle = FunctionCaller.getParam(parameters, 2);
    String linkTo = FunctionCaller.getParam(parameters, 3);
    String who = FunctionCaller.getParam(parameters, 4, "self");
    String args = FunctionCaller.getParam(parameters, 5);
    String target = FunctionCaller.getParam(parameters, 6, "impersonated");
    
    LinkFunctions links = LinkFunctions.getInstance();
    Object link = links.createLink(parser, linkTo, who, args, target);
    Object anchor = links.createAnchor(parser, clickableLinkTitle, link.toString(), null,  null, null);    

    if (linkTitle == null) { linkTitle = "%link%"; }
    if (!linkTitle.contains("%link%")) { linkTitle = linkTitle + " %link%"; }
    linkTitle = linkTitle.replace("%link%", anchor.toString());
    OutputToFunction.getInstance().outputTo(parser, sendToWho, linkTitle, false, "impersonated");
  }

}
