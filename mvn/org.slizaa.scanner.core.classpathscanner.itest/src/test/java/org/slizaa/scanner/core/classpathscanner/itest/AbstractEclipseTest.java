/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.core.classpathscanner.itest;

import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public abstract class AbstractEclipseTest {

  {
    System.setProperty("org.ops4j.pax.url.mvn.localRepository",
        System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository");
  }

  /** - */
  @Inject
  private BundleContext bundleContext;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public BundleContext bundleContext() {
    return bundleContext;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   * @throws IOException
   */
  @Configuration
  public Option[] config() throws IOException {

    //
    return options(

        //
        bootDelegationPackages("sun.*", "com.sun.*"),

        // add the test dependencies
        mavenBundle("org.assertj", "assertj-core").versionAsInProject(), junitBundles(),

        //
        mavenBundle("com.google.guava", "guava", "21.0"),
        mavenBundle("org.eclipse.platform", "org.eclipse.equinox.common").versionAsInProject(),
        mavenBundle("org.slizaa.scanner.core", "org.slizaa.scanner.core.classpathscanner")
            .versionAsInProject());
  }

  /**
   * <p>
   * </p>
   * 
   * @throws BundleException
   */
  protected void startAllBundles() throws BundleException {
    for (Bundle bundle : bundleContext.getBundles()) {
      bundle.start();
    }
  }
}
