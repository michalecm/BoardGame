package stonesgame.game.state;

import  lombok.Getter;

@Getter
public enum Player {

    PLAYER_REDSTONE('R'),
    PLAYER_BLUESTONE('B');

    private char symbol;

    private Player(char symbol) {
        this.symbol = symbol;
    }

    public Player opponent() {
        switch(this) {
            case PLAYER_REDSTONE:
                return PLAYER_BLUESTONE;
            case PLAYER_BLUESTONE:
                return PLAYER_REDSTONE;
        }

        throw new AssertionError("This does not exist.");
    }
}
