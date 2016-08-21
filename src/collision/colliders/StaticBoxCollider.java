package collision.colliders;

import collision.Collider;
import collision.spatial.Direction;
import collision.spatial.Point;

/**
 * Created by Alex on 8/20/2016.
 */
public class StaticBoxCollider extends Collider {
    private int width,height;

    public StaticBoxCollider(Point origin, int width, int height,  Direction direction) {
        super(origin, direction);
        this.width = width;
        this.height = height;
    }


    @Override
    public boolean hasCollisionAtPoint(Point p) {
        return ((p.getX()>getPoint().getX()&&p.getX()<getPoint().getX()+width)||
                (p.getY()>getPoint().getY()&&p.getY()<getPoint().getY()+height));
    }
}
