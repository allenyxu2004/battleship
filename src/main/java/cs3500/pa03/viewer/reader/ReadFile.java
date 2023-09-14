package cs3500.pa03.viewer.reader;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a class used to read a given file
 */
public class ReadFile implements Reader {
  private final File file;

  /**
   * @param file the file to be read from
   */
  public ReadFile(File file) {
    this.file = Objects.requireNonNull(file);
  }

  /**
   * @return a string containing the contents of a file
   */
  @Override
  public String read() {
    StringBuilder output = new StringBuilder();
    try {
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        output.append(scanner.nextLine()).append("\n");
      }
    } catch (IOException e) {
      throw new RuntimeException("File can't be read!");
    }
    return output.toString().trim();
  }
}
