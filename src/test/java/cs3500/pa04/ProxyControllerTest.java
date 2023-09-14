package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.players.Player;
import cs3500.pa03.model.ships.ShipType;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetUpRequirementJson;
import cs3500.pa04.json.VolleyJson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for the proxy controller
 */
public class ProxyControllerTest {

  private ByteArrayOutputStream testLog;
  private ProxyController controller;

  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }

  /**
   * Test for server setup
   */
  @Test
  public void testResponse() {
    // Prepare sample message
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    SetUpRequirementJson endJson = new SetUpRequirementJson(6, 6, specifications);
    JsonNode sampleMessage = createSampleMessage("setup", endJson);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    try {
      BoardsState boards = new BoardsState();
      Player superBot = new SuperBot(boards, new Random(1));
      this.controller = new ProxyController(socket, superBot, boards);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }

    this.controller.run();

    String expected =
        "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{\"length\":6,"
            + "\"coord\":{\"x\":0,\"y\":0},\"direction\":\"VERTICAL\"},"
            + "{\"length\":5,\"coord\":{\"x\":1,\"y\":0},\"direction\":\"VERTICAL\"},"
            + "{\"length\":4,\"coord\":{\"x\":2,\"y\":1},\"direction\":\"HORIZONTAL\"},"
            + "{\"length\":3,\"coord\":{\"x\":2,\"y\":5},\"direction\":\"HORIZONTAL\"}]}}"
            + System.lineSeparator();
    assertEquals(expected, logToString());
  }


  /**
   * Test for server join
   */
  @Test
  public void testJoin() {
    // Prepare sample message
    JoinJson joinJson = new JoinJson("Test", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("join", joinJson);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    try {
      BoardsState boards = new BoardsState();
      Player superBot = new SuperBot(boards, new Random(1));
      this.controller = new ProxyController(socket, superBot, boards);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    this.controller.run();
    String expected =
        "{\"method-name\":\"join\",\"arguments\":"
            + "{\"name\":\"khvitri\",\"game-type\":\"SINGLE\"}}"
            + System.lineSeparator();
    assertEquals(expected, logToString());
  }

  /**
   * Test for server take shots
   */
  @Test
  public void testVolley() {
    // Prepare sample message
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);

    ArrayList<Coord> shots = new ArrayList<>();
    shots.add(new Coord(0, 0));
    shots.add(new Coord(1, 1));
    shots.add(new Coord(2, 2));
    shots.add(new Coord(3, 3));
    VolleyJson volleyJson = new VolleyJson(shots);
    JsonNode sampleMessage = createSampleMessage("take-shots", volleyJson);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    try {
      BoardsState boards = new BoardsState();
      Player superBot = new SuperBot(boards, new Random(1));
      superBot.setup(6, 6, specifications);
      this.controller = new ProxyController(socket, superBot, boards);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    this.controller.run();
    String expected =
        "{\"method-name\":\"take-shots\",\"arguments\":"
            + "{\"coordinates\":[{\"x\":2,\"y\":3},"
            + "{\"x\":3,\"y\":2},{\"x\":2,\"y\":2},{\"x\":3,\"y\":3}]}}"
            + System.lineSeparator();
    assertEquals(expected, logToString());
  }


  /**
   * Test for end game
   */
  @Test
  public void testEndGame() {
    // Prepare sample message
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    EndGameJson endGame = new EndGameJson("You won", "testing");
    JsonNode sampleMessage = createSampleMessage("end-game", endGame);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    try {
      BoardsState boards = new BoardsState();
      Player superBot = new SuperBot(boards, new Random(1));
      superBot.setup(6, 6, specifications);
      this.controller = new ProxyController(socket, superBot, boards);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    this.controller.run();
    String expected =
        "{\"method-name\":\"end-game\",\"arguments\":{}}"
            + System.lineSeparator();
    assertEquals(expected, logToString());
  }


  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName, JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }


}
