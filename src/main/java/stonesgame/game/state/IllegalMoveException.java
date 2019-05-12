package stonesgame.game.state;

/**
 * Exception class to be thrown whenever a player attempts to make an invalid move.
 */
public class IllegalMoveException extends IllegalArgumentException {

    public IllegalMoveException(String reason){
        super(reason);
    }
}
