package ar.edu.itba;

public class Config {

    private Integer N;
    private Double step;
    private Double radius;

    public Config(Integer N, Double step, Double radius) {
        this.N = N;
        this.step = step;
        this.radius = radius;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "\nConfig{\n" +
                "N:" + N + "\n" +
                "step:" + step + "\n" +
                "radius:" + radius + "\n" +
                "}";
    }

    public Double getStep() {
        return step;
    }

    public Integer getN() {
        return N;
    }
}
