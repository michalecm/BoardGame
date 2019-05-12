package stonesgame.game.state;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class MoveReader {

    private Scanner scanner = new Scanner(System.in);

    public MoveReader() {

    }

    public Cell readMove(StonesGameState state) {

        if (state.isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }

        try {
            if(!scanner.hasNextLine()) {
                return null;
            }

            String[] tokens = null;
            tokens = scanner.nextLine().trim().split("\\s+");
            if(tokens.length != 2) {
                throw new IllegalArgumentException("You had either too few or too many arguments!");
            }

            int row = Integer.parseInt(tokens[0]);
            int col = Integer.parseInt(tokens[1]);
            if(!state.isInBounds(row, col)) {
                throw new IllegalMoveException("Your choice of cell was out of bounds. Please try again!");
            }
            if(!state.isValidMove(row, col)) {
                throw new IllegalMoveException("Your choice of cell was already taken by your opponent. Please try again!");
            }
            else {
                return new Cell(row, col);
            }

        } catch (Exception e) {
            if(e instanceof NumberFormatException) {
                System.out.println("Please enter input that is of the type INTEGER.");
            }
            else {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }
}
