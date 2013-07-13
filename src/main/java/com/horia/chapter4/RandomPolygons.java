package com.horia.chapter4;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class RandomPolygons extends Applet implements KeyListener {
	private int[] xpoints = { 0, -10, -7, 7, 10 };
	private int[] ypoints = { -10, -2, 10, 10, -2 };
	private int[] astx = { -20, -13, 0, 20, 22, 20, 12, 2, -10, -22, -16 };
	private int[] asty = { 20, 23, 17, 20, 16, -20, -22, -14, -17, -20, -5 };
	// here’s the shape used for drawing
	private Polygon poly;
	private Polygon asteroid;

	// applet init event
	public void init() {
		poly = new Polygon(xpoints, ypoints, xpoints.length);
		asteroid = new Polygon(astx, asty, asty.length);
		addKeyListener(this);
	}

	// applet paint event
	public void paint(Graphics g) {
		// create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		// save the window width/height
		int width = getSize().width;
		int height = getSize().height;
		// fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		drawShapes(g2d, poly, width, height);
		drawShapes(g2d, asteroid, width, height);
	}

	private void drawShapes(Graphics2D g2d, Polygon polygon, int width,
			int height) {
		// save the identity transform
		AffineTransform identity = new AffineTransform();
		// create a random number generator
		Random rand = new Random();
		for (int n = 0; n < 300; n++) {
			// reset Graphics2D to the identity transform
			g2d.setTransform(identity);
			// move, rotate, and scale the shape randomly
			g2d.translate(rand.nextInt() % width, rand.nextInt() % height);
			g2d.rotate(Math.toRadians(360 * rand.nextDouble()));
			g2d.scale(5 * rand.nextDouble(), 5 * rand.nextDouble());
			// draw the shape with a random color
			g2d.setColor(new Color(rand.nextInt()));
			g2d.fill(polygon);
		}

	}

	@Override
	public void keyPressed(KeyEvent k) {
		switch (k.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			repaint();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}