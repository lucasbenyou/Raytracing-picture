package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: check the normal of Triangle
        Point p1 = new Point(2, 0, 0);
        Point p2 = new Point(-2, 2, 2);
        Point p3 = new Point(0, 2, 1);
        Triangle triangle = new Triangle(p1, p2, p3);
        Vector N = (new Vector(-2, 0, -4)).normalize();
        assertTrue(triangle.getNormal(new Point(-2, 0, -4)).equals(N) || triangle.getNormal(new Point(-2, 0, -4)).equals(N.scale(-1d)),
                "ERROR: Ge normal test isn't working");
        // TC02: check if the vec is normal
        assertEquals(1, triangle.getNormal(p1).length(), 0.0000001, "ERROR: the vec isn't normal");

    }

    @Test
    void testFindIntersections()
    {
        Triangle t = new Triangle(new Point(-3, 0, 0), new Point(-1, 0, 0), new Point(0, 0, 1));
        //======================Boundary value test========================
        // TC01:the origin of the ray is in a vertex
        assertNull(t.findIntersections(new Ray(new Point(-1.8, -2.36, 0), new Vector(-1.2, 2.36, 0))),
                "ERROR:we should not get any intersection");
        //TC02:the origin of the ray is in an edge
        assertNull( t.findIntersections(new Ray(new Point(-1.8, -2.36, 0), new Vector(0.18, 2.36, 0))),
               "ERROR:we should not get any intersection");
        //TC03:the origin of the ray is in the edge continuation
        assertNull( t.findIntersections(new Ray(new Point(0.27, -1.75, 0), new Vector(0.36, 1.75, 0))),
                "ERROR:we should not get any intersection");

        //====================Equivalent Partition tes=====================================================
        //TC01:the origin of the ray is in the triangle
       assertEquals(1, t.findIntersections(new Ray(new Point(-1.12, -1.63, 0), new Vector(-0.15, 1.63, 0.35))).size()
                , "ERROR: test failed we should get one intersection");
        //TC02:the origin of the ray is next to a vertex but not inside the triangle
       assertNull( t.findIntersections(new Ray(new Point(0.27, -1.75, 0), new Vector(-2.08, -0.61, 0)))
                , "ERROR: test failed we should get two intersection");
        //TC03:the origin of the ray is between the continuation of the edge
        assertNull( t.findIntersections(new Ray(new Point(2, -4, 0), new Vector(0.73, 4, 2.79)))
               , "ERROR: test failed we should get two intersection");

    }


}