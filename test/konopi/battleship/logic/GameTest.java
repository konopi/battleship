package konopi.battleship.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    /* I'm not really sure how to do this test properly without making 'random' a settable field. */
    @Test
    void testInitialise() {
        assertEquals(3, game.getOceanGrid().getActiveShipAmount());
        assertTrue(game.isRunning());
    }

    @Test
    void testTick() {
        game.setTargetCoordinates("D6");

        assertNull(game.getShotResult());

        game.tick();

        assertNotNull(game.getShotResult());
    }

    @Test
    void testIsRunning() {
        assertTrue(game.isRunning());

        /*  */
        char letter = 'A';
        while (game.getOceanGrid().getActiveShipAmount() > 0) {
            for (int i = 1; game.getOceanGrid().getActiveShipAmount() > 0 && i <= game.GRID_X; ++i) {
                game.setTargetCoordinates(letter + String.valueOf(i));
                game.tick();
            }
            ++letter;
        }

        assertFalse(game.isRunning());
    }
}
