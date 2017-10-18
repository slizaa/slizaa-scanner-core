package org.slizaa.scanner.core.classpathscanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMethodAnnotationMatchHandler {

  Class<? extends Annotation> getAnnotationToMatch();

  void consume(Map<Class<?>, List<Executable>> matchingMethodsOrConstructors);
}
