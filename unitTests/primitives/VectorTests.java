package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class VectorTests {

    /**
     * Test method for{@link primitives.Vector#equals(Object)}
     */
    @Test
    void testTestEquals() {
        Vector v1 = new Vector(1, 2, 3);
        assertTrue(v1.equals(new Vector(1, 2, 3)),
                "ERROR: Equals test failed");
    }

    /**
     * Test method for{@link Vector#lengthSquared()}
     */
    @Test
    void testLengthSquared() {
        //============================Equivalence Partititons Tests==========================================
        assertEquals(14d, new Vector(1, 2, 3).lengthSquared(), 0.0001,
                "ERROR: length squared test failed");
    }

    /**
     * Test method for{@link Vector#length()}
     */
    @Test
    void testLength() {
        //============================Equivalence Partititons Tests==========================================
        assertEquals(3d, new Vector(2, 2, 1).length(), 0.000001,
                "ERROR: Length test isn't working");
    }

    /**
     * Test method for{@link Vector#normalize()}
     */
    @Test
    void testNormalize() {
        Vector v1 = new Vector(2, 2, 1);
        Vector n = v1.normalize();
        //============================Equivalence Partititons Tests==========================================
        assertEquals(1d, n.length(), 0.00001, "ERROR: Normalize test failed");


    }

    /**
     * Test method for{@link primitives.Vector#dotProduct(Vector)}
     */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        //============================Equivalence Partititons Tests==========================================
        assertEquals(17, v1.dotProduct(new Vector(1, 2, 4)), 0.00001,
                "ERROR: Dot Product isn't working");
        // =============== Boundary Values Tests ==================
        assertEquals(0, v1.dotProduct(new Vector(0, 3, -2)), 0.00001,
                "ERROR: the Dot Product test isn't working");
    }


    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    public void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v2);
        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals(v1.length() * v2.length(), vr.length(), 0.00001, "crossProduct() wrong result length");
        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");

        assertTrue(isZero(vr.dotProduct(v2)), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-product of co-lined vectors
        Vector v3 = new Vector(-2, -4, -6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3),
                "crossProduct() for parallel vectors does not throw an exception");
    }


    /**
     * Test method for{@link primitives.Vector#scale(double)}
     */
    @Test
    void testScale() {
        Vector v1 = new Vector(2, 3, 4);
        assertEquals(new Vector(4, 6, 8), v1.scale(2),
                "ERROR: Scale isn't working");
        // ============ Equivalence Partitions Tests ==============
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 5, 10).scale(0d));
    }

    /**
     * Test method for{@link primitives.Vector#add(Vector)}
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(2, 3, 4);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(1, 1, 1), v1.add(new Vector(-1, -2, -3)),
                "ERROR: Add Test isn't working");
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 1, 1).add(new Vector(-1, -1, -1)));

    }

    /**
     * Test method for{@link primitives.Vector#subtract(Point)}
     */
    @Test
    void testSubtract() {
        Vector v1 = new Vector(2, 3, 4);
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(1, 1, 1), v1.subtract(new Vector(1, 2, 3)),
                "ERROR: Subtract isn't working");
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 1, 1).subtract(new Vector(1, 1, 1)));
    }
}