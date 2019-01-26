package de.pki.minichess.client.ui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import de.pki.minichess.game.GameController;
import de.pki.minichess.client.connector.Client;
import de.pki.minichess.client.connector.IMCSGame;

/**
 * This class provides an old-fashion text-based console for the minichess game. This console takes commands from the
 * command line and sends them to the telnet server via the <code>connector.Client</code>. Furthermore it formats the
 * responses to be readable in the command line.
 *
 */
public class Console {

  // The Client that sends the commands to the telnet server.
  private Client client;
  // Reads input from the command line.
  private Scanner scanner = new Scanner(System.in);
  
  private char color;

  /**
   * Initializes the connection to the telnet server.
   * 
   * @throws IOException
   *           On any network errors.
   */
  public Console() throws IOException {
    System.out.println("================\n" //
        + "=   Minichess v1.0  =\n" //
        + "================\n");
    //$NON-NLS-1$
    int port = Integer.parseInt(Messages.getString("Console.port"));
    //$NON-NLS-1$
    String host = Messages.getString("Console.host");
    System.out.println("Initializing connection to telnet server " + host + ":" + port);
    this.connect(host, port);
    System.out.println("Connection established.");
  }

  /**
   * Reads a command from the command line, interprets it and sends it to the telnet server.
   * Also formats and outputs the response.
   * 
   * @return true if further commands shall be requested, false on exit.
   * @throws IOException
   *           On any network errors.
   */
  public boolean requestCommand() throws IOException {
    System.out.print("> ");
    String in = scanner.nextLine();
    String[] parts = in.split(" ");
    String command = parts[0];
    boolean goOn = true;
    String response;

    switch (Command.valueOf(command.trim().toUpperCase())) {
    case ACCEPT:
      if (parts.length == 2) {
        response = this.accept(parts[1]);
      } else if (parts.length == 3) {
        response = this.accept(parts[1], parts[2]);
      } else {
        response = "Wrong number of arguments!";
      }
      break;
    case LIST:
      response = this.getGameList();
      break;
    case HELP:
      response = this.listMethods();
      break;
    case LOGIN:
      if (parts.length == 3) {
        response = this.login(parts[1], parts[2]);
      } else {
        response = "Wrong number of arguments!";
      }
      break;
    case REGISTER:
      if (parts.length == 3) {
        response = this.register(parts[1], parts[2]);
      } else {
        response = "Wrong number of arguments!";
      }
      break;
    case OFFER:
      if (parts.length == 1) {
        response = this.offerGameAndWait();
      } else if (parts.length == 2) {
        response = this.offerGameAndWait(parts[1]);
      } else {
        response = "Wrong number of arguments!";
      }
      break;
    case PASSWORD:
      if (parts.length == 1) {
        response = this.changePassword(parts[1]);
      } else {
        response = "Wrong number of arguments!";
      }
      break;
    case RATINGS:
      response = this.getRatingList();
      break;
    case RERATE:
      response = this.rerate();
      break;
    case START:
      response = this.runGame();
      break;
    case EXIT:
      response = this.exit();
      goOn = false;
      break;
    default:
      response = "Command not recognized. Please try again.";
      break;

    }

    System.out.println(response);
    return goOn;

  }

  private String listMethods() {
    StringBuilder builder = new StringBuilder();
    String header = "=================\n" //
        + "=   Operations  =\n" //
        + "=================\n";
    builder.append(header);

    for (Command command : Command.values()) {
      builder.append(command.toString().toLowerCase() + "\n"); //$NON-NLS-1$
    }

    return builder.toString();

  }

  /**
   * Proxy methods for accessing the <code>Client</code>.
   * Used to handle and format the response and to add some meaningful text around it.
   * 
   */

  private String accept(String gameId) throws IOException {
    char response = this.client.accept(gameId);
    this.color = response;
    return "Game started. You are " + response;
  }

  private String accept(String gameId, String color) throws IOException {
    this.client.accept(gameId, color.charAt(0));
    this.color = color.charAt(0);
    return "Game started. You are " + color.charAt(0);
  }

  private String changePassword(String password) throws IOException {
    this.client.changePassword(password);
    return "Password successfully changed.";
  }

  private String getGameList() throws IOException {
    List<IMCSGame> games = this.client.getGameList();
    StringBuilder builder = new StringBuilder();
    builder.append("Games available: \n");
    for (IMCSGame game : games) {
      builder.append(game.toString() + "\n");
    }
    return builder.toString();

  }

  private String getRatingList() throws IOException {
    Map<String, Integer> ratings = this.client.getRatingsList();
    StringBuilder builder = new StringBuilder();
    builder.append("Ratings: \n");
    for (String player : ratings.keySet()) {
      builder.append(player + ": " + ratings.get(player) + "\n");
    }
    return builder.toString();
  }

  private String login(String username, String password) throws IOException, RuntimeException {
    this.client.login(username, password);
    return "Successfully logged in.";
  }

  private String register(String username, String password) throws IOException, RuntimeException {
    this.client.register(username, password);
    return "Successfully registered.";
  }

  private String offerGameAndWait() throws IOException, RuntimeException {
    char color = this.client.offerGameAndWait();
    this.color = color;
    return "Game started. You play " + color;
  }

  private String offerGameAndWait(String color) throws IOException, RuntimeException {
    this.client.offerGameAndWait(color.charAt(0));
    this.color = color.charAt(0);
    return "Game started. You play " + color.charAt(0);
  }

  private String rerate() throws IOException {
    this.client.rerate();
    return "Rerated.";

  }

  private String runGame() {
    try {
      return new GameController(client).runGame(this.color);
    } catch (IOException e) {
      return "Error while communicating with the telnet server.";
    }
  }

  private String exit() throws IOException {
    this.client.close();
    return "Bye bye!";
  }

  private void connect(String host, int port) throws IOException {
    this.client = new Client(host, port);
  }

}
