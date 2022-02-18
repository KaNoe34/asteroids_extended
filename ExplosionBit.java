/**
 * Tiny chunks rendered for cinematic purposes
 * Don't collide with anything
 * Have a limited lifespan
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class ExplosionBit extends GameObject {

    //lifespan is how long the bit lasts
    private int lifespan;
    
    //spin is how much it rotates
    //each movement
    private double spin;    
    
    /*
     * The constructor takes a location and the 
     * lifespan of the bit.
     */
    public ExplosionBit (Coordinate l, int ls) {
        
        //crate a random number of edges between 1 and 6
        int edges = (int)Math.round(Math.random()*5+1);
        
        //for each edge...
        for (int c = 0; c < edges; c++) {
            
            //make each vertex a random tiny distance away from the center
            relativeVertices.add(new Coordinate(Math.round
                (Math.random()*6)+1, Math.round(Math.random()*6)+1));
            
        }
        
        //make spin a random number between -0.5 and 0.5 rounded
        //to the nearest hundredth i believe
        spin = Math.round(Math.random()*100-50)/100.0;
        
        //make movement be at least 0.1 and up to 1.6 rounded to
        //the nearest tenth
        //in a random direction
        movementVect.add(new Vector(Math.round
            (Math.random()*15)/10.0+0.1,
                Math.round(Math.random()*360)));
        
        //make the lifespan between 0 and the provided
        //number
        //the lifespan as the argument is just a 
        //sugestion, most bits will last much
        //shorter to have them dissapear one by
        //one
        lifespan = (int)Math.round(Math.random()*ls);
        
        loc = l;
    }
    
    /*
     * Just makes the bit keep spinning.
     */
    protected void preMovementActions () {
        rotate(spin);
    }
    
    /*
     * Just makes the bit remove itself
     * once it's lifespan is depleted.
     */
    protected void postMovementActions () {
        lifespan--;
        if (lifespan <= 0) destroy();
    }
}