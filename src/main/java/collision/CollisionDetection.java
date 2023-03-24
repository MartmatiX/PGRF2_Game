package collision;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

public class CollisionDetection {

    public static boolean detectCollision(Entity entity1, Entity entity2) {
        Vector3f position1 = entity1.getPosition();
        Vector3f position2 = entity2.getPosition();
        float radius1 = entity1.getScale() + 2; // develop note: change the scale and create more realistic collisions
        float radius2 = entity2.getScale() + 2;

        float dx = position1.x - position2.x;
        float dy = position1.y - position2.y;
        float dz = position1.z - position2.z;
        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        return distance < radius1 + radius2;
    }

}
