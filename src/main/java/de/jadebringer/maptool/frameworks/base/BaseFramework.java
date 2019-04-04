package de.jadebringer.maptool.frameworks.base;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.chatmacros.CallMacro;
import de.jadebringer.maptool.frameworks.base.chatmacros.FrameworksMacro;
import de.jadebringer.maptool.frameworks.base.functions.DebugFunctions;
import de.jadebringer.maptool.frameworks.base.functions.ButtonFrameFunctions;
import de.jadebringer.maptool.frameworks.base.functions.ContentFunctions;
import de.jadebringer.maptool.frameworks.base.functions.InputFunctions;
import de.jadebringer.maptool.frameworks.base.functions.LinkFunctions;
import de.jadebringer.maptool.frameworks.base.functions.MacrosFunctions;
import de.jadebringer.maptool.frameworks.base.functions.OutputToFunction;
import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import de.jadebringer.maptool.frameworks.base.functions.SettingsFunctions;
import de.jadebringer.maptool.frameworks.base.functions.TrySecurity;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionChatMacro;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFrameworkBundle;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.maptool.client.functions.frameworkfunctions.Version;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

public class BaseFramework extends ExtensionFrameworkBundle {
	
	private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
	private List<ExtensionChatMacro> chatMacros = new LinkedList<>();
	
	public BaseFramework() {
	  super(new Version(1,0,0,"jadebringer"));
	  
	  PingFunction ping = PingFunction.getInstance(this);
	  
		functions.add(InputFunctions.getInstance());
		functions.add(ping);
		functions.add(LinkFunctions.getInstance());
		functions.add(OutputToFunction.getInstance());
		functions.add(SettingsFunctions.getInstance());
		functions.add(DebugFunctions.getInstance());
    functions.add(MacrosFunctions.getInstance());
    functions.add(ButtonFrameFunctions.getInstance());
    functions.add(ContentFunctions.getInstance());
    functions.add(TrySecurity.getInstance());
    
		chatMacros.add(new FrameworksMacro());
		chatMacros.add(new CallMacro());
		
    functionButtons.add(new ExtensionFunctionButton("list settings", "list setting", "settings", "setup", "/images/settings-knobs.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction("listSettings", SettingsFunctions.getInstance(), parser);
        if (result != null) { MapTool.addLocalMessage(result.toString()); }
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("get setting", "get setting", "settings", "setup", "/images/read.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException{
        Object key = FunctionCaller.askForVariable(parser, "key", null);
        if (key == null) { return; }
        Object result = FunctionCaller.callFunction("getSetting", SettingsFunctions.getInstance(), parser, key);
        if (result != null) { MapTool.addLocalMessage(key + " = " + result.toString()); }
      }     
    });
		functionButtons.add(new ExtensionFunctionButton("set setting", "set setting", "settings", "setup", "/images/pencil.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object key = FunctionCaller.askForVariable(parser, "key", null);
        Object value = FunctionCaller.askForVariable(parser, "value", null);
        if (key == null) { return; }
        FunctionCaller.callFunction("setSetting", SettingsFunctions.getInstance(), parser, key, value);
        MapTool.addLocalMessage(key + " = " + value);
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("delete setting", "delete setting", "settings", "setup", "/images/alligator-clip.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object key = FunctionCaller.askForVariable(parser, "key", null);
        if (key == null) { return; }
        FunctionCaller.callFunction("deleteSetting", SettingsFunctions.getInstance(), parser, key);
        MapTool.addLocalMessage(key + " deleted.");
      }     
    });

    functionButtons.add(new ExtensionFunctionButton("debug", "toggle debug", "debug", "setup", "/images/spotted-bug.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction("toggleDebug", DebugFunctions.getInstance(), parser);
        MapTool.addLocalMessage("debug.enabled = " +  result);
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("trace", "toggle trace", "debug", "setup", "/images/footsteps.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction("toggleTrace", DebugFunctions.getInstance(), parser);
        MapTool.addLocalMessage("trace.enabled = " +  result);
      }     
    });

		
		functionButtons.add(new ExtensionFunctionButton("Ping", "Click for ping", "test", "setup", "/images/ping-pong-bat.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object pong = FunctionCaller.callFunction("ping", ping, parser);
        MapTool.addLocalMessage(pong.toString());
      }    
		});
		
    functionButtons.add(new ExtensionFunctionButton("Open frames", "Open frames", "manage", "frames", "/images/window.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException{
        FunctionCaller.callFunction("frames_showAllFrames", ButtonFrameFunctions.getInstance(), parser);
      }   
    });
    functionButtons.add(new ExtensionFunctionButton("Close frames", "Close frames", "manage", "frames", "/images/closed-doors.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException{
        FunctionCaller.callFunction("frames_hideAllFrames", ButtonFrameFunctions.getInstance(), parser);
        FunctionCaller.callFunction("frames_showFrame", ButtonFrameFunctions.getInstance(), parser, getPrefix()+"frames");
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
