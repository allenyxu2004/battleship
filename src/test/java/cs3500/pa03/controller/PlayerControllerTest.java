package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.viewer.reader.ReadFile;
import java.io.File;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for player controller class
 */
class PlayerControllerTest {

  Random random;
  PrintStream output;

  /**
   * Initializes random and output needed for testing player controller
   */
  @BeforeEach
  public void initControllerTest() {
    this.random = new Random();
    this.output = new PrintStream(System.out);
  }

  /**
   * @param path the path to the file
   * @return the string read from the file
   */
  private String readFromPath(String path) {
    File file = new File(path);
    ReadFile reader = new ReadFile(file);
    return reader.read();
  }

  /**
   * @param inputPath  the path to the input file
   * @param outputPath the path to the expected output file
   */
  private void runTest(String inputPath, String outputPath) {

    for (int i = 0; i < 100; i++) {
      this.random = new Random();
      String inputString = this.readFromPath(inputPath);
      StringReader input = new StringReader(inputString);
      PlayerController controller = new PlayerController(input, this.output, this.random);
      controller.run();
    }


    String expectedOutputResult =
        this.readFromPath(outputPath);

  }

  /**
   * Test for player win scenario
   */
  @Test
  public void runPlayerWin() {
    this.runTest("src/test/resources/inputTest/inputPlayerWin.txt",
        "src/test/resources/expectedOutput/expectedPlayerWinOutput.txt");
  }

  /**
   * Test for player draw scenario
   */
  @Test
  public void runPlayerDraw() {
    this.runTest("src/test/resources/inputTest/inputPlayerDraw.txt",
        "src/test/resources/expectedOutput/expectedPlayerDrawOutput.txt");
  }

  /**
   * Test for player lost scenario
   */
  @Test
  public void runPlayerLost() {
    this.runTest("src/test/resources/inputTest/inputPlayerLose.txt",
        "src/test/resources/expectedOutput/expectedPlayerLoseOutput.txt");
  }

  /**
   * Test for edge case scenario
   */
  @Test
  public void runEdgeCase() {
    this.runTest("src/test/resources/inputTest/inputEdgeCase.txt",
        "src/test/resources/expectedOutput/expectedEdgeCaseOutput.txt");
  }

  @Test
  public void runComp() {
    this.runTest("src/test/resources/inputTest/test.txt",
        "src/test/resources/expectedOutput/results.txt");
  }
}