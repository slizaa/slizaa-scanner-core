package org.slizaa.scanner.core.cypherregistry;

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
  private List<String> _requiredStatements;

  /** - */
  private Object       _codeSource;

  /** - */
  private String       _relativePath;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGroupId() {
    return _groupId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatementId() {
    return _statementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFullyQualifiedName() {
    return _groupId + "." + _statementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return _description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatement() {
    return _statement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRequiredStatements() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRelativePath() {
    return _relativePath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCodeSource() {
    return _codeSource;
  }

  public void setRelativePath(String relativePath) {
    _relativePath = relativePath;
  }

  public void setGroup(String group) {
    _groupId = group;
  }

  public void setName(String name) {
    _statementId = name;
  }

  public void setDescription(String description) {
    _description = description;
  }

  public void setCypherStatement(String cypherStatement) {
    _statement = cypherStatement;
  }

  public void setRequires(List<String> requires) {
    _requiredStatements = requires;
  }

  public void setCodeSource(Object codeSource) {
    _codeSource = codeSource;
  }

  public boolean isValid() {
    return _groupId != null && _statementId != null && _description != null;
  }

  @Override
  public String toString() {
    return "DefaultCypherStatement [groupId=" + _groupId + ", statementId=" + _statementId + ", description="
        + _description + ", statement=" + _statement + ", requiredStatements=" + _requiredStatements + ", codeSource="
        + _codeSource + ", relativePath=" + _relativePath + "]";
  }
}
