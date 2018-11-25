package org.slizaa.scanner.cypherregistry;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import org.slizaa.scanner.cypherregistry.CypherStatementRegistry;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CypherRegistryTest {

  /** - */
  private ICypherStatementRegistry _statementRegistry;

  @Before
  public void init() {
    this._statementRegistry = new CypherStatementRegistry(
        () -> CypherRegistryUtils.getCypherStatementsFromClasspath(CypherRegistryTest.class));
    this._statementRegistry.rescan();
  }

  @Test
  public void testExistingStatements() {
    assertThat(this._statementRegistry.getAllStatements()).hasSize(3);
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.test-statement-1")).isNotEmpty();
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.test-statement-2")).isNotEmpty();
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.test-statement-3")).isNotEmpty();
  }

  @Test
  public void testNonExistingsStatements() {
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.NOT_EXISTING")).isEmpty();
  }

  @Test
  public void testRequiredStatements() {
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.test-statement-1").get()
        .getRequiredStatements()).containsExactly("test-statement-2");
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.test-statement-2").get()
        .getRequiredStatements()).isEmpty();
    assertThat(this._statementRegistry.getStatement("org.slizaa.example.typeresolution.test-statement-3").get()
        .getRequiredStatements()).containsExactlyInAnyOrder("test-statement-1", "test-statement-2");
  }

  @Test
  public void testCypherStatements() {

    //
    ICypherStatement cypherStatement = this._statementRegistry
        .getStatement("org.slizaa.example.typeresolution.test-statement-1").get();

    //
    assertThat(cypherStatement.getStatement()).isNotNull();
  }

  @Test
  public void testOrder() {
    List<ICypherStatement> cypherStatements = this._statementRegistry.getAllStatements();
    assertThat(cypherStatements).hasSize(3);
    assertThat(cypherStatements.stream().map(s -> s.getStatementId()).collect(Collectors.toList()))
        .containsExactly("test-statement-2", "test-statement-1", "test-statement-3");
  }
}
