package stonesgame.game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.pool.TypePool;

import javax.naming.ldap.InitialLdapContext;

/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class StonesGameState {

    /**
     * Character defining empty space on the board.
     */
    private static final char EMPTY = '_';

    /**
     * The array defining the initial game board setup.
     */
    private static final char[][] INITIAL = new char[][]{
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
    };

    /**
     * The array to be instantiated for the actual board of the game.
     */
    private char[][] gameBoard;

    /**
     * The count of pieces on the board.
     */
    private int numOfMarks;

    /**
     * The current player.
     */
    private Player currentPlayer;

    private int playerOneMoves;

    private int playerTwoMoves;

    /**
     * The state of the game: ongoing or over.
     */
    private boolean gameOver;

    /**
     * Winner of the game.
     */
    private Player winner;

    /**
     * Single argument constructor of the game state.
     * By default, it is called with an empty board.
     *
     */
    public StonesGameState() {
        setGameBoard(INITIAL);
        setCurrentPlayer(Player.PLAYER_REDSTONE);
        setGameOver(false);
        setNumOfMarks(0);
        setWinner(null);
        setPlayerTwoMoves(0);
        setPlayerOneMoves(0);
    }

    /**
     *
     */
    public void init(){
        for(int x = 0; x < gameBoard.length; x++){
            for(int y = 0; y < gameBoard[x].length; y++) {
                if(gameBoard[x][y] != EMPTY) {
                    gameBoard[x][y] = EMPTY;
                }
            }
        }
    }

    /**
     * Function to place a piece on the board wherever the {@code currentPlayer}
     * desires, as long as the position is valid.
     *
     * @param row x-value of the coordinate of the desired piece location
     * @param col y-value of the coordinate of the desired piece location
     */
    public void move(int row, int col) {
        if (!isInBounds(row, col)){
            throw new IllegalMoveException("You cannot play this space! It is out of bounds!");
        }
        if(!isValidMove(row, col)) {
            throw new IllegalMoveException("You cannot play this space, it is taken!");
        }

        gameBoard[row][col] = getCurrentPlayer().getSymbol();
        log.info("Player {} placed a piece at ({},{})", StonesGame.getName(this), row, col);
        incrementCurrentPlayerMoves();
        numOfMarks++;
        calcIsGameOver(row, col);
        currentPlayer = currentPlayer.opponent();
    }

    public void incrementCurrentPlayerMoves() {
        switch(currentPlayer) {
            case PLAYER_REDSTONE: setPlayerOneMoves(getPlayerOneMoves() + 1); break;
            case PLAYER_BLUESTONE: setPlayerTwoMoves(getPlayerTwoMoves() + 1); break;
        }
    }

    /**
     * Calculates if the game is over by checking the total number of pieces on the board against the maximum,
     * and by checking to see if the {@code currentPlayer} has gotten a winning streak.
     *
     * @param row x-value of the coordinate of the desired piece location
     * @param col y-value of the coordinate of the desired piece location
     */
    public void calcIsGameOver(int row, int col) {
        if(numOfMarks == 25) {
            setWinner(Player.PLAYER_TIE);
            setGameOver(true);
        }
        else if(checkForStreak(row, col)) {
            setWinner(currentPlayer);
            setGameOver(true);
        }
        else {
            setGameOver(false);
        }
    }

    /**
     * Calls the winning-case checker methods.
     *
     * @param row x-value of the coordinate of the desired piece location
     * @param col y-value of the coordinate of the desired piece location
     * @return true if the {@code currentPlayer} has a winning streak, false if not
     */
    private boolean checkForStreak(int row, int col){
        return (checkDiagonalWin() || checkHorizontalWin(row) || checkVerticalWin(col));
    }

    /**
     * Checks the row of the most recently placed piece by the {@code currentPlayer}
     * for a winning streak.
     *
     * @param row x-value of the coordinate of the placed piece
     * @return true if the {@code currentPlayer} has a winning streak, false if not
     */
    private boolean checkHorizontalWin(int row) {
        StringBuilder builder = new StringBuilder();
        for(int x = 0; x < gameBoard[row].length; x++){
            builder.append(gameBoard[row][x]);
        }
        if(builder.toString().chars().filter(i -> i == getCurrentPlayer().getSymbol()).count() >= 3) {
            return true;
        }

        return false;
    }

    /**
     * Checks the column of the most recently placed piece by the {@code currentPlayer}
     * for a winning streak.
     *
     * @param col y-value of the coordinate of the placed piece
     * @return true if the {@code currentPlayer} has a winning streak, false if not
     */
    private boolean checkVerticalWin(int col) {
        StringBuilder builder = new StringBuilder();
        for(int x = 0; x < gameBoard.length; x++){
            builder.append(gameBoard[x][col]);
        }
        if(builder.toString().chars().filter(i -> i == getCurrentPlayer().getSymbol()).count() >= 3) {
            return true;
        }

        return false;
    }

    /**
     * Checks the whole board and all of its possible diagonals for a winning streak.
     *
     * @return true if the {@code currentPlayer} has a winning streak, false if not
     */
    private boolean checkDiagonalWin() {
        StringBuilder builder = new StringBuilder();
        //top-left middle
        for(int x = 0; x < gameBoard.length; x++){
            builder.append(gameBoard[x][x]);
        }
        builder.append(" ");
        //top-right
        for(int x = 0; x < gameBoard.length-1; x++){
            builder.append(gameBoard[x][x+1]);
        }
        builder.append(" ");
        for(int x = 0; x < gameBoard.length-2; x++){
            builder.append(gameBoard[x][x+2]);
        }
        builder.append(" ");
        //top-left
        for(int y = 0; y < gameBoard.length-1; y++){
            builder.append(gameBoard[y+1][y]);
        }
        builder.append(" ");
        for(int y = 0; y < gameBoard.length-2; y++){
            builder.append(gameBoard[y+2][y]);
        }
        builder.append(" ");
        //bottom-left middle
        for(int x = gameBoard.length-1; x >= 0; x--){
            builder.append(gameBoard[x][gameBoard.length-1-x]);
        }
        builder.append(" ");
        //bottom-left left
        for(int x = gameBoard.length-2; x >= 0; x--){
            builder.append(gameBoard[x][gameBoard.length-2-x]);
        }
        builder.append(" ");
        for(int x = gameBoard.length-3; x >= 0; x--){
            builder.append(gameBoard[x][gameBoard.length-3-x]);
        }
        builder.append(" ");
        //bottom-left right
        for(int x = gameBoard.length-1; x >= 1; x--) {
            for(int y = 1; y < gameBoard.length-1; y++)
                builder.append(gameBoard[x][y]);
        }
        builder.append(" ");
        for(int x = gameBoard.length-1; x >= 1; x--) {
            for(int y = 1; y < gameBoard.length-1; y++)
                builder.append(gameBoard[x][y]);
        }

        String[] diagonals = builder.toString().split(" ");
        for(String testMe : diagonals){
            if(testMe.chars().filter(i -> i == getCurrentPlayer().getSymbol()).count() >= 3) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if the desired move location is {@code EMPTY}
     *
     * @param row x-value of the coordinate of the desired piece location
     * @param col y-value of the coordinate of the desired piece location
     * @return true if is {@code EMPTY}, false if not
     */
    public boolean isValidMove(int row, int col){
        return gameBoard[row][col] == EMPTY;
    }

    /**
     * Checks to see if the desired move location is in-bounds
     *
     * @param row x-value of the coordinate of the desired piece location
     * @param col y-value of the coordinate of the desired piece location
     * @return true if it is, false if it is not
     */
    public boolean isInBounds(int row, int col){
       log.info("{}",row >= 0 && row < 5 && col >= 0 && col < 5);
        return row >= 0 && row < 5 && col >= 0 && col < 5;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                builder.append(gameBoard[row][col]).append(' ');
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}
