package stonesgame.game.state;

import  lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
public enum Player {

    PLAYER_REDSTONE('R'),
    PLAYER_BLUESTONE('B'),
    TIE('T');

    private char symbol;
    @Setter
    @NotNull
    private String name;

    @Setter
    private int steps;

    private Player(char symbol) {
        this.symbol = symbol;
        steps = 0;
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
