/**
 * @(#)Search.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * The solution searcher
 */

package artificialintelligence;

import java.util.LinkedList;
import java.util.PriorityQueue;

import game.Model;
import gameobjects.DirectionEnum;
import gameobjects.GameTypeEnum;
import gameobjects.Pit;

public class Search {

	private static int nodesProcessed;
	/**
	 * MiniMax search to find the next move
	 * @param problem need for make initial state
	 * @param gametype used to get heuristic 
	 * @param player the player who's turn this search is ford
	 * @return the last node
	 */
	public static Node MiniMaxNextMove(Model problem, GameTypeEnum gametype, int player) {
		Node retNode = null;
				
		if(problem.getCurrentPlayer() == player) {	
			long startTime = System.currentTimeMillis();			// start a timer
			nodesProcessed = 0;										// nodes process in total
			Heuristic heuristic = new Heuristic(gametype, player);	// make a heuristic based on game type and the player
			
			Node node = new Node(new State(problem));				// initial node
			node.setAction(null);			 					  	// no action for initial node
			node.setActionPit(null);							  	// no action for initial node
			
			LinkedList<Node> expandedChildren = expandNode(node);	// Expand this node to get children
			nodesProcessed += expandedChildren.size();				// Increase node processed count
			
			// fringe queue to keep unprocessed nodes
			PriorityQueue<Node> fringe = new PriorityQueue<Node>(heuristic);
			
			// process children
			expandedChildren.forEach((aNode) -> {
				// Pruning -> then use alpha beta to prune
				if(problem.isPrune())
					aNode.setValue(minValue(aNode, problem.getPly(), heuristic, Integer.MIN_VALUE, Integer.MAX_VALUE));
				// No pruning -> examine whole tree
				else 
					aNode.setValue(minValue(aNode, problem.getPly(), heuristic));
				
				fringe.add(aNode);
			});
			
			// Return node with highest value
			retNode = fringe.poll();
			
			// set the stat
			problem.setStats(	"Using "+gametype+": " + (problem.isPrune()? "with":"without") + " Pruning\n" +
								"Took " + ((System.currentTimeMillis() - startTime)/1000.0) + " seconds\n" +
								"Expanded " + nodesProcessed + " nodes\n" +
								"Depth="+problem.getPly()+
								", Number of Hole(s)="+problem.getHoleNumber()+
								", Stone(s) Per Hole="+problem.getInitStoneNumber()+"\n"+
								"Button="+retNode.getAction().toString()+"point="+retNode.getActionPit().getLocation().toString());
		}
		
		return retNode;
	}

	/**
	 * Method of getting the max (best) move from a given node Perspective of the player
	 * From the given node, gets all the next moves the other player could make,
	 * and return the value of the maximum move
	 * @param node the node above
	 * @param ply the depth
	 * @param heuristic 
	 * @return the best move
	 */
	private static int maxValue(Node node, int ply, Heuristic heuristic) {
		// Stop going deeper in tree
		if (ply <= 0 || node.getState().isDeadState())
			return heuristic.getHeuristic(node);
		
		int value = Integer.MIN_VALUE;

		// Expand this node to get children
		LinkedList<Node> expandedChildren = expandNode(node);
		nodesProcessed += expandedChildren.size();
		for (Node aNode : expandedChildren)
			value = Math.max(value, minValue(aNode, ply-1, heuristic));

		return value;
	}
	
	/**
	 * Method of getting the min (worst) move from a given node Perspective of opponent
	 * From the given node, gets all the next moves I could make and have the other player
	 * return the value of the minimum move
	 * @param node the node above
	 * @param ply the depth
	 * @param heuristic 
	 * @return the worst move
	 */
	private static int minValue(Node node, int ply, Heuristic heuristic) {
		// Stop going deeper in tree
		if (ply <= 0 || node.getState().isDeadState())
			return heuristic.getHeuristic(node);
		
		int value = Integer.MAX_VALUE;
		
		// Expand this node to get children
		LinkedList<Node> expandedChildren = expandNode(node);
		nodesProcessed += expandedChildren.size();		
		for (Node aNode : expandedChildren)
			value = Math.min(value, maxValue(aNode, ply-1, heuristic));
		
		return value;
	}

	/**
	 * Method of getting the max (best) move from a given node Perspective of the player
	 * From the given node, gets all the next moves the other player could make,
	 * and return the value of the maximum move using alpha-beta pruning
	 * @param node the node above
	 * @param ply the depth
	 * @param heuristic 
	 * @param alpha
	 * @param beta
	 * @return the best move
	 */
	private static int maxValue(Node node, int ply, Heuristic heuristic, int alpha, int beta) {
		// Stop going deeper in tree
		if (ply <= 0 || node.getState().isDeadState())
			return heuristic.getHeuristic(node);
		
		int value = Integer.MIN_VALUE;

		// Expand this node to get children
		LinkedList<Node> expandedChildren = expandNode(node);
		nodesProcessed += expandedChildren.size();
		for (Node aNode : expandedChildren) {
			value = Math.max(value, minValue(aNode, ply-1, heuristic, alpha, beta));
			alpha = Math.max(alpha, value);
			
			// if beta is bigger than alpha, no need to continue exploring
			if (beta <= alpha)
				break;
		}

		return value;
	}
	
	/**
	 * Method of getting the min (worst) move from a given node Perspective of opponent
	 * From the given node, gets all the next moves I could make and have the other player
	 * return the value of the minimum move using alpha-beta pruning
	 * @param node the node above
	 * @param ply the depth
	 * @param heuristic 
	 * @param alpha
	 * @param beta
	 * @return the worst move
	 */
	private static int minValue(Node node, int ply, Heuristic heuristic, int alpha, int beta) {
		// Stop going deeper in tree
		if (ply <= 0 || node.getState().isDeadState())
			return heuristic.getHeuristic(node);
		
		int value = Integer.MAX_VALUE;
		
		// Expand this node to get children
		LinkedList<Node> expandedChildren = expandNode(node);
		nodesProcessed += expandedChildren.size();		
		for (Node aNode : expandedChildren) {
			value = Math.min(value, maxValue(aNode, ply-1, heuristic, alpha, beta));
			beta = Math.min(beta, value);
			
			// if beta is bigger than alpha, no need to continue exploring
			if (beta <= alpha)
				break;
		}
		
		return value;
	}
	
	/**
	 * Expand method for expanding child nodes
	 * @param node		the parent node
	 * @return list of children nodes
	 */
	private static LinkedList<Node> expandNode(Node node) {
		LinkedList<Node> sucessor = new LinkedList<Node>(); // Successor list to keep child nodes

		int currentPlayer = node.getState().getCurrentPlayer();
		Pit pits[][] = node.getState().getPits();
		
		// next moves for current player (to keep things short do not visit pits with zero stones)
    	for(int x = 0; x < (node.getState().getHoleNumber()+Model.MANCALAS_PER_PLAYER); x++) {
    		Pit oldPit = pits[x][currentPlayer];
    		// non-empty hole is next move
    		if(oldPit.isHole() && !oldPit.isEmpty()){
    			// name more in all directions {left / right}
    			for(DirectionEnum d : DirectionEnum.values()){
    				// make new child state with copy of parent state
    				State aState = new State(node.getState());

        			// new pit from new state (can't use oldPit as its from old state)
            		Pit aPit = aState.getPits()[x][currentPlayer];

        			// simulate moving and switch players if needed
        			if(move(aState, aPit, d))
        				switchPlayer(aState);
        			else // its a dead state as we go again no need to check opponent
        				aState.setDeadState(true);
    				
        			// if game over endGame and set state as dead
        			if(isOver(aState)) {
        				aState.setDeadState(true);
        				endGame(aState);
        			}
        			
        			// new node
        			Node aNode = new Node(aState);
        			aNode.setAction(d);
        			aNode.setActionPit(aPit);
        			// add the new node
        			sucessor.add(aNode);
    	    	}
    		}
    	}
    	return sucessor;
	}	

	/**
	 * @param aState state to check with
	 * @return true game is over, if one of the players holes are all empty
	 */
	private static boolean isOver(State aState) {
		int empty;
    	for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++){
			 empty = 0;
    		for(int x = 1; x < aState.getHoleNumber() + Model.MANCALAS_PER_PLAYER-1; x++)
   				if (aState.getPits()[x][y].isEmpty())
   					empty++;
    		if (empty == aState.getHoleNumber()) // that means one side is empty
    			return true;
    	}
    	return false;
    }

    /**
	 * @param aState state to check with
     * the opponent empties its pits in its mancala
     */
	 private static void endGame(State aState){
    	// switch the player
    	for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++){
    		for(int x = 1; x < aState.getHoleNumber() + Model.MANCALAS_PER_PLAYER-1; x++) {
    			int st = aState.getPits()[x][y].removeStones();
    			aState.getPits()[0][y].addStones(st); // drop in mancala
    		}
    	}
    }
    
    /**
     * Make a move in a direction
	 * @param aState state to check with
     * @param pit the pit to move from
     * @param dir direction to move in
     * @return true if turn over false for another turn
     */
	 private static boolean move(State aState, Pit pit, DirectionEnum dir){
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
    				if((x+dir.getValue()) > (aState.getHoleNumber()+Model.MANCALAS_PER_PLAYER-1)){
    					y = (y + 1)%Model.NUMBER_OF_PLAYERS;
    					dir = dir.opposite();
    				}
    				// Otherwise move in the direction
    				else {
    					x+=dir.getValue();
    				}
        		}

    			Pit aPit = aState.getPits()[x][y];
    			
				// if its a hole drop the stone
    			if(aPit.isHole()){
    				hand--;
    				aPit.addStones(1);
    				// if the hole has 0 stone meaning you dropped it in a empty hole
    				// then you get to take your opponents opposite sided
    				// holes stones and put them all in you mancala 
    				if(hand == 0 &&  aPit.getPlayer() == aState.getCurrentPlayer())
    					aState.getPits()[0][y].addStones(
    						aState.getPits()[x][(y+1)%Model.NUMBER_OF_PLAYERS].removeStones()
    					);
    			}
    			// if its a mancala drop the stone only if its the current players
    			else if(aPit.isMancala() && aPit.getPlayer() == aState.getCurrentPlayer()) {
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
	 * @param aState state to check with
	 */
	 private static void switchPlayer(State aState){
		 aState.setCurrentPlayer((aState.getCurrentPlayer()+1)%Model.NUMBER_OF_PLAYERS); 
	}
}