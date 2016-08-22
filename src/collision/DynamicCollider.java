package collision;

import collision.spatial.Direction;
import collision.spatial.Point;
import collision.spatial.Motionable;
import collision.spatial.Vector;

/**
 * Created by alexthomas on 8/17/16.
 */
public abstract class DynamicCollider extends Collider implements Motionable {
    protected boolean[][] localTakenPoints;
    private Vector velocity;


    public DynamicCollider(int x, int y, Direction direction) {
        super(x, y, direction);
    }

    public boolean localPointIsTaken(Point p){
        return localTakenPoints[p.getX()][p.getY()];
    }

    public boolean globalPointIsTaken(Point p){
        int x,y;
        x=p.getX()-this.getX();
        y=p.getY()-this.getY();
        return localTakenPoints[x][y];
    }

    @Override
    public boolean hasCollisionAtPoint(Point p) {
        return globalPointIsTaken(p);
    }

    protected abstract void renderTakenPoints();


    @Override
    public Vector getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    @Override
    public void accelerate(Vector v) {
       this.velocity = Vector.add(this.velocity,v);
    }

    @Override
    public void move(Vector v) {
        this.setPoint(new Point((int)(v.getX()+this.getX()),(int)(this.getY()+v.getY())));
    }

    @Override
    public void moveTo(Point p) {
        this.setPoint(p);
    }

    public abstract CollisionDetails hasCollision(Collider c);

    public void init(){
        renderTakenPoints();
    }

}
