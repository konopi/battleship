package konopi.battleship.ui;

import konopi.battleship.logic.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * The ConsoleUI is a UI prototype developed in order to perform system testing.
 */
public class ConsoleUI implements UI {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reference to the game instance.
     */
    private Game game;

    private final String WELCOME_MSG = "Welcome to Battleship (but less)!";
    private final String PROMPT_MSG = "Type in target coordinates:";
    private final String GAME_END_MSG = "It's over.";

    /**
     * Length of a single square in text.
     */
    private final int SYMBOL_LENGTH = 3;
    private final String UNKNOWN = "[?]";
    private final String HIT = "[H]";
    private final String MISS = "[M]";
    private final String SINK = "[S]";

    /**
     * The {@code StringBuilder} in which the UI assembles a printable target grid.
     */
    private StringBuilder targetGrid;

    /**
     * Sets the game reference and draws the entry screen.
     * @param game {@link #game}.
     */
    @Override
    public void initialise(Game game) {
        this.game = game;
        System.out.println(WELCOME_MSG);
        drawTargetGrid();
        System.out.println(PROMPT_MSG);
    }

    /**
     * Updates the console with the current game state.
     */
    @Override
    public void update() {
        drawTargetGrid();
        OceanGrid.ShotResult result = game.getShotResult();

        if (result != null) switch (result.hitDesignation()) {
            case MISS -> System.out.println("Miss.\n");
            case HIT -> System.out.println("Hit! " + result.shipName() + ".\n");
            case SINK -> System.out.println("Sink! " + result.shipName() + ".\n");
        } else System.out.println("Out of bounds.\n");

        if (game.isRunning()) {
            System.out.println(PROMPT_MSG);
        } else {
            System.out.println(GAME_END_MSG);
        }
    }

    /**
     * Prints out the target grid showing taken shots and results.
     */
    private void drawTargetGrid() {
        HashMap<Coordinates, Ship.HitDesignation> hitMap = game.getOceanGrid().getHitMap();
        targetGrid = new StringBuilder((game.GRID_X * SYMBOL_LENGTH + 3) * (game.GRID_Y + 1));
        initialiseTargetGrid();

        hitMap.forEach(this::registerHit);
        System.out.println(targetGrid.toString());
    }

    /**
     * Updates the {@link #targetGrid} setting the specified hit designation at the given coordinates.
     * @param coordinates Coordinates to update.
     * @param hitDesignation New hit designation.
     */
    private void registerHit(Coordinates coordinates, Ship.HitDesignation hitDesignation) {
        char letterCoordinate = coordinates.getLetterCoordinate();
        int numberCoordinate = coordinates.getNumberCoordinate();

        int lineSize = 2 + SYMBOL_LENGTH * game.GRID_X + 1; // 3 -> letter label, space, newline
        int index = 1 + 3 * game.GRID_X + 1 // skipping number coordinates label, 2 -> letter label space, newline
                + lineSize * (letterCoordinate - 'A') // finding the right line
                + 2 + (numberCoordinate - 1) * SYMBOL_LENGTH; // finding the right place in line

        switch (hitDesignation) {
            case HIT -> targetGrid.replace(index, index + SYMBOL_LENGTH, HIT);
            case MISS -> targetGrid.replace(index, index + SYMBOL_LENGTH, MISS);
            case SINK -> targetGrid.replace(index, index + SYMBOL_LENGTH, SINK);
        }
    }

    /**
     * Appends a target grid filled with unknown squares.
     */
    private void initialiseTargetGrid() {
        /* number coordinate labels */
        targetGrid.append(" "); // 1 char
        IntStream.range(0, Math.min(9, game.GRID_X))
                .forEach(i -> targetGrid.append("  ").append(i + 1)); // single digit
        IntStream.range(9, game.GRID_X)
                .forEach(i -> targetGrid.append(" ").append(i + 1)); // double digit
        targetGrid.append('\n'); // 1 char

        /* filling the grid with empty squares */
        IntStream.range(0, game.GRID_Y).forEach(this::appendLine); // lineSize
    }

    /**
     * Appends a line of unknown squares.
     * @param lineNumber Appends at the beginning of the line as the letter coordinate.
     */
    /* line length should be summed up in #registerHit as lineSize */
    private void appendLine(int lineNumber) {
        /* letter coordinate label */
        targetGrid.append((char)(lineNumber + 'A')).append(" "); // 2 chars
        /* the unknown squares */
        IntStream.range(0, game.GRID_X).forEach(i -> targetGrid.append(UNKNOWN)); // SYMBOL_LENGTH * game.GRID_X
        targetGrid.append('\n'); // 1 char
    }

    /**
     * Validates and applies input.
     * Breaks the single-responsibility principle but is small enough to not warrant another class.
     */
    @Override
    public void handleInput() {
        String input;
        while (!validateInput(input = scanner.nextLine())) {
            System.out.println("Wrong input! Examples of proper coordinates: 'A4', 'B-2', 'g8', 'c-3'.");
        }
        game.setTargetCoordinates(input);
    }

    /**
     * Checks if the input can be interpreted as game coordinates.
     * @param input String to validate.
     * @return {@code true} if the input is valid.
     */
    private boolean validateInput(String input) {
        char letter = Character.toUpperCase(input.charAt(0));
        if (letter < 'A' || letter > 'Z') {
            return false;
        }

        try {
            Integer.parseInt(input.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
