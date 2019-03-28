package de.jadebringer.maptool.frameworks.base;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.frameworks.base.chatmacros.CallMacro;
import de.jadebringer.maptool.frameworks.base.chatmacros.FrameworksMacro;
import de.jadebringer.maptool.frameworks.base.functions.DebugFunctions;
import de.jadebringer.maptool.frameworks.base.functions.ButtonFrameFunctions;
import de.jadebringer.maptool.frameworks.base.functions.InputFunctions;
import de.jadebringer.maptool.frameworks.base.functions.LinkFunctions;
import de.jadebringer.maptool.frameworks.base.functions.MacroFunctions;
import de.jadebringer.maptool.frameworks.base.functions.OutputToFunction;
import de.jadebringer.maptool.frameworks.base.functions.PingFunction;
import de.jadebringer.maptool.frameworks.base.functions.SettingsFunctions;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionChatMacro;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFrameworkBundle;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

public class BaseFramework extends ExtensionFrameworkBundle {
	
	private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
	private List<ExtensionChatMacro> chatMacros = new LinkedList<>();

	public BaseFramework() {
		functions.add(InputFunctions.getInstance());
		functions.add(PingFunction.getInstance(this));
		functions.add(LinkFunctions.getInstance());
		functions.add(OutputToFunction.getInstance());
		functions.add(SettingsFunctions.getInstance());
		functions.add(DebugFunctions.getInstance());
    functions.add(MacroFunctions.getInstance());
    functions.add(ButtonFrameFunctions.getInstance());
    
		chatMacros.add(new FrameworksMacro());
		chatMacros.add(new CallMacro());
		
    functionButtons.add(new ExtensionFunctionButton("list settings", "list setting", "settings", "setup", "/images/settings-knobs.png", false, false) {
      @Override
      public void run(Parser parser) throws ParserException {
        Object result = FunctionCaller.callFunction("listSettings", SettingsFunctions.getInstance(), parser);
        if (result != null) { MapTool.addLocalMessage(result.toString()); }
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("get setting", "get setting", "settings", "setup", "/images/read.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }     
    });
		functionButtons.add(new ExtensionFunctionButton("set setting", "set setting", "settings", "setup", "/images/pencil.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("remove setting", "remove setting", "settings", "setup", "/images/alligator-clip.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }     
    });

    functionButtons.add(new ExtensionFunctionButton("debug", "toggle debug", "debug", "setup", "/images/spotted-bugs.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("trace", "toggle trace", "debug", "setup", "/images/footsteps.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }     
    });

		
		functionButtons.add(new ExtensionFunctionButton("Ping", "Click for ping", "basics", "test", "/images/ping-pong-bat.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }		  
		});
    functionButtons.add(new ExtensionFunctionButton("Ping 2", "Click for ping", "basics", "test", "/images/hammer-nails.png", false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button2 clicked");
      }   
    });
    
    functionButtons.add(new ExtensionFunctionButton("Defend", "Click to defend", "battle", "actions", null, false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button1 clicked");
      }     
    });
    functionButtons.add(new ExtensionFunctionButton("Attack", "Click to attach", "battle", "actions", null, false, false) {
      @Override
      public void run(Parser parser) {
        MapTool.addLocalMessage("button2 clicked");
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
