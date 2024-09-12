package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTests {
    @Test
    void testGetNormal() {
        Tube tube = new Tube(1, new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)));
        //============ Equivalence Partitions Tests ==============
        //TC01: check if the get normal is working.
        assertEquals(new Vector(0, 0, 1), tube.getNormal(new Point(1, 0, 1)),
                "ERROR: get normal of tube  isn't working");
        // TC02: if its already normal the vec.
        assertEquals(1, tube.getNormal(new Point(2, 1, 0)).length(), 0.0001,
                "ERROR: get normal isn't working");
        //============ Boundary values Tests ==============
        assertThrows(IllegalArgumentException.class, () -> {
                    tube.getNormal(new Point(0, 0, 0));
                },
                "ERROR can't be vector 0,0,0");
    }
}