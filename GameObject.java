import java.awt.*;
import java.util.*;
/**
 * GameObject is abstract and has all the methods that all
 * the polygons in the game will need to act properly
 * Can be easily extended by new Classes
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public abstract class GameObject extends Polygon {

    //a vector contains an object's movement information
    protected Vector movementVect = new Vector();

    //the loc coordinate is the presumed
    //center of an object
    protected Coordinate loc;

    //orientation is rotation in degrees, 0 and 360 facing
    //right
    protected double orientation = 0;

    //Classes extending GameObject should have the 
    //constructor fill this list with coordinates.
    //Basically it is a list of points where the 
    //vertices would lie if the object was positioned
    //at the origin (0, 0) and had orientation 0
    protected ArrayList<Coordinate> relativeVertices = 
        new ArrayList<Coordinate>();

    //and this is the list of vertices with coordinates of
    //their exact location on the plane, updated to match
    //the location and orientation
    protected ArrayList<Coordinate> actualVertices = 
        new ArrayList<Coordinate>();

    //setting this to true does not quite
    //remove the object from the game, but makes it 
    //unable to move and extending classes can 
    //use it to signify other things
    protected boolean hidden = false;
    public Vector getMovementVector () {
        return movementVect;
    }

    /*
     * Return the x coordinate of this object.
     */
    public double getX () {
        return loc.getX();
    }

    /*
     * Return the y coordinate of this object.
     */
    public double getY () {
        return loc.getY();
    }
    
    public double getOrientation () {
        return orientation;
    }

    /*
     * Return the list of actual vertex locations.
     */
    public ArrayList<Coordinate> getVertices () {
        return actualVertices;
    }

    public boolean isHidden () {
        return hidden;
    }

    /*
     * Rotate this game object. The final rotation
     * will be between 0 and 360.
     * 
     * @param degrees Number in degrees by which to rotate 
     * this shape. Can be negative.
     */
    public void rotate (double degrees) {
        //sum the current and additional rotation first
        orientation += degrees;

        //then make sure its between 0 and 360
        while (orientation > 360) orientation -= 360;
        while (orientation < 0) orientation += 360;
    }

    /*
     * A call to this method updates the actualVertices list
     * and the polygon's points. Should be called after each
     * time the object is rotated or moved.
     */
    protected void updatePoints () {

        //delete all existing points from
        //from the polygon and the vertex list
        reset();
        actualVertices.clear();

        //Instantiate a new vector that will aid in translating the 
        //points
        Vector help = new Vector();

        //for every coordinate of this object's vertices...
        for (Coordinate p: relativeVertices) {
            //make help represent the movement between the 
            //origin and this coordinate
            help.representDistance(new Coordinate(0, 0), p);

            //then rotate this vertex around the origin
            //by the object's orientation
            help.addDegrees(orientation);

            //add a vertex to this polygon using the calculated 
            //movement data and the GameObjects's actual center location
            actualVertices.add(new Coordinate(loc.getX()+help.getX(),
                loc.getY()+help.getY()));
            addPoint((int)Math.round(loc.getX()+help.getX()),
                (int)Math.round(loc.getY()+help.getY()));
        }
    }
    
    /*
     * A method to more precisely calculate collision between
     * two GameObjects. A collision is defined as having a vertex 
     * of either object located inside the other one. The vertices of 
     * the passed object are checked first, then the
     * vertices of this object.
     * 
     * @param obj GameObject to check collision with.
     * @returns true if a collision is detected, otherwise
     * false.
     */
    protected boolean checkCollision (GameObject obj) {
        
        //for each vertex of the passed object...
        for (Coordinate p: obj.getVertices()) {
            
            //if this shape contains the vertex 
            //return true
            if (contains(p.getX(), p.getY())) return true;
        }
        
        //for each vertex of this object...
        for (Coordinate p: actualVertices) {
            
            //if the passed object's shape contains
            //this vertex return false
            if (obj.contains(p.getX(), p.getY())) return true;
        }
        
        //return false if no collision is found
        return false;
    }

    /*
     * If another class wants to destroy this object,
     * it calls this method. All it does is remove it
     * from the game so an extending class might
     * want to override this method.
     */
    public void destroy () {
        Global.removeFromGame(this);
    }

    /*
     * This method is different from destroy and only 
     * causes a visual explosion to occur inside of this
     * object's shape.
     * 
     * @param time The maximum amount of time
     * the explosion particles will last for.
     * In unit of 10 milliseconds.
     * @param intensity The amount of particles
     * to generate.
     */
    protected void explode (int time, int intensity) {
        
        //loop for the amount of intensity...
        for (int c = 0; c < intensity; c++) {
            
            //add a new ExplosionBit to the game
            //the coordinate is a random location
            //within this object's rectangular bounds
            Global.addToGame(new ExplosionBit(
                new Coordinate(getBounds(), true), time));
        }
    }

    /*
     * A call to this method makes the 
     * object perform its usual operation once.
     * This is called once from
     * the main loop should never be called 
     * again.
     */
    public void operate () {
        preMovementActions();
        move();
        postMovementActions();
    }

    /*
     * This method moves the center of the shape by
     * using the movemen vector and then calls
     * updatePoints(). This is called from operate()
     * and should not be invoked by anything else.
     */
    private void move () {
        
        //do something only if the object isn't hidden
        if (!hidden) {
            
            //translate the center coordinate using the 
            //object's current movement vector
            loc.displace(movementVect);

            //update the polygon's vertices using an
            //updated coordinate
            updatePoints();
        }
    }

    /*
     * Immediately teleports the object to
     * a new coordinate while maintaining 
     * movement data. Does not update the
     * vertices.
     * 
     * @param l The coordinate to be the 
     * object's new center.
     */
    public void moveTo (Coordinate l) {
        loc = l;
    }
    
    //The following should be overriden for
    //the object to actually do anything
    //other than move
    protected void preMovementActions () {
    }

    protected void postMovementActions() {
    }
}