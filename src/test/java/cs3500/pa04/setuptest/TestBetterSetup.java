package cs3500.pa04.setuptest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.players.Player;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa03.model.ships.ShipType;
import cs3500.pa04.SuperBot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for the better setup
 */
public class TestBetterSetup {

  private Player botModel;
  BoardsState botDataSetup;
  List<Ship> shipList;


  /**
   * Intalizes ship data for setup
   */
  @BeforeEach
  public void initData() {
    botDataSetup = new BoardsState();
    setShips();
    botDataSetup.initializeBoard(6, 6, shipList);
    botModel = new SuperBot(botDataSetup, new Random(1));
  }

  /**
   * Sets up the ships
   */
  private void setShips() {
    ArrayList<Coord> coordsSub1 = new ArrayList<>();
    coordsSub1.add(new Coord(0, 0));
    coordsSub1.add(new Coord(1, 0));
    coordsSub1.add(new Coord(2, 0));
    Ship sub = new Ship(coordsSub1, ShipType.SUBMARINE);

    ArrayList<Coord> coordsCarrier = new ArrayList<>();
    coordsCarrier.add(new Coord(0, 3));
    coordsCarrier.add(new Coord(1, 3));
    coordsCarrier.add(new Coord(2, 3));
    coordsCarrier.add(new Coord(3, 3));
    coordsCarrier.add(new Coord(4, 3));
    coordsCarrier.add(new Coord(5, 3));
    Ship carrier = new Ship(coordsCarrier, ShipType.CARRIER);

    shipList = new ArrayList<>();
    shipList.add(sub);
    shipList.add(carrier);

  }


  /**
   * Tests setting up a board given specifications
   */
  @Test
  public void testSetup() {
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    List<Ship> setupList = botModel.setup(6, 8, specifications);
    // checks that coords and content of lists is changed upon setup
    assertNotEquals(shipList, setupList);
    assertEquals(4, setupList.size());
  }

  /**
   * Tests with different specifications
   */
  @Test
  public void testSetup2() {
    Map<ShipType, Integer> specifications2 = new HashMap<>();
    specifications2.put(ShipType.CARRIER, 3);
    specifications2.put(ShipType.BATTLESHIP, 2);
    specifications2.put(ShipType.DESTROYER, 2);
    specifications2.put(ShipType.SUBMARINE, 2);
    List<Ship> setupList2 = botModel.setup(9, 10, specifications2);
    assertEquals(9, setupList2.size());

  }
}