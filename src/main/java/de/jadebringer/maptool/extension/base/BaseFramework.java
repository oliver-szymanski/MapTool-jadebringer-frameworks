/*
 * This software is copyright by the Jadebringer.de development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool-jadebringer-extension Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.extension.base;

import de.jadebringer.maptool.extension.base.chatmacros.CallMacro;
import de.jadebringer.maptool.extension.base.chatmacros.FrameworksMacro;
import de.jadebringer.maptool.extension.base.functions.ButtonFrameFunctions;
import de.jadebringer.maptool.extension.base.functions.ContentFunctions;
import de.jadebringer.maptool.extension.base.functions.DebugFunctions;
import de.jadebringer.maptool.extension.base.functions.InputFunctions;
import de.jadebringer.maptool.extension.base.functions.LinkFunctions;
import de.jadebringer.maptool.extension.base.functions.MacrosFunctions;
import de.jadebringer.maptool.extension.base.functions.MapFunctions;
import de.jadebringer.maptool.extension.base.functions.OutputToFunction;
import de.jadebringer.maptool.extension.base.functions.PingFunction;
import de.jadebringer.maptool.extension.base.functions.SettingsFunctions;
import de.jadebringer.maptool.extension.hook.ExtensionChatMacro;
import de.jadebringer.maptool.extension.hook.ExtensionFrameworkBundle;
import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import de.jadebringer.maptool.extension.hook.ExtensionFunctionButton;
import de.jadebringer.maptool.extension.hook.FunctionCaller;
import de.jadebringer.maptool.extension.hook.Version;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.rptools.maptool.client.MapTool;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

public class BaseFramework extends ExtensionFrameworkBundle {

  private List<ExtensionFunction> functions = new LinkedList<>();
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
  private List<ExtensionChatMacro> chatMacros = new LinkedList<>();

  public BaseFramework() {
    super(new Version(1, 0, 0, "jadebringer-base"));

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

    chatMacros.add(new FrameworksMacro());
    chatMacros.add(new CallMacro());

    functionButtons.add(
        new ExtensionFunctionButton(
            "list settings",
            "list settings",
            "list setting",
            "settings",
            "basics",
            "/images/settings-knobs.png",
            false,
            true) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object result =
                FunctionCaller.callFunction(
                    SettingsFunctions.LIST_SETTINGS, SettingsFunctions.getInstance(), parser);
            if (result != null) {
              MapTool.addLocalMessage(result.toString());
            }
          }
        });
    functionButtons.add(
        new ExtensionFunctionButton(
            "get setting",
            "get setting",
            "get setting",
            "settings",
            "basics",
            "/images/read.png",
            false,
            true) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object key = FunctionCaller.askForVariable(parser, "key", null);
            if (key == null) {
              return;
            }
            Object result =
                FunctionCaller.callFunction(
                    SettingsFunctions.GET_SETTING, SettingsFunctions.getInstance(), parser, key);
            if (result != null) {
              MapTool.addLocalMessage(key + " = " + result.toString());
            }
          }
        });
    functionButtons.add(
        new ExtensionFunctionButton(
            "set setting",
            "set setting",
            "set setting",
            "settings",
            "basics",
            "/images/pencil.png",
            false,
            true) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object key = FunctionCaller.askForVariable(parser, "key", null);
            Object value = FunctionCaller.askForVariable(parser, "value", null);
            if (key == null) {
              return;
            }
            FunctionCaller.callFunction(
                SettingsFunctions.SET_SETTING, SettingsFunctions.getInstance(), parser, key, value);
            MapTool.addLocalMessage(key + " = " + value);
          }
        });
    functionButtons.add(
        new ExtensionFunctionButton(
            "delete setting",
            "delete setting",
            "delete setting",
            "settings",
            "basics",
            "/images/alligator-clip.png",
            false,
            true) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object key = FunctionCaller.askForVariable(parser, "key", null);
            if (key == null) {
              return;
            }
            FunctionCaller.callFunction(
                SettingsFunctions.DELETE_SETTING, SettingsFunctions.getInstance(), parser, key);
            MapTool.addLocalMessage(key + " deleted.");
          }
        });

    functionButtons.add(
        new ExtensionFunctionButton(
            "debug",
            "debug",
            "toggle debug",
            "debug",
            "basics",
            "/images/spotted-bug.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object result =
                FunctionCaller.callFunction(
                    DebugFunctions.DEBUG_TOGGLE_DEBUG, DebugFunctions.getInstance(), parser);
            MapTool.addLocalMessage("debug.enabled = " + result);
          }
        });
    functionButtons.add(
        new ExtensionFunctionButton(
            "trace",
            "trace",
            "toggle trace",
            "debug",
            "basics",
            "/images/footsteps.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object result =
                FunctionCaller.callFunction(
                    DebugFunctions.DEBUG_TOGGLE_TRACE, DebugFunctions.getInstance(), parser);
            MapTool.addLocalMessage("trace.enabled = " + result);
          }
        });

    functionButtons.add(
        new ExtensionFunctionButton(
            "Ping",
            "Ping",
            "Click for ping",
            "test",
            "basics",
            "/images/ping-pong-bat.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            Object pong = FunctionCaller.callFunction(PingFunction.PING, ping, parser);
            MapTool.addLocalMessage(pong.toString());
          }
        });

    functionButtons.add(
        new ExtensionFunctionButton(
            "Open frames",
            "Open frames",
            "Open all frames",
            "manage",
            "frames",
            "/images/window.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            FunctionCaller.callFunction(
                ButtonFrameFunctions.FRAMES_SHOW_ALL_FRAMES,
                ButtonFrameFunctions.getInstance(),
                parser);
          }
        });
    functionButtons.add(
        new ExtensionFunctionButton(
            "Close frames",
            "Close frames",
            "Close all frames",
            "manage",
            "frames",
            "/images/closed-doors.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            FunctionCaller.callFunction(
                ButtonFrameFunctions.FRAMES_HIDE_ALL_FRAMES,
                ButtonFrameFunctions.getInstance(),
                parser);
            FunctionCaller.callFunction(
                ButtonFrameFunctions.FRAMES_SHOW_FRAME,
                ButtonFrameFunctions.getInstance(),
                parser,
                getPrefix() + "frames");
          }
        });

    functionButtons.add(
        new ExtensionFunctionButton(
            "Center",
            "Center",
            "Center",
            "maps",
            "basics",
            "/images/convergence-target.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            FunctionCaller.callFunction(
                MapFunctions.MAPS_CENTER, MapFunctions.getInstance(), parser);
          }
        });
    functionButtons.add(
        new ExtensionFunctionButton(
            "Extent",
            "Extent",
            "Extent",
            "maps",
            "basics",
            "/images/treasure-map.png",
            false,
            false) {
          @Override
          public void run(Parser parser) throws ParserException {
            FunctionCaller.callFunction(
                MapFunctions.MAPS_EXTENT, MapFunctions.getInstance(), parser);
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
