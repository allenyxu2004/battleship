package cs3500.pa03.viewer.writer;

import java.io.IOException;

/**
 * Represents a writer that can write to anything as long as it is an appendable
 */
public class WriterAppendable implements Writer {

  private final Appendable output;

  /**
   * @param appendable the appendable to be written to
   */
  public WriterAppendable(Appendable appendable) {
    this.output = appendable;
  }

  /**
   * @param phrase the string to be written by this writer
   */
  @Override
  public void write(String phrase) {
    try {
      this.output.append(phrase);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
