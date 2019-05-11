package stonesgame.game.state;

import java.util.Scanner;

public class MoveReader {

    private Scanner scanner = new Scanner(System.in);

    public MoveReader() {

    }

    public Cell readMove(StonesGameState state) {
        if(state.isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }
        String line = null;
        try {
            if(!scanner.hasNextLine())
                return null;
            String[] tokens = scanner.nextLine().trim().split("\\s+");
            if(tokens.length != 2) {
                throw new IllegalMoveException("Too many coordinate values!");
            }

            int row = Integer.parseInt(tokens[0]);
            int col = Integer.parseInt(tokens[1]);
            if(!state.isValidMove(row, col)) {
                throw new IllegalMoveException("This move is invalid. Please choose again.");
            }

            return new Cell(row, col);
        } catch(NumberFormatException e) {
            throw new IllegalMoveException("Invalid input");
        }
    }

}
