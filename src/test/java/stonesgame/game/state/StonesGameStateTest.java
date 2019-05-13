package stonesgame.game.state;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class StonesGameStateTest {

    StonesGameState tester;

    @Test
    void testMove(){
        tester = new StonesGameState();
        tester.move(0, 0);
        assertThrows(IllegalMoveException.class, () -> tester.move(0, 0));
        assertThrows(IllegalMoveException.class, () -> tester.move(100, 100));
        assertThrows(IllegalMoveException.class, () -> tester.move(-1, -2));
        tester.init();
    }

    @Test
    void testCalcIsGameOver() {
        tester = new StonesGameState();
        tester.setNumOfMarks(24);
        tester.move(0, 0);
        assertTrue(tester.isGameOver());
        tester.init();
        tester.move(0, 0);
        tester.move(4, 1);
        tester.move(1, 1);
        tester.move(4, 2);
        tester.move(4, 4);
        assertTrue(tester.isGameOver());
        tester.init();
    }

    @Test
    void testIsValidMove(){
        tester = new StonesGameState();
        assertTrue(tester.isValidMove(0,0));
        tester.move(0, 0);
        tester.move(1, 1);
        assertTrue(tester.isValidMove(0, 1));
        assertTrue(tester.isValidMove(3, 3));
        assertFalse(tester.isValidMove(0, 0));
        tester.move(3, 3);
        assertFalse(tester.isValidMove(3, 3));
        assertFalse(tester.isValidMove(1, 1));
        tester.init();
    }

    @Test
    void testIsInBounds() {
        tester = new StonesGameState();
        assertTrue(tester.isInBounds(0, 0));
        assertTrue(tester.isInBounds(4, 4));
        assertFalse(tester.isInBounds(-4, -4));
        assertFalse(tester.isInBounds(100, 100));
        tester.init();
    }

}