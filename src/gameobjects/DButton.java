/**
 * @(#)DirectionButton.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * A button which keeps track of direction
 */

package gameobjects;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class DButton extends JButton{
	private	DirectionEnum 	direction;

	public	DirectionEnum 	getDirection()	 						{ return direction; }
	public 	void 			setDirection(DirectionEnum direction) 	{ this.direction = direction; }
}