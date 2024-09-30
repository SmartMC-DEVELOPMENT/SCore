package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import org.joml.Vector3i;

import java.util.*;

@Getter
public class Cuboid {

    private final Vector3i min, max;

    public Cuboid(Vector3i loc1, Vector3i loc2) {
        int xMin = Math.min(loc1.x(), loc2.x());
        int yMin = Math.min(loc1.y(), loc2.y());
        int zMin = Math.min(loc1.z(), loc2.z());

        int xMax = Math.max(loc1.x(), loc2.x());
        int yMax = Math.max(loc1.y(), loc2.y());
        int zMax = Math.max(loc1.z(), loc2.z());

        this.min = new Vector3i(xMin, yMin, zMin);
        this.max = new Vector3i(xMax, yMax, zMax);
    }

    public List<Vector3i> getVectors() {
        List<Vector3i> vectors = new ArrayList<>();
        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {
                    vectors.add(new Vector3i(x, y, z));
                }
            }
        }
        return vectors;
    }


    public boolean contains(Vector3i vector) {
        int vectorX = vector.x;
        int vectorY = vector.y;
        int vectorZ = vector.z;

        return vectorX >= min.x() && vectorX <= max.x() &&
                vectorY >= min.y() && vectorY <= max.y() &&
                vectorZ >= min.z() && vectorZ <= max.z();
    }

    public double getVolume() {
        return (max.x() - min.x() + 1) *
                (max.y() - min.y() + 1) *
                (max.z() - min.z() + 1);
    }

    public Vector3i[] getCorners() {
        return new Vector3i[] {
                new Vector3i(min.x(), min.y(), min.z()),
                new Vector3i(min.x(), min.y(), max.z()),
                new Vector3i(min.x(), max.y(), min.z()),
                new Vector3i(min.x(), max.y(), max.z()),
                new Vector3i(max.x(), min.y(), min.z()),
                new Vector3i(max.x(), min.y(), max.z()),
                new Vector3i(max.x(), max.y(), min.z()),
                new Vector3i(max.x(), max.y(), max.z())
        };
    }
}
