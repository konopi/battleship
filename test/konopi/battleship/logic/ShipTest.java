package konopi.battleship.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    private Ship s;

    @BeforeEach
    void setUp() {
        s = new Ship(new Coordinates("D8"), 4, Ship.Orientation.HORIZONTAL);
    }

    @Test
    void testHit() {
        assertTrue(s.hit(new Coordinates("D10"))); // first hit should return true
        assertFalse(s.hit(new Coordinates("D10"))); // second hit should return false

        assertFalse(s.hit(new Coordinates("E10"))); // not a ship coordinate
    }

    @Test
    void testIsSunk() {
        s.hit(new Coordinates("D8"));
        assertFalse(s.isSunk());

        s.hit(new Coordinates("D9"));
        assertFalse(s.isSunk());

        s.hit(new Coordinates("D10"));
        assertFalse(s.isSunk());

        s.hit(new Coordinates("D11"));
        assertTrue(s.isSunk()); // every square was hit => ship should be sunk
    }

    @Test
    void shouldNotAcceptSizeSmallerThanOne() {
        String expected = "Size should be >= 1: 0";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Ship(new Coordinates("A1"), 0, Ship.Orientation.VERTICAL)
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldNotReturnOutOfBoundsShip() {
        String expected = "Coordinates out of bounds";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Ship(new Coordinates("Z1"), 3, Ship.Orientation.VERTICAL)
        );
        assertEquals(expected, exception.getMessage());
    }
}
