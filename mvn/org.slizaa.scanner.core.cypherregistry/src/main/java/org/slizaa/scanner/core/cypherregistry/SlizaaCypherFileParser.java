package org.slizaa.scanner.core.cypherregistry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;

import com.google.common.io.ByteSource;

public class SlizaaCypherFileParser {

  private static final String COMMENT_PATTERN = "/\\*(?:.|[\\n\\r])*?\\*/";

  private static final String DOCLET_PATTERN  = "\\x40(\\S*)\\s*(.*)";

  /**
   * <p>
   * </p>
   *
   * @param inputStream
   * @return
   * @throws IOException
   */
  public static DefaultCypherStatement parse(InputStream inputStream) {

    String content;
    try {
      content = readFile(inputStream, Charset.defaultCharset());
    } catch (IOException e) {
      return null;
    }

    Pattern pattern_comment = Pattern.compile(COMMENT_PATTERN);
    Pattern pattern_doclet = Pattern.compile(DOCLET_PATTERN);
    Matcher matcher_comment = pattern_comment.matcher(content);

    DefaultCypherStatement defaultCypherStatement = new DefaultCypherStatement();

    while (matcher_comment.find()) {
      String comment = matcher_comment.group(0);

      Matcher matcher_doclet = pattern_doclet.matcher(comment);
      while (matcher_doclet.find()) {

        //
        if ("slizaa.group".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setGroupId(matcher_doclet.group(2));
        }

        //
        if ("slizaa.name".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setStatementId(matcher_doclet.group(2));
        }
      }
    }

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
