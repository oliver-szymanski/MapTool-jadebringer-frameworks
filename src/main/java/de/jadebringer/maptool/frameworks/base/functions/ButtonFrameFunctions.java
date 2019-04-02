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
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;
import net.rptools.maptool.client.functions.frameworkfunctions.FrameworksFunctions;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.maptool.client.functions.frameworkfunctions.ui.ButtonFrame;
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
		    Alias.create("isFrameVisible", 1, 1),
		    Alias.create("addButton", 9, 9),
        Alias.create("removeButton", 4, 4),
        Alias.create("showButton", 4, 4),
        Alias.create("hideButton", 4, 4),
        Alias.create("enableButton", 4, 4),
        Alias.create("disableButton", 4, 4),
        Alias.create("isHiddenButton", 4, 4),
        Alias.create("isEnabledButton", 4, 4)
		    );
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
    } if("addButton".equals(functionName)) {
      return addButton(parser, functionName, parameters);
    } if("removeButton".equals(functionName)) {
      return removeButton(parser, functionName, parameters);
    } if("hideButton".equals(functionName)) {
      return hideButton(parser, functionName, parameters);
    } if("showButton".equals(functionName)) {
      return showButton(parser, functionName, parameters);
    } if("enableButton".equals(functionName)) {
      return enableButton(parser, functionName, parameters);
    } if("disableButton".equals(functionName)) {
      return disableButton(parser, functionName, parameters);
    } if("isHiddenButton".equals(functionName)) {
      return isHiddenButton(parser, functionName, parameters);
    } if("isEnabledButton".equals(functionName)) {
      return isEnabledButton(parser, functionName, parameters);
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

  public Object addButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String macroName = FunctionCaller.getParam(parameters, 0);
    String name = FunctionCaller.getParam(parameters, 1);
    String tooltip = FunctionCaller.getParam(parameters, 2);
    String group = FunctionCaller.getParam(parameters, 3);
    String frame = FunctionCaller.getParam(parameters, 4);
    String prefix = FunctionCaller.getParam(parameters, 5);
    String imageFile = FunctionCaller.getParam(parameters, 6);
    boolean frameAndImage = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 7));
    boolean trustedRequired = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 8));
        
    ExtensionFunctionButton extensionFunctionButton = new ExtensionFunctionButton(name, tooltip, group, frame, imageFile, frameAndImage, trustedRequired) {
      @Override
      public void run(Parser parser) throws ParserException{
        StringBuffer macroArgs = new StringBuffer();
        macroArgs.append(frame).append(".").append(group).append(".").append("name");
        MacroFunctions.getInstance().executeMacro(parser, false, FunctionCaller.toObjectList(macroName, "self", macroArgs.toString(), "impersonated"));
      }   
    };
    
    FrameworksFunctions.getInstance().addExtensionFunctionButton(extensionFunctionButton);
    
    return BigDecimal.ONE;
  }
  
  public Object showButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    buttonFrame.show(extensionFunctionButton);
    return BigDecimal.ONE;
  }
  
  public Object hideButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    buttonFrame.hide(extensionFunctionButton);
    return BigDecimal.ONE;
  }
  
  public Object removeButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    buttonFrame.remove(extensionFunctionButton);
    return BigDecimal.ONE;
  }
  
  public Object enableButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    buttonFrame.enable(extensionFunctionButton);
    return BigDecimal.ONE;
  }
  
  public Object disableButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    buttonFrame.disable(extensionFunctionButton);
    return BigDecimal.ONE;
  }
  
  public Object isHiddenButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    return FunctionCaller.fromBoolean(buttonFrame.isHidden(extensionFunctionButton));
  }
  
  public Object isEnabledButton(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton = FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) { return BigDecimal.ZERO; }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) { return BigDecimal.ZERO; }
    return FunctionCaller.fromBoolean(buttonFrame.isEnabled(extensionFunctionButton));
  }
}
