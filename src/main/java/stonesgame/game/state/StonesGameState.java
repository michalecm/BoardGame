package stonesgame.game.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class StonesGameState {

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

    public StonesGameState() {
        this(INITIAL);
    }

    public StonesGameState(char[][] gameBoard) {
        if (!isValidBoard(gameBoard)) {
            throw new IllegalArgumentException("Board is invalid!");
        }

        setGameBoard(gameBoard);
        setNumOfMarks(0);
        setCurrentPlayer(Player.PLAYER_REDSTONE);
        setGameOver(false);
        setWinner(null);
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
        if (!(isValidMove(row, col) && isInBounds(row, col))) {
            throw new IllegalMoveException("You cannot play this space!");
        }

        gameBoard[row][col] = getCurrentPlayer().getSymbol();
        log.info("Player {} placed a piece at ({},{})", currentPlayer, row, col);
        numOfMarks++;
        currentPlayer.setSteps(currentPlayer.getSteps()+1);
        calcIsGameOver(row, col);
        currentPlayer = currentPlayer.opponent();
    }

    public void calcIsGameOver(int row, int col) {
        if(numOfMarks == 25) {
            setGameOver(true);
            setWinner(null);
        }
        else if(checkForStreak(row, col)) {
            setGameOver(true);
            setWinner(currentPlayer);
        }
        else {
            setGameOver(false);
        }
    }

    private boolean checkForStreak(int row, int col){
        return (checkDiagonalWin() || checkHorizontalWin(row) || checkVerticalWin(col));
    }

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

    public boolean isValidMove(int row, int col){
        return getGameBoard()[row][col] == EMPTY;
    }

    public boolean isInBounds(int row, int col){
       return row >= 0 && row < 5 && col >= 0 && col < 5;
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
