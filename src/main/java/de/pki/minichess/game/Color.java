package de.pki.minichess.game;

public enum Color {
    WHITE('W'), BLACK('B'), EMPTY('e');

    private final char colorCode;

    Color(char colorCode) {
        this.colorCode = colorCode;
    }

    public char getColorCode() {
        return colorCode;
    }
    
    public static Color getColor(char c) {
      if ((""+c).toUpperCase().equals(""+WHITE.getColorCode())) return Color.WHITE;
      if ((""+c).toUpperCase().equals(""+BLACK.getColorCode())) return Color.BLACK; 
      return Color.EMPTY;
    }
}
