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
import static org.slizaa.scanner.spi.internal.Preconditions.checkState;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.ContentType;
import org.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.spi.contentdefinition.filebased.IFileBasedContentDefinition;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public final class FileBasedContentDefinition implements IFileBasedContentDefinition {

  /** the empty resource standin set */
  private static final List<IFile> EMPTY_RESOURCE_SET = Collections.emptyList();

  /** - */
  private static final List<File>  EMPTY_ROOTPATH_SET = Collections.emptyList();

  /** the name of this entry */
  private String                   _name;

  /** the version of this entry */
  private String                   _version;

  /** the analyze mode of this entry */
  private AnalyzeMode              _analyze;

  /** the binary pathes */
  private List<File>               _binaryPaths;

  /** the source pathes */
  private List<File>               _sourcePaths;

  /** indicates that the content has been initialized */
  private boolean                  _isInitialized;

  /** the set of binary resource standins */
  private Map<String, IFile>       _binaryResources;

  /** the set of source resource standins */
  private Map<String, IFile>       _sourceResources;

  /**
   * <p>
   * Creates a new instance of type {@link FileBasedContentDefinition}.
   * </p>
   */
  public FileBasedContentDefinition() {

    //
    setAnalyzeMode(AnalyzeMode.BINARIES_ONLY);

    //
    _binaryPaths = new LinkedList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return _version;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isAnalyze() {
    return _analyze.isAnalyze();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AnalyzeMode getAnalyzeMode() {
    return _analyze;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Collection<IFile> getBinaryFiles() {
    assertIsInitialized();
    return _binaryResources != null ? Collections.unmodifiableCollection(_binaryResources.values())
        : EMPTY_RESOURCE_SET;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Collection<IFile> getSourceFiles() {
    assertIsInitialized();
    return _sourceResources != null ? Collections.unmodifiableCollection(_sourceResources.values())
        : EMPTY_RESOURCE_SET;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<IFile> getFiles(ContentType type) {
    assertIsInitialized();

    switch (type) {
    case BINARY: {
      return getBinaryFiles();
    }
    case SOURCE: {
      return getSourceFiles();
    }
    default: {
      return null;
    }
    }
  }

  /**
   * {@inheritDoc}
   */
  public IFile getResource(String path, ContentType type) {
    assertIsInitialized();

    switch (type) {
    case BINARY: {
      return binaryResourcesMap().get(path);
    }
    case SOURCE: {
      return sourceResourcesMap().get(path);
    }
    default: {
      return null;
    }
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if the content has been initialized yet.
   * </p>
   * 
   * @return <code>true</code> if the content has been initialized yet.
   */
  protected boolean isInitialized() {
    return _isInitialized;
  }

  /**
   * <p>
   * Sets the name of this content entry.
   * </p>
   * 
   * @param name
   *          the name of this content entry.
   */
  public void setName(String name) {
    checkNotNull(name);
    _name = name;
  }

  /**
   * <p>
   * Sets the version of this content entry.
   * </p>
   * 
   * @param name
   *          the version of this content entry.
   */
  public void setVersion(String version) {
    checkNotNull(version);
    _version = version;
  }

  /**
   * <p>
   * Sets the {@link AnalyzeMode} of this content entry.
   * </p>
   * 
   * @param name
   *          the {@link AnalyzeMode} of this content entry.
   */
  public void setAnalyzeMode(AnalyzeMode analyzeMode) {
    checkNotNull(analyzeMode, "Paramter 'analyzeMode' must not be null");

    //
    _analyze = analyzeMode;
  }

  /**
   * <p>
   * Initializes this content entry.
   * </p>
   */
  public final void initialize() {

    // return if content already is initialized
    if (isInitialized()) {
      return;
    }

    //
    // add the binary resources
    if (_binaryPaths != null) {
      for (File binaryPath : _binaryPaths) {
        for (String filePath : getAllChildren(binaryPath)) {

          //
          createNewResource(binaryPath, filePath, ContentType.BINARY);
        }
      }
    }

    // add the source resources
    if (_sourcePaths != null) {
      for (File sourcePath : _sourcePaths) {
        for (String filePath : getAllChildren(sourcePath)) {

          //
          createNewResource(sourcePath, filePath, ContentType.SOURCE);
        }
      }
    }

    // set initialized
    _isInitialized = true;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private Map<String, IFile> binaryResourcesMap() {

    //
    if (_binaryResources == null) {
      _binaryResources = new HashMap<String, IFile>();
    }

    //
    return _binaryResources;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private Map<String, IFile> sourceResourcesMap() {

    //
    if (_sourceResources == null) {
      _sourceResources = new HashMap<String, IFile>();
    }

    //
    return _sourceResources;
  }

  /**
   * {@inheritDoc}
   */
  public List<File> getBinaryRootPaths() {
    return Collections.unmodifiableList(_binaryPaths);
  }

  /**
   * {@inheritDoc}
   */
  public List<File> getSourceRootPaths() {
    return _sourcePaths != null ? _sourcePaths : EMPTY_ROOTPATH_SET;
  }

  /**
   * <p>
   * </p>
   * 
   * @param rootPath
   * @param type
   */
  public void addRootPath(File rootPath, ContentType type) {
    checkNotNull(rootPath);
    checkNotNull(type);

    //
    if (type.equals(ContentType.BINARY)) {
      _binaryPaths.add(rootPath);
    } else if (type.equals(ContentType.SOURCE)) {
      sourcePaths().add(rootPath);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(getClass().getSimpleName());
    builder.append(" [_binaryPaths=");
    builder.append(_binaryPaths);
    builder.append(", _sourcePaths=");
    builder.append(_sourcePaths);
    builder.append(", getName()=");
    builder.append(getName());
    builder.append(", getVersion()=");
    builder.append(getVersion());
    builder.append(", isAnalyze()=");
    builder.append(isAnalyze());
    builder.append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_analyze == null) ? 0 : _analyze.hashCode());
    result = prime * result + ((_binaryPaths == null) ? 0 : _binaryPaths.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_sourcePaths == null) ? 0 : _sourcePaths.hashCode());
    result = prime * result + ((_version == null) ? 0 : _version.hashCode());
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
    FileBasedContentDefinition other = (FileBasedContentDefinition) obj;
    if (_analyze != other._analyze)
      return false;
    if (_binaryPaths == null) {
      if (other._binaryPaths != null)
        return false;
    } else if (!_binaryPaths.equals(other._binaryPaths))
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_sourcePaths == null) {
      if (other._sourcePaths != null)
        return false;
    } else if (!_sourcePaths.equals(other._sourcePaths))
      return false;
    if (_version == null) {
      if (other._version != null)
        return false;
    } else if (!_version.equals(other._version))
      return false;
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param root
   * @param path
   * @param type
   * @return
   */
  private IFile createNewResource(File root, String path, ContentType type) {

    //
    Map<String, IFile> resourcesMap = type.equals(ContentType.BINARY) ? binaryResourcesMap() : sourceResourcesMap();

    //
    if (!resourcesMap.containsKey(path)) {

      IFile result = new DefaultFile(root.getAbsolutePath(), path, () -> getContent(root, path));

      // add the resource
      switch (type) {
      case BINARY: {
        binaryResourcesMap().put(path, result);
        break;
      }
      case SOURCE: {
        sourceResourcesMap().put(path, result);
        break;
      }
      default:
        break;
      }

      // return resource
      return result;

    } else {
      // TODO!!
      System.out.println(String.format("DUPLICATE RESOURCE IN ENTRY '%s': '%s'", getName(), path));
      if (_isInitialized) {
        return getResource(path, type);
      } else {
        return null;
      }
    }
  }

  public void removeResource(IFile resource, ContentType type) {

    //
    // add the resource
    switch (type) {
    case BINARY: {
      binaryResourcesMap().remove(resource.getPath());
      break;
    }
    case SOURCE: {
      sourceResourcesMap().remove(resource.getPath());
      break;
    }
    default:
      break;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private List<File> sourcePaths() {

    // lazy initialization
    if (_sourcePaths == null) {
      _sourcePaths = new LinkedList<>();
    }

    // return the source paths
    return _sourcePaths;
  }

  /**
   * <p>
   * </p>
   */
  private void assertIsInitialized() {
    if (!_isInitialized) {
      checkState(false, String.format("ProjectContentEntry '%s' has to be initialized.", toString()));
    }
  }

  /**
   * <p>
   * Returns a list with all children of the given file. If the file is a directory, all file paths contained in all
   * folders and sub-folders are returned. If the file is a zip file or a jar archive, the paths of all entries are
   * returned.
   * </p>
   * 
   * @param file
   *          the file
   * @return the list of paths
   */
  public static List<String> getAllChildren(File file) {

    //
    if (file.isDirectory()) {

      List<String> result = new LinkedList<String>();
      getAllChildren(file, file, result);
      return result;
    }

    //
    else if (file.isFile() && (file.getName().endsWith(".zip") || file.getName().endsWith(".jar"))) {

      try {
        ZipFile zipFile = new ZipFile(file);

        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

        List<String> result = new LinkedList<String>();

        while (enumeration.hasMoreElements()) {
          ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
          if (!zipEntry.isDirectory()) {
            String name = zipEntry.getName();
            name = name.replace('\\', '/');
            result.add(name);
          }
        }

        zipFile.close();

        return result;

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    // throw the core exception
    throw new RuntimeException(String.format("File '%s' does not exist.", file.getAbsolutePath()));
  }

  /**
   * <p>
   * </p>
   * 
   * @param root
   * @param directory
   * @return
   */
  private static void getAllChildren(File root, File directory, List<String> content) {

    int length = root.getAbsolutePath().length();

    //
    for (File child : directory.listFiles()) {

      if (child.isFile()) {

        String entry = child.getAbsolutePath().substring(length + 1);

        entry = entry.replace("\\", "/");

        content.add(entry);

      } else if (child.isDirectory()) {
        getAllChildren(root, child, content);
      }
    }
  }

  private static byte[] getContent(File root, String path) {

    // jar file?
    if (root.isFile()) {

      try (ZipFile zipFile = new ZipFile(root)) {

        ZipEntry zipEntry = zipFile.getEntry(path);

        InputStream is = zipFile.getInputStream(zipEntry);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
          buffer.write(data, 0, nRead);
        }
        buffer.flush();

        byte[] result = buffer.toByteArray();

        is.close();

        // return the result
        return result;

      } catch (Exception ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
        throw new RuntimeException("Error while parsing '" + root + "' with path '" + path + "': " + ex, ex);
      }
    }

    //
    if (root.isDirectory()) {

      try {

        File file = new File(root, path);
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
          buffer.write(data, 0, nRead);
        }
        buffer.flush();

        byte[] result = buffer.toByteArray();

        is.close();
        buffer.close();

        //
        return result;

      } catch (Exception ex) {
        throw new RuntimeException("Error while parsing '" + root + "' with path '" + path + "': " + ex, ex);
      }

    } else {
      throw new RuntimeException("Error while parsing '" + root + "' with path '" + path + "'.");
    }
  }
}
