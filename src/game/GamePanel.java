package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int WIDTH = 900;
	private int HEIGHT = 900;

	private int columns = 1000;
	private int rows = 400;

	private int marginCol = -200;

	private int boxSize = 3;
	private Color aliveColor = Color.BLACK;

	private int currentRow = 0;

	private boolean[][] gameState;


	private boolean started = false;

	private int ruleset = 1;

	private Boolean[] rulesetBit;

	public GamePanel() {
		super();
		this.setSize(WIDTH,HEIGHT);
		this.setBackground(Color.WHITE);

		init();

	}



	private void init() {
		gameState = new boolean[rows][columns];

		populate1stGen((int)(columns/boxSize/1.5));
		//		printNeighbors();
		//create100Rows();

		rulesetBit = convertToBit(ruleset);
		//		for(int j = 0 ; j< rows - 1;j++) {
		//			applyRules(currentRow);
		//			currentRow++;
		//		}
	}

	public void setRuleset(int rule) {
		if(rule>=0 && rule<256) {
			ruleset = rule;
			rulesetBit = convertToBit(ruleset);
		}
	}

	public void reinit() {
		currentRow = 0;
		gameState = new boolean[rows][columns];

		populate1stGen((int)(columns/boxSize/1.5));
		//		printNeighbors();
		//create100Rows();
		repaint();

		rulesetBit = convertToBit(ruleset);



	}

	// Create a new row by applying rules
	private void applyRules(int row) {

		for(int col=0;col<columns;col++) {
			if(getNeighborBit((row-1),col).equals("000")) gameState[row][col] = rulesetBit[7];
			else if(getNeighborBit((row-1),col).equals("001")) gameState[row][col] = rulesetBit[6];
			else if(getNeighborBit((row-1),col).equals("010")) gameState[row][col] = rulesetBit[5];
			else if(getNeighborBit((row-1),col).equals("011")) gameState[row][col] = rulesetBit[4];
			else if(getNeighborBit((row-1),col).equals("100")) gameState[row][col] = rulesetBit[3];
			else if(getNeighborBit((row-1),col).equals("101")) gameState[row][col] = rulesetBit[2];
			else if(getNeighborBit((row-1),col).equals("110")) gameState[row][col] = rulesetBit[1];
			else if(getNeighborBit((row-1),col).equals("111")) gameState[row][col] = rulesetBit[0];
		}


	}

	private Boolean[] convertToBit(int ruleset) {
		String bit = Integer.toBinaryString(ruleset);
		Boolean[] formattedBit = new Boolean[8];
		if(bit.length()!=8) {
			int zerosToAdd = 8 - bit.length();
			int zeroesAdded = 0;
			for(int i=0;i<zerosToAdd;i++) {
				formattedBit[i] = false;
				zeroesAdded ++;
			}
			for(int i=0;i<bit.length();i++) {
				formattedBit[zeroesAdded + i] = bit.charAt(i) == '1';
			}

		}
		else {
			for(int i=0;i<bit.length();i++) {
				formattedBit[i] = bit.charAt(i) == '1';
			}
		}
		return formattedBit;
	}

	private void createGrids(Graphics2D g2d) {
		for(int row = 0;row< rows;row++) {
			g2d.setColor(Color.gray);
			for(int col = 0;col<columns;col++) {
				g2d.drawRect(col * boxSize, row * boxSize, boxSize, boxSize);
			}
		}
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;

		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		update(g2d);
	}

	private void update(Graphics2D g2d) {
		createGrids(g2d);
		paintCells(g2d);

		if(currentRow<rows) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			applyRules(currentRow);
			currentRow++;
		}


	}

	//Returns 000,010,.....111
	private String getNeighborBit(int row,int col) {
		String neighborBit = "";
		if(col == 0) {
			String neighborBit1 = "0";
			String neighborBit2 = (gameState[row][col] == true ? "1" : "0" );
			String neighborBit3 = (gameState[row][col+1] == true ? "1" : "0");
			neighborBit = neighborBit1 + neighborBit2 + neighborBit3;
		}
		else if(col == columns - 1) {
			String neighborBit1 = (gameState[row][col-1] == true ? "1" : "0");
			String neighborBit2 = (gameState[row][col] == true ? "1" : "0" );
			String neighborBit3 = "0";
			neighborBit = neighborBit1 + neighborBit2 + neighborBit3;
		}
		else {
			String neighborBit1 = (gameState[row][col-1] == true ? "1" : "0");
			String neighborBit2 = (gameState[row][col] == true ? "1" : "0" );
			String neighborBit3 = (gameState[row][col+1] == true ? "1" : "0");
			neighborBit = neighborBit1 + neighborBit2 + neighborBit3;
		}

		return neighborBit;
	}

	private void paintCells(Graphics2D g2d) {
		for(int row = 0;row< rows;row++) {
			for(int column=0;column<columns;column++) {
				if(gameState[row][column]==true) {
					g2d.setColor(aliveColor);
					g2d.fillRect(column * boxSize + marginCol + 1, row * boxSize + 1 , boxSize - 1, boxSize - 1);
				}

			}
		}
	}




	private void populate1stGen(int colToPopulate) {

		//int row = 0;
		for(int col=0;col<columns;col++) {
			if(col==colToPopulate) gameState[0][col] = true;
			else gameState[0][col] = false;
		}
		currentRow++;

	}



	@Override
	public void run() {
		repaint();
	}


}
