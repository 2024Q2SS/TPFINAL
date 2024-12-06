package ar.edu.itba;

public class Config {

    private Integer N;
    private Double step;

    public Config(Integer N, Double step) {
        this.N = N;
        this.step = step;
    }

    @Override
    public String toString() {
        return "\nConfig{\n" +
                "N:" + N + "\n" +
                "step:" + step + "\n" +
                "}";
    }

    public Double getStep() {
        return step;
    }

    public Integer getN() {
        return N;
    }
}
