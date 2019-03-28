/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.functions;

import java.time.LocalDate;
import java.util.List;

import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFrameworkBundle;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class PingFunction extends ExtensionFunction {
  
  private ExtensionFrameworkBundle framework;
  
	protected PingFunction(ExtensionFrameworkBundle framework) {
		super(false, Alias.create("ping"));
		this.framework = framework;
	}
	
	public static PingFunction getInstance(ExtensionFrameworkBundle framework) {
    return new PingFunction(framework);
  }

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

	  StringBuilder result = new StringBuilder();
	  result.append("pong ").append(LocalDate.now().toString());
	  if (parameters != null) {
	    if (parameters.size() > 0) {
	      result.append(":");
        for(Object parameter : parameters) {
          result.append(" ").append(parameter.toString()).append(";");
        }
	    }
	  }
	  
	  if (framework != null) {
	    result.append("(").append(framework.name()).append("/").append(framework.version()).append(")");
	  }
    return result.toString();
	}

}
