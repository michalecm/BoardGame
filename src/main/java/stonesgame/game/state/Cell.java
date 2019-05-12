package stonesgame.game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Class representing a single movable location on the game board.
 */
@Data
public class Cell {

    /**
     * Character denoting empty space on the board.
     */
    public static final char EMPTY = '_';

    /**
     * The x-variable of the location of the {@code Cell}
     */
    private int row;

    /**
     * The y-variable of the location of the {@code Cell}
     */
    private int col;

    public Cell() {
        setRow(0);
        setCol(0);
    }

    public Cell(int row, int col) {
        setRow(row);
        setCol(col);
    }

    public String toString() {
        return String.format("%d, %d", row, col);
    }

}
