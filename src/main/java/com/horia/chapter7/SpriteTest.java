package com.horia.chapter7;

//SpriteTest program
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;

import com.horia.chapter6.ImageEntity;
import com.horia.chapter6.Sprite;

public class SpriteTest extends JFrame implements Runnable {
	int screenWidth = 640;
	int screenHeight = 480;
	// double buffer objects
	BufferedImage backbuffer;
	Graphics2D g2d;
	Sprite asteroid;
	Sprite asteroid2;
	ImageEntity background;
	Thread gameloop;
	Random rand = new Random();
	Point pos = new Point(300, 300);
	int currentFrame = 0;
	Image image;
	private int frameCount = 0;
	int frameDelay = 10;
	int animationDirection = 1;
	int totalFrames = 30;
	private boolean crash;

	public static void main(String[] args) {
		new SpriteTest();
	}

	public SpriteTest() {
		super("Sprite Test");
		setSize(640, 480);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// create the back buffer for smooth graphics
		backbuffer = new BufferedImage(screenWidth, screenHeight,
				BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		// load the background
		background = new ImageEntity(this);
		background.load("../../../bluespace.png");
		// load the asteroid sprite
		asteroid = new Sprite(this, g2d);
		asteroid.load("../../../asteroid2.png");
		asteroid2 = new Sprite(this, g2d);
		asteroid2.load("../../../asteroid2.png");
		gameloop = new Thread(this);
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("../../../explosion.png"));
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
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// draw the background
			g2d.drawImage(background.getImage(), 0, 0, screenWidth - 1,
					screenHeight - 1, this);
			int width = screenWidth - asteroid.imageWidth() - 1;
			int height = screenHeight - asteroid.imageHeight() - 1;
			if (crash) {
				pos.x = (asteroid.center().x + asteroid2.center().x) / 2;
				pos.y = (asteroid.center().y + asteroid2.center().y) / 2;
				asteroid.draw();
				asteroid2.draw();
				drawFrame(image, g2d, pos.x-32, pos.y-32, 5, currentFrame, 64, 64);

				frameCount++;
				if (frameCount > frameDelay) {
					frameCount = 0;
					// update the animation frame
					currentFrame += animationDirection;
					if (currentFrame > totalFrames - 1) {
						currentFrame = 0;
						crash = false;
					} else if (currentFrame < 0) {
						currentFrame = totalFrames - 1;
					}
				}
				repaint();
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Point point = new Point(rand.nextInt(width),
						rand.nextInt(height));
				asteroid.setPosition(point);
				asteroid.transform();
				asteroid.draw();
				Point point1 = new Point(rand.nextInt(width),
						rand.nextInt(height));
				asteroid2.setPosition(point1);
				asteroid2.transform();
				asteroid2.draw();
				repaint();
				if (asteroid.collidesWith(asteroid2)) {
					crash = true;
				}
			}
		}
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