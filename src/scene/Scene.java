package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a scene in a computer graphics environment.
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;

    /**
     * The background color of the scene.
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light in the scene.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * The light sources in the scene.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a new Scene object with the specified name.
     *
     * @param name The name of the scene.
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Retrieves the background color of the scene.
     *
     * @return The background color of the scene.
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Retrieves the ambient light in the scene.
     *
     * @return The ambient light in the scene.
     */
    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    /**
     * Retrieves the geometries in the scene.
     *
     * @return The geometries in the scene.
     */
    public Geometries getGeometries() {
        return geometries;
    }

    /**
     * Retrieves the light sources in the scene.
     *
     * @return The light sources in the scene.
     */
    public List<LightSource> getLights() {
        return lights;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background The background color to be set.
     * @return The Scene object with the updated background color.
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light in the scene.
     *
     * @param ambientLight The ambient light to be set.
     * @return The Scene object with the updated ambient light.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries in the scene.
     *
     * @param geometries The geometries to be set.
     * @return The Scene object with the updated geometries.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Sets the light sources in the scene.
     *
     * @param lights The light sources to be set.
     * @return The Scene object with the updated light sources.
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
