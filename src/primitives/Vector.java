package primitives;
/**
 Vector class present a vector that succesor from Point class
 in the Vector class we are doing some algebric calculates.
   */
public class Vector  extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector can't be 0");
    }

    Vector(Double3 xyz) {
        this(xyz.d1, xyz.d2, xyz.d3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return xyz.equals(vector.xyz);
    }


    public double lengthSquared() {
        double dx = xyz.d1;
        double dy = xyz.d2;
        double dz = xyz.d3;
        return dx * dx + dy * dy + dz * dz;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize() {
        double len = length();
        return new Vector(xyz.reduce(len));
    }

    public double dotProduct(Vector v1) {
        return (xyz.d1 * v1.xyz.d1 + xyz.d2 * v1.xyz.d2 + xyz.d3 * v1.xyz.d3);
    }


    public Vector crossProduct(Vector v2) {
        double dx = xyz.d2 * v2.xyz.d3 - xyz.d3 * v2.xyz.d2;
        double dy = xyz.d3 * v2.xyz.d1 - xyz.d1 * v2.xyz.d3;
        double dz = xyz.d1 * v2.xyz.d2 - xyz.d2 * v2.xyz.d1;
        return new Vector(dx, dy, dz);
    }

    public Vector scale(double scalar) {
        if (scalar == 0)
            throw new IllegalArgumentException("ERROR: can't be 0");
        double dx = xyz.d1 * scalar;
        double dy = xyz.d2 * scalar;
        double dz = xyz.d3 * scalar;
        return new Vector(dx, dy, dz);

    }

    public Vector add(Vector v) {
        double dx = xyz.d1 + v.xyz.d1;
        double dy = xyz.d2 + v.xyz.d2;
        double dz = xyz.d3 + v.xyz.d3;
       return new Vector(dx, dy, dz);
        // Vector v1 = new Vector(dx,dy,dz);
       // if (v1.equals(new Vector(Double3.ZERO))){
           // throw new IllegalArgumentException("ERROR: add of vectors can't be 0");
        //}
       // return v1;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "xyz=" + xyz +
                '}';
    }
}

