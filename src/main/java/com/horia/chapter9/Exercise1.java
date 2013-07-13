package com.horia.chapter9;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Exercise1 extends JFrame implements KeyListener {

	SoundClip sc = new SoundClip();
	SoundClip sc2 = new SoundClip();
	Graphics2D g2d = null;
	private int keyCode;

	public Exercise1() {
		super("Exercise1");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sc.load("gong.wav");
		sc2.load("GUNSHOT.wav");
		addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g2d = (Graphics2D) g;
	}

	public static void main(String[] args) {
		new Exercise1();
	}

	@Override
	public void keyPressed(KeyEvent k) {
		keyCode = k.getKeyCode();
		if (keyCode > 48 && keyCode < 58) {
			sc.play();
		} else {
			sc2.play();
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
