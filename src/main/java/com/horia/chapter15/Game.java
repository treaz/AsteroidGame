package com.horia.chapter15;

/*****************************************************
* Beginning Java Game Programming, 2nd Edition
* by Jonathan S. Harbour
* Applet Game Framework class
*****************************************************/

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

abstract class Game extends Applet implements Runnable, KeyListener,
    MouseListener, MouseMotionListener {

    //the main game loop thread
    private Thread gameloop;

    //internal list of sprites
    private LinkedList _sprites;
    //screen and double buffer related variables
    private BufferedImage backbuffer;

    private Graphics2D g2d;
    private int screenWidth, screenHeight;
    //keep track of mouse position and buttons
    private Point2D mousePos = new Point2D(0,0);

    private boolean mouseButtons[] = new boolean[4];
    //frame rate counters and other timing variables
    private int _frameCount = 0;

    private int _frameRate = 0;
    private int desiredRate;
    private long startTime = System.currentTimeMillis();
    //game pause state
    private boolean _gamePaused = false;
    
    /*****************************************************
     * constructor
     *****************************************************/
    public Game(int frameRate, int width, int height) {
        desiredRate = frameRate;
        screenWidth = width;
        screenHeight = height;
    }
    /*****************************************************
     * applet update event method
     *****************************************************/
    public void update(Graphics g) {
        //calculate frame rate
        _frameCount++;
        if (System.currentTimeMillis() > startTime + 1000) {
            startTime = System.currentTimeMillis();
            _frameRate = _frameCount;
            _frameCount = 0;

            //once every second all dead sprites are deleted
            purgeSprites();
        }
        //this method implemented by sub-class
        gameRefreshScreen();

        //draw the internal list of sprites
        drawSprites();

        paint(g);
    }
    /*****************************************************
      * once every second during the frame update, this method
      * is called to remove all dead sprites from the linked list
      *****************************************************/
     private void purgeSprites() {
         for (int n=0; n < _sprites.size(); n++) {
             AnimatedSprite spr = (AnimatedSprite) _sprites.get(n);
             if (!spr.alive()) {
                 _sprites.remove(n);
             }
         }
     }
    abstract void gameRefreshScreen();

    /*****************************************************
      * draw all active sprites in the sprite list
      * sprites lower in the list are drawn on top
      *****************************************************/
     protected void drawSprites() {
         //draw sprites in reverse order (reverse priority)
         for (int n=0; n<_sprites.size(); n++) {
             AnimatedSprite spr = (AnimatedSprite) _sprites.get(n);
             if (spr.alive()) {
                 spr.updateFrame();
                 spr.transform();
                 spr.draw();
                 spriteDraw(spr);
             }
         }
     }
    abstract void spriteDraw(AnimatedSprite sprite);
    /*****************************************************
      * applet window paint event method
      *****************************************************/
     public void paint(Graphics g) {
         g.drawImage(backbuffer, 0, 0, this);
     }
    /*****************************************************
      * thread run event (game loop)
      *****************************************************/
     public void run() {
         //acquire the current thread
         Thread t = Thread.currentThread();

         //process the main game loop thread
         while (t == gameloop) {
             try {
                 //set a consistent frame rate
                 Thread.sleep(1000 / desiredRate);
             }
             catch(InterruptedException e) {
                 e.printStackTrace();
             }

             //update the internal list of sprites
             updateSprites();
             testCollisions();

             //allow main game to update if needed
             gameTimedUpdate();

             //refresh the screen
             repaint();
         }
     }
    /*****************************************************
      * update the sprite list from the game loop thread
      *****************************************************/
     protected void updateSprites() {
         for (int n=0; n < _sprites.size(); n++) {
             AnimatedSprite spr = (AnimatedSprite) _sprites.get(n);
             if (spr.alive()) {
                 spr.updatePosition();
                 spr.updateRotation();
                 spr.updateAnimation();
                 spriteUpdate(spr);
                 spr.updateLifetime();
                 if (!spr.alive()) {
                     spriteDying(spr);
                 }
             }
         }
     }
    abstract void spriteUpdate(AnimatedSprite sprite);
    abstract void spriteDying(AnimatedSprite sprite);
    /*****************************************************
      * perform collision testing of all active sprites
      *****************************************************/
     protected void testCollisions() {
         //iterate through the sprite list, test each sprite against
         //every other sprite in the list
         for (int first=0; first < _sprites.size(); first++) {

             //get the first sprite to test for collision
             AnimatedSprite spr1 = (AnimatedSprite) _sprites.get(first);
             if (spr1.alive()) {

                 //look through all sprites again for collisions
                 for (int second = 0; second < _sprites.size(); second++) {

                     //make sure this isn't the same sprite
                     if (first != second) {

                         //get the second sprite to test for collision
                         AnimatedSprite spr2 = (AnimatedSprite) _sprites.get(second);
                         if (spr2.alive()) {
                             if (spr2.collidesWith(spr1)) {
                                 spriteCollision(spr1, spr2);
                                 break;
                             }
                             else
                                spr1.setCollided(false);

                         }
                     }
                 }
             }
         }
     }
    abstract void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2);
    abstract void gameTimedUpdate();
    public void mouseDragged(MouseEvent e) {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseDown();
         gameMouseMove();
     }
    /*****************************************************
     * mouse listener events
     *****************************************************/
     public void mousePressed(MouseEvent e) {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseDown();
     }
    public void mouseReleased(MouseEvent e) {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseUp();
     }

    public void mouseMoved(MouseEvent e) {
         checkButtons(e);
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseMove();
     }

    /*****************************************************
      * checkButtons stores the state of the mouse buttons
      *****************************************************/
     private void checkButtons(MouseEvent e) {
             switch(e.getButton()) {
             case MouseEvent.BUTTON1:
                 mouseButtons[1] = true;
                 mouseButtons[2] = false;
                 mouseButtons[3] = false;
                 break;
             case MouseEvent.BUTTON2:
                 mouseButtons[1] = false;
                 mouseButtons[2] = true;
                 mouseButtons[3] = false;
                 break;
             case MouseEvent.BUTTON3:
                 mouseButtons[1] = false;
                 mouseButtons[2] = false;
                 mouseButtons[3] = true;
                 break;
             default:

             }
    }

    abstract void gameMouseDown();

    public void mouseEntered(MouseEvent e) {
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseMove();
     }
    public void mouseExited(MouseEvent e) {
         mousePos.setX(e.getX());
         mousePos.setY(e.getY());
         gameMouseMove();
     }

    abstract void gameMouseMove();

    abstract void gameMouseUp();

    /*****************************************************
     * applet init event method
     *****************************************************/
    public void init() {
        //create the back buffer and drawing surface
        backbuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //create the internal sprite list
        _sprites = new LinkedList<AnimatedSprite>();

        //start the input listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        //this method implemented by sub-class
        gameStartup();
    }

     //declare the game event methods that sub-class must implement
    abstract void gameStartup();
     /*****************************************************
      * thread stop event
      *****************************************************/
     public void stop() {
         //kill the game loop
         gameloop = null;

         //this method implemented by sub-class
         gameShutdown();
     }

     abstract void gameShutdown();

     public void keyPressed(KeyEvent k) {
         gameKeyDown(k.getKeyCode());
     }
     abstract void gameKeyDown(int keyCode);
     public void keyReleased(KeyEvent k) {
         gameKeyUp(k.getKeyCode());
     }

     abstract void gameKeyUp(int keyCode);

    public LinkedList sprites() { return _sprites; }
     public boolean gamePaused() { return _gamePaused; }
     public void pauseGame() { _gamePaused = true; }
     public void resumeGame() { _gamePaused = false; }
     //return g2d object so sub-class can draw things
    public Graphics2D graphics() { return g2d; }
     //current frame rate
    public int frameRate() { return _frameRate; }
     //mouse buttons and movement
    public boolean mouseButton(int btn) { return mouseButtons[btn]; }

     public Point2D mousePosition() { return mousePos; }
     /*****************************************************
      * thread start event - start the game loop running
      *****************************************************/
     public void start() {
         gameloop = new Thread(this);
         gameloop.start();
     }

     /*****************************************************
      * key listener events
      *****************************************************/
     public void keyTyped(KeyEvent k) { }

     //this event is not needed
     public void mouseClicked(MouseEvent e) { }

     /*****************************************************
      * X and Y velocity calculation functions
      *****************************************************/
     protected double calcAngleMoveX(double angle) {
         return (double)(Math.cos(angle * Math.PI / 180));
     }

     protected double calcAngleMoveY(double angle) {
         return (double) (Math.sin(angle * Math.PI / 180));
     }


}
