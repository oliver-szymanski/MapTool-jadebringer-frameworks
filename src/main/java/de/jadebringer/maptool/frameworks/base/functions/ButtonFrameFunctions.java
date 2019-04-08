/*
 * This software is copyright by the Jadebringer.de development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool-jadebringer-framework Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.functions;

import de.jadebringer.maptool.extensionframework.ExtensionFunction;
import de.jadebringer.maptool.extensionframework.ExtensionFunctionButton;
import de.jadebringer.maptool.extensionframework.FrameworksFunctions;
import de.jadebringer.maptool.extensionframework.FunctionCaller;
import de.jadebringer.maptool.extensionframework.ui.ButtonFrame;
import java.math.BigDecimal;
import java.util.List;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/** @author oliver.szymanski */
public class ButtonFrameFunctions extends ExtensionFunction {

  public static final String FRAMES_IS_ENABLED_BUTTON = "frames_isEnabledButton";
  public static final String FRAMES_IS_HIDDEN_BUTTON = "frames_isHiddenButton";
  public static final String FRAMES_DISABLE_BUTTON = "frames_disableButton";
  public static final String FRAMES_ENABLE_BUTTON = "frames_enableButton";
  public static final String FRAMES_HIDE_BUTTON = "frames_hideButton";
  public static final String FRAMES_SHOW_BUTTON = "frames_showButton";
  public static final String FRAMES_REMOVE_BUTTON = "frames_removeButton";
  public static final String FRAMES_SHOW_FRAME = "frames_showFrame";
  public static final String FRAMES_ADD_BUTTON = "frames_addButton";
  public static final String FRAMES_SET_BUTTON_TEXT = "frames_setButtonText";
  public static final String FRAMES_SET_BUTTON_IMAGE = "frames_setButtonImage";
  public static final String FRAMES_IS_FRAME_VISIBLE = "frames_isFrameVisible";
  public static final String FRAMES_HIDE_FRAME = "frames_hideFrame";
  public static final String FRAMES_HIDE_ALL_FRAMES = "frames_hideAllFrames";
  public static final String FRAMES_SHOW_ALL_FRAMES = "frames_showAllFrames";

  protected ButtonFrameFunctions() {
    super(
        Alias.create(FRAMES_SHOW_ALL_FRAMES, 0, 0),
        Alias.create(FRAMES_HIDE_ALL_FRAMES, 0, 0),
        Alias.create(FRAMES_SHOW_FRAME),
        Alias.create(FRAMES_HIDE_FRAME),
        Alias.create(FRAMES_IS_FRAME_VISIBLE, 1, 1),
        Alias.create(FRAMES_ADD_BUTTON, 10, 10),
        Alias.create(FRAMES_SET_BUTTON_TEXT, 5, 5),
        Alias.create(FRAMES_SET_BUTTON_IMAGE, 5, 5),
        Alias.create(FRAMES_REMOVE_BUTTON, 4, 4),
        Alias.create(FRAMES_SHOW_BUTTON, 4, 4),
        Alias.create(FRAMES_HIDE_BUTTON, 4, 4),
        Alias.create(FRAMES_ENABLE_BUTTON, 4, 4),
        Alias.create(FRAMES_DISABLE_BUTTON, 4, 4),
        Alias.create(FRAMES_IS_HIDDEN_BUTTON, 4, 4),
        Alias.create(FRAMES_IS_ENABLED_BUTTON, 4, 4));
    setTrustedRequired(false);
  }

  private static final ButtonFrameFunctions instance = new ButtonFrameFunctions();

  public static ButtonFrameFunctions getInstance() {
    return instance;
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if (FRAMES_SHOW_FRAME.equals(functionName)) {
      return showFrame(parser, functionName, parameters);
    }
    if (FRAMES_HIDE_FRAME.equals(functionName)) {
      return hideFrame(parser, functionName, parameters);
    } else if (FRAMES_SHOW_ALL_FRAMES.equals(functionName)) {
      showAllFrames(parser, functionName, parameters);
      return BigDecimal.ONE;
    }
    if (FRAMES_HIDE_ALL_FRAMES.equals(functionName)) {
      hideAllFrames(parser, functionName, parameters);
      return BigDecimal.ONE;
    }
    if (FRAMES_IS_FRAME_VISIBLE.equals(functionName)) {
      return isFrameVisible(parser, functionName, parameters);
    }
    if (FRAMES_ADD_BUTTON.equals(functionName)) {
      return addButton(parser, functionName, parameters);
    }
    if (FRAMES_REMOVE_BUTTON.equals(functionName)) {
      return removeButton(parser, functionName, parameters);
    }
    if (FRAMES_SET_BUTTON_TEXT.equals(functionName)) {
      return setButtonText(parser, functionName, parameters);
    }
    if (FRAMES_SET_BUTTON_IMAGE.equals(functionName)) {
      return setButtonImage(parser, functionName, parameters);
    }
    if (FRAMES_HIDE_BUTTON.equals(functionName)) {
      return hideButton(parser, functionName, parameters);
    }
    if (FRAMES_SHOW_BUTTON.equals(functionName)) {
      return showButton(parser, functionName, parameters);
    }
    if (FRAMES_ENABLE_BUTTON.equals(functionName)) {
      return enableButton(parser, functionName, parameters);
    }
    if (FRAMES_DISABLE_BUTTON.equals(functionName)) {
      return disableButton(parser, functionName, parameters);
    }
    if (FRAMES_IS_HIDDEN_BUTTON.equals(functionName)) {
      return isHiddenButton(parser, functionName, parameters);
    }
    if (FRAMES_IS_ENABLED_BUTTON.equals(functionName)) {
      return isEnabledButton(parser, functionName, parameters);
    }

    return throwNotFoundParserException(functionName);
  }

  private Object isFrameVisible(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String frame = FunctionCaller.getParam(parameters, 0);
    String prefix = FunctionCaller.getParam(parameters, 1, "");
    boolean result = FrameworksFunctions.getInstance().isFrameVisible(prefix + frame);
    return (result) ? BigDecimal.ONE : BigDecimal.ZERO;
  }

  private Object showFrame(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    if (parameters == null) {
      return false;
    }
    String frame = FunctionCaller.getParam(parameters, 0);
    String prefix = FunctionCaller.getParam(parameters, 1, "");
    boolean result = FrameworksFunctions.getInstance().showFrame(prefix + frame);
    return (result) ? BigDecimal.ONE : BigDecimal.ZERO;
  }

  private Object hideFrame(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    if (parameters == null) {
      return false;
    }
    String frame = FunctionCaller.getParam(parameters, 0);
    String prefix = FunctionCaller.getParam(parameters, 1, "");
    boolean result = FrameworksFunctions.getInstance().hideFrame(prefix + frame);
    return (result) ? BigDecimal.ONE : BigDecimal.ZERO;
  }

  private void showAllFrames(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    FrameworksFunctions.getInstance().showAllFrames();
  }

  private void hideAllFrames(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    FrameworksFunctions.getInstance().hideAllFrames();
  }

  public Object addButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String macroName = FunctionCaller.getParam(parameters, 0);
    String name = FunctionCaller.getParam(parameters, 1);
    String text = FunctionCaller.getParam(parameters, 2);
    String tooltip = FunctionCaller.getParam(parameters, 3);
    String group = FunctionCaller.getParam(parameters, 4);
    String frame = FunctionCaller.getParam(parameters, 5);
    String prefix = FunctionCaller.getParam(parameters, 6);
    String imageFile = FunctionCaller.getParam(parameters, 7);
    boolean frameAndImage = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 8));
    boolean trustedRequired = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 9));

    ExtensionFunctionButton extensionFunctionButton =
        new ExtensionFunctionButton(
            name, text, tooltip, group, frame, imageFile, frameAndImage, trustedRequired) {
          @Override
          public void run(Parser parser) throws ParserException {
            StringBuffer macroArgs = new StringBuffer();
            macroArgs
                .append(prefix)
                .append(".")
                .append(frame)
                .append(".")
                .append(group)
                .append(".")
                .append(name);
            MacrosFunctions.getInstance()
                .executeMacro(
                    parser,
                    false,
                    FunctionCaller.toObjectList(
                        macroName, "self", macroArgs.toString(), "impersonated"));
          }
        };

    FrameworksFunctions.getInstance().addExtensionFunctionButton(extensionFunctionButton, prefix);

    return BigDecimal.ONE;
  }

  public Object showButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.show(extensionFunctionButton);
    return BigDecimal.ONE;
  }

  public Object hideButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.hide(extensionFunctionButton);
    return BigDecimal.ONE;
  }

  public Object removeButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.remove(extensionFunctionButton);
    return BigDecimal.ONE;
  }

  public Object enableButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.enable(extensionFunctionButton);
    return BigDecimal.ONE;
  }

  public Object disableButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.disable(extensionFunctionButton);
    return BigDecimal.ONE;
  }

  public Object isHiddenButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    return FunctionCaller.fromBoolean(buttonFrame.isHidden(extensionFunctionButton));
  }

  public Object isEnabledButton(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    return FunctionCaller.fromBoolean(buttonFrame.isEnabled(extensionFunctionButton));
  }

  public Object setButtonText(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    String text = FunctionCaller.getParam(parameters, 4);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    extensionFunctionButton.setText(text);
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.update(extensionFunctionButton);
    return BigDecimal.ONE;
  }

  public Object setButtonImage(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String name = FunctionCaller.getParam(parameters, 0);
    String group = FunctionCaller.getParam(parameters, 1);
    String frame = FunctionCaller.getParam(parameters, 2);
    String prefix = FunctionCaller.getParam(parameters, 3);
    String imageFile = FunctionCaller.getParam(parameters, 4);
    ExtensionFunctionButton extensionFunctionButton =
        FrameworksFunctions.getInstance().getExtensionFunctionButton(name, group, frame, prefix);
    if (extensionFunctionButton == null) {
      return BigDecimal.ZERO;
    }
    extensionFunctionButton.setImageFile(imageFile);
    ButtonFrame buttonFrame = FrameworksFunctions.getInstance().getButtonFrame(frame, prefix);
    if (buttonFrame == null) {
      return BigDecimal.ZERO;
    }
    buttonFrame.update(extensionFunctionButton);
    return BigDecimal.ONE;
  }
}
