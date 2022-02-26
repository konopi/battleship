package konopi.battleship.logic;

import konopi.battleship.ui.UI;

public class Game {
    private boolean running = true;

    private String currentAnswer;

    public Game() {}

    public boolean isRunning() {
        return running;
    }

    public void tick() {
        if ("quit".equals(currentAnswer)) {
            running = false;
        }
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }
}
