package konopi.battleship.ui;

import konopi.battleship.logic.Game;

/**
 * The UI is injected into the App object in the constructor.
 */
public interface UI {
    /**
     * This method is called before the game loop.
     * @param game The game reference which should be set as a field for other methods.
     */
    void initialise(Game game);

    /**
     * This method is called after every game tick inside the game loop.
     */
    void update();

    /**
     * This method is called before every game tick inside the game loop.
     */
    void handleInput();
}
