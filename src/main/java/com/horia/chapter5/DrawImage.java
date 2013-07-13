package com.horia.chapter5;

//DrawImage program
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;

public class DrawImage extends JFrame implements KeyListener{
	private Image image;

	public static void main(String[] args) {
		new DrawImage();
	}

	public DrawImage() {
		super("DrawImage");
		addKeyListener(this);
		setSize(600, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("castle.png"));
	}

	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {
		}
		return url;
	}
	
	AffineTransform identity = new AffineTransform();


	double scale =0.1;
	
	public void paint(Graphics g) {
		// zoom in/out function
		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		// if scale <1 this doesn't work.
		if (plusP) {
			scale++;
			plusP = false;
		} else if (minusP) {
			scale--;
			minusP = false;
		}
		trans.scale(scale, scale);
		// create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		// fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		// draw the image
		g2d.drawImage(image, trans, this);
	}

	boolean plusP = false;
	boolean minusP = false;
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_1:
			System.out.println("+");
			plusP = true;
			repaint();
			break;
		case KeyEvent.VK_2:
			minusP = true;
			repaint();
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}