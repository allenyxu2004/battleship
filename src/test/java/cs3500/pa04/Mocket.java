package cs3500.pa04;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;

/**
 * Represents a mock socket
 */
public class Mocket extends Socket {

  private final InputStream testInputs;
  private final ByteArrayOutputStream testLog;

  /**
   * Constructor for a mocket
   *
   * @param testLog the log of outputs
   * @param toSend the list of strings to send
   */
  public Mocket(ByteArrayOutputStream testLog, List<String> toSend) {
    this.testLog = testLog;

    // Set up the list of inputs as separate messages of JSON for the ProxyDealer to handle
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    for (String message : toSend) {
      printWriter.println(message);
    }
    this.testInputs = new ByteArrayInputStream(stringWriter.toString().getBytes());
  }

  /**
   * Returns input stream
   *
   * @return the test inputs
   */
  @Override
  public InputStream getInputStream() {
    return this.testInputs;
  }

  /**
   * Returns output stream
   *
   * @return the saved test log
   */
  @Override
  public OutputStream getOutputStream() {
    return this.testLog;
  }
}
