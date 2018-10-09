package org.slizaa.scanner.testfwk.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slizaa.scanner.testfwk.SlizaaTestServerRule;

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

    return throwUsageException(groupId, artifactId);
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
        URL url = urls.nextElement();

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

    return throwUsageException(groupId, artifactId);
  }

  private static String throwUsageException(String groupId, String artifactId) {

    StringBuffer buffer = new StringBuffer();

    buffer.append("Couldn't resolve version information for maven artifact.");
    buffer.append(String.format("Maven artifact '%s:%s':\n", groupId, artifactId));
    buffer.append("\n");
    buffer.append("(1) Please make sure that you have integrated the following plugin in your project setup:\n");
    buffer.append("    <plugin>\n");
    buffer.append("      <groupId>org.apache.servicemix.tooling</groupId>\n");
    buffer.append("      <artifactId>depends-maven-plugin</artifactId>\n");
    buffer.append("      <version>1.4.0</version>\n");
    buffer.append("      <executions>\n");
    buffer.append("        <execution>\n");
    buffer.append("          <id>generate-depends-file</id>\n");
    buffer.append("          <goals>\n");
    buffer.append("            <goal>generate-depends-file</goal>\n");
    buffer.append("          </goals>\n");
    buffer.append("        </execution>\n");
    buffer.append("      </executions>\n");
    buffer.append("    </plugin>\n");
    buffer.append("\n");
    buffer.append("(2) Append the artifact to the list of dependencies:\n");
    buffer.append("    <dependency>\n");
    buffer.append(String.format("      <groupId>%s</groupId>\n", groupId));
    buffer.append(String.format("      <artifactId>%s</artifactId>\n", artifactId));
    buffer.append("      <artifactId>THE_ARTIFACT_VERSION</artifactId>\n");
    buffer.append("    </dependency>\n");
    buffer.append("\n");
    buffer.append("(3) Execute the maven build to generate the depends file\n");

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
