package cs3500.pa03.viewer.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Test for ReadFile class
 */
class ReadFileTest {
  /**
   * Test for read
   */
  @Test
  public void testRead() {
    File abcFile = new File("src/test/resources/readerTest/abc.txt");
    ReadFile readFile = new ReadFile(abcFile);
    assertEquals("# abc\n\n\nsth", readFile.read());
  }

  /**
   * Test for read exceptions
   */
  @Test
  public void testReadException() {
    File corruptFile = new File("src/invalid");
    ReadFile readFile = new ReadFile(corruptFile);
    assertThrows(RuntimeException.class, () -> readFile.read());
  }
}