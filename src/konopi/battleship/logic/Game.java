package konopi.battleship.logic;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * The Game class is used to handle all the game logic. Each {@link #tick() tick} call is a next game step.
 */
public class Game {
    /**
     * Game state.
     */
    private boolean running = true;

    /**
     * Size of the grid on the number axis.
     */
    public final int GRID_X = 10;
    /**
     * Size of the grid on the letter axis.
     */
    public final int GRID_Y = 10;

    /**
     * Association of the ship name to its size.
     */
    private final HashMap<String, Integer> shipNameSizeMap = new HashMap<>();
    /**
     * Association of the ship name to amount of ships on the grid.
     */
    private final HashMap<String, Integer> shipNameAmountMap = new HashMap<>();

    /**
     * Enemy ocean grid.
     */
    private final OceanGrid oceanGrid = new OceanGrid(GRID_X, GRID_Y);

    /**
     * Coordinates which will be fired at in the next {@link #tick() tick}.
     */
    private String targetCoordinates = "A1";
    /**
     * Result of the shot in the last {@link #tick() tick}.
     */
    private OceanGrid.ShotResult shotResult;

    private final Random random = new Random();

    /**
     * Main constructor. It's not actually worth writing documentation for it,
     * but it wanted to be like the other cool constructors. Calls {@link #initialise() initialise}.
     */
    public Game() {
        initialise();
    }

    /**
     * Sets the field according to {@link #shipNameSizeMap} and {@link #shipNameAmountMap} maps, which are also
     * here for now, since the amount and type of ships is constant.
     */
    private void initialise() {
        /* setting the size for the ships */
        shipNameSizeMap.put("Battleship", 5);
        shipNameSizeMap.put("Destroyer", 4);

        /* setting the ship amount of each type */
        shipNameAmountMap.put("Battleship", 1);
        shipNameAmountMap.put("Destroyer", 2);

        /* placing the ships */
        shipNameAmountMap.forEach(this::placeShips);
    }

    /**
     * Checks the state of the game.
     * @return {@code true} if the game is in progress. Otherwise {@code false}.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Updates the game in relation to the current {@link #targetCoordinates}.
     */
    public void tick() {
        if (!running) return;

        shotResult = oceanGrid.shoot(new Coordinates(targetCoordinates));

        if (oceanGrid.getActiveShipAmount() == 0) {
            running = false;
        }
    }

    public OceanGrid getOceanGrid() {
        return oceanGrid;
    }

    public void setTargetCoordinates(String targetCoordinates) {
        this.targetCoordinates = targetCoordinates;
    }

    public OceanGrid.ShotResult getShotResult() {
        return shotResult;
    }

    /**
     * Calls {@link #placeShip(String) placeShip} {@code amount} times with {@code shipName} as the argument.
     * @param shipName The name of the ships to place.
     * @param amount The amount of the ships to place.
     */
    private void placeShips(String shipName, int amount) {
        IntStream.range(0, amount).forEach(i -> placeShip(shipName));
    }

    /**
     * Adds the ship to the grid in a random spot.
     * @param shipName The name of the ship to place.
     */
    private void placeShip(String shipName) {
        boolean placed = false;
        /* simplest solution for now, can potentially become a problem if free space is extremely limited */
        while (!placed) {
            placed = oceanGrid.addShip(new Ship(getRandomCoordinates(GRID_Y, GRID_X),
                    shipNameSizeMap.get(shipName),
                    Ship.Orientation.values()[random.nextInt(2)], // random orientation
                    shipName));
        }
    }

    /**
     * Gets random coordinates in the specified range.
     * @param letterRange Range for the Y axis.
     * @param numberRange Range for the X axis.
     * @return Randomised coordinates.
     */
    private Coordinates getRandomCoordinates(int letterRange, int numberRange) {
        char randomLetter = (char)( 'A' + random.nextInt(letterRange) );
        int randomNumber = random.nextInt(1, numberRange);

        return new Coordinates(randomLetter + String.valueOf(randomNumber));
    }
}
