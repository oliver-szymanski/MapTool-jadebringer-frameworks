/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.chatmacros;

import java.util.LinkedList;
import java.util.List;

import de.jadebringer.maptool.extensionframework.ExtensionChatMacro;
import de.jadebringer.maptool.extensionframework.FrameworksFunctions;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.MacroContext;
import net.rptools.maptool.client.macro.MacroDefinition;
import net.rptools.parser.ParserException;

/**
 * Macro to support adding frameworks by using /frameworks chat command
 * 
 * @author oliver.szymanski
 */
@MacroDefinition(
		name = "frameworks",
		aliases = { "frameworks" },
		description = "framework.description",
		expandRolls = false)
public class FrameworksMacro extends ExtensionChatMacro {
	
	public FrameworksMacro() {
    super(false);
  }

  public void run(MacroContext context, String macroParameter, MapToolMacroContext executionContext) {
		if (macroParameter == null || macroParameter.trim().length() == 0) {
			return;
		}

		List<Object> parameters = new LinkedList<>();		
		for(String parameter : macroParameter.split(" ")) {
			parameters.add(parameter.trim());
		}
		
		if (parameters.size() >= 1) {
			String functionName = (String)parameters.remove(0);
			
			try {
				MapTool.getParser().enterContext(null);
				Object result = FrameworksFunctions.getInstance().childEvaluate(null , functionName, parameters);
				if (result != null) MapTool.addLocalMessage(result.toString());
			} catch (ParserException e) {
				// ignore exception
				MapTool.showError(null, e);
			} finally {
				MapTool.getParser().exitContext();
			}
		}
	}

}
