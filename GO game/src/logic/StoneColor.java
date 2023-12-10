package logic;

public enum StoneColor {
    BLACK("X"),
    WHITE("O");

    private String symbol;

    StoneColor(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static StoneColor getStoneColor(String color) {
        for (StoneColor stoneColor : values()) {
            if (stoneColor.name().equalsIgnoreCase(color)) {
                return stoneColor;
            }
        }
        return null;
    }
}

