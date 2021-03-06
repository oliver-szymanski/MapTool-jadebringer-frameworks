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
package de.jadebringer.maptool.extension.base.functions;

import de.jadebringer.maptool.extension.hook.ExtensionBundle;
import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import java.time.LocalDate;
import java.util.List;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/** @author oliver.szymanski */
public class PingFunction extends ExtensionFunction {

  public static final String PING = "ping";

  private ExtensionBundle extension;

  protected PingFunction(ExtensionBundle extension) {
    super(Alias.create(PING));
    setTrustedRequired(false);
    this.extension = extension;
  }

  public static PingFunction getInstance(ExtensionBundle extension) {
    return new PingFunction(extension);
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if (PING.equals(functionName)) {
      StringBuilder result = new StringBuilder();
      result.append("pong ").append(LocalDate.now().toString());
      if (parameters != null) {
        if (parameters.size() > 0) {
          result.append(":");
          for (Object parameter : parameters) {
            result.append(" ").append(parameter.toString()).append(";");
          }
        }
      }

      if (extension != null) {
        result
            .append(" (")
            .append(extension.name())
            .append("/")
            .append(extension.version())
            .append(")");
      }
      return result.toString();
    }

    return throwNotFoundParserException(functionName);
  }
}
