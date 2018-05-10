package org.slizaa.scanner.core.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.cypherregistry.impl.DependencyGraph;

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
  public List<ICypherStatement> computeOrder(List<ICypherStatement> statements) {

    //
    DependencyGraph<ICypherStatement> dependencyGraph = new DependencyGraph<ICypherStatement>();

    //
    for (ICypherStatement parent : statements) {

      //
      for (String requiredStatement : parent.getRequiredStatements()) {

        //
        ICypherStatement child = this._cypherStatements.get(requiredStatement);

        //
        if (child == null) {
          child = this._cypherStatements.get(parent.getGroupId() + "." + requiredStatement);
        }

        //
        if (child == null) {
          // TODO
          throw new RuntimeException("Missing requirement.");
        }

        //
        dependencyGraph.addEdge(parent, child);
      }
    }

    //
    return dependencyGraph.calculateOrder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICypherStatement> getAllStatements() {
    return Collections.unmodifiableList(new ArrayList<>(this._cypherStatements.values()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICypherStatement> getStatements(String group) {
    checkNotNull(group);
    return this._cypherStatements.values().stream().filter(statement -> group.equalsIgnoreCase(statement.getGroupId()))
        .collect(Collectors.toList());
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
}
