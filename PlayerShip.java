import java.awt.*;
import java.util.*;
/**
 * Player-controlled spacecraft >:3
 * 
 * ^???
 * 
 * Unprofessional comment is unprofessional.
 * 
 * @author Me
 * @version whatever
 */
public class PlayerShip extends GameObject {

    //booleans that tell the object if continuous actions
    //are supposed to be happening
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean accelerate = false;
    private boolean decelerate = false;
    private boolean shoot = false;
    private boolean hyperdrive = false;

    //amount of lifes left before game is
    //over for this player
    private int lifes = 3;

    //the score this player has managed
    //to amass so far
    private long score = 0;

    //this times 100,000 says
    //how many points the player needs
    //to achieve to gain another life
    //tricky to name
    private int hundredTh = 1;

    //should be set to above 0 each time
    //the object gets hidden
    //the minimum time before the object is 
    //no longer hidden
    //in unit of 10 milliseconds
    private int timeout = 0;

    //while this is more than 0, hyperspace
    //can't be used
    private int hyperdriveCooldown = 0;

    //says how many active bullets are out
    //there fired by this player
    //the max is 4
    private int projectilesOwned = 0;

    //has this player been exploded and
    //not yet respawned?
    private boolean dead = false;

    /*
     * Create a new PlayerShip at the given coordinate.
     */
    public PlayerShip (Coordinate l) {

        //Restrict the speed to 0.5 pixels per
        //millisecond
        movementVect.setMaxMagnitude(5);

        //set location to the passed argument
        loc = l;

        //this creates the ship's shape
        relativeVertices.add(new Coordinate(-25, -25));
        relativeVertices.add(new Coordinate(35, 0));
        relativeVertices.add(new Coordinate(-25, 25));
        relativeVertices.add(new Coordinate(-5, 0));

    }

    //The below methods are called from GamePanel when
    //an appropriate key is pressed
    public void setTurnLeft (boolean c) {
        turnLeft = c;
    }

    public void setTurnRight (boolean c) {
        turnRight = c;
    }

    public void setAccelerate (boolean c) {
        accelerate = c;
    }

    public void setDecelerate (boolean c) {
        decelerate = c;
    }

    public void setShoot (boolean c) {
        shoot = c;
    }

    public void setHyperdrive (boolean c) {
        hyperdrive = c;
    }

    public int getLifes () {
        return lifes;
    }

    public long getScore () {
        return score;
    }

    /*
     * Add points towards this player's overall 
     * score.
     * 
     * @param n The amount of points to add.
     * Could be negative.
     */
    public void addScore (long n) {

        //add the points first
        score += n;

        //if the new score is exactly or above
        //what is required to gain a new life...
        if (score >= (100000*hundredTh)) {

            //set the bar higher
            hundredTh++;

            //but reward the player with a life
            lifes++;

        }
    }

    /*
     * Update the amount of active projectiles
     * this ship owns.
     * 
     * @param n The amount of projeciles to add.
     * Or substract if negative.
     */
    public void addProjectilesOwned (int n) {
        projectilesOwned += n;
    }

    /*
     * Main function here is to respond to player's controls.
     */
    protected void preMovementActions () {

        //all instantaneous actions take place before movement

        //if shoot is marked true...
        if (shoot) {

            //and if the ship is not hidden and the 
            //projectile limit has not been exceeded...
            if (!hidden && projectilesOwned < 4) {

                projectilesOwned++;

                //make this ship shoot a new projectile
                Global.addToGame(new Projectile(this));
            }

            //make it so that only one shot is fired
            //or ignore request to fire if hidden
            shoot = false;
        }

        //if hyperdrive is marked true...
        if (hyperdrive) {

            //and the hyperdrive has cooled down 
            //enough and the ship isn't otherwise
            //hidden...
            if (hyperdriveCooldown == 0 && !hidden) {

                //make it now hidden
                hidden = true;

                //set the location to a new random location
                loc = new Coordinate();

                //make it unhidden after .5 seconds
                timeout = 50;

                //remove all of this polygon's vertices
                //to not make them show
                reset();

                //begin the hyperdrive cooldown
                hyperdriveCooldown = 500;
            }

            //if the ship was hidden or not ready to
            //use the hyperdrive ignore this request
            hyperdrive = false;
        }

        //do something only if not hidden
        if (!hidden) {

            //rotate the ship one degree clockwise
            if (turnLeft) rotate(1.5);

            //...counterclockwise
            if (turnRight) rotate(-1.5);

            //if the ship is accelerating...
            if (accelerate) {

                //add movement data to the existing movement
                //the speed of which speed is 0.3 in the direction that
                //the ship is facing
                movementVect.add(new Vector(0.3, getOrientation()));

            }

            //if not accelerating, add a tiny negative magnitude
            //to slowly slow down
            else movementVect.addMagnitude(-0.01);

            //add movement of 0.1 magnitude in the opposite direction
            if (decelerate) movementVect.add(new Vector(0.1,
                getOrientation()-180));
        }
    }

    /*
     * Main function here is to recover from a hidden state.
     */
    protected void postMovementActions () {

        //if the object is in timeout...
        if (timeout > 0) {

            //decrease the time remaining
            timeout--;

            //if there is no more time left...
            if (timeout == 0) {

                //and the ship still has lifes left...
                if (lifes >= 0) {

                    //don't care if the space is ocupied...
                    boolean spaceOccupied = false;;

                    //unless its recovering from death...
                    if (dead) {
                        //then check if it can safely reappear

                        //update points just to make sure the
                        //polygon's bounding box is available
                        //for the following calculations
                        updatePoints();

                        //for every object in the game...
                        for (GameObject obj: Global.getGameObjects()) {

                            //if it's an asteroid...
                            if (obj instanceof Asteroid) {

                                //and it intersects with the ship's 
                                //boundry...
                                if (intersects(obj.getBounds2D())) {

                                    //mark this true
                                    spaceOccupied = true;

                                    //remove the polygon again
                                    reset();

                                    //stop checking more asteroids
                                    break;

                                }
                            }
                        }
                    }

                    //so if it turns out that the space is occupied...
                    if (spaceOccupied) {

                        //keep the ship in timeout
                        timeout++;

                        //otherwise make it no longer hidden
                        //or dead
                    } else {
                        dead = false;
                        hidden = false;
                    }

                    //but if it has no more lifes left, call
                    //it a Game Over
                } else Global.setGameOver(true);

            }
        }
        
        //if the hyperdrive is in cooldown, 
        //keep counting down until it can be used
        //again
        if (hyperdriveCooldown != 0) hyperdriveCooldown--;
    }

    /*
     * A call to this method puts the ship exactly where it
     * would need to be at the begining of a new round.
     */
    public void resetPosition () {

        //move to the center
        moveTo(new Coordinate(Global.getWidth()/2,
            Global.getHeight()/2));

        //stop movement
        movementVect.reset();

        //reset orientation
        orientation = 0;

        //update points immediately
        updatePoints();
    }

    /*
     * Explodes the ship, hides it, and causes a timeout.
     */
    public void destroy () {
        
        //take away a life
        lifes--;

        //mark it hidden
        hidden = true;

        //...and dead
        dead = true;

        //cause an explosion
        explode(200, 200);

        //move to start
        resetPosition();

        //cause a timeout period that
        //will last 2 seconds
        timeout = 200;

        //remove all points from this polygon
        reset();
    }
}