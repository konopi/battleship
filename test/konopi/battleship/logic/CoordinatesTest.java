package konopi.battleship.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinatesTest {

    @Test
    void testEquals() {
        Coordinates c1 = new Coordinates("A5");
        Coordinates c2 = new Coordinates("A5");

        assertEquals(c1, c2);
        assertEquals(c1, c1);

        assertNotEquals(new Coordinates("B5"), c1);
        assertNotEquals(null, c1);
    }

    @Test
    void testHashCode() {
        Coordinates c1 = new Coordinates("A5");
        Coordinates c2 = new Coordinates("A5");
        int hash1 = c1.hashCode();

        assertEquals(hash1, c1.hashCode());
        assertEquals(hash1, c2.hashCode());
    }

    @Test
    void shouldNotDistinctLetterCase() {
        Coordinates c1 = new Coordinates("a5");
        Coordinates c2 = new Coordinates("A5");

        assertEquals(c1, c2);
    }

    @Test
    void shouldNotDistinctHyphen() {
        Coordinates c1 = new Coordinates("A-5");
        Coordinates c2 = new Coordinates("A5");

        assertEquals(c1, c2);
    }

    @Test
    void shouldNotAcceptValuesLargerThanIntegerAsNumber() {
        String expected = "Coordinate number must be an integer: 99999999999999999999999";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Coordinates("A99999999999999999999999")
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldNotAcceptZeroAsNumber() {
        String expected = "Coordinate number must be > 0: 0";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Coordinates("A0")
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldNotAcceptFloatsAsNumber() {
        String expected = "Coordinate number must be an integer: 4.52";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Coordinates("A4.52")
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldNotAcceptStringsAsNumber() {
        String expected = "Coordinate number must be an integer: Bcd";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Coordinates("ABcd")
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldNotAcceptNonLettersAsLetter() {
        String expected = "Coordinate letter must be a latin character: $";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new Coordinates("$5")
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldGetOffset() {
        Coordinates expected = new Coordinates("D6");
        Coordinates c = new Coordinates("A2");

        assertEquals(expected, c.getOffset(3, 4));
    }

    @Test
    void shouldNotReturnOutOfBoundsOffset() {
        String expected = "Coordinates out of bounds";
        Coordinates c = new Coordinates("A2");

        Exception exception = assertThrows(
                Exception.class, () -> c.getOffset(-3, -4)
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void shouldGetNorthernNeighbour() {
        Coordinates expected = new Coordinates("A5");
        Coordinates c = new Coordinates("B5");

        assertEquals(expected, c.getOffset(-1, 0));
    }

    @Test
    void shouldGetSouthernNeighbour() {
        Coordinates expected = new Coordinates("E6");
        Coordinates c = new Coordinates("D6");

        assertEquals(expected, c.getOffset(1, 0));
    }

    @Test
    void shouldGetWesternNeighbour() {
        Coordinates expected = new Coordinates("B2");
        Coordinates c = new Coordinates("B3");

        assertEquals(expected, c.getOffset(0, -1));
    }

    @Test
    void shouldGetEasternNeighbour() {
        Coordinates expected = new Coordinates("G5");
        Coordinates c = new Coordinates("G4");

        assertEquals(expected, c.getOffset(0, 1));
    }

    @Test
    void testToString() {
        Coordinates c1 = new Coordinates("A5");
        Coordinates c2 = new Coordinates("K47");

        assertEquals("A5", c1.toString());
        assertEquals("K47", c2.toString());
    }
}
