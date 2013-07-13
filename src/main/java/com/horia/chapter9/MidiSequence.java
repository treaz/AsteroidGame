package com.horia.chapter9;

import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiSequence {
	// primary midi sequencer object
	private Sequencer sequencer;
	// provide Sequence as a read-only property
	private Sequence song;

	public Sequence getSong() {
		return song;
	}

	// filename property is read-only
	private String filename;

	public String getFilename() {
		return filename;
	}

	// looping property for looping continuously
	private boolean looping = false;

	public boolean getLooping() {
		return looping;
	}

	public void setLooping(boolean _looping) {
		looping = _looping;
	}

	// repeat property for looping a fixed number of times
	private int repeat = 0;

	public void setRepeat(int _repeat) {
		repeat = _repeat;
	}

	public int getRepeat() {
		return repeat;
	}

	// returns whether the sequence is ready for action
	public boolean isLoaded() {
		return (boolean) (sequencer.isOpen());
	}

	// primary constructor
	public MidiSequence() {
		try {
			// fire up the sequencer
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
		}
	}

	// overloaded constructor accepts midi filename
	public MidiSequence(String midifile) {
		this(); // call default constructor first
		load(midifile); // load the midi file
	}

	private URL getURL(String filename) {
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {
		}
		return url;
	}

	// load a midi file into a sequence
	public boolean load(String midifile) {
		try {
			// load the midi file into the sequencer
			filename = midifile;
			song = MidiSystem.getSequence(getURL(filename));
			sequencer.setSequence(song);
			sequencer.open();
			return true;
		} catch (InvalidMidiDataException e) {
			return false;
		} catch (MidiUnavailableException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}// play the midi sequence

	public void play() {
		if (!sequencer.isOpen())
			return;
		if (looping) {
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
		} else {
			sequencer.setLoopCount(repeat);
			sequencer.start();
		}
	}

	// stop the midi sequence
	public void stop() {
		sequencer.stop();
	}
}