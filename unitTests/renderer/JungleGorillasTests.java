package renderer;

import org.junit.jupiter.api.Test;

import geometries.Geometries;
import geometries.Polygon;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Test d'integration "image" : deux gorilles modelises uniquement a partir de
 * polygones (Polygon), places dans une jungle.
 * <p>
 * L'image met en oeuvre les deux effets demandes :
 * <ul>
 * <li><b>la transparence</b> (kT) : les grandes feuilles du premier plan, les
 * lianes translucides et la flaque d'eau laissent passer la lumiere ; elles
 * produisent donc aussi des <i>ombres partielles</i> colorees ;</li>
 * <li><b>les ombres</b> (rayons d'ombre) : le projecteur principal projette les
 * ombres portees des gorilles et des troncs sur le sol de la jungle.</li>
 * </ul>
 *
 * @author lucas
 */
public class JungleGorillasTests {

    /** Resolution de l'image finale (image carree). */
    private static final int RES = 600;

    /** La scene de test. */
    private final Scene scene = new Scene("Deux gorilles dans la jungle");

    // ------------------------------------------------------------------
    // Palette (couleur propre des objets)
    // ------------------------------------------------------------------
    private static final Color FUR_ADULT = new Color(18, 16, 18);
    private static final Color FUR_YOUNG = new Color(28, 22, 19);
    private static final Color SILVER_BACK = new Color(30, 29, 33);
    private static final Color SKIN = new Color(24, 18, 18);
    private static final Color EYE = new Color(86, 52, 14);
    private static final Color GROUND = new Color(16, 22, 12);
    private static final Color BARK = new Color(26, 19, 13);
    private static final Color LEAF = new Color(10, 30, 12);
    private static final Color LEAF_LIGHT = new Color(16, 44, 16);
    private static final Color CANOPY = new Color(6, 18, 10);
    private static final Color WATER = new Color(4, 10, 12);

    // ------------------------------------------------------------------
    // Materiaux
    // Le coefficient diffus kD est un Double3 : il porte donc la couleur de
    // l'objet sous la lumiere (sinon toutes les surfaces eclairees virent au gris).
    // ------------------------------------------------------------------
    /** Pelage : mat, tres sombre, a peine speculaire. */
    private static final Material FUR_MAT = new Material()
            .setkD(new Double3(0.10, 0.095, 0.11)).setkS(new Double3(0.06)).setnShininess(30);
    /** Dos argente du male adulte. */
    private static final Material SILVER_MAT = new Material()
            .setkD(new Double3(0.15, 0.15, 0.17)).setkS(new Double3(0.08)).setnShininess(40);
    /** Peau nue (museau, oreilles) : un peu brillante. */
    private static final Material SKIN_MAT = new Material()
            .setkD(new Double3(0.09, 0.075, 0.075)).setkS(new Double3(0.26)).setnShininess(100);
    /** Oeil : brillant. */
    private static final Material EYE_MAT = new Material()
            .setkD(new Double3(0.26, 0.17, 0.06)).setkS(new Double3(0.55)).setnShininess(250);
    /** Sol de la jungle (mousse). */
    private static final Material GROUND_MAT = new Material()
            .setkD(new Double3(0.17, 0.26, 0.12)).setkS(new Double3(0.05)).setnShininess(10);
    /** Ecorce. */
    private static final Material BARK_MAT = new Material()
            .setkD(new Double3(0.16, 0.12, 0.08)).setkS(new Double3(0.07)).setnShininess(20);
    /** Feuillage opaque du fond. */
    private static final Material CANOPY_MAT = new Material()
            .setkD(new Double3(0.07, 0.19, 0.09)).setkS(new Double3(0.05)).setnShininess(20);
    /** Feuille translucide : c'est elle qui donne les ombres partielles vertes. */
    private static final Material LEAF_MAT = new Material()
            .setkD(new Double3(0.09, 0.30, 0.11)).setkS(new Double3(0.16)).setnShininess(60)
            .setkT(new Double3(0.42, 0.55, 0.42));
    /** Eau : transparente ET legerement reflechissante. */
    private static final Material WATER_MAT = new Material()
            .setkD(new Double3(0.03, 0.05, 0.06)).setkS(new Double3(0.45)).setnShininess(300)
            .setkT(new Double3(0.38, 0.45, 0.45)).setkR(new Double3(0.40));

    // ==================================================================
    // Le test
    // ==================================================================

    /**
     * Produit l'image "gorillasJungle.png" : un dos argente et son petit, en
     * polygones, dans une clairiere de jungle, avec transparence et ombres.
     */
    @Test
    public void twoGorillasInTheJungle() {
        buildJungle(scene.geometries);

        // le grand male (dos argente) - un peu a gauche et en retrait
        new GorillaBuilder(scene.geometries, -48, -200, 1.0, FUR_ADULT, SILVER_BACK, SKIN).build();
        // le jeune gorille - plus petit, plus pres de la camera
        new GorillaBuilder(scene.geometries, 74, -95, 0.58, FUR_YOUNG, FUR_YOUNG, SKIN).build();

        // ---------------- lumieres ----------------
        // lumiere ambiante : la penombre verte du sous-bois
        scene.setAmbientLight(new AmbientLight(new Color(72, 96, 78), 0.10));
        scene.setBackground(new Color(8, 20, 14));

        // rayon de soleil qui traverse la canopee : c'est lui qui dessine les ombres
        scene.lights.add(new SpotLight(new Color(1150, 1060, 840), new Point(-380, 620, 320),
                new Vector(380, -560, -420)).setkL(3E-4).setkQ(6E-7));
        // rebond vert du feuillage, a droite
        scene.lights.add(new PointLight(new Color(150, 205, 142), new Point(430, 300, 460))
                .setkL(8E-4).setkQ(3E-7));
        // legere lumiere d'appoint cote camera pour decoller les silhouettes du fond
        scene.lights.add(new PointLight(new Color(120, 135, 165), new Point(-120, 260, 700))
                .setkL(1E-3).setkQ(4E-7));

        // ---------------- camera ----------------
        Camera camera = new Camera(new Point(0, 90, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPSize(270, 270).setVPDistance(1000);

        camera.setImageWriter(new ImageWriter("gorillasJungle", RES, RES))
                .setRayTracer(new RayTracerBasic(scene))
                .setAdaptiveAntiAliasing(3)   // anti-crenelage adaptatif : aretes lissees
                .setMultithreading(0)         // tous les coeurs disponibles
                .renderImage()
                .writeToImage();
    }

    // ==================================================================
    // Outils de construction (uniquement des polygones)
    // ==================================================================

    /**
     * Ajoute les 6 polygones d'un prisme droit a base rectangulaire, eventuellement
     * effile (les sections du bas et du haut peuvent avoir des tailles et des
     * centres differents). Les 4 faces laterales restent planes et convexes, elles
     * peuvent donc etre decrites par des {@link Polygon}.
     *
     * @param geometries la liste de geometries a completer
     * @param base       centre de la face inferieure
     * @param top        centre de la face superieure
     * @param bx         demi-largeur (x) en bas
     * @param bz         demi-profondeur (z) en bas
     * @param tx         demi-largeur (x) en haut
     * @param tz         demi-profondeur (z) en haut
     * @param emission   couleur propre
     * @param material   materiau
     */
    private static void addPrism(Geometries geometries, Point base, Point top,
                                 double bx, double bz, double tx, double tz,
                                 Color emission, Material material) {
        double x0 = base.getX(), y0 = base.getY(), z0 = base.getZ();
        double x1 = top.getX(), y1 = top.getY(), z1 = top.getZ();

        Point b1 = new Point(x0 - bx, y0, z0 - bz);
        Point b2 = new Point(x0 + bx, y0, z0 - bz);
        Point b3 = new Point(x0 + bx, y0, z0 + bz);
        Point b4 = new Point(x0 - bx, y0, z0 + bz);
        Point t1 = new Point(x1 - tx, y1, z1 - tz);
        Point t2 = new Point(x1 + tx, y1, z1 - tz);
        Point t3 = new Point(x1 + tx, y1, z1 + tz);
        Point t4 = new Point(x1 - tx, y1, z1 + tz);

        geometries.add(
                new Polygon(b1, b2, b3, b4).setEmission(emission).setMaterial(material), // dessous
                new Polygon(t1, t2, t3, t4).setEmission(emission).setMaterial(material), // dessus
                new Polygon(b4, b3, t3, t4).setEmission(emission).setMaterial(material), // avant
                new Polygon(b1, b2, t2, t1).setEmission(emission).setMaterial(material), // arriere
                new Polygon(b1, t1, t4, b4).setEmission(emission).setMaterial(material), // gauche
                new Polygon(b2, b3, t3, t2).setEmission(emission).setMaterial(material));// droite
    }

    /**
     * Ajoute un quadrilatere plan defini par ses 4 sommets.
     */
    private static void addQuad(Geometries geometries, Point a, Point b, Point c, Point d,
                                Color emission, Material material) {
        geometries.add(new Polygon(a, b, c, d).setEmission(emission).setMaterial(material));
    }

    /**
     * Ajoute une feuille : un parallelogramme (donc toujours plan et convexe)
     * defini par son centre et ses deux demi-diagonales.
     */
    private static void addLeaf(Geometries geometries, Point center, Vector u, Vector v,
                                Color emission, Material material) {
        Vector mu = u.scale(-1);
        Vector mv = v.scale(-1);
        geometries.add(new Polygon(
                center.add(u).add(v),
                center.add(u).add(mv),
                center.add(mu).add(mv),
                center.add(mu).add(v)).setEmission(emission).setMaterial(material));
    }

    // ==================================================================
    // Le decor
    // ==================================================================

    /** Construit le sol, les troncs, le rideau de vegetation et les feuilles. */
    private static void buildJungle(Geometries g) {
        // ---- sol de la clairiere ----
        addQuad(g, new Point(-3000, 0, -3000), new Point(3000, 0, -3000),
                new Point(3000, 0, 900), new Point(-3000, 0, 900), GROUND, GROUND_MAT);

        // ---- rideaux de vegetation, du plus lointain au plus proche ----
        addQuad(g, new Point(-2600, 0, -1700), new Point(2600, 0, -1700),
                new Point(2600, 1500, -1700), new Point(-2600, 1500, -1700), CANOPY, CANOPY_MAT);
        addQuad(g, new Point(-1400, 0, -1250), new Point(1400, 0, -1250),
                new Point(1400, 430, -1250), new Point(-1400, 430, -1250),
                new Color(14, 38, 18), CANOPY_MAT);
        addQuad(g, new Point(-1100, 0, -820), new Point(1100, 0, -820),
                new Point(1100, 360, -820), new Point(-1100, 360, -820),
                new Color(18, 48, 20), CANOPY_MAT);

        // ---- troncs d'arbres (prismes effiles) ----
        addPrism(g, new Point(-165, 0, -560), new Point(-178, 640, -580), 32, 32, 20, 20, BARK, BARK_MAT);
        addPrism(g, new Point(188, 0, -700), new Point(198, 690, -720), 34, 34, 21, 21, BARK, BARK_MAT);
        addPrism(g, new Point(-95, 0, -1150), new Point(-108, 800, -1170), 40, 40, 24, 24,
                new Color(48, 36, 26), BARK_MAT);
        addPrism(g, new Point(152, 0, -1320), new Point(164, 860, -1340), 44, 44, 26, 26,
                new Color(48, 36, 26), BARK_MAT);
        addPrism(g, new Point(-238, 0, -1500), new Point(-250, 720, -1520), 38, 38, 24, 24,
                new Color(44, 33, 24), BARK_MAT);

        // ---- racines / rochers au sol ----
        addPrism(g, new Point(-210, 0, -430), new Point(-190, 34, -410), 60, 26, 44, 18,
                new Color(46, 40, 30), GROUND_MAT);
        addPrism(g, new Point(196, 0, -520), new Point(180, 28, -500), 52, 24, 38, 16,
                new Color(46, 40, 30), GROUND_MAT);

        // ---- lianes (fines et sombres) ----
        addPrism(g, new Point(62, 120, -760), new Point(54, 900, -780), 4, 4, 5, 5,
                new Color(30, 40, 20), BARK_MAT);
        addPrism(g, new Point(-132, 90, -600), new Point(-124, 880, -620), 3, 3, 4, 4,
                new Color(30, 40, 20), BARK_MAT);

        // ---- grandes feuilles TRANSLUCIDES du premier plan ----
        // elles se superposent aux gorilles : on voit a travers (kT)
        addLeaf(g, new Point(-72, 176, 120), new Vector(72, -16, 0), new Vector(0, 10, 46),
                LEAF_LIGHT, LEAF_MAT);
        addLeaf(g, new Point(96, 150, 40), new Vector(60, -22, 0), new Vector(0, 12, 42),
                LEAF, LEAF_MAT);
        addLeaf(g, new Point(4, 192, -20), new Vector(86, -10, 0), new Vector(0, 14, 38),
                LEAF, LEAF_MAT);
        addLeaf(g, new Point(70, 74, 40), new Vector(50, -28, 0), new Vector(0, 16, 38),
                LEAF_LIGHT, LEAF_MAT);
        addLeaf(g, new Point(-104, 58, 96), new Vector(44, 24, 0), new Vector(0, 14, 34),
                LEAF, LEAF_MAT);

        // ---- feuilles translucides hors champ, au-dessus :
        //      elles filtrent le rayon de soleil et posent des ombres partielles vertes
        addLeaf(g, new Point(-120, 420, 180), new Vector(130, -30, 0), new Vector(0, 26, 120),
                LEAF, LEAF_MAT);
        addLeaf(g, new Point(70, 470, 40), new Vector(120, -26, 0), new Vector(0, 22, 110),
                LEAF, LEAF_MAT);
        addLeaf(g, new Point(-40, 360, -140), new Vector(150, -34, 0), new Vector(0, 28, 100),
                LEAF, LEAF_MAT);

        // feuille qui passe DEVANT le grand gorille : on voit son corps au travers
        addLeaf(g, new Point(-54, 81, 100), new Vector(46, -24, 0), new Vector(0, 16, 34),
                LEAF_LIGHT, LEAF_MAT);

        // ---- touffes de feuillage a mi-distance (opaques), pour donner de la profondeur ----
        addLeaf(g, new Point(-300, 150, -700), new Vector(90, -34, 0), new Vector(0, 40, 26),
                new Color(12, 34, 14), CANOPY_MAT);
        addLeaf(g, new Point(-215, 96, -640), new Vector(70, 30, 0), new Vector(0, 34, 22),
                new Color(15, 42, 17), CANOPY_MAT);
        addLeaf(g, new Point(300, 175, -880), new Vector(100, 40, 0), new Vector(0, 44, 28),
                new Color(11, 32, 13), CANOPY_MAT);
        addLeaf(g, new Point(236, 152, -700), new Vector(80, -32, 0), new Vector(0, 36, 24),
                new Color(16, 46, 18), CANOPY_MAT);
        addLeaf(g, new Point(-60, 300, -1050), new Vector(120, -46, 0), new Vector(0, 50, 30),
                new Color(9, 28, 12), CANOPY_MAT);
        addLeaf(g, new Point(120, 250, -980), new Vector(105, 42, 0), new Vector(0, 46, 28),
                new Color(10, 30, 13), CANOPY_MAT);
        addLeaf(g, new Point(-150, 215, -900), new Vector(95, 36, 0), new Vector(0, 42, 26),
                new Color(10, 30, 13), CANOPY_MAT);
        addLeaf(g, new Point(30, 238, -760), new Vector(85, -30, 0), new Vector(0, 38, 24),
                new Color(14, 40, 16), CANOPY_MAT);

        // ---- fougeres au sol, devant les gorilles ----
        addLeaf(g, new Point(-215, 34, -300), new Vector(58, 22, 0), new Vector(0, 26, 20),
                new Color(18, 50, 20), CANOPY_MAT);
        addLeaf(g, new Point(-165, 26, -250), new Vector(46, -18, 0), new Vector(0, 22, 17),
                new Color(20, 56, 22), CANOPY_MAT);
        addLeaf(g, new Point(215, 30, -330), new Vector(52, 20, 0), new Vector(0, 24, 18),
                new Color(18, 50, 20), CANOPY_MAT);
        addLeaf(g, new Point(160, 22, -240), new Vector(44, -16, 0), new Vector(0, 20, 16),
                new Color(20, 56, 22), CANOPY_MAT);

        // ---- flaque d'eau au premier plan (transparente + reflechissante) ----
        addQuad(g, new Point(-460, 0.6, 80), new Point(460, 0.6, 80),
                new Point(460, 0.6, 460), new Point(-460, 0.6, 460), WATER, WATER_MAT);
    }

    // ==================================================================
    // Le gorille
    // ==================================================================

    /**
     * Petit "squelette" qui assemble un gorille a partir de prismes (donc de
     * polygones). Les coordonnees sont donnees dans le repere local du gorille :
     * pieds en y = 0, gorille regardant vers la camera (+z), puis translatees en
     * (cx, 0, cz) et mises a l'echelle.
     */
    private static class GorillaBuilder {
        private final Geometries target;
        private final double cx;
        private final double cz;
        private final double scale;
        private final Color fur;
        private final Color chest;
        private final Color skin;

        /**
         * @param target liste de geometries a completer
         * @param cx     abscisse du gorille dans la scene
         * @param cz     profondeur du gorille dans la scene
         * @param scale  echelle (1 = environ 165 unites de haut)
         * @param fur    couleur du pelage
         * @param chest  couleur du poitrail / du dos (argente pour le male adulte)
         * @param skin   couleur de la peau nue
         */
        GorillaBuilder(Geometries target, double cx, double cz, double scale,
                       Color fur, Color chest, Color skin) {
            this.target = target;
            this.cx = cx;
            this.cz = cz;
            this.scale = scale;
            this.fur = fur;
            this.chest = chest;
            this.skin = skin;
        }

        /** Passage du repere local du gorille au repere de la scene. */
        private Point p(double x, double y, double z) {
            return new Point(cx + x * scale, y * scale, cz + z * scale);
        }

        /** Une partie du corps = un prisme effile en coordonnees locales. */
        private void part(double x0, double y0, double z0, double bx, double bz,
                          double x1, double y1, double z1, double tx, double tz,
                          Color color, Material material) {
            addPrism(target, p(x0, y0, z0), p(x1, y1, z1),
                    bx * scale, bz * scale, tx * scale, tz * scale, color, material);
        }

        /** Assemble le gorille : 22 volumes, soit 132 polygones. */
        void build() {
            // le poitrail du male adulte est plus clair (dos argente)
            Material chestMaterial = chest == SILVER_BACK ? SILVER_MAT : FUR_MAT;

            // pieds et jambes (courtes et massives)
            for (int side = -1; side <= 1; side += 2) {
                part(side * 24, 0, 10, 15, 22, side * 24, 12, 8, 14, 20, fur, FUR_MAT);
                part(side * 22, 8, 0, 15, 17, side * 20, 54, -2, 18, 19, fur, FUR_MAT);
            }

            // tronc : hanches etroites -> epaules tres larges
            part(0, 46, 0, 29, 20, 0, 92, 1, 36, 24, fur, FUR_MAT);
            part(0, 90, 1, 36, 24, 0, 118, 0, 38, 22, fur, FUR_MAT);
            part(0, 84, 22, 13, 4, 0, 112, 21, 19, 4, chest, chestMaterial);

            // bras (longs, poings poses pres du sol)
            for (int side = -1; side <= 1; side += 2) {
                part(side * 50, 58, 4, 13, 14, side * 38, 110, 0, 18, 18, fur, FUR_MAT);
                part(side * 56, 20, 9, 11, 12, side * 50, 60, 4, 13, 14, fur, FUR_MAT);
                part(side * 58, 2, 14, 12, 14, side * 56, 22, 9, 11, 12, fur, FUR_MAT);
            }

            // cou et crane
            part(0, 108, 0, 19, 17, 0, 126, 1, 17, 15, fur, FUR_MAT);
            part(0, 122, 0, 23, 21, 0, 154, -2, 22, 19, fur, FUR_MAT);
            part(0, 152, -4, 9, 13, 0, 165, -6, 4, 8, fur, FUR_MAT);      // crete sagittale
            part(0, 142, 18, 22, 5, 0, 150, 19, 21, 5, fur, FUR_MAT);     // arcade sourciliere
            part(0, 123, 18, 16, 11, 0, 141, 17, 14, 10, skin, SKIN_MAT); // museau
            part(0, 126, 29, 12, 2, 0, 128, 29, 11, 2,
                    new Color(10, 7, 7), SKIN_MAT);                        // bouche

            // yeux et oreilles
            for (int side = -1; side <= 1; side += 2) {
                part(side * 10, 135, 22, 4, 2, side * 10, 141, 22, 4, 2, EYE, EYE_MAT);
                part(side * 24, 138, -2, 3, 5, side * 24, 147, -3, 2, 4, skin, SKIN_MAT);
            }
        }
    }
}
