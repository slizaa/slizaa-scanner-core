
package org.slizaa.mojos.ecoregenerator.fwk;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.MetadataNotFoundException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;

/**
 * A simplistic transfer listener that logs uploads/downloads to the console.
 */
public class ConsoleTransferListener extends AbstractTransferListener {

  /** - */
  private final PrintStream                 out;

  /** - */
  private final Map<TransferResource, Long> downloads = new ConcurrentHashMap<TransferResource, Long>();

  /** - */
  private int                               lastLength;

  public ConsoleTransferListener() {
    this(null);
  }

  public ConsoleTransferListener(PrintStream out) {
    this.out = (out != null) ? out : System.out;
  }

  @Override
  public void transferInitiated(TransferEvent event) {
    String message = event.getRequestType() == TransferEvent.RequestType.PUT ? "Uploading" : "Downloading";

    out.println(message + ": " + event.getResource().getRepositoryUrl() + event.getResource().getResourceName());
  }

  @Override
  public void transferProgressed(TransferEvent event) {
    TransferResource resource = event.getResource();
    downloads.put(resource, Long.valueOf(event.getTransferredBytes()));

    StringBuilder buffer = new StringBuilder(64);

    for (Map.Entry<TransferResource, Long> entry : downloads.entrySet()) {
      long total = entry.getKey().getContentLength();
      long complete = entry.getValue().longValue();

      buffer.append(getStatus(complete, total)).append("  ");
    }

    int pad = lastLength - buffer.length();
    lastLength = buffer.length();
    pad(buffer, pad);
    buffer.append('\r');

    out.print(buffer);
  }

  private String getStatus(long complete, long total) {
    if (total >= 1024) {
      return toKB(complete) + "/" + toKB(total) + " KB ";
    } else if (total >= 0) {
      return complete + "/" + total + " B ";
    } else if (complete >= 1024) {
      return toKB(complete) + " KB ";
    } else {
      return complete + " B ";
    }
  }

  private void pad(StringBuilder buffer, int spaces) {
    String block = "                                        ";
    while (spaces > 0) {
      int n = Math.min(spaces, block.length());
      buffer.append(block, 0, n);
      spaces -= n;
    }
  }

  @Override
  public void transferSucceeded(TransferEvent event) {
    transferCompleted(event);

    TransferResource resource = event.getResource();
    long contentLength = event.getTransferredBytes();
    if (contentLength >= 0) {
      String type = (event.getRequestType() == TransferEvent.RequestType.PUT ? "Uploaded" : "Downloaded");
      String len = contentLength >= 1024 ? toKB(contentLength) + " KB" : contentLength + " B";

      out.println(
          type + ": " + resource.getRepositoryUrl() + resource.getResourceName() + " (" + len + ")");
    }
  }

  @Override
  public void transferFailed(TransferEvent event) {
    transferCompleted(event);

    if (!(event.getException() instanceof MetadataNotFoundException)) {
      event.getException().printStackTrace(out);
    }
  }

  private void transferCompleted(TransferEvent event) {
    downloads.remove(event.getResource());

    StringBuilder buffer = new StringBuilder(64);
    pad(buffer, lastLength);
    buffer.append('\r');
    out.print(buffer);
  }

  @Override
  public void transferCorrupted(TransferEvent event) {
    event.getException().printStackTrace(out);
  }

  protected long toKB(long bytes) {
    return (bytes + 1023) / 1024;
  }

}
