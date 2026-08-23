package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.*;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Tube extends RadialGeometry {
 protected final Ray axisRay;

    @Override
    public Vector getNormal(Point p) {
       Point p0= axisRay.getP0();
       Vector v1 = axisRay.getDir();
       Vector po_p = p.subtract(p0);
       double t = alignZero(v1.dotProduct(po_p));

       if (isZero(t))
           return po_p.normalize();

       Point o = p0.add(v1.scale(t));
       if (p.equals(o))
           throw new IllegalArgumentException("The point can not be on the axis");
       Vector N = p.subtract(o).normalize();
       return N;
    }

    public Tube(double radius, Ray aR) {
        super(radius);
        this.axisRay = aR;
    }

    public Ray getAxisRay() {
        return axisRay;
    }
     @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance)
    {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();
        Point pa = axisRay.getP0();
        Vector va = axisRay.getDir();

       
        double vx = v.getX(), vy = v.getY(), vz = v.getZ();
        double ax = va.getX(), ay = va.getY(), az = va.getZ();

       
        double dx = p0.getX() - pa.getX();
        double dy = p0.getY() - pa.getY();
        double dz = p0.getZ() - pa.getZ();

        double vva = vx * ax + vy * ay + vz * az;   // v . va
        double dva = dx * ax + dy * ay + dz * az;   // deltaP . va

        // once the part along the axis is removed, it is a line/circle problem,
        // so we solve a quadratic equation A*t^2 + B*t + C = 0
        double A = alignZero(1 - vva * vva);        // dir is already normalized
        if (isZero(A))
            return null;                            // the ray is parallel to the axis

        double B = 2 * ((vx * dx + vy * dy + vz * dz) - vva * dva);
        double C = (dx * dx + dy * dy + dz * dz) - dva * dva - radius * radius;

        double discriminant = alignZero(B * B - 4 * A * C);
        if (discriminant <= 0)
            return null;                            // no intersection, or tangent

        double sqrtD = Math.sqrt(discriminant);
        double t1 = alignZero((-B - sqrtD) / (2 * A));
        double t2 = alignZero((-B + sqrtD) / (2 * A));

        List<GeoPoint> intersections = new LinkedList<>();
        if (t1 > 0 && alignZero(t1 - maxDistance) <= 0)
            intersections.add(new GeoPoint(this, ray.getPoint(t1)));
        if (t2 > 0 && alignZero(t2 - maxDistance) <= 0)
            intersections.add(new GeoPoint(this, ray.getPoint(t2)));

        return intersections.isEmpty() ? null : intersections;
    }
}
