/**
 * @(#)View.java
 * Assignment#2
 * @author Ankush Varshneya
 * @student# 100853074
 * Main View
 */

package game;

import javax.swing.*;

import gameobjects.Board;
import gameobjects.GameTypeEnum;

import java.awt.*;
import java.util.HashMap;

@SuppressWarnings("serial")
public class View extends JPanel {
	// offsets
	public static final Dimension DIMENTION(Model m) {
		return new Dimension(240 + 100*(((m==null)? 6:m.getHoleNumber())+Model.MANCALAS_PER_PLAYER), 200 + 174*Model.NUMBER_OF_PLAYERS);
	}	

	private Model			model; // The model to which this view is attached.
	private Board			board; // The board to which this view is attached.

	private JLabel			frameRateLabel, currentLabel, plyLabel, stoneLabel, holeLabel, statLabel;
	private JTextField		frameRateField, currentField, plyField;
	private JSlider			stoneSlider, holeSlider;
	private JTextArea		statField;
	private JButton			startStopButton;
	private JToggleButton	pruneButton;
	private HashMap<Integer, JList<GameTypeEnum>>
							typeMap;
	private HashMap<Integer, JTextField>
							scoreMap;
	
	// Public method to allow access to board.
	public Board			getBoard() { return board;}

	// Public method to allow access to JComponents.
	public JLabel			getFrameRateLabel()		{ return frameRateLabel; }
	public JLabel			getCurrentLabel()		{ return currentLabel; }
	public JLabel			getPlyLabel()			{ return plyLabel; }
	public JLabel			getStoneLabel()			{ return stoneLabel; }
	public JLabel			getHoleLabel()			{ return holeLabel; }
	public JLabel			getStatLabel()			{ return statLabel; }
	public JTextField		getFrameRateField()		{ return frameRateField; }
	public JTextField		getCurrentField()		{ return currentField; }
	public JTextField		getPlyField()			{ return plyField; }
	public JSlider			getStoneSlider()		{ return stoneSlider; }
	public JSlider			getHoleSlider()			{ return holeSlider; }
	public JTextArea		getStatField()			{ return statField; }
	public JButton			getStartStopButton()	{ return startStopButton; }
	public JToggleButton	getPruneButton()		{ return pruneButton; }
	public HashMap<Integer, JList<GameTypeEnum>>
							getTypeMap()			{ return typeMap; }
	public HashMap<Integer, JTextField>
							getScoreMap()			{ return scoreMap; }
	public View() {
    	// Choose grid bag layout
		this.setLayout(new GridBagLayout());

		// player selection
		typeMap = new HashMap<Integer, JList<GameTypeEnum>>();
		scoreMap = new HashMap<Integer, JTextField>();
		for(int y = 0, offset = 0; y < Model.NUMBER_OF_PLAYERS; y++) {
			JLabel aLabel = new JLabel("Player "+y+" type:");
			((GridBagLayout) this.getLayout()).setConstraints(
				aLabel,
				this.makeConstraints(	0, offset++, 1, 1, 0, 0,
										new Insets(4, 4, 4, 4),
										GridBagConstraints.HORIZONTAL,
										GridBagConstraints.NORTH));
			this.add(aLabel);
			
			JList<GameTypeEnum> aList = new JList<GameTypeEnum>(GameTypeEnum.values());
			aList.setSelectedIndex(0);
			((GridBagLayout) this.getLayout()).setConstraints(
					aList,
				this.makeConstraints(	0, offset++, 1, 1, 0, 0,
										new Insets(4, 4, 4, 4),
										GridBagConstraints.BOTH,
										GridBagConstraints.NORTH));
			this.add(aList);
			
			typeMap.put(y, aList);
		
			aLabel = new JLabel("Player "+y+" score:");
			((GridBagLayout) this.getLayout()).setConstraints(
				aLabel,
				this.makeConstraints(	0, offset++, 1, 1, 0, 0,
										new Insets(4, 4, 4, 4),
										GridBagConstraints.HORIZONTAL,
										GridBagConstraints.NORTH));
			this.add(aLabel);
			
			JTextField aField = new JTextField();
			aField.setEditable(false);
			((GridBagLayout) this.getLayout()).setConstraints(
					aField,
				this.makeConstraints(	0, offset++, 1, 1, 0, 0,
										new Insets(4, 4, 4, 4),
										GridBagConstraints.BOTH,
										GridBagConstraints.NORTH));
			this.add(aField);
			scoreMap.put(y, aField);
		}
		
		//Stone
		this.stoneLabel = new JLabel("Number of Stone(s):");
		((GridBagLayout) this.getLayout()).setConstraints(
			this.stoneLabel,
			this.makeConstraints(	0, Model.NUMBER_OF_PLAYERS*4+1, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.stoneLabel);
		this.stoneSlider = new JSlider(JSlider.HORIZONTAL, 1,6,4);
		this.stoneSlider.setMajorTickSpacing(1);
		this.stoneSlider.setPaintTicks(true);
		this.stoneSlider.setPaintLabels(true);
		this.stoneSlider.setEnabled(true);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.stoneSlider,
			this.makeConstraints(	0, Model.NUMBER_OF_PLAYERS*4+2, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.stoneSlider);

		//Hole
		this.holeLabel = new JLabel("Number of Hole(s):");
		((GridBagLayout) this.getLayout()).setConstraints(
			this.holeLabel,
			this.makeConstraints(	0, Model.NUMBER_OF_PLAYERS*4+3, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.holeLabel);

		this.holeSlider = new JSlider(JSlider.HORIZONTAL, this.stoneSlider.getValue()-1,2*(this.stoneSlider.getValue()-1),6);
		this.holeSlider.setMajorTickSpacing(1);
		this.holeSlider.setPaintTicks(true);
		this.holeSlider.setPaintLabels(true);
		this.holeSlider.setEnabled(true);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.holeSlider,
			this.makeConstraints(	0, Model.NUMBER_OF_PLAYERS*4+4, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.holeSlider);
	
		//Frame Rate
		this.frameRateLabel = new JLabel("Frame Rate (ms):");
		((GridBagLayout) this.getLayout()).setConstraints(
			this.frameRateLabel,
			this.makeConstraints(	1, Model.NUMBER_OF_PLAYERS*4+1, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.frameRateLabel);

		this.frameRateField = new JTextField();
		this.frameRateField.setEditable(true);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.frameRateField,
			this.makeConstraints(	1, Model.NUMBER_OF_PLAYERS*4+2, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.frameRateField);

		//current player
		this.currentLabel = new JLabel("Current Player:");
		((GridBagLayout) this.getLayout()).setConstraints(
			this.currentLabel,
			this.makeConstraints(	1, Model.NUMBER_OF_PLAYERS*4+3, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.currentLabel);

		this.currentField = new JTextField();
		this.currentField.setEditable(false);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.currentField,
			this.makeConstraints(	1, Model.NUMBER_OF_PLAYERS*4+4, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.currentField);

		//Ply
		this.plyLabel = new JLabel("Ply Depth:");
		((GridBagLayout) this.getLayout()).setConstraints(
			this.plyLabel,
			this.makeConstraints(	2, Model.NUMBER_OF_PLAYERS*4+1, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.plyLabel);

		this.plyField = new JTextField();
		this.plyField.setEditable(true);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.plyField,
			this.makeConstraints(	2, Model.NUMBER_OF_PLAYERS*4+2, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.plyField);
		
		//prune button
		this.pruneButton = new JToggleButton();
		((GridBagLayout) this.getLayout()).setConstraints(
			this.pruneButton,
			this.makeConstraints(	2, Model.NUMBER_OF_PLAYERS*4+3, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.pruneButton);
		
		//Start/Stop button
		this.startStopButton = new JButton();
		((GridBagLayout) this.getLayout()).setConstraints(
			this.startStopButton,
			this.makeConstraints(	2, Model.NUMBER_OF_PLAYERS*4+4, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.startStopButton);
		

		//Stat
		this.statLabel = new JLabel("Stats:");
		((GridBagLayout) this.getLayout()).setConstraints(
			this.statLabel,
			this.makeConstraints(	3, Model.NUMBER_OF_PLAYERS*4+1, 1, 1, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.statLabel);

		this.statField = new JTextArea();
		this.statField.setEditable(false);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.statField,
			this.makeConstraints(	3, Model.NUMBER_OF_PLAYERS*4+2, 1, 3, 0, 0,
									new Insets(4, 4, 4, 4),
									GridBagConstraints.HORIZONTAL,
									GridBagConstraints.NORTH));
		this.add(this.statField);

		// Update the view
		this.update();
	}

	// Used make constraints and to shorten code.
	private GridBagConstraints makeConstraints(int gX, int gY, int gW, int gH, int wX, int wY, Insets insets, int fill, int anchor) {
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = gX;
		c.gridy = gY;
		c.gridwidth = gW;
		c.gridheight = gH;
		c.weightx = wX;
		c.weighty = wY;
		c.insets = insets;
		c.fill = fill;
		c.anchor = anchor;

		return c;
	}

	// Updates the view based on the inputed model.
	public void update() {
		// If the button is not selected its labeled "start" otherwise its labeled "stop".
		if(!this.startStopButton.isSelected()) this.startStopButton.setText("Start");
		else this.startStopButton.setText("Stop");

		// If the button is not selected its labeled "prune" otherwise its labeled "Dont prune".
		if(!this.pruneButton.isSelected()) this.pruneButton.setText("Prune");
		else this.pruneButton.setText("Dont Prune");
		
		// Update fields.
		if (this.model != null) {
			this.stoneSlider.setValue(this.model.getInitStoneNumber());
			this.holeSlider.setValue(this.model.getHoleNumber());
			this.frameRateField.setText(this.model.getFrameRate()+"");
			this.currentField.setText(this.model.getCurrentPlayer()+"");
			this.plyField.setText(this.model.getPly()+"");
			this.statField.setText(this.model.getStats());
			// update scores
			for(int y = 0; y < Model.NUMBER_OF_PLAYERS; y++)
				scoreMap.get(y).setText(this.model.playerScore(y)+"");
		}
		// update board
		if (this.board != null)
			this.board.setPits();
	}

	/**
	 *	Makes a new board based on the model passed in. 
	 * @param model the model to make the board with
	 */
	public void getNewBoard(Model model) {
		// Remove the board button, then the board if they exist
		if (this.board != null) {
			this.board.removeAll();
			this.remove(this.board);
		}
		// Update the model to the one passed in to this function.
		this.model = model;
		// Make a new board, then used the gridConstraints to add a new board.
		this.board = new Board(this.model);
		((GridBagLayout) this.getLayout()).setConstraints(
			this.board, 
			this.makeConstraints(	1, 0, 3, Model.NUMBER_OF_PLAYERS*4, 1, 1,
									new Insets(0, 0, 0, 0),
									GridBagConstraints.BOTH,
									GridBagConstraints.CENTER));
		this.add(this.board);
		// Update the UI to match the new board.
		this.board.updateUI();
		this.updateUI();
	}

    /**
     * Testing
     * @param args
     */
    public static void main(String[] args) {
    	Model aModel = new Model();
		View aView = new View();
		aView.getNewBoard(aModel);
		aView.update();
		JFrame frame = new JFrame("The Mancala Game View Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(DIMENTION(aModel));
		frame.getContentPane().add(aView);
		frame.setVisible(true);
	}
}