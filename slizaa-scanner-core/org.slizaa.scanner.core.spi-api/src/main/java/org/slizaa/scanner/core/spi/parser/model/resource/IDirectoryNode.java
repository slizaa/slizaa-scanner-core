package org.slizaa.scanner.core.spi.parser.model.resource;

public interface IDirectoryNode {

  /**
   * <p>
   * The full path of the directory, e.g. <code>'org/example'</code>. Note that resource paths are always
   * slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_PATH     = "path";

  /**
   * <p>
   * the root directory or archive file that contains the resource (e.g. <code>'c:/dev/classes.zip'</code> or
   * <code>'c:/dev/source'</code>). Note that resource paths are always slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_ROOT     = "root";

  /** - */
  public static final String PROPERTY_IS_EMPTY = "isEmpty";
}
