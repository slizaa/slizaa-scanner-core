package org.slizaa.scanner.core.spi.parser;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ICypherStatementExecutor {

  /**
   * <p>
   * </p>
   *
   * @param cypherStatement
   */
  IResult executeCypherStatement(String cypherStatement);

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static interface IResult {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<String> keys();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    Map<String, Object> single();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<Map<String, Object> > list();

    /**
     * <p>
     * </p>
     *
     * @param mapFunction
     * @return
     */
    <T> List<T> list(Function<Map<String, Object> , T> mapFunction);
  }
}
