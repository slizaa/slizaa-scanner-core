package org.slizaa.mojos.ecoregenerator;

import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Ecore_Generator_2_Test extends AbstractEcoreGeneratorTest {

  /**
   * <p>
   * Creates a new instance of type {@link Ecore_Generator_2_Test}.
   * </p>
   */
  public Ecore_Generator_2_Test() {
    super("ecore-generator-2");
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  public void test() throws Exception {

    //
    getEcoreGeneratorMojo().execute();
    
    // https://stackoverflow.com/questions/9386348/register-ecore-meta-model-programmatically
  }
}
