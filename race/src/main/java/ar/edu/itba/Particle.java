package ar.edu.itba;

public class Particle {
    private Coordinate coordinates;
    private Integer vx;
    private Integer vy;

    public Particle(Integer vx, Integer vy, Coordinate coordinates) {
        this.vx = vx;
        this.vy = vy;
        this.coordinates = coordinates;
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
