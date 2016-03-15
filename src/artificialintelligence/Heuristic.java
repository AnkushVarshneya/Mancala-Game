/**
 * @(#)Heuristic.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Heuristic
 */

package artificialintelligence;

import java.util.Comparator;

import game.Model;
import gameobjects.GameTypeEnum;
import gameobjects.Pit;

public class Heuristic implements Comparator<Node> {
	private GameTypeEnum 	type;
	private int 			player;
	
	public Heuristic(GameTypeEnum type, int player) {
		this.type = type;
		this.player = player;
	}

	/**
	 * Comparator for A Star computes f in f = g + h
	 */
	@Override
	public int compare(Node o1, Node o2) {
		return o2.getValue() - o1.getValue();
	}

	/**
	 * Get h
	 * @param n the node
	 * @return the heuristic cost
	 */
	public int getHeuristic(Node n) {
		switch(this.type){
		case HOLEHEURISTIC:
			return this.holeHeuristic(n);
		case MANCALAHEURISTIC:
			return this.mancalaHeuristic(n);
		case HOLEHEURISTICCOMPARISION:
			return this.holeHeuristicCompare(n);
		case MANCALAHEURISTICCOMPARISION:
			return this.mancalaHeuristicCompare(n);
		default:
			return 0; // else just 0
		}
	}
	
	/**
	 * Gets the stones in all holes
	 * @param n the node
	 * @return the h value
	 */
	private int holeHeuristic(Node n) {
		int playerCount = 0;
		for(int x = 0; x < n.getState().getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++) {
			Pit aPit = n.getState().getPits()[x][player];
			if(aPit.isHole())
				playerCount += aPit.getStones();
		}
		return playerCount;
	}
	
	/**
	 * Gets the stones in all mancala stones of player - opponent
	 * @param n the node
	 * @return the h value
	 */
	private int mancalaHeuristic(Node n) {
		int playerCount = 0;
		for(int x = 0; x < n.getState().getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++) {
			Pit aPit = n.getState().getPits()[x][player];
			if(aPit.isMancala())
				playerCount += aPit.getStones();
		}
		return playerCount;
	}

	/**
	 * Gets the stones in all holes stones of player - opponent
	 * @param n the node
	 * @return the h value
	 */
	private int holeHeuristicCompare(Node n) {
		int playerCount = 0;
		for(int x = 0; x < n.getState().getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++){
			Pit aPit = n.getState().getPits()[x][player];
			if(aPit.isHole())
				playerCount += aPit.getStones();
		}
		
		int opponentCount = 0;
		for(int x = 0; x < n.getState().getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++){
			Pit aPit = n.getState().getPits()[x][(player+1)%Model.NUMBER_OF_PLAYERS];
			if(aPit.isHole())
				opponentCount += aPit.getStones();
		}
		return playerCount-opponentCount;
	}
	
	/**
	 * Gets the stones in all mancala stones of player - opponent
	 * @param n the node
	 * @return the h value
	 */
	private int mancalaHeuristicCompare(Node n) {
		int playerCount = 0;
		for(int x = 0; x < n.getState().getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++){
			Pit aPit = n.getState().getPits()[x][player];
			if(aPit.isMancala())
				playerCount += aPit.getStones();
		}
		
		int opponentCount = 0;
		for(int x = 0; x < n.getState().getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++){
			Pit aPit = n.getState().getPits()[x][(player+1)%Model.NUMBER_OF_PLAYERS];
			if(aPit.isMancala())
				opponentCount += aPit.getStones();
		}
		return playerCount-opponentCount;
	}
}