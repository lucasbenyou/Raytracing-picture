package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Test method for{@link primitives.Point#equals(Object)}
 */
class PointTest {
    @Test
    void testTestEquals(){
        Point p = new Point(2, 4, 6);
        //============================Equivalence Partititons Tests==========================================
        assertTrue(p.equals(new Point(2,4,6)),
                "ERROR: Equals test failed");

    }
    /**
     *  Test method for{@link primitives.Point#subtract(Point)}
     */
    @Test
    void testSubtract() {
        //============================Equivalence Partititons Tests==========================================
        assertThrows(IllegalArgumentException.class,
                () -> new Point(1, 1, 1).subtract(new Point(1, 1, 1)));
    }



    /**
     * Test method for{@link primitives.Point#add(Vector)}
     */
    @Test
    void testAdd() {
        Point p1 = new Point(1, 2, 3);
        //============================Equivalence Partititons Tests==========================================
        assertEquals(new Point(0, 0, 0),
                p1.add(new Vector(-1, -2, -3)),
                "ERROR: Point + Vector does not work correctly");
    }

    /**
     *  Test method for{@link primitives.Point#distance(Point)}
     */
    @Test
    void testDistance() {
        Point p = new Point(1, 1, 1);
//============================Equivalence Partititons Tests==========================================
        assertEquals(Math.sqrt(14),
                p.distance(new Point(2, 3, 4)),
                "ERROR: distance test failed");
    }

    /**
     * *Test method for{@link primitives.Point#distanceSquared(Point)}
     */
    @Test
    void testDistanceSquared() {
        Point p = new Point(1, 1, 1);
//============================Equivalence Partititons Tests==========================================
        assertEquals(14,
                p.distanceSquared(new Point(2, 3, 4)),
                "ERROR: distanceSquared test failed");
    }
}