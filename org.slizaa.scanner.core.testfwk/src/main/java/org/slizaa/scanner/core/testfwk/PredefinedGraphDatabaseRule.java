package org.slizaa.scanner.core.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PredefinedGraphDatabaseRule extends AbstractSlizaaTestServerRule {

  /** - */
  private TestDB _testDB;

  /** - */
  private int    _port;

  /**
   * <p>
   * Creates a new instance of type {@link PredefinedGraphDatabaseRule}.
   * </p>
   *
   * @param testDB
   * @param port
   */
  public PredefinedGraphDatabaseRule(TestDB testDB, int port) {
    super(createBackendLoaderConfigurer());

    //
    _testDB = checkNotNull(testDB);
    _port = port;
  }

  /**
   * <p>
   * Creates a new instance of type {@link PredefinedGraphDatabaseRule}.
   * </p>
   *
   * @param testDB
   * @param port
   * @param backendLoaderConfigurer
   */
  public PredefinedGraphDatabaseRule(TestDB testDB, int port, Consumer<IMvnResolverJob> backendLoaderConfigurer) {
    super(backendLoaderConfigurer);

    //
    _testDB = checkNotNull(testDB);
    _port = port;
  }

  /**
   * <p>
   * Creates a new instance of type {@link PredefinedGraphDatabaseRule}.
   * </p>
   *
   * @param testDB
   * @param port
   * @param workingDirectory
   * @param backendLoaderConfigurer
   */
  public PredefinedGraphDatabaseRule(TestDB testDB, int port, File workingDirectory,
      Consumer<IMvnResolverJob> backendLoaderConfigurer) {
    super(workingDirectory, backendLoaderConfigurer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IGraphDb createGraphDatabase(ITestFwkBackEnd testFwkBackEnd) throws IOException {

    //
    Path tempParentDirectoryPath = Files.createTempDirectory("TestNeo4jServerCreatorServiceTempDirectory");

    //
    File databaseDirectory = unzipDatabase(this._testDB, tempParentDirectoryPath);

    //
    executeWithThreadContextClassLoader(testFwkBackEnd.getClassLoader(),
        () -> setGraphDb(testFwkBackEnd.getGraphDbFactory().newGraphDb(_port, databaseDirectory).create()));

    // create new GraphDb
    return getGraphDb();
  }

  /**
   * <p>
   * </p>
   *
   * @param testDB
   * @param parentDirectory
   * @return
   */
  private File unzipDatabase(TestDB testDB, Path parentDirectory) {

    //
    checkNotNull(testDB);
    checkNotNull(parentDirectory);

    //
    File databaseDirectory = new File(parentDirectory.toFile(), testDB.getName());

    //
    if (!databaseDirectory.exists()) {
      try (InputStream inputStream = PredefinedGraphDatabaseRule.class.getResourceAsStream(testDB.getPath())) {
        unzip(inputStream, databaseDirectory);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    //
    return databaseDirectory;
  }

  /**
   * <p>
   * </p>
   *
   * @param inputStream
   * @param folder
   */
  private static void unzip(InputStream inputStream, File folder) {

    checkNotNull(inputStream);
    checkNotNull(folder);

    byte[] buffer = new byte[1024];

    try {

      // create output directory is not exists
      if (!folder.exists()) {
        folder.mkdir();
      }

      // get the zip file content
      try (ZipInputStream zis = new ZipInputStream(inputStream)) {
        // get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {

          if (!ze.isDirectory()) {

            String fileName = ze.getName();
            File newFile = new File(folder + File.separator + fileName);

            // create all non exists folders
            // else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
              int len;
              while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
              }
            }
          }
          ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static Consumer<IMvnResolverJob> createBackendLoaderConfigurer() {

    // @formatter:off
    return job -> job
        .withDependency("org.slizaa.scanner.neo4j:org.slizaa.scanner.neo4j.importer:1.0.0-SNAPSHOT")
        .withDependency("org.slizaa.scanner.neo4j:org.slizaa.scanner.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
        .withExclusionPattern("*:org.slizaa.scanner.core.spi-api")
        .withExclusionPattern("*:jdk.tools");
    // @formatter:on
  }
}
