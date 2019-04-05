package de.jadebringer.maptool.frameworks.jadebringer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import de.jadebringer.maptool.frameworks.jadebringer.functions.JadebringerSettingsFunctions;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionChatMacro;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFrameworkBundle;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.maptool.client.functions.frameworkfunctions.Version;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

public class JadebringerFramework extends ExtensionFrameworkBundle {
	
	private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
	private List<ExtensionChatMacro> chatMacros = new LinkedList<>();
	
	public JadebringerFramework() {
	  super(new Version(1,0,0,"jadebringer"));
		functions.add(PingFunction.getInstance(this));
    functions.add(JadebringerSettingsFunctions.getInstance());
		
    functionButtons.add(new ExtensionFunctionButton("godmode", "godmode", "toggle godmode", "settings", "GM", "/images/hand-of-god.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction("toggleGodMode", JadebringerSettingsFunctions.getInstance(), parser);
        MapTool.addLocalMessage("jadebringer.godmode.enabled = " +  result);
      }     
    });

	}
	
	@Override
	public List<ExtensionFunction> getFunctions() {
		return functions;
	}
	
	@Override
	public List<ExtensionChatMacro> getChatMacros() {
		return chatMacros;
	}

  @Override
  public Collection<? extends ExtensionFunctionButton> getFunctionButtons() {
    return functionButtons;
  }
}
