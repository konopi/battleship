package konopi.battleship;

import konopi.battleship.logic.Game;
import konopi.battleship.ui.UI;

public class App implements Runnable {
    private final UI ui;
    private final Game game;

    public App(UI ui) {
        this.ui = ui;
        this.game = new Game();
        start();
    }

    public synchronized void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        ui.initialise(game);
        while(game.isRunning()) {
            ui.handleInput(game);
            game.tick();
            ui.update(game);
        }
    }
}
