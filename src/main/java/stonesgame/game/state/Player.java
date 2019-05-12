package stonesgame.game.state;

import  lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Player class to provide player-change functionality to the game
 * and to store some information about the players like their name and step-count.
 */
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

    Player(char symbol) {
        this.symbol = symbol;
        steps = 0;
    }

    /**
     * This method is called to switch between players at the end of every turn.
     *
     * @return {@code currentPlayer}
     */
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
