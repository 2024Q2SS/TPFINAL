package ar.edu.itba;

public class Config {

    private final int N;
    private final double radius;
    private final double a;
    private final double b;
    private final double beta;
    private final double tau;
    private final double maxSpeed;
    private final double goalBias;

    public Config(int n, double radius, double a, double b, double beta, double tau, double maxSpeed, double goalBias) {
        N = n;
        this.radius = radius;
        this.a = a;
        this.b = b;
        this.beta = beta;
        this.tau = tau;
        this.maxSpeed = maxSpeed;
        this.goalBias = goalBias;
    }

    public double getGoalBias() {
        return goalBias;
    }

    public int getN() {
        return N;
    }

    public double getRadius() {
        return radius;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getBeta() {
        return beta;
    }

    public double getTau() {
        return tau;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

}
