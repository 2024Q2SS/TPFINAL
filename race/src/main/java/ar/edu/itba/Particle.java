package ar.edu.itba;

import java.util.ArrayList;

public class Particle {
    private Coordinate coordinates;
    private Integer vx;
    private Integer vy;

    public Particle(Integer vx, Integer vy, Coordinate coordinates) {
        this.vx = vx;
        this.vy = vy;
        this.coordinates = coordinates;
    }

    // aca hay que usar el cpm, por ahora lo hice asi para tener algo;
    public Boolean updatePosition(ArrayList<Particle> collidedCars, Double step) {
        this.coordinates.setX(this.coordinates.getX() + step * this.vx);
        for (Particle obstacle : collidedCars) {
            if (obstacle.getCoordinates().getX() == this.getCoordinates().getX())
                return false;
        }
        return checkWalls();
    }
    
    //a esto tambien le falta laburo, claramente
    public Boolean checkWalls() {
        return true;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public Integer getVx() {
        return vx;
    }

    public Integer getVy() {
        return vy;
    }

}
