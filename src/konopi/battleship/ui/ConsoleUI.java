package konopi.battleship.ui;

import konopi.battleship.logic.Game;

import java.util.Scanner;

public class ConsoleUI implements UI {
    private final Scanner scanner = new Scanner(System.in);

    private final String welcomeMsg = "Welcome to Not Yet Battleship!";
    private final String playerMoveMsg = "Type 'quit' to quit:";
    private final String gameOverMsg = "It's over.";

    @Override
    public void initialise(Game game) {
        System.out.println(welcomeMsg);
        System.out.println(playerMoveMsg);
    }

    @Override
    public void update(Game game) {
        if (game.isRunning()) {
            System.out.flush();
            System.out.println(playerMoveMsg);
        }
        else {
            System.out.println(gameOverMsg);
        }
    }

    @Override
    public void handleInput(Game game) {
        ConsoleInput input = new ConsoleInput(scanner.nextLine());
        game.setCurrentAnswer(input.value);
    }

    private static class ConsoleInput {
        private final String value;

        public ConsoleInput(String input) {
            this.value = input;
        }
    }
}
