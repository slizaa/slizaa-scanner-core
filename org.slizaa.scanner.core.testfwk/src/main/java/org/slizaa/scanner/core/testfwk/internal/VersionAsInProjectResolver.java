package org.slizaa.scanner.core.testfwk.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slizaa.scanner.core.testfwk.SlizaaTestServerRule;

public class VersionAsInProjectResolver {

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @return
   */
  public static String resolveVersion(String groupId, String artifactId) {

    //
    Properties properties = loadPropertiesFromHomeDir();

    //
    if (properties != null) {

      //
      String version = properties.getProperty(String.format("%s/%s/version", groupId, artifactId));

      //
      if (version != null) {
        return version;
      }
    }

    return throwUsageException();
  }

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @return
   */
  private static String loadVersionFromClasspathProperties(String groupId, String artifactId) {

    try {

      //
      Enumeration<URL> urls = SlizaaTestServerRule.class.getClassLoader()
          .getResources("META-INF/maven/dependencies.properties");

      //
      while (urls.hasMoreElements()) {

        //
        URL url = (URL) urls.nextElement();

        Properties properties = new Properties();
        try (InputStream stream = url.openStream()) {
          properties.load(stream);
        }

        //
        String version = properties.getProperty(String.format("%s/%s/version", groupId, artifactId));

        if (version != null) {
          return version;
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    throwUsageException();
    return null;
  }

  private static String throwUsageException() {

    StringBuffer buffer = new StringBuffer();
    
    buffer.append("<plugin>");
    buffer.append("  <groupId>org.apache.servicemix.tooling</groupId>");
    buffer.append("  <artifactId>depends-maven-plugin</artifactId>");
    buffer.append("  <version>1.4.0</version>");
    buffer.append("  <executions>");
    buffer.append("    <execution>");
    buffer.append("      <id>generate-depends-file</id>");
    buffer.append("      <goals>");
    buffer.append("        <goal>generate-depends-file</goal>");
    buffer.append("      </goals>");
    buffer.append("    </execution>");
    buffer.append("  </executions>");
    buffer.append("</plugin>");

    throw new RuntimeException(buffer.toString());
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static Properties loadPropertiesFromHomeDir() {

    //
    File file = new File(System.getProperty("user.dir"), "target/classes/META-INF/maven/dependencies.properties");

    //
    try {
      if (file.exists()) {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
          properties.load(stream);
        }
        return properties;
      }
    } catch (Exception e) {
      return null;
    }

    //
    return null;
  }

}
