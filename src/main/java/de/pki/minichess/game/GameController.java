package de.pki.minichess.game;

import java.io.IOException;

import de.pki.minichess.ai.IPlayer;
import de.pki.minichess.ai.PlayerRandom;
import de.pki.minichess.ai.Team3Player;
import de.pki.minichess.client.connector.Client;

/**
 * Controls the automatic game processing.
 * 
 * A good point to insert the KI.
 *
 */
public class GameController {
    
  // The Client that sends the commands to the telnet server.
  private Client client;
  
  public GameController(Client client) {
    this.client = client;
  }
   
  /**
   * Executes an automatically played game.
   * KI should be inserted here.
   *  
   * @return The game steps until game finishes.
   * @throws IOException On any communicaton problem with the telnet server.
   */
  public String runGame(char color) throws IOException {
    State gameState = new State();
    Color playerColor = Color.getColor(color);
    if (playerColor == Color.EMPTY) return "Invalid color: "+color;
    
    IPlayer player = new PlayerRandom(Color.getColor(color));
    
    StringBuilder builder = new StringBuilder();
    boolean gameOver = false;
    
    Color lastPlayer;
    int i = 0;
    while (!gameOver) {
      System.out.println("Move: " + i++);
      Move nextMove = null;
      
      lastPlayer = gameState.getCurrentPlayer();

      if (gameState.getCurrentPlayer() == playerColor) {
        nextMove = player.pickMove(gameState.getBoard());
        this.client.sendMove(nextMove.toTelnetString());

      } else  {
     
        String response = this.client.getMove();
    
        nextMove = new Move(response);
      }
      builder.append(nextMove+"\n");
      gameOver = gameState.moveByMove(nextMove);

      builder.append("------------------\n");
      builder.append(gameState.getCurrentStateToString() + "\n");
      System.out.println(""+gameState.getMoveNumber() + ": Current payer "+lastPlayer.toString()+" plays "+ nextMove.toTelnetString() );
    }
    return builder.toString();
  }

}
