package chuzzgen;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.stream.events.StartDocument;

public class Pawn extends Piece {
	private boolean firstMoveMade = false;
	
    public Pawn(ImageIcon pieceImage, String colour, char FEN) {
    	super(pieceImage, colour, true, FEN);
    }
    
    @Override
    public boolean canMove(Square start, Square end) {
    	if (Game.currentPlayer == Game.player1) {
    		if (end.getXPosition() >= start.getXPosition() || start.getXPosition() - end.getXPosition() > 2) {
    			return false;
    		}
    		
    		if (!canMoveTwo()) {
    			if (start.getXPosition() - end.getXPosition() > 1) {
    				return false;
    			}
    		}
    	}
    	else {
    		if (end.getXPosition() <= start.getXPosition() || end.getXPosition() - start.getXPosition() > 2) {
    			return false;
    		}
    		
    		if (!canMoveTwo()) {
    			if (end.getXPosition() - start.getXPosition() > 1) {
    				return false;
    			}
    		}
    	}
    	
		// Check path infront is clear (if moving forward 2)
		if (Math.abs(start.getXPosition() - end.getXPosition()) == 2) {
		  	if (Game.board.getSquares()[(start.getXPosition() + end.getXPosition()) / 2][start.getYPosition()].isPieceOnSquare()) {
				return false;
			}  
		}
		
    	// Implement taking
		if (end.isPieceOnSquare()) {
			if (canCapture(start, end)) { 
				capture(end); // Capture the piece
			}
			else {
				return false;
			}
		}
		else {
			if (Math.abs(start.getXPosition() - end.getXPosition()) >= 1 && Math.abs(start.getYPosition() - end.getYPosition()) >= 1) {
				return false;
			}
		}

		// Implement En Passant rule (later)
    	
    	return isAlive();
    }
    
    @Override
    public boolean canCapture(Square start, Square end) {
    	// Check take is valid
    	if (end.getPieceOnSquare().getColour() != Game.currentPlayer.getColour()) {
    		return Math.abs(start.getXPosition() - end.getXPosition()) == 1 && Math.abs(start.getYPosition() - end.getYPosition()) == 1;
    	}
    	return false;
    }
    
    /**
     * Check if pawn is at the end of the board (so promote)
     */
    public Piece promote(Square start, Square end) {
    	if (end.getXPosition() == 0 || end.getXPosition() == 7) {
    		try {
    			String pieceColour = start.getPieceOnSquare().getColour();
    			char FEN = pieceColour.equals("White") ? 'Q' : 'q';
    			return new Queen(new ImageIcon(ImageIO.read(new File("queen_" + pieceColour.toLowerCase() + ".png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), pieceColour, FEN);
    		}
    		catch (IOException e) {
    		}
    	}
    	return start.getPieceOnSquare(); // If can't promote just return piece
    }
    
    public void setFirstMoveMade() {
    	firstMoveMade = true;
    }
    
    public boolean canMoveTwo() {
    	return !firstMoveMade;
    }
}
