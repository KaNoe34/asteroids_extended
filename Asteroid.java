import java.awt.*;
/**
 * Main obstacle and source of points in the game
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class Asteroid extends GameObject {

    //variables that can be passed to the constructor
    //by an outside class
    public static final int BIG = 3; // default setting
    public static final int MEDIUM = 2;
    public static final int SMALL = 1;

    //stores one of the above size settings
    private int size;

    //how fast the asteroid spins
    private double spin;
    
    /*
     * This constructor createas an asteroid
     * at the coordinate, and with the provided
     * size setting.
     */
    public Asteroid (Coordinate l, int size) {
        this.size = size;
        loc = l;

        //Instantiate a new vector that will aid in translating the 
        //points
        Vector help;

        //randomly generated asteroids, but with a controlled
        //radius range and number of vertices
        if (size == SMALL) { 
            
            //8 corners
            int corners = 8;
            
            for (int c = 0; c < corners; c++) {
                
                //distance from center at least 10 and up to 30
                //distance range quite large, 20 pixels which 
                //makes the overall shape quite rough
                //the second argument is the direction,
                //that formula makes all corners equally spaced apart
                help = new Vector(Math.round(Math.random()*20)+10,
                    (360/corners)*(c+1));
                
                relativeVertices.add(new Coordinate(help.getX(),
                    help.getY()));
            }
        } else if (size == MEDIUM) {
            
            //20 corners
            int corners = 20;
            for (int c = 0; c < corners; c++) {
                
                //distance from center at least 35 and up to 50
                //distance range is medium, 15 pixels
                help = new Vector(Math.round(Math.random()*15)+35,
                    (360/corners)*(c+1));
                
                relativeVertices.add(new Coordinate(help.getX(),
                    help.getY()));
            }
        
        } else {
            
            //automatically sets undefined
            //size settings to BIG
            this.size = BIG;
            
            //40 corners
            int corners = 40;
            
            for (int c = 0; c < corners; c++) {
                
                //distance from center at least 70 and up to 80
                //small distance range, 10 pixels which makes
                //the overall shape circular and smooth
                help = new Vector(Math.round(Math.random()*10)+70,
                    (360/corners)*(c+1));
                
                relativeVertices.add(new Coordinate(help.getX(),
                    help.getY()));
            }
        }
         
        //makes spin a random number between -1 and 1 rounded to
        //nearest hundredth
        spin = Math.round(Math.random()*200-100)/100.0;
        
        //creates random movement between 0 and 3 pixels per
        //second rounded to nearest tenth, and any direction
        movementVect.add(new Vector(Math.round
            (Math.random()*30)/10.0+.1, Math.round(Math.random()*360)));
        
    }
       
    public int getSize () {
        return size;
    }

    /*
     * Just makes the asteroids keep spinning.
     */
    protected void preMovementActions () {
        rotate(spin);
    }

    /*
     * Main function here is to check for collisions.
     */
    protected void postMovementActions () {
        
        //don't bother checking anything if the player is invincible
        if (!Global.getPlayer().isHidden()) {
            
            //first, check if this asteroid's bounding
            //box intersects the player's bounding box
            if (intersects(Global.getPlayer().getBounds2D())) {
                
                //if it does, then begin more precise calculations
                //for collision
                if (checkCollision(Global.getPlayer())) {
                    
                    //if collision is detected, subtract the appropriate
                    //amount from the destroyed player's score
                    if (size == Asteroid.BIG) Global.getPlayer()
                        .addScore(-100);
                    else if (size == Asteroid.MEDIUM) 
                        Global.getPlayer().addScore(-200);
                    else if (size == Asteroid.SMALL ) 
                        Global.getPlayer().addScore(-400);
                    
                    //destroy the ship
                    Global.getPlayer().destroy();
                    
                }
            }
        }
    }

    /*
     * Explode and remove this asteroid. 
     * Add 2 smaller ones in its place.
     */
    public void destroy () {
        if (size == BIG) {
            
            //biggest size warrants
            //the biggest explosion
            explode(70, 50);
            
            //add 2 medium asteroids
            for (int c = 0; c < 2; c++) {
                
                //the coordinate is within this asteroid's bounds
                Global.addToGame(new Asteroid(new Coordinate
                    (getBounds(), true), Asteroid.MEDIUM));
            
            }
        } else if (size == MEDIUM) {
            
            //medium size, medium explosion
            explode(60, 30);
            
            //add 2 small asteroids
            for (int c = 0; c < 2; c++) {
                Global.addToGame(new Asteroid(new Coordinate
                    (getBounds(), true), Asteroid.SMALL));
            }
            
            //dont add anything, just make
            //a small explosion
        } else explode(50, 10);
        
        //finally, remove this one from the game
        Global.removeFromGame(this);
    }
}