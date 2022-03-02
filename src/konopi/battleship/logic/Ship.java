package konopi.battleship.logic;

import java.util.HashSet;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * The Ship class provides means to create and manage a single ship entity.
 * It keeps track of the afloat squares occupied by it.
 */
public class Ship {
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * Set containing all ship squares that are afloat.
     */
    private final HashSet<Coordinates> activeShipSquares = new HashSet<>();

    /**
     * Main constructor. The ship is generated starting from the {@code sternSquare}, positioned by
     * {@code orientation} towards increasing values.
     * @param sternSquare Starting coordinates for ship squares generation. The stern of the ship.
     * @param size Ship length. Amount of ship squares.
     * @param orientation The designation for the direction of the ship.
     * @throws IllegalArgumentException A part of the specified ship found itself out of bounds.
     */
    public Ship(Coordinates sternSquare, int size, Orientation orientation) throws IllegalArgumentException {
        if (size < 1) {
            throw new IllegalArgumentException("Size should be >= 1: " + size);
        }

        /* Function which gets coordinates in the direction the ship is facing. */
        IntFunction<Coordinates> getOrientedOffset = switch (orientation) {
            case HORIZONTAL -> (i) -> sternSquare.getOffset(0, i);
            case VERTICAL -> (i) -> sternSquare.getOffset(i, 0);
        };

        IntStream.range(0, size)
                .mapToObj(getOrientedOffset)
                .forEach(activeShipSquares::add);
    }

    /**
     * Registers the hit on the target square. Returns the outcome.
     * @param coordinates Target square.
     * @return {@code true} if the hit was successful.
     */
    public boolean hit(Coordinates coordinates) {
        return activeShipSquares.remove(coordinates);
    }

    /**
     * Checks if the ship is sunk.
     * @return {@code true} if the ship has no squares afloat.
     */
    public boolean isSunk() {
        return activeShipSquares.isEmpty();
    }

    public HashSet<Coordinates> getActiveShipSquares() {
        return activeShipSquares;
    }
}
