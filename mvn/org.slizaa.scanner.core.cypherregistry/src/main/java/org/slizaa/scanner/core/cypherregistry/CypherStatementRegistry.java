package org.slizaa.scanner.core.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;

public class CypherStatementRegistry implements ICypherStatementRegistry {

  private Supplier<List<ICypherStatement>> _supplier;

  /** - */
  private List<ICypherStatement>           _cypherStatements;

  /**
   * <p>
   * Creates a new instance of type {@link CypherStatementRegistry}.
   * </p>
   *
   * @param supplier
   */
  public CypherStatementRegistry(Supplier<List<ICypherStatement>> supplier) {
    _supplier = checkNotNull(supplier);
    _cypherStatements = Collections.emptyList();
  }

  /**
   * <p>
   * </p>
   */
  @Override
  public void rescan() {
    _cypherStatements = _supplier.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICypherStatement> getAllStatements() {
    return Collections.unmodifiableList(_cypherStatements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ICypherStatement> getStatements(String group) {
    checkNotNull(group);
    return _cypherStatements.stream().filter(statement -> group.equalsIgnoreCase(statement.getGroupId()))
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ICypherStatement> getStatement(String fullyQualifedName) {
    checkNotNull(fullyQualifedName);
    return _cypherStatements.stream()
        .filter(statement -> fullyQualifedName.equalsIgnoreCase(statement.getFullyQualifiedName())).findFirst();
  }
}
