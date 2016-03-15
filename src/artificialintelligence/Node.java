/**
 * @(#)Node.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Used to represent a node in the search
 */

package artificialintelligence;

import gameobjects.DirectionEnum;
import gameobjects.Pit;

public class Node {
	private int 			value;		// node value
	private State 			state;		// state of node
	private DirectionEnum	action;		// action of node
	private Pit				actionPit;	// the pit resulting from the action
	

	public int 				getValue()			{ return value; }
	public State 			getState()			{ return state; }
	public DirectionEnum	getAction()			{ return action; }
	public Pit 				getActionPit() 		{ return actionPit;	}
	
	public void	setValue(int value) 			{ this.value = value; }
	public void	setState(State state) 			{ this.state = state; }
	public void setAction(DirectionEnum action)	{ this.action = action;	}
	public void	setActionPit(Pit actionPit) 	{ this.actionPit = actionPit; }

	/**
	 * Main constructor 
	 * @param state state to make it for.
	 */
	public Node(State state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Node other = (Node) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}	
}
