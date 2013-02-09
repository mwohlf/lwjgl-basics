package net.wohlfart.tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CharFrame extends JFrame {

	public static void main(String[] args) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new CharFrame().setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}


	CharFrame() {
		addContent();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void addContent() {
		FontRenderer fontRenderer = new FontRenderer();
		fontRenderer.init();
		final Image image = fontRenderer.getCharacterAtlas().getImage();

		final Container content = getContentPane();

		content.add(new JPanel() {

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(image.getWidth(null), image.getHeight(null));
			}

		    @Override
		    public void paintComponent(Graphics g) {
		        super.paintComponent(g);
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
