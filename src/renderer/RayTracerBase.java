package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

/**
 * abstract class
 */
public abstract class RayTracerBase {
    protected Scene scene;
    /**
     * constructor with parameter
     */
    public RayTracerBase(Scene scene)
    {
        this.scene = scene;
    }
    /**
     * abstract class we are not impliment it here "virtual"
     */
    public abstract Color traceRay(Ray r);
}
