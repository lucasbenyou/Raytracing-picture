package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

public class Cylinder extends Tube{
    private double height;

    public Cylinder(double radius, Ray aR, double height) {
        super(radius, aR);
        this.height = height;
    }

    public double getHeight() {
        return height;
    }


    @Override
    public Vector getNormal(Point point) throws UnsupportedOperationException {

        Point p0 = axisRay.getP0();

        Vector v = axisRay.getDir();



        // Check if the given point is the same as the base point of the axis

        if (point.equals(p0))

            return v;



        // Calculate the projection of (point - p0) onto the axis ray

        Vector u = point.subtract(p0);



        // Calculate the distance from p0 to the object in front of the given point

        double t = Util.alignZero(u.dotProduct(v));



        // If the given point is at the base of the object or at the top of the object

        if (t == 0 || Util.isZero(height - t))

            return v;



        // Calculate the other point on the axis facing the given point

        Point o = p0.add(v.scale(t));



        // Calculate the normalized vector from the given point to the other point on the axis

        return point.subtract(o).normalize();
}
    private GeoPoint findCapIntersection(Ray ray, Point capCenter, double maxDistance) {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();
        Vector va = axisRay.getDir();

        double denominator = va.dotProduct(v);
        if (Util.isZero(denominator))
            return null;                    // the ray is parallel to the disc

        if (p0.equals(capCenter))
            return null;

        double t = Util.alignZero(va.dotProduct(capCenter.subtract(p0)) / denominator);
        if (t <= 0 || Util.alignZero(t - maxDistance) > 0)
            return null;

        Point p = ray.getPoint(t);
        // strictly inside the disc, the border belongs to the side surface
        if (Util.alignZero(p.distanceSquared(capCenter) - radius * radius) >= 0)
            return null;

        return new GeoPoint(this, p);
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance)
    {
        Point pa = axisRay.getP0();
        Vector va = axisRay.getDir();
        Point topCenter = pa.add(va.scale(height));

        List<GeoPoint> intersections = new LinkedList<>();

        // side surface: reuse the Tube computation, then keep only the points
        // whose projection on the axis falls between 0 and the height
        List<GeoPoint> side = super.findGeoIntersectionsHelper(ray, maxDistance);
        if (side != null) {
            for (GeoPoint gp : side) {
                double t = Util.alignZero(va.dotProduct(gp.point.subtract(pa)));
                if (t > 0 && Util.alignZero(t - height) < 0)
                    intersections.add(new GeoPoint(this, gp.point));
            }
        }

        // the two closing discs
        GeoPoint bottom = findCapIntersection(ray, pa, maxDistance);
        if (bottom != null)
            intersections.add(bottom);

        GeoPoint top = findCapIntersection(ray, topCenter, maxDistance);
        if (top != null)
            intersections.add(top);

        return intersections.isEmpty() ? null : intersections;
    }
}

