package ar.edu.itba;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private Config config;
    private List<Vector2D> goals = new ArrayList<>();
    private List<Car> workingCars = new ArrayList<>();
    private List<Car> collidedCars = new ArrayList<>();
    private List<Car> allCars = new ArrayList<>();
    private List<Collision> collisions = new ArrayList<>();
    private final double dt;

    public Board(Config config) {
        this.config = config;
        this.dt = config.getRadius() / (config.getMaxSpeed() * 2);
    }

    public void initialize() {
        goals = new ArrayList<>();
        goals.add(new Vector2D(25.00, 50.00));
        goals.add(new Vector2D(25.00, 25.00));
        goals.add(new Vector2D(55.00, 25.00));
        goals.add(new Vector2D(55.00, 85.00));
        goals.add(new Vector2D(75.00, 85.00));
        goals.add(new Vector2D(75.00, 55.00));
        goals.add(new Vector2D(100.00, 50.00));

        workingCars = new ArrayList<>();
        Vector2D position;
        Double spacing = (10 - 2 * config.getRadius()) / (config.getN() + 1);
        // hay que chequear que entren los autos en el espacio dado, sino ponerlos en
        // fila?
        for (int i = 0; i < config.getN(); i++) {
            position = new Vector2D(0.00, 45.00 + i * spacing);
            workingCars
                    .add(new Car(config.getBeta(), config.getA(), config.getB(), config.getTau(), config.getMaxSpeed(),
                            config.getRadius(), position, goals.get(0)));
        }
        allCars = workingCars;
    }

    public void run() {
        double time = 0;
        initialize();
        StringBuilder builder = new StringBuilder();
        builder.append("t");
        for (int i = 0; i < allCars.size(); i++) {
            builder.append(",p" + i + "x,p" + i + "y");
        }
        try (PrintWriter csv = new PrintWriter(new FileWriter("output.csv"))) {
            csv.println(builder.toString());
            while (!finishConditionsMet()) {
                collisions.clear();
                for (Car particle : allCars) {
                    csv.println(particle.getPosition().getX() + "," + particle.getPosition().getY());
                }
            }
        } catch (

        Exception e) {
        }
    }

    public Boolean finishConditionsMet() {
        return false;
        // poner algun caso con steps por las dudas?
        // if (workingCars.isEmpty())
        // return true;
        // for (Car car : workingCars) {
        // if (car.getPosition().getX() == 100.00)
        // return true;
        // }
        // return false;
    }

    public Boolean isInsideTrack(Car particle) {
        return false;
    }

}
