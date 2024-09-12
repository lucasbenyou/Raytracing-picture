package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        return null;
    }
}
