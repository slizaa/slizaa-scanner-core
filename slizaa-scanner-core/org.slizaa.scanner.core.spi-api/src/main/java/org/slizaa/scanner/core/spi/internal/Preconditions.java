package org.slizaa.scanner.core.spi.internal;

public class Preconditions {

  /**
   * Ensures the reference is not null.
   *
   * @param reference
   *          The reference to test.
   * @return The reference.
   * @throws NullPointerException
   */
  public static <T> T checkNotNull(final T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  public static <T> T checkNotNull(final T reference, Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  public static void checkState(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new IllegalStateException(String.valueOf(errorMessage));
    }
  }
}