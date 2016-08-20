package collision;

import collision.spatial.Vector;

import java.util.List;

/**
 * Created by alexthomas on 8/17/16.
 */
public class CollisionDetails {
    private Vector vector;
    private List<Collider> colliders;

    public CollisionDetails(Vector vector, List<Collider> colliders) {
        this.vector = vector;
        this.colliders = colliders;
    }

    public Vector getVector() {
        return vector;
    }

    public List<Collider> getColliders() {
        return colliders;
    }
}
