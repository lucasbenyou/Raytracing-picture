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
}
