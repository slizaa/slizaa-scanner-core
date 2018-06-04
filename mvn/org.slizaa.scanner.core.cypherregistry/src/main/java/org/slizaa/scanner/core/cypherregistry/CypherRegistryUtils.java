package org.slizaa.scanner.core.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;

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
    ClassLoader pathToScan = new URLClassLoader(
        new URL[] { checkNotNull(clazz).getProtectionDomain().getCodeSource().getLocation() });

    //
    IClasspathScannerFactory classpathScanner = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl).create();

    //
    List<ICypherStatement> statements = new ArrayList<>();

    //
    classpathScanner.createScanner(pathToScan).matchFiles("cypher",

        // parse the statements
        (relativePath, inputStream, lengthBytes) -> {
          DefaultCypherStatement statement = SlizaaCypherFileParser.parse(relativePath, inputStream);
          statement.setRelativePath(relativePath);
          return statement;
        },

        // fill the collector
        (codeSource, statementList) -> {
          for (ICypherStatement cypherStatement : statementList) {
            if (cypherStatement.isValid()) {
              ((DefaultCypherStatement) cypherStatement).setCodeSource(codeSource);
              statements.add(cypherStatement);
            }
          }
        }).scan();

    //
    return statements;
  }
}
