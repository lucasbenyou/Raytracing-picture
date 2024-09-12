package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import geometries.Intersectable.GeoPoint;
public abstract class Geometry extends Intersectable{
     protected Color emission = Color.BLACK;
     private Material material = new Material();

     public Material getMaterial() {
          return material;
     }

     public Geometry setMaterial(Material material) {
          this.material = material;
          return this;
     }
     public abstract Vector getNormal(Point v);
     /**
      * A getter method for "emission".
      */
     public Color getEmission() {
          return emission;
     }

     /**
      * A setter method that follows a builder-like pattern
      * for "emission".
      */
     public Geometry setEmission(Color emission) {
          this.emission = emission;
          return this;
     }
}
