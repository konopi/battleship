package konopi.battleship.logic;

import java.util.HashSet;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * The Ship class provides means to create and manage a single ship entity.
 * It keeps track of the afloat squares occupied by it and its name.
 */
public class Ship {
    public enum HitDesignation {
        HIT,
        MISS,
        SINK
    }
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * Set containing all ship squares that are afloat.
     */
    private final HashSet<Coordinates> activeSquares = new HashSet<>();
    /**
     * Ship designation, e.g. "Carrier", "Battleship", "Destroyer", etc.
     */
    private final String name;

    /**
     * Main constructor. The ship is generated starting from the {@code sternSquare}, positioned by
     * {@code orientation} towards increasing values.
     * @param sternSquare Starting coordinates for ship squares generation. The stern of the ship.
     * @param size Ship length. Amount of ship squares.
     * @param orientation The designation for the direction of the ship.
     * @param name Ship identifier, e.g. "Cruiser" or "Destroyer".
     * @throws IllegalArgumentException Specified size is less than 1.
     */
    public Ship(Coordinates sternSquare, int size, Orientation orientation, String name) {
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
                .forEach(activeSquares::add);

        this.name = name;
    }

    /**
     * Registers the hit on the target square. Returns the outcome.
     * @param coordinates Target square.
     * @return A {@link HitDesignation} value according to the outcome.
     */
    public HitDesignation hit(Coordinates coordinates) {
        boolean hit = activeSquares.remove(coordinates);
        if (hit) {
            return isSunk() ? HitDesignation.SINK : HitDesignation.HIT;
        } else return HitDesignation.MISS;
    }

    /**
     * Checks if the ship is sunk.
     * @return {@code true} if the ship has no squares afloat.
     */
    public boolean isSunk() {
        return activeSquares.isEmpty();
    }

    /**
     * Gets a set of ship squares which are afloat.
     * @return {@code HashSet} containing active ship coordinates.
     */
    public HashSet<Coordinates> getActiveSquares() {
        return activeSquares;
    }

    /**
     * Gets the ship identifier.
     * @return Ship identifier, e.g. "Cruiser" or "Destroyer".
     */
    public String getName() {
        return name;
    }
}
