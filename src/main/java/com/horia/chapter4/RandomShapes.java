package com.horia.chapter4;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class RandomShapes extends Applet {
	// here’s the shape used for drawing
	private Shape shape;

	// applet init event
	public void init() {
		shape = new Rectangle2D.Double(-1.0, -1.0, 1.0, 1.0);
	}

	// applet paint event
	public void paint(Graphics g) {
		// create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		// save the identity transform
		AffineTransform identity = new AffineTransform();
		// create a random number generator
		Random rand = new Random();
		// save the window width/height
		int width = getSize().width;
		int height = getSize().height;
		// fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		for (int n = 0; n < 300; n++) {
			// reset Graphics2D to the identity transform
			g2d.setTransform(identity);
			// move, rotate, and scale the shape randomly
			g2d.translate(rand.nextInt() % width, rand.nextInt() % height);
			g2d.rotate(Math.toRadians(360 * rand.nextDouble()));
			g2d.scale(60 * rand.nextDouble(), 60 * rand.nextDouble());
			// draw the shape with a random color
			g2d.setColor(new Color(rand.nextInt()));
			g2d.fill(shape);
		}
	}
}