package ar.edu.itba;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
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

        try (PrintWriter csv = new PrintWriter(new FileWriter("obs_output.csv"))) {
            csv.println("t, cols_p");
            for (int i = 0; i < 1; i++) {
                double time = board.run();
                csv.println(String.valueOf(time) + "," + String.valueOf(board.collisionPercentage()));
                board.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
