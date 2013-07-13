package com.horia.chapter11;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

//Primary class for the game
public class GalacticWar extends Applet implements Runnable, KeyListener,
		MouseMotionListener, MouseListener, MouseWheelListener {
	// the main thread becomes the game loop
	Thread gameloop;
	// use this as a double buffer
	BufferedImage backbuffer;
	// the main drawing object for the back buffer
	Graphics2D g2d;
	// toggle for drawing bounding boxes
	boolean showBounds = false;
	// create the asteroid array
	int ASTEROIDS = 20;
	Asteroid[] ast = new Asteroid[ASTEROIDS];
	// create the bullet array
	int BULLETS = 10;
	Bullet[] bullet = new Bullet[BULLETS];
	int currentBullet = 0;
	// the player’s ship
	ImageEntity ship = new ImageEntity(this);
	// create the identity transform (0,0)
	AffineTransform identity = new AffineTransform();
	// create a random number generator
	Random rand = new Random();

	// applet init event
	public void init() {
		// create the back buffer for smooth graphics
		backbuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		// set up the ship
		// set up the ship
		ship.setX(320);
		ship.setY(240);
		ship.load("spaceship.png");
		ship.setGraphics(g2d);
		// set up the bullets
		for (int n = 0; n < BULLETS; n++) {
			bullet[n] = new Bullet();
		}
		// create the asteroids
		for (int n = 0; n < ASTEROIDS; n++) {
			ast[n] = new Asteroid();
			ast[n].setRotationVelocity(rand.nextInt(3) + 1);
			ast[n].setX((double) rand.nextInt(600) + 20);
			ast[n].setY((double) rand.nextInt(440) + 20);
			ast[n].setMoveAngle(rand.nextInt(360));
			double ang = ast[n].getMoveAngle() - 90;
			ast[n].setVelX(calcAngleMoveX(ang));
			ast[n].setVelY(calcAngleMoveY(ang));
		}
		// start the user input listener
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	// applet update event to redraw the screen
	public void update(Graphics g) {
		// start off transforms at identity
		g2d.setTransform(identity);
		// erase the background
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		// print some status information
		g2d.setColor(Color.WHITE);
		g2d.drawString(
				"Ship: " + Math.round(ship.getX()) + ","
						+ Math.round(ship.getY()), 5, 10);
		g2d.drawString("Move angle: " + Math.round(ship.getMoveAngle()) + 90,
				5, 25);
		g2d.drawString("Face angle: " + Math.round(ship.getFaceAngle()), 5, 40);
		// draw the game graphics
		drawShip();
		drawBullets();
		drawAsteroids();
		// repaint the applet window
		paint(g);
	}

	// drawShip called by applet update event
	public void drawShip() {
		// transform and draw the ship
		ship.transform();
		ship.draw();
		// draw bounding rectangle around ship
		if (showBounds) {
			g2d.setTransform(identity);
			g2d.setColor(Color.BLUE);
			g2d.draw(ship.getBounds());
		}
	}

	// drawBullets called by applet update event
	public void drawBullets() {
		// iterate through the array of bullets
		for (int n = 0; n < BULLETS; n++) {
			// is this bullet currently in use?
			if (bullet[n].isAlive()) {
				// draw the bullet
				g2d.setTransform(identity);
				g2d.translate(bullet[n].getX(), bullet[n].getY());
				g2d.setColor(Color.MAGENTA);
				g2d.draw(bullet[n].getShape());
				if (showBounds) {
					g2d.setTransform(identity);
					g2d.setColor(Color.RED);
					g2d.draw(bullet[n].getBounds());
				}
			}
		}
	}

	// drawAsteroids called by applet update event
	public void drawAsteroids() {
		// iterate through the asteroids array
		for (int n = 0; n < ASTEROIDS; n++) {
			// is this asteroid being used?
			if (ast[n].isAlive()) {
				// draw the asteroid
				g2d.setTransform(identity);
				g2d.translate(ast[n].getX(), ast[n].getY());
				g2d.rotate(Math.toRadians(ast[n].getMoveAngle()));
				g2d.setColor(Color.DARK_GRAY);
				g2d.fill(ast[n].getShape());
				if (showBounds) {
					g2d.setTransform(identity);
					g2d.setColor(Color.RED);
					g2d.draw(ast[n].getBounds());
				}
			}
		}
	}

	// applet window repaint event- -draw the back buffer
	public void paint(Graphics g) {
		// draw the back buffer onto the applet window
		g.drawImage(backbuffer, 0, 0, this);
	}

	// thread start event - start the game loop running
	public void start() {
		// create the gameloop thread for real-time updates
		gameloop = new Thread(this);
		gameloop.start();
	}

	// thread run event (game loop)
	public void run() {
		// acquire the current thread
		Thread t = Thread.currentThread();
		// keep going as long as the thread is alive
		while (t == gameloop) {
			try {
				// update the game loop
				gameUpdate();
				// target framerate is 50 fps
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	// thread stop event
	public void stop() {
		// kill the gameloop thread
		gameloop = null;
	}

	// move and animate the objects in the game
	private void gameUpdate() {
		// TODO controlling the ship with the help of the mouse doesn't work
		controlShipByMouse(movedMouse);
		controlShip();
		updateShip();
		updateBullets();
		updateAsteroids();
		checkCollisions();
	}

	private void controlShip() {
		// when left is pressed rotate the ship by 5 degrees
		if (leftPressed) {
			ship.incFaceAngle(-5);
			if (ship.getFaceAngle() < 0)
				ship.setFaceAngle(360 - 5);
		}
		// when right is pressed rotate the ship by 5 degrees
		if (rightPressed) {
			ship.incFaceAngle(5);
			if (ship.getFaceAngle() > 360)
				ship.setFaceAngle(5);
		}
		// adds thrust to ship (1/10 normal speed)
		if (upPressed) {
			ship.setMoveAngle(ship.getFaceAngle() - 90);
			ship.incVelX(calcAngleMoveX(ship.getMoveAngle()) * 0.1);
			ship.incVelY(calcAngleMoveY(ship.getMoveAngle()) * 0.1);
		}
	}

	private void controlShipByMouse(int x) {
		ship.incFaceAngle(-x);
		if (ship.getFaceAngle() < 0)
			ship.setFaceAngle(360 - x);
		movedMouse = 0;
	}

	private void accelerateShipByMouse() {
		ship.setMoveAngle(ship.getFaceAngle() - 90);
		ship.incVelX(calcAngleMoveX(ship.getMoveAngle()) * 0.1);
		ship.incVelY(calcAngleMoveY(ship.getMoveAngle()) * 0.1);
	}

	// Update the ship position based on velocity
	public void updateShip() {
		// update ship’s X position
		ship.incX(ship.getVelX());
		// wrap around left/right
		if (ship.getX() < -10)
			ship.setX(getSize().width + 10);
		else if (ship.getX() > getSize().width + 10)
			ship.setX(-10);
		// update ship’s Y position
		ship.incY(ship.getVelY());
		// wrap around top/bottom
		if (ship.getY() < -10)
			ship.setY(getSize().height + 10);
		else if (ship.getY() > getSize().height + 10)
			ship.setY(-10);
	}

	// Update the bullets based on velocity
	public void updateBullets() {
		// move each of the bullets
		for (int n = 0; n < BULLETS; n++) {
			// is this bullet being used?
			if (bullet[n].isAlive()) {
				// update bullet’s x position
				bullet[n].incX(bullet[n].getVelX());
				// bullet disappears at left/right edge
				if (bullet[n].getX() < 0 || bullet[n].getX() > getSize().width) {
					bullet[n].setAlive(false);
				}
				// update bullet’s y position
				bullet[n].incY(bullet[n].getVelY());
				// bullet disappears at top/bottom edge
				if (bullet[n].getY() < 0 || bullet[n].getY() > getSize().height) {
					bullet[n].setAlive(false);
				}
			}
		}
	}

	// Update the asteroids based on velocity
	public void updateAsteroids() {
		// move and rotate the asteroids
		for (int n = 0; n < ASTEROIDS; n++) {
			// is this asteroid being used?
			if (ast[n].isAlive()) {
				// update the asteroid’s X value
				ast[n].incX(ast[n].getVelX());
				// warp the asteroid at screen edges
				if (ast[n].getX() < -20)
					ast[n].setX(getSize().width + 20);
				else if (ast[n].getX() > getSize().width + 20)
					ast[n].setX(-20);
				// update the asteroid’s Y value
				ast[n].incY(ast[n].getVelY());
				// warp the asteroid at screen edges
				if (ast[n].getY() < -20)
					ast[n].setY(getSize().height + 20);
				else if (ast[n].getY() > getSize().height + 20)
					ast[n].setY(-20);
				// update the asteroid’s rotation
				ast[n].incMoveAngle(ast[n].getRotationVelocity());
				// keep the angle within 0-359 degrees
				if (ast[n].getMoveAngle() < 0)
					ast[n].setMoveAngle(360 - ast[n].getRotationVelocity());
				else if (ast[n].getMoveAngle() > 360)
					ast[n].setMoveAngle(ast[n].getRotationVelocity());
			}
		}
	}

	// Test asteroids for collisions with ship or bullets
	public void checkCollisions() {
		// iterate through the asteroids array
		for (int m = 0; m < ASTEROIDS; m++) {
			// is this asteroid being used?
			if (ast[m].isAlive()) {
				// check for collision with bullet
				for (int n = 0; n < BULLETS; n++) {
					// is this bullet being used?
					if (bullet[n].isAlive()) {
						// perform the collision test
						if (ast[m].getBounds().contains(bullet[n].getX(),
								bullet[n].getY())) {
							bullet[n].setAlive(false);
							ast[m].setAlive(false);
							continue;
						}
					}
				}
				// check for collision with ship
				if (ast[m].getBounds().intersects(ship.getBounds())) {
					ast[m].setAlive(false);
					ship.setX(320);
					ship.setY(240);
					ship.setFaceAngle(0);
					ship.setVelX(0);
					ship.setVelY(0);
					continue;
				}
			}
		}
	}

	boolean leftPressed = false;
	boolean rightPressed = false;
	boolean upPressed = false;
	private int movedMouse;
	private int accelerateMouseWheel;

	// key listener events
	public void keyReleased(KeyEvent k) {
		int keyCode = k.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			leftPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = false;
			break;
		case KeyEvent.VK_UP:
			upPressed = false;
			break;
		}
	}

	public void keyTyped(KeyEvent k) {
	}

	public void keyPressed(KeyEvent k) {
		int keyCode = k.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			// left arrow sets a flag
			leftPressed = true;
			break;
		case KeyEvent.VK_RIGHT:
			// right arrow sets a flag
			rightPressed = true;
			break;
		case KeyEvent.VK_UP:
			// up arrow sets a flag
			upPressed = true;
			break;
		// Ctrl, Enter, or Space can be used to fire weapon
		case KeyEvent.VK_CONTROL:
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			// fire a bullet
			currentBullet++;
			if (currentBullet > BULLETS - 1)
				currentBullet = 0;
			bullet[currentBullet].setAlive(true);
			// point bullet in same direction ship is facing
			bullet[currentBullet].setX(ship.getCenterX());
			bullet[currentBullet].setY(ship.getCenterY());
			bullet[currentBullet].setMoveAngle(ship.getFaceAngle() - 90);
			// fire bullet at angle of the ship
			double angle = bullet[currentBullet].getMoveAngle();
			bullet[currentBullet].setVelX(calcAngleMoveX(angle) * 3);
			bullet[currentBullet].setVelY(calcAngleMoveY(angle) * 3);
			break;
		case KeyEvent.VK_B:
			showBounds = !showBounds;
		}
	}

	// calculate X movement value based on direction angle
	public double calcAngleMoveX(double angle) {
		return (double) (Math.cos(angle * Math.PI / 180));
	}

	// calculate Y movement value based on direction angle
	public double calcAngleMoveY(double angle) {
		return (double) (Math.sin(angle * Math.PI / 180));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// fire a bullet
		currentBullet++;
		if (currentBullet > BULLETS - 1)
			currentBullet = 0;
		bullet[currentBullet].setAlive(true);
		// point bullet in same direction ship is facing
		bullet[currentBullet].setX(ship.getCenterX());
		bullet[currentBullet].setY(ship.getCenterY());
		bullet[currentBullet].setMoveAngle(ship.getFaceAngle() - 90);
		// fire bullet at angle of the ship
		double angle = bullet[currentBullet].getMoveAngle();
		bullet[currentBullet].setVelX(calcAngleMoveX(angle) * 3);
		bullet[currentBullet].setVelY(calcAngleMoveY(angle) * 3);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		movedMouse = e.getX();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
			accelerateShipByMouse();
	}
}