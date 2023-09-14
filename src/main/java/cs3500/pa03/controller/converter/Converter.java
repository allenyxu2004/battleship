package cs3500.pa03.controller.converter;

import cs3500.pa03.model.ships.ShipType;
import java.util.Map;

/**
 * A class that converts one type/class to another type/class
 */
public class Converter {
  /**
   * @param input the user input as a string
   * @return an array of integers
   * @throws NumberFormatException unable to convert the string into an array of integers
   */
  public static Integer[] convertStrToIntArray(String input) throws NumberFormatException {
    String[] inputArr = input.split(" ");
    Integer[] result = new Integer[inputArr.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = Integer.parseInt(inputArr[i]);
    }
    return result;
  }

  /**
   * @param input the array of integers
   * @return a map with ShipType as key and Integer as value (fleet selection format)
   */
  public static Map<ShipType, Integer> convertIntArrayToMapShipType(Integer[] input)
      throws IndexOutOfBoundsException {
    return Map.of(ShipType.CARRIER, input[0], ShipType.BATTLESHIP, input[1], ShipType.DESTROYER,
        input[2], ShipType.SUBMARINE, input[3]);
  }
}
