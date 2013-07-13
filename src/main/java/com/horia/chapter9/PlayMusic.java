package com.horia.chapter9;

import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JFrame;

public class PlayMusic extends JFrame {
	String filename = "title-music.mid";
	Sequence song;

	public static void main(String[] args) {
		new PlayMusic();
	}

	public PlayMusic() {
		super("PlayMusic");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			song = MidiSystem.getSequence(getURL(filename));
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.setSequence(song);
			sequencer.open();
			sequencer.start();
		} catch (InvalidMidiDataException e) {
		} catch (MidiUnavailableException e) {
		} catch (IOException e) {
		}
		repaint();
	}

	public void paint(Graphics g) {
		g.drawString("Midi File: " + filename, 10, 40);
		g.drawString("Resolution: " + song.getResolution(), 10, 55);
		g.drawString("Tick length: " + song.getTickLength(), 10, 70);
		g.drawString("Tracks: " + song.getTracks().length, 10, 85);
		g.drawString("Patches: " + song.getPatchList().length, 10, 100);
	}

	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
}