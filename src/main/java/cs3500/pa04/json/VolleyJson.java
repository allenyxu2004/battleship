package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.coordinates.Coord;
import java.util.List;

/**
 * Represents the json for a volley
 *
 * @param coords the list of coordinates for the volley
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<Coord> coords) {
  
}
