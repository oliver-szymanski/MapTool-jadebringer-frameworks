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
import net.rptools.maptool.client.functions.frameworkfunctions.FrameworksFunctions;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class ButtonFrameFunctions extends ExtensionFunction {
  
  protected ButtonFrameFunctions() {
		super(false, 
		    Alias.create("showAllFrames", 0, 0), 
		    Alias.create("hideAllFrames", 0, 0),
        Alias.create("showFrame"), 
        Alias.create("hideFrame"),
		    Alias.create("isFrameVisible", 1, 1));
	}
	
  private static final ButtonFrameFunctions instance = new ButtonFrameFunctions();
  
	public static ButtonFrameFunctions getInstance() {
    return instance;
  }

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

	  if("showFrame".equals(functionName)) {
	    return showFrame(parser, functionName, parameters);
	  } if("hideFrame".equals(functionName)) {
	    return hideFrame(parser, functionName, parameters);
	  } else if("showAllFrames".equals(functionName)) {
      showAllFrames(parser, functionName, parameters);
      return BigDecimal.ONE;
    } if("hideAllFrames".equals(functionName)) {
      hideAllFrames(parser, functionName, parameters);
      return BigDecimal.ONE;
    } if("isFrameVisible".equals(functionName)) {
      return isFrameVisible(parser, functionName, parameters);
    }
	  
    throw new ParserException("non existing function: " + functionName);
	}
	
	private Object isFrameVisible(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String frame = FunctionCaller.getParam(parameters, 0);
    boolean result =  FrameworksFunctions.getInstance().isFrameVisible(frame);
    return (result) ? BigDecimal.ONE : BigDecimal.ZERO;
  }
	
	private Object showFrame(Parser parser, String functionName, List<Object> parameters) throws ParserException {
	  if (parameters == null) { return false; }
	  boolean result = true;
	  for(Object parameter : parameters) {
	    if (!FrameworksFunctions.getInstance().showFrame(parameter.toString())) {
	      result = false;
	    }
	  }
	  return (result) ? BigDecimal.ONE : BigDecimal.ZERO;
	}

	private Object hideFrame(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    if (parameters == null) { return false; }
	  boolean result = true;
    for(Object parameter : parameters) {
      if (!FrameworksFunctions.getInstance().hideFrame(parameter.toString())) {
        result = false;
      }
    }
    return (result) ? BigDecimal.ONE : BigDecimal.ZERO;
	}
	
	private void showAllFrames(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    FrameworksFunctions.getInstance().showAllFrames();
  }

  private void hideAllFrames(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    FrameworksFunctions.getInstance().hideAllFrames();
  }


}
