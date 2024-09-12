package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        Point a = new Point(1, 0, 0),
                b = new Point(2, 0, 0),
                c = new Point(3, 0, 0);

        List<Point> lst1 = List.of(a, b, c);
        List<Point> lst2 = List.of();
        List<Point> lst3 = List.of(b, c, a);
        List<Point> lst4 = List.of(c, a, b);
        // ============ Equivalence Partitions Tests ==============
        // TC01: The middle point
        assertEquals(a, ray.findClosestPoint(lst4), "TC01: The middle point test failed");

        // =============== Boundary Values Tests ==================
        // TC01: Empty set
        assertNull(ray.findClosestPoint(lst2), "TC01: Empty  test failed");

        // TC02: First point
        assertEquals(a, ray.findClosestPoint(lst1), "TC02: First point  test failed");

        // TC03: Last point
        assertEquals(a, ray.findClosestPoint(lst3), "TC03: Last point test failed");
    }
}