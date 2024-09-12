package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTests {

    @Test
    void testGetNormal() {
        Sphere sphere = new Sphere(new Point(0,0,0),5);
        //============ Equivalence Partitions Tests ==============
        //TC01: tests to check if the get normal is working
        assertEquals(new Vector(0,0,1), sphere.getNormal(new Point(0,0,5)),
                "ERROR: sphere get normal test isn't working ");

        // TC02: test to check if the Vector is normal
        assertEquals(1,sphere.getNormal(new Point(1,1,0)).length(),0.0001,
                "ERROR: get normal test isn't working");
    }
    @Test
    void testFindIntersections()
    {
        Sphere sphere = new Sphere(new Point(-3,0,0),1);
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray does not intersect the sphere.
        assertNull(sphere.findIntersections(new Ray(new Point(3,0,0),new Vector(-1,0,1))), "Ray that doesn't intersect sphere EP doesn't work.");

        // TC02: Ray intersects the sphere twice.
        List<Point> res = sphere.findIntersections(new Ray(new Point(3,0,0),new Vector(-1,0,0)));
        assertEquals(res.size(), 2, "Ray intersects sphere twice EP doesn't work.");


        // TC03: Ray intersects the sphere from inside the sphere.
        res = sphere.findIntersections(new Ray(new Point(-3.5,0,0),new Vector(-1,0,0)));
        assertEquals(res.size(), 1, "Ray from inside sphere EP doesn't work.");



        // TC04: Ray goes to the opposite direction of the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(3,0,0),new Vector(1,0,0))), "Ray in opposite dir of sphere EP doesn't work.");


        // =============== Boundary Values Tests ================
        // TC05: Ray starts at sphere and goes inside
        res = sphere.findIntersections(new Ray(new Point(-4,0,0),new Vector(1,0,1)));
        assertEquals(res.size(), 1, "Ray from the sphere inwards BVA doesn't work.");

        // TC06: Ray starts at sphere and goes outside
        assertNull(sphere.findIntersections(new Ray(new Point(-4,0,0),new Vector(-1,0,-1))), "Ray from the sphere outwards BVA doesn't work.");

        // TC:07 Ray starts before the sphere
        res = sphere.findIntersections(new Ray(new Point(-5,0,0),new Vector(1,0,0)));
        assertEquals(res.size(), 2, "Ray through center 2 points BVA doesn't work.");


        // TC08: Ray starts at sphere and goes inside
        res = sphere.findIntersections(new Ray(new Point(-4,0,0),new Vector(1,0,0)));
        assertEquals(res.size(), 1, "Ray on sphere through center inwards BVA doesn't work.");

        // TC09: Ray starts inside after the center
        res = sphere.findIntersections(new Ray(new Point(-2.5,0,0),new Vector(1,0,0)));
        assertEquals(res.size(), 1, "Ray in sphere through center BVA doesn't work.");


        // TC10: Ray starts at the center
        res = sphere.findIntersections(new Ray(new Point(-3,0,0),new Vector(1,0,0)));
        assertEquals(res.size(), 1, "Ray from center BVA doesn't work.");


        // TC11: Ray starts at sphere and goes outside
        res = sphere.findIntersections(new Ray(new Point(-4,0,0),new Vector(-1,0,0)));
        assertNull(res, "Ray on sphere through center outwards BVA doesn't work.");

        // TC12: Ray starts after sphere
        res = sphere.findIntersections(new Ray(new Point(-1,0,0),new Vector(1,0,0)));
        assertNull(res, "Ray out of sphere through center BVA doesn't work.");


        // TC13: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0,0,1),new Vector(-1,0,0))), "Ray tangent to the sphere BVA doesn't work.");

        // TC14: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(-3,0,1),new Vector(-1,0,0))), "Ray tangent to the sphere BVA doesn't work.");

        // TC15: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(-4,0,1),new Vector(-1,0,0))), "Ray tangent to the sphere BVA doesn't work.");


    }
}