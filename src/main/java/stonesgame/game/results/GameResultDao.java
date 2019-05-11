package stonesgame.game.results;

import stonesgame.game.util.jpa.GenericJpaDao;

import javax.transaction.Transactional;
import java.util.List;

/**
 * DAI ckass for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {

    public GameResultDao() {
        super(GameResult.class);
    }

    /**
     * Returns the list of {@code n} best results with respect to the time
     * spent for solving the puzzle.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the time
     * spent solving the puzzle
     */
    @Transactional
    public List<GameResult> findBest(int n) {
       return entityManager.createQuery("SELECT r FROM GameResult r WHERE r.solved = true ORDER BY r.duration ASC, r.created DESC", GameResult.class)
                .setMaxResults(n)
                .getResultList();
    }
}
