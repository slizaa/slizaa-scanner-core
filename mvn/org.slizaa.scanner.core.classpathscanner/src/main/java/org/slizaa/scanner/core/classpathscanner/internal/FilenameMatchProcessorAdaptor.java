package org.slizaa.scanner.core.classpathscanner.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.core.classpathscanner.IFileMatchHandler;
import org.slizaa.scanner.core.classpathscanner.IFileMatchHandlerTransformator;

public class FilenameMatchProcessorAdaptor<T> {

  /** - */
  private IFileMatchHandler<T>              _processor;

  /** - */
  private IFileMatchHandlerTransformator<T> _transformator;

  /** - */
  private List<T>                           _result;

  /**
   * <p>
   * Creates a new instance of type {@link FilenameMatchProcessorAdaptor}.
   * </p>
   *
   * @param processor
   */
  public FilenameMatchProcessorAdaptor(IFileMatchHandler<T> processor,
      IFileMatchHandlerTransformator<T> transformator) {

    //
    _processor = checkNotNull(processor);
    _transformator = checkNotNull(transformator);
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
  public void transformAndAdd(String relativePath, InputStream inputStream, long lengthBytes) {
    T item = _transformator.transformFileMatch(relativePath, inputStream, lengthBytes);
    _result.add(item);
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
    FilenameMatchProcessorAdaptor<?> other = (FilenameMatchProcessorAdaptor<?>) obj;
    if (_processor == null) {
      if (other._processor != null)
        return false;
    } else if (!_processor.equals(other._processor))
      return false;
    return true;
  }
}
