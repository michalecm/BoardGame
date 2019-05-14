package stonesgame.game.state;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import stonesgame.game.results.GameResult;
import stonesgame.game.results.GameResultDao;
import stonesgame.game.util.guice.PersistenceModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.GenericArrayType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main class of {@code StonesGame} where the {@code main} method runs.
 */
@Slf4j
public class StonesGame {
    static String playerOne;
    static String playerTwo;
    /**
     * {@code main} consists of player-name choosing, and sending and receiving information with the
     * controller class {@code MoveReader}. {@code main} also persists end-game data to the database.
     *
     * Occasionally, there will be runtime issues with the database. These can be resolved by following the instructions here
     * @see <a href="https://stackoverflow.com/questions/8158969/h2-database-error-database-may-be-already-in-use-locked-by-another-process">StackOverflow tutorial </a>
     * and deleting the instance of the database as well as the lock file.
     *
     * @param args arguments from the command line
     */
    public static void main(String[] args) {

        String tie = "Tie";
        StonesGameState state = new StonesGameState();
        Injector injector = Guice.createInjector(new PersistenceModule("game"));
        GameResultDao dao = injector.getInstance(GameResultDao.class);
        Scanner scanner = new Scanner(System.in);
        LocalDateTime begin = LocalDateTime.now();
        System.out.println("Welcome to STONESGAME! ~ ~ ~ ~\n");
        do {
            System.out.print("Player 1 of REDSTONE, please enter your name: ");
            playerOne = (scanner.nextLine());
        }while(playerOne.equals(null) || playerOne.length() < 1);
        log.info("Player {} entered the game with the name {}", Player.PLAYER_REDSTONE, playerOne);
        do {
            System.out.print("\nPlayer 2 of BLUESTONE, please enter your name: ");
            playerTwo = scanner.nextLine();
        }while(playerTwo.equals(null) || playerTwo.length() < 1);
        log.info("Player {} entered the game with the name {}", Player.PLAYER_BLUESTONE, playerTwo);

        System.out.flush();
        System.out.println("\n" + state);
        MoveReader reader = new MoveReader();
        GameResult game;
        while (!state.isGameOver()) {
            Cell cellToMove;

            do {
                System.out.printf("%s's move: ", getName(state));
                System.out.flush();
                cellToMove = reader.readMove(state);
            } while (cellToMove == null);
            state.move(cellToMove.getRow(), cellToMove.getCol());
            System.out.println(state);
            System.out.flush();
        }
        if (state.getWinner() != null) {
            log.info("{} won!", state.getWinner());
            System.out.printf("%s won%n", getName(state));
        }
        game = GameResult.builder()
                .player(getName(state))
                .duration(Duration.between(begin, LocalDateTime.now()))
                .solved(true)
                .turns(getTurns(state))
                .build();

        dao.persist(game);

        List<GameResult> toPrint = dao.findBest(5);
        for(GameResult gameResult : toPrint) {
            System.out.println(gameResult.toString());
        }

    }

    public static String getName(StonesGameState stonesGameState) {
        if(stonesGameState.isGameOver()){
            switch(stonesGameState.getWinner()) {
                case PLAYER_REDSTONE: return playerOne;
                case PLAYER_BLUESTONE: return playerTwo;
            }
        }

        else {
            switch (stonesGameState.getCurrentPlayer()) {
                case PLAYER_REDSTONE: return playerOne;
                case PLAYER_BLUESTONE: return playerTwo;
            }
        }

        return null;
    }

    public static int getTurns(StonesGameState stonesGameState) {
        switch (stonesGameState.getWinner()) {
            case PLAYER_REDSTONE:
                return stonesGameState.getPlayerOneMoves();
            case PLAYER_BLUESTONE:
                return stonesGameState.getPlayerTwoMoves();
                default: return 0;
        }
    }

}
