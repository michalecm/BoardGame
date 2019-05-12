package stonesgame.game.state;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import stonesgame.game.results.GameResult;
import stonesgame.game.results.GameResultDao;
import stonesgame.game.util.guice.PersistenceModule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

@Slf4j
public class StonesGame {

    public static void main(String[] args) {
        StonesGameState state = new StonesGameState();
        Injector injector = Guice.createInjector(new PersistenceModule("game"));
        GameResultDao dao = injector.getInstance(GameResultDao.class);
        Scanner scanner = new Scanner(System.in);
        LocalDateTime begin = LocalDateTime.now();
        System.out.println("Welcome to STONESGAME! ~ ~ ~ ~\n");
        do {
            System.out.print("Player 1 of REDSTONE, please enter your name: ");
            Player.PLAYER_REDSTONE.setName(scanner.nextLine());
        }while(Player.PLAYER_REDSTONE.getName().equals(null) || Player.PLAYER_REDSTONE.getName().length() < 1);
        do {
            System.out.print("\nPlayer 2 of BLUESTONE, please enter your name: ");
            Player.PLAYER_BLUESTONE.setName(scanner.nextLine());
        }while(Player.PLAYER_REDSTONE.getName().equals(null) || Player.PLAYER_REDSTONE.getName().length() < 1);

        System.out.flush();
        System.out.println("\n" + state);
        MoveReader reader = new MoveReader();
        GameResult game = new GameResult();
        boolean askName = true;
        while (!state.isGameOver()) {
            Cell cellToMove = null;

            do {
                //try {
                    System.out.printf("%s's move: ", state.getCurrentPlayer().getName());
                    System.out.flush();
                    cellToMove = reader.readMove(state);
                //} catch(IllegalMoveException e) {
                    //System.err.println(e.getMessage());
                //}
            } while (cellToMove == null);
            state.move(cellToMove.getRow(), cellToMove.getCol());
            System.out.println(state);
            System.out.flush();
        }
        if (state.getWinner() != null)
            System.out.printf("%s won%n", state.getWinner().getName());
        else
            System.out.println("Draw");

        game = GameResult.builder()
                .solved(true)
                .player(state.getWinner().getName())
                .duration(Duration.between(begin, LocalDateTime.now()))
                .build();

        dao.persist(game);


    }

}
