/**
 * @(#)PitButton.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * A pit button
 */

package gameobjects;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import image.Icon;

@SuppressWarnings("serial")
public class PitComponent extends JPanel{
	private DButton 		leftButton;
	private DButton			rightButton;
	private JLabel			text;
	private Pit				model;
	
	// Public method to allow access to JComponents.
	public Pit 			getModel() 			{ return model; }
	public DButton 		getLeftButton() 	{ return leftButton; }
	public DButton 		getRightButton()	{ return rightButton; }
	public JLabel	 	getText()			{ return text; }
	public void 		setModel(Pit model) { this.model = model; }
	public DButton		getButton(DirectionEnum d) {
		return (d == DirectionEnum.LEFT)? getLeftButton() : getRightButton();
	}
	
	/**
	 * Main constructor
	 * @param pit inner pit model
	 */
	public PitComponent(Pit pit) {
		// set model
		this.model = pit;
		
		// choose border layout
		this.setLayout(new BorderLayout());
		
		// if its hole then only display arrows
		if(this.model.isHole()) {
			this.leftButton = new DButton();
			this.leftButton.setEnabled(true);
			this.leftButton.setDirection(DirectionEnum.LEFT);
			this.leftButton.setIcon(Icon.Left);
			this.leftButton.setPreferredSize(new Dimension(20, 20));
			this.add(BorderLayout.WEST, this.leftButton);
			
			this.rightButton = new DButton();
			this.rightButton.setDirection(DirectionEnum.RIGHT);
			this.rightButton.setEnabled(true);
			this.rightButton.setIcon(Icon.Right);
			this.rightButton.setPreferredSize(new Dimension(20, 20));
			this.add(BorderLayout.EAST, this.rightButton);
		}
		this.text = new JLabel(this.model.isHole()? Icon.Hole: Icon.Mancala);
		this.text.setFont(new Font(this.text.getFont().getName(), Font.PLAIN, 25));
		this.text.setText(this.model.getStones()+"");
		this.text.setIconTextGap(-this.text.getIcon().getIconWidth()/2-10);
	    this.add(BorderLayout.CENTER,this.text);
	}

	/**
	 * Disable or enable buttons
	 * @param t true?
	 */
	public void setEnabled(boolean t){
		if(this.leftButton != null)
			this.leftButton.setEnabled(t);
		if(this.rightButton != null)
			this.rightButton.setEnabled(t);
	}
	
	/**
	 * Set selected
	 * @param t true?
	 */
	public void setSelected(boolean t){
		if(this.leftButton != null)
			this.leftButton.setSelected(t);
		if(this.rightButton != null)
			this.rightButton.setSelected(t);
	}
	
	/**
	 * update pit
	 */
	public void update() {
		this.text.setText(this.model.getStones()+"");
		// disable if no stone
		if(this.model.isEmpty())
			this.setEnabled(false);
	}
}