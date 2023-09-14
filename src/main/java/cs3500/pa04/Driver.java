package cs3500.pa04;

import cs3500.pa03.controller.BattleshipController;
import cs3500.pa03.controller.PlayerController;
import cs3500.pa03.model.board.BoardsState;
import cs3500.pa03.model.players.BotPlayer;
import cs3500.pa03.model.players.Player;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * This method connects to the server at the given host and port, builds a proxy referee
   * to handle communication with the server, and sets up a client player.
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if there is a communication issue with the server
   * @return the amt of games played
   */
  public static int runClient(String host, int port)
      throws IOException, IllegalStateException {
    int gamesPlayed = 0;
    for (int i = 0; i < 50; i++) {
      Socket socket = new Socket(host, port);
      BoardsState boards = new BoardsState();
      Player superBot = new SuperBot(boards, new Random());
      BattleshipController controller = new ProxyController(socket, superBot, boards);
      controller.run();
      gamesPlayed++;
    }
    return gamesPlayed;
  }

  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    try {
      if (args.length == 0) {
        PlayerController battleShipController = new PlayerController();
        battleShipController.run();
      } else if (args.length == 2) {
        runClient(args[0], Integer.parseInt(args[1]));
      } else {
        throw new IllegalArgumentException("Please enter 0 or 2 arguments!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went very wrong!");
    }
  }
}