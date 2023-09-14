package cs3500.pa03.model.ships;

import cs3500.pa03.model.coordinates.Coord;
import java.util.ArrayList;

/**
 * Represents the different ship types
 */
public enum ShipType {

  /**
   * Represents a carrier in battleship
   */
  CARRIER(6),

  /**
   * Represents a battleship in battleship
   */
  BATTLESHIP(5),

  /**
   * Represents a destroyer in battleship
   */
  DESTROYER(4),

  /**
   * Represents a submarine in battleship
   */
  SUBMARINE(3);

  /**
   * The size of the ship type
   */
  private final int size;

  ShipType(int size) {
    this.size = size;
  }

  /**
   * @param startPosition the starting coordinate to generate the ship area
   * @param vertical ship be placed vertical or horizontal?
   * @param inverted invert the up/right direction?
   * @return list of coordinates this ship type is placed on
   */
  public ArrayList<Coord> generateShipArea(Coord startPosition, boolean vertical,
                                           boolean inverted) {
    int dx = vertical ? 0 : 1;
    int dy = vertical ? 1 : 0;
    int direction = inverted ? -1 : 1;
    ArrayList<Coord> shipArea = new ArrayList<>();
    for (int i = 0; i < this.size; i++) {
      shipArea.add(new Coord(startPosition.posx + (dx * i * direction),
          startPosition.posy + (dy * i * direction)));
    }
    return shipArea;
  }

  /**
   * Returns the size of the ship type
   *
   * @return the ship type size
   */
  public int returnSize() {
    return this.size;
  }
}
