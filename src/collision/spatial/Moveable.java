package collision.spatial;

/**
 * Created by alexthomas on 8/18/16.
 */
public interface Moveable extends Placeable {
    void move(Vector v);
    void moveTo(Point p);
}
