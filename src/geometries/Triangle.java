package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

public class Triangle extends Polygon{
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance)
    {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();
        List<GeoPoint> intersections = plane.findGeoIntersectionsHelper(ray,maxDistance);

        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = v.dotProduct(n1);  // s = sign
        double s2 = v.dotProduct(n2);
        double s3 = v.dotProduct(n3);
        if(intersections==null)
            return null;
        if((s1<0 && s2<0 && s3<0 )||(s1>0 && s2>0 && s3>0)) {
            intersections.get(0).geometry = this;
            return intersections;
        }
        return null;
    }



    public Triangle(Point... vertices) {
        super(vertices);
    }
}
