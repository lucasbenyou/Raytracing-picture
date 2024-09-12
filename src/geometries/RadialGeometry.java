package geometries;

import primitives.Ray;

import java.util.List;

abstract class RadialGeometry extends Geometry{
    protected double radius;
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxdistance)
    {
        return null;
    }
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
