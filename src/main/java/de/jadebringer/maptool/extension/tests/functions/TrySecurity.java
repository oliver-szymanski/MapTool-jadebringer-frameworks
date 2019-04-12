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
package de.jadebringer.maptool.extension.tests.functions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/** @author oliver.szymanski */
public class TrySecurity extends ExtensionFunction {

  protected TrySecurity() {
    super(Alias.create("exit"), Alias.create("io"));
    setTrustedRequired(true);
  }

  public static TrySecurity getInstance() {
    return new TrySecurity();
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if ("exit".equals(functionName)) {
      System.exit(0);
    } else if ("io".equals(functionName)) {
      File f = new File("./test.txt");
      try {
        f.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return throwNotFoundParserException(functionName);
  }
}
