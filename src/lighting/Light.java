package lighting;

import primitives.Color;
import primitives.Point;

import java.util.List;


abstract class Light {
     private Color intensity;
     protected Light(Color intensity)
     {this.intensity = intensity;}

    public Color getIntensity(){return intensity;}

}
