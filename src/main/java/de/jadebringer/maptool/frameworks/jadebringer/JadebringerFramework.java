package de.jadebringer.maptool.frameworks.jadebringer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionChatMacro;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFrameworkBundle;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;

public class JadebringerFramework extends ExtensionFrameworkBundle {
	
	private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
	private List<ExtensionChatMacro> chatMacros = new LinkedList<>();
	
	public JadebringerFramework() {
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
