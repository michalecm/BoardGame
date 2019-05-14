package stonesgame.game.state;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * Controller class to allow the player to interact with the board by playing pieces.
 */
@Slf4j
public class MoveReader {

    private Scanner scanner = new Scanner(System.in);

    public MoveReader() {

    }

    /**
     * Receives an instance of the ongoing {@code StonesGameState} and checks
     * user input for validity against the state. The purpose of this method is to
     * alllow the user to choose a {@code Cell} to play into.
     *
     * @param state the current running instance of the {@code StonesGameState} class
     * @return {@code Cell} which the {@code currentPlayer} chose to move to
     */
    public Cell readMove(StonesGameState state) {

        if (state.isGameOver()) {
            log.error("Player {} tried to play, but the game is already over! Something went wrong :(.", StonesGame.getName(state));
            throw new IllegalStateException("Game is over!");
        }

        try {
            if(!scanner.hasNextLine()) {
                return null;
            }

            String[] tokens = scanner.nextLine().trim().split("\\s+");
            if(tokens.length != 2) {
                log.error("Player {} had too great or too few inputs for play coordinats.", StonesGame.getName(state));
                throw new IllegalArgumentException("You had either too few or too many arguments!");
            }

            int row = Integer.parseInt(tokens[0]);
            int col = Integer.parseInt(tokens[1]);
            if(!state.isInBounds(row, col)) {
                log.error("{}'s choice of move was out of bounds.", StonesGame.getName(state));
                throw new IllegalMoveException("Your choice of cell was out of bounds. Please try again!");
            }
            if(!state.isValidMove(row, col)) {
                log.error("{}'s choice of move was invalid because there is already a piece at the location.", StonesGame.getName(state));
                throw new IllegalMoveException("Your choice of cell was already taken by your opponent. Please try again!");
            }
            else {
                return new Cell(row, col);
            }

        } catch (Exception e) {
            if(e instanceof NumberFormatException) {
                log.error("Player {} tried to input non-integer values as coordinates.", StonesGame.getName(state));
                System.out.println("Please enter input that is of the type INTEGER.");
            }
            else {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }
}
