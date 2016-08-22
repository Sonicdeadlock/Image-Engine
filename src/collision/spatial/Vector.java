package collision.spatial;

/**
 * Created by alexthomas on 8/18/16.
 */
public class Vector {
    private double amplitude;
    private double direction;
    private static final double UNIT_CIRCLE = Math.PI*2;

    public Vector(double amplitude, double direction) {
        this.amplitude = amplitude;
        this.direction = direction%UNIT_CIRCLE;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getDirection() {
        return direction;
    }

    public double getX(){
        return amplitude*Math.cos(direction);
    }

    public double getY(){
        return amplitude*Math.sin(direction);
    }

    public static Vector createFromPoints(Point p1,Point p2){
        double diffX = p1.getX()-p2.getX();
        double diffY = p1.getY()-p2.getY();
        double amplitude = Math.sqrt(Math.pow(diffX,2)+Math.pow(diffY,2));
        double direction = Math.atan2(diffY,diffX)%UNIT_CIRCLE;
        return new Vector(amplitude,direction);
    }

    public static Vector add(Vector v1,Vector v2){
        double xNet = v1.getX()+v2.getX();
        double yNet = v1.getY()+v2.getY();
        double amplitude = Math.sqrt(Math.pow(xNet,2)+Math.pow(yNet,2));
        double direction = Math.atan2(yNet,xNet)%UNIT_CIRCLE;
        return new Vector(amplitude,direction);

    }

    public static Vector subtract(Vector v1, Vector v2){
        double xNet = v1.getX()-v2.getX();
        double yNet = v1.getY()-v2.getY();
        double amplitude = Math.sqrt(Math.pow(xNet,2)+Math.pow(yNet,2));
        double direction = Math.atan2(yNet,xNet)%UNIT_CIRCLE;
        return new Vector(amplitude,direction);
    }
}
