/**
 * @(#)Pit.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Pit Model
 */

package gameobjects;

import java.awt.Point;

public class Pit {
	private	int		stones;		// stones in pit
	private PitEnum	type;		// type of pit {hole, mancala}
	private Point 	location;	// location of pit
	private int 	player;		// player number to which the pit belongs to

	// Public methods to allow access to view private variable.
    public Point	getLocation()	{ return location; }
	public int 		getPlayer() 	{ return player;  }
    public int  	getStones()  	{ return  stones; }

	/**
     * Main constructor 
     * @param t	pit type
     */
	
    public Pit(PitEnum t, Point l, int p)  {
    	this.stones  =  0;
    	this.type = t;
    	this.location = l;
    	this.player = p;
	}

    /**
     * Copy constructor 
     * @param other the other pit
     */
    public Pit(Pit other) {
    	this(other.type, new Point(other.location), other.player);
       	this.stones = other.stones;
    }
    
    /**
     * Add stones to pit
     * @param stones stones to add
     */
    public void addStones(int  stones) {
    	this.stones += stones;
    }
   
    /**
     * @return if pit is empty
     */
    public boolean isEmpty() {
    	return stones == 0;
    }
    
    /**
     * remove all stones in pit
     * @return number of stones that were removed from the mit
     */
    public int removeStones() {
        int  stones  =  this.stones;
        this.stones  =  0;
        return  stones;
    }
    

    /**
     * @return if pit is a hole
     */
    public  boolean  isHole() {
    	return this.type == PitEnum.HOLE;
    }
    
    /**
     * @return if pit is a mancala
     */
    public  boolean  isMancala() {
    	return this.type == PitEnum.MANCALA;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + player;
		result = prime * result + stones;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pit other = (Pit) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (player != other.player)
			return false;
		if (stones != other.stones)
			return false;
		if (type != other.type)
			return false;
		return true;
	};
}