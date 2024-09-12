package renderer;

import geometries.Intersectable;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.Camera;
import geometries.Triangle;
import geometries.Plane;
import geometries.Sphere;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationRayCamera {

    public double pointsNumber(Camera c, Intersectable I) {
        int sum = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                var help = I.findIntersections(c.constructRay(3, 3, j, i));
                if (help != null)
                    sum += help.size();
            }
        }
        return sum;
    }

    @Test
    public void integrationRay() {
        //we will use camera cs1 for the plane and the triangle due to the fact that they use the same camera
        //=================building for sphere======================================================================
        Sphere s1 = new Sphere(new Point(0, 0, -3),1);//2
        Camera cs1 = new Camera(new Point(0, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1).setVPSize(3, 3);
        Sphere s2 = new Sphere( new Point(0, 0, -2.5),2.5);//18
        Camera cs2 = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1).setVPSize(3, 3);
        Sphere s3 = new Sphere(new Point(0, 0, -2),2);//10
        Camera cs3 = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1).setVPSize(3, 3);
        Sphere s4 = new Sphere(new Point(0, 0, 1),4);
        Camera cs4 = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1).setVPSize(3, 3);
        Sphere s5 = new Sphere(new Point(0, 0, 1),0.5);//0
        Camera cs5 = new Camera(new Point(0, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1).setVPSize(3, 3);
        //==================building for plane===================================================================
        Plane p1 = new Plane(new Point(0, 0, -3), new Vector(0, 0, -1));//9
        Plane p2 = new Plane(new Point(0, 0, -2), new Vector(0, 2, 3));//9
        Plane p3 = new Plane(new Point(0, 0, -3), new Vector(1, 0, -1));//6
        //=================building for triangle=================================================================
        Triangle t1 = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));//1
        Triangle t2 = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));//2
        //===================================TESTS================================================================
        //===================================SPHERE=============================================================
        //
        //TC01 for the sphere with 2 intersections points
        assertEquals(2, pointsNumber(cs1, s1), "Sphere must be 2");
        //TC02 for the sphere with 18 intersections points
        assertEquals(18, pointsNumber(cs2, s2), "sphere must be 18");
        //TC03 for the sphere with 10 intersections points
        assertEquals(10, pointsNumber(cs3, s3), "sphere must be 10");
        //TC04 for the sphere with 0 intersection point
        assertEquals(9, pointsNumber(cs4, s4), "sphere must be 9");
        //TC05 for the sphere with 0 intersection point
        assertEquals(0, pointsNumber(cs5, s5), "sphere must be 0");
        //=================================PLANE===================================================================
        //TC01 for the plane with 9 intersections points
        assertEquals(9, pointsNumber(cs1, p1), "plane must be 9");
        //TC02 for the plane with 9 intersections points
        assertEquals(9, pointsNumber(cs1, p2), "plane must be 9");
        //TC03 for the plane with 6 intersections points
        assertEquals(6, pointsNumber(cs1, p3), "plane must be 6");
        //==================================TRIANGLE==================================================
        //TC01 for the triangle with 1 intersections points
        assertEquals(1, pointsNumber(cs1, t1), "triangle must be 1");
        //TC02 for the triangle with 2 intersections points
        assertEquals(2, pointsNumber(cs1, t2), "triangle must be 2");
    }

}
