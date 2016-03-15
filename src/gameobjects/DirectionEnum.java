/**
 * @(#)DirectionEnum.java
 * Assignment#1
 * @author Ankush Varshneya
 * @student# 100853074
 * The Direction Enum
 */

package gameobjects;

public enum DirectionEnum {
	LEFT(-1), 
	RIGHT(1);
	
	private int value;
	
	/**
	 * Main constructor
	 * @param val its value
	 */
	private DirectionEnum(int val){
		this.value = val;
	}

	public int getValue() { return this.value; }
	
	/**
	 * Gets the opposite direction of a given direction
	 * @return			the opposite direction
	 */
	public DirectionEnum opposite() {
		if (this == LEFT)
			return RIGHT;
		else
			return LEFT;
	}
}