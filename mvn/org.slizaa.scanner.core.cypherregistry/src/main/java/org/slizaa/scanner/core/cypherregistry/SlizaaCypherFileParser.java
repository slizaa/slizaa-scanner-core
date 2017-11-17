package org.slizaa.scanner.core.cypherregistry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slizaa.scanner.core.cypherregistry.impl.CypherNormalizer;

import com.google.common.io.ByteSource;

public class SlizaaCypherFileParser {

  private static final String COMMENT_PATTERN = "/\\*(?:.|[\\n\\r])*?\\*/";

  private static final String DOCLET_PATTERN  = "\\x40(\\S*)\\s*(.*)";

  /**
   * <p>
   * </p>
   * 
   * @param relativePath
   *
   * @param inputStream
   * @return
   * @throws IOException
   */
  public static DefaultCypherStatement parse(String relativePath, InputStream inputStream) {

    //
    String fileContent;

    //
    try {
      fileContent = readFile(inputStream, Charset.defaultCharset());
    } catch (IOException e) {
      return null;
    }

    //
    Pattern pattern_comment = Pattern.compile(COMMENT_PATTERN);
    Pattern pattern_doclet = Pattern.compile(DOCLET_PATTERN);
    Matcher matcher_comment = pattern_comment.matcher(fileContent);

    //
    DefaultCypherStatement defaultCypherStatement = new DefaultCypherStatement();

    //
    defaultCypherStatement.setStatement(CypherNormalizer.normalize(fileContent));

    //
    if (matcher_comment.find()) {

      //
      String comment = matcher_comment.group(0);

      //
      Matcher matcher_doclet = pattern_doclet.matcher(comment);
      while (matcher_doclet.find()) {

        //
        if ("slizaa.groupId".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setGroupId(matcher_doclet.group(2));
        }

        //
        else if ("slizaa.statementId".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setStatementId(matcher_doclet.group(2));
        }

        //
        else if ("slizaa.description".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setDescription(matcher_doclet.group(2));
        }

        //
        else if ("slizaa.requiredStatements".equals(matcher_doclet.group(1))) {

          //
          String requiredStatements = matcher_doclet.group(2);

          //
          defaultCypherStatement.setRequiredStatements(Arrays.asList(requiredStatements.split(",")).stream()
              .map(statement -> statement.trim()).collect(Collectors.toList()));
        }

        else if (matcher_doclet.group(1).startsWith("slizaa")) {
          new RuntimeException("Unknown doclet '@" + matcher_doclet.group(1) + "' in " + relativePath + ".")
              .printStackTrace();
        }
      }
    }

    // TODO
    return defaultCypherStatement;
  }

  /**
   * <p>
   * </p>
   */
  private static String readFile(InputStream inputStream, Charset encoding) throws IOException {

    ByteSource byteSource = new ByteSource() {
      @Override
      public InputStream openStream() throws IOException {
        return inputStream;
      }
    };

    return byteSource.asCharSource(encoding).read();
  }
}
