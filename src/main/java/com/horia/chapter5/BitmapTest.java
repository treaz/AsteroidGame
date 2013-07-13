package com.horia.chapter5;

//BitmapTest program
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;

public class BitmapTest extends JFrame implements Runnable {
	Image image;
	Thread gameloop;
	Random rand = new Random();

	public static void main(String[] args) {
		new BitmapTest();
	}

	public BitmapTest() {
		super("Opaque Bitmap Test");
		setSize(640, 480);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("asteroid2.png"));
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
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int width = 640 - image.getWidth(this);
		int height = 480 - image.getHeight(this);
		g2d.drawImage(image, rand.nextInt(width), rand.nextInt(height), this);
	}
}