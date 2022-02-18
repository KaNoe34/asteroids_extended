import java.util.*;
import java.awt.*;
/**
 * Contains the game's main runtime code
 * Contains the loop that keeps updating
 * game objects and then calling the paint 
 * method
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class Game {

    //level is the round number, starting at 0
    private int level = 0;
    
    //asteroids left on this level
    private static int asteroidsLeft;

    //breakTime is the time between rounds
    //once set to something other than 0
    //it will count down and then a new
    //round will start
    private static int breakTime = 0;
    
    /*
     * The constructor begins the game.
     */
    public Game () {
        beginRuntime();
    }
    
    /*
     * This method adds number of asteroids 
     * left in this level, doesn't add new 
     * asteroids to the game.
     * 
     * @param n The number of asteroids to add.
     * Can be negative.
     */
    public static void addAsteroid (int n) {
        asteroidsLeft += n;
    }

    /*
     * Begin the game sequence first by setting up the 
     * game objects and then starting the loop.
     * It is not safe to call this from anywhere at 
     * any time, only the constructor has to call it.
     */
    private void beginRuntime () {
        
        //instantiate a new PlayerShip and add to the game
        PlayerShip player = new PlayerShip(new Coordinate(Global
            .getWidth()/2, Global.getHeight()/2));
        Global.addToGame(player);
        
        //use the usual round procedure to add starting 
        //asteroids
        placeAsteroids();
        
        //run the loop
        run();
    }
    
    /*
     * A call to this method adds the appropriate amount of 
     * asteroids for this level. The asteroids are added far 
     * from the player. It is somewhat safe to call this method 
     * at any time but it is only meant to be done between rounds.
     */
    private void placeAsteroids () {
        
        //create a new rectangle that is a border around the player
        //and extends 400 pixels in every direction
        Rectangle safeZone = new Rectangle((int)Global.getPlayer()
            .getX()-400, (int)Global.getPlayer().getY()-400, 800, 800);
        
        //make the amount of asteroids to be added 5 plus
        //the level number
        int asteroids = 5+level;
        
        //20 asteroids is the maximum
        if (asteroids > 20) asteroids = 20;
        
        //for the amount of asteroids to be added...
        for (int c = 0; c < asteroids; c++) {
            
            //add a random big asteroid at a coordinate NOT in
            //the safe zone
            Global.addToGame(new Asteroid(
                new Coordinate(safeZone, false), Asteroid.BIG));
        }
    }

    /*
     * The game's main loop.
     */
    public void run () {
        
        //prepare the panel for a new session
        Global.getGamePanel().reset();
        
        //request focus on the game panel
        Global.getGamePanel().requestFocus();
        
        //while a game ending condition has not happened...
        while (!Global.gameOver()) {
            
            //any objects waiting to be added or deleted are 
            //to be processed now
            Global.updateObjectList();
            
            //don't move any game objects while paused
            if (!Global.isPaused()) {
                
                //for every object in the game...
                for (GameObject obj: Global.getGameObjects()) {
                    
                    //perform its usual operation
                    obj.operate();
                    
                }
                
                //if the countdown to end the round
                //is happening...
                if (breakTime > 0) {
                    
                    //count down 1
                    breakTime--;
                    
                    //if the countdown is over...
                    if (breakTime == 0) {
                        
                        //a new level has begun
                        level++;
                        
                        //put the player in starting position
                        Global.getPlayer().resetPosition();
                        
                        //place the right number of asteroids
                        placeAsteroids();
                        
                    }
                    
                    //if no more asteroids are left...
                } else if (asteroidsLeft == 0) {
                    
                    //begin the countdown at 2 seconds
                    breakTime = 200;
                }
            }

            //repaint the screen
            Global.getGamePanel().repaint();

            //wait for 10 milliseconds before continuing
            try {
                Thread.sleep(10);
                
                //this exception thankfully has never happened
            } catch (InterruptedException x) {
            }
        }

        //once gameOver has been set to true, the following will 
        //execute...
        Global.getGamePanel().setMessage("Game Over! You scored " 
            + Global.getPlayer().getScore());
        Global.getGamePanel().repaint();
        reset();
    }
    
    /*
     * Ready the game to be absolutely prepared
     * for a new session to begin.
     * A call to this method will remove all existing 
     * objects from the game and erase the player.
     */
    private void reset () {
        
        //for every object in the game...
        for (GameObject obj: Global.getGameObjects()) {
            
            //mark it for removal
            Global.removeFromGame(obj);
            
        }
        Global.removePlayers();
    }
}