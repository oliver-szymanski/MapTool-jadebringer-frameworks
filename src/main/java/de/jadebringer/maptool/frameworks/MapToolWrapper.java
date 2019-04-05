package de.jadebringer.maptool.frameworks;

import java.util.List;

import de.jadebringer.maptool.extensionframework.FrameworksFunctions;
import net.rptools.maptool.client.MapTool;
import net.rptools.parser.function.Function;

public class MapToolWrapper {

  public static void main(String[] args) {
    FrameworksFunctions frameworkFunctions = FrameworksFunctions.getInstance();
    List<Function> macroFunctions = MapTool.getParser().getMacroFunctions();
    if (!macroFunctions.contains(frameworkFunctions)) {
      MapTool.getParser().getMacroFunctions().add(frameworkFunctions);
    }
    MapTool.main(args);
  }
}
