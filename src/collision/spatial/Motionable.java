package collision.spatial;

/**
 * Created by alexthomas on 8/17/16.
 */
public interface Motionable extends  Moveable{
    public void setVelocity(Vector v);
    public Vector getVelocity();
    public void accelerate(Vector v);

}
