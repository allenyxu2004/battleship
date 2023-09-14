package cs3500.pa04.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple utils class used to hold static methods that help with serializing and deserializing JSON.
 */
public class JsonUtils {
  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * Converts a given record object to a JsonNode.
   *
   * @param record the record to convert
   * @return the JsonNode representation of the given record
   * @throws IllegalArgumentException if the record could not be converted correctly
   */
  public static JsonNode serializeRecord(Record record) throws IllegalArgumentException {
    try {
      return mapper.convertValue(record, JsonNode.class);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Given record cannot be serialized");
    }
  }

  /**
   * Serializes a record and method name into json node
   *
   * @param methodName name of the method
   * @param record the record to serialize
   * @return the json node output
   */
  public static JsonNode serializeRecordAsMessageJson(String methodName, Record record) {
    JsonNode incompleteMessage = serializeRecord(record);
    MessageJson messageJson = new MessageJson(methodName, incompleteMessage);
    return serializeRecord(messageJson);
  }
}
