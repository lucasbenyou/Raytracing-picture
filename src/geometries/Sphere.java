package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import geometries.Intersectable.GeoPoint;
import static java.lang.Math.sqrt;
import static primitives.Util.alignZero;

/**
 * successor from Radial Geometry represent a 3D
 */
public class Sphere extends RadialGeometry {
    private Point center;

    /**
     * the center Point
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    public Point getCenter() {
        return center;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        if (P0.equals(center)) {
            if(alignZero(this.radius-maxDistance)<=0) {
                return List.of(new GeoPoint(this, ray.getPoint(this.radius)));
            }
            return List.of(new GeoPoint(this,center.add(v.scale(radius))));
        }

        Vector U = center.subtract(P0);
        double tm = alignZero(v.dotProduct(U));
        double d = alignZero(Math.sqrt(U.lengthSquared() - tm * tm));

        // no intersections : the ray direction is above the sphere
        if (d >= radius) {
            return null;
        }

        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if (t1 > 0 && t2 > 0) {

            Point P1 = ray.getPoint(t1);
            Point P2 = ray.getPoint(t2);
            return List.of(new GeoPoint(this,P1),new GeoPoint(this, P2));
        }
        if (t1 > 0) {
            Point P1 = ray.getPoint(t1);
            return List.of(new GeoPoint(this,P1));
        }
        if (t2 > 0) {
            Point P2 = ray.getPoint(t2);
            return List.of(new GeoPoint(this,P2));
        }
        return null;
    }

    @Override
    public Vector getNormal(Point p) {
        Vector n = p.subtract(center).normalize();
        return n;
    }
}
