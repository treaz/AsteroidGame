package com.horia.chapter8;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class MouseTest extends JFrame implements MouseListener, MouseMotionListener {

	// declare some mouse event variables
	int clickx, clicky;
	int pressx, pressy;
	int releasex, releasey;
	int enterx, entery;
	int exitx, exity;
	int dragx, dragy;
	int movex, movey;
	int mousebutton;
	List<Rectangle> rectList = new ArrayList<Rectangle>();

	public static void main(String[] args) {
		new MouseTest();
	}

	public MouseTest() {
		super("Mouse Test");
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		addMouseMotionListener(this);
	}// redraw the window

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle(0, 0, 500, 400));
		g2d.setColor(Color.BLACK);
		g2d.drawString("Mouse clicked " + mousebutton + " at " + clickx + ","
				+ clicky, 10, 40);
		g2d.drawString("Mouse entered at " + enterx + "," + entery, 10, 55);
		g2d.drawString("Mouse exited at " + exitx + "," + exity, 10, 70);
		g2d.drawString("Mouse pressed " + mousebutton + " at " + pressx + ","
				+ pressy, 10, 85);
		g2d.drawString("Mouse released " + mousebutton + " at " + releasex
				+ "," + releasey, 10, 100);
		g2d.drawString("Mouse dragged at " + dragx + "," + dragy, 10, 115);
		g2d.drawString("Mouse moved at " + movex + "," + movey, 10, 130);
		if (mousebutton==1 || mousebutton==2 || mousebutton==3) {
			rectList.add(new Rectangle(clickx, clicky, 2, 2));
		}
		for (Rectangle rect:rectList) {
			if (mousebutton==3) {
			g2d.setColor(Color.GREEN);
			}
			g2d.fill(rect);
		}
	}

	// custom method called by mouse events to report button status
	private void checkButton(MouseEvent e) {
		// check the mouse buttons
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mousebutton = 1;
			break;
		case MouseEvent.BUTTON2:
			mousebutton = 2;
			break;
		case MouseEvent.BUTTON3:
			mousebutton = 3;
			break;
		default:
			mousebutton = 0;
		}
	}

	public void mouseClicked(MouseEvent e) {
		// save the mouse position values
		clickx = e.getX();
		clicky = e.getY();
		// get an update on buttons
		checkButton(e);
		// refresh the screen (call the paint event)
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		enterx = e.getX();
		entery = e.getY();
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		exitx = e.getX();
		exity = e.getY();
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		pressx = e.getX();
		pressy = e.getY();
		checkButton(e);
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		releasex = e.getX();
		releasey = e.getY();
		checkButton(e);
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		dragx = e.getX();
		dragy = e.getY();
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		movex = e.getX();
		movey = e.getY();
		repaint();
	}
}
