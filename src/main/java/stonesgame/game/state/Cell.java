package stonesgame.game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Cell {

    public static final char EMPTY = '_';
    private int row;
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
