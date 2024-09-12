package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

public class SpotLight extends PointLight{
    private Vector direction;

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        double dl = alignZero(getL(p).dotProduct(this.direction));
        if (dl <= 0) return Color.BLACK;
        return super.getIntensity(p).scale(dl);
    }


    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }


}
