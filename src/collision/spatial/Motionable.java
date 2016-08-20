package collision.spatial;

/**
 * Created by alexthomas on 8/17/16.
 */
public interface Motionable extends  Moveable{
    public double getXVelocity();
    public double getYVelocity();
    public void setXVelocity(double vx);
    public void setYVelocity(double vy);
    public void accelerate(Vector v);

}
