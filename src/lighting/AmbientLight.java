package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Vector;

public class AmbientLight extends Light{

    public static AmbientLight NONE = new AmbientLight(Color.BLACK,Double3.ZERO);

    /**
     constructor with parameters , cal intensity.
     */
    public AmbientLight(Color Ia, Double3 Ka ) {
         super(Ia.scale(Ka));
    }

    public AmbientLight(Color Ia,double Ka){
        super(Ia.scale(Ka));
    }

}
