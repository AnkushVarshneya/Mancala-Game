/**
 * @(#)Board.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Board View
 */

package gameobjects;

import javax.swing.*;
import java.awt.*;

import game.Model;

@SuppressWarnings("serial")
public class Board extends JPanel {
	private Model 				model; 			// The model to which this view is attached.
	private PitComponent[][]	pitButtons; 	// square button array.

    // Public methods to allow access to view private variable.
	public PitComponent[][]		getPitButtons()	{ return this.pitButtons; }
	public Model				getModel() 		{ return this.model; }

	/**
     * Main constructor 
	 * @param m the model
	 */
    public Board(Model m) {
    	this.model = m;

    	// Choose grid layout.
    	this.setLayout(new GridLayout(Model.NUMBER_OF_PLAYERS, this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER, 10, 10));

		// Initialize arrays of JButtons.
    	this.pitButtons = new PitComponent[this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER][Model.NUMBER_OF_PLAYERS];

    	// Sets board.
		for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++) {
			for(int x = 0; x < this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++) {
    			// Make the board's buttons
    			this.pitButtons[x][y] = new PitComponent(this.model.getPits()[x][y]);
    			this.pitButtons[x][y].setBackground(( x != 0 && x != this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER - 1)? Color.white : Color.gray);
    			// Add the button to the panel.
				this.add(pitButtons[x][y]);
				// Set the button unselected.
				this.pitButtons[x][y].setSelected(false);
    		}
    	}
		
		// set pits
		this.setPits();
    }
    
    /**
     * Sets the pits
     */
    public void setPits() {
    	for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++) {
			for(int x = 0; x < this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER; x++) {
    			// set active players buttons enabled
    			this.pitButtons[x][y].setEnabled(this.model.getCurrentPlayer() == y);
    			this.pitButtons[x][y].update();
			}
    	}
	}
    
    /**
     * Testing
     * @param args
     */
    public static void main(String[] args) {
    	Model aModel = new Model();
		Board aView = new Board(aModel);

		JFrame frame = new JFrame("The Mancala Game Board Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(100*(aModel.getHoleNumber()+Model.MANCALAS_PER_PLAYER), 117*Model.NUMBER_OF_PLAYERS); // Manually computed sizes.
		frame.getContentPane().add(aView);
		frame.setVisible(true);
	}
}