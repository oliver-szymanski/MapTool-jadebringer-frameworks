package de.jadebringer.maptool.frameworks.base;

import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.chatmacros.CallMacro;
import de.jadebringer.maptool.frameworks.base.chatmacros.FrameworksMacro;
import de.jadebringer.maptool.frameworks.base.functions.DebugFunctions;
import de.jadebringer.maptool.frameworks.base.functions.InputFunctions;
import de.jadebringer.maptool.frameworks.base.functions.LinkFunctions;
import de.jadebringer.maptool.frameworks.base.functions.OutputToFunction;
import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import de.jadebringer.maptool.frameworks.base.functions.SettingsFunctions;
import net.rptools.maptool.client.functions.FrameworksFunctions.Framework;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.parser.function.Function;

public class BaseFramework implements Framework {
	
	private List<Function> functions = new LinkedList<>();
	private List<Macro> chatMacros = new LinkedList<>();

	public BaseFramework() {
		functions.add(InputFunctions.getInstance());
		functions.add(PingFunction.getInstance());
		functions.add(LinkFunctions.getInstance());
		functions.add(OutputToFunction.getInstance());
		functions.add(SettingsFunctions.getInstance());
		functions.add(DebugFunctions.getInstance());
    
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
