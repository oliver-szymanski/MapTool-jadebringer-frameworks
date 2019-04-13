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
package de.jadebringer.maptool.extension.hook;

import de.jadebringer.maptool.extension.base.BaseExtension;
import de.jadebringer.maptool.extension.base.chatmacros.CallMacro;
import de.jadebringer.maptool.extension.hook.EventDispatcher.Event;
import de.jadebringer.maptool.extension.hook.EventDispatcher.EventHandler;
import de.jadebringer.maptool.extension.hook.ui.BaseComponentListener;
import de.jadebringer.maptool.extension.hook.ui.ButtonFrame;
import java.awt.EventQueue;
import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.rptools.common.expression.ExpressionParser;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.maptool.client.macro.MacroContext;
import net.rptools.maptool.client.macro.MacroDefinition;
import net.rptools.maptool.client.macro.MacroManager;
import net.rptools.maptool.language.I18N;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.Function;
import net.rptools.parser.function.ParameterException;
import net.sf.json.JSONObject;

/** @author oliver.szymanski */
public class ExtensionFunctions implements Function, EventHandler {
  private static final ExtensionFunctions instance = new ExtensionFunctions();
  private static final String IMPORT_FUNCTIONS_BUNDLE = "importFunctionsBundle";
  private static final String INIT_EXTENSION = "initExtensions";
  private static final String RESET_EXTENSION = "resetExtensions";
  private static final String UNPACK_ARGS_FUNCTION_NAME = "unpackArgs";
  private static final String TOGGLE_CHAT = "toggleChat";
  private final String[] FUNCTION_NAMES = {
    IMPORT_FUNCTIONS_BUNDLE, INIT_EXTENSION, RESET_EXTENSION, UNPACK_ARGS_FUNCTION_NAME, TOGGLE_CHAT
  };

  private static final AccessControlContext accessControlContextForExtensionFunctions;
  public static final AccessControlContext accessControlContextAllAllowed;

  private final int minParameters;
  private final int maxParameters;
  private final boolean deterministic;
  private volatile boolean windowComponentListening = false;

  private List<ExtensionClassLoader> extensionsClassLoader = new LinkedList<>();
  private Map<ExtensionClassLoader, String> extensionsClassLoaderToLibs = new HashMap<>();;

  private final List<ExtensionFunction> extensionFunctions = new LinkedList<>();
  private final List<ExtensionChatMacro> extensionChatMacros = new LinkedList<>();
  private final Map<String, ExtensionFunction> extensionFunctionsAliasMap = new HashMap<>();
  private final Map<String, String> extensionAliasPrefixMap = new HashMap<>();
  private Map<String, ButtonFrame> buttonFrames = new HashMap<>();;
  private String[] aliases;

  static {
    // initialization of the allowed permissions
    PermissionCollection allowedPermissions = new Permissions();
    // allowedPermissions.add(new PropertyPermission("*", "read"));
    // allowedPermissions.add(new RuntimePermission("accessDeclaredMembers"));
    // ... <many more permissions here> ...
    accessControlContextForExtensionFunctions =
        new AccessControlContext(
            new ProtectionDomain[] {new ProtectionDomain(null, allowedPermissions)});

    PermissionCollection allPermissions = new Permissions();
    allPermissions.add(new AllPermission());
    accessControlContextAllAllowed =
        new AccessControlContext(
            new ProtectionDomain[] {
              new ProtectionDomain(
                  MapTool.class.getProtectionDomain().getCodeSource(), allPermissions),
              new ProtectionDomain(
                  ExtensionFunctions.class.getProtectionDomain().getCodeSource(), allPermissions)
            });
  }

  private ExtensionFunctions() {
    this.minParameters = 2;
    this.maxParameters = 2;
    this.deterministic = true;
    init();
  }

  private List<File> getPossibleExtensions(File directory) {
    List<File> files = new LinkedList<>();

    for (File file : directory.listFiles()) {
      if (file.isFile()) {
        if (file.getName().endsWith(".jar")) {
          files.add(file);
        }
      } else if (file.isDirectory()) {
        files.addAll(getPossibleExtensions(file));
      }
    }

    return files;
  }

  protected String[] getExtensionFunctionsNames() {
    return FUNCTION_NAMES;
  }

  public synchronized void init() {
    extensionFunctions.clear();
    extensionFunctionsAliasMap.clear();
    extensionAliasPrefixMap.clear();

    for (@SuppressWarnings("unused") ExtensionChatMacro chatMacro : extensionChatMacros) {
      // currently MT does not support to remove a macro
    }

    for (ButtonFrame buttonFrame : buttonFrames.values()) {
      buttonFrame.hide();
      buttonFrame.clear();
    }
    buttonFrames.clear();

    SecurityManager current = System.getSecurityManager();
    if (current == null) {
      // make sure at least some default policy/security manager is setup
      // one is required to have access control on extensions code
      // by default Java does not have one

      Policy.setPolicy(new AllPermissionsPolicy());
      System.setSecurityManager(new SecurityManagerPackageAccess());
    }

    extensionsClassLoader.clear();
    extensionsClassLoaderToLibs.clear();

    // init call macro, as the loadBaseExtension during getAliases is to late for that
    ExtensionChatMacro callMacro = new CallMacro();
    MacroDefinition macroDefinition = callMacro.getClass().getAnnotation(MacroDefinition.class);
    if (macroDefinition != null) {
      MacroManager.registerMacro(callMacro);
    }

    registerMapToolListener();
  }

  private void loadBaseExtension() {
    try {
      Parser parser = new Parser();
      this.importFunctionsBundle(
          parser,
          true,
          "importFunctionsBundle",
          FunctionCaller.toObjectList("", BaseExtension.class.getName()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized void initExtensionsFromExtensionDirectory() {
    // try to get extensions jar libs from default extension-extensions folder
    File extensionsDirectory = new File("./extension-extensions");

    if (extensionsDirectory.exists()
        && extensionsDirectory.canRead()
        && extensionsDirectory.isDirectory()) {
      List<File> possibleExtensionLibs = getPossibleExtensions(extensionsDirectory);

      for (File extensionLib : possibleExtensionLibs) {
        MapTool.addLocalMessage(
            "found possible extension extension: " + extensionLib.getAbsolutePath());
      }

      for (File extensionLib : possibleExtensionLibs) {
        initExtension(extensionLib);
      }
    } else {
      MapTool.addLocalMessage(
          "no extension-extensions directory found: " + extensionsDirectory.getAbsolutePath());
    }
  }

  private void initExtension(File extensionLib) {
    try {
      initExtension(extensionLib.toURI().toURL());
    } catch (MalformedURLException e) {
      MapTool.addLocalMessage("failed init extension extension: " + extensionLib.getAbsolutePath());
      throw new RuntimeException(e);
    }
  }

  private synchronized void initExtension(String extensionLibURL) {
    try {
      initExtension(new URL(extensionLibURL));
    } catch (MalformedURLException e) {
      MapTool.addLocalMessage("failed init extension extension: " + extensionLibURL);
      throw new RuntimeException(e);
    }
  }

  private void initExtension(URL extensionLibURL) {
    if (!MapTool.confirm(
        "Really want to load {0}?\n\nIt's a security risk to load extension extensions.\nMake sure you got the file from a trusted source.",
        extensionLibURL.toString())) {
      return;
    }
    MapTool.addLocalMessage("init extension extension: " + extensionLibURL.toString());

    ExtensionClassLoader extensionClassLoader =
        new ExtensionClassLoader(new URL[] {}, this.getClass().getClassLoader());
    extensionClassLoader.addURL(extensionLibURL);

    // add classloader to front of the list so that a later added one
    // can override function/macro definitions
    extensionsClassLoader.add(0, extensionClassLoader);
    extensionsClassLoaderToLibs.put(extensionClassLoader, extensionLibURL.getFile());
  }

  public static ExtensionFunctions getInstance() {
    return instance;
  }

  private synchronized void registerMapToolListener() {
    if (windowComponentListening) return;

    if (MapTool.getFrame() != null
        && MapTool.getFrame().getCommandPanel() != null
        && MapTool.getEventDispatcher() != null) {
      windowComponentListening = true;
      MapTool.getFrame().addComponentListener(BaseComponentListener.instance);
      MapTool.getFrame().getInitiativePanel().addComponentListener(BaseComponentListener.instance);
      MapTool.getFrame().getGlassPane().addComponentListener(BaseComponentListener.instance);
      EventDispatcher.getInstance().init();
      EventDispatcher.getInstance().addEventHandler(this);
    } else {
      // try to register later
      EventQueue.invokeLater(
          new Runnable() {
            public void run() {
              registerMapToolListener();
            }
          });
    }
  }

  public Object childEvaluate(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    // only untrusted function so far
    if (UNPACK_ARGS_FUNCTION_NAME.equals(functionName)) {
      Object message = FunctionCaller.getParam(parameters, 0);
      return JSONObject.fromObject(message).get("args");
    }

    // all extension function need to be trusted
    if (!MapTool.getParser().isMacroTrusted()) {
      throw new ParserException(I18N.getText("macro.function.general.noPerm", functionName));
    }

    if (IMPORT_FUNCTIONS_BUNDLE.equals(functionName)) {
      return importFunctionsBundle(parser, false, IMPORT_FUNCTIONS_BUNDLE, parameters);
    } else if (RESET_EXTENSION.equals(functionName)) {
      init();
      return BigDecimal.ONE;
    } else if (INIT_EXTENSION.equals(functionName)) {
      if (parameters.size() == 0) {
        // auto add from extension-extensions sub directory
        initExtensionsFromExtensionDirectory();
      } else {
        for (Object parameter : parameters) {
          // get from a file or http
          initExtension(parameter.toString());
        }
      }
      return BigDecimal.ONE;
    } else if (TOGGLE_CHAT.equals(functionName)) {
      //      TranslucentFrame chatFrame = new TranslucentFrame("Chat", "", "Chat", true);
      //    chatFrame.show();
      return BigDecimal.ONE;
    } else {
      return executeFunction(parser, functionName, parameters);
    }
  }

  private synchronized Object importFunctionsBundle(
      Parser parser, boolean silent, String functionName, List<Object> parameters)
      throws ParameterException, ParserException {
    this.checkParameters(parameters);

    if (parameters.size() < 2) {
      throw new ParserException(
          I18N.getText(
              "macro.function.general.notEnoughParam", functionName, 2, parameters.size()));
    }

    List<String> newFunctionNames = new LinkedList<String>();
    List<String> newChatMacros = new LinkedList<String>();
    Set<String> newButtonFrames = new HashSet<String>();

    String prefix = FunctionCaller.getParam(parameters, 0);
    String extensionFunctionBundle = FunctionCaller.getParam(parameters, 1);

    try {

      ExtensionBundle extension = null;

      // check all extension lib classloader in order
      for (ExtensionClassLoader extensionClassLoader : extensionsClassLoader) {
        try {
          extension =
              (ExtensionBundle)
                  Class.forName(extensionFunctionBundle.toString(), true, extensionClassLoader)
                      .getDeclaredConstructor()
                      .newInstance();

          MapTool.addLocalMessage(
              "imported bundle: '"
                  + extensionFunctionBundle.toString()
                  + "' from '"
                  + extensionsClassLoaderToLibs.get(extensionClassLoader)
                  + "'");
        } catch (Exception e) {
          // safe to ignore
        }
      }

      // try current classloader if nothing yet found
      if (extension == null) {
        try {
          extension =
              (ExtensionBundle)
                  Class.forName(
                          extensionFunctionBundle.toString(),
                          true,
                          this.getClass().getClassLoader())
                      .getDeclaredConstructor()
                      .newInstance();
          if (!silent && MapTool.getPlayer() != null) {
            MapTool.addLocalMessage(
                "imported bundle: '"
                    + extensionFunctionBundle.toString()
                    + "' from base libraries.");
          }
        } catch (Exception e) {
          // safe to ignore
        }
      }

      // check if bundle was found
      if (extension == null && MapTool.getPlayer() != null) {
        MapTool.addLocalMessage(
            "bundle not found in any lib: " + extensionFunctionBundle.toString());
        return BigDecimal.ZERO;
      }

      Collection<? extends ExtensionFunction> functions = extension.getFunctions();
      Collection<? extends ExtensionFunctionButton> functionButtons =
          extension.getFunctionButtons();
      Collection<? extends ExtensionChatMacro> chatMacros = extension.getChatMacros();

      // are we running in trusted context
      boolean trusted = MapTool.getParser().isMacroPathTrusted() || MapTool.getPlayer().isGM();

      // init extension functions
      for (ExtensionFunction function : functions) {
        if (extensionFunctions.contains(function)) {
          // if overridden remove and add again
          extensionFunctions.remove(function);
        }
        extensionFunctions.add(function);
        
        String localPrefix = "";
        if (prefix != null && prefix.length() > 0) {
          localPrefix = prefix + "_";
          function.setPrefix(localPrefix);
        }

        for (String alias : function.getAliases()) {
          String aliasWithPrefix = localPrefix + alias;
          extensionAliasPrefixMap.put(aliasWithPrefix, alias);
          extensionFunctionsAliasMap.put(aliasWithPrefix, function);
          newFunctionNames.add(aliasWithPrefix);
        }
      }

      // init chat macros
      for (ExtensionChatMacro chatMacro : chatMacros) {
        if (chatMacro.isTrustedRequired() && !trusted) {
          continue;
        }
        chatMacro.setPrefix(prefix);
        MacroDefinition macroDefinition = chatMacro.getClass().getAnnotation(MacroDefinition.class);
        if (macroDefinition == null) continue;
        MacroManager.registerMacro(chatMacro);
        newChatMacros.add(macroDefinition.name());
        extensionChatMacros.add(chatMacro);
      }

      // init extension function buttons
      for (ExtensionFunctionButton functionButton : functionButtons) {
        if (functionButton.isTrustedRequired() && !trusted) {
          continue;
        }

        this.addExtensionFunctionButton(functionButton, prefix);
        newButtonFrames.add(functionButton.getPrefixedFrameId());
      }
    } catch (Exception e) {
      if (MapTool.getPlayer() != null) {
        MapTool.addLocalMessage(
            "could not load bundle (maybe it's libary was not imported): "
                + extensionFunctionBundle.toString()
                + "("
                + e.toString()
                + ")");
      } else {
        e.printStackTrace();
      }
      return BigDecimal.ZERO;
    }

    String functions = newFunctionNames.stream().collect(Collectors.joining(", "));
    String buttonFrames = newButtonFrames.stream().collect(Collectors.joining(", "));
    String macros = newChatMacros.stream().collect(Collectors.joining(", "));

    // make sure the alias list will be recalculated next time it is accessed
    this.aliases = null;

    // MapTool might not be fully initialized yet if this is called via startup
    // this check prevents a null pointer exception which would happen sending the messages
    if (!silent && MapTool.getPlayer() != null) {
      MapTool.addLocalMessage(
          "bundle " + extensionFunctionBundle + " defined chat macros: " + macros);
      MapTool.addLocalMessage(
          "bundle " + extensionFunctionBundle + " defined button frames macros: " + buttonFrames);
      MapTool.addLocalMessage(
          "bundle " + extensionFunctionBundle + " defined functions: " + functions);
    }

    return BigDecimal.ONE;
  }

  public ExtensionFunctionButton getExtensionFunctionButton(
      String name, String group, String frame, String prefix) {
    if (prefix != null && prefix.length() > 0) {
      frame = prefix + frame;
    }

    ButtonFrame buttonFrame = buttonFrames.get(frame);
    if (buttonFrame == null) {
      return null;
    }

    return buttonFrame.getExtensionFunctionButton(name, group);
  }

  public void addExtensionFunctionButton(ExtensionFunctionButton extensionFunctionButton) {
    addExtensionFunctionButton(extensionFunctionButton, null);
  }

  public ButtonFrame getButtonFrame(String frame, String prefix) {
    if (prefix != null && prefix.length() > 0) {
      frame = prefix + frame;
    }

    return buttonFrames.get(frame);
  }

  public ButtonFrame addExtensionFunctionButton(
      ExtensionFunctionButton extensionFunctionButton, String prefix) {
    extensionFunctionButton.setPrefix(prefix);
    String frame = extensionFunctionButton.getPrefixedFrameId();
    ButtonFrame buttonFrame = buttonFrames.get(frame);

    if (buttonFrame == null) {
      buttonFrame =
          new ButtonFrame(
              extensionFunctionButton.getFrame(),
              extensionFunctionButton.getPrefixedFrame(),
              extensionFunctionButton.getPrefixedFrameId());
      buttonFrames.put(frame, buttonFrame);
    }

    buttonFrame.add(extensionFunctionButton);
    return buttonFrame;
  }

  private Object executeFunction(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    String aliasWithoutPrefix = extensionAliasPrefixMap.get(functionName);
    ExtensionFunction function = extensionFunctionsAliasMap.get(functionName);

    if (function != null) {
      return executeExtensionFunctionWithAccessControl(
          parser, parameters, aliasWithoutPrefix, function);
    }

    return BigDecimal.ZERO;
  }

  public boolean isFrameVisible(String frame) {
    if (!buttonFrames.containsKey(frame)) {
      return false;
    }

    try {
      return executePrivileged(
          new Run<Boolean>() {
            public Boolean run() {
              return buttonFrames.get(frame).isVisible();
            }
          });
    } catch (Exception e) {
      return false;
    }
  }

  public void showAllFrames() {
    for (String frame : this.buttonFrames.keySet()) {
      showFrame(frame);
    }
  }

  public void hideAllFrames() {
    for (String frame : this.buttonFrames.keySet()) {
      hideFrame(frame);
    }
  }

  public boolean showFrame(String frame) {
    if (!buttonFrames.containsKey(frame)) {
      return false;
    }

    try {
      return executePrivileged(
          new Run<Boolean>() {
            public Boolean run() {
              return buttonFrames.get(frame).show();
            }
          });
    } catch (Exception e) {
      return false;
    }
  }

  public boolean hideFrame(String frame) {
    if (!buttonFrames.containsKey(frame)) {
      return false;
    }

    try {
      return executePrivileged(
          new Run<Boolean>() {
            public Boolean run() {
              return buttonFrames.get(frame).hide();
            }
          });
    } catch (Exception e) {
      return false;
    }
  }

  static <T> T executePrivileged(Run<T> runnable) throws Exception {
    // run the runnable in a controlled environment without restrictions
    T result =
        AccessController.doPrivileged(
            (PrivilegedExceptionAction<T>)
                () -> {
                  return runnable.run();
                });
    return result;
  }

  static <T> T executeNotPrivileged(Run<T> runnable, boolean restrictFurther)
      throws ParserException {
    T result = null;
    try {
      // run runnable in a controlled environment with restrictions
      // permissions are given via the accessControlContext.
      // package access is controlled by permissions and the exceptions in
      // SecurityManagerPackageAccess
      // (Functions loaded as extension but inside the main libs are
      // having normal access control).

      // classes calling this are already restricted by ACC during classloading
      // and by security manager. only call doPriviledged with another
      // ACC if you want to further reduce the access even for whatever
      // is called now in the callstack.
      if (!restrictFurther) {
        result = runnable.run();
      } else {
        AccessController.doPrivileged(
            new PrivilegedExceptionAction<Object>() {
              public T run() throws Exception {
                return runnable.run();
              }
            },
            accessControlContextForExtensionFunctions);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ParserException(e);
    }
    return result;
  }

  static Object executeExtensionFunctionWithAccessControl(
      Parser parser, List<Object> parameters, String alias, ExtensionFunction function)
      throws ParserException {
    Object result =
        executeNotPrivileged(
            new Run<Object>() {
              public Object run() throws ParserException {
                return function.run(parser, alias, parameters);
              }
            },
            false);
    return result;
  }

  static void executeExtensionChatMacroWithAccessControl(
      ExtensionChatMacro chatMacro,
      MacroContext context,
      String macroName,
      MapToolMacroContext executionContext) {
    try {
      executeNotPrivileged(
          new Run<Object>() {
            public Object run() throws ParserException {
              chatMacro.run(context, macroName, executionContext);
              return null;
            }
          },
          false);
    } catch (ParserException e) {
      e.printStackTrace();
    }
  }

  static void executeExtensionFunctionButtonWithAccessControl(
      ExtensionFunctionButton functionButton) throws ParserException {
    executeNotPrivileged(
        new Run<Object>() {
          public Object run() throws ParserException {
            MapToolVariableResolver resolver = new MapToolVariableResolver(null);
            ExpressionParser parser = new ExpressionParser(resolver);
            parser.getParser().addFunctions(MapTool.getParser().getMacroFunctions());
            functionButton.run(parser.getParser());
            return null;
          }
        },
        false);
  }

  public String[] getAliases() {
    if (this.extensionFunctions == null || this.extensionFunctions.isEmpty()) {
      loadBaseExtension();
    }

    if (aliases == null) {
      // cache current alias list
      String[] extensionAliases = extensionFunctionsAliasMap.keySet().toArray(new String[] {});
      String[] buildInExtensionAliases = getExtensionFunctionsNames();
      String[] allAliases = new String[buildInExtensionAliases.length + extensionAliases.length];
      System.arraycopy(buildInExtensionAliases, 0, allAliases, 0, buildInExtensionAliases.length);
      System.arraycopy(
          extensionAliases, 0, allAliases, buildInExtensionAliases.length, extensionAliases.length);
      this.aliases = allAliases;
    }

    return aliases;
  }

  public int getMinimumParameterCount() {
    return this.minParameters;
  }

  public int getMaximumParameterCount() {
    return this.maxParameters;
  }

  public boolean isDeterministic() {
    return this.deterministic;
  }

  public void checkParameters(List<Object> parameters) throws ParameterException {
    int pCount = parameters == null ? 0 : parameters.size();
    if (pCount < this.minParameters
        || this.maxParameters != -1 && parameters.size() > this.maxParameters) {
      throw new ParameterException(
          String.format(
              "Invalid number of parameters %d, expected %s",
              pCount, this.formatExpectedParameterString()));
    }
  }

  public final Object evaluate(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    return this.childEvaluate(parser, functionName, parameters);
  }

  private String formatExpectedParameterString() {
    if (this.minParameters == this.maxParameters) {
      return String.format("exactly %d parameter(s)", this.maxParameters);
    }
    if (this.maxParameters == -1) {
      return String.format("at least %d parameters", this.minParameters);
    }
    return String.format("between %d and %d parameters", this.minParameters, this.maxParameters);
  }

  private static class AllPermissionsPolicy extends Policy {

    private static volatile PermissionCollection perms;

    public AllPermissionsPolicy() {
      super();

      if (perms == null) {
        synchronized (AllPermissionsPolicy.class) {
          perms = new Permissions();
          perms.add(new AllPermission());
        }
      }
    }

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
      return perms;
    }
  }

  public static interface Run<T> {
    T run() throws Exception;
  }

  @Override
  public void handleEvent(Event event) {
    if (EventDispatcher.EventType.CampaignEvent.name().equals(event.getEventType())
        && (EventDispatcher.EventSubType.CampaignStopping.name().equals(event.getEventSubType())
        )) {
      this.init();
    }
  }

  @Override
  public boolean handleVetoableEvent(Event event) {
    return true;
  }
}
