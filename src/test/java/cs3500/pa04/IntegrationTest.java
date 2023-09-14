package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class IntegrationTest {

  /**
   * Integration test, runs game 50 times
   * note: make sure to have server running for test to pass
   */
  @Test
  public void testIntegration() {
    int actual = 0;
    String jarFilePath = "server.jar";
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFilePath);
      Process process = processBuilder.start();
      actual = new Driver().runClient("0.0.0.0", 35001);
      int exitCode = process.waitFor();
      System.out.println("The .jar file exited with code: " + exitCode);
    } catch (Exception e) {
      System.err.println("Couldn't run client");
    }
    assertEquals(50, actual);
  }

}
