/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.chatmacros;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.maptool.client.macro.MacroContext;
import net.rptools.maptool.client.macro.MacroDefinition;
import net.rptools.parser.ParserException;

/**
 * Macro to support calling macro function as /call function [params] chat command
 * 
 * @author oliver.szymanski
 */
@MacroDefinition(
		name = "call",
		aliases = { "call" },
		description = "call.description",
		expandRolls = false)
public class CallMacro implements Macro {
	
	public void execute(MacroContext context, String code, MapToolMacroContext executionContext) {
		if (code == null || code.trim().length() == 0) {
			return;
		}
		if (code.indexOf("(")<0) {
			code = code+"()";
		}

		try {
			MapTool.getParser().enterContext(null);
			String result = MapTool.getParser().parseLine("[r: "+code+"]");
			if (result != null) MapTool.addLocalMessage(result.toString());
		} catch (ParserException e) {
			MapTool.showError(null, e);
		} finally {
			MapTool.getParser().exitContext();
		}
	}

}
