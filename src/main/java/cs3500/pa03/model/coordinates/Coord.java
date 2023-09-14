package cs3500.pa03.model.coordinates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a coordinate on a battleship board
 */
public class Coord {
  /**
   * x position of a coordinate
   */
  @JsonProperty("x")
  public int posx;
  /**
   * y position of a coordinate
   */
  @JsonProperty("y")
  public int posy;

  /**
   * @param posx the x position of a coordinate
   * @param posy the y position of a coordinate
   */
  @JsonCreator
  public Coord(@JsonProperty("x") int posx,
               @JsonProperty("y") int posy) {
    this.posx = posx;
    this.posy = posy;
  }

  /**
   * @param obj the object to compare to this coordinate
   * @return whether the object is extensionally equal to this coordinate or not
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coord)) {
      return false;
    }
    Coord other = (Coord) obj;
    return this.posx == other.posx && this.posy == other.posy;
  }

  /**
   * @return the hashcode of this coordinate
   */
  @Override
  public int hashCode() {
    return (this.posx + this.posy) * 547;
  }

  /**
   * Changes the y value of a coord
   *
   * @param addValue whether you add or subtract 1
   * @return the new changed coordinate
   */
  public Coord changeY(boolean addValue) {
    if (addValue) {
      return new Coord(this.posx, this.posy + 1);
    } else {
      return new Coord(this.posx, this.posy - 1);
    }
  }

  /**
   * Changes the x value of a coord
   *
   * @param addValue whether you add or sub by 1
   * @return the new altered coord
   */
  public Coord changeX(boolean addValue) {
    if (addValue) {
      return new Coord(this.posx + 1, this.posy);
    } else {
      return new Coord(this.posx - 1, this.posy);
    }
  }

  /**
   * Checks if a coord is within the bounds of a given range
   *
   * @param height the max height of the coord
   * @param width the max width of the coord
   * @return if it is in bounds or not
   */
  public boolean inBounds(int height, int width) {
    boolean validX = (this.posx >= 0 && this.posx < width);
    boolean validY = (this.posy >= 0 && this.posy < height);
    return validX && validY;
  }

}
