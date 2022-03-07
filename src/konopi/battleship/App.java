package konopi.battleship;

import konopi.battleship.logic.Game;
import konopi.battleship.ui.UI;

/**
 * The game application.
 */
public class App implements Runnable {
    private final UI ui;
    private final Game game;

    /**
     * Main constructor. The game loop runs as a thread starting with the object construction.
     * @param ui Dependency injection of the user interface implementation.
     */
    public App(UI ui) {
        this.ui = ui;
        this.game = new Game();
        start();
    }

    public synchronized void start() {
        new Thread(this).start();
    }

    /**
     * Initialises the user interface and runs the game loop.
     */
    @Override
    public void run() {
        ui.initialise(game);
        while(game.isRunning()) {
            ui.handleInput();
            game.tick();
            ui.update();
        }
    }
}
