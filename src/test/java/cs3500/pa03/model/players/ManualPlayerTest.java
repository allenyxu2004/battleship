package cs3500.pa03.model.players;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for a manual player
 */
class ManualPlayerTest {
  Random random;
  BoardsState mockBoard;
  Player manualPlayer;
  Map<ShipType, Integer> mockSpecifications;
  Ship mockCarrier;
  Ship mockBattleship;
  Ship mockDestroyer;
  Ship mockSubmarine;

  /**
   * Initializations for manual player test
   */
  @BeforeEach
  public void initManualPlayerTest() {
    this.random = new Random(2);
    this.mockBoard = new BoardsState();
    this.manualPlayer = new ManualPlayer(this.mockBoard, this.random);

    this.mockSpecifications =
        Map.of(ShipType.CARRIER, 1, ShipType.BATTLESHIP, 1, ShipType.DESTROYER,
            1, ShipType.SUBMARINE, 1);
    this.initMockShips();
  }

  /**
   * Initializes the mock ships needed for testing
   */
  private void initMockShips() {
    Coord coordCarrier1 = new Coord(0, 3);
    Coord coordCarrier2 = new Coord(1, 3);
    Coord coordCarrier3 = new Coord(2, 3);
    Coord coordCarrier4 = new Coord(3, 3);
    Coord coordCarrier5 = new Coord(4, 3);
    Coord coordCarrier6 = new Coord(5, 3);
    ArrayList<Coord> carrierArea = new ArrayList<Coord>(
        Arrays.asList(coordCarrier1, coordCarrier2, coordCarrier3, coordCarrier4, coordCarrier5,
            coordCarrier6));
    this.mockCarrier = new Ship(carrierArea, ShipType.CARRIER);

    Coord coordBattleShip1 = new Coord(1, 5);
    Coord coordBattleShip2 = new Coord(2, 5);
    Coord coordBattleShip3 = new Coord(3, 5);
    Coord coordBattleShip4 = new Coord(4, 5);
    Coord coordBattleShip5 = new Coord(5, 5);
    ArrayList<Coord> battleshipArea = new ArrayList<Coord>(
        Arrays.asList(coordBattleShip1, coordBattleShip2, coordBattleShip3, coordBattleShip4,
            coordBattleShip5));
    this.mockBattleship = new Ship(battleshipArea, ShipType.BATTLESHIP);

    Coord coordDestroyer1 = new Coord(0, 0);
    Coord coordDestroyer2 = new Coord(1, 0);
    Coord coordDestroyer3 = new Coord(2, 0);
    Coord coordDestroyer4 = new Coord(3, 0);
    ArrayList<Coord> destroyerArea = new ArrayList<Coord>(
        Arrays.asList(coordDestroyer1, coordDestroyer2, coordDestroyer3, coordDestroyer4));
    this.mockDestroyer = new Ship(destroyerArea, ShipType.DESTROYER);

    Coord coordSubmarine1 = new Coord(5, 2);
    Coord coordSubmarine2 = new Coord(4, 2);
    Coord coordSubmarine3 = new Coord(3, 2);
    ArrayList<Coord> submarineArea = new ArrayList<Coord>(
        Arrays.asList(coordSubmarine1, coordSubmarine2, coordSubmarine3));
    this.mockSubmarine = new Ship(submarineArea, ShipType.SUBMARINE);
  }

  /**
   * Test for name method
   */
  @Test
  public void name() {
    assertEquals("khvitri", this.manualPlayer.name());
  }

  /**
   * Test for setup method for a player
   */
  @Test
  public void setup() {
    List<Ship> expectedShips = new ArrayList<>(
        Arrays.asList(this.mockCarrier, this.mockBattleship, this.mockDestroyer,
            this.mockSubmarine));
    assertEquals(expectedShips, this.manualPlayer.setup(6, 6, this.mockSpecifications));
  }

  /**
   * Test for when manual player take shots at their opponent
   */
  @Test
  public void takeShots() {
    this.setup();
    Coord shot1 = new Coord(0, 0);
    Coord shot2 = new Coord(1, 0);
    Coord shot3 = new Coord(2, 0);
    Coord shot4 = new Coord(3, 0);
    this.mockBoard.addToShotTaken(shot1);
    this.mockBoard.addToShotTaken(shot2);
    this.mockBoard.addToShotTaken(shot3);
    this.mockBoard.addToShotTaken(shot4);

    List<Coord> shotsTaken = new ArrayList<>(Arrays.asList(shot4, shot3, shot2, shot1));
    assertEquals(shotsTaken, this.manualPlayer.takeShots());
  }

  /**
   * Test for the damage inflicted upon the player's ship when the opponent shoots
   */
  @Test
  public void reportDamage() {
    this.setup();
    List<Coord> shotsFired1 =
        new ArrayList<>(Arrays.asList(new Coord(0, 0), new Coord(1, 0)));
    assertEquals(shotsFired1, this.manualPlayer.reportDamage(shotsFired1));

    List<Coord> shotsFired2 =
        new ArrayList<>(Arrays.asList(new Coord(0, 4), new Coord(0, 5)));

    List<Coord> missAllShots = new ArrayList<Coord>();
    assertEquals(missAllShots, this.manualPlayer.reportDamage(shotsFired2));
  }

  /**
   * Test for successful hits
   */
  @Test
  public void successfulHits() {
    this.manualPlayer.successfulHits(new ArrayList<Coord>(Arrays.asList(new Coord(0, 0))));
  }

  /**
   * Test for endGame method
   */
  @Test
  public void endGame() {
    this.manualPlayer.endGame(new GameResult(), "nothing");
  }
}