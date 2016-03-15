/**
 * @(#)Model.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Main Model
 */

package game;

import java.awt.Point;
import java.util.Random;
import gameobjects.*;

public class Model {
	public static final int NUMBER_OF_PLAYERS = 2;		// number of players
	public static final int MANCALAS_PER_PLAYER = 2;	// number of mancala

	private int		initStoneNumber;// initial number of stones
	private int		holeNumber;		// number of holes
	private	int		currentPlayer;	// the active player
	private int		frameRate;		// the frame rate
	private int		ply;			// the ply
	private Pit[][]	pits;			// all the holes / mancalas
	private String	stats;			// search stat
	private boolean	prune;			// turn on pruning?
	
	// Public methods to allow access to view private variable.
	public int 		getInitStoneNumber(){ return initStoneNumber; }
	public int 		getHoleNumber() 	{ return holeNumber; }
	public int 		getCurrentPlayer()	{ return currentPlayer; }
	public int		getFrameRate() 		{ return frameRate; }
	public int		getPly() 			{ return ply; }
	public String	getStats() 			{ return stats; }
	public Pit[][] 	getPits() 			{ return pits; }
	public boolean	isPrune()			{ return prune; }
	public void 	setPrune(Boolean ip){ this.prune = ip; }
	public void 	setStats(String s) 	{ this.stats = s; }
	
    /**
     * default constructor
     */
	public Model() {
    	this(6, 1000, 4, 4, false);
    }

    /**
     * Main constructor 
     * @param h number of holes per player
     * @param f the frame rate
     * @param p the ply
     * @param s number of stones per player
     * @param ip is prunning enabled
     */
    public Model(int h, int f, int p, int s, boolean ip) {
    	this.holeNumber = h;
    	Random rand = new Random();
    	this.currentPlayer = rand.nextInt(2);
    	this.frameRate = f;
    	this.ply = p;
    	this.pits = new Pit[this.holeNumber+Model.MANCALAS_PER_PLAYER][Model.NUMBER_OF_PLAYERS];
    	this.initStoneNumber = s;
    	this.prune = ip;
    	for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++){
    		for(int x = 0; x < (this.holeNumber+Model.MANCALAS_PER_PLAYER); x++){
    			if(x != 0 && x != (this.holeNumber+Model.MANCALAS_PER_PLAYER-1)) {
    				// holes in the middle with s stones
    				this.pits[x][y] = new Pit(PitEnum.HOLE, new Point (x, y), y);
    				this.pits[x][y].addStones(this.initStoneNumber);
    			}
    			else {
    				// mancalas at the ends with zero stones
    				this.pits[x][y] = new Pit(PitEnum.MANCALA, new Point (x, y), y);
    				this.pits[x][y].addStones(0);
    			}
    		}
    	}
    }

	/**
	 * @return true game is over, if one of the players holes are all empty
	 */
    public boolean isOver() {
		int empty;
    	for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++){
			 empty = 0;
    		for(int x = 1; x < this.holeNumber + Model.MANCALAS_PER_PLAYER-1; x++)
   				if (this.pits[x][y].isEmpty())
   					empty++;
    		if (empty == this.holeNumber) // that means one side is empty
    			return true;
    	}
    	return false;
    }
    
    /**
     * the opponent empties its pits in its mancala
     */
    public void endGame(){
    	// switch the player
    	for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++){
    		for(int x = 1; x < this.holeNumber + Model.MANCALAS_PER_PLAYER-1; x++) {
    			int st = this.pits[x][y].removeStones();
    			this.pits[0][y].addStones(st); // drop in mancala
    		}
    	}
    }
    
    /**
     * Make a move in a direction
     * @param pit the pit to move from
     * @param dir direction to move in
     * @return true if turn over false for another turn
     */
    public boolean move(Pit pit, DirectionEnum dir){
    	boolean turnOver = true; // assume only one turn
		int x = pit.getLocation().x;
		int y = pit.getLocation().y;
    	if(pit.isHole()) { // only play if its a hole
    		int hand = pit.removeStones(); // pick up the stones
    		while (hand > 0){    			
    			// move to next pit
    			if(dir == DirectionEnum.LEFT) {
    				// if out of bound move to next players holes and swap directions
    				if((x+dir.getValue()) < 0){
    					y = (y + 1)%Model.NUMBER_OF_PLAYERS;
    					dir = dir.opposite();
    				}
    				// Otherwise move in the direction
    				else {
    					x+=dir.getValue();
    				}
    			}
    			else if(dir == DirectionEnum.RIGHT) {
    				// if out of bound move to next players holes and swap directions
    				if((x+dir.getValue()) > (this.holeNumber+Model.MANCALAS_PER_PLAYER-1)){
    					y = (y + 1)%Model.NUMBER_OF_PLAYERS;
    					dir = dir.opposite();
    				}
    				// Otherwise move in the direction
    				else {
    					x+=dir.getValue();
    				}
        		}

    			Pit aPit = this.pits[x][y];
    			
				// if its a hole drop the stone
    			if(aPit.isHole()){
    				hand--;
    				aPit.addStones(1);
    				// if the hole has 0 stone meaning you dropped it in a empty hole
    				// then you get to take your opponents opposite sided
    				// holes stones and put them all in you mancala 
    				if(hand == 0 &&  aPit.getPlayer() == this.currentPlayer)
    					this.pits[0][y].addStones(
    						this.pits[x][(y+1)%Model.NUMBER_OF_PLAYERS].removeStones()
    					);
    			}
    			// if its a mancala drop the stone only if its the current players
    			else if(aPit.isMancala() && aPit.getPlayer() == this.currentPlayer) {
    				hand--;
    				aPit.addStones(1);
    				// if you dropped last stone in your own mancala you get to go again!
    				if(hand == 0)
    					turnOver = false;
    			}
    		}
    	}
    	return turnOver;
    }

	/**
	 * Switch the player
	 */
	public void switchPlayer(){
		this.currentPlayer = (this.currentPlayer+1)%Model.NUMBER_OF_PLAYERS; 
	}
	
	/**
	 * reset hole number
	 * @param h
	 */
	public void setHoleNumber(int h) {
		this.holeNumber = h;
	}
	
	/**
	 * Get the players score
	 * @param p the player
	 * @return score
	 */
	public int playerScore(int p) {
		int score = 0;
		
		for(int x = 0; x < this.holeNumber + Model.MANCALAS_PER_PLAYER; x++)
			if(this.pits[x][p].isMancala())
				score += this.pits[x][p].getStones();
		
		return score;
	}
	
	/**
	 * Get the players that won
	 * @return winner
	 */
	public int getWinner() {
		int maxScore = 0;
		int winner = 0;
		
		for(int p = 0; p < Model.NUMBER_OF_PLAYERS; p++) {
			int someScore = playerScore(p);
			if(someScore >  maxScore) {
				winner = p;
				maxScore = someScore;
			}
		}
		return winner;
	}
	
	/**
	 * Log the board
	 */
	public void logBoard() {
		for(int i = 0; i < Model.NUMBER_OF_PLAYERS; i++){
    		for(int j = 0; j < (this.holeNumber+Model.MANCALAS_PER_PLAYER); j++){
    			System.out.print(this.pits[j][i].getStones());
    		}
			System.out.print("\n");
    	}
		System.out.print("\n");
	}
}