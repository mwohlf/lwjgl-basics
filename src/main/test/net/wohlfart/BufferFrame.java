package net.wohlfart;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.nio.IntBuffer;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.wohlfart.gl.texture.SimplexProcTexture;

@SuppressWarnings("serial")
public class BufferFrame extends JFrame {

    public static void main(String[] args) {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    new BufferFrame().setVisible(true);
                }
            });
        } catch (InvocationTargetException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    protected final SimplexProcTexture textureBuffer;
    protected BufferedImage image;

    protected final JSlider octaveSlider = new JSlider();
    protected final JSlider persistenceSlider = new JSlider();
    protected final JSlider wSlider = new JSlider();

    BufferFrame() {
        textureBuffer = new SimplexProcTexture(700, 700);
        textureBuffer.init();
        addContent();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addContent() {
        final Container content = getContentPane();
        content.add(new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(textureBuffer.getWidth(), textureBuffer.getHeight());
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                updateImage();
                g.drawImage(image, 0, 0, null);
            }

        }, BorderLayout.CENTER);

        content.add(createControlsPanel(), BorderLayout.WEST);
    }

    private JComponent createControlsPanel() {
        final Box result = Box.createVerticalBox();
        result.add(new JLabel("Octave:"));
        result.add(octaveSlider);
        result.add(new JLabel("w:"));
        result.add(wSlider);
        result.add(new JLabel("Persistence:"));
        result.add(persistenceSlider);

        octaveSlider.setMaximum(10);
        octaveSlider.setMinimum(0);
        octaveSlider.setValue(textureBuffer.getOctaves());
        octaveSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textureBuffer.setOctaves(octaveSlider.getValue());
                        textureBuffer.init();
                        repaint();
                    }
                });
            }
        });

        final float maxW = 100;
        wSlider.setMaximum((int) maxW);
        wSlider.setMinimum(0);
        wSlider.setValue((int) (textureBuffer.getW() * maxW));
        wSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textureBuffer.setW(wSlider.getValue() / maxW);
                        textureBuffer.init();
                        repaint();
                    }
                });
            }
        });

        final float maxPersistence = 100;
        persistenceSlider.setMaximum((int) maxW);
        persistenceSlider.setMinimum(0);
        persistenceSlider.setValue((int) (textureBuffer.getPersistence() * maxPersistence));
        persistenceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textureBuffer.setPersistence(persistenceSlider.getValue() / maxPersistence);
                        textureBuffer.init();
                        repaint();
                    }
                });
            }
        });

        return result;
    }

    private void updateImage() {
        final IntBuffer buffer = textureBuffer.getBuffer();
        image = new BufferedImage(textureBuffer.getWidth(), textureBuffer.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                image.setRGB(x, y, buffer.get(x + y * image.getWidth()));
            }
        }
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
