package renderer;

import static java.awt.Color.*;

import geometries.Intersectable;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;


import org.junit.jupiter.api.Test;

public class ImageLevel7Tests {

    // private Scene scene = new Scene("Test scene");
    private Intersectable sphere = new Sphere(new Point(0, 0, -200), 60d)                                         //
            .setEmission(new Color(BLUE))                                                                                  //
            .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30));
    private Material trMaterial = new Material().setkD(0.5).setkS(0.5).setnShininess(30);

    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera(new Point(0, 0, 1200), new Vector(0, 0, -1), new Vector(0, 1, 0))   //
            .setVPSize(200, 200).setVPDistance(1000)                                                                       //
            .setRayTracer(new RayTracerBasic(scene));

    @Test
    public void Our_image() {

        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

        scene.geometries.add( //
                new Triangle(new Point(-150, -150, -160), new Point(150, -150, -150),
                        new Point(0, 120, -170)).setEmission(new Color(GRAY).scale(0.7)) //
                        .setMaterial(new Material().setkR(new Double3(1)).setkS(0.8).setnShininess(60)), //
                new Sphere(new Point(60, -30, -11), 20d) //
                        .setEmission(new Color(BLUE)).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)),
                new Sphere(new Point(-60, -30, -10), 20d) //
                        .setEmission(new Color(GREEN).scale(new Double3(0.1))).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)),
                new Sphere(new Point(0, 60, -5), 20d) //
                        .setEmission(new Color(YELLOW).scale(new Double3(0.3))).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)),
                new Sphere(new Point(60, 60, -5), 20d) //
                        .setEmission(new Color(BLUE).scale(new Double3(0.3))).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)),
                new Sphere(new Point(60, 60, -5), 40d) //
                        .setEmission(new Color(YELLOW).scale(new Double3(0.3))).setMaterial(new Material().setkD(0.5).setkS(0.5)
                                .setnShininess(30).setkT(new Double3(1))),
                new Sphere(new Point(-60, 60, -5), 20d) //
                        .setEmission(new Color(YELLOW).scale(new Double3(0.3))).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)),
                new Sphere(new Point(-60, 60, -5), 40d) //
                        .setEmission(new Color(BLUE).scale(new Double3(0.3))).setMaterial(new Material().setkD(0.5).setkS(0.5)
                                .setnShininess(30).setkT(new Double3(1))),


                new Sphere(new Point(0, -100, -6), 20d) //
                        .setEmission(new Color(RED)).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)),

                new Sphere(new Point(-10, -100, 50), 15d) //
                        .setEmission(new Color(BLUE)).setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkT(new Double3(0.6))),

                new Sphere(new Point(0, 0, -0), 20d) //
                        .setEmission(new Color(RED)).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30))//
                //      .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)) //
        );
        scene.lights.add( //
                new PointLight(new Color(WHITE).scale(0.8), new Point(0, 0, 80)) //
                        .setkL(4E-4).setkQ(2E-5));
        scene.lights.add( //
                new PointLight(new Color(WHITE).scale(1), new Point(80, 80, 80)) //36,255,159
                        .setkL(4E-4).setkQ(2E-5));
        scene.lights.add( //
                new PointLight(new Color(WHITE).scale(1), new Point(-80, 60, -5)) //36,255,159
                        .setkL(4E-4).setkQ(2E-5));
        scene.lights.add( //
                new PointLight(new Color(WHITE).scale(1), new Point(80, 60, -5)) //36,255,159
                        .setkL(4E-4).setkQ(2E-5));

        camera.setImageWriter(new ImageWriter("ImageLevel7", 600, 600)) //
                .renderImage() //
                .writeToImage();
    }
}
