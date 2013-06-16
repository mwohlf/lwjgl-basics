package net.wohlfart.gl.elements.debug;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.wohlfart.gl.view.CanMove;

import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("serial")
public class PositionFrame extends JFrame {

    protected final JTextField position = new JTextField();
    protected final CanMove canMove;

    public PositionFrame(CanMove canMove) {
        this.canMove = canMove;
        addContent();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addContent() {
        final Container content = getContentPane();
        content.add(createControlsPanel(), BorderLayout.CENTER);
    }


    private Component createControlsPanel() {
        position.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = position.getText();
                String[] values = value.split(",");
                if (values.length == 3) {
                    try {
                        int x = Integer.valueOf(values[0]);
                        int y = Integer.valueOf(values[1]);
                        int z = Integer.valueOf(values[2]);
                        Vector3f pos = new  Vector3f(x,y,z);
                        canMove.setPosition(pos);
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        final Box result = Box.createVerticalBox();
        result.add(new JLabel("Position:"));
        result.add(position);
        return result;
    }


    public void setup() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    PositionFrame.this.setVisible(true);
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    @Override
    public void setVisible(boolean isVisible) {
        if (isVisible) {
            pack();
            setLocationRelativeTo(null);
        }
        super.setVisible(isVisible);
    }

}
