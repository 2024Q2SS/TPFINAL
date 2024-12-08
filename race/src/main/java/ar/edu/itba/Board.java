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
    // 3rd iteration find new directions and magnitude
    public void calculateTargetDirection() {
        for (Car car : workingCars) {
            Vector2D tempGoal = car.getGoal().subtract(car.getPosition()).versor();
            double velocityModule = car.getVelocity().module();
            if (velocityModule == 0.0) {
                car.setTempGoal(tempGoal);
                continue;
            }

            switch (goals.indexOf(car.getGoal())) {
                case 0:
                    Vector2D nearestWall = car.getPosition().getY() >= 50 ? new Vector2D(0, 55)
                            : new Vector2D(0, 45);
                    double distanceToWall = car.getPosition().distance(nearestWall) - car.getRadius();
                    Vector2D wallCollisionVector = nearestWall.subtract(car.getPosition()).versor().multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector);
                    break;
                case 1:
                    Vector2D nearestWall1 = car.getPosition().getX() >= 25 ? new Vector2D(30, 0)
                            : new Vector2D(20, 0);
                    double distanceToWall1 = car.getPosition().distance(nearestWall1) - car.getRadius();
                    Vector2D wallCollisionVector1 = nearestWall1.subtract(car.getPosition()).versor()
                            .multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall1 / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector1);
                    break;
                case 2:
                    Vector2D nearestWall2 = car.getPosition().getY() >= 25 ? new Vector2D(0, 30)
                            : new Vector2D(0, 20);
                    double distanceToWall2 = car.getPosition().distance(nearestWall2) - car.getRadius();
                    Vector2D wallCollisionVector2 = nearestWall2.subtract(car.getPosition()).versor()
                            .multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall2 / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector2);
                    break;
                case 3:
                    Vector2D nearestWall3 = car.getPosition().getX() >= 55 ? new Vector2D(60, 0)
                            : new Vector2D(50, 0);
                    double distanceToWall3 = car.getPosition().distance(nearestWall3) - car.getRadius();
                    Vector2D wallCollisionVector3 = nearestWall3.subtract(car.getPosition()).versor()
                            .multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall3 / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector3);

                    break;
                case 4:
                    Vector2D nearestWall4 = car.getPosition().getY() >= 85 ? new Vector2D(0, 90)
                            : new Vector2D(0, 80);
                    double distanceToWall4 = car.getPosition().distance(nearestWall4) - car.getRadius();
                    Vector2D wallCollisionVector4 = nearestWall4.subtract(car.getPosition()).versor()
                            .multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall4 / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector4);
                    break;
                case 5:
                    Vector2D nearestWall5 = car.getPosition().getX() >= 75 ? new Vector2D(80, 0)
                            : new Vector2D(70, 0);
                    double distanceToWall5 = car.getPosition().distance(nearestWall5) - car.getRadius();
                    Vector2D wallCollisionVector5 = nearestWall5.subtract(car.getPosition()).versor()
                            .multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall5 / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector5);
                    break;
                case 6:
                    Vector2D nearestWall6 = car.getPosition().getY() >= 50 ? new Vector2D(0, 55)
                            : new Vector2D(0, 45);
                    double distanceToWall6 = car.getPosition().distance(nearestWall6) - car.getRadius();
                    Vector2D wallCollisionVector6 = nearestWall6.subtract(car.getPosition()).versor()
                            .multiply(car.getA())
                            .multiply(Math.exp(-distanceToWall6 / car.getB()));
                    tempGoal = tempGoal.add(wallCollisionVector6);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid goal");
            }
            for (Car other : allCars) {
                if (car.equals(other))
                    continue;
                Vector2D collisionVector = other.getPosition().subtract(car.getPosition()).versor()
                        .multiply(car.getA())
                        .multiply(Math.exp(
                                -(car.getPosition().distance(other.getPosition()) - other.getRadius() - car.getRadius())
                                        / car.getB()));
                tempGoal = tempGoal.add(collisionVector);
            }
            car.setTempGoal(tempGoal);
        }

    }

    // 4th iteration update SPEED FIRST and position
    public void updateCars() {
        for (Car car : workingCars) {
            car.setVelocity(car.getTempGoal().multiply(car.getMaxSpeed()));
            car.setPosition(car.getPosition().add(car.getVelocity().multiply(dt)));
        }
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

                findCollisions();
                calculateTargetDirection();
                updateCars();
                time += dt;

                builder.setLength(0);
                builder.append(time);

                for (Car particle : allCars) {
                    builder.append("," + particle.getPosition().getX() + "," + particle.getPosition().getY());
                    csv.println(builder.toString());
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
