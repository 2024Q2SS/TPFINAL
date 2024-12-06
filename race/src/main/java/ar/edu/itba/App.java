package ar.edu.itba;

import java.io.FileReader;
import java.nio.file.Paths;
import com.google.gson.Gson;

public class App {
    private static Board board;
    private static Config config;
    private String configPath = "../config.json";
    private final static String rootDir = System.getProperty("user.dir");

    public void setup() {
        if (!Paths.get(configPath).isAbsolute()) {

            configPath = Paths.get(rootDir, configPath).toString();

        }

        try (FileReader reader = new FileReader(configPath)) {
            config = new Gson().fromJson(reader, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(config);
        board = new Board(config);
    }

    public static void main(String[] args) {
        App app = new App();
        app.setup();
        board.run();
    }
}
