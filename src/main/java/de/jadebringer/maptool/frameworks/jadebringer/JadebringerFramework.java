package de.jadebringer.maptool.frameworks.jadebringer;

import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.jadebringer.functions.PingFunction;
import net.rptools.maptool.client.functions.FrameworksFunctions.Framework;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.parser.function.Function;

public class JadebringerFramework implements Framework {
	
	private List<Function> functions = new LinkedList<Function>();
	private List<Macro> chatMacros = new LinkedList<>();
	
	public JadebringerFramework() {
		functions.add(PingFunction.getInstance());
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
