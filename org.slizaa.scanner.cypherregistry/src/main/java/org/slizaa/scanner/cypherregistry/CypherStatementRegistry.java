package org.slizaa.scanner.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.cypherregistry.impl.DependencyGraph;

public class CypherStatementRegistry implements ICypherStatementRegistry {

  /** - */
  private Supplier<List<ICypherStatement>> _supplier;

  /** - */
  private Map<String, ICypherStatement>    _cypherStatements;

  /**
   * <p>
   * Creates a new instance of type {@link CypherStatementRegistry}.
   * </p>
   *
   * @param supplier
   */
  public CypherStatementRegistry(Supplier<List<ICypherStatement>> supplier) {
    this._supplier = checkNotNull(supplier);
    rescan();
  }

  /**
   * <p>
   * </p>
   */
  @Override
  public void rescan() {

    //
    List<ICypherStatement> statements = this._supplier.get();
    this._cypherStatements = new HashMap<>();

    //
    for (ICypherStatement statement : statements) {
      this._cypherStatements.put(statement.getFullyQualifiedName(), statement);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICypherStatement> getAllStatements() {
    return Collections.unmodifiableList(computeOrder(this._cypherStatements.values()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICypherStatement> getStatements(String group) {
    checkNotNull(group);
    return computeOrder(this._cypherStatements.values().stream()
        .filter(statement -> group.equalsIgnoreCase(statement.getGroupId())).collect(Collectors.toList()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ICypherStatement> getStatement(String fullyQualifedName) {
    checkNotNull(fullyQualifedName);
    return this._cypherStatements.values().stream()
        .filter(statement -> fullyQualifedName.equalsIgnoreCase(statement.getFullyQualifiedName())).findFirst();
  }

  /**
   * <p>
   * </p>
   *
   * @param statements
   * @return
   */
  private List<ICypherStatement> computeOrder(Collection<ICypherStatement> statements) {

    // create the dependency graph
    DependencyGraph<ICypherStatement> dependencyGraph = new DependencyGraph<ICypherStatement>();

    checkNotNull(statements).forEach(cypherStatement -> {

      // add node
      dependencyGraph.addVertex(cypherStatement);

      //
      cypherStatement.getRequiredStatements().forEach(requiredStatementName -> {

        //
        ICypherStatement requiredStatement = this._cypherStatements.get(requiredStatementName);

        //
        if (requiredStatement == null) {
          requiredStatement = this._cypherStatements.get(cypherStatement.getGroupId() + "." + requiredStatementName);
        }

        //
        if (requiredStatement == null) {
          // TODO
          throw new RuntimeException(String.format(
              "Missing requirement: statement '%s' requires missing statement '%s'. Registered statements are %s.",
              cypherStatement.getFullyQualifiedName(), requiredStatementName, this._cypherStatements.keySet()));
        }

        //
        dependencyGraph.addEdge(cypherStatement, requiredStatement);
      });
    });

    //
    return dependencyGraph.calculateOrder();
  }

}
