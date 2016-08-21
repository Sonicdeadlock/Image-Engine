package collision;

import collision.spatial.Direction;
import collision.spatial.Placeable;
import collision.spatial.Point;

/**
 * Created by alexthomas on 8/17/16.
 */
public abstract class Collider implements Placeable {
    private Point point;
    private double direction;


    public Collider(int x, int y, double direction) {
        this(new Point(x,y),direction);
    }

    public Collider(int x, int y, Direction direction){
        this(new Point(x,y),direction);
    }

    public Collider(Point point, double direction) {
        this.point = point;
        this.direction = direction;
    }

    public Collider(Point point, Direction direction) {
        this.point = point;
        this.direction = direction.getDirection();
    }

    @Override
    public int getX() {
        return point.getX();
    }

    @Override
    public int getY() {
        return point.getY();
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }


    public abstract boolean hasCollisionAtPoint(Point p);


}
