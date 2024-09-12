package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    private Point position;
    private double kC=1;
    private double kL=0;
    private double kQ=0;

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }
    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }
    @Override
    public Color getIntensity(Point p) {
        double d = p.distance(this.position);
        return this.getIntensity().reduce(this.kC + this.kL * d + this.kQ * d * d);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(this.position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

}
