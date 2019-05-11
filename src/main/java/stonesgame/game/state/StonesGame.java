package stonesgame.game.state;

public class StonesGame {

    public static void main(String[] args) {
        StonesGameState state = new StonesGameState();
        System.out.println(state);
        MoveReader reader = new MoveReader();
        while (!state.isGameOver()) {
            Cell cellToMove = null;
            do {
                try {
                    System.out.printf("%s's move: ", state.getCurrentPlayer().getSymbol());
                    System.out.flush();
                    if ((cellToMove = reader.readMove(state)) == null) {
                        System.out.println("Game terminated");
                        return;
                    }
                } catch(IllegalMoveException e) {
                    System.err.println(e.getMessage());
                }
            } while (cellToMove == null);
            System.out.println(cellToMove);
            state.move(cellToMove.getRow(), cellToMove.getCol());
            System.out.println(state);
        }
        if (state.getWinner() != null)
            System.out.printf("%s won%n", state.getWinner().getSymbol());
        else
            System.out.println("Draw");
    }

}
