/**
 * @(#)State.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Used to represent a state in the game needed by node
 */

package artificialintelligence;

import java.util.Arrays;
import game.Model;
import gameobjects.Pit;

public class State {
	private boolean	isDeadState;	// if this a dead state or not
	private	int		currentPlayer;	// the active player
	private int 	holeNumber;		// holes
	private Pit[][]	pits;			// all the holes / mancalas
	
    // Public methods to allow access to private variable.
	public boolean 	isDeadState()	 	{ return isDeadState; }
	public int 		getCurrentPlayer()	{ return currentPlayer; }
	public int 		getHoleNumber() 	{ return holeNumber; }
	public Pit[][] 	getPits() 			{ return pits; }

	public void 	setDeadState(boolean isDeadState)	{ this.isDeadState = isDeadState; }
	public void		setCurrentPlayer(int currentPlayer)	{ this.currentPlayer = currentPlayer; }
	public void 	setHoleNumber(int holeNumber) 		{ this.holeNumber = holeNumber; }
	public void 	setPits(Pit[][] pits) 				{ this.pits = pits; }

	/**
	 * Main constructor
	 * @param isDeadState
	 * @param currentPlayer
	 * @param holeNumber
	 * @param pits
	 */
	public State(boolean isDeadState, int currentPlayer, int holeNumber, Pit[][] pits) {
		this.isDeadState = isDeadState;
		this.currentPlayer = currentPlayer;
		this.holeNumber = holeNumber;
		this.pits = new Pit[this.holeNumber+Model.MANCALAS_PER_PLAYER][Model.NUMBER_OF_PLAYERS];		
		for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++)
    		for(int x = 0; x < (this.holeNumber+Model.MANCALAS_PER_PLAYER); x++)
    			this.pits[x][y] = new Pit(pits[x][y]);
	}
	
	/**
	 * Make state from a model problem
	 * Needed for initial state
	 */
    public State(Model problem) {
    	this(false, problem.getCurrentPlayer(), problem.getHoleNumber(), problem.getPits());
	}

    /**
	 * Copy constructor
	 * @param other the state to copy from
	 */
	public State(State other) {
		this(other.isDeadState, other.currentPlayer, other.holeNumber, other.pits);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentPlayer;
		result = prime * result + holeNumber;
		result = prime * result + (isDeadState ? 1231 : 1237);
		result = prime * result + Arrays.deepHashCode(pits);
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
		State other = (State) obj;
		if (currentPlayer != other.currentPlayer)
			return false;
		if (holeNumber != other.holeNumber)
			return false;
		if (isDeadState != other.isDeadState)
			return false;
		if (!Arrays.deepEquals(pits, other.pits))
			return false;
		return true;
	}
}