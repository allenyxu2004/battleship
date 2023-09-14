package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the join json
 *
 * @param githubName player name
 * @param gameMode the player game mode
 */
public record JoinJson(
    @JsonProperty("name") String githubName,
    @JsonProperty("game-type") String gameMode) {
}
