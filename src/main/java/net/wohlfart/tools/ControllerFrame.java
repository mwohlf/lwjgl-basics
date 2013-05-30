package net.wohlfart.tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.elements.debug.Cube;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * ControllerFrame class.
 * </p>
 */
@SuppressWarnings("serial")
public class ControllerFrame extends JFrame {

    protected final JTextField xcoord = new JTextField(5);
    protected final JTextField ycoord = new JTextField(5);
    protected final JTextField zcoord = new JTextField(5);
    protected final JButton apply = new JButton("apply");
    private AbstractRenderable cube;

    /**
     * <p>
     * init.
     * </p>
     */
    public ControllerFrame init() {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setupContent();
                    setupCube();
                    registerButton();
                    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    setVisible(true);
                }

            });
        } catch (InvocationTargetException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    private void registerButton() {
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final float x = Float.parseFloat(xcoord.getText());
                final float y = Float.parseFloat(ycoord.getText());
                final float z = Float.parseFloat(zcoord.getText());
                cube.setPosition(new Vector3f(x, y, z));
            }
        });
    }

    /**
     * <p>
     * Getter for the field <code>cube</code>.
     * </p>
     * 
     * @return a {@link net.wohlfart.gl.elements.debug.AbstractRenderableWireframe} object.
     */
    public AbstractRenderable getCube() {
        return cube;
    }

    private void setupCube() {
        cube = new Cube(1f).withColor(ReadableColor.RED);
    }

    private void setupContent() {
        final Container content = getContentPane();
        final Box verticalBox = Box.createVerticalBox();
        final JPanel panel = new JPanel();
        panel.add(new JLabel("Cube Position:"));
        panel.add(xcoord);
        panel.add(ycoord);
        panel.add(zcoord);
        verticalBox.add(panel);
        content.add(verticalBox, BorderLayout.CENTER);
        content.add(apply, BorderLayout.SOUTH);
    }

    /** {@inheritDoc} */
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            pack();
            setLocationRelativeTo(null);
        }
    }

}
