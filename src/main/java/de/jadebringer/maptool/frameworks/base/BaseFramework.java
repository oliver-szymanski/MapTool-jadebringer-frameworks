package de.jadebringer.maptool.frameworks.base;

import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.chatmacros.CallMacro;
import de.jadebringer.maptool.frameworks.base.chatmacros.FrameworksMacro;
import de.jadebringer.maptool.frameworks.base.functions.InputFunction;
import de.jadebringer.maptool.frameworks.base.functions.LinkFunction;
import de.jadebringer.maptool.frameworks.base.functions.OutputToFunction;
import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import de.jadebringer.maptool.frameworks.base.functions.SettingsFunction;
import net.rptools.maptool.client.functions.FrameworksFunctions.Framework;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.parser.function.Function;

public class BaseFramework implements Framework {
	
	private List<Function> functions = new LinkedList<>();
	private List<Macro> chatMacros = new LinkedList<>();

	public BaseFramework() {
		functions.add(InputFunction.getInstance());
		functions.add(PingFunction.getInstance());
		functions.add(LinkFunction.getInstance());
		functions.add(OutputToFunction.getInstance());
		functions.add(SettingsFunction.getInstance());
		
		chatMacros.add(new FrameworksMacro());
		chatMacros.add(new CallMacro());
	}
	
	@Override
	public List<Function> getFunctions() {
		return functions;
	}
	
	@Override
	public List<Macro> getChatMacros() {
		return chatMacros;
	}
	
}
