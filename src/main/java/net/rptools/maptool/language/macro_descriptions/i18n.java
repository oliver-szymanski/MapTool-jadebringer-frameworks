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
package net.rptools.maptool.language.macro_descriptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

public class i18n extends ResourceBundle {

  private static final String PROPERTIES_FILE_SUFFIX = ".properties";
  protected List<ResourceBundle> bundles;

  public i18n() throws IOException {
    this.bundles = initResourceBundles();
  }

  private ResourceBundle loadResourceBundlePropertiesFile(String fileName) throws IOException {
    URL url = this.getClass().getClassLoader().getResource(fileName);
    URLConnection connection = url.openConnection();
    connection.setUseCaches(false);
    ResourceBundle bundle = null;
    try (InputStream stream = connection.getInputStream()) {
      bundle = new PropertyResourceBundle(stream);
    }

    return bundle;
  }

  private final String toFilePath(String bundleName) {
    return bundleName.replace('.', '/');
  }

  protected List<ResourceBundle> initResourceBundles() throws IOException {
    List<ResourceBundle> bundles = new LinkedList<>();
    String className = this.getClass().getSimpleName();
    String packageName = this.getClass().getPackageName();
    bundles.add(
        loadResourceBundlePropertiesFile(
            toFilePath(packageName)
                .concat("/extension.")
                .concat(className)
                .concat(PROPERTIES_FILE_SUFFIX)));
    bundles.add(
        loadResourceBundlePropertiesFile(
            toFilePath(packageName).concat("/").concat(className).concat(PROPERTIES_FILE_SUFFIX)));
    return bundles;
  }

  public List<ResourceBundle> resourceBundle() {
    return bundles;
  }

  @Override
  protected Object handleGetObject(String key) {
    if (bundles != null) {
      for (ResourceBundle bundle : bundles) {
        if (bundle.containsKey(key)) {
          return bundle.getObject(key);
        }
      }
    }

    return null;
  }

  @Override
  public Enumeration<String> getKeys() {
    Set<String> keys = new HashSet<String>();

    if (bundles != null) {
      for (ResourceBundle bundle : bundles) {
        keys.addAll(Collections.list(bundle.getKeys()));
      }
    }

    return Collections.enumeration(keys);
  }
}
