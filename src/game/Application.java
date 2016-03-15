package game;
/**
 * @(#)Application.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Main Application / Controller
 */

import java.awt.event.*; // Needed for ActionListener.
import gameobjects.DButton;
import gameobjects.DirectionEnum;
import gameobjects.GameTypeEnum;
import gameobjects.PitComponent;

import javax.swing.*; // Needed for JFrame.
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import artificialintelligence.Node;
import artificialintelligence.Search;

import java.awt.*; //Needed for Component.

@SuppressWarnings("serial")
public class Application extends JFrame implements ActionListener, ChangeListener, ItemListener{

	private Model 	model; 			// The model to which this view is attached.
	private View 	view; 			// The view will show the state of the model.
	private Timer 	timer; 			// timer to check for turns
	private int 	currentPlayer;	// who is current player needed to check if player changed	
	private boolean	goAgain;		// needed to get ai to go again as needed
	private boolean gameInProgress;

    public Model getModel() { return model;	}
	public View getView() { return view; }
	public boolean isGameInProgress() {	return gameInProgress; }

	public Application(String title) {
    	super(title); // Sets the title for the window.

    	this.view = new View(); // only make the view

		// add action listener to the start/stop button clicking the start button will start a new game.
		this.view.getStartStopButton().addActionListener(this);
		// add change listener to the slider to update other slider
		this.view.getStoneSlider().addChangeListener(this);
		// add item listener
		this.view.getPruneButton().addItemListener(this);
		// add the view
		this.getContentPane().add(view);
		
		// timer
		timer = new Timer(100, this);

		// Manually computed sizes.
		this.setSize(View.DIMENTION(this.model));
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	/**
	 * This is the single event handler for all the buttons
	 * @param e event
	 */
    public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.view.getStartStopButton()){
			if(e.getActionCommand()=="Start") {	this.handleStartButtonSelection(); } // Deal with the start button.
			else if (e.getActionCommand()=="Stop") { this.handleStopButtonSelection(); } // Deal with the stop button.
		}
		else if(e.getSource() == timer) {
			handleTimerTick(e);
		}
		else { handleMoveButtonSelection(e); } // Move buttons are the other buttons remaining.
     	this.view.update();	// Update the view.
    }
	
    /**
	 * This is the single change handler for all the sliders
	 * @param e event
     */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == this.view.getStoneSlider()){
			// reset min/max of hole slider
			this.view.getHoleSlider().setMinimum(this.view.getStoneSlider().getValue()-1);
			this.view.getHoleSlider().setMaximum(2*(this.view.getStoneSlider().getValue()-1));
			this.view.getHoleSlider().setValue(2*(this.view.getStoneSlider().getValue()-1));
		}
	}
    
	/**
	 * This it the single item state change handler
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == this.view.getPruneButton()) {
			int state = e.getStateChange();
			if (this.model != null) {
				this.model.setPrune(state == ItemEvent.SELECTED);
			}
		}
     	this.view.update();	// Update the view.
	}

	/**
	 * Handle timer
	 */
	private void handleTimerTick(ActionEvent e) {
		if(this.currentPlayer != this.model.getCurrentPlayer() || goAgain){
			// this means a player changed
			// update it
			this.currentPlayer = this.model.getCurrentPlayer();
			
			// handle the change
			
			// if this is an ai player calculate the move otherwise do nothing
			if(this.view.getTypeMap().get(currentPlayer).getSelectedValue() != GameTypeEnum.HUMAN) {
				// calculate move
				Node aNode = Search.MiniMaxNextMove(this.model, this.view.getTypeMap().get(currentPlayer).getSelectedValue(), currentPlayer);
			
				if (aNode != null) {
					Point loc = aNode.getActionPit().getLocation();
					DirectionEnum action = aNode.getAction();
					this.getView().getBoard().getPitButtons()[loc.x][loc.y].getButton(action).doClick();
				}
			}
		}
		
		// if its still your turn in the model then go gain
		goAgain = this.currentPlayer == this.model.getCurrentPlayer();
		
		// if go gain then slow down to show move
		if(goAgain) {
			try {
				Thread.sleep(this.getModel().getFrameRate());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Handle moves
	 * @param e event
	 */
    private void handleMoveButtonSelection(ActionEvent e) {
		gameInProgress=!(model.isOver());
		// If the game is not in progress simulate pressing the stop button.
		if(!gameInProgress) {
			this.model.endGame();
			this.handleStopButtonSelection();
		}
		// else play the move!
		else {
			DButton source = (DButton)e.getSource();
			PitComponent p = (PitComponent) source.getParent();
			if(this.model.move(p.getModel(), source.getDirection())){
				this.model.switchPlayer();
			}
			// check again!
			gameInProgress=!(model.isOver());
			// If the game is not in progress simulate pressing the stop button.
			if(!gameInProgress) {
				this.model.endGame();
				this.handleStopButtonSelection();
			}
		}
	}
    
    /**
     * Starts a new game
     */
	private void handleStartButtonSelection() {
		this.gameInProgress = true;
		
		this.model = new Model(	this.view.getHoleSlider().getValue(),
								Integer.parseInt(this.view.getFrameRateField().getText()),
								Integer.parseInt(this.view.getPlyField().getText()),
								this.view.getStoneSlider().getValue(),
								this.view.getPruneButton().isSelected());
		this.view.getNewBoard(this.model);
	
		// This labels the button "Stop".
		this.view.getStartStopButton().setSelected(true);
		
		// disable the parameter fields
       	this.view.getHoleSlider().setEnabled(false);
       	this.view.getStoneSlider().setEnabled(false);
       	this.view.getFrameRateField().setEnabled(false);
       	this.view.getPlyField().setEnabled(false);
       	       	
		// Manually computed sizes.
		this.setSize(View.DIMENTION(this.model));
		
		// add action to buttons
		for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++) {
			for(int x = 1; x < this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER - 1; x++) {
				this.view.getBoard().getPitButtons()[x][y].getLeftButton().addActionListener(this);
				this.view.getBoard().getPitButtons()[x][y].getRightButton().addActionListener(this);
			}
		}
		
		// Start Timer with new frame delay
		timer.setDelay(this.getModel().getFrameRate());
		timer.start();
   	
		// set the current player to a non-player to force timer evaluation
		this.currentPlayer = -1;
		this.goAgain = false;
	}

	/**
	 * Stops the current game
	 */
    private void handleStopButtonSelection() {
		this.gameInProgress = false;

    	// Get All The components of the board (the Jbuttons) and Disable all of them so we can show the board.
		Component c[] = this.view.getBoard().getComponents();
		for(int i=0; i<c.length; i++) ((PitComponent)c[i]).setEnabled(false);

		// This labels the button "Start".
		this.view.getStartStopButton().setSelected(false);
		
		// enable the parameter fields
       	this.view.getHoleSlider().setEnabled(true);
       	this.view.getStoneSlider().setEnabled(true);
       	this.view.getFrameRateField().setEnabled(true);
       	this.view.getPlyField().setEnabled(true);
       	
       	// add action to buttons
		for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++) {
			for(int x = 1; x < this.model.getHoleNumber() + Model.MANCALAS_PER_PLAYER - 1; x++) {
				this.view.getBoard().getPitButtons()[x][y].getLeftButton().removeActionListener(this);
				this.view.getBoard().getPitButtons()[x][y].getRightButton().removeActionListener(this);
			}
		}
		this.model.setStats((this.model.getStats()!=null? this.model.getStats()+"\n":"")+"Winner: Player#"+this.model.getWinner());
    
		// Stop Timer
		timer.stop();
    }
        
	/**
	 *  This is where the program begins.
	 */
    public static void main(String[] args) {
		JFrame frame = new Application("The Mancala Game");
		frame.setVisible(true);
	}
}