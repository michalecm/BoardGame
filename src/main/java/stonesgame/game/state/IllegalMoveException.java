package stonesgame.game.state;

public class IllegalMoveException extends IllegalArgumentException {

    public IllegalMoveException(String reason){
        super(reason);
    }
}
