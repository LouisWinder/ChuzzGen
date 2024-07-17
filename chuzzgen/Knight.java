package chuzzgen;

import javax.swing.*;

public class Knight extends Piece {
    public Knight(ImageIcon pieceImage, String colour, char FEN) {
    	super(pieceImage, colour, true, FEN);
    }
    
    @Override
    public boolean canMove(Square start, Square end) {
    	// Implement taking
    	if (end.isPieceOnSquare()) {
    		if (canCapture(start, end)) {
    			capture(end);
    		}
    		else {
    		    return false;
    		}
    	}
    	
    	return (Math.abs(start.getXPosition() - end.getXPosition())) * (Math.abs(start.getYPosition() - end.getYPosition())) == 2;
    }

	@Override
	public boolean canCapture(Square start, Square end) {
		// Check take is valid
		return end.getPieceOnSquare().getColour() != Game.currentPlayer.getColour();
	}
}
