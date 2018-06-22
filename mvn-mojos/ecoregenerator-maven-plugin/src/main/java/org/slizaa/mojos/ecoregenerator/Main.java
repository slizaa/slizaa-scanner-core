/**
 *
 */
package org.slizaa.mojos.ecoregenerator;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class Main {

  /**
   * <p>
   * </p>
   *
   * @param args
   */
  public static void main(String[] args) {

    //
    try (ZipFile zipFile = new ZipFile(new File(
        "C:\\Users\\wuetherich\\.m2\\repository\\org\\slizaa\\hierarchicalgraph\\org.slizaa.hierarchicalgraph.core.model\\1.0.0-SNAPSHOT\\org.slizaa.hierarchicalgraph.core.model-1.0.0-SNAPSHOT.jar"))) {
      // "C:\\Users\\wuetherich\\.m2\\repository\\org\\eclipse\\emf\\org.eclipse.emf.ecore\\2.12.0\\org.eclipse.emf.ecore-2.12.0.jar")))
      // {

      ZipEntry zipEntry = zipFile.getEntry("plugin.xml");

      if (zipEntry != null) {
        InputStream inputStream = zipFile.getInputStream(zipEntry);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);

        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/plugin/extension[@point='org.eclipse.emf.ecore.generated_package']/package/@genModel";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
          System.out.println(nodeList.item(i).getTextContent());
        }
      }
      //
      // //
      // for (IExtensionPoint extensionPoint : extensionRegistry.getExtensionPoints()) {
      //
      // //
      // System.out.println(extensionPoint.getSchemaReference());
      // }
      // IExtensionPoint extensionPoint =
      // extensionRegistry.getExtensionPoint("org.eclipse.emf.ecore.generated_package");
      // for (IExtension extension : extensionPoint.getExtensions()) {
      // extension.getContributor();
      // System.out.println(extension);
      // for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
      // System.out.println(" - " + configurationElement.getAttribute("genModel"));
      // }
      // }

    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

}
