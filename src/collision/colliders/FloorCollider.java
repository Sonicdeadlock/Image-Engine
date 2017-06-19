package collision.colliders;

import collision.Collider;
import collision.CollisionDetails;
import collision.spatial.Direction;
import collision.spatial.Point;

/**
 * Created by Alex on 8/20/2016.
 */
public class FloorCollider extends Collider {
    private int y;


    public FloorCollider(int y){
        super(new Point(0,y), Direction.NONE);
        this.y = y;

    }

//    @Override
//    public CollisionDetails hasCollision(Collider c) {
//        return c.hasCollision(this);
//    }
//
    @Override
    public boolean hasCollisionAtPoint(Point p) {
        return (p.getY()==this.y);
    }
}
