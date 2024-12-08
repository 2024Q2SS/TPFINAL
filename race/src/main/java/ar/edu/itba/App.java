package ar.edu.itba;

import java.io.FileReader;
import java.nio.file.Path;
import com.google.gson.Gson;

public class App {
    private static final String absPath = System.getProperty("user.dir");
    private static final String confPath = "../config.json";
    private static final Gson Gson = new Gson();

    public Config setUp() {
        String filePath = Path.of(absPath, confPath).toString();
        Config conf = null;
        try (FileReader reader = new FileReader(filePath)) {
            conf = Gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return conf;
    }

    public static void main(String[] args) {

        App app = new App();
        Board board = new Board(app.setUp());
        board.run();

    }
}
