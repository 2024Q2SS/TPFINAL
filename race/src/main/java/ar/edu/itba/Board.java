package ar.edu.itba;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private Config config;
    private ArrayList<Coordinate> goals;
    private ArrayList<Particle> workingCars;
    private ArrayList<Particle> collidedCars;
    private ArrayList<Particle> allCars;

    public Board(Config config) {
        this.config = config;
    }

    public void initialize() {
        goals = new ArrayList<>();
        goals.add(new Coordinate(25.00, 50.00));
        goals.add(new Coordinate(25.00, 25.00));
        goals.add(new Coordinate(55.00, 25.00));
        goals.add(new Coordinate(55.00, 85.00));
        goals.add(new Coordinate(75.00, 85.00));
        goals.add(new Coordinate(75.00, 55.00));
        goals.add(new Coordinate(100.00, 50.00));

        workingCars = new ArrayList<>();
        Coordinate aux;
        Double spacing = (10 - 2 * config.getRadius()) / (config.getN() + 1);
        // hay que chequear que entren los autos en el espacio dado, sino ponerlos en
        // fila?
        for (int i = 0; i < config.getN(); i++) {
            aux = new Coordinate(0.00, i * spacing);
            // arrancan todos con toda la velocidad en x.
            workingCars.add(new Particle(1, 0, aux));
        }
        allCars = workingCars;
    }

    public void run() {
        initialize();
        try (PrintWriter csv = new PrintWriter(new FileWriter("output.csv"))) {
            csv.print("x,y");
            while (!finishConditionsMet()) {
                for (Particle car : workingCars) {
                    if (!car.updatePosition(collidedCars, config.getStep())) {
                        workingCars.remove(car);
                        collidedCars.add(car);// nose si es necesario mantener una lista con los colisionados pero bueno
                    }
                }
                for (Particle particle : allCars) {
                    csv.println(particle.getCoordinates().getX() + "," + particle.getCoordinates().getY());
                }
            }
        } catch (

        Exception e) {
        }
    }

    public Boolean finishConditionsMet() {
        // poner algun caso con steps por las dudas?
        if (workingCars.isEmpty())
            return true;
        for (Particle car : workingCars) {
            if (car.getCoordinates().getX() == 100.00)
                return true;
        }
        return false;
    }

    public Boolean isInsideTrack(Particle particle) {
        return false;
    }

}
