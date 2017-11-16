package org.slizaa.scanner.core.api.cypherregistry;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ICypherStatement {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getGroupId();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getStatementId();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getFullyQualifiedName();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getDescription();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<String> getRequiredStatements();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getStatement();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  Object getCodeSource();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getRelativePath();
}
