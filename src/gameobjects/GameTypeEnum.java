/**
 * @(#)GameTypeEnum.java
 * Assignment#1
 * @author Ankush Varshneya
 * @student# 100853074
 * The Game Type Enum
 */

package gameobjects;

public enum GameTypeEnum {
	HUMAN("Human Player"), 
	HOLEHEURISTIC("Stones In Hole Heuristic"),
	MANCALAHEURISTIC("Stones In Mancala Heuristic"), 
	HOLEHEURISTICCOMPARISION("Stones In Hole Heuristic Comparision"),
	MANCALAHEURISTICCOMPARISION("Stones In Mancala Heuristic Comparision");
	
	private String displayName;
	
	/**
	 * Main constructor
	 * @param dn display name
	 */
	private GameTypeEnum(String dn){
		this.displayName = dn;
	}

	public String getDisplayName() { return this.displayName; }
	
	/**
	 * toString does display name
	 */
	public String toString() {
		return displayName;
	}
}
