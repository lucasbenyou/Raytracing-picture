package geometries;

import primitives.Point;
import primitives.Ray;
import geometries.Intersectable.GeoPoint;
import java.util.*;


public class Geometries extends Intersectable {

    List<Intersectable> ourlist;

    public Geometries() {
        ourlist = new LinkedList<>();

    }

    public Geometries(Intersectable... geometries) {
        this.ourlist=List.of(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(ourlist,geometries);
    }

   /* public void add(Intersectable... geometries) {
        if (geometries.length != 0)
            this.ourlist.addAll(Arrays.asList(geometries));
    }*/

    /**
     * @Override public List<Point> findIntersections(Ray ray) {
     * List<Point> result= new LinkedList<Point>();
     * for(Intersectable item: ourlist)
     * {
     * List<Point> itemResult= item.findIntersections(ray);
     * if (itemResult!= null)
     * result.addAll(itemResult);
     * }
     * if(result.isEmpty())
     * return null;
     * return result;
     * }
     */

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {
        List<GeoPoint> intersections = null;
        for (Intersectable geometry : ourlist) {
            var geometryIntersections = geometry.findGeoIntersections(ray);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections= new LinkedList<>(geometryIntersections);
                else
                 intersections.addAll(geometryIntersections);

            }

        }

        return intersections;
    }


}
