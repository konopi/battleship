package konopi.battleship.ui;

import konopi.battleship.logic.Game;

public interface UI {
    void initialise(Game game);
    void update(Game game);
    void handleInput(Game game);
}
