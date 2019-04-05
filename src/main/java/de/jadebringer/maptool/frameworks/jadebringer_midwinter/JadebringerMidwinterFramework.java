package de.jadebringer.maptool.frameworks.jadebringer_midwinter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.extensionframework.ExtensionChatMacro;
import de.jadebringer.maptool.extensionframework.ExtensionFrameworkBundle;
import de.jadebringer.maptool.extensionframework.ExtensionFunction;
import de.jadebringer.maptool.extensionframework.ExtensionFunctionButton;
import de.jadebringer.maptool.extensionframework.Version;
import de.jadebringer.maptool.frameworks.base.functions.PingFunction;

public class JadebringerMidwinterFramework extends ExtensionFrameworkBundle {
	
	private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
  private List<ExtensionChatMacro> chatMacros = new LinkedList<>();
	
	public JadebringerMidwinterFramework() {
	  super(new Version(1,0,0,"jadebringer-midwinter"));
		functions.add(PingFunction.getInstance(this));
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
