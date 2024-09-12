package primitives;

import java.util.Objects;
import java.util.List;

import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable.GeoPoint;


public class Ray {
    /**
     * two fields that belongs to RAY
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;

    private static final double DELTA = 0.1;

    private Point p0;
    private Vector dir;

    /**
     * implements of RAY:
     */
    public Ray(Point p, Vector di) {
        this.p0 = p;
        Vector n = di.normalize();
        this.dir = n;
    }

    public Ray(Point p0, Vector direction, Vector normal) {
        Vector delta = normal.scale(normal.dotProduct(direction) > 0 ? DELTA : - DELTA);
        this.p0 = p0.add(delta);
        this.dir = direction;
    }

    public Point getP0() {
        return p0;
    }

    public Vector getDir() {
        return dir;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return p0.equals(ray.p0) && dir.equals(ray.dir);
    }

    public Point getPoint(double t) {
        Point p = p0.add(dir.scale(t));
        return p;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(p0, dir);
    }

    /**
     method to help us find the point that is closet to the beginning of the ray
     */
    public Point findClosestPoint(List<Point> pointList){
        return pointList == null || pointList.isEmpty() ? null
                : findClosestGeoPoint(pointList.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
    public GeoPoint findClosestGeoPoint(List<GeoPoint> pl){
        if (pl == null )
            return null;
        GeoPoint closestGeopoint = null;
        double distance = Double.POSITIVE_INFINITY;
        double checker;
        for (GeoPoint p : pl) {
            checker = p.point.distanceSquared(p0);
            if (checker < distance) {
                distance = checker;
                closestGeopoint = p;

            }
        }
        return closestGeopoint;


    }
}