
/**
 * Bullets that destroy asteroids and spacecraft
 * Have a limited range
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */
public class Projectile extends GameObject {
    
    //the lifespan is how many times
    //the bullet travels
    //75 in this case is .75 seconds
    private int lifespan = 75;

    //remembers who fired this projectile
    private PlayerShip owner;

    /*
     * This contructor takes a PlayerShip as 
     * a parameter to create a bullet fired
     * by it.
     */
    public Projectile (PlayerShip ship) {

        //store the owner
        owner = ship;

        //make the bullet appear in the location
        //of the ship's second vertex, which is its 
        //pointed tip
        loc = new Coordinate(ship.getVertices().get(1).getX(),
            ship.getVertices().get(1).getY());

        //make a tiny rectangle shape
        relativeVertices.add(new Coordinate(4, -1));
        relativeVertices.add(new Coordinate(4, 1));
        relativeVertices.add(new Coordinate(0, 1));
        relativeVertices.add(new Coordinate(0, -1));

        //make the bullet face the same direction as the ship
        rotate(ship.getOrientation());

        //make the speed 1 pixel per millisecond
        //in the direction its facing
        movementVect.add(new Vector(10, orientation));

    }

    /*
     * Maing function here is to limit the 
     * projectile's range and check for collisions.
     */
    protected void postMovementActions () {

        //each time the object operates, decrement
        //the lifespan
        lifespan--;

        //if the lifespan has expired, remove this from the
        //game and stop there
        if (lifespan == 0) {
            destroy();
            return;
        }

        //for every object in the game...
        for (GameObject obj: Global.getGameObjects()) {

            //if its an asteroid...
            if (obj instanceof Asteroid) {

                //and the bounding boxes intersect...
                if (intersects(obj.getBounds2D())) {

                    //and a more accurate calculation
                    //indicates collision...
                    if (obj.checkCollision(this)) {
                        //the reason I call checkCollision() from
                        //the asteroid class and pass this as the 
                        //argument is the order in which the collision
                        //checks are performed. It's more likely that
                        //the vertices of the projectile are contained
                        //within the asteroid's shape

                        //know that its an asteroid now so cast
                        //it to learn its size
                        Asteroid a = (Asteroid)obj;

                        //add appropriate amount of points to
                        //this projectile's owner
                        if (a.getSize() == Asteroid.BIG) 
                            owner.addScore(50);
                        else if (a.getSize() == Asteroid.MEDIUM) 
                            owner.addScore(100);
                        else if (a.getSize() == Asteroid.SMALL)
                            owner.addScore(250);

                        //destroy the asteroid
                        obj.destroy();

                        //and destroy the projectile
                        destroy();
                    }
                }
            }
        }
    }

    /*
     * Remove self from game as well
     * as notify owner that it has
     * been destroyed.
     */
    public void destroy () {
        Global.removeFromGame(this);
        owner.addProjectilesOwned(-1);
    }
}