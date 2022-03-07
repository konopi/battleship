package konopi.battleship.logic;

import java.util.HashMap;

/**
 * The OceanGrid class is created specifying its length (y) and width (x). It provides the means
 * to add a ship to the grid, fire a shot on the specified coordinates and get a map of hits.
 */
public class OceanGrid {
    /**
     * Record returned as result of {@link #shoot(Coordinates) shoot}.
     */
    public record ShotResult(String shipName, Ship.HitDesignation hitDesignation) {}

    /**
     * Amount of active ships on the grid. Increases in {@link #addShip(Ship) addShip}.
     * Decreases in {@link #shoot(Coordinates) shoot}.
     */
    private int activeShipAmount = 0;

    /**
     * Coordinates are mapped to a ship which occupies them. Populated by calling {@link #addShip(Ship) addShip}.
     */
    private final HashMap<Coordinates, Ship> shipMap = new HashMap<>();
    /**
     * Coordinates are mapped to the {@link Ship.HitDesignation} determined in {@link #shoot(Coordinates) shoot}.
     */
    private final HashMap<Coordinates, Ship.HitDesignation> hitMap = new HashMap<>();

    /**
     * Size of the grid on the number coordinates.
     */
    private final int sizeX;
    /**
     * Size of the grid on the letter coordinates.
     */
    private final int sizeY;

    /**
     * Main constructor.
     * @param sizeX {@link #sizeX}.
     * @param sizeY {@link #sizeY}.
     */
    public OceanGrid(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    /**
     * Checks if the specified coordinates exceed grid limits.
     * @param coordinates Target square.
     * @return {@code true} if the square is not part of the grid.
     */
    private boolean isOutOfBounds(Coordinates coordinates) {
        return coordinates.getLetterCoordinate() >= sizeY + 'A' || coordinates.getNumberCoordinate() >= sizeX + 1;
    }

    /**
     * Checks if a ship exists on the specified coordinates.
     * @param coordinates Target square.
     * @return {@code true} if the square is taken by a ship.
     */
    private boolean isOccupied(Coordinates coordinates) {
        return shipMap.containsKey(coordinates);
    }

    /**
     * If possible adds the specified ship to the grid and returns {@code true}.
     * If the ship failed to be added returns {@code false}.
     * @param ship The ship which is to be added to the grid.
     * @return {@code true} if the ship was added successfully. {@code false} if the ship coordinates
     * are out of bounds or already occupied by another ship.
     */
    public boolean addShip(Ship ship) {
        /* Checks if the ship fits in the grid. */
        for (Coordinates coordinates : ship.getActiveSquares()) {
            if (isOutOfBounds(coordinates) || isOccupied(coordinates)) return false;
        }
        ship.getActiveSquares().forEach(coordinates -> shipMap.put(coordinates, ship));
        ++activeShipAmount;
        return true;
    }

    /**
     * Executes an action of firing on the specified square.
     * Checks for a ship in the given coordinates and updates the grid state accordingly. Returns the results.
     * @param coordinates Target square.
     * @return {@link ShotResult} consisting of {@link Ship.HitDesignation} and name of the ship if hit.
     * Returns {@code null} if specified coordinates are out of bounds.
     */
    public ShotResult shoot(Coordinates coordinates) {
        if (isOutOfBounds(coordinates)) return null;

        Ship targetShip = shipMap.get(coordinates);

        if (targetShip == null) {
            /* It's a miss. */
            hitMap.put(coordinates, Ship.HitDesignation.MISS);
            return new ShotResult(null, Ship.HitDesignation.MISS);
        }

        /* It's a hit. */
        Ship.HitDesignation hit = targetShip.hit(coordinates);
        hitMap.put(coordinates, hit);
        if (hit == Ship.HitDesignation.SINK) {
            --activeShipAmount;
        }
        return new ShotResult(targetShip.getName(), hit);
    }

    public int getActiveShipAmount() {
        return activeShipAmount;
    }

    /**
     * Gets the history of all taken shots with valid coordinates.
     * @return A map of coordinates given in a {@link #shoot(Coordinates) shoot} method to the hit result.
     */
    public HashMap<Coordinates, Ship.HitDesignation> getHitMap() {
        return hitMap;
    }
}
