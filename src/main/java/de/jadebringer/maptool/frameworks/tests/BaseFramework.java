package de.jadebringer.maptool.frameworks.tests;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.extensionframework.ExtensionChatMacro;
import de.jadebringer.maptool.extensionframework.ExtensionFrameworkBundle;
import de.jadebringer.maptool.extensionframework.ExtensionFunction;
import de.jadebringer.maptool.extensionframework.ExtensionFunctionButton;
import de.jadebringer.maptool.extensionframework.FunctionCaller;
import de.jadebringer.maptool.extensionframework.Version;
import de.jadebringer.maptool.frameworks.base.chatmacros.CallMacro;
import de.jadebringer.maptool.frameworks.base.chatmacros.FrameworksMacro;
import de.jadebringer.maptool.frameworks.base.functions.DebugFunctions;
import de.jadebringer.maptool.frameworks.base.functions.ButtonFrameFunctions;
import de.jadebringer.maptool.frameworks.base.functions.ContentFunctions;
import de.jadebringer.maptool.frameworks.base.functions.InputFunctions;
import de.jadebringer.maptool.frameworks.base.functions.LinkFunctions;
import de.jadebringer.maptool.frameworks.base.functions.MacrosFunctions;
import de.jadebringer.maptool.frameworks.base.functions.MapFunctions;
import de.jadebringer.maptool.frameworks.base.functions.OutputToFunction;
import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import de.jadebringer.maptool.frameworks.base.functions.SettingsFunctions;
import de.jadebringer.maptool.frameworks.tests.functions.TrySecurity;
import net.rptools.maptool.client.MapTool;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

public class BaseFramework extends ExtensionFrameworkBundle {
	
	private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
	private List<ExtensionChatMacro> chatMacros = new LinkedList<>();
	
	public BaseFramework() {
	  super(new Version(1,0,0,"jadebringer-test"));
	  
	  functions.add(TrySecurity.getInstance());
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
