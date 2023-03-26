package heightmap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class HeightMapGenerator {
    public static void generateHeightMap() {
        Random random = new Random();
        double seed = random.nextDouble(0.0035);
//        double seed = 0.003;
        int width = 2048;
        int height = 2048;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double noise = generateNoise(x * seed, y * seed);
                int gray = (int) (noise * 255);
                gray = Math.min(255, Math.max(0, gray));
                Color color = new Color(gray, gray, gray);
                image.setRGB(x, y, color.getRGB());
            }
        }
        try {
            File output = new File("src/main/resources/heightmap.png");
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            System.out.println("Failed to save image: " + e.getMessage());
        }
    }

    private static double generateNoise(double x, double y) {
        int n = 8;
        double persistence = 0.5;
        double total = 0.0;
        double frequency = 1.0;
        double amplitude = 1.0;
        double maxValue = 0.0;
        for (int i = 0; i < n; i++) {
            total += noise(x * frequency, y * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }
        return total / maxValue;
    }

    private static double noise(double x, double y) {
        int ix = (int) Math.floor(x);
        int iy = (int) Math.floor(y);
        double fx = x - ix;
        double fy = y - iy;
        double a = random(ix, iy);
        double b = random(ix + 1, iy);
        double c = random(ix, iy + 1);
        double d = random(ix + 1, iy + 1);
        double s = smooth(fx);
        double t = smooth(fy);
        double u = lerp(s, a, b);
        double v = lerp(s, c, d);
        return lerp(t, u, v);
    }

    private static double random(int x, int y) {
        int n = x + y * 57;
        n = (n << 13) ^ n;
        return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
    }

    private static double smooth(double x) {
        return x * x * (3 - 2 * x);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }
}