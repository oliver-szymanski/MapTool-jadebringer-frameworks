/*
 * This software Copyright by the RPTools.net development team, and licensed under the Affero GPL Version 3 or, at your option, any later version.
 *
 * MapTool Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License * along with this source Code. If not, please visit <http://www.gnu.org/licenses/> and specifically the Affero license text
 * at <http://www.gnu.org/licenses/agpl.html>.
 */
package de.jadebringer.maptool.frameworks.base.functions;

import java.math.BigDecimal;
import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.FrameworksFunctions.FunctionCaller;
import net.rptools.maptool.language.I18N;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.AbstractFunction;

/**
 * 
 * @author oliver.szymanski
 */
public class DebugFunctions extends AbstractFunction {
	public DebugFunctions() {
		super(0, UNLIMITED_PARAMETERS, "isDebug", "debug", "trace", "isTrace", "setDebug", "setTrace", "cc");
	}

	private final static DebugFunctions instance = new DebugFunctions();
	private final SettingsFunctions settingsFunctions = SettingsFunctions.getInstance();
  
	public static DebugFunctions getInstance() {
		return instance;
	}

	@Override
	public Object childEvaluate(Parser parser, String functionName, List<Object> parameters) throws ParserException {

		if (!MapTool.getParser().isMacroTrusted()) {
			throw new ParserException(I18N.getText("macro.function.general.noPerm", functionName));
		}
		
		if ("setDebug".equals(functionName)) {
			BigDecimal debugEnabled = FunctionCaller.getParam(parameters, 0);
			return setDebug(parser, debugEnabled);
		} else if ("setTrace".equals(functionName)) {
      BigDecimal traceEnabled = FunctionCaller.getParam(parameters, 0);
      return setTrace(parser, traceEnabled);
    } else if ("isDebug".equals(functionName)) {
			return isDebug(parser);
		} else if ("isTrace".equals(functionName)) {
      return isTrace(parser);
    } else if ("debug".equals(functionName)) {
      if (BigDecimal.ONE.equals(isDebug(parser))) {
        StringBuilder sb = new StringBuilder();
        for(Object parameter : parameters) {
          sb.append(parameter.toString());
        }
        
        MapTool.addLocalMessage("DEBUG:" + sb.toString());
      }
      return "";
    } else if ("trade".equals(functionName)) {
      if (BigDecimal.ONE.equals(isTrace(parser))) {
        StringBuilder sb = new StringBuilder();
        for(Object parameter : parameters) {
          sb.append(parameter.toString());
        }
        
        MapTool.addLocalMessage("DEBUG:" + sb.toString());
      }
      return "";
    } else if ("cc".equals(functionName)) {
      if (BigDecimal.ONE.equals(isTrace(parser))) {
        StringBuilder sb = new StringBuilder();
        for(Object parameter : parameters) {
          sb.append(parameter.toString());
        }
        
        MapTool.addLocalMessage("CC reached:" + sb.toString());
      }
      return "";
    }
	
		return "";
	}

  private Object setTrace(Parser parser, BigDecimal traceEnabled)
      throws ParserException {
    settingsFunctions.setSetting(parser, "trace.enabled", traceEnabled, null);
    return "";
  }

  private Object setDebug(Parser parser, BigDecimal debugEnabled)
      throws ParserException {
    settingsFunctions.setSetting(parser, "debug.enabled", debugEnabled, null);
    return "";
  }

  private Object isTrace(Parser parser) throws ParserException {
    if (BigDecimal.ONE.compareTo((BigDecimal)settingsFunctions.getSetting(parser, "trace.enabled", BigDecimal.ZERO, null)) <= 0) {
      return BigDecimal.ONE;
    } else {
      return BigDecimal.ZERO;
    }
  }

  private BigDecimal isDebug(Parser parser) throws ParserException {
    if (BigDecimal.ONE.compareTo((BigDecimal)settingsFunctions.getSetting(parser, "debug.enabled", BigDecimal.ZERO, null)) <= 0) {
      return BigDecimal.ONE;
    } else {
      return BigDecimal.ZERO;
    }
  }

}
