package geometries;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing an intersectable geometry object.
 * An intersectable object is capable of finding intersections with a given ray.
 */
public abstract class Intersectable {

    /**
     * Finds the intersections between the geometry object and a given ray.
     *
     * @param ray The ray to find intersections with
     * @return A list of intersection points, or null if no intersections exist
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Finds the geometric intersections between the geometry object and a given ray,
     * up to a specified maximum distance.
     *
     * @param ray         The ray to find intersections with
     * @param maxDistance The maximum distance for intersections to be considered
     * @return A list of geometric intersection points, or null if no intersections exist
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Finds the geometric intersections between the geometry object and a given ray.
     *
     * @param ray The ray to find intersections with
     * @return A list of geometric intersection points, or null if no intersections exist
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Helper method for finding the geometric intersections between the geometry object
     * and a given ray, up to a specified maximum distance.
     *
     * @param ray         The ray to find intersections with
     * @param maxDistance The maximum distance for intersections to be considered
     * @return A list of geometric intersection points, or null if no intersections exist
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Class representing a geometric intersection point between a geometry object and a ray.
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point point;

        /**
         * Constructs a GeoPoint object with the specified geometry and point.
         *
         * @param geometry The geometry object
         * @param point    The intersection point
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
