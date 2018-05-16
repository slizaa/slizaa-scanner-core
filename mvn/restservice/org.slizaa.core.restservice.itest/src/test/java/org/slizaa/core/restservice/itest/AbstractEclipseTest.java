/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.core.restservice.itest;

import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

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
  private BundleContext         bundleContext;

  protected static final int    PORT     = 8080;

  protected static final String CONTEXT  = "/helloworld";

  protected static final URI    BASE_URI = URI.create("http://localhost:" + PORT + CONTEXT);

  protected static final Logger LOGGER   = Logger.getLogger(AbstractEclipseTest.class.getName());

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public BundleContext bundleContext() {
    return this.bundleContext;
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
        bootDelegationPackages("sun.*", "com.sun.*"), junitBundles(),

        // add the test dependencies
        mavenBundle("org.assertj", "assertj-core").versionAsInProject(),

        //
        mavenBundle("com.google.guava", "guava").versionAsInProject(),
        mavenBundle("org.eclipse.platform", "org.eclipse.equinox.common").versionAsInProject(),

        //
        mavenBundle("org.javassist", "javassist", "3.22.0-CR2"),
        mavenBundle("javax.annotation", "javax.annotation-api", "1.2"),
        mavenBundle("javax.validation", "validation-api", "1.1.0.Final"),
        mavenBundle("javax.servlet", "javax.servlet-api", "3.1.0"),

        mavenBundle("org.glassfish.hk2", "osgi-resource-locator", "1.0.1"),
        mavenBundle("org.glassfish.hk2", "hk2-api", "2.5.0-b42"),
        mavenBundle("org.glassfish.hk2", "hk2-locator", "2.5.0-b42"),
        mavenBundle("org.glassfish.hk2", "hk2-utils", "2.5.0-b42"),
        mavenBundle("org.glassfish.hk2.external", "javax.inject", "2.5.0-b42"),
        mavenBundle("org.glassfish.hk2.external", "aopalliance-repackaged", "2.5.0-b42"),

        // JAX-RS API
        mavenBundle("javax.ws.rs", "javax.ws.rs-api", "2.1"),

        // Jersey bundles
        mavenBundle("org.glassfish.jersey.core", "jersey-common", "2.27"),
        mavenBundle("org.glassfish.jersey.core", "jersey-server", "2.27"),
        mavenBundle("org.glassfish.jersey.core", "jersey-client", "2.27"),
        mavenBundle("org.glassfish.jersey.containers", "jersey-container-servlet-core", "2.27"),
        mavenBundle("org.glassfish.jersey.inject", "jersey-hk2", "2.27"),
        mavenBundle("org.glassfish.jersey.core", "jersey-client", "2.27"),

        // jetty bundles (incl. service loader stuff)
        mavenBundle("org.eclipse.jetty", "jetty-http", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-io", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-security", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-server", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-servlet", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-deploy", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-xml", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-util", "9.4.9.v20180320"),
        mavenBundle("org.eclipse.jetty", "jetty-webapp", "9.4.9.v20180320"),
        mavenBundle("org.apache.aries.spifly", "org.apache.aries.spifly.dynamic.bundle", "1.0.10"),
        mavenBundle("org.ow2.asm", "asm-all", "5.2"),
        mavenBundle("org.apache.aries", "org.apache.aries.util", "1.1.3"));
  }

  public WebTarget webAppTestTarget(String appRoot) throws Exception {

    final Client c = ClientBuilder.newClient();

    //
    return c.target(BASE_URI + appRoot);
  }

  /**
   * <p>
   * </p>
   *
   * @throws BundleException
   */
  protected void startAllBundles() throws BundleException {
    for (Bundle bundle : this.bundleContext.getBundles()) {
      bundle.start();
    }
  }
}
