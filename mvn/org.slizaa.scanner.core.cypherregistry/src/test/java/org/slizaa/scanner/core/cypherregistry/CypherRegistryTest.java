package org.slizaa.scanner.core.cypherregistry;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.classpathscanner.internal.ClasspathScannerFactory;

public class CypherRegistryTest {

  @Test
  public void testIt() {

    //
    ICypherStatementRegistry statementRegistry = new CypherStatementRegistry(() -> getCypherStatements());
    statementRegistry.rescan();

    System.out.println(statementRegistry.getAllStatements());

    //
    assertThat(statementRegistry.getAllStatements()).hasSize(2);
    assertThat(statementRegistry.getStatement("org.slizaa.jtype.typeresolution.type-references")).isNotEmpty();
    assertThat(statementRegistry.getStatement("org.slizaa.jtype.typeresolution.type-references-1")).isNotEmpty();

    //
    assertThat(statementRegistry.getStatement("org.slizaa.jtype.typeresolution.type-references-2")).isEmpty();
  }

  private List<ICypherStatement> getCypherStatements() {

    // create test class loader
    ClassLoader pathToScan = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    //
    IClasspathScannerFactory classpathScanner = new ClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    List<ICypherStatement> statements = new ArrayList<>();

    //
    classpathScanner.createScanner(pathToScan).matchFiles("cypher",

        // parse the statements
        (relativePath, inputStream, lengthBytes) -> {
          DefaultCypherStatement statement = SlizaaCypherFileParser.parse(inputStream);
          statement.setRelativePath(relativePath);
          return statement;
        },

        // fill the collector
        (codeSource, statementList) -> {
          for (ICypherStatement cypherStatement : statementList) {
            ((DefaultCypherStatement) cypherStatement).setCodeSource(codeSource);
            statements.add(cypherStatement);
          }
        }).scan();

    //
    return statements;
  }
}
