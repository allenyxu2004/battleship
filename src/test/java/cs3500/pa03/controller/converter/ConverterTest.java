package cs3500.pa03.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.ships.ShipType;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test for converter class
 */
class ConverterTest {

  /**
   * Test for converting strings to an integer array
   */
  @Test
  public void convertStrToIntArray() {
    assertTrue(
        Arrays.equals(new Integer[] {1, 2, 3, 4}, Converter.convertStrToIntArray("1 2 3 4")));
    assertTrue(
        Arrays.equals(new Integer[] {1, 2, 3}, Converter.convertStrToIntArray("1 2 3 ")));
    assertTrue(
        Arrays.equals(new Integer[] {1, 2}, Converter.convertStrToIntArray("1 2")));
  }

  /**
   * Test for any exceptions regarding the conversion of string to integer arrays
   */
  @Test
  public void convertStrToIntArrayException() {
    assertThrows(NumberFormatException.class,
        () -> Converter.convertStrToIntArray("not an integer"));
    assertThrows(NumberFormatException.class,
        () -> Converter.convertStrToIntArray("1, 2, 3"));
  }

  /**
   * Test for conversion from integer array to fleet selection format
   */
  @Test
  public void convertIntArrayToMapShipType() {
    Map<ShipType, Integer> expectedMap1 =
        Map.of(ShipType.CARRIER, 1, ShipType.BATTLESHIP, 2, ShipType.DESTROYER,
            3, ShipType.SUBMARINE, 4);
    assertEquals(expectedMap1, Converter.convertIntArrayToMapShipType(new Integer[] {1, 2, 3, 4}));
    assertEquals(expectedMap1,
        Converter.convertIntArrayToMapShipType(new Integer[] {1, 2, 3, 4, 5}));
  }

  /**
   * Test for any exceptions regarding the conversion of integer arrays to fleet selection format
   */
  @Test
  public void convertIntArrayToMapShipTypeException() {
    assertThrows(IndexOutOfBoundsException.class,
        () -> Converter.convertIntArrayToMapShipType(new Integer[] {1, 2, 3}));
    assertThrows(IndexOutOfBoundsException.class,
        () -> Converter.convertIntArrayToMapShipType(new Integer[] {1, 2}));
  }
}