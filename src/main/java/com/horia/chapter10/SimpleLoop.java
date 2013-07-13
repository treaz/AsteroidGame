package com.horia.chapter10;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;

public class SimpleLoop extends JFrame implements KeyListener, MouseListener {
	Random rand = new Random();

	public static void main(String[] args) {
		new SimpleLoop();
	}

	public SimpleLoop() {
		super("SimpleLoop");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// create a random rectangle
		int w = rand.nextInt(100);
		int h = rand.nextInt(100);
		int x = rand.nextInt(getSize().width - w);
		int y = rand.nextInt(getSize().height - h);
		Rectangle rect = new Rectangle(x, y, w, h);
		// generate a random color
		int red = rand.nextInt(256);
		int green = rand.nextInt(256);
		int blue = rand.nextInt(256);
		g2d.setColor(new Color(red, green, blue));
		// draw the rectangle
		g2d.fill(rect);
	}

	// handle keyboard events
	public void keyReleased(KeyEvent k) {
	}

	public void keyTyped(KeyEvent k) {
	}

	public void keyPressed(KeyEvent k) {
		repaint();
	}

	// handle mouse events
	public void mouseEntered(MouseEvent m) {
	}

	public void mouseExited(MouseEvent m) {
	}

	public void mouseReleased(MouseEvent m) {
	}

	public void mouseClicked(MouseEvent m) {
	}

	public void mousePressed(MouseEvent m) {
		repaint();
	}
}
