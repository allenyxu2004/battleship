package cs3500.pa03.viewer.writer;

import java.io.IOException;

/**
 * Class used for testing writer
 */
public class MockAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    this.throwInOut();
    return null;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    this.throwInOut();
    return null;
  }

  @Override
  public Appendable append(char c) throws IOException {
    this.throwInOut();
    return null;
  }

  private void throwInOut() throws IOException {
    throw new IOException("Mock throwing an error");
  }
}
