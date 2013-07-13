package com.horia.chapter9;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class Exercise2 extends JFrame implements KeyListener {

	SoundClip sc = new SoundClip();
	SoundClip sc2 = new SoundClip();
	MidiSequence md = new MidiSequence();
	MidiSequence md2 = new MidiSequence();
	Map sounds = new HashMap();
	Graphics2D g2d = null;
	private int keyCode;
	private int lastKeyCode = 0;

	public Exercise2() {
		super("Exercise1");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		md.load("title-music.mid");
		md2.load("GUNSHOTs.mid");
		sc.load("gong.wav");
		sc2.load("GUNSHOT.wav");
		sounds.put(49, md);
		for (int i = 50; i < 58; i++) {
			sounds.put(i, md2);
		}

		sounds.put(KeyEvent.VK_SPACE, sc);
		sounds.put(KeyEvent.VK_ENTER, sc2);
		addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g2d = (Graphics2D) g;
	}

	public static void main(String[] args) {
		new Exercise2();
	}

	@Override
	public void keyPressed(KeyEvent k) {
		keyCode = k.getKeyCode();
		Object lastSong = sounds.get(lastKeyCode);
		if (null != lastSong) {
			if (lastSong instanceof SoundClip) {
				((SoundClip) lastSong).stop();
			} else {
				((MidiSequence) lastSong).stop();
			}
		}
		Object nextSong = sounds.get(keyCode);
		if (null != nextSong) {
			if (nextSong instanceof SoundClip) {
				((SoundClip) nextSong).play();
			} else {
				((MidiSequence) nextSong).play();
			}
		}
		lastKeyCode = keyCode;
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
