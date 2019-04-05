/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.extensionframework;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolLineParser;
import net.rptools.maptool.client.MapToolMacroContext;
import de.jadebringer.maptool.extensionframework.FrameworksFunctions;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

public abstract class ExtensionFunctionButton {

  private String name;
  private String text;
  private String tooltip;
  private String group;
  private String frame;
  private String imageFile;
  private boolean textAndImage;
  private final boolean trustedRequired;
  private String prefix = null;

  public ExtensionFunctionButton(
      String name,
      String text,
      String tooltip,
      String group,
      String frame,
      String imageFile,
      boolean textAndImage,
      boolean trustedRequired) {
    this.trustedRequired = trustedRequired;
    this.name = name;
    this.text = text;
    this.tooltip = tooltip;
    this.group = group;
    this.frame = frame;
    this.imageFile = imageFile;
    this.textAndImage = textAndImage;
  }

  public void setImageFile(String imageFile) {
    this.imageFile = imageFile;
  }
  
  public String getImageFile() {
    return imageFile;
  }

  public boolean isTextAndImage() {
    return textAndImage;
  }

  public abstract void run(Parser parser) throws ParserException;

  public final void execute() {
    MapToolMacroContext executionContext =
        new MapToolMacroContext(
            MapToolLineParser.CHAT_INPUT, MapToolLineParser.CHAT_INPUT, MapTool.getPlayer().isGM());
    if (trustedRequired && !executionContext.isTrusted()) {
      return;
    }

    MapTool.getParser().enterContext(executionContext);

    try {
      FrameworksFunctions.executeExtensionFunctionButtonWithAccessControl(this);
    } catch (Exception e) {
      MapTool.showError(null, e);
    } finally {
      MapTool.getParser().exitContext();
    }
  }

  public String getName() {
    return name;
  }
  
  public String getText() {
    return text;
  }

  public String getPrefixedFrame() {
    if (this.prefix != null && prefix.length() > 0) {
      return prefix.substring(0, prefix.length() - 1) + ": " + frame;
    }
    return frame;
  }

  public String getPrefixedFrameId() {
    if (this.prefix != null) {
      return prefix + frame;
    }
    return frame;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public void setText(String text) {
    this.textAndImage = true;
    this.text = text;
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getFrame() {
    return frame;
  }

  public void setFrame(String frame) {
    this.frame = frame;
  }

  public final boolean isTrustedRequired() {
    return trustedRequired;
  }

  protected String getPrefix() {
    return prefix;
  }

  void setPrefix(String prefix) {
    this.prefix = prefix;
  }
}
