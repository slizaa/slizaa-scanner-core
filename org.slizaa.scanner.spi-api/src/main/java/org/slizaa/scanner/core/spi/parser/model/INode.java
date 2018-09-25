/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.spi.parser.model;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Represents and encapsulates a node in the underlying graph database model.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface INode {

  /** the key for the attribute 'fqn' */
  public static final String FQN      = "fqn";

  /** the key for the attribute 'name' */
  public static final String NAME     = "name";

  /**
   * <p>
   * Returns the internal id of the underlying database node.
   * </p>
   * 
   * @return the internal id of the underlying database node.
   */
  long getId();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getFullyQualifiedName();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getName();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  Map<String, Object> getProperties();

  /**
   * <p>
   * </p>
   *
   * @param key
   * @return
   */
  Object getProperty(String key);

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<Label> getLabels();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  Map<RelationshipType, List<IRelationship>> getRelationships();

  /**
   * <p>
   * </p>
   *
   * @param key
   * @return
   */
  List<IRelationship> getRelationships(RelationshipType key);

  /**
   * <p>
   * </p>
   *
   * @return
   */
  boolean hasNodeId();
  
  /**
   * <p>
   * </p>
   * 
   * @param targetBean
   * @param relationshipType
   * @return
   */
  IRelationship addRelationship(INode targetBean, RelationshipType relationshipType);
  
  /**
   * <p>
   * </p>
   *
   */
  void clearRelationships();

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @param value
   */
  void putProperty(String key, Object value);

  /**
   * <p>
   * </p>
   * 
   * @param label
   */
  void addLabel(Label label);
  
  void setNodeId(long id);

  boolean containsRelationship(RelationshipType type, INode node);
}
