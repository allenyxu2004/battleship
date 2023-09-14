package cs3500.pa04;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.controller.BattleshipController;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.players.Player;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.viewer.TerminalViewer;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetUpRequirementJson;
import cs3500.pa04.json.VolleyJson;
import cs3500.pa04.mode.GameMode;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the proxy controller
 */
public class ProxyController implements BattleshipController {
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final ObjectMapper mapper = new ObjectMapper();
  private final BoardsState boardsState;

  /**
   * Constructor for a proxy controller
   *
   * @param server server to connect to
   * @param player type of player
   * @param boardsState the board to store info
   * @throws IOException if controller cannot be created
   */
  public ProxyController(Socket server, Player player, BoardsState boardsState) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
    this.boardsState = boardsState;
  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON.
   */
  @Override
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    switch (name) {
      case "join":
        handleJoin(name);
        break;
      case "setup":
        handleSetUp(name, arguments);
        break;
      case "take-shots":
        handleTakeShots(name);
        break;
      case "report-damage":
        handleReportDamage(name, arguments);
        break;
      case "successful-hits":
        handleSuccessfulHits(name, arguments);
        break;
      case "end-game":
        handleEndGame(name, arguments);
        break;
      default:
        throw new IllegalArgumentException("Invalid message name!");
    }
  }

  private void handleJoin(String methodName) {
    String githubName = this.player.name();

    JoinJson response = new JoinJson(githubName, GameMode.MULTI.toString());
    JsonNode jsonResponse = JsonUtils.serializeRecordAsMessageJson(methodName, response);
    this.out.println(jsonResponse);
  }

  private void handleSetUp(String methodName, JsonNode arguments) {
    SetUpRequirementJson setUpArgs =
        this.mapper.convertValue(arguments, SetUpRequirementJson.class);

    List<Ship> shipList =
        this.player.setup(setUpArgs.height(), setUpArgs.width(), setUpArgs.specifications());

    List<ShipAdapter> shipAdapters = new ArrayList<>();
    for (Ship ship : shipList) {
      shipAdapters.add(new ShipAdapter(ship));
    }

    FleetJson response = new FleetJson(shipAdapters);
    JsonNode jsonResponse = JsonUtils.serializeRecordAsMessageJson(methodName, response);
    this.out.println(jsonResponse);
  }

  private void handleTakeShots(String methodName) {
    List<Coord> shots = this.player.takeShots();
    this.boardsState.updatePlayerShotsRound(shots);
    System.out.println();
    System.out.println("Round: ");
    System.out.println("Ships left: " + this.boardsState.getShipsLeft());
    for (Coord coord : shots) {
      System.out.println("X: " + coord.posx + ", Y: " + coord.posy);
    }

    VolleyJson response = new VolleyJson(shots);
    JsonNode jsonResponse = JsonUtils.serializeRecordAsMessageJson(methodName, response);
    this.out.println(jsonResponse);
  }

  private void handleReportDamage(String methodName, JsonNode arguments) {
    VolleyJson opponentShotsJson = this.mapper.convertValue(arguments, VolleyJson.class);

    List<Coord> opponentShots = opponentShotsJson.coords();

    List<Coord> damage = this.player.reportDamage(opponentShots);
    this.boardsState.updatePlayerDamage(this.boardsState.getPlayerShipList(), damage);

    VolleyJson response = new VolleyJson(damage);
    JsonNode jsonResponse = JsonUtils.serializeRecordAsMessageJson(methodName, response);
    this.out.println(jsonResponse);
  }

  private void handleSuccessfulHits(String methodName, JsonNode arguments) {
    VolleyJson successfulShotJson = this.mapper.convertValue(arguments, VolleyJson.class);

    List<Coord> successfulShots = successfulShotJson.coords();

    this.boardsState.updateOpponentDamage(successfulShots);
    this.player.successfulHits(successfulShots);

    MessageJson response = new MessageJson(methodName, this.mapper.createObjectNode());
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.println(jsonResponse);
  }

  private void handleEndGame(String methodName, JsonNode arguments) {
    EndGameJson endGameJson = this.mapper.convertValue(arguments, EndGameJson.class);

    System.out.println(endGameJson.win());
    System.out.println(endGameJson.reason());

    MessageJson response = new MessageJson(methodName, this.mapper.createObjectNode());
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.println(jsonResponse);
  }
}
