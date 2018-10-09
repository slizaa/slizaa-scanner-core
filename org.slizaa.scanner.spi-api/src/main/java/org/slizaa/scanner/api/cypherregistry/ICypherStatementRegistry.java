package org.slizaa.scanner.api.cypherregistry;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ICypherStatementRegistry {

  /**
   * <p>
   * </p>
   *
   */
  void rescan();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<ICypherStatement> getAllStatements();

  /**
   * <p>
   * </p>
   *
   * @param group
   * @return
   */
  List<ICypherStatement> getStatements(String group);

  /**
   * <p>
   * </p>
   *
   * @param fullyQualifedName
   * @return
   */
  Optional<ICypherStatement> getStatement(String fullyQualifedName);
}
