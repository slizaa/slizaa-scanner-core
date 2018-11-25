package org.slizaa.scanner.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import org.slizaa.scanner.api.cypherregistry.ICypherStatement;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CypherRegistryUtils {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static List<ICypherStatement> getCypherStatementsFromClasspath(Class<?> clazz) {

    // create test class loader
    ClassLoader classLoader = new URLClassLoader(
        new URL[] { checkNotNull(clazz).getProtectionDomain().getCodeSource().getLocation() });

    //
    return getCypherStatementsFromClasspath(classLoader);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static List<ICypherStatement> getCypherStatementsFromClasspath(ClassLoader... classLoaders) {

    //
    List<ICypherStatement> statements = new ArrayList<>();

    //
    try (ScanResult scanResult =
             new ClassGraph()
                 .overrideClassLoaders(classLoaders)
                 .scan()) {

      scanResult
          .getResourcesWithExtension("cypher")
          .forEachByteArray((Resource res, byte[] fileContent) -> {
            DefaultCypherStatement cypherStatement = SlizaaCypherFileParser
                .parse(res.getPathRelativeToClasspathElement(), new String(fileContent));
            cypherStatement.setRelativePath(res.getPathRelativeToClasspathElement());
            if (cypherStatement.isValid()) {
              statements.add(cypherStatement);
            }
          });
    }

    //
    return statements;
  }
}
