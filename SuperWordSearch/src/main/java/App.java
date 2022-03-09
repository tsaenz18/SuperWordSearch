package src.main.java;
import src.main.java.game.PlayGame;
import src.main.java.game.SuperSearchWordPersistenceException;

public class App {

    public static void main(String[] args) throws SuperSearchWordPersistenceException {
        PlayGame play = new PlayGame();
        play.PlayGame("data.txt");
    }
}
