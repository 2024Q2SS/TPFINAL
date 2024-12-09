package ar.edu.itba;

public class Car {
    private final double beta;
    private final double a;
    private final double b;
    private final double tau;
    private final double maxSpeed;
    private double radius;
    private Vector2D velocity;
    private Vector2D goal;
    private Vector2D position;
    private Vector2D nextVelocity;
    private Vector2D tempGoal;

    private boolean collided = false;

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }

    public Car(final double beta, final double a, final double b, final double tau, final double maxSpeed,
            final double radius, final Vector2D position, final Vector2D goal) {
        this.beta = beta;
        this.a = a;
        this.b = b;
        this.tau = tau;
        this.maxSpeed = maxSpeed;
        this.radius = radius;
        this.velocity = Vector2D.ZERO();
        this.nextVelocity = Vector2D.ZERO();
        this.position = position;
        this.goal = goal;
        this.tempGoal = goal;
    }

    public double getBeta() {
        return beta;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getTau() {
        return tau;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getGoal() {
        return goal;
    }

    public void setGoal(Vector2D goal) {
        this.goal = goal;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getNextVelocity() {
        return nextVelocity;
    }

    public void setNextVelocity(Vector2D nextVelocity) {
        this.nextVelocity = nextVelocity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Car other = (Car) obj;
        if (position == null) {

            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

    public Vector2D getTempGoal() {
        return tempGoal;
    }

    public void setTempGoal(Vector2D tempGoal) {
        this.tempGoal = tempGoal;
    }
}
