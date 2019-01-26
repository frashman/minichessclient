package de.pki.minichess.ai;

import java.util.Vector;

import de.pki.minichess.game.Color;
import de.pki.minichess.game.Move;
import de.pki.minichess.game.MoveService;
import de.pki.minichess.game.Square;
import de.pki.minichess.game.State;
import de.pki.minichess.game.utils.PieceUtil;

/**
 * Implementation of Random AI Player
 */
public class Team3Player implements IPlayer {

  enum Pieces {
    Pawn(100), Knight(300), Bishop(300), Rook(500), Queen(900), King(99999);

    private int value;

    Pieces(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static int getValue(char c) {
      String s = ("" + c).toUpperCase();
      switch (s) {
      case "P":
        return Pawn.getValue();
      case "N":
        return Knight.getValue();
      case "B":
        return Bishop.getValue();
      case "R":
        return Rook.getValue();
      case "Q":
        return Queen.getValue();
      case "K":
        return King.getValue();
      default:
        return 0;
      }
    }

  }

  private Color color;

  /**
   * Generates a player with given color
   *
   * @param color
   *          color of the player
   */
  public Team3Player(Color color) {
    this.color = color;
  }

  @Override
  public Move pickMove(char[][] board) {
    Vector<Square> currentPlayerPieces = scanPiecesForPlayer(board, this.color);
    Vector<Move> possibleMoves = new Vector<>();
    for (Square piece : currentPlayerPieces) {
      possibleMoves.addAll(MoveService.getPossibleMoves(piece.getX(), piece.getY(), board));
    }

    return getMostValuableMove(board, possibleMoves);
  }

  private Move getMostValuableMove(char[][] board, Vector<Move> possibleMoves) {
    State state = new State();
    int gain = Integer.MIN_VALUE;
    Move bestMove = null;
    for (Move move : possibleMoves) {
      state.setBoard(cloneBoard(board));
      // return the move if it mates the opponent.
      if (state.moveByMove(move)) return move;
      Color opponentColor = this.color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
      // Get all possible moves of opponent after our move.
      Vector<Square> oppositePlayerPieces = scanPiecesForPlayer(state.getBoard(), opponentColor);
      Vector<Move> possibleOpponentMoves = new Vector<>();
      for (Square piece : oppositePlayerPieces) {
        possibleOpponentMoves.addAll(MoveService.getPossibleMoves(piece.getX(), piece.getY(), state.getBoard()));
      }
      
      int opponentGain = Integer.MIN_VALUE;
      Move bestOpponentMove = null;
      char[][] boardBeforeOpponentMove = cloneBoard(state.getBoard());
      int opponentBoardValueBeforeOpponentMove = evaluateBoard(boardBeforeOpponentMove, opponentColor);
      for (Move opponentMove : possibleOpponentMoves) {
        state.setBoard(cloneBoard(boardBeforeOpponentMove));
        // Opponent move makes us mate, don't do it! :(
        if (state.moveByMove(opponentMove)) {
          bestOpponentMove = opponentMove;
          break;
        };
        int opponentBoardValueAfterOpponentMove = evaluateBoard(state.getBoard(), opponentColor);
        if ((opponentBoardValueAfterOpponentMove - opponentBoardValueBeforeOpponentMove) > opponentGain) {
          opponentGain = opponentBoardValueAfterOpponentMove - opponentBoardValueBeforeOpponentMove;
          bestOpponentMove = opponentMove;
        }
      }
      // Set board before any opponent move and do the best opponent move, afterwards evaluate the effect on the board
      // after doing our and the opponent move.
      state.setBoard(cloneBoard(boardBeforeOpponentMove));
      state.moveByMove(bestOpponentMove);
      if (getBoardValueDiff(state.getBoard(), color, opponentColor) > gain) {
        gain = getBoardValueDiff(state.getBoard(), color, opponentColor);
        bestMove = move;
      }

    }

    return bestMove;

  }
  
  private char[][] cloneBoard(char[][] board) {
    char[][] newBoard = new char[board.length][];
    
   
    for (int i = 0; i < board.length; i++) {
      newBoard[i] = board[i].clone();
    }
    
    return newBoard;
  }

  private int getBoardValueDiff(char[][] board, Color myColor, Color opponentColor) {
    return evaluateBoard(board, myColor) - evaluateBoard(board, opponentColor);
  }

  private int evaluateBoard(char[][] board, Color color) {
    int value = 0;
    for (char[] line : board) {
      for (char c : line) {
        if (PieceUtil.getColorForPiece(c).equals(color)) {
          value += Pieces.getValue(c);
        }
      }
    }
    return value;
  }

  private Vector<Square> scanPiecesForPlayer(char[][] board, Color color) {
    Vector<Square> ownedPieces = new Vector<>();
    for (int row = 0; row < 6; row++) {
      for (int column = 0; column < 5; column++) {
        if (PieceUtil.getColorForPiece(board[row][column]) == color) {
          ownedPieces.add(new Square(column, row));
        }
      }
    }
    return ownedPieces;
  }
}
