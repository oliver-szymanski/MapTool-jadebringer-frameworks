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
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import com.jidesoft.grid.PropertyTable;
import de.jadebringer.maptool.frameworks.base.ui.PropertiesTableModel;
import de.jadebringer.maptool.frameworks.base.ui.WordWrapCellRenderer;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunction;
import net.rptools.maptool.client.functions.frameworkfunctions.FunctionCaller;
import net.rptools.maptool.language.I18N;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;

/**
 * 
 * @author oliver.szymanski
 */
public class DebugFunctions extends ExtensionFunction {
	public static final String ERROR = "error";
	public static final String WARN = "warn";
	public static final String CC = "cc";
	public static final String TRACE = "trace";
	public static final String DEBUG = "debug";
	public static final String DEBUG_SET_TRACE = "debug_setTrace";
	public static final String DEBUG_SET_DEBUG = "debug_setDebug";
	public static final String DEBUG_IS_TRACE = "debug_isTrace";
	public static final String DEBUG_TOGGLE_TRACE = "debug_toggleTrace";
	public static final String DEBUG_TOGGLE_DEBUG = "debug_toggleDebug";
	public static final String DEBUG_IS_DEBUG = "debug_isDebug";
	public static final String DEBUG_INSPECT = "debug_inspect";
	public static final String DEBUG_MANIPULATE = "debug_manipulate";

	public DebugFunctions() {
		super( 
		    Alias.create(DEBUG_MANIPULATE), 
		    Alias.create(DEBUG_INSPECT), 
		    Alias.create(DEBUG_IS_DEBUG, 0, 0).setTrustedRequired(false), 
        Alias.create(DEBUG_TOGGLE_DEBUG, 0, 0), 
        Alias.create(DEBUG_TOGGLE_TRACE, 0, 0), 
		    Alias.create(DEBUG_IS_TRACE, 0, 0).setTrustedRequired(false), 
		    Alias.create(DEBUG_SET_DEBUG, 1, 1), 
		    Alias.create(DEBUG_SET_TRACE, 1, 1), 
		    Alias.create(DEBUG).setTrustedRequired(false), 
		    Alias.create(TRACE).setTrustedRequired(false), 
		    Alias.create(CC).setTrustedRequired(false), 
		    Alias.create(WARN).setTrustedRequired(false), 
		    Alias.create(ERROR).setTrustedRequired(false)
		    );
		setTrustedRequired(true);
	}

	private final static DebugFunctions instance = new DebugFunctions();
	private final static String[] DEFAULT_VARIABLES = { "macro.args", "macro.args.num", "macro.catchAbort", "macro.catchAssert" };

	private final SettingsFunctions settingsFunctions = SettingsFunctions.getInstance();
  
	public static DebugFunctions getInstance() {
		return instance;
	}

	@Override
	public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {

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
    } else if ("toggleDebug".equals(functionName)) {
      setDebug(parser, BigDecimal.ZERO.equals(isDebug(parser)) ? BigDecimal.ONE : BigDecimal.ZERO);
      return isDebug(parser);
    } else if ("toggleTrace".equals(functionName)) {
      setTrace(parser, BigDecimal.ZERO.equals(isTrace(parser)) ? BigDecimal.ONE : BigDecimal.ZERO);
      return isTrace(parser);
    } else if ("inspect".equals(functionName)) {
      return inspect(parser, parameters);
    } else if ("manipulate".equals(functionName)) {
      return manipulate(parser, parameters);
    } else if (DEBUG.equals(functionName)) {
      if (BigDecimal.ONE.equals(isDebug(parser))) {
        String result = concatenateParametersToString(parameters);
        MapTool.addLocalMessage("DEBUG:" + result);
      }
      return "";
    } else if (WARN.equals(functionName)) {
      String result = concatenateParametersToString(parameters);
      MapTool.addLocalMessage("WARN:" + result);
      return "";
    } else if (ERROR.equals(functionName)) {
      String result = concatenateParametersToString(parameters);
      MapTool.addLocalMessage("ERROR:" + result);
      return "";
    } else if (TRACE.equals(functionName)) {
      if (BigDecimal.ONE.equals(isTrace(parser))) {
        String result = concatenateParametersToString(parameters);
        MapTool.addLocalMessage("DEBUG:" + result);
      }
      return "";
    } else if (CC.equals(functionName)) {
      if (BigDecimal.ONE.equals(isTrace(parser))) {
        String result = concatenateParametersToString(parameters);
        MapTool.addLocalMessage("CC reached:" + result);
      }
      return "";
    }
	
		return throwNotFoundParserException(functionName);
	}

  private String concatenateParametersToString(List<Object> parameters) {
    if (parameters == null || parameters.size() == 0) {
      return "";
    }
    
    StringBuilder sb = new StringBuilder();
    for(Object parameter : parameters) {
      sb.append(parameter.toString());
    }
    return sb.toString();
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
  
  private Object inspect(Parser parser, List<Object> parameters) throws ParserException {
    inspector(parser, true, parameters);
    return "OK";
  }

  private Object manipulate(Parser parser, List<Object> parameters) throws ParserException {
    return inspector(parser, false, parameters);
  }
  
  private Object inspector(Parser parser, boolean watch, List<Object> parameters) throws ParserException {
    MapToolVariableResolver resolver = (MapToolVariableResolver)parser.getVariableResolver();
    boolean autoPrompt = resolver.getAutoPrompt();
    resolver.setAutoPrompt(false);
    
    // get variables defined in params
    List<PropertiesTableModel.PropertyModel> props = new LinkedList<>();
    parameters.stream().forEach(p -> {
      try {
        Object value = resolver.getVariable(p.toString());
        props.add(new PropertiesTableModel.PropertyModel(p.toString(), value));
      } catch (Exception e) {
        // safe to ignore
        props.add(new PropertiesTableModel.PropertyModel(p.toString(), null));
      }
    });
    
    // add default variables
    for(String variable : DEFAULT_VARIABLES) {
      Object value = resolver.getVariable(variable);
      props.add(new PropertiesTableModel.PropertyModel(variable, value));
    }
    resolver.setAutoPrompt(autoPrompt);
    
    PropertiesTableModel model = new PropertiesTableModel(props);
    PropertiesTableModel.PropertyModel selected = null;
    if (props.size() > 0) { selected = props.get(0); }
    final InspectTable inspectTable = new InspectTable(model, selected, !watch);
    
    int buttons;
    if (watch) {
      buttons = JOptionPane.DEFAULT_OPTION;
    } else {
      buttons = JOptionPane.OK_CANCEL_OPTION;
    }

    JOptionPane jop = new JOptionPane(new JScrollPane(inspectTable), JOptionPane.INFORMATION_MESSAGE, buttons);
    JDialog dlg = jop.createDialog(MapTool.getFrame(), "Inspector");
    dlg.setResizable(true);
    
    Runnable openInspector = new Runnable() {
      @Override
      public void run() {
        dlg.setVisible(true); // this opens the dialog
        dlg.dispose();        
        // if required set up callbacks needed for desired runtime behavior
        //dlg.addComponentListener(new FixupComponentAdapter(ip));
      }
    };
    
    if (watch) {
      SwingUtilities.invokeLater(openInspector);
    } else {
      openInspector.run();
      try {
        int dlgResult = (Integer) jop.getValue();
        if (dlgResult == JOptionPane.OK_OPTION) {
          
          // set the changed variables
          for (Object row : model.getProperties(true)) {
            if (row instanceof PropertiesTableModel.PropertyModel) {
              PropertiesTableModel.PropertyModel p = (PropertiesTableModel.PropertyModel) row;
              if (p.isDirty()) {
                String variable = p.getName();
                Object newValue = p.getValue();
                parser.getVariableResolver().setVariable(variable, newValue);
              }
            }
          }
          MapTool.addLocalMessage("variable added");
        }
      } catch (NullPointerException npe) {
      }
    }
    
    return "OK";
  }
  
  static protected class InspectTable extends PropertyTable
  {
    private static final long serialVersionUID = 1689554114150152927L;
    boolean valuesEditable = false;
    private final WordWrapCellRenderer cellRenderer = new WordWrapCellRenderer();
    
    public InspectTable(PropertiesTableModel model, PropertiesTableModel.PropertyModel select, boolean valuesEditable)
    {
      super(model);
      this.valuesEditable = valuesEditable;
      //this.setCellEditor(new MultilineStringCellEditor());
      this.setDefaultRenderer(String.class, cellRenderer);
      this.setDefaultRenderer(Object.class, cellRenderer);   
      if (select != null) {
        setSelectedProperty(select);
      }
    } 
    
    @Override
    public boolean isCellEditable(int row, int column) {
      if (column == 1) { return valuesEditable; }
      return false;
    };
  }
}
