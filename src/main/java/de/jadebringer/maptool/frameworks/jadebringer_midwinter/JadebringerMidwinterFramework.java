package de.jadebringer.maptool.frameworks.jadebringer_midwinter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionChatMacro;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFrameworkBundle;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;
import net.rptools.maptool.client.functions.frameworkfunctions.Version;

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
