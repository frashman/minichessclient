package de.pki.minichess.game;

import java.util.Objects;

/**
 * Move Class which holds the starting and destination position
 */
public class Move {
    private Square from;
    private Square to;

    /**
     * Generates new Move with given starting and destination position
     *
     * @param from starting position
     * @param to   destination position
     */
    Move(Square from, Square to) {
        this.from = from;
        this.to = to;
    }
    
    /**
     * Generates new Move from given move string.
     * 
     * @param moveString A move String (i.e. a1-b2) 
     */
    Move(String moveString) {
      String[] parts = moveString.split("-");
      this.from = new Square(parts[0]);
      this.to = new Square(parts[1]);
      
    }

    /**
     * Getter starting position
     *
     * @return
     */
    public Square getFrom() {
        return from;
    }

    /**
     * Getter destination position
     *
     * @return
     */
    public Square getTo() {
        return to;
    }

    /**
     * Returns starting and destination positions as string
     *
     * @return
     */
    public String toString() {
        return from.getX() + "," + from.getY() + "-" + to.getX() + "," + to.getY();
    }
    
    /**
     * Returns starting and destination positions as string
     *
     * @return
     */
    public String toTelnetString() {
      char a = 'a';
      char fromChar = (char)(a + from.getX());
      char toChar = ((char)(a + to.getX()));
        return  (""+fromChar) +   (6-from.getY()) + "-" + (""+toChar)  + (6-to.getY());
    }

    /**
     * Equals method to compare Moves
     *
     * @param o object to compare
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) &&
                Objects.equals(to, move.to);
    }

    /**
     * Generates hashcode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
