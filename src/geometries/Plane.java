package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;
import java.util.List;
import geometries.Intersectable.GeoPoint;

public class Plane extends Geometry {
    private final Point q0;

    private final Vector normal;

    public Plane(Point q0, Vector vector) {
        this.q0 = q0;
        if(vector.length()!=0)
            vector.normalize();
        this.normal = vector;
    }

    public Plane (Point p1, Point p2, Point p3)
    {
        this.q0 = p1;
        Vector U = p1.subtract(p2);
        Vector V = p1.subtract(p3);
        Vector N = U.crossProduct(V);

        this.normal = N.normalize();

    }
    @Override
    public Vector getNormal(Point v) {
        // return getNormal();
        return normal;
    }

    public Point getQ0() {
        return q0;
    }

    public Vector getNormal() {
        return normal;
    }



    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxdistance)
    {
       Point p1 = ray.getP0();
        Vector v1 = ray.getDir();
        double nQMinusP0;
        double t;
        double nv ;
        nv = normal.dotProduct(v1);
        if(isZero(nv)|| q0.equals(p1))
            return null;
         nQMinusP0 = normal.dotProduct(q0.subtract(p1));
        t = alignZero(nQMinusP0 / nv);

        return (t <= 0&&alignZero(t-maxdistance)<=0 )? null : List.of(new GeoPoint(this, ray.getPoint(t)));


    }


}