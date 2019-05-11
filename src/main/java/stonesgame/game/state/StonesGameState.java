package stonesgame.game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class StonesGameState implements Cloneable {

    private static final char EMPTY = '_';
    private static final char[][] INITIAL = new char[][]{
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
    };

    private char[][] gameBoard;

    private int numOfMarks;

    private Player currentPlayer;

    private boolean gameOver;

    private Player winner;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private int piecesInRow;


    public StonesGameState() {
        this(INITIAL);
    }

    public StonesGameState(char[][] gameBoard) {
        if (!isValidBoard(gameBoard)) {
            throw new IllegalArgumentException("Board is invalid!");
        }

        setGameBoard(gameBoard);
    }


    public boolean isValidBoard(char[][] gameBoard)
    {
        if(gameBoard == null || gameBoard.length != 5) {
            return false;
        }
        for(int x = 0; x < gameBoard.length; x++) {
            if(gameBoard[x] == null || gameBoard[x].length != 5) {
                return false;
            }

            for(int y = 0; y < gameBoard[x].length; y++) {
                if(gameBoard[x][y] != EMPTY) {
                    if(!(gameBoard[x][y] == Player.PLAYER_REDSTONE.getSymbol() || gameBoard[x][y] == Player.PLAYER_BLUESTONE.getSymbol())) {
                        return false;
                    }
                    else {
                        return isGameOver();
                    }
                }
            }
        }

        return true;
    }

    public void move(int row, int col) {
        if (!isValidMove(row, col)) {
            throw new IllegalMoveException("You cannot play this space!");
        }

        gameBoard[row][col] = currentPlayer.getSymbol();
        log.info("Player {} placed a piece at ({},{})", currentPlayer, row, col);
        ++numOfMarks;
        calcIsGameOver(row, col);
        currentPlayer = currentPlayer.opponent();
    }

    public void calcIsGameOver(int row, int col) {
        if(numOfMarks == 25) {
            setGameOver(true);
        }
        if (checkForStreak(row, col)) {
            setGameOver(true);
            setWinner(currentPlayer);
        }
        setGameOver(false);
    }

    private boolean checkForStreak ( int row, int col){
        if (!isValid(row, col)) {
            return false;
        }

        setPiecesInRow(getPiecesInRow() + 1);

        if (getPiecesInRow() >= 3) {
            return true;
        }

        checkForStreak(row--, col);
        checkForStreak(row++, col);
        checkForStreak(row, col--);
        checkForStreak(row, col++);

        setPiecesInRow(0);
        return false;

    }

    public boolean isValidMove ( int row, int col){
        return row >= 0 && row < 5 && col >= 0 && col < 5 && gameBoard[row][col] == EMPTY;
    }

    public boolean isValid ( int row, int col){
        return row >= 0 && row < 5 && col >= 0 && col < 5 && gameBoard[row][col] == currentPlayer.getSymbol();
    }

    public StonesGameState clone() {
        StonesGameState copy = null;
        try {
            copy = (StonesGameState) super.clone();
        } catch (CloneNotSupportedException e) {
            for(int row = 0; row < 5; row++) {
                copy.gameBoard[row] = gameBoard[row].clone();
            }
        }

        return copy;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                builder.append(gameBoard[row][col]).append(' ');
            }

            builder.append('\n');
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(new StonesGameState());
    }
}
