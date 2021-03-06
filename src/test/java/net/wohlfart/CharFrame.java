package net.wohlfart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import net.wohlfart.basic.hud.txt.CharAtlasBuilder;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;


@SuppressWarnings("serial")
public class CharFrame extends JFrame {

    public static void main(String[] args) {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        new CharFrame().setVisible(true);
                    } catch (final LWJGLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (InvocationTargetException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    CharFrame() throws LWJGLException {
        setupDisplay();
        addContent();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void setupDisplay() throws LWJGLException {
        final PixelFormat pixelFormat = new PixelFormat();
        final ContextAttribs contextAtributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
        Display.setDisplayMode(new DisplayMode(100, 100));
        Display.setResizable(false);
        Display.setTitle("testing");
        Display.setVSyncEnabled(true);
        Display.create(pixelFormat, contextAtributes); // creates the GL context
    }

    private void addContent() {
        final CharAtlasBuilder fontRenderer = new CharAtlasBuilder();
        final Image image = fontRenderer
                    .setFontSize(30)
                    .setBorderOn(true)
                    .build()
                    .getImage();

        final Container content = getContentPane();

        content.add(new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(image.getWidth(null), image.getHeight(null));
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawImage(image, 0, 0, null);
            }

        }, BorderLayout.CENTER);

    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            pack();
            setLocationRelativeTo(null);
        }
    }

}
