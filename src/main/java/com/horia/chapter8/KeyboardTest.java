package com.horia.chapter8;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class KeyboardTest extends JFrame implements KeyListener {
	int keyCode;
	char keyChar;
	private int newFont = 13;

	public static void main(String[] args) {
		new KeyboardTest();
	}

	public KeyboardTest() {
		super("Keyboard Test");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle(0, 0, 500, 400));
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Ariel", Font.PLAIN, newFont));
		g2d.drawString("Press a key. . .", 20, 40);
		g2d.drawString("Key code: " + keyCode, 20, 60);
		g2d.drawString("Key char: " + keyChar, 20, 80);
	}

	public void keyPressed(KeyEvent e) {
		keyCode = e.getKeyCode();
		keyChar = ' ';
		if (keyCode > 48 && keyCode < 58) {
			newFont = keyCode - 40;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
		keyChar = e.getKeyChar();
		repaint();
	}
}