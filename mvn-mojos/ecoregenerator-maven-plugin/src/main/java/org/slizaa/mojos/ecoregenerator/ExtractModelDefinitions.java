package org.slizaa.mojos.ecoregenerator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExtractModelDefinitions {

  /**
   * <p>
   * </p>
   *
   * @param args
   */
  public static void extractModelDefinitions(File jarFile, String targetRoot) {

    //
    checkNotNull(jarFile);
    checkState(jarFile.isFile());
    checkNotNull(targetRoot);

    //
    try (ZipFile zipFile = new ZipFile(jarFile)) {

      //
      String projectName = extractProjectName(zipFile, null);

      // we assume that the jar file is an OSGi bundle...
      if (projectName != null) {

        //
        List<String> generatedModels = extractGeneratedModels(zipFile);

        //
        exportModelFiles(zipFile, generatedModels, targetRoot + File.separator + projectName);
      }

    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param zipFile
   * @param generatedModels
   * @param projectName
   */
  private static void exportModelFiles(ZipFile zipFile, List<String> generatedModels, String targetPath) {

    //
    generatedModels.forEach(path -> {

      //
      ZipEntry zipEntry = zipFile.getEntry(path);

      //
      if (zipEntry != null) {

        //
        try {
          InputStream is = zipFile.getInputStream(zipEntry);
          Path p = Paths.get(targetPath + File.separator + path);
          p.toFile().getParentFile().mkdirs();
          Files.copy(is, p);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * <p>
   * </p>
   *
   * @param zipFile
   * @return
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws XPathExpressionException
   */
  private static List<String> extractGeneratedModels(ZipFile zipFile)
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {

    //
    List<String> entries = new ArrayList<>();

    //
    ZipEntry zipEntry_pluginXml = zipFile.getEntry("plugin.xml");
    if (zipEntry_pluginXml != null) {

      InputStream inputStream = zipFile.getInputStream(zipEntry_pluginXml);

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputStream);

      XPath xPath = XPathFactory.newInstance().newXPath();
      String expression = "/plugin/extension[@point='org.eclipse.emf.ecore.generated_package']/package/@genModel";
      NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodeList.getLength(); i++) {
        String path = nodeList.item(i).getTextContent();
        entries.add(path);
        entries.add(path.replace(".genmodel", ".ecore"));
      }
    }
    return entries;
  }

  /**
   * <p>
   * </p>
   *
   * @param zipFile
   * @return
   * @throws IOException
   */
  private static String extractProjectName(ZipFile zipFile, String defaultName) throws IOException {

    ZipEntry zipEntry_Manifest = zipFile.getEntry("META-INF/MANIFEST.MF");
    if (zipEntry_Manifest != null) {

      //
      Properties properties = new Properties();
      properties.load(zipFile.getInputStream(zipEntry_Manifest));

      //
      String bundleSymbolicName = properties.getProperty("Bundle-SymbolicName");

      if (bundleSymbolicName != null) {
        String[] splittedString = bundleSymbolicName.split(";");
        return splittedString[0];
      }
    }
    return defaultName;
  }

}
