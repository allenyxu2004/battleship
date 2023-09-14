package cs3500.pa03.viewer.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Represents a writer appendable test
 */
class WriterAppendableTest {

  /**
   * Test for write method
   */
  @Test
  void testWrite() {
    StringBuilder testStr = new StringBuilder();
    WriterAppendable writerGeneral = new WriterAppendable(testStr);

    assertEquals("", testStr.toString());

    writerGeneral.write("some string\n");

    assertEquals("some string\n", testStr.toString());
  }

  /**
   * Test for exceptions in write method
   */
  @Test
  void testWriteException() {
    MockAppendable mock = new MockAppendable();
    WriterAppendable writerGeneral = new WriterAppendable(mock);

    assertThrows(RuntimeException.class, () -> writerGeneral.write("something"));
  }
}