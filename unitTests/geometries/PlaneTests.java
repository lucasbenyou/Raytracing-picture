package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {
    @Test
    void testConstructor() {
        Point p1 = new Point(1, 2, 4);
        Point p2 = new Point(2, 4, 6);
        Point p3 = new Point(2, 4, 6);
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p2, p3));

        Point q1 = new Point(1, 4, 4);
        Point q2 = new Point(2, 4, 6);
        Point q3 = new Point(2, 4, 6);
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(q1, q2, q3));

    }

    @Test
    void testGetNormal() {
        //============ Equivalence Partitions Tests ==============
        //TC:01 diff points
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(0, 1, 1);
        Point p3 = new Point(1, 0, 0);
        Plane plane = new Plane(p1, p2, p3);
        Vector N = (new Vector(-1, -3, 2)).normalize();
        assertTrue(plane.getNormal().equals(N) || plane.getNormal().equals(N.scale(-1d)));

        //TC02: if the vector is already normal.
        assertEquals(1, plane.getNormal().length(), 0.0001,
                "ERROR: the vec isn't normal");

    }

    @Test
    void testFindIntersections() {


        Point p1 = new Point(-2, 0, 0);
        Point p2 = new Point(0, 2, 0);
        Point p3 = new Point(0, 0, 1.24);
        Point p4 = new Point(2.92,-3.82,1.85);
        Vector N = new Vector(0,0,2) ;
        Plane plane = new Plane(p1,p2,p3);
        Plane plane1 = new Plane(new Point(0,0,1),new Point(0,0,3),new Point(0,3,0));
        // =================================== Boundary value test ====================================================
        //TC01: the ray is in the plan
        assertNull(plane.findIntersections(new Ray(new Point(0,0,2),new Vector(2,0,2))),
                "Error : test is not working when the ray is inside");
        //TC02: the origin of the ray is outside and the ray is parallel to the plan
        assertNull(plane1.findIntersections(new Ray(p4,new Vector(2,0,2))),
               "Error : test is not working when it is parallel");
        //TC03:is the origin of the ray is outside and the ray is orthogonal to the plan and the origin of the plan is before
        assertEquals(1,plane.findIntersections(new Ray(p4,new Vector(-1.24,1.24,2.01))).size(),
               "Error : test is not working when it is orthonogal and just before the plan");
        //TC04:the ray is  orthogonal to the plane and it's origin is inside the plan
        assertEquals(1,plane.findIntersections(new Ray(new Point(1.68,-2.57,3.86),new Vector(-1.09,1.09,1.77))).size(),
               "Error : test is not working when it is orthonogal and in the plan");
        //TC05:the origin of the ray is outside and the ray is orthogonal to the plan and the origin of the plan is before
        assertNull(plane.findIntersections(new Ray(new Point(1.14,-2.04,4.73),new Vector(-1.09,1.09,1.77))),
                "Error : test is not working when it is orthonogal and in the plan");
        //TC06:the point is in the plane, but it is not orthogonal or parallel
        assertNull(plane.findIntersections(new Ray(new Point(-0.55,-1.82,2.02),
                new Vector(-1.78,-1.21,-2.02))),
                "ERROR: test failed when origin is in the plan and the ray is neither orthogonal nor parallel");
        //TC07:the origin is one of the point that we used to create the plane
        assertNull(plane.findIntersections(new Ray(new Point(0,0,1.24),
                new Vector(2.92,-3.82,0.62))),
                "ERROR: test failed when origin is in the plan and the ray is neither orthogonal nor parallel");
        // =================================== Equivalent Partition Test ============================================
        //TC01:the ray intersect the plane
        assertEquals(1,plane.findIntersections(new Ray(p4,
                new Vector(-5.16,6.3,-1.85))).size(),
                "ERROR:test failed when the ray intersect the plane");


    }

}