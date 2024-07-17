package chuzzgen;

import javax.swing.*;
import java.awt.*;

public class Square extends JButton
{
    private int xPos, yPos;
    private Piece piece;
    public String colourName;
    
    public Square(Color squareColour, int xPos, int yPos, Piece piece)
    {
    	this.setBackground(squareColour);
    	this.xPos = xPos;
    	this.yPos = yPos;
    	this.piece = piece;
    	this.addActionListener(e -> squareClicked());
    }

	public int getXPosition()
    {
        return xPos;
    }
    
    public int getYPosition()
    {
        return yPos;
    }
    
    public Piece getPieceOnSquare()
    {
        return piece;
    }
    
    public void setPieceOnSquare(Piece piece) {
    	if (piece instanceof King) ((King) piece).setCurrentLocation(this);
    	this.piece = piece;
    	this.setIcon(piece.PIECE_IMAGE);
    }
    
    public void removePieceFromSquare() {
    	this.setIcon(null);
    	this.piece = null;
    }
    
    public boolean isPieceOnSquare() {
    	return getPieceOnSquare() != null;
    }
    
    private void squareClicked() {
    	try {
    		if (!(Game.moveInProgress)) {
    			Game.moveInProgress = true;
    			Board.saveStart(this);
    		}
    		else {
    			Game.moveInProgress = false;
    			// Check if it's the correct player's turn
    			if (Board.start.isPieceOnSquare()) {
    			    if (Board.start.getPieceOnSquare().getColour() == Game.currentPlayer.getColour()) {
    				    Game.makeMove(Board.start, this);
    			    }
    			}
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
	}
}