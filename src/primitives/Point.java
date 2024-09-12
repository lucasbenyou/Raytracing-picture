package primitives;

/**
 * point class present a point of three points, we calculate distance and will help by it in the other classes
 */

import java.util.Objects;
import java.util.Objects;

import static primitives.Util.isZero;

public class Point {

    final Double3 xyz;
    public final static Point ZERO = new Point(0, 0, 0);

    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    Point(Double3 xyz) {
        this.xyz = new Double3(xyz.d1, xyz.d2, xyz.d3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        //return isZero(this.xyz.d1-point.xyz.d1)&& (this.xyz.d2-point.xyz.d2)&&(this.xyz.d3-point.xyz.d3);
        return xyz.equals(point.xyz);
    }


    @Override
    public String toString() {
        return "Point: " + xyz;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xyz);
    }

    public Vector subtract(Point p) {
        if (this.equals(p)) {
            throw new IllegalArgumentException("Error cant be the same value ");
        }
        return new Vector(xyz.subtract(p.xyz));
    }

    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    public double distanceSquared(Point other) {
        double dx = xyz.d1 - other.xyz.d1;
        double dy = xyz.d2 - other.xyz.d2;
        double dz = xyz.d3 - other.xyz.d3;
        return dx * dx + dy * dy + dz * dz;
    }

    //Getters
    public Double getX() {
        return xyz.d1;
    }

    public Double getY() {
        return xyz.d2;
    }

    public Double getZ() {
        return xyz.d3;
    }



}
