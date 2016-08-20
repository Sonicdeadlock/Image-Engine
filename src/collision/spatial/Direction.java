package collision.spatial;

/**
 * Created by alexthomas on 8/18/16.
 */
public enum Direction {
    UP(Math.PI/2),
    DOWN(Math.PI/2*3),
    LEFT(Math.PI),
    RIGHT(0),
    NONE(0);

    private double direction;

    Direction(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }
}
