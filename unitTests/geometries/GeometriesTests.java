package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class GeometriesTests {

    @Test
    void findIntersections() {
        Triangle triangle = new Triangle(new Point(0.81486,1.38087,0), new Point(0.95156,4.48537,0), new Point(-0.88168,4.58488,0));
        Sphere sphere = new Sphere(new Point(0,0,0.2),1);
        Plane plane = new Plane(new Point(-3.07591,-1.36608,0), new Point(-1.58192,4.18223,0), new Point(-4.15138,1.01425,-2.21357));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that findIntersections work currently when the ray intersect some shapes
        Geometries ge = new Geometries(triangle, sphere, plane);
        assertEquals(3,ge.findIntersections(new Ray(new Point(3,0,0), new Vector(-1,0,0))).size(),
                "The findIntersections did`nt work currently when the ray intersect some shapes");


        // =============== Boundary Values Tests ==================
        // TC11: Test that findIntersections returns NULL when there is no Geometries
        Geometries geo = new Geometries();
        assertNull(geo.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0.25, 0, -1))),
                "The findIntersections did`nt return NULL when there is no Geometries");

        // TC12: Test that findIntersections returns NULL when there is no intersections on the Geometries
        Geometries geom = new Geometries(triangle, sphere);
        assertNull(geom.findIntersections(new Ray(new Point(3,0,0), new Vector(-1.27,-1.41,0))),
                "The findIntersections did`nt return NULL when there is no intersections on the Geometries");

        // TC13: Test that findIntersections work currently when the ray intersect just one shape
        assertEquals(2,geom.findIntersections(new Ray(new Point(3,0,0), new Vector(-1,0,0))).size(),
                "The findIntersections did`nt work currently when the ray intersect just one shape");

        // TC14: Test that findIntersections work currently when the ray intersect all shapes
        assertEquals(4,ge.findIntersections(new Ray(new Point(1.79961,4.57061,-0.28315), new Vector(-1.12,-1.84,0.28))).size(),
                "The findIntersections did`nt work currently when the ray intersect all shapes");
    }

}
