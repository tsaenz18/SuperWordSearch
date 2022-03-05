package src.main.java;
import src.main.java.game.PlayGame;
import src.main.java.game.SuperSearchWordPersistenceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

public class App {

    public static void main(String[] args) throws SuperSearchWordPersistenceException {
        PlayGame play = new PlayGame();
        play.PlayGame("data.txt");
    }
}
