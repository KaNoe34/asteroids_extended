import java.math.*;
import java.awt.geom.*;
/**
 * Analogous to GridWorld's Location class
 * Contains coordinate location information
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class Coordinate {
    //x and y represent a position in space analogous to a 
    //Cartesian coordinate plane's (x, y) coordinate
    //these values are in units of pixels away
    //from the top left corner of the screen
    private double x;
    private double y;
    
    /*
     * This constructor makes a completely random coordinate 
     * within the visible region.
     */
    Coordinate () {
        x = Math.round(Math.random()*Global.getWidth());
        y = Math.round(Math.random()*Global.getHeight());
    }
    
    /*
     * If inside is true, creates a random coordinate inside 
     * the Rectangle shape, if false creates one outside.
     */
    Coordinate (Rectangle2D shape, boolean inside) {
        if (inside) {
            x = shape.getX()+Math.round(Math.random()*shape.getWidth());
            y = shape.getY()+Math.round(Math.random()*shape.getHeight());
        } else {
            
            //create a random coordinate
            x = Math.round(Math.random()*Global.getWidth());
            y = Math.round(Math.random()*Global.getHeight());
            
            //if the shape contains it, try again
            while (x > shape.getX()
                    && x < (shape.getX()+shape.getWidth())
                    && y > shape.getY()
                    && y < (shape.getY()+shape.getHeight())) {
                x = Math.round(Math.random()*Global.getWidth());
                y = Math.round(Math.random()*Global.getHeight());
            }
        }
    }

    /*
     * Make a new coordinate with x and y as the
     * coordinate values.
     */
    Coordinate (double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX () {
        return x;
    }

    public double getY () {
        return y;
    }
    
    /*
     * Translate this coordinate into a new
     * position.
     * 
     * @param v Vector that tells this coordinate
     * in which direction to move and by how much.
     */
    public void displace (Vector v) {
        
        //add the x and y values first
        x += v.getX();
        y += v.getY();
        
        //the rest is how I make the objects move from one edge of the
        //screen to the other
        //basically if a coordinate is 50 pixels off screen, then it
        //is moved to the parallel edge of the screen and maintains its
        //exact distance away from the center of the screen
        if (x > Global.getWidth()+50) x = x-(Global.getWidth()+50);
        else if (x < -50) x = Global.getWidth()+50-(-50-x);
        if (y > Global.getHeight()+50) y = y-(Global.getHeight()+50);
        else if (y < -50) y = Global.getHeight()+50-(-50-y);
    }
}