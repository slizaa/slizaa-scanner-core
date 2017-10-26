package org.slizaa.addons.mvnresolver.impl;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slizaa.addons.mvnresolver.api.MvnResolverService;
import org.slizaa.addons.mvnresolver.implementation.internal.TransitiveDependenciesResolver;

public class MvnResolverServiceImplementation implements MvnResolverService {

  @Override
  public File[] resolve(String coords) {
    try {
      intitialize();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return TransitiveDependenciesResolver.resolve(coords);
  }

  private void intitialize() throws Exception {
    // https://github.com/kamranzafar/JCL

    //
    try (InputStream inputStream = this.getClass().getProtectionDomain().getCodeSource().getLocation().openStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

      ZipEntry zipEntry = null;
      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        System.out.println(zipEntry.getName());
      }

      // //
      // JarClassLoader jcl = new JarClassLoader();
      //
      // // Loading classes from different sources
      // jcl.add(url.openStream());
      //
      // //
      // Class<?> clazz = jcl.loadClass("org.eclipse.aether.ConfigurationProperties");
      // System.out.println(clazz);

    }
  }
}
