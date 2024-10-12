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

    public Vector3i getCenter() {
        int centerX = (min.x() + max.x()) / 2;
        int centerY = (min.y() + max.y()) / 2;
        int centerZ = (min.z() + max.z()) / 2;

        return new Vector3i(centerX, centerY, centerZ);
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

    public Vector3i getRelativeCoordinates(Vector3i location) {
        if (!contains(location)) {
            throw new IllegalArgumentException("La ubicación está fuera del cubo.");
        }

        int relativeX = location.x() - min.x();
        int relativeY = location.y() - min.y();
        int relativeZ = location.z() - min.z();

        return new Vector3i(relativeX, relativeY, relativeZ);
    }

    public Vector3i getGlobalCoords(Vector3i relativeCoordinates) {
        int globalX = min.x() + relativeCoordinates.x();
        int globalY = min.y() + relativeCoordinates.y();
        int globalZ = min.z() + relativeCoordinates.z();

        return new Vector3i(globalX, globalY, globalZ);
    }


    public boolean contains(Vector3i vector) {
        int vectorX = vector.x;
        int vectorY = vector.y;
        int vectorZ = vector.z;

        return vectorX >= min.x() && vectorX <= max.x() &&
                vectorY >= min.y() && vectorY <= max.y() &&
                vectorZ >= min.z() && vectorZ <= max.z();
    }

    public int getWidth() {
        return Math.abs(max.x() - min.x());
    }

    public int getHeight() {
        return Math.abs(max.y() - min.y());
    }

    public int getDepth() {
        return Math.abs(max.z() - min.z());
    }

    public int getVolume() {
        return getWidth() * getHeight() * getDepth();
    }

    public Vector3i[] getCorners() {
        return new Vector3i[]{
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
