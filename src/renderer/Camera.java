package renderer;

import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.MissingResourceException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

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

    /**
     * Anti-crenelage "classique" : nombre de rayons par cote de pixel.
     * 1 = un seul rayon au centre du pixel (comportement d'origine),
     * 3 = grille de 3x3 = 9 rayons par pixel, etc.
     */
    private int antiAliasing = 1;

    /**
     * Anti-crenelage adaptatif : profondeur maximale de subdivision du pixel.
     * 0 = desactive. Quand il est actif, il remplace la grille reguliere :
     * on n'echantillonne finement que les pixels ou la couleur change
     * (les aretes), ce qui coute beaucoup moins cher.
     */
    private int adaptiveDepth = 0;

    /** Ecart maximal (0-255, par canal) en dessous duquel deux couleurs sont jugees identiques. */
    private static final int ADAPTIVE_TOLERANCE = 6;

    /** Nombre de fils d'execution : 1 = mono-thread, 0 = tous les coeurs disponibles. */
    private int threadsCount = 1;

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

    /**
     * Active l'anti-crenelage par super-echantillonnage regulier.
     *
     * @param samplesPerSide nombre de rayons par cote de pixel (1 = desactive,
     *                       3 donne 9 rayons par pixel, 4 en donne 16...)
     * @return la camera elle-meme (chainage)
     */
    public Camera setAntiAliasing(int samplesPerSide) {
        if (samplesPerSide < 1)
            throw new IllegalArgumentException("Il faut au moins 1 rayon par pixel");
        this.antiAliasing = samplesPerSide;
        return this;
    }

    /**
     * Active l'anti-crenelage adaptatif : le pixel n'est subdivise que si ses
     * quatre coins n'ont pas la meme couleur (donc uniquement sur les aretes).
     *
     * @param depth profondeur maximale de subdivision (0 = desactive, 2 ou 3 suffisent)
     * @return la camera elle-meme (chainage)
     */
    public Camera setAdaptiveAntiAliasing(int depth) {
        if (depth < 0)
            throw new IllegalArgumentException("La profondeur ne peut pas etre negative");
        this.adaptiveDepth = depth;
        return this;
    }

    /**
     * Active le rendu multi-thread (une ligne d'image par tache).
     *
     * @param threads nombre de fils (1 = mono-thread, 0 = tous les coeurs de la machine)
     * @return la camera elle-meme (chainage)
     */
    public Camera setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Nombre de threads invalide");
        this.threadsCount = threads == 0 ? Runtime.getRuntime().availableProcessors() : threads;
        return this;
    }

    /**
     * Rayon passant par un point quelconque de la vue, exprime en coordonnees
     * continues de pixels : (0,0) est le coin superieur gauche de l'image et
     * (nX, nY) son coin inferieur droit. Le centre du pixel (j,i) est donc
     * (j + 0.5, i + 0.5).
     *
     * @param nX nombre de pixels par ligne
     * @param nY nombre de pixels par colonne
     * @param u  abscisse continue dans [0, nX]
     * @param v  ordonnee continue dans [0, nY]
     * @return le rayon partant de la camera et passant par ce point
     */
    public Ray constructRayThroughPoint(int nX, int nY, double u, double v) {
        Point Pij = location.add(vTo.scale(distance));
        double Ry = height / nY;
        double Rx = width / nX;
        double Xj = (u - nX / 2d) * Rx;
        double Yi = -(v - nY / 2d) * Ry;
        if (!isZero(Xj))
            Pij = Pij.add(vRight.scale(Xj));
        if (!isZero(Yi))
            Pij = Pij.add(vUp.scale(Yi));
        Vector Vij = Pij.subtract(location);
        return new Ray(location, Vij);
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
        return constructRayThroughPoint(nX, nY, j + 0.5, i + 0.5);
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

        // en mode adaptatif, les coins de pixels sont partages par 4 pixels voisins :
        // on les memorise pour ne lancer qu'un rayon par coin
        Color[][] corners = adaptiveDepth > 0 ? new Color[nX + 1][nY + 1] : null;

        if (threadsCount <= 1) {
            for (int i = 0; i < nY; ++i)
                for (int j = 0; j < nX; ++j)
                    imageWriter.writePixel(j, i, pixelColor(nX, nY, j, i, corners));
            return this;
        }

        // rendu parallele : une ligne de l'image par tache
        ForkJoinPool pool = new ForkJoinPool(threadsCount);
        try {
            pool.submit(() -> IntStream.range(0, nY).parallel().forEach(i -> {
                for (int j = 0; j < nX; ++j)
                    imageWriter.writePixel(j, i, pixelColor(nX, nY, j, i, corners));
            })).join();
        } finally {
            pool.shutdown();
        }
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

    /**
     * Couleur d'un pixel, selon la strategie d'echantillonnage choisie.
     */
    private Color pixelColor(int nX, int nY, int j, int i, Color[][] corners) {
        // 1) anti-crenelage adaptatif
        if (adaptiveDepth > 0)
            return adaptive(nX, nY, j, i, 1d, adaptiveDepth,
                    corner(corners, nX, nY, j, i),
                    corner(corners, nX, nY, j + 1, i),
                    corner(corners, nX, nY, j, i + 1),
                    corner(corners, nX, nY, j + 1, i + 1));

        // 2) super-echantillonnage regulier : grille de n x n rayons dans le pixel
        if (antiAliasing > 1) {
            Color sum = Color.BLACK;
            for (int r = 0; r < antiAliasing; r++)
                for (int c = 0; c < antiAliasing; c++)
                    sum = sum.add(colorAt(nX, nY,
                            j + (c + 0.5) / antiAliasing,
                            i + (r + 0.5) / antiAliasing));
            return sum.reduce(antiAliasing * antiAliasing);
        }

        // 3) un seul rayon au centre du pixel
        return colorAt(nX, nY, j + 0.5, i + 0.5);
    }

    /** Couleur obtenue en lancant un rayon a travers le point continu (u,v). */
    private Color colorAt(int nX, int nY, double u, double v) {
        return rayTracer.traceRay(constructRayThroughPoint(nX, nY, u, v));
    }

    /** Couleur d'un coin de pixel, calculee une seule fois puis memorisee. */
    private Color corner(Color[][] cache, int nX, int nY, int u, int v) {
        Color c = cache[u][v];
        if (c == null) {
            c = colorAt(nX, nY, u, v);
            cache[u][v] = c;
        }
        return c;
    }

    /**
     * Super-echantillonnage adaptatif d'un carre de la vue.
     * Si les quatre coins ont pratiquement la meme couleur, on considere que le
     * carre est uniforme et on renvoie leur moyenne ; sinon on le decoupe en
     * quatre et on recommence (les couleurs deja calculees sont reutilisees).
     *
     * @param u,v   coin superieur gauche du carre (coordonnees continues de pixels)
     * @param size  cote du carre
     * @param depth profondeur de subdivision restante
     */
    private Color adaptive(int nX, int nY, double u, double v, double size, int depth,
                           Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
        if (depth == 0 || (similar(topLeft, topRight) && similar(topLeft, bottomLeft)
                && similar(topLeft, bottomRight)))
            return average(topLeft, topRight, bottomLeft, bottomRight);

        double h = size / 2;
        Color center = colorAt(nX, nY, u + h, v + h);
        Color top = colorAt(nX, nY, u + h, v);
        Color bottom = colorAt(nX, nY, u + h, v + size);
        Color left = colorAt(nX, nY, u, v + h);
        Color right = colorAt(nX, nY, u + size, v + h);

        return average(
                adaptive(nX, nY, u, v, h, depth - 1, topLeft, top, left, center),
                adaptive(nX, nY, u + h, v, h, depth - 1, top, topRight, center, right),
                adaptive(nX, nY, u, v + h, h, depth - 1, left, center, bottomLeft, bottom),
                adaptive(nX, nY, u + h, v + h, h, depth - 1, center, right, bottom, bottomRight));
    }

    /** Moyenne de quatre couleurs. */
    private static Color average(Color a, Color b, Color c, Color d) {
        return a.add(b, c, d).reduce(4);
    }

    /** Deux couleurs sont-elles assez proches pour ne pas subdiviser ? */
    private static boolean similar(Color a, Color b) {
        java.awt.Color ca = a.getColor();
        java.awt.Color cb = b.getColor();
        return Math.abs(ca.getRed() - cb.getRed()) <= ADAPTIVE_TOLERANCE
                && Math.abs(ca.getGreen() - cb.getGreen()) <= ADAPTIVE_TOLERANCE
                && Math.abs(ca.getBlue() - cb.getBlue()) <= ADAPTIVE_TOLERANCE;
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
