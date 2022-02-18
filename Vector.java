import java.math.*;
/**
 * A class capable of numerically representing
 * and calculating movement
 * Also useful for creating new Vertices
 * 
 * @author Oskar Czarnecki
 * @version 02/18/22
 */
public class Vector {

    //the x and y variables are a coordinate
    //representation of movement, in relation 
    //to the origin (0, 0)
    private double x;
    private double y;
    
    //magnitude is the distance in pixels
    //also used as speed, in pixels per 10 
    //milliseconds
    private double magnitude;
    
    //maximum allowed magnitude
    //a GameObject change
    //this variable to set a speed limit
    private double maxMagnitude = 99999;
    
    //direction in degrees
    //0 is facing right
    private double direction;
    
    /*
     * Create an empty vector.
     */
    public Vector () {
        magnitude = 0;
        direction = 0;
        x = 0;
        y = 0;
    }

    /*
     * Create a vector with the magnitude mag in 
     * direction dir.
     */
    public Vector (double mag, double dir) {
        magnitude = mag;
        direction = dir;
        recalculateOrderedPair();
    }

    public double getX () {
        return x;
    }

    public double getY () {
        return y;
    }

    public double getMagnitude () {
        return magnitude;
    }

    /*
     * Restrict this Vector's magnitude.
     * 
     * @param m The new maximum magnitude.
     */
    public void setMaxMagnitude (double m) {
        maxMagnitude = m;
    }

    public double getDirection () {
        return direction;
    }

    /*
     * Makes this vector now represent
     * the movement from Coordinate a
     * to Coordinate b.
     * 
     * @param a The starting coordinate.
     * @param b The ending coordinate.
     */
    public void representDistance (Coordinate a, Coordinate b) {
        x = b.getX() - a.getX();
        y = b.getY() - a.getY();
        recalculateMovement();
    }
    
    /*
     * Recalculate x and y. Should be called after
     * every time magnitude or direction is changed.
     */
    private void recalculateOrderedPair () {
        
        //values rounded to 10 decimal places 
        //because of precision loss
        x = magnitude*Math.round(Math.cos(Math.toRadians(direction))
            *(100000*100000))/(100000*100000);
        
        //also had to compensate for inversed y-axis
        y = magnitude*Math.round(Math.sin(Math.toRadians(direction))
            *(100000*100000))/(100000*100000)*-1;
    }

    /*
     * Recalculate magnitude and direction. Should be
     * called after every time x or y is changed.
     */
    private void recalculateMovement () {
        
        //this boolean is used to check if this
        //vector's allowed magnitude is exceeded
        boolean badspeed = false;
        
        //Distance formula
        magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        
        //if the magnitude exceeds maximum allowed
        //magnitude then level it and mark badspeed
        //true
        if (magnitude > maxMagnitude) {
            magnitude = maxMagnitude;
            badspeed = true;
        }
        
        //calculate new direction
        direction = Math.toDegrees(Math.atan(y/x));
        if (y >= 0 && x >= 0) direction = 360 - direction;
        else if (y >= 0) direction = 180 + (direction*-1);
        else if (x >= 0) direction *= -1;
        else direction = 180 - direction;
        
        //restricting speed is a bit more complicated
        //than I thought. The program does twice the work
        //if the magnitude is exceeded and needs to be brought 
        //down but its the only solution i could think of
        //so far
        if (badspeed) recalculateOrderedPair();
        
    }

    /*
     * Algebraically add a Vector to this Vector.
     * 
     * @param v Vector to add.
     */
    public void add (Vector v) {
        x += v.getX();
        y += v.getY();
        recalculateMovement();
    }

    /*
     * Convenience method to change only the magnitude by
     * adding the given amount. The x and y values
     * are automatically updated.
     * 
     * @param n Magnitude to add. Could be negative.
     */
    public void addMagnitude (double n) {
        
        //add
        magnitude += n;
        
        //make sure the magnitude ends up between 0 and
        //maximum magnitude
        if (magnitude < 0) magnitude = 0;
        else if (magnitude > maxMagnitude) magnitude = maxMagnitude;
        
        //recalculate x and y
        recalculateOrderedPair();
    }

    /*
     * Convenience method to change only the direction by
     * adding the given amount in degrees. The sum amount will
     * be changed to be between 0 and 360. The x and y values
     * are automatically updated.
     * 
     * @param n Amount of degrees to add. Could be negative.
     */
    public void addDegrees (double n) {
        //add
        direction += n;
        
        //make sure it ends up between 0 and 360
        while (direction > 360) direction -= 360;
        while (direction < 0) direction += 360;
        
        //recalculate x and y
        recalculateOrderedPair();
    }
    
    /*
     * Make this vector empty.
     * Represent no movement.
     */
    public void reset () {
        magnitude = 0;
        direction = 0;
        x = 0;
        y = 0;
    }
}