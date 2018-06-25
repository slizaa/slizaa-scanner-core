package org.slizaa.scanner.core.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultCypherStatement implements ICypherStatement {

  /** the greoup id - never null */
  private String       _groupId;

  /** - */
  private String       _statementId;

  /** - */
  private String       _description;

  /** - */
  private String       _statement;

  /** - */
  private Object       _codeSource;

  /** - */
  private String       _relativePath;

  /** - */
  private List<String> _requiredStatements = new ArrayList<>();

  /**
   * <p>
   * Creates a new instance of type {@link DefaultCypherStatement}.
   * </p>
   */
  public DefaultCypherStatement() {
    super();
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultCypherStatement}.
   * </p>
   *
   * @param groupId
   * @param statementId
   * @param statement
   */
  public DefaultCypherStatement(String groupId, String statementId, String statement) {
    this._groupId = checkNotNull(groupId);
    this._statementId = checkNotNull(statementId);
    this._statement = checkNotNull(statement);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGroupId() {
    return this._groupId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatementId() {
    return this._statementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFullyQualifiedName() {
    return this._groupId + "." + this._statementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return this._description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatement() {
    return this._statement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRequiredStatements() {
    return this._requiredStatements;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRelativePath() {
    return this._relativePath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCodeSource() {
    return this._codeSource;
  }

  public void setGroupId(String groupId) {
    this._groupId = groupId;
  }

  public void setStatementId(String statementId) {
    this._statementId = statementId;
  }

  public void setDescription(String description) {
    this._description = description;
  }

  public void setStatement(String statement) {
    this._statement = statement;
  }

  public void setRequiredStatements(List<String> requiredStatements) {
    checkNotNull(requiredStatements).forEach(element -> checkNotNull(element));
    this._requiredStatements = requiredStatements;
  }

  public void setCodeSource(Object codeSource) {
    this._codeSource = codeSource;
  }

  public void setRelativePath(String relativePath) {
    this._relativePath = relativePath;
  }

  @Override
  public boolean isValid() {
    return this._groupId != null && this._statementId != null;
  }

  @Override
  public String toString() {
    return "DefaultCypherStatement [groupId=" + this._groupId + ", statementId=" + this._statementId + ", description="
        + this._description + ", statement=" + this._statement + ", requiredStatements=" + this._requiredStatements
        + ", codeSource=" + this._codeSource + ", relativePath=" + this._relativePath + "]";
  }
}
