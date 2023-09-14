package cs3500.pa03.controller;

import cs3500.pa03.controller.validator.BattleshipValidator;
import cs3500.pa03.controller.validator.BoardDimensionValidator;
import cs3500.pa03.controller.validator.FleetSelectionValidator;
import cs3500.pa03.controller.validator.TakeShotsValidator;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.board.CellState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.players.BotPlayer;
import cs3500.pa03.model.players.ManualPlayer;
import cs3500.pa03.model.players.Player;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import cs3500.pa03.viewer.BattleshipViewer;
import cs3500.pa03.viewer.TerminalViewer;
import cs3500.pa04.SuperBot;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a controller for a manual player
 */
public class PlayerController implements BattleshipController {

  /**
   * The manual player of this controller
   */
  private Player player;

  /**
   * The bot player of this controller
   */
  private Player bot;

  /**
   * The manual player's board state of this controller
   */
  private BoardsState boardsState;

  /**
   * The bot player board state
   */
  private BoardsState botBoardState;

  /**
   * The object to display to in this controller
   */
  private final BattleshipViewer viewer;

  /**
   * Separates each instruction in game
   */
  private static final String TEXT_SEPARATOR = "-".repeat(50) + "\n";

  /**
   * @param readable   the input to be read from
   * @param appendable the appendable to write to
   * @param random     random seed for testing
   */
  public PlayerController(Readable readable, Appendable appendable, Random random) {
    // Set up viewer
    Readable input = readable;
    Appendable output = appendable;
    this.viewer = new TerminalViewer(input, output);

    // Set up the game by initializing player and bot
    this.setUpGame(random);
  }

  /**
   * Constructor for actual game
   */
  public PlayerController() {
    this(new InputStreamReader(System.in), new PrintStream(System.out), new Random(5));
  }

  /**
   * Starts the battleship program by allowing user input in this Player Controller
   */
  @Override
  public void run() {
    while (this.boardsState.getShipsLeft() > 0) {
      List<Coord> botShots = this.bot.takeShots();
      if (botShots.size() == 0) {
        break;
      }
      // Get user's shot
      this.displayBoards();
      this.enterShots();

      // Launch Player Shots
      List<Coord> playerShots = this.player.takeShots();
      this.boardsState.updatePlayerShotsRound(playerShots);
      this.boardsState.updateOpponentShotsRound(botShots);

      this.botBoardState.updatePlayerShotsRound(botShots);
      this.botBoardState.updateOpponentShotsRound(playerShots);

      // report damage
      List<Coord> playerShotsDamage = this.bot.reportDamage(playerShots);
      List<Coord> botShotsDamage = this.player.reportDamage(botShots);

      // update damage to board
      this.boardsState.updatePlayerDamage(this.boardsState.getPlayerShipList(), botShotsDamage);
      this.boardsState.updateOpponentDamage(this.botBoardState.getPlayerShipList(),
          playerShotsDamage);

      this.botBoardState.updatePlayerDamage(this.botBoardState.getPlayerShipList(),
          playerShotsDamage);
      this.botBoardState.updateOpponentDamage(botShotsDamage);
      this.bot.successfulHits(botShotsDamage);
    }

    int botShipsLeft = 0;
    for (Ship botShips : this.botBoardState.getPlayerShipList()) {
      if (!botShips.isSunk()) {
        botShipsLeft++;
      }
    }
    this.displayBoards();
    this.decideWinner(botShipsLeft, this.boardsState.getShipsLeft());
  }

  /**
   * @param botShips    the number of ships left for the bot
   * @param playerShips the number of ships left for the player
   */
  private void decideWinner(int botShips, int playerShips) {
    if (botShips <= 0 && playerShips <= 0) {
      this.viewer.displayText("It was a draw!");
    } else if (playerShips <= 0) {
      this.viewer.displayText("You lost!");
    } else {
      this.viewer.displayText("You win!");
    }
  }

  /**
   * Displays the players' boards
   */
  private void displayBoards() {
    CellState[][] playerBoard = this.boardsState.getPlayerBoard();
    CellState[][] opponentBoard = this.boardsState.getOpponentBoard();
    CellState[][] AIBoard = this.botBoardState.getOpponentBoard();
    this.viewer.displayText(TEXT_SEPARATOR);
    this.viewer.displayText("Opponent Board Data:\n");
    this.viewer.displayBoard(opponentBoard);
    this.viewer.displayText("Your Board:\n");
    this.viewer.displayBoard(playerBoard);
    this.viewer.displayText("AI Board:\n");
    this.viewer.displayBoard(AIBoard);

  }

  /**
   * Prompt user to input coordinates of their shots
   */
  private void enterShots() {
    int shotsToTake = this.boardsState.getShipsLeft();
    int boardHeight = this.boardsState.getPlayerBoard().length;
    int boardWidth = this.boardsState.getPlayerBoard()[0].length;
    if (boardHeight * boardWidth - this.boardsState.getShotsTaken().size() < shotsToTake) {
      shotsToTake = boardHeight * boardWidth - this.boardsState.getShotsTaken().size();
    }

    this.viewer.displayText(
        "Please Enter " + shotsToTake + " Shots (x, y) [Convert hex to decimal]:\n");
    this.viewer.displayText(TEXT_SEPARATOR);

    for (int shots = 0; shots < shotsToTake; shots++) {
      String invalidShotsMessage = TEXT_SEPARATOR
          + "Please enter shots that have not been taken or/and shots that are within the "
          + "board dimension.\n" + (shotsToTake - shots) + " Shots Left:\n" + TEXT_SEPARATOR;
      Coord target =
          this.processInput(new TakeShotsValidator(this.boardsState), invalidShotsMessage);
      this.boardsState.addToShotTaken(target);
    }
  }

  /**
   * @param random random seed for testing
   */
  private void setUpGame(Random random) {
    // get board dimension
    String invalidBoardDimensionMsg = TEXT_SEPARATOR
        + "Uh Oh! You've entered invalid dimensions. Please remember that the height and width\n"
        + "of the game must be in the range (6, 15), inclusive. Try again!\n" + TEXT_SEPARATOR;
    this.viewer.displayText("Hello! Welcome to the OOD BattleSalvo Game!\n");
    this.viewer.displayText("Please enter a valid height and width below:\n");
    this.viewer.displayText(TEXT_SEPARATOR);
    Integer[] boardDimension =
        this.processInput(new BoardDimensionValidator(), invalidBoardDimensionMsg);
    int boardHeight = boardDimension[0];
    int boardWidth = boardDimension[1];
    int smallerDimension = Math.min(boardWidth, boardHeight);

    // get fleet selection
    String invalidFleetSelectionMsg =
        TEXT_SEPARATOR + "Uh Oh! You've entered invalid fleet sizes.\n"
            + "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].\n"
            + "Remember, your fleet may not exceed size " + smallerDimension + ".\n"
            + TEXT_SEPARATOR;
    this.viewer.displayText(TEXT_SEPARATOR);
    this.viewer.displayText(
        "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].\n");
    this.viewer.displayText("Remember, your fleet may not exceed size " + smallerDimension + ".\n");
    this.viewer.displayText(TEXT_SEPARATOR);

    final Map<ShipType, Integer> fleetSelection =
        this.processInput(new FleetSelectionValidator(smallerDimension), invalidFleetSelectionMsg);

    // initialize player and bot
    this.boardsState = new BoardsState();
    this.botBoardState = new BoardsState();

    this.player = new ManualPlayer(this.boardsState, random);
    this.player.setup(boardHeight, boardWidth, fleetSelection);

    this.bot = new SuperBot(this.botBoardState, random);
    this.bot.setup(boardHeight, boardWidth, fleetSelection);
  }

  /**
   * @param validator conditions for validating an input
   * @param errorMsg  the error message to display if input is invalid
   * @param <T>       data type to return if input is valid
   * @return a valid processed user input
   */
  private <T> T processInput(BattleshipValidator<T> validator, String errorMsg) {
    boolean valid = false;
    T result = null;
    while (!valid) {
      try {
        String userInput = this.viewer.getUserInput();
        result = validator.validate(userInput);
        valid = true;
      } catch (Exception e) {
        this.viewer.displayText(errorMsg);
      }
    }
    return result;
  }
}
