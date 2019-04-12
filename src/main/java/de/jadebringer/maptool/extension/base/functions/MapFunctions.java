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

import de.jadebringer.maptool.extension.hook.ExtensionFunction;
import de.jadebringer.maptool.extension.hook.FunctionCaller;
import java.awt.Rectangle;
import java.util.List;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.CellPoint;
import net.rptools.maptool.model.ZonePoint;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.sf.json.JSONObject;

/** @author oliver.szymanski */
public class MapFunctions extends ExtensionFunction {

  public static final String MAPS_GET_EXTENT = "maps_getExtent";
  public static final String MAPS_EXTENT = "maps_extent";
  public static final String MAPS_CENTER = "maps_center";

  protected MapFunctions() {
    super(
        Alias.create(MAPS_CENTER, 0, 0),
        Alias.create(MAPS_EXTENT, 0, 0),
        Alias.create(MAPS_GET_EXTENT, 0, 0));
    setTrustedRequired(false);
  }

  private static final MapFunctions instance = new MapFunctions();
  private static final String EQUALS = "=";

  public static MapFunctions getInstance() {
    return instance;
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {

    if (MAPS_CENTER.equals(functionName)) {
      return center(parser, functionName, parameters);
    } else if (MAPS_EXTENT.equals(functionName)) {
      return extent(parser, functionName, parameters);
    } else if (MAPS_GET_EXTENT.equals(functionName)) {
      return getExtent(parser, functionName, parameters);
    }

    return throwNotFoundParserException(functionName);
  }

  public Object center(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    ZoneRenderer renderer = MapTool.getFrame().getCurrentZoneRenderer();
    Rectangle rectangle = renderer.zoneExtents(renderer.getPlayerView());
    int centerX = (rectangle.width / 2) + rectangle.x;
    int centerY = (rectangle.height / 2) + rectangle.y;
    MapTool.getFrame().getCurrentZoneRenderer().centerOn(new ZonePoint(centerX, centerY));
    return "";
  }

  public Object extent(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    ZoneRenderer renderer = MapTool.getFrame().getCurrentZoneRenderer();
    Rectangle rectangle = renderer.zoneExtents(renderer.getPlayerView());
    MapTool.getFrame()
        .getCurrentZoneRenderer()
        .enforceView(rectangle.x, rectangle.y, 1.0, rectangle.width, rectangle.height);
    center(parser, functionName, parameters);
    return "";
  }

  public Object getExtent(Parser parser, String functionName, List<Object> parameters)
      throws ParserException {
    ZoneRenderer renderer = MapTool.getFrame().getCurrentZoneRenderer();
    Rectangle rectangle = renderer.zoneExtents(renderer.getPlayerView());
    rectangle.width = rectangle.x + rectangle.width;
    rectangle.height = rectangle.y + rectangle.height;

    boolean pixels = FunctionCaller.toBoolean(FunctionCaller.getParam(parameters, 0, true));
    String delim = FunctionCaller.getParam(parameters, 1, ";");

    if (!pixels) {
      ZonePoint zp = new ZonePoint(rectangle.x, rectangle.y);
      CellPoint cp = renderer.getZone().getGrid().convert(zp);
      rectangle.x = cp.x;
      rectangle.y = cp.y;
      zp = new ZonePoint(rectangle.width, rectangle.height);
      cp = renderer.getZone().getGrid().convert(zp);
      rectangle.width = cp.x;
      rectangle.height = cp.y;
    }

    if ("json".equalsIgnoreCase(delim)) {
      return createBoundsAsJSON(
          rectangle.x, rectangle.y, rectangle.width + rectangle.x, rectangle.height + rectangle.y);
    } else {
      return createBoundsAsStringProps(
          delim,
          rectangle.x,
          rectangle.y,
          rectangle.width + rectangle.x,
          rectangle.height + rectangle.y);
    }
  }

  private Object createBoundsAsStringProps(
      String delim, int offsetX, int offsetY, int width, int height) {
    StringBuffer sb = new StringBuffer();
    sb.append("startX").append(EQUALS).append(offsetX).append(delim);
    sb.append("startY").append(EQUALS).append(offsetY).append(delim);
    sb.append("endX").append(EQUALS).append(width).append(delim);
    sb.append("endY").append(EQUALS).append(height);
    return sb.toString();
  }

  private JSONObject createBoundsAsJSON(int offsetX, int offsetY, int width, int height) {
    JSONObject bounds = new JSONObject();
    bounds.put("startX", offsetX);
    bounds.put("startY", offsetY);
    bounds.put("endX", width);
    bounds.put("endY", height);
    return bounds;
  }
}
