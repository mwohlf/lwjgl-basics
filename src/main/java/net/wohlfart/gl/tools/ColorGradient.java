package net.wohlfart.gl.tools;

import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;


/**
 * converting noise to color
 *
 * @author michael
 */
public class ColorGradient {
    private static final double LEFT = -1d;
    private static final double RIGHT = +1d;

    private final Set<GradientPoint> gradientPoints = new TreeSet<GradientPoint>();

    public ColorGradient(Color... colors) {
        if (colors.length == 1) {
            gradientPoints.add(new GradientPoint(LEFT, colors[0]));
            gradientPoints.add(new GradientPoint(RIGHT, colors[0]));
        } else {
            for (int i = 0; i < colors.length; i++) {
                double delta = ((RIGHT - LEFT) * i / (colors.length - 1d));
                gradientPoints.add(new GradientPoint(LEFT + delta, colors[i]));
            }
        }
    }

    public ColorGradient(GradientPoint... points) {
        for (GradientPoint gradientPoint : points) {
            gradientPoints.add(gradientPoint);
        }
    }

    // for testing
    Set<GradientPoint> getGradientPoints() {
        return gradientPoints;
    }

    /**
     *
     * @param value
     *            [-1 .. +1]
     * @return
     */
    public Color getColor(final double value) {

        int size = gradientPoints.size();
        GradientPoint pointArray[] = new GradientPoint[size];
        gradientPoints.toArray(pointArray);

        GradientPoint left = pointArray[0];
        for (int i = 0; i < size; i++) {
            GradientPoint next = pointArray[i];
            if (next.point > value) {
                break; // we need to stay below the next value
            }
            left = next;
        }

        GradientPoint right = pointArray[size - 1];
        for (int i = size - 1; i >= 0; i--) {
            GradientPoint next = pointArray[i];
            if (next.point < value) {
                break; // we need to stay below the next value
            }
            right = next;
        }

        if (left == right) {
            return right.color;
        }

        // now calculate gradient between left and right
        double delta = 1d;
        double distanceLeft = 0.5d;
        double distanceRight = 0.5d;
        if (right.point > left.point) {
            delta = right.point - left.point;
            distanceLeft = (value - left.point) / delta;
            distanceRight = (right.point - value) / delta;
        }

        return calculateRGBColor(left, right, distanceLeft, distanceRight);
    }

    private Color calculateRGBColor(final GradientPoint left2, final GradientPoint right2, final double distanceLeft, final double distanceRight) {

        float red = ((((float) left2.color.getRed() * (float) distanceRight) + ((float) right2.color.getRed() * (float) distanceLeft))) / 256f;

        float green = ((((float) left2.color.getGreen() * (float) distanceRight) + ((float) right2.color.getGreen() * (float) distanceLeft))) / 256f;

        float blue = ((((float) left2.color.getBlue() * (float) distanceRight) + ((float) right2.color.getBlue() * (float) distanceLeft))) / 256f;

        return new Color(red, green, blue);
    }

    public static class GradientPoint implements Comparable<GradientPoint> {
        final double point;
        final Color color;

        public GradientPoint(final double point, final Color color) {
            this.point = point;
            this.color = color;
        }

        @Override
        public int compareTo(final GradientPoint that) {
            return Double.compare(this.point, that.point);
        }
    }

    public static Color cosGradient(final Color top, final Color low, final float v) {
        // v [-1 .. 1]
        float value = v;

        value = (float)Math.sin(value * (float)Math.PI/2f);
        value = (float)Math.sin(value * (float)Math.PI/2);
        value = (float)Math.sin(value * (float)Math.PI/2);

        float red = (((float) top.getRed() * value) + ((float) low.getRed() * (1f - value))) / 256f;
        float green = (((float) top.getGreen() * value) + ((float) low.getGreen() * (1f - value))) / 256f;
        float blue = (((float) top.getBlue() * value) + ((float) low.getBlue() * (1f - value))) / 256f;
        return new Color(red, green, blue);
    }

    public static Color linearGradient(final Color top, final Color low, final double skyNoise) {
        // v [-1 .. 1]
        float value = (float) (skyNoise + 1f) / 2f;

        float red = (((float) top.getRed() * value) + ((float) low.getRed() * (1f - value))) / 256f;
        float green = (((float) top.getGreen() * value) + ((float) low.getGreen() * (1f - value))) / 256f;
        float blue = (((float) top.getBlue() * value) + ((float) low.getBlue() * (1f - value))) / 256f;
        return new Color(red, green, blue);
    }

}
