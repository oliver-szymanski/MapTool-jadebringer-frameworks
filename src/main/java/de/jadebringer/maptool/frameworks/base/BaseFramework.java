package de.jadebringer.maptool.frameworks.base;

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
import de.jadebringer.maptool.frameworks.base.functions.TrySecurity;
import net.rptools.maptool.client.MapTool;
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
    functions.add(MapFunctions.getInstance());
    functions.add(TrySecurity.getInstance());
    
		chatMacros.add(new FrameworksMacro());
		chatMacros.add(new CallMacro());
		
    functionButtons.add(new ExtensionFunctionButton("list settings", "list settings", "list setting", "settings", "basics", "/images/settings-knobs.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction(SettingsFunctions.LIST_SETTINGS, SettingsFunctions.getInstance(), parser);
        if (result != null) { MapTool.addLocalMessage(result.toString()); }
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("get setting", "get setting", "get setting", "settings", "basics", "/images/read.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException{
        Object key = FunctionCaller.askForVariable(parser, "key", null);
        if (key == null) { return; }
        Object result = FunctionCaller.callFunction(SettingsFunctions.GET_SETTING, SettingsFunctions.getInstance(), parser, key);
        if (result != null) { MapTool.addLocalMessage(key + " = " + result.toString()); }
      }     
    });
		functionButtons.add(new ExtensionFunctionButton("set setting", "set setting", "set setting", "settings", "basics", "/images/pencil.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object key = FunctionCaller.askForVariable(parser, "key", null);
        Object value = FunctionCaller.askForVariable(parser, "value", null);
        if (key == null) { return; }
        FunctionCaller.callFunction(SettingsFunctions.SET_SETTING, SettingsFunctions.getInstance(), parser, key, value);
        MapTool.addLocalMessage(key + " = " + value);
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("delete setting", "delete setting", "delete setting", "settings", "basics", "/images/alligator-clip.png", false, true) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object key = FunctionCaller.askForVariable(parser, "key", null);
        if (key == null) { return; }
        FunctionCaller.callFunction(SettingsFunctions.DELETE_SETTING, SettingsFunctions.getInstance(), parser, key);
        MapTool.addLocalMessage(key + " deleted.");
      }     
    });

    functionButtons.add(new ExtensionFunctionButton("debug", "debug", "toggle debug", "debug", "basics", "/images/spotted-bug.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction(DebugFunctions.DEBUG_TOGGLE_DEBUG, DebugFunctions.getInstance(), parser);
        MapTool.addLocalMessage("debug.enabled = " +  result);
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("trace", "trace", "toggle trace", "debug", "basics", "/images/footsteps.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction(DebugFunctions.DEBUG_TOGGLE_TRACE, DebugFunctions.getInstance(), parser);
        MapTool.addLocalMessage("trace.enabled = " +  result);
      }     
    });

		functionButtons.add(new ExtensionFunctionButton("Ping", "Ping", "Click for ping", "test", "basics", "/images/ping-pong-bat.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object pong = FunctionCaller.callFunction(PingFunction.PING, ping, parser);
        MapTool.addLocalMessage(pong.toString());
      }    
		});
		
    functionButtons.add(new ExtensionFunctionButton("Open frames", "Open frames", "Open frames", "manage", "frames", "/images/window.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException{
        FunctionCaller.callFunction(ButtonFrameFunctions.FRAMES_SHOW_ALL_FRAMES, ButtonFrameFunctions.getInstance(), parser);
      }   
    });
    functionButtons.add(new ExtensionFunctionButton("Close frames", "Close frames", "Close frames", "manage", "frames", "/images/closed-doors.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException{
        FunctionCaller.callFunction(ButtonFrameFunctions.FRAMES_HIDE_ALL_FRAMES, ButtonFrameFunctions.getInstance(), parser);
        FunctionCaller.callFunction(ButtonFrameFunctions.FRAMES_SHOW_FRAME, ButtonFrameFunctions.getInstance(), parser, getPrefix()+"frames");
      }   
    });
    
    functionButtons.add(new ExtensionFunctionButton("Center", "Center", "Center", "maps", "basics", "/images/convergence-target.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException{
        FunctionCaller.callFunction(MapFunctions.MAPS_CENTER, MapFunctions.getInstance(), parser);
      }   
    });
    functionButtons.add(new ExtensionFunctionButton("Extent", "Extent", "Extent", "maps", "basics", "/images/treasure-map.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException{
        FunctionCaller.callFunction(MapFunctions.MAPS_EXTENT, MapFunctions.getInstance(), parser);
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
