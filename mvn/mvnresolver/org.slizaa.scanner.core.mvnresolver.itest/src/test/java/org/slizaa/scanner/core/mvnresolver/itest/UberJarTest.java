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
package org.slizaa.scanner.core.mvnresolver.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.osgi.framework.BundleException;
import org.slizaa.scanner.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverServiceFactory;

public class UberJarTest extends AbstractEclipseTest {

  /**
   * <p>
   * </p>
   * 
   * @throws BundleException
   */
  @Test
  public void checkUberJar() throws Exception {

    // checkStart
    startAllBundles();

    //
    IMvnResolverServiceFactory factory = MvnResolverServiceFactoryFactory.createNewResolverServiceFactory();
    IMvnResolverService impl = factory.newMvnResolverService().create();

    //
    File[] files = impl.resolve("org.neo4j.test:neo4j-harness:2.3.3");

    //
    assertThat(files).hasSize(75);
  }
}
