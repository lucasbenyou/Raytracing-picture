package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import geometries.Geometries;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * This class represents a basic ray tracer implementation.
 */

public class RayTracerBasic extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final Double3 INITIAL_K = Double3.ONE;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.01;
/**
 private boolean unshaded(GeoPoint gp , Vector l, Vector n) {
 Vector lightDirection = l.scale(-1); // from point to light source
 Vector epsVector = n.scale(DELTA);
 Point point = gp.point.add(epsVector);
 Ray lightRay = new Ray(point, lightDirection);
 List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
 return intersections == null;
 }*/


    /**
     * private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
     * double number = light.getDistance(gp.point);
     * Vector lightDirection = l.scale(-1);
     * Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
     * Point point = gp.point.add(epsVector);
     * Ray lightRay = new Ray(point, lightDirection,n);
     * var intersections = scene.geometries.findGeoIntersections(lightRay,number);
     * if (intersections == null) return true;
     * double lightDistance = light.getDistance(gp.point);
     * for (GeoPoint geoPoint : intersections) {
     * if (alignZero(geoPoint.point.distance(gp.point) - lightDistance) <= 0) return false;
     * }
     * return true;
     * }
     */

    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    private Double3 transparency(GeoPoint geoPoint, LightSource lightSource, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1);
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        Double3 ktr = Double3.ONE;
        if (intersections == null) return ktr;

        double distance = lightSource.getDistance(geoPoint.point);

        for (GeoPoint intersection : intersections) {

            if (distance > intersection.point.distance(geoPoint.point)) {
                ktr = ktr.product(intersection.geometry.getMaterial().kT);
            }
        }
        return ktr;
    }


    /**
     *
     * bug of anonymous
     */


    /**
     * Constructs a RayTracerBasic object for the given scene.
     * @param scene The scene to be rendered.
     */


    /**
     * Calculates the color at the given intersection point using the Phong reflection model.
     *
     * @param point The intersection point.
     * @param ray   The ray that intersected the geometry.
     * @return The color at the intersection point.
     */
    private Color calcColor(GeoPoint point, Ray ray) {

        return scene.getAmbientLight().getIntensity()
                .add(calcColor(point, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    private Color calcColor(GeoPoint point, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(point, ray, k);
        return level == 1 ? color
                : color.add(calcGlobalEffects(point, ray, level, k));
    }

    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = gp.geometry.getMaterial();
        Ray reflectedRay = constructReflectedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
        Ray refractedRay = constructRefractedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
        return calcGlobalEffects(gp, level, color, material.kR, k, reflectedRay)
                .add(calcGlobalEffects(gp, level, color, material.kT, k, refractedRay));
    }

    private Color calcGlobalEffects(GeoPoint geoPoint, int level, Color color, Double3 kx, Double3 k, Ray ray) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint reflectedPoint = findClosestIntersection(ray);
        if (reflectedPoint != null) {
            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
        }
        return color;
    }

    public GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> gp = scene.geometries.findGeoIntersections(ray);
        if (gp == null)
            return null;
        return ray.findClosestGeoPoint(gp);
    }


    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray, Double3 k) {
        Vector v = ray.getDir();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(n.dotProduct(v));
        double nl;
        Material material = geoPoint.geometry.getMaterial();
        Color color = geoPoint.geometry.getEmission();
        if (nv == 0) return Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(geoPoint.point);
            nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) {
                Double3 ktr = transparency(geoPoint, lightSource, l, n);

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color intensity = lightSource.getIntensity(geoPoint.point).scale(ktr);
                    color = color.add(intensity.scale(calcDiffusive(material, nl)),
                            intensity.scale(calcSpecular(material, n, l, nl, v)));
                }
            }
        }
        return color;
    }


    /**
     * helper functions for "calcLocalEffects"
     * fun1: calcSpecular.
     * fun2: calcDiffusive.
     */
    private Double3 calcSpecular(Material material, Vector normal, Vector lightVector, double nl, Vector vector) {
        Vector reflectedVector = lightVector.subtract(normal.scale(2 * nl));
        double cosTeta = alignZero(-vector.dotProduct(reflectedVector));
        return cosTeta <= 0 ? Double3.ZERO : material.kS.scale(Math.pow(cosTeta, material.nShininess));

    }

    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }

    private Ray constructReflectedRay(GeoPoint gp, Vector n, Vector v) {
        Vector r = v.subtract(n.scale(2 * v.dotProduct(n)));
        return new Ray(gp.point, r, n);
    }

    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, n, v);
    }
}
