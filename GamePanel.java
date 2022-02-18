import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
/**
 * Responsible for the game's main visuals
 * Paints all objects at once
 * Listens for key presses
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class GamePanel extends JPanel implements KeyListener {
    //a message to display and a timer to hide it
    private String message = "";
    private int messageTime = 0;
    //the timer was useful at one point

    //these are marked false once the key is pressed
    //and are used to avoid having these actions happen
    //non-stop
    private boolean shootReload = true;
    private boolean hyperspaceReload = true;
    
    //arrays that hold information to draw
    //the stars
    private int[] xstars = new int[40];
    private int[] ystars = new int[xstars.length];
    private int[] wstars = new int[xstars.length];
    private int[] hstars = new int[xstars.length];

    /*
     * The constructor sets a font for the game
     * and adds this as a KeyListener.
     */
    public GamePanel () {
        setFont(new Font("rockwell", Font.BOLD, 22));
        addKeyListener(this);
    }

    /*
     * Display a message in the lower right corner for
     * two seconds.
     * 
     * @param text Message to display.
     */
    public void setMessage (String text) {
        messageTime = 200;
        message = text;
    }

    /*
     * Function is self explanatory, redraws the screen.
     */
    public void paintComponent (Graphics g) {
        //draws the black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Global.getWidth(), Global.getHeight());
        
        //switch to white
        g.setColor(Color.WHITE);
        
        //draws all on-screen objects next
        
        //draw the stars
        for (int c = 0; c < xstars.length; c++) {
            g.fillOval(xstars[c], ystars[c], wstars[c], hstars[c]);
        }
        
        //draw GameObjects
        try {
            
            //for every game object...
            for (GameObject obj: Global.getGameObjects()) {
                
                //draw its polygon
                g.drawPolygon(obj);
                
            }
            
            //this exception still happens from time to time and 
            //theres nothing more I can do about it.
            //this type of exception occurs when a collection is 
            //being iterated over while also being modified
            //somewhere else
        } catch (ConcurrentModificationException x) {
        }

        //don't attempt to draw the following
        //if the player is not set
        //for example, the split second right
        //before the game starts
        if (Global.getPlayer() != null) {

            //draws the life meter next

            //starting coordinates for the first ship
            int x = 50;
            int y = 100;

            //for everylife, draw a polygon
            for (int c = 0; c < Global.getPlayer().getLifes(); c++) {
                g.drawPolygon(
                    new int[]{x*(c+1)-13, x*(c+1)+0, x*(c+1)+13, 
                        x*(c+1)+0}, new int[]{y+13, y-18, y+13, y+3}, 
                        4);
            }
            
            //create the score String and draw it
            String score = "" + Global.getPlayer().getScore();
            g.drawString(score, 50, 150);
        }

        //draws the message, if it hasn't
        //timed out yet
        if (messageTime > 0) {
            messageTime--;
            g.drawString(message, 100, Global.getHeight()-100);
        }
    }

    public void keyPressed (KeyEvent e) {
        if (Global.getPlayer() != null) {
            // the procedure here is that if the pressed key
            // is found by the settings to be mapped to an action
            // which happens continuously, then the player's
            // ship gets notified that that key is being held down
            if (e.getKeyCode() == Global.getAccelerateKey()) 
                Global.getPlayer().setAccelerate(true);
            else if (e.getKeyCode() == Global.getDecelerateKey()) 
                Global.getPlayer().setDecelerate(true);
            else if (e.getKeyCode() == Global
                .getRotateCounterclockwiseKey()) Global.getPlayer()
                    .setTurnLeft(true);
            else if (e.getKeyCode() == Global.getRotateClockwiseKey()) 
                Global.getPlayer().setTurnRight(true);

            //similar to above, but actions here happen 
            //only once per keystroke. the booleans will be 
            //automatically marked false by the ship
            //once it performs the action
            //the booleans with suffix Reload are there to 
            //make sure when the key is held down the 
            //actions dont keep repeating
            else if (e.getKeyCode() == Global.getShootKey() && 
                shootReload) {
                Global.getPlayer().setShoot(true);
                shootReload = false;
            } else if (e.getKeyCode() == Global.getHyperspaceKey()
                && hyperspaceReload) {
                Global.getPlayer().setHyperdrive(true);
                hyperspaceReload = false;
            }
        }
        //other actions

        //ends the game and switches to the menu
        if (e.getKeyCode() == Global.getQuitGameKey()) {
            Global.setGameOver(true);
            Global.getMainFrame().switchToMenu();

            //pauses/unpauses the game
        }else if (e.getKeyCode() == Global.getPauseGameKey()) 
            Global.pause();
    }
    
    public void keyReleased (KeyEvent e) {
        if (Global.getPlayer() != null) {
            
            //causes continuous actions to stop occuring
            if (e.getKeyCode() == Global.getAccelerateKey()) 
                Global.getPlayer().setAccelerate(false);
            else if (e.getKeyCode() == Global.getDecelerateKey()) 
                Global.getPlayer().setDecelerate(false);
            else if (e.getKeyCode() == Global
                .getRotateCounterclockwiseKey()) 
                    Global.getPlayer().setTurnLeft(false);
            else if (e.getKeyCode() == Global.getRotateClockwiseKey()) 
                Global.getPlayer().setTurnRight(false);

            //makes instant actions able to happen again
            else if (e.getKeyCode() == Global.getShootKey()) 
                shootReload = true;
            else if (e.getKeyCode() == Global.getHyperspaceKey()) 
                hyperspaceReload = true;
        }
    }

    public void keyTyped (KeyEvent e) {
        //this method does not work properly for some
        //mysterious magical reason
    }

    /*
     * Ready this object to be absolutely prepared
     * for a new instance of the game to begin.
     * A call to this method will randomly generate
     * new stars, reset the message, and reset
     * instant action keys.
     */
    public void reset () {
        for (int c = 0; c < xstars.length; c++) {
            xstars[c] = (int)(Math.random()*Global.getWidth());
            ystars[c] = (int)(Math.random()*Global.getHeight());
            wstars[c] = (int)(Math.random()*2+1);
            hstars[c] = (int)(Math.random()*2+1);
        }
        shootReload = true;
        hyperspaceReload = true;
        message = "";
    }
}