package cs3500.pa03.model.ships;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.board.CellState;
import cs3500.pa03.model.coordinates.Coord;
import java.util.ArrayList;

/**
 * Represents a ship in a battleship game
 */
public class Ship {

  /**
   * The area/coordinates that this ship takes up
   */
  private ArrayList<Coord> shipArea;

  /**
   * The type of this ship
   */
  private ShipType shipType;

  /**
   * The amount of time this ship has been hit
   */
  private int noOfHit;

  private boolean sunk;

  /**
   * @param shipArea The area/coordinates that this ship takes up
   * @param shipType The type of this ship
   */
  public Ship(ArrayList<Coord> shipArea, ShipType shipType) {
    this.shipArea = shipArea;
    this.shipType = shipType;
    this.noOfHit = 0;
    this.sunk = false;
  }

  /**
   * @param coord the given coordinate
   * @return whether this ship contains the given coordinates
   */
  public boolean containsCoord(Coord coord) {
    return shipArea.contains(coord);
  }

  /**
   * @param coord the given coordinate
   * @return whether this ship got hit by the given coordinates
   */
  public boolean isHit(Coord coord) {
    boolean hit = shipArea.contains(coord);
    if (hit) {
      noOfHit += 1;
    }
    return hit;
  }

  /**
   * @return whether this ship has sunken or not
   */
  public boolean isSunk() {
    return noOfHit == shipArea.size();
  }

  /**
   * @param board the board to place this ship on
   */
  public void placeShipOnBoard(CellState[][] board) {
    for (Coord coord : shipArea) {
      board[coord.posy][coord.posx] = CellState.valueOf(shipType.toString());
    }
  }

  /**
   * @param board the board to place this sunken ship on
   */
  public void sinkShip(CellState[][] board) {
    for (Coord coord : shipArea) {
      board[coord.posy][coord.posx] = CellState.SUNK;
    }
  }

  /**
   * @param obj the thing to be compared
   * @return whether this ship is the same as the thing to be compared
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Ship)) {
      return false;
    }
    Ship other = (Ship) obj;
    return this.shipArea.equals(other.shipArea) && this.shipType == other.shipType
        && this.noOfHit == other.noOfHit;
  }

  /**
   * @return a unique number of this ship
   */
  @Override
  public int hashCode() {
    int n = 0;
    for (Coord coord : this.shipArea) {
      n += (coord.posx + coord.posy) * 197;
    }
    n += this.noOfHit;
    return n;
  }

  /**
   * Obtains the type of ship this is
   *
   * @return the shipType enum
   */
  public ShipType getShipType() {
    return this.shipType;
  }

  /**
   * Sets the sink status for the ship
   *
   * @param sunk the status to set it to
   */
  public void setSinkStatus(boolean sunk) {
    this.sunk = sunk;
  }

  /**
   * Obtains the sink status of the ship
   *
   * @return the sink status
   */
  public boolean getSinkStatus() {
    return this.sunk;
  }

  /**
   * Obtains the list of coords for a ship
   *
   * @return the list of coords
   */
  public ArrayList<Coord> getShipCoords() {
    return this.shipArea;
  }

  /**
   * Checks if a ship is vertically oriented
   *
   * @return the ship orientation
   */
  public boolean isVertical() {
    int firstY = shipArea.get(0).posy;
    for (Coord c : shipArea) {
      if (c.posy != firstY) {
        return true;
      }
    }
    return false;
  }

  /**
   * Obtains the origin point of the ship
   *
   * @return the coordinate for the ship start
   */
  public Coord getStartCoord() {
    int x1 = this.shipArea.get(0).posx;
    int y1 = this.shipArea.get(0).posy;
    int x2 = this.shipArea.get(this.shipArea.size() - 1).posx;
    int y2 = this.shipArea.get(this.shipArea.size() - 1).posy;
    if (x1 + y1 < x2 + y2) {
      return this.shipArea.get(0);
    } else {
      return this.shipArea.get(this.shipArea.size() - 1);
    }
  }

  /**
   * Checks if coordinates overlap or not
   *
   * @param coord the coordinate to check
   * @return whether it overlaps the ship or not
   */
  public boolean coordOverlap(Coord coord) {
    boolean hit = shipArea.contains(coord);
    return hit;
  }
}
