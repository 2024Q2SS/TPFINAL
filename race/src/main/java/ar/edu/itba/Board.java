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
    // private List<Collision> collisions = new ArrayList<>();
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
        Double spacing = (10 - (2 * config.getRadius()) * config.getN()) / (config.getN() + 1);
        double M = Double.valueOf(config.getN());
        if (spacing <= 0) {
            M = getMaxPerColumn();
            spacing = (10 - (2 * config.getRadius()) * M) / (M + 1);
        }
        System.out.println("M=" + M + "\n" + "Spacing=" + spacing);
        int multiplier = 0;
        for (int i = 1; i <= config.getN(); i++) {
            double x = 0.00;
            if (i == M) {
                x = 1.00;
                multiplier = 1;
            }
            position = new Vector2D(x, 45.00 + multiplier++ * spacing);
            workingCars
                    .add(new Car(config.getBeta(), config.getA(), config.getB(), config.getTau(),
                            config.getMaxSpeed(),
                            config.getRadius(), position, goals.get(0)));
        }
        allCars = workingCars;
    }

    public double getMaxPerColumn() {
        Double spacing = (10 - (2 * config.getRadius()) * config.getN()) / (config.getN() + 1);
        double M = config.getN();
        while (spacing <= 0) {
            M = Math.ceil(config.getN() / 2.0);
            spacing = (10 - (2 * config.getRadius()) * M) / (M + 1);
        }
        return M;
    }

    // 1st iteration
    public void findCollisions() {
        for (Car car : workingCars) {
            double x = car.getPosition().getX();
            double y = car.getPosition().getY();
            // wall check
            switch (goals.indexOf(car.getGoal())) {
                case 0:
                    if (y + car.getRadius() >= 55 || y - car.getRadius() <= 45) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                case 1:
                    if (x + car.getRadius() >= 30 || x - car.getRadius() <= 20) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                case 2:
                    if (y + car.getRadius() >= 30 || y - car.getRadius() <= 20) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                case 3:
                    if (x + car.getRadius() >= 60 || x - car.getRadius() <= 50) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                case 4:
                    if (y + car.getRadius() >= 90 || y - car.getRadius() <= 80) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                case 5:
                    if (x + car.getRadius() >= 80 || x - car.getRadius() <= 70) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                case 6:
                    if (y + car.getRadius() >= 55 || y - car.getRadius() <= 45) {
                        workingCars.remove(car);
                        collidedCars.add(car);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid goal");

            }
            // car check
            for (Car other : allCars) {
                if (car.equals(other))
                    continue;
                if (car.getPosition().distance(other.getPosition()) < car.getRadius() + other.getRadius()) {
                    workingCars.remove(car);
                    workingCars.remove(other);
                    collidedCars.add(car);
                    collidedCars.add(other);
                }
            }

        }
    }

    // 2nd iteration radii adjustment (no, i dont think i will)
    // 3rd iteration find new directions and magintude
    public void calculateTargetDirection() {
    }

    // 4th iteration update SPEED FIRST and position
    public void updateCars() {

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
                // collisions.clear();
                for (Car particle : allCars) {
                    csv.println(particle.getPosition().getX() + "," + particle.getPosition().getY());
                }

                findCollisions();
                calculateTargetDirection();
                updateCars();
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
