package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.coordinates.Coord;
import cs3500.pa03.model.ships.Ship;
import cs3500.pa04.direction.Direction;

/**
 * Represents a ship adaptor for json
 */
public class ShipAdapter {
  @JsonProperty("coord")
  private Coord coord;
  @JsonProperty("length")
  private int shipLength;
  @JsonProperty("direction")
  private Direction direction;

  /**
   * Constructor for adapting a ship
   *
   * @param ship the ship to be adapted
   */
  public ShipAdapter(Ship ship) {
    this.coord = ship.getStartCoord();
    this.shipLength = ship.getShipCoords().size();
    if (ship.isVertical()) {
      this.direction = Direction.VERTICAL;
    } else {
      this.direction = Direction.HORIZONTAL;
    }
  }

  /**
   * Gets the coordinate of the ship
   *
   * @return the ship coordinate
   */
  public Coord getCoord() {
    return this.coord;
  }

  /**
   * Gets the length of the ship
   *
   * @return ship length
   */
  public int getLength() {
    return this.shipLength;
  }


}
