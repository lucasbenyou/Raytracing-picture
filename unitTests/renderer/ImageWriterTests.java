package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTests {
    @Test
    public void writeToImage() {
        ImageWriter imageWriter = new ImageWriter("one", 800, 500);
        int lineX = imageWriter.getNx() / 16;
        int lineY = imageWriter.getNy() / 10;
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                if (i % lineX == 0 || j % lineY == 0) {
                    imageWriter.writePixel(i, j, Color.BLACK);
                } else {
                    imageWriter.writePixel(i, j, new Color(255, 255, 0));
                }
            }
        }
        imageWriter.writeToImage();

    }

}