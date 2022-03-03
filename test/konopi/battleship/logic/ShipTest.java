package konopi.battleship.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    private Ship s;

    @BeforeEach
    void setUp() {
        s = new Ship(new Coordinates("D8"), 4, Ship.Orientation.HORIZONTAL, "Battleship");
    }

    @Test
    void testGetActiveSquares() {
        HashSet<Coordinates> expected = Stream.of("D8", "D9", "D10", "D11")
                .map(Coordinates::new)
                .collect(Collectors.toCollection(HashSet::new));

        assertEquals(expected, s.getActiveSquares());
    }

    @Test
    void testGetName() {
        String expected = "Battleship";

        assertEquals(expected, s.getName());
    }

    @Test
    void testHit() {
        /* a hit */
        assertEquals(Ship.HitDesignation.HIT, s.hit(new Coordinates("D10")));
        /* repeated shot should not be a hit */
        assertEquals(Ship.HitDesignation.MISS, s.hit(new Coordinates("D10")));

        s.hit(new Coordinates("D8"));
        s.hit(new Coordinates("D9"));
        /* if the hit square is the last one active it should be a sink */
        assertEquals(Ship.HitDesignation.SINK, s.hit(new Coordinates("D11")));

        assertEquals(Ship.HitDesignation.MISS, s.hit(new Coordinates("E10"))); // not a ship coordinate
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
                IllegalArgumentException.class,
                () -> new Ship(new Coordinates("A1"), 0, Ship.Orientation.VERTICAL, "Raft")
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldNotReturnOutOfBoundsShip() {
        String expected = "Coordinates out of bounds";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Ship(new Coordinates("Z1"), 3, Ship.Orientation.VERTICAL, "Cruiser")
        );
        assertEquals(expected, exception.getMessage());
    }
}
