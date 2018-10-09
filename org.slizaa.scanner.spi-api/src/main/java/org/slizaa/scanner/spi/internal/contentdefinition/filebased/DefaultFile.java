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
package org.slizaa.scanner.spi.internal.contentdefinition.filebased;

import static org.slizaa.scanner.spi.internal.Preconditions.checkNotNull;

import java.util.function.Supplier;

import org.slizaa.scanner.spi.contentdefinition.filebased.IFile;

/**
 * <p>
 * Default implementation of the interface {@link IProjectContentResource}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noextend This class is not intended to be extended by clients.
 */
class DefaultFile implements IFile {

  /** the root of the resource */
  private String           _root;

  /** the path of the resource */
  private String           _path;

  /** - */
  private Supplier<byte[]> _contentSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultFile}.
   * </p>
   * 
   * @param root
   * @param path
   */
  DefaultFile(String root, String path, Supplier<byte[]> contentSupplier) {
    _root = checkNotNull(root).replace('\\', '/');
    _path = checkNotNull(path);
    _contentSupplier = checkNotNull(contentSupplier);
  }

  /**
   * {@inheritDoc}
   */
  public String getRoot() {
    return _root;
  }

  /**
   * {@inheritDoc}
   */
  public String getPath() {
    return _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDirectory() {
    int lastIndex = _path.lastIndexOf('/');
    return lastIndex != -1 ? _path.substring(0, lastIndex) : "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    int lastIndex = _path.lastIndexOf('/');
    return lastIndex != -1 ? _path.substring(lastIndex + 1) : _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getContent() {
    return _contentSupplier.get();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_path == null) ? 0 : _path.hashCode());
    result = prime * result + ((_root == null) ? 0 : _root.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DefaultFile other = (DefaultFile) obj;
    if (_path == null) {
      if (other._path != null)
        return false;
    } else if (!_path.equals(other._path))
      return false;
    if (_root == null) {
      if (other._root != null)
        return false;
    } else if (!_root.equals(other._root))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "DefaultResource [_root=" + _root + ", _path=" + _path + "]";
  }
}
