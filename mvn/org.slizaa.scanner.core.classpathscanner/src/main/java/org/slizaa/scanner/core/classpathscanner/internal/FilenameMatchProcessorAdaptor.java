package org.slizaa.scanner.core.classpathscanner.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.core.classpathscanner.IFilenameMatchHandler;

public class FilenameMatchProcessorAdaptor {

  /** - */
  private IFilenameMatchHandler _processor;

  /** - */
  private List<String>          _result;

  /**
   * <p>
   * Creates a new instance of type {@link FilenameMatchProcessorAdaptor}.
   * </p>
   *
   * @param processor
   */
  public FilenameMatchProcessorAdaptor(IFilenameMatchHandler processor) {

    //
    checkNotNull(processor);

    //
    _processor = processor;
  }

  /**
   * <p>
   * </p>
   * 
   * @param codeSource
   *
   */
  public void beforeScan(Object codeSource) {
    _result = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param path
   */
  public void addPath(String path) {
    _result.add(path);
  }

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   * @param classLoader
   */
  public void afterScan(Object codeSource) {
    _processor.processMatch(codeSource, _result);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_processor == null) ? 0 : _processor.hashCode());
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
    FilenameMatchProcessorAdaptor other = (FilenameMatchProcessorAdaptor) obj;
    if (_processor == null) {
      if (other._processor != null)
        return false;
    } else if (!_processor.equals(other._processor))
      return false;
    return true;
  }
}
