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

public class Version implements Comparable<Version> {

  private static final Version UNKNOWN_VERSION = new Version(0, 0, 0, null, null, null);

  protected final int _majorVersion;

  protected final int _minorVersion;

  protected final int _patchLevel;

  protected final String _groupId;

  protected final String _artifactId;

  /** Additional information for snapshot versions; null for non-snapshot (release) versions. */
  protected final String _snapshotInfo;

  public Version(int major, int minor, int patchLevel, String snapshotInfo) {
    this(major, minor, patchLevel, snapshotInfo, null, null);
  }

  public Version(
      int major,
      int minor,
      int patchLevel,
      String snapshotInfo,
      String groupId,
      String artifactId) {
    _majorVersion = major;
    _minorVersion = minor;
    _patchLevel = patchLevel;
    _snapshotInfo = snapshotInfo;
    _groupId = (groupId == null) ? "" : groupId;
    _artifactId = (artifactId == null) ? "" : artifactId;
  }

  /**
   * Method returns canonical "not known" version, which is used as version in cases where actual
   * version information is not known (instead of null).
   */
  public static Version unknownVersion() {
    return UNKNOWN_VERSION;
  }

  /** @since 2.7 to replace misspelled {@link #isUknownVersion()} */
  public boolean isUnknownVersion() {
    return (this == UNKNOWN_VERSION);
  }

  public boolean isSnapshot() {
    return (_snapshotInfo != null && _snapshotInfo.length() > 0);
  }

  /** @deprecated Since 2.7 use correctly spelled method {@link #isUnknownVersion()} */
  @Deprecated
  public boolean isUknownVersion() {
    return isUnknownVersion();
  }

  public int getMajorVersion() {
    return _majorVersion;
  }

  public int getMinorVersion() {
    return _minorVersion;
  }

  public int getPatchLevel() {
    return _patchLevel;
  }

  public String getGroupId() {
    return _groupId;
  }

  public String getArtifactId() {
    return _artifactId;
  }

  public String toFullString() {
    return _groupId + '/' + _artifactId + '/' + toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(_majorVersion).append('.');
    sb.append(_minorVersion).append('.');
    sb.append(_patchLevel);
    if (isSnapshot()) {
      sb.append('-').append(_snapshotInfo);
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return _artifactId.hashCode()
        ^ _groupId.hashCode() + _majorVersion - _minorVersion + _patchLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != getClass()) return false;
    Version other = (Version) o;
    return (other._majorVersion == _majorVersion)
        && (other._minorVersion == _minorVersion)
        && (other._patchLevel == _patchLevel)
        && other._artifactId.equals(_artifactId)
        && other._groupId.equals(_groupId);
  }

  @Override
  public int compareTo(Version other) {
    if (other == this) return 0;

    int diff = _groupId.compareTo(other._groupId);
    if (diff == 0) {
      diff = _artifactId.compareTo(other._artifactId);
      if (diff == 0) {
        diff = _majorVersion - other._majorVersion;
        if (diff == 0) {
          diff = _minorVersion - other._minorVersion;
          if (diff == 0) {
            diff = _patchLevel - other._patchLevel;
          }
        }
      }
    }
    return diff;
  }
}
