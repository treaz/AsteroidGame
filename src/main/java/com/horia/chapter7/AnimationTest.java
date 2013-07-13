package com.horia.chapter7;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;

public class AnimationTest extends JFrame implements Runnable {
	static int ScreenWidth = 640;
	static int ScreenHeight = 480;
	Thread gameloop;
	Random rand = new Random();
	// double buffer objects
	BufferedImage backbuffer;
	Graphics2D g2d;
	// sprite variables
	Image image;
	int spriteNo = 10;
	Point[] pos = new Point[spriteNo];
	// animation variables
	int[] currentFrame = new int[spriteNo];
	int totalFrames = 30;
	int animationDirection = 1;
	int[] frameCount = new int[spriteNo];
	int[] frameDelay = new int[spriteNo];

	public static void main(String[] args) {
		new AnimationTest();
	}

	public AnimationTest() {
		super("Animation Test");
		setSize(ScreenWidth, ScreenHeight);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// create the back buffer for smooth graphics
		backbuffer = new BufferedImage(ScreenWidth, ScreenHeight,
				BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		// load the ball animation strip
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("../../../explosion.png"));
		for (int i = 0; i < frameDelay.length; i++) {
			frameDelay[i] = rand.nextInt(20);
			frameCount[i] = 0;
			currentFrame[i]=0;
			pos[i] = new Point(rand.nextInt(600), rand.nextInt(600));
		}
		gameloop = new Thread(this);
		gameloop.start();
	}

	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {
		}
		return url;
	}

	public void run() {
		Thread t = Thread.currentThread();
		while (t == gameloop) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameUpdate();
		}
	}

	public void gameUpdate() {
		// clear the background
		g2d.setColor(Color.BLACK);
		g2d.fill(new Rectangle(0, 0, ScreenWidth - 1, ScreenHeight - 1));
		// draw the current frame of animation
		for (int i = 0; i < pos.length; i++) {
			drawFrame(image, g2d, pos[i].x, pos[i].y, 5, currentFrame[i], 64, 64);
		}

		g2d.setColor(Color.WHITE);
		// g2d.drawString("Position: " + pos.x + "," + pos.y, 10, 50);
		g2d.drawString("Animation: " + currentFrame, 10, 70);
		// see if it’s time to animate
		
		for (int i = 0; i < pos.length; i++) {
			frameCount[i]++;
			if (frameCount[i] > frameDelay[i]) {
				frameCount[i] = 0;
				// update the animation frame
				currentFrame[i] += animationDirection;
				if (currentFrame[i] > totalFrames - 1) {
					currentFrame[i] = 0;
					pos[i].x = rand.nextInt(ScreenWidth - 128);
					pos[i].y = rand.nextInt(ScreenHeight - 128);
				} else if (currentFrame[i] < 0) {
					currentFrame[i] = totalFrames - 1;
				}
			}
		}
		repaint();
	}

	public void paint(Graphics g) {
		// draw the back buffer to the screen
		g.drawImage(backbuffer, 0, 0, this);
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
