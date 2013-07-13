package com.horia.chapter5;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;

public class RandomImages extends JFrame {
	private Image image;
	private Image image2;

	public static void main(String[] args) {
		new RandomImages();
	}

	// applet init event
	public RandomImages() {
		super("RandomImages");
		setSize(600, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("raumschiffe png/schiff 10000.png"));
		image2 = tk.getImage(getURL("raumschiffe png/schiff 20000.png"));
	}

	// identity transformation
	AffineTransform identity = new AffineTransform();

	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {
		}
		return url;
	}

	// applet paint event
	public void paint(Graphics g) {
		// create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		// working transform object
		AffineTransform trans = new AffineTransform();
		// random number generator
		Random rand = new Random();
		// applet window width/height
		int width = getSize().width;
		int height = getSize().height;
		// fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		// draw the image multiple times
		for (int n = 0; n < 50; n++) {
			trans.setTransform(identity);
			// move, rotate, scale the image randomly
			trans.translate(rand.nextInt() % width, rand.nextInt() % height);
			trans.rotate(Math.toRadians(360 * rand.nextDouble()));
			double scale = rand.nextDouble() + 1;
			trans.scale(scale, scale);
			// draw the image
			g2d.drawImage(image, trans, this);
			trans.setTransform(identity);
			// move, rotate, scale the image randomly
			trans.translate(rand.nextInt() % width, rand.nextInt() % height);
			trans.rotate(Math.toRadians(360 * rand.nextDouble()));
			trans.scale(scale, scale);
			// draw the image
			g2d.drawImage(image2, trans, this);
		}
	}
}