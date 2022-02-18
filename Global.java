import java.awt.event.*;
import java.util.*;
import java.awt.*;
/**
 * Universally accessible class that should have all the 
 * things that other classes might want to randomly
 * access or modify
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class Global {
    //all the key mappings are stored here
    private static int accelerate = KeyEvent.VK_UP;
    private static int decelerate = KeyEvent.VK_DOWN;
    private static int counterclockwise = KeyEvent.VK_LEFT;
    private static int clockwise = KeyEvent.VK_RIGHT;
    private static int shoot = KeyEvent.VK_SPACE;
    private static int hyperspace = KeyEvent.VK_SHIFT;
    private static int exit = KeyEvent.VK_ESCAPE;
    private static int pause = KeyEvent.VK_P;

    //objects waiting in queue to be added or deleted...
    private static ArrayList<GameObject> toAdd = 
        new ArrayList<GameObject>();
    private static ArrayList<GameObject> toRemove = 
        new ArrayList<GameObject>();

    //all objects currently operating within the game...
    private static ArrayList<GameObject> gameObjects = 
        new ArrayList<GameObject>();
    private static PlayerShip player;

    //GUIs ...
    private static MainFrame frame;
    private static GamePanel game;
    
    //window dimensions...
    private static int height;
    private static int width;
    
    //Game conditions...
    private static boolean gameOver = false;
    private static boolean paused = false;

    //The constructor is made private because this
    //class never needs to be instantiated but all
    //its methods and variables are important.
    //It' just like the Math class.
    private Global () {
    }

    public static int getAccelerateKey () {
        return accelerate;
    }

    public static int getDecelerateKey () {
        return decelerate;
    }

    public static int getRotateCounterclockwiseKey () {
        return counterclockwise;
    }

    public static int getRotateClockwiseKey () {
        return clockwise;
    }

    public static int getShootKey () {
        return shoot;
    }

    public static int getHyperspaceKey () {
        return hyperspace;
    }

    public static int getQuitGameKey () {
        return exit;
    }

    public static int getPauseGameKey () {
        return pause;
    }

    /*
     * Adds an object to the waiting list to be
     * eventually added to the game.
     * It's safe to call this method at any time.
     * 
     * @param obj Any class extending GameObject 
     * that is to be added to the game.
     */
    public static void addToGame (GameObject obj) {
        //add the object to queue, if it is a player
        //ship and a player hasn't been assigned yet, assign player
        //to it
        toAdd.add(obj);
        if (obj instanceof PlayerShip) {
            if (player == null) player = (PlayerShip)obj;
        }
    }

    /*
     * Adds an object to the waiting list to be
     * eventually removed from the game.
     * If the object is not in the game,
     * there will be no effect.
     * It's safe to call this method at any time.
     * 
     * @param obj Any calss extending GameObject 
     * that is to be removed from the game.
     */
    public static void removeFromGame (GameObject obj) {
        toRemove.add(obj);
    }

    /*
     * Updates the list of objects in the game by adding
     * and removing those on the waiting lists. This method
     * is called at a specific time in the game loop and is
     * not safe to call at any other time.
     */
    public static void updateObjectList () {
        for (GameObject obj: toAdd) {
            gameObjects.add(obj);
            if (obj instanceof Asteroid) {
                Game.addAsteroid(1);
            }
        }
        toAdd.clear();
        for (GameObject obj: toRemove) {
            gameObjects.remove(obj);
            if (obj instanceof Asteroid) {
                Game.addAsteroid(-1);
            }
        }
        toRemove.clear();
    }

    public static ArrayList<GameObject> getGameObjects () {
        return gameObjects;
    }

    public static PlayerShip getPlayer () {
        return player;
    }
    
    public static void removePlayers () {
        player = null;
    }

    public static void setMainFrame (MainFrame f) {
        frame = f;
    }

    public static MainFrame getMainFrame () {
        return frame;
    }

    public static void setGamePanel (GamePanel p) {
        game = p;
    }

    public static GamePanel getGamePanel () {
        return game;
    }

    public static int getWidth () {
        return width;
    }

    public static int getHeight () {
        return height;
    }
    
    public static void setGameOver (boolean c) {
        gameOver = c;
    }
    
    public static boolean gameOver () {
        return gameOver;
    }
    
    public static void pause () {
        paused = !paused;
    }
    
    public static boolean isPaused () {
        return paused;
    }

    /*
     * A call to this method updates the local height
     * and width variables according to the screen size at
     * the time of the call.
     */
    public static void updateDimensions () {
        height = (int)Toolkit.getDefaultToolkit().getScreenSize()
            .getHeight();
        width = (int)Toolkit.getDefaultToolkit().getScreenSize()
            .getWidth();
    }
}