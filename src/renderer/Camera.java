package renderer;

import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;
import renderer.ImageWriter;
import renderer.RayTracerBase;

import java.util.MissingResourceException;

import static primitives.Util.isZero;


public class Camera {

    private Point location;
    private Vector vRight;
    private Vector vUp;
    private Vector vTo;
    private Double height;
    private Double width;
    private Double distance;
    private ImageWriter imageWriter;

    private RayTracerBase rayTracer;

    public Point getLocation() {
        return location;
    }

    public Vector getvRight() {
        return vRight;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getVto() {
        return vTo;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWidth() {
        return width;
    }

    public Double getDistance() {
        return distance;
    }


    public Camera(Point origin, Vector to, Vector up) {
        if (!isZero(up.dotProduct(to)))
            throw new IllegalArgumentException("Vectors has to be orthogonal");
        location = origin;
        vUp = up.normalize();
        vTo = to.normalize();
        vRight = to.crossProduct(up).normalize();
    }

    public Camera setVPSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Camera setVPDistance(double distance) {
        /**
         * set distance is a setter
         */
        this.distance = distance;
        return this;
    }

    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    public Camera setRayTracer(RayTracerBasic rayTrace) {
        this.rayTracer = rayTrace;
        return this;
    }

    public Ray constructRay(int nX, int nY, int j, int i)
    /**Constructs a ray from Camera location throw the center of a pixel (i,j) in the view plane.
     Params:
     Nx – number of pixels in a row of view plane Ny – number of pixels in a column of view plane
     j – number of the pixel in a row i – number of the pixel in a column
     Returns:
     The ray through pixel's center
     */
    {
        Point Pij = location.add(vTo.scale(distance));
        double Ry = height / nY;
        double Rx = width / nX;
        double Xj = (j - (nX - 1d) / 2) * Rx;
        double Yi = -(i - (nY - 1d) / 2) * Ry;
        if (Xj != 0)
            Pij = Pij.add(vRight.scale(Xj));
        if (Yi != 0)
            Pij = Pij.add(vUp.scale(Yi));
        Vector Vij = Pij.subtract(location);
        return new Ray(location, Vij);

    }

    public Camera renderImage() {
        try {
            if (location == null || vRight == null || vUp == null || vTo == null || width == 0 ||
                    height == 0 || distance == 0 || imageWriter == null || rayTracer == null)
                throw new MissingResourceException("ressource not set", "Camera", "render image");
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException();
        }
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                this.imageWriter.writePixel(j, i, castRay(nX, nY, j, i));
        return this;


    }


    public void printGrid(int Interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("resource not set", "Camera", "print Grid");
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                if (i % Interval == 0 || j % Interval == 0)
                    imageWriter.writePixel(i, j, color);
            }
        }
    }

    private Color castRay(int nX, int nY, int j, int i) {
        return this.rayTracer.traceRay(this.constructRay(nX, nY, j, i));
    }

    public Camera writeToImage()
    {
        if (imageWriter == null)
            throw new MissingResourceException("Image writer cant be empty", "Camera",
                    "writeImage");
        this.imageWriter.writeToImage();
        return this;
    }
}