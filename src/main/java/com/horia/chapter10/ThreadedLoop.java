package com.horia.chapter10;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;

public class ThreadedLoop extends JFrame implements Runnable {
	Random rand = new Random();
	// the main thread
	Thread thread;
	// count the number of rectangles drawn
	long count = 0, total = 0;
	private Image image;
	private Point pos;
	private int currentFrame = 0;
	private int totalFrames = 30;
	private int animationDirection = 1;
	private int frameDelay = 5;
	private int frameCount = 0;
	static int ScreenWidth = 640;
	static int ScreenHeight = 480;

	public static void main(String[] args) {
		new ThreadedLoop();
	}

	public ThreadedLoop() {
		super("ThreadedLoop");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("../../../explosion.png"));
		thread = new Thread(this);
		thread.start();
	}

	// thread run event
	public void run() {
		long start = System.currentTimeMillis();
		// acquire the current thread
		Thread current = Thread.currentThread();
		// here’s the new game loop
		while (current == thread) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// draw something
			repaint();
			// figure out how fast it’s running
			if (start + 1000 < System.currentTimeMillis()) {
				start = System.currentTimeMillis();
				total = count;
				count = 0;
			}
		}
	}

	// JFrame paint event
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// create a random rectangle
		// int w = rand.nextInt(100);
		// int h = rand.nextInt(100);
		// int x = rand.nextInt(getSize().width - w);
		// int y = rand.nextInt(getSize().height - h);
		// Rectangle rect = new Rectangle(x, y, w, h);
		// // generate a random color
		// int red = rand.nextInt(256);
		// int green = rand.nextInt(256);
		// int blue = rand.nextInt(256);
		// g2d.setColor(new Color(red, green, blue));
		// // draw the rectangle
		// g2d.fill(rect);
		pos = new Point(rand.nextInt(600), rand.nextInt(600));
		drawFrame(image, g2d, pos.x, pos.y, 5, currentFrame, 64, 64);
		frameCount++;
		if (frameCount  > frameDelay) {
			frameCount = 0;
			// update the animation frame
			currentFrame += animationDirection;
			if (currentFrame > totalFrames - 1) {
				currentFrame = 0;
				pos.x = rand.nextInt(ScreenWidth - 128);
				pos.y = rand.nextInt(ScreenHeight - 128);
			} else if (currentFrame < 0) {
				currentFrame = totalFrames  - 1;
			}
		}
		
		// add another to the counter
		count++;
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle(0, 360, 600, 600));
		g2d.setColor(Color.BLACK);
		g2d.drawString("Rectangles per second: " + total, 10, 380);
	}

	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {
		}
		return url;
	}
	// draw a single frame of animation
		public void drawFrame(Image source, Graphics2D dest, int x, int y,
				int cols, int frame, int width, int height) {
			int fx = (frame % cols) * width;
			int fy = (frame / cols) * height;
			dest.drawImage(source, x, y, x + width, y + height, fx, fy, fx + width,
					fy + height, this);
		}
}