package ar.edu.itba;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {

    private Config config;
    private List<Vector2D> goals = new ArrayList<>();
    private List<Car> workingCars = new ArrayList<>();
    private List<Car> collidedCars = new ArrayList<>();
    private List<Car> finishedCars = new ArrayList<>();
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
        double multiplier = spacing;
        int counter = 0;
        for (int i = 1; i <= config.getN(); i++) {
            double x = 0.00;
            if (i > M) {
                x = 1.00;
                multiplier = 1;
                counter = 0;
            }
            position = new Vector2D(x,
                    45.00 + multiplier + config.getRadius() + (counter) * config.getRadius() * 2);
            multiplier += spacing;
            counter++;
            workingCars
                    .add(new Car(config.getBeta(), config.getA(), config.getB(), config.getTau(),
                            config.getMaxSpeed(),
                            config.getRadius(), position, goals.get(0)));
        }
        allCars.addAll(workingCars);
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
        Set<Car> toRemove = new HashSet<>();
        List<Car> auxCars = new ArrayList<>();
        auxCars.addAll(allCars);
        auxCars.removeAll(finishedCars);
        for (Car car : workingCars) {
            double x = car.getPosition().getX();
            double y = car.getPosition().getY();
            // wall check

            switch (goals.indexOf(car.getGoal())) {
                case 0:
                    if (y + car.getRadius() >= 55 || y - car.getRadius() <= 45) {
                        System.out.println("Collision with wall, goal 0");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                case 1:
                    if (x + car.getRadius() >= 30 || (x - car.getRadius() <= 20 && y - car.getRadius() <= 45)) {
                        System.out.println("Collision with wall, goal 1");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                case 2:
                    if ((y + car.getRadius() >= 30 && x + car.getRadius() >= 30) || y - car.getRadius() <= 20) {
                        System.out.println("Collision with wall, goal 2");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                case 3:
                    if (x + car.getRadius() >= 60 || (x - car.getRadius() <= 50 && y + car.getRadius() >= 30)) {
                        System.out.println("Collision with wall, goal 3");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                case 4:
                    if (y + car.getRadius() >= 90 || (y - car.getRadius() <= 80 && x + car.getRadius() >= 60)) {
                        System.out.println("Collision with wall, goal 4");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                case 5:
                    if (x + car.getRadius() >= 80 || (x - car.getRadius() <= 70 && y - car.getRadius() <= 80)) {
                        System.out.println("Collision with wall, goal 5");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                case 6:
                    if ((x + car.getRadius() >= 80 && y + car.getRadius() >= 55) || y - car.getRadius() <= 45) {
                        System.out.println("Collision with wall, goal 6");
                        toRemove.add(car);
                        car.setCollided(true);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid goal");

            }
            // car check
            for (Car other : auxCars) {
                if (car.equals(other))
                    continue;
                if (car.getPosition().distance(other.getPosition()) < car.getRadius() + other.getRadius()) {
                    toRemove.add(car);
                    System.out.println("Collision with car");
                    car.setCollided(true);
                }
            }

        }
        workingCars.removeAll(toRemove);
        collidedCars.addAll(toRemove);
    }

    public Vector2D calculateWallCollisionVector(Car car, Vector2D other) {
        return other.subtract(car.getPosition()).versor()
                .multiply(car.getA())
                .multiply(Math.exp(
                        -(car.getPosition().distance(other) - car.getRadius())
                                / car.getB()));
    }

    public Vector2D calculateCarCollisionVector(Car car, Car other) {
        return car.getPosition().subtract(other.getPosition()).versor()
                .multiply(car.getA())
                .multiply(Math.exp(
                        -(car.getPosition().distance(other.getPosition()) - other.getRadius() - car.getRadius())
                                / car.getB()));
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
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall));
                    break;
                case 1:
                    Vector2D nearestWall1 = car.getPosition().getX() >= 25 ? new Vector2D(30, 0)
                            : new Vector2D(20, 0);
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall1));
                    break;
                case 2:
                    Vector2D nearestWall2 = car.getPosition().getY() >= 25 ? new Vector2D(0, 30)
                            : new Vector2D(0, 20);
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall2));

                    break;
                case 3:
                    Vector2D nearestWall3 = car.getPosition().getX() >= 55 ? new Vector2D(60, 0)
                            : new Vector2D(50, 0);
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall3));
                    break;
                case 4:
                    Vector2D nearestWall4 = car.getPosition().getY() >= 85 ? new Vector2D(0, 90)
                            : new Vector2D(0, 80);
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall4));
                    break;
                case 5:
                    Vector2D nearestWall5 = car.getPosition().getX() >= 75 ? new Vector2D(80, 0)
                            : new Vector2D(70, 0);
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall5));
                    break;
                case 6:
                    Vector2D nearestWall6 = car.getPosition().getY() >= 50 ? new Vector2D(0, 55)
                            : new Vector2D(0, 45);
                    tempGoal = tempGoal.add(calculateWallCollisionVector(car, nearestWall6));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid goal");
            }
            for (Car other : allCars) {
                if (car.equals(other))
                    continue;
                tempGoal = tempGoal.add(calculateCarCollisionVector(car, other));
            }
            car.setTempGoal(tempGoal);
        }

    }

    // 4th iteration update SPEED FIRST and position
    public void updateCars() {

        for (Car car : workingCars) {
            car.setVelocity(car.getTempGoal().multiply(car.getMaxSpeed()));
            car.setPosition(car.getPosition().add(car.getVelocity().multiply(dt)));
            System.out.println("Car position: " + car.getPosition());
            switch (goals.indexOf(car.getGoal())) {
                case 0:
                    if (car.getPosition().getX() - car.getRadius() > 20) {
                        car.setGoal(goals.get(1));
                    }
                    break;
                case 1:
                    if (car.getPosition().getY() + car.getRadius() < 30) {
                        car.setGoal(goals.get(2));
                    }
                    break;
                case 2:
                    if (car.getPosition().getX() - car.getRadius() > 50) {
                        car.setGoal(goals.get(3));
                    }
                    break;
                case 3:
                    if (car.getPosition().getY() - car.getRadius() > 80) {
                        car.setGoal(goals.get(4));
                    }
                    break;
                case 4:
                    if (car.getPosition().getX() - car.getRadius() > 70) {
                        car.setGoal(goals.get(5));
                    }
                    break;
                case 5:
                    if (car.getPosition().getY() + car.getRadius() < 55) {
                        car.setGoal(goals.get(6));
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public void run() {
        double time = 0;
        int count = 0;
        // int maxRounds = 100000000000;
        initialize();
        StringBuilder builder = new StringBuilder();
        builder.append("t");
        for (int i = 0; i < allCars.size(); i++) {
            builder.append(",p" + i + "x,p" + i + "y,c" + i);
        }
        try (PrintWriter csv = new PrintWriter(new FileWriter("output.csv"))) {
            csv.println(builder.toString());
            builder.setLength(0);
            builder.append(time);

            for (Car particle : allCars) {
                builder.append("," + particle.getPosition().getX() + "," + particle.getPosition().getY() + ","
                        + particle.isCollided());
            }
            csv.println(builder.toString());
            while (!finishConditionsMet() /* && (count < maxRounds) */) {
                count++;
                // collisions.clear();
                if (count % 10 == 0) {
                    System.out.println("Round " + count);
                }

                findCollisions();
                calculateTargetDirection();
                updateCars();
                time += dt;

                builder.setLength(0);
                builder.append(time);

                for (Car particle : allCars) {
                    builder.append("," + particle.getPosition().getX() + "," + particle.getPosition().getY() + ","
                            + particle.isCollided());
                }
                csv.println(builder.toString());
            }
            System.out.println("Simulation finished");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean finishConditionsMet() {
        if (workingCars.isEmpty()) {
            return true;
        }
        Set<Car> toRemove = new HashSet<>();
        for (Car car : workingCars) {
            if (car.getPosition().getX() + car.getRadius() >= 100) {
                toRemove.add(car);
            }
        }
        workingCars.removeAll(toRemove);
        finishedCars.addAll(toRemove);
        return false;
    }

    public Boolean isInsideTrack(Car particle) {
        return false;
    }

}
