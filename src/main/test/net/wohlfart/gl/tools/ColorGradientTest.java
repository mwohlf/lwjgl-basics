package net.wohlfart.gl.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Set;

import net.wohlfart.gl.tools.ColorGradient.GradientPoint;

import org.junit.Test;

public class ColorGradientTest {

    @Test
    public void checkSimpleOrdering() {
        ColorGradient colorGradient = new ColorGradient(
                new ColorGradient.GradientPoint(-1, new Color(255, 0, 0)), 
                new ColorGradient.GradientPoint(0, new Color(0, 0, 0)), 
                new ColorGradient.GradientPoint(+1, new Color(0, 0, 255)));

        Set<GradientPoint> gradientPoints = colorGradient.getGradientPoints();
        int size = gradientPoints.size();
        GradientPoint pointArray[] = new GradientPoint[size];
        gradientPoints.toArray(pointArray);

        assertTrue(pointArray[0].point < pointArray[1].point);
        assertTrue(pointArray[1].point < pointArray[2].point);
    }

    @Test
    public void checkOrdering() {
        ColorGradient colorGradient = new ColorGradient(
                new ColorGradient.GradientPoint(+1.0d, new Color(255, 0, 0)), 
                new ColorGradient.GradientPoint(+0.4d, new Color(255, 0, 0)), 
                new ColorGradient.GradientPoint(+0.1d, new Color(255, 0, 0)),
                new ColorGradient.GradientPoint(+0.0d, new Color(0, 0, 0)), 
                new ColorGradient.GradientPoint(-0.2d, new Color(255, 0, 0)),
                new ColorGradient.GradientPoint(-0.1d, new Color(0, 0, 255)));

        Set<GradientPoint> gradientPoints = colorGradient.getGradientPoints();
        int size = gradientPoints.size();
        GradientPoint pointArray[] = new GradientPoint[size];
        gradientPoints.toArray(pointArray);

        assertEquals(-0.2, pointArray[0].point, 0.001);
        assertEquals(-0.1, pointArray[1].point, 0.001);
        assertEquals(+0.0, pointArray[2].point, 0.001);
        assertEquals(+0.1, pointArray[3].point, 0.001);
        assertEquals(+0.4, pointArray[4].point, 0.001);
        assertEquals(+1.0, pointArray[5].point, 0.001);
    }

    @Test
    public void checkPoints() {
        ColorGradient colorGradient = new ColorGradient(Color.BLACK, Color.WHITE);
        Set<GradientPoint> gradientPoints = colorGradient.getGradientPoints();
        int size = gradientPoints.size();
        assertEquals(2, size);
        GradientPoint pointArray[] = new GradientPoint[size];
        gradientPoints.toArray(pointArray);

        assertEquals(Color.BLACK, pointArray[0].color);
        assertEquals(Color.WHITE, pointArray[1].color);

    }

    @Test
    public void middleSimpleGrey() {
        ColorGradient colorGradient;
        Color middle;

        colorGradient = new ColorGradient(Color.BLACK, Color.WHITE);
        middle = colorGradient.getColor(0d);
        assertEquals(255 / 2, middle.getRed());
        assertEquals(255 / 2, middle.getGreen());
        assertEquals(255 / 2, middle.getBlue());

        colorGradient = new ColorGradient(new Color(0, 0, 0), new Color(255, 0, 0));
        middle = colorGradient.getColor(0d);
        assertEquals(255 / 2, middle.getRed());
        assertEquals(0, middle.getGreen());
        assertEquals(0, middle.getBlue());

        colorGradient = new ColorGradient(new Color(0, 0, 0), new Color(0, 255, 0));
        middle = colorGradient.getColor(0d);
        assertEquals(0, middle.getRed());
        assertEquals(255 / 2, middle.getGreen());
        assertEquals(0, middle.getBlue());

        colorGradient = new ColorGradient(new Color(0, 0, 0), new Color(0, 0, 255));
        middle = colorGradient.getColor(0d);
        assertEquals(0, middle.getRed());
        assertEquals(0, middle.getGreen());
        assertEquals(255 / 2, middle.getBlue());
    }

    @Test
    public void threeColor() {
        Color color;
        ColorGradient colorGradient = new ColorGradient(
                new ColorGradient.GradientPoint(-1, new Color(255, 0, 0)), 
                new ColorGradient.GradientPoint(0, new Color(0, 0, 0)), 
                new ColorGradient.GradientPoint(+1, new Color(0, 0, 255)));

        color = colorGradient.getColor(-1d);
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());

        color = colorGradient.getColor(-0.5d);
        assertEquals(255 / 2, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());

        color = colorGradient.getColor(0d);
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());

        color = colorGradient.getColor(+0.5d);
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(255 / 2, color.getBlue());

        color = colorGradient.getColor(+1d);
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(255, color.getBlue());

    }

    @Test
    public void threeColorSimple() {
        Color color;
        ColorGradient colorGradient = new ColorGradient(
                new Color(255, 0, 0), 
                new Color(0, 0, 0), 
                new Color(0, 0, 255));

        color = colorGradient.getColor(-1d);
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());

        color = colorGradient.getColor(-0.5d);
        assertEquals(255 / 2, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());

        color = colorGradient.getColor(0d);
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());

        color = colorGradient.getColor(+0.5d);
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(255 / 2, color.getBlue());

        color = colorGradient.getColor(+1d);
        assertEquals(0, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(255, color.getBlue());

    }

}
